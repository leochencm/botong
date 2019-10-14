package com.botongglcontroller.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheng on 2016/11/28.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    public static ArrayList<EventHandler> ehList = new ArrayList<EventHandler>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            Log.e("netWorkState", String.valueOf(netWorkState));
            Intent intent1 = new Intent("com.broadcast.refresh3");
            context.sendBroadcast(intent1);
            if (netWorkState == -1) {
                MyApplication.sp.setnetWorkState(String.valueOf(netWorkState));
            } else if (netWorkState == 0) {
                MyApplication.sp.setnetWorkState(String.valueOf(netWorkState));
            } else if (netWorkState == 1) {
                CheckNetWork(context);
                for (int i = 0; i < ehList.size(); i++) {
                    ((EventHandler) ehList.get(i)).scanResultsAvaiable();
                }
            }
        }
    }

    public void CheckNetWork(final Context context) {
        AjaxParams params = new AjaxParams();
        MyApplication.http.configTimeout(2000);
        MyApplication.http.post(Api.TestNetWork, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("网络状况", o.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        Log.i("netWorkState", String.valueOf(1));
                        MyApplication.sp.setnetWorkState(String.valueOf(1));
//                        Intent intent1 = new Intent("com.broadcast.refresh3");
//                        context.sendBroadcast(intent1);
                    } else {
                        //能连接无线但不能上网
                        MyApplication.sp.setnetWorkState(String.valueOf(2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                //能连接无线但不能上网
                Log.i("netWorkState", String.valueOf(2));
                MyApplication.sp.setnetWorkState(String.valueOf(2));
            }
        });

    }

    public static abstract interface EventHandler {
        public abstract void handleConnectChange();

        public abstract void scanResultsAvaiable();

        public abstract void wifiStatusNotification();
    }

}
