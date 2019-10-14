package com.botongglcontroller.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.Api;
import com.botongglcontroller.db.BoilerManager;
import com.botongglcontroller.db.WifiManager;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.dialog.AlertDialog;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class BindGuoluActivity extends BaseActivity {
    ImageView mBack;
    TextView txtmsg;
    TextView txtsid;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String[] msg1 = (String[]) msg.obj;
                    txtsid.setText("锅炉：" + msg1[0]);
                    txtmsg.setText(msg1[1]);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private BoilerManager mgr;
    private WifiManager mg;

    @Override
    public int getLayoutId() {
        return R.layout.bind_guolu;
    }

    @Override
    public void initView() {
        mgr = new BoilerManager(BindGuoluActivity.this);
        mg = new WifiManager(BindGuoluActivity.this);
        mBack = (ImageView) findViewById(R.id.img_back);
        txtmsg = (TextView) findViewById(R.id.txtmsg);
        txtsid = (TextView) findViewById(R.id.txt_sid);
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String wifiName = wifiInfo.getSSID().replace("\"", "");
        MyApplication.sp.setWifiName(wifiName);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        final String result = intent.getStringExtra("result");
        final String[] info = result.split("\\s+");
        final String wifiname = info[1];

        new AlertDialog(BindGuoluActivity.this).builder().setTitle("提示").setImagetitle(R.mipmap.alarm).setMsg("确定绑定锅炉" + info[0] + "？").setPositiveButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("account", MyApplication.sp.GetMobile());
                    ps.put("companyname", MyApplication.sp.GetCompanyname());
                    ps.put("serialnumber", info[0]);
                    ps.put("wifiname", wifiname);
                    Log.i("ps", ps.toString());
                    showLoadingDialog();
                    MyApplication.http.post(Api.BindBoiler, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            hideLoadingDialog();
                            super.onSuccess(o);
                            Log.e("绑定锅炉", o.toString());
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    // ToastUtil.showToast(BindGuoluActivity.this, "绑定锅炉成功");

                                    JSONObject ob = object.getJSONObject("data");
                                    List<Boilers> list = new ArrayList<Boilers>();
                                    Boilers boilers = new Boilers();
                                    boilers.setSerialnumber(ob.getString("serialnumber"));
                                    boilers.setModel(ob.getString("model"));
                                    boilers.setWorkstate(ob.getString("workstate"));
                                    boilers.setConnstate(ob.getString("connstate"));
                                    boilers.setImage(ob.getString("image"));
                                    Message msg = new Message();
                                    info[1] = "绑定锅炉成功";
                                    msg.obj = info;//message的内容
                                    msg.what = 1;//指定message
                                    handler.sendMessage(msg);//handler发送message

                                    // boilers.setShowenable(ob.getJSONArray("showenable").toString());
                                    list.add(boilers);
                                    mgr.add(list);
//保存锅炉WIFI
//                                    List<Boilers> li = new ArrayList<Boilers>();
//                                    Boilers data = new Boilers();
//                                    data.setSerialnumber(ob.getString("serialnumber"));
//                                    data.setWifiname(wifiname.substring(1,wifiname.length()-1));
//                                    li.add(data);
//                                    mg.add(li);
                                    Boilers boilers1 = new Boilers();
                                    boilers1.serialnumber = ob.getString("serialnumber");
                                    //Log.i("showenable", mgr.queryshown(boilers1)[1]);
                                    //  Bundle bundle = new Bundle();
                                    //   bundle.putString("serialnumber", info[0].toString());
                                    //   bundle.putString("name", info[1].toString());

                                    //  Intents.getIntents().Intent(BindGuoluActivity.this, GuolulistActivity.class, bundle);
                                    // finish();


                                } else {
                                    Message msg = new Message();
                                    info[1] = object.getString("message");
                                    msg.obj = info;//message的内容
                                    msg.what = 1;//指定message
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
                            ToastUtil.showToast(BindGuoluActivity.this, "当前没有网络！");
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }).show();
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                Intents.getIntents().Intent(BindGuoluActivity.this, GuolulistActivity.class);
                finish();
                break;

        }
    }
}
