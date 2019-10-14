package com.botongglcontroller.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.util.Log;

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by cheng on 2016/11/28.
 */
public class NetUtil {

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {

        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    public static String getSSID(Context context) {
        android.net.wifi.WifiManager mWifiManager;
        WifiInfo mWifiInfo;// 定义WifiInfo对象
        mWifiManager = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();


        return mWifiInfo.getSSID();
    }

    //WIFI IP
    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

    public static void CheckNetWork(final Context context) {
        AjaxParams params = new AjaxParams();
        MyApplication.http.configTimeout(3000);
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
                    } else {
                        //能连接无线但不能上网
                        Intent intent1 = new Intent("com.broadcast.wifi");
                        intent1.putExtra("wifistate", 2);
                        context.sendBroadcast(intent1);
                        Log.i("netWorkState", String.valueOf(2));
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
                Intent intent1 = new Intent("com.broadcast.wifi");
                intent1.putExtra("wifistate", 2);
                context.sendBroadcast(intent1);
                Log.i("netWorkState", String.valueOf(2));
                MyApplication.sp.setnetWorkState(String.valueOf(2));
            }
        });

    }
}
