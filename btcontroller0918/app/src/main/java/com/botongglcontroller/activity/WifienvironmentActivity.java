package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.Api;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class WifienvironmentActivity extends BaseActivity {

    ImageView mBack;
    Button mComfirm;
    EditText mWifiname, mWifipwd;
    private Devices devices;
    SocketMessage socketMessage;
    boolean isconnect = false;
    TextView mSetting;
    CheckBox mBox;
    Boolean isreceived = false;
    int isrefesh = 0;
    String serialnumber;

    @Override
    public int getLayoutId() {
        return R.layout.wifi_environment;
    }

    @Override
    public void initView() {
        mComfirm = (Button) findViewById(R.id.btn_confirm);
        mWifiname = (EditText) findViewById(R.id.edt_wifiname);
        mWifipwd = (EditText) findViewById(R.id.edt_wifipwd);
        mBack = (ImageView) findViewById(R.id.img_back);
        mSetting = (TextView) findViewById(R.id.txt_setting);
        mBox = (CheckBox) findViewById(R.id.cb);
        String wifiName = MyApplication.sp.getWifiName();
        String wifiPass = MyApplication.sp.getWifiPassword();
        if(wifiName != null && !wifiName.equals(""))
        {
            mWifiname.setText(wifiName);
        }
        if(wifiPass != null && !wifiPass.equals(""))
        {
            mWifipwd.setText(wifiPass);
        }
    }

    @Override
    public void initData() {
//        serialnumber = getIntent().getStringExtra("serialnumber");

        socketMessage = new SocketService().getSocketMessage();
        devices = new Devices(new SocketService().getSocketMessage());
        IntentFilter filter = new IntentFilter("com.broadcast.saveNetwork");
        registerReceiver(saveNetwork, filter);
        IntentFilter filter2 = new IntentFilter("com.broadcast.refresh2");
        registerReceiver(refresh2, filter2);
    }

    BroadcastReceiver saveNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("state", 1) == 3) {
                isreceived = true;
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }
    };
    BroadcastReceiver refresh2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isrefesh ++;
            Log.e("isrefeshb",""+isrefesh);
            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);
        }
    };

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mComfirm.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int passwordLength = mWifipwd.getText().length();
                mWifipwd.setInputType(isChecked ?
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) :
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                mWifipwd.setSelection(passwordLength);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_setting:
                Intent intent = new Intent(WifienvironmentActivity.this, WifiListActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_confirm:
                if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                ToastUtil.showToast(WifienvironmentActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                return;
            }
                if (mWifiname.getText().equals("") || TextUtils.isEmpty(mWifiname.getText())) {
                    ToastUtil.showToast(WifienvironmentActivity.this, "您还未填写WIFI名称");
                    return;
                }
                if (mWifipwd.getText().equals("") || TextUtils.isEmpty(mWifipwd.getText())) {
                    ToastUtil.showToast(WifienvironmentActivity.this, "您还未填写WIFI密码");
                    return;
                }
                if (mWifipwd.getText().length() < 8) {
                    ToastUtil.showToast(WifienvironmentActivity.this, "WIFI密码必须大于8位");
                    return;
                }
                devices.saveNetwork(mWifiname.getText().toString(), mWifipwd.getText().toString());
                isreceived = false;
                isrefesh=0;
                showLoadingDialog();
                Message message = new Message();
                message.what = 1;
                handler.sendMessageDelayed(message, 5000);
//                Timer timer = new Timer();
//                TimerTask tt = new TimerTask() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);// 发送消息
//                    }
//                };
//                timer.schedule(tt, 5000);
                MyApplication.sp.setWifiName(mWifiname.getText().toString());
                MyApplication.sp.setWifiPassword(mWifipwd.getText().toString());
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                if (!(progressDialg == null)) {
                    hideLoadingDialog();
                    if (!isreceived) {
                        LemonHello.getErrorHello("提示", "发送失败,请连接锅炉重新配置！")
                                .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                                    @Override
                                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                        helloView.hide();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toIntent();
                                            }
                                        }, 350L);
                                    }
                                }))
                                .show(WifienvironmentActivity.this);
//                        LemonBubble.showError(WifienvironmentActivity.this, "发送失败", 2000);
                    }
                }
            } else if (msg.what == 2) {
                hideLoadingDialog();
                Message message = new Message();
                message.what = 3;
                handler.sendMessageDelayed(message, 800);
            } else if (msg.what == 3) {
                showStringLoadingDialog("正在配置中...");
                Message message = new Message();
                message.what = 4;
                handler.sendMessageDelayed(message, 20000);
            } else if (msg.what == 4) {
                hideStringLoadingDialog();
                Log.e("isrefesh4",""+isrefesh);
                if (isrefesh==1) {
                    Log.e("配置成功","配置成功");
                    LemonHello.getSuccessHello("提示", "配置成功！")
                            .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toIntent();
                                        }
                                    }, 350L);

                                }
                            }))
                            .show(WifienvironmentActivity.this);
                } else if (isrefesh==0){
                    Log.e("getISonline","getISonline");
                    getISonline();
                }
                isrefesh++;
            }
            super.handleMessage(msg);
        }
    };

    private void toIntent() {

        if (MyApplication.sp.GetIdentity().equals("user")) {
            Intent intent = new Intent(WifienvironmentActivity.this, GuolulistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            OperatingconditionsActivity.instance.finish();
//            SettingcanshuActivity.instance.finish();
//            GuolulistActivity.instance.finish();
            finish();
        } else {
            Intent intent1 = new Intent(WifienvironmentActivity.this, HomeActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据上面发送过去的请求吗来区别
        String wifiname = "";

        switch (requestCode) {
            case 0:

                break;
            case 1:
                if (data.getStringExtra("wifi") != null) {
                    wifiname = data.getStringExtra("wifi");
                }
                mWifiname.setText(wifiname);
                break;

        }
    }

    private void getISonline() {
        showLoadingDialog();
        AjaxParams params = new AjaxParams();
        try {
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("serialnumber", MyApplication.sp.GetSerialnumber());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("param222s",""+params);
        MyApplication.http.post(Api.IsOnline, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.e("getISonline",o.toString());
                hideLoadingDialog();
                try {
                    JSONObject  object=new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        LemonHello.getSuccessHello("提示", "配置成功！")
                                .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                                    @Override
                                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                        helloView.hide();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toIntent();
                                            }
                                        }, 350L);
                                    }
                                }))
                                .show(WifienvironmentActivity.this);
                    }else{
                        LemonHello.getErrorHello("提示", "配置失败,请连接锅炉重新配置！")
                                .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                                    @Override
                                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                        helloView.hide();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toIntent();
                                            }
                                        }, 350L);
                                    }
                                }))
                                .show(WifienvironmentActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                hideLoadingDialog();
                        LemonHello.getErrorHello("警告", "手机无网络，请已锅炉实际连接为准")
                .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toIntent();
                            }
                        }, 350L);
                    }
                }))
                .show(WifienvironmentActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(saveNetwork);
        unregisterReceiver(refresh2);
    }


}
