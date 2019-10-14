package com.botongglcontroller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.botongglcontroller.utils.FinalHttpLog;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.SharedPreferencesUtil;
import com.botongglcontroller.utils.UserHelp;
import com.chinamobile.iot.onenet.http.Config;
import com.chinamobile.iot.onenet.OneNetApi;
import com.wby.EEApplication;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import cn.jpush.android.api.JPushInterface;


/**
 * Created by hasee on 2016/12/19.
 */

public class MyApplication extends EEApplication {
    public static SharedPreferencesUtil sp;
    public static Boolean isconnect = false;
    private static MyApplication mAppApplication;
    private static Context context;
    private List<Activity> oList;//用于存放所有启动的Activity的集合

    public static Context getContext() {
        return context;
    }

    /**
     * 获取Application
     */
    public static MyApplication getMyApplication() {
        return mAppApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        oList = new ArrayList<Activity>();
        mAppApplication = this;
        sp = new SharedPreferencesUtil(this);
        http = new FinalHttpLog();
        http.configUserAgent("baseAndroid/1.0");
        http.configTimeout(10000);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //JPushInterface.setAlias(this,1,"13852744365");
        Config config = Config.newBuilder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
                .retryCount(2)
                .build();
        OneNetApi.init(this, true, config);

        String savedApiKey = Api.Apikey;
        if (!TextUtils.isEmpty(savedApiKey)) {
            OneNetApi.setAppKey(savedApiKey);
        }

        context = getApplicationContext();

        SetScreatMsg();


    }

    private void SetScreatMsg() {
        AjaxParams params = new AjaxParams();
        try {
            params.put("time", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode("mantoo") + UserHelp.dateToStamp(UserHelp.getPosttime())));
            Log.i("params", params.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyApplication.http.post(Api.GetScreatMsg, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("SSSSSSSSSSSSS", o.toString());
                try {
                    JSONObject object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        JSONObject ob = object.getJSONObject("data");
                        sp.setScreatMsg(ob.getString("screatname"));
//                        getBoilersName();
                        Log.i("screeb", ob.getString("screatname"));
                    }
                } catch (JSONException e) {
                }
            }
        });
    }


    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }


}
