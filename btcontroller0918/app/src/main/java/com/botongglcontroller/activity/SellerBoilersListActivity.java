package com.botongglcontroller.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.adapter.AllotBoilersAdapter;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.layout.SwipeLayoutManager;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SellerBoilersListActivity extends BaseActivity {
    public List<String> boilersname;
    ImageView mBack;
    AllotBoilersAdapter myAdapter;
    ListView myListView;
    //private Set<SlideLayout> sets=new HashSet();
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://获取成功
                    myAdapter = new AllotBoilersAdapter(SellerBoilersListActivity.this, boilersname, this);
                    myListView.setAdapter(myAdapter);
                case 2://通信失败
                    hideLoadingDialog();
                    ToastUtil.showToast(SellerBoilersListActivity.this, msg.obj.toString());
                    break;
                case 3:
                    deleteboiler(boilersname.get(msg.arg1));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_setseller;
    }

    private void deleteboiler(final String s) {
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("sid", s);
            Log.i("ps", ps.toString());
            showLoadingDialog();
            MyApplication.http.post(Api.RESETALLOTSELLER, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.e("重置分配锅炉", o.toString());
                    Message msg = new Message();
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        msg.obj = object.getString("message");//message的内容
                        if (object.getString("resultcode").equals("0")) {
                            boilersname.remove(s);
                            msg.what = 1;//指定message
                        } else {
                            msg.what = 2;//指定message
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg.obj = "通信失败";
                        msg.what = 2;//指定message
                    }
                    handler.sendMessage(msg);//handler发送message
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
    public void initView() {
        mBack = findViewById(R.id.img_back);
        myListView = findViewById(R.id.mylist);
        boilersname = new ArrayList<>();
        //  for (int i = 0; i < 10; i++) {
        //    boilersname.add("test" + i);
        // }
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

        final String acc = intent.getStringExtra("parent");
        final String companyname = intent.getStringExtra("sellername");
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", acc);
            ps.put("name", companyname);
            Log.i("ps", ps.toString());
            showLoadingDialog();
            MyApplication.http.post(Api.SEARCHALLOTBOILER, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.e("分配锅炉列表", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        Message msg = new Message();
                        msg.obj = object.getString("message");//message的内容
                        if (object.getString("resultcode").equals("0")) {
                            // ToastUtil.showToast(BindGuoluActivity.this, "绑定锅炉成功");


                            JSONArray oba = object.getJSONArray("data");
                            for (int i = 0; i < oba.length(); i++) {
                                JSONObject ob = oba.getJSONObject(i);
                                boilersname.add(ob.getString("serialnumber"));
                            }
                            msg.what = 1;//指定message
                        } else {
                            msg.what = 2;//指定message
                        }
                        handler.sendMessage(msg);//handler发送message
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
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


