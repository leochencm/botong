package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.db.WifiManager;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.botongglcontroller.wifi.WIFIContron;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

public class GuolulinkActivity extends BaseActivity {
    ImageView mBack;
    private WIFIContron wifiContron;
    private SocketMessage socketMessage;
    private Devices devices;
    private final String TAG = getClass().getName();
    private WifiManager mgr;
    boolean isconnect = false;
    String serialnumber;
    TextView linkwifi, number, wifiname;
    Button btn_confirm;
    String s;

    @Override
    public int getLayoutId() {
        return R.layout.guolu_link;
    }

    @Override
    public void initView() {
        mgr = new WifiManager(GuolulinkActivity.this);
        linkwifi = (TextView) findViewById(R.id.linkwifi);
        number = (TextView) findViewById(R.id.number);
        wifiname = (TextView) findViewById(R.id.wifiname);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
//        IntentFilter filter = new IntentFilter("com.broadcast.wifi");
//        registerReceiver(broadcastReceiver, filter);
        mBack = (ImageView) findViewById(R.id.img_back);
    }

    @Override
    public void initData() {
        wifiContron = new WIFIContron(GuolulinkActivity.this);
        final Intent intent = getIntent();
        s = intent.getStringExtra("name");
        serialnumber = intent.getStringExtra("serialnumber");
        number.setText(serialnumber);
        wifiname.setText(s);
        linkWifi();
    }

    private void linkWifi() {
        LemonHello.getInformationHello("链接WIFI：" + s, "是否前往设置WIFI?")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("确定", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        Intent intent = new Intent();
                        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                        startActivityForResult(intent, 1);
                        helloView.hide();
                    }

                }
                ))
                .show(GuolulinkActivity.this);
    }

    private void back() {
        if(MyApplication.sp.GetIdentity().equals("user")){
            finish();
        }else{
        LemonHello.getInformationHello("退出之后该锅炉记录将不存在，", "是否退出？")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("确定", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 320L);

                    }
                }
                ))
                .show(GuolulinkActivity.this);}
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                if (!(progressDialg == null)) {
                    hideLoadingDialog();
//
                }
            } else if (msg.what == 2) {
                hideLoadingDialog();
            }
            super.handleMessage(msg);
        }
    };

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean _run = true;
            while (_run) {
                try {
                    Thread.sleep(20000);// 线程暂停10秒，单位毫秒
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

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        linkwifi.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                back();
                break;
            case R.id.btn_confirm:
                if (!MyApplication.sp.GetNetWorkState().equals("2")) {
                    showStringToastMsg("请检查网络或等待网络稳点!");
                    return;
                }
                if (MyApplication.sp.GetIdentity().equals("user")) {
                    Intent intent1 = new Intent(GuolulinkActivity.this, OperatingconditionsActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("id", "2");
                    intent1.putExtra("serialnumber", serialnumber);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent2 = new Intent(GuolulinkActivity.this, SettingcanshuActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("id", "1");
                    intent2.putExtra("serialnumber", serialnumber);
                    startActivity(intent2);
                }
                break;
            case R.id.linkwifi:
                Intent intent = new Intent();
                intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                startActivityForResult(intent, 1);
                break;

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra("wifistate", 1);
            isconnect = true;
            hideLoadingDialog();
            if (num == 2) {
                if (MyApplication.sp.GetIdentity().equals("user")) {
                    Intent intent1 = new Intent(GuolulinkActivity.this, OperatingconditionsActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("id", "2");
                    intent1.putExtra("serialnumber", serialnumber);
                    context.startActivity(intent1);
                    finish();
                } else {
                    Intent intent2 = new Intent(GuolulinkActivity.this, SettingcanshuActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.putExtra("id", "1");
                    intent2.putExtra("serialnumber", serialnumber);
                    context.startActivity(intent2);
                    finish();
                }
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (MyApplication.sp.GetIdentity().equals("user")) {
//            Intent intent1 = new Intent(GuolulinkActivity.this, OperatingconditionsActivity.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.putExtra("id", "2");
//            intent1.putExtra("serialnumber", serialnumber);
//            startActivity(intent1);
//            finish();
//        } else {
//            Intent intent2 = new Intent(GuolulinkActivity.this, SettingcanshuActivity.class);
//            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent2.putExtra("id", "1");
//            intent2.putExtra("serialnumber", serialnumber);
//            startActivity(intent2);
////            finish();
//        }


    }
}
