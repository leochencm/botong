package com.botongglcontroller.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.botongglcontroller.BroadCastManager;
import com.botongglcontroller.activity.OperatingconditionsActivity;
import com.botongglcontroller.adapter.NewsAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.db.BoilerManager;
import com.botongglcontroller.db.DBManager;
import com.botongglcontroller.db.NewsData;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.db.paraManager;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.activity.NewsItemActivity;
import com.botongglcontroller.activity.NewsSelectActivity;
import com.botongglcontroller.beans.Boilers;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.btcontroller.MyApplication.list;

/**
 * Created by hasee on 2016/12/17.
 */

public class NewsFragment extends Fragment {
    ListView mNews;
    NewsAdapter mAdapter;
    private DBManager mgr;
    private TextView mNonews;
    private paraManager mpara;
    private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
    BoilerManager mg;
    TextView clear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savdInstanceState) {
        mgr = new DBManager(getContext());
        mg = new BoilerManager(getContext());
        mpara= new paraManager(getContext());
        View root = inflater.inflate(R.layout.news_list, container, false);
        mNews = (ListView) root.findViewById(R.id.lv_news);
        mNonews = (TextView) root.findViewById(R.id.tv_nonews);
        clear = (TextView) root.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LemonHello.getWarningHello("警告！", "您确认清空消息么？")
                        .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("确定删除", Color.RED, new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                                // 提示框使用了LemonBubble，请您参考：https://github.com/1em0nsOft/LemonBubble4Android
                                LemonBubble.showRoundProgress(getActivity(), "正在删除中...");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mgr.clearnews();
                                        LemonBubble.showRight(getActivity(), "删除成功", 1000);
                                        MyApplication.sp.setUnreadNumber(0);
                                        query();
                                    }
                                }, 2000);
                            }
                        }))
                        .show(getActivity());

            }
        });

        //点击消息
        mNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //如果消息已读，不能查看详情
                final Map<String, String> map = list.get(i);
                if(map.get("isRead").equals("1"))
                {
                    return;
                }

                int num = MyApplication.sp.GetUnreadNumber();
                Intent intent1 = new Intent(
                        "data.broadcast.unreadnumber");
                intent1.putExtra("num", --num);
                MyApplication.sp.setUnreadNumber(num);
                getActivity().sendBroadcast(intent1);

                //修改状态
                if(!map.get("type").equals("6"))
                {
                    // 更新消息已读状态
                    try {
                        AjaxParams ps = new AjaxParams();
                        ps.put("screatname", MyApplication.sp.GetScreatMsg());
                        ps.put("screatword", UserHelp.getPosttime());
                        ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                        ps.put("Id", map.get("id"));
                        MyApplication.http.post(Api.ReadInform, ps, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                try {
                                    JSONObject object = new JSONObject(o.toString());
                                    if (object.getString("resultcode").equals("0")) {
                                        // 成功更新服务器后，再更新本地状态
                                        ArrayList<NewsData> persons = new ArrayList<NewsData>();
                                        NewsData person1 = new NewsData(map.get("id"), map.get("UID"),
                                                map.get("title"), map.get("msg"), map.get("date"),
                                                map.get("type"), "1");
                                        persons.add(person1);
                                        mgr.add(persons);
                                    } else {
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onFailure(Throwable t, String strMsg) {
                                super.onFailure(t, strMsg);
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Intent intent = new Intent(getActivity(), NewsSelectActivity.class);
                    intent.putExtra("id", list.get(i).get("id"));
                    intent.putExtra("UID", list.get(i).get("UID"));
                    intent.putExtra("title", list.get(i).get("title"));
                    intent.putExtra("msg", list.get(i).get("msg"));
                    intent.putExtra("date", list.get(i).get("date"));
                    intent.putExtra("type", list.get(i).get("type"));
                    startActivity(intent);
                    return;
                }


                //1是报警2是系统消息
                if (list.get(i).get("type").equals("1")) {
                   // Boilers boiler = new Boilers();
                   Boilers boiler=mg.querymodel(list.get(i).get("UID"));
                    if(boiler.serialnumber.equals("none"))
                    {
                        Intent intent=new Intent("com.broadcast.newsmsg");
                        intent.putExtra("msg",1);
                        BroadCastManager.getInstance().sendBroadCast(getActivity(), intent);
                        Intent it = new Intent(getActivity(), GuolulistActivity.class);
                        startActivity(it);
                    }
                    else if(boiler.connstate.equals("0"))
                    {
                        Intent intent=new Intent("com.broadcast.newsmsg");
                        intent.putExtra("msg",2);
                        BroadCastManager.getInstance().sendBroadCast(getActivity(), intent);
                        Intent it = new Intent(getActivity(), GuolulistActivity.class);
                        startActivity(it);
                    }
                    else
                    {


                    //list1=new ArrayList<BoilersPara>();
                        ArrayList<BoilersPara> list1=mpara.queryshown(boiler.model);

//                    if(mg.queryshown(boiler).length==0||mg.queryshown(boiler)==null){
//                        LemonBubble.showError(getContext(), "该锅炉未绑定，请绑定锅炉！", 2000);
//                        return;
//                    }
                   // Log.i("show",""+  list.get(i).get("UID"));
                   // Log.i("show",""+ mg.queryshown(boiler)[0]);
                  //  if (!mg.queryshown(boiler).equals("")||!(mg.queryshown(boiler).length==0)) {
                  //      String showenable = mg.queryshown(boiler)[1];
                    Intent intent = new Intent(getActivity(), OperatingconditionsActivity.class);
                    intent.putExtra("id", "3");
                    Bundle bd=new Bundle();
                    bd.putSerializable("boiler",list1);
                    intent.putExtras(bd);
                    intent.putExtra("serialnumber", boiler.serialnumber);
                    //intent.putExtra("showenable", list.get(i).getShowenable());
                    intent.putExtra("oid",boiler.oid);
                    intent.putExtra("apikey",boiler.apikey);
                    intent.putExtra("connstate",boiler.connstate);
                    intent.putExtra("model",boiler.model);
                    intent.putExtra("image",boiler.image);
//                    startActivityForResult(intent, 0);
                    startActivity(intent);

                    }
                } else if (list.get(i).get("type").equals("2")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", list.get(i).get("title"));
                    bundle.putString("msg", list.get(i).get("msg"));
                    Intents.getIntents().Intent(getActivity(), NewsItemActivity.class, bundle);
                }

            }
        });

        return root;
    }

    public void query() {

        // 请求求助列表
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("Account", MyApplication.sp.GetAccountName());

            MyApplication.http.post(Api.GetInform, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("消息列表", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            list.clear();
                            JSONArray array = object.getJSONArray("data");
                            for(int i=0; i<array.length(); i++)
                            {
                                JSONObject obj = array.getJSONObject(i);
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("UID", obj.getString("serialnumber"));
                                map.put("id", obj.getString("id"));
                                if(obj.getString("messagetype").equals("1"))
                                {
                                    map.put("msg", obj.getString("message"));
                                    map.put("date", obj.getString("time"));
                                    map.put("title", "报警消息");
                                }
                                else
                                {
                                    map.put("msg", obj.getString("message1"));
                                    map.put("date", obj.getString("submittime"));
                                    map.put("title", "通知消息");
                                }

                                map.put("type", obj.getString("messagetype"));
                                map.put("isRead", "0");
                                list.add(map);
                            }

                            Collections.reverse(list);

                            // 本地已读消息
                            if (!mgr.query().isEmpty()) {
                                List<NewsData> persons = mgr.query();
                                for (NewsData person : persons) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("UID", person.UID);
                                    map.put("id", person.id);
                                    map.put("title", person.title);
                                    map.put("msg", person.msg);
                                    map.put("date", person.date);
                                    map.put("type", person.type);
                                    map.put("isRead", person.isRead);
                                    list.add(map);
                                }

                                if(list.size() > 0)
                                {
                                    mNonews.setVisibility(View.GONE);
                                    mNews.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    mNonews.setVisibility(View.VISIBLE);
                                    mNews.setVisibility(View.GONE);
                                }
                            }

                            mAdapter = new NewsAdapter(getContext(), list);
                            mNews.setAdapter(mAdapter);

                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                }
            });


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        query(); //查询消息列表
    }
}
