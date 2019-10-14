package com.botongglcontroller.Fragment;

import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.activity.BaseActivity;
import com.botongglcontroller.activity.BindGuoluActivity;
import com.botongglcontroller.activity.GuolulinkActivity;
import com.botongglcontroller.activity.MainActivity;
import com.botongglcontroller.activity.MipcaActivityCapture;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.view.MyImageView;
import com.readystatesoftware.viewbadger.BadgeView;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class HomemainActivity extends Fragment {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    MyImageView mSuggestion, mPersonal;
    //    Button mGuolu;
    MyImageView mScan, mNews;
    BadgeView bv, bv1;
    MyImageView mGuolu;
    Bundle bundle;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra("num", 0);
            Log.i("获得广播", String.valueOf(num));
            bv.setText(String.valueOf(num));
            //initView();
        }
    };
    private long touchTime = 0;
    private int missionnos; //个人中心

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.homemain, null);
        bundle = new Bundle();
        mGuolu = (MyImageView) rootview.findViewById(R.id.ib_golu);
        mNews = (MyImageView) rootview.findViewById(R.id.ib_news);
        mSuggestion = (MyImageView) rootview.findViewById(R.id.ib_suggestion);
        mPersonal = (MyImageView) rootview.findViewById(R.id.ib_personal);
        mScan = (MyImageView) rootview.findViewById(R.id.img_scan);
        bv = new BadgeView(getActivity(), mNews);
        bv1 = new BadgeView(getActivity(), mPersonal);
        IntentFilter filter = new IntentFilter("com.broadcast.set.broadcast");
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, filter);

        mGuolu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.getIntents().Intent(getActivity(), GuolulistActivity.class);

                //getActivity().finish();
            }
        });

        mNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("news", 2);
                Intents.getIntents().Intent(getActivity(), MainActivity.class, bundle);
                //getActivity().finish();
            }
        });

        mPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("personal", 4);
                Intents.getIntents().Intent(getActivity(), MainActivity.class, bundle);
                //finish();
            }
        });

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });

        mSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("suggestion", 3);
                Intents.getIntents().Intent(getActivity(), MainActivity.class, bundle);
                //finish();
            }
        });

        // 获取消息数量
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
        ps.put("identity", MyApplication.sp.GetIdentity());
        MyApplication.http.post(Api.GetInformNum, ps, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        int news = Integer.parseInt(object.getString("messageno"));
                        MyApplication.sp.setUnreadNumber(news);
                        missionnos = Integer.parseInt(object.getString("missionno"));

                        //设置消息小红点
                        getBadgeView();
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
        // initListener();
        initData();
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getBadgeView() {
        //消息列表
        int s = MyApplication.sp.GetUnreadNumber();
        if (s == 0 || s < 0) {
            bv.setVisibility(View.GONE);
        } else {
            bv.setVisibility(View.VISIBLE);
            bv.setText(String.valueOf(s));
            bv.setTextColor(Color.BLACK);
            bv.setTextSize(18);
            bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认值
            bv.show();
        }

        // 个人中心
        if (missionnos == 0 || missionnos < 0) {
            bv1.setVisibility(View.GONE);
        } else {
            bv1.setVisibility(View.VISIBLE);
            bv1.setText(String.valueOf(missionnos));
            bv1.setTextColor(Color.BLACK);
            bv1.setTextSize(18);
            bv1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认值
            bv1.show();
        }
    }

    public void initData() {

    }

    public void initListener() {
        mGuolu.setOnClickListener((View.OnClickListener) this);
        mNews.setOnClickListener((View.OnClickListener) this);
        mSuggestion.setOnClickListener((View.OnClickListener) this);
        mPersonal.setOnClickListener((View.OnClickListener) this);
        mScan.setOnClickListener((View.OnClickListener) this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
    }

    private void VerifyConn(final String serialnumber, final String name) {
        //showLoadingDialog();
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("serialnumber", serialnumber);
            ps.put("account", MyApplication.sp.GetMobile());
            MyApplication.http.post(Api.VerifyConn, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    // hideLoadingDialog();
                    Log.i("验证", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            bundle.putString("serialnumber", serialnumber);
                            bundle.putString("name", name);
                            //Intents.getIntents().Intent(HomemainActivity.this, GuolulinkActivity.class, bundle);
                        } else {
                            LemonBubble.showError(HomemainActivity.this, object.getString("message"), 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    // hideLoadingDialog();
                    ToastUtil.showToast(getContext(), "扫描失败，请检查网络！");
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
