package com.botongglcontroller.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.botongglcontroller.Api;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.adapter.AllotBoilersAdapter;
import com.botongglcontroller.adapter.SellerBoilersAdapter;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.beans.SellerBoilers;
import com.botongglcontroller.db.BoilerManager;
import com.botongglcontroller.db.WifiManager;
import com.botongglcontroller.dialog.AlertDialog;
import com.botongglcontroller.layout.SlideLayout;
import com.botongglcontroller.layout.SwipeLayoutManager;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetSellerActivity extends BaseActivity {
    ImageView mBack;
    public List<String> boilersname;
    AllotBoilersAdapter myAdapter;
    ListView myListView;
    //private Set<SlideLayout> sets=new HashSet();

    @Override
    public int getLayoutId() {
        return R.layout.activity_setseller;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    myAdapter = new AllotBoilersAdapter(SetSellerActivity.this, boilersname, this);
                    myListView.setAdapter(myAdapter);
                case 2:
                    hideLoadingDialog();
                    ToastUtil.showToast(SetSellerActivity.this, msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void initView() {
        mBack = findViewById(R.id.img_back);
        myListView = findViewById(R.id.mylist);
        boilersname = new ArrayList<>();
        myAdapter = new AllotBoilersAdapter(this, boilersname, handler);
        myListView.setAdapter(myAdapter);
        myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        final String result = intent.getStringExtra("result");
        final String[] info = result.split("\\s+");
        final String acc = intent.getStringExtra("account");
        final String companyname = intent.getStringExtra("companyname");
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", acc);
            ps.put("sid", info[0]);
            ps.put("name", companyname);

            Log.i("ps", ps.toString());
            showLoadingDialog();
            MyApplication.http.post(Api.SENDALLOTBOILER, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    hideLoadingDialog();
                    super.onSuccess(o);
                    Log.e("分配锅炉", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            // ToastUtil.showToast(BindGuoluActivity.this, "绑定锅炉成功");

                            //JSONObject ob = object.getJSONObject("data");
                            Message msg = new Message();
                            msg.obj = object.getString("message");//message的内容
                            msg.what = 1;//指定message
                            handler.sendMessage(msg);//handler发送message

                            // boilers.setShowenable(ob.getJSONArray("showenable").toString());
                            boilersname.add(info[0]);

//保存锅炉WIFI
//                                    List<Boilers> li = new ArrayList<Boilers>();
//                                    Boilers data = new Boilers();
//                                    data.setSerialnumber(ob.getString("serialnumber"));
//                                    data.setWifiname(wifiname.substring(1,wifiname.length()-1));
//                                    li.add(data);
//                                    mg.add(li);
                            // Boilers boilers1 = new Boilers();
                            //  boilers1.serialnumber = ob.getString("serialnumber");
                            //Log.i("showenable", mgr.queryshown(boilers1)[1]);
                            //  Bundle bundle = new Bundle();
                            //   bundle.putString("serialnumber", info[0].toString());
                            //   bundle.putString("name", info[1].toString());

                            //  Intents.getIntents().Intent(BindGuoluActivity.this, GuolulistActivity.class, bundle);
                            // finish();


                        } else {
                            Message msg = new Message();
                            //info[1] = object.getString("message");
                            msg.obj = object.getString("message");//message的内容
                            msg.what = 2;//指定message
                            handler.sendMessage(msg);//handler发送message
                        }
//                                if (object.getString("resultcode").equals("1")) {
//                                        // txtmsg.setText(object.getString("message"));
//                                        ToastUtil.showToast(BindGuoluActivity.this, object.getString("message"));
//                                    } else if (object.getString("resultcode").equals("2")) {
//                                        // txtmsg.setText(object.getString("message"));
//                                        ToastUtil.showToast(BindGuoluActivity.this, object.getString("message"));
//                                    } else if (object.getString("resultcode").equals("3")) {
//                                        //txtmsg.setText(object.getString("message"));
//                                        ToastUtil.showToast(BindGuoluActivity.this, object.getString("message"));
//                                    }
//                                }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideLoadingDialog();
                    //  ToastUtil.showToast(BindGuoluActivity.this, "当前没有网络！");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                //Intents.getIntents().Intent(SetSellerActivity.this, GuolulistActivity.class);
                finish();
                break;

        }
    }

}


