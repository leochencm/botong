package com.botongglcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.botongglcontroller.db.DBManager;
import com.botongglcontroller.db.NewsData;
import com.botongglcontroller.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private DBManager mgr;

    @Override
    public void onReceive(Context context, Intent intent) {
        mgr = new DBManager(context);

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Log.e("推送消息啦", "DDD" + bundle.getString(JPushInterface.EXTRA_EXTRA) + "DDD");
            try {
                JSONObject object = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                ArrayList<NewsData> persons = new ArrayList<NewsData>();
                String current = String.valueOf(System.currentTimeMillis());
//                JSONObject ob = new JSONObject(object.getString("message"));
//type 3运行工况刷新4.运行工况刷新5.锅炉列表刷新1,报警2.系统消息推送
                if (object.getString("type").equals("3")) {
                    Intent intent1 = new Intent("com.broadcast.refresh");
                    context.sendBroadcast(intent1);
                } else if (object.getString("type").equals("4")) {
                    Intent intent1 = new Intent("com.broadcast.refresh2");
                    context.sendBroadcast(intent1);
                } else if (object.getString("type").equals("5")) {
                    Intent intent1 = new Intent("com.broadcast.refresh3");
                    context.sendBroadcast(intent1);
                } else if (object.getString("type").equals("2")){
                    /*NewsData person1 = new NewsData(current, object.getString("UID"),
                            bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE),
                            bundle.getString(JPushInterface.EXTRA_ALERT),
                            object.getString("date"),
                            object.getString("type"),
                            object.getString("point"),
                            "0");
                    persons.add(person1);
                    mgr.add(persons);*/

                }else{
//                    NewsData person1 = new NewsData(current, object.getString("UID"),
//                            bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE),
//                            bundle.getString(JPushInterface.EXTRA_ALERT),
//                            object.getString("date"),
//                            object.getString("type"),
//                            object.getString("point"),
//                            "0");
//                    persons.add(person1);
//                    mgr.add(persons);
                    int num = MyApplication.sp.GetUnreadNumber();
                    Intent intent1 = new Intent("com.broadcast.set.broadcast");
                    intent1.putExtra("num", ++num);
                    context.sendBroadcast(intent1);
                    MyApplication.sp.setUnreadNumber(num);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("news", 2);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
    }


}
