package com.botongglcontroller.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.botongglcontroller.adapter.SuggestionAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.activity.PublishSuggestionActivity;
import com.botongglcontroller.configInfo.SuggestionInfo;
import com.botongglcontroller.view.MyListView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2016/12/17.
 * 修改日记：（2017年2月4日11：49）添加了下拉刷新上拉加载，
 * 修改人：mrj
 */

public class SuggestionFragment extends Fragment implements View.OnClickListener {
    private final String TAG = getClass().getName();
    private ImageView mAdd;
    private MyListView mylist;
    private List<SuggestionInfo> suggestionInfos;
    private SuggestionAdapter suggestionAdapter;
    private int number = 1;
    private boolean flag = true;
    /**
     * 下拉刷新上拉加载实现接口
     */
    private MyListView.OnRefreshListener onRefreshListener = new MyListView.OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            Log.i(TAG, "下拉刷新");
            if (suggestionInfos.size() > 0) {
                suggestionInfos.clear();
                suggestionAdapter.notifyDataSetChanged();
            }
            //重置请求
            number = 1;
            flag = true;
            sumberDataService(number);
            handler.postDelayed(runnable, 1300);
        }

        @Override
        public void onLoadingMore() {
            Log.i(TAG, "上拉加载");
            if (flag) {
                sumberDataService(number);
            } else {
                //隐藏底
                mylist.loadMoreComplete();
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mylist.onRefreshComplete();//头部消失
            //隐藏底
            mylist.loadMoreComplete();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 0x01:
                    //隐藏底
                    mylist.loadMoreComplete();
                    Object o = msg.obj;
                    try {
                        Log.d(TAG, o.toString());
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            JSONArray jsona = object.getJSONArray("data");
                            for (int i = 0; i < jsona.length(); i++) {
                                JSONObject ob = jsona.getJSONObject(i);
                                SuggestionInfo suggestionInfo = new SuggestionInfo();
                                suggestionInfo.account = ob.getString("account");
                                suggestionInfo.contact = ob.getString("contact");
                                suggestionInfo.submitdate = ob.getString("submitdate");
                                suggestionInfo.suggestion = ob.getString("suggestion");
                                suggestionInfos.add(suggestionInfo);
                                suggestionAdapter.notifyDataSetChanged();
                            }
                            if (jsona.length() >= 6) {
                                number++;
                                flag = true;
                            } else {
                                flag = false;
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.suggestion, container, false);
        initView(root);
        sumberDataService(number);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 请求数据
     */
    private void sumberDataService(final int number) {
        if (flag) {
            AjaxParams params = new AjaxParams();
            try {
                params.put("screatname", MyApplication.sp.GetScreatMsg());
                params.put("screatword", UserHelp.getPosttime());
                params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                params.put("num", "6");
                params.put("pages", number + "");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            Log.i(TAG, "参数：" + params.toString());
            MyApplication.http.post(Api.GetSuggestion, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Message msg = new Message();
                    msg.arg1 = 0x01;
                    msg.obj = o;
                    handler.sendMessage(msg);
                }
                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
//                    Log.e(TAG, strMsg);
                }
            });
        } else {
            Log.i(TAG, "flag->" + flag);
        }
    }

    private void initView(View view) {
        mAdd = (ImageView) view.findViewById(R.id.img_add);
        mylist = (MyListView) view.findViewById(R.id.mylist_suggestion);
        suggestionInfos = new ArrayList<SuggestionInfo>();
        suggestionAdapter = new SuggestionAdapter(suggestionInfos, view.getContext());

        mylist.setOnScrollListener(mylist);
        mylist.setOnRefreshListener(onRefreshListener);
        mylist.setAdapter(suggestionAdapter);

        mAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                Intents.getIntents().Intent(getActivity(), PublishSuggestionActivity.class);
                break;
        }
    }
}
