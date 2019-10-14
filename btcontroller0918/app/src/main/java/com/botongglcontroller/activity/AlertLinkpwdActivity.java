package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.db.WifiManager;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

public class AlertLinkpwdActivity extends BaseActivity {
    ImageView mBack;
    Button mConfirm;
    EditText  mWifipwd;
    TextView mWifiname;
    private Devices devices;
    WifiManager mg;
    SocketMessage socketMessage;
    boolean isconnect = false;
    CheckBox mBox;
    @Override
    public int getLayoutId() {
        return R.layout.alert_linkpwd;
    }

    @Override
    public void initView() {
        mg = new WifiManager(this);
        mBack = (ImageView) findViewById(R.id.img_back);
        mConfirm = (Button) findViewById(R.id.btn_confirm);
        mWifiname = (TextView) findViewById(R.id.edt_name);
        mWifipwd = (EditText) findViewById(R.id.edt_pwd);
        mBox= (CheckBox) findViewById(R.id.cb);
        devices = new Devices(new SocketService().getSocketMessage());
        IntentFilter filter = new IntentFilter("com.broadcast.settingNetworkPassword");
        registerReceiver(settingNetworkPassword, filter);
        socketMessage = new SocketService().getSocketMessage();
    }
    BroadcastReceiver settingNetworkPassword = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            ToastUtil.showToast(AlertLinkpwdActivity.this, "已获取广播settingNetworkPassword" + intent.getStringExtra("state"));
            isconnect=true;
            hideLoadingDialog();
            if(intent.getStringExtra("state").equals("成功")){
                LemonBubble.showRight(AlertLinkpwdActivity.this, "设置成功", 2000);
                Intent intent1=new Intent(AlertLinkpwdActivity.this, GuolulistActivity.class);
                startActivity(intent1);
                finish();
            }else if (intent.getStringExtra("state").equals("失败")){
                LemonBubble.showError(AlertLinkpwdActivity.this, "设置失败", 2000);
                Intent intent1=new Intent(AlertLinkpwdActivity.this, GuolulistActivity.class);
                startActivity(intent1);
                finish();
            }
        }
    };

    @Override
    public void initData() {
      String s=  MyApplication.sp.GetSerialnumber();
//        Boilers boilers = new Boilers();
//        boilers.serialnumber = MyApplication.sp.GetSerialnumber();
//        String[] str = mg.querytewifi(boilers);
//        if (!(str.length == 0)) {
//            mWifiname.setText(str[1]);
//        }else{
//            Log.e("空","空");
//        }
        mWifiname.setText("BT-"+s.substring(0,6));

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
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
            case R.id.btn_confirm:

                if (socketMessage == null|| !MyApplication.sp.GetNetWorkState().equals("2")) {
                    ToastUtil.showToast(AlertLinkpwdActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                    return;
                }

                if (mWifiname.getText().equals("")|| TextUtils.isEmpty(mWifiname.getText())) {
                    ToastUtil.showToast(AlertLinkpwdActivity.this, "您还未填写WIFI名称");
                    return;
                }
                if (mWifipwd.getText().equals("")|| TextUtils.isEmpty(mWifipwd.getText())) {
                    ToastUtil.showToast(AlertLinkpwdActivity.this, "您还未填写WIFI密码");
                    return;
                }
                if (mWifipwd.getText().length()<8){
                    ToastUtil.showToast(AlertLinkpwdActivity.this, "WIFI密码必须大于8位");
                    return;
                }

                devices.settingNetworkPassword(mWifiname.getText().toString(), mWifipwd.getText().toString());
                showLoadingDialog();
                new Thread(new MyThread()).start();
                break;

        }
    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(settingNetworkPassword);
        super.onDestroy();

    }
    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean _run = true;
            while (_run) {
                try {
                    Thread.sleep(6000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    if (!isconnect) {
                        message.what = 1;
                        handler.sendMessage(message);// 发送消息
                    } else {
                        message.what = 2;
                        handler.sendMessage(message);// 发送消息
                    }
                    _run = false;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
//            Boilers person3 = new Boilers();
//            person3.serialnumber =MyApplication.sp.GetSerialnumber();
//            person3.wifiname = mWifiname.getText().toString();
//            person3.wifipwd =  mWifipwd.getText().toString();
//            mg.updateTye(person3);
            if (msg.what == 1) {
                if (!(progressDialg == null)) {
                    hideLoadingDialog();
                    warning();
                }
            } else if (msg.what == 2) {
                hideLoadingDialog();
                warning();
            }
            super.handleMessage(msg);
        }
    };
    private void warning() {
        LemonHello.getErrorHello("警告", "与锅炉链接已断开，需要重新连接！")
                .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        if (MyApplication.sp.GetIdentity().equals("user")) {
                            Intent intent = new Intent(AlertLinkpwdActivity.this, GuolulistActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(AlertLinkpwdActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }))
                .show(AlertLinkpwdActivity.this);
    }
}
