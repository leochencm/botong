package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.botongglcontroller.Api;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.Fragment.HomemainActivity;
import com.botongglcontroller.Fragment.HomemanageActivity;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.adapter.FragmentAdapter;
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

public class HomeActivity extends BaseActivity {

    public Fragment fg;
    public ViewPager vpager;
    Bundle bundle;
    private long touchTime = 0;
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.home;
    }

    @Override
    public void initView() {


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
                        //missionnos = Integer.parseInt(object.getString("missionno"));

                        //设置消息小红点
                        // getBadgeView();
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        vpager = (ViewPager) findViewById(R.id.viewpager);
        vpager.setOffscreenPageLimit(2);
        vpager.setCurrentItem(0);
        HomemainActivity homemain = new HomemainActivity();
        HomemanageActivity homemanger = new HomemanageActivity();
        FragmentAdapter pageAdapter = new FragmentAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(homemain);
        // if (MyApplication.sp.getParentTel() != "") pageAdapter.addFragment(homemanger);
        vpager.setAdapter(pageAdapter);
        vpager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void initData() {

    }


    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - touchTime > 1500) {
                ToastUtil.showToast(HomeActivity.this, "再按一次退出应用");
                touchTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(broadcastReceiver);
    }

}
