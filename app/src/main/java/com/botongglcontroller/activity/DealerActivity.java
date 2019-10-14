package com.botongglcontroller.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.utils.DateTimePickerDialog;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.kyleduo.switchbutton.SwitchButton;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class DealerActivity extends BaseActivity {
    private final String TAG = getClass().getName();
    ImageView mBack;
    SwitchButton mButton;
    TextView txt;
    TextView date;
    boolean isconnect = false;
    String intention = "";//判断是哪个广播
    String time = "";
    BroadcastReceiver lockMachineDays = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            ToastUtil.showToast(DealerActivity.this, "已获取广播lockMachineDays" + intent.getStringExtra("state"));
            isconnect = true;
            hideLoadingDialog();
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "设置成功", 2000);
                date.setText(time);
                MyApplication.sp.setsocketData(date.getText().toString());
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "设置失败", 2000);
            }

        }
    };
    BroadcastReceiver foreverOpenAdmin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            ToastUtil.showToast(DealerActivity.this, "已获取广播foreverOpenAdmin" +
            isconnect = true;
            hideLoadingDialog();
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "设置成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "设置失败", 2000);
            }
        }
    };
    BroadcastReceiver lockMachine = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
            hideLoadingDialog();
//            ToastUtil.showToast(DealerActivity.this, "已获取广播lockMachine" + intent.getStringExtra("state"));
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "设置锁机成功", 2000);
                MyApplication.sp.setlockstate(mButton.isChecked() ? "1" : "0");

            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "设置锁机成功", 2000);
            }

        }
    };
    BroadcastReceiver lockMachinetime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
            hideLoadingDialog();
//            ToastUtil.showToast(DealerActivity.this, "已获取广播lockMachine" + intent.getStringExtra("state"));
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "设置成功", 2000);
//                MyApplication.sp.setlockstate(mButton.get);

            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "设置失败", 2000);
            }

        }
    };
    BroadcastReceiver timeInitialization = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
            hideLoadingDialog();
//            ToastUtil.showToast(DealerActivity.this, "已获取广播timeInitialization" + intent.getStringExtra("state"));
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "设置时间成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "设置时间失败", 2000);
            }
        }
    };
    BroadcastReceiver temperatureCalibration = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
            hideLoadingDialog();
//            ToastUtil.showToast(DealerActivity.this, "已获取广播timeInitialization" + intent.getStringExtra("state"));
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(DealerActivity.this, "标定温度成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(DealerActivity.this, "标定温度失败", 2000);
            }
        }
    };
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                if (intention.equals("lockMachine")) {
                    hideLoadingDialog();
                    LemonBubble.showError(DealerActivity.this, "链接超时", 1000);
                    mButton.setChecked(!mButton.isChecked());
                } else {
                    hideLoadingDialog();
                    LemonBubble.showError(DealerActivity.this, "链接超时", 1000);
                }

            } else if (msg.what == 2) {
                hideLoadingDialog();
            }
            super.handleMessage(msg);
        }
    };
    private SocketMessage socketMessage;
    private Devices devices;

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStrin(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(date);

        return dateString;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dealer;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        txt = (TextView) findViewById(R.id.txt);
        date = (TextView) findViewById(R.id.date);
        mButton = (SwitchButton) findViewById(R.id.btn_lock);
    }

    @Override
    public void initData() {
        socketMessage = new SocketService().getSocketMessage();
        devices = new Devices(new SocketService().getSocketMessage());
        IntentFilter filter = new IntentFilter("com.broadcast.lockMachineDays");
        registerReceiver(lockMachineDays, filter);
        IntentFilter filter2 = new IntentFilter("com.broadcast.foreverOpenAdmin");
        registerReceiver(foreverOpenAdmin, filter2);
        IntentFilter filter3 = new IntentFilter("com.broadcast.lockMachine");
        registerReceiver(lockMachine, filter3);
        IntentFilter filter6 = new IntentFilter("com.broadcast.lockMachinetime");
        registerReceiver(lockMachinetime, filter6);
        IntentFilter filter4 = new IntentFilter("com.broadcast.timeInitialization");
        registerReceiver(timeInitialization, filter4);
        IntentFilter filter5 = new IntentFilter("com.broadcast.temperatureCalibration");
        registerReceiver(temperatureCalibration, filter5);
        String state = MyApplication.sp.Getlockstate();
        if (state.equals("1")) {
            mButton.setChecked(true);
        } else if (state.equals("0")) {
            mButton.setChecked(false);
        }
        date.setText(MyApplication.sp.GetsocketData());
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        findViewById(R.id.rl_date).setOnClickListener(this);
        findViewById(R.id.pwd).setOnClickListener(this);
        findViewById(R.id.time).setOnClickListener(this);
//        findViewById(R.id.temperature2).setOnClickListener(this);
//        findViewById(R.id.temperature).setOnClickListener(this);
        mButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lock:

                if (mButton.isChecked()) {
                    if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                        ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                        mButton.setChecked(false);
                        return;
                    }
                    intention = "lockMachine";
                    showLoadingDialog();
                    devices.lockMachinetime(0x01);

                    new Thread(new MyThread()).start();
                } else {
                    if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                        ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                        mButton.setChecked(true);
                        return;
                    }
                    intention = "lockMachine";
                    showLoadingDialog();
                    devices.lockMachine(0x01);
                    new Thread(new MyThread()).start();
                }
                break;
            case R.id.img_back:
                finish();
                break;
//            case R.id.temperature:
//
//                // 载入xml文件的布局
//                LayoutInflater lf2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                ViewGroup vg2 = (ViewGroup) lf2.inflate(R.layout.edittext_alertdialog,
//                        null);
//                final EditText etShow2 = (EditText) vg2
//                        .findViewById(R.id.edt_msg);
//                etShow2.setHint("设置温度标定一");
//                etShow2.setInputType(InputType.TYPE_CLASS_NUMBER);
//                new AlertDialog.Builder(DealerActivity.this)
//                        .setView(vg2)
//                        .setTitle("温度一")
//                        .setPositiveButton("确定",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//
//                                        String str = etShow2.getText()
//                                                .toString();
//                                        if (socketMessage == null) {
//                                            ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
//                                            return;
//                                        }
////0表示一通道，1表示二通道
//                                        devices.temperatureCalibration(0, Integer.valueOf(str));
//                                        showLoadingDialog();
//                                        new Thread(new MyThread()).start();
//                                    }
//                                }).setNegativeButton("取消", null).show();
//                break;
//            case R.id.temperature2:
//                // 载入xml文件的布局
//                LayoutInflater lf3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                ViewGroup vg3 = (ViewGroup) lf3.inflate(R.layout.edittext_alertdialog,
//                        null);
//                final EditText etShow3 = (EditText) vg3
//                        .findViewById(R.id.edt_msg);
//                etShow3.setHint("设置温度标定一");
//                etShow3.setInputType(InputType.TYPE_CLASS_NUMBER);
//                new AlertDialog.Builder(DealerActivity.this)
//                        .setView(vg3)
//                        .setTitle("温度一")
//                        .setPositiveButton("确定",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        String str = etShow3.getText()
//                                                .toString();
//                                        if (socketMessage == null) {
//                                            ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
//                                            return;
//                                        }
//                                        devices.temperatureCalibration(1, Integer.valueOf(str));
//                                        showLoadingDialog();
//                                        new Thread(new MyThread()).start();
//                                    }
//                                }).setNegativeButton("取消", null).show();
//                break;
            case R.id.pwd:
                // 载入xml文件的布局
                LayoutInflater lf1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup vg1 = (ViewGroup) lf1.inflate(R.layout.edittext_alertdialog,
                        null);
                final EditText pwd = (EditText) vg1
                        .findViewById(R.id.edt_msg);
                final EditText confirmpwd = (EditText) vg1
                        .findViewById(R.id.edt_msg2);
                confirmpwd.setVisibility(View.VISIBLE);
                pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                confirmpwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                pwd.setHint("输入密码 （四位数字）");
                pwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                confirmpwd.setInputType(InputType.TYPE_CLASS_NUMBER);
                confirmpwd.setHint("确认密码 （四位数字）");
                new AlertDialog.Builder(DealerActivity.this)
                        .setView(vg1)
                        .setTitle("设置永久开机密码")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                                            ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                                            return;
                                        }
                                        if (TextUtils.isEmpty(pwd.getText()) || TextUtils.isEmpty(confirmpwd.getText())) {
                                            ToastUtil.showToast(DealerActivity.this, "请填写密码");
                                            try {
                                                Field field = dialog.getClass()
                                                        .getSuperclass().getDeclaredField(
                                                                "mShowing");
                                                field.setAccessible(true);
                                                // 将mShowing变量设为false，表示对话框已关闭
                                                field.set(dialog, false);
                                                dialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }
                                        if ((pwd.getText().length() < 4) || (confirmpwd.getText().length() < 4)) {
                                            ToastUtil.showToast(DealerActivity.this, "请填写四位数字密码");
                                            try {
                                                Field field = dialog.getClass()
                                                        .getSuperclass().getDeclaredField(
                                                                "mShowing");
                                                field.setAccessible(true);
                                                // 将mShowing变量设为false，表示对话框已关闭
                                                field.set(dialog, false);
                                                dialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }
                                        if (!TextUtils.equals(pwd.getText(), confirmpwd.getText())) {
                                            ToastUtil.showToast(DealerActivity.this, "两次密码不正确");
                                            try {
                                                Field field = dialog.getClass()
                                                        .getSuperclass().getDeclaredField(
                                                                "mShowing");
                                                field.setAccessible(true);
                                                // 将mShowing变量设为false，表示对话框已关闭
                                                field.set(dialog, false);
                                                dialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return;
                                        }
                                        showLoadingDialog();
                                        devices.foreverOpenAdmin(confirmpwd.getText().toString());
                                        new Thread(new MyThread()).start();
                                        try {
                                            Field field = dialog.getClass()
                                                    .getSuperclass().getDeclaredField(
                                                            "mShowing");
                                            field.setAccessible(true);
                                            // 将mShowing变量设为false，表示对话框已关闭
                                            field.set(dialog, true);
                                            dialog.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            Field field = dialog.getClass()
                                    .getSuperclass().getDeclaredField(
                                            "mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, true);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).show();


                break;
            case R.id.rl_date:
                // 载入xml文件的布局

                LayoutInflater lf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup vg = (ViewGroup) lf.inflate(R.layout.edittext_alertdialog,
                        null);
                final EditText etShow = (EditText) vg
                        .findViewById(R.id.edt_msg);
                etShow.setHint("最大为255天");
                etShow.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(DealerActivity.this)
                        .setView(vg)
                        .setTitle("设置宽限天数")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        time = etShow.getText()
                                                .toString();
                                        if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                                            ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                                            return;
                                        }
                                        if (Integer.valueOf(time) > 255) {
                                            ToastUtil.showToast(DealerActivity.this, "最大天数为255天");
                                            return;
                                        }
                                        showLoadingDialog();
                                        devices.lockMachineDays(Integer.valueOf(time));

                                        new Thread(new MyThread()).start();

                                    }
                                }).setNegativeButton("取消", null).show();
                break;
            case R.id.time:
                DateTimePickerDialog d = new DateTimePickerDialog(this, System.currentTimeMillis());
                d.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
                    @Override
                    public void OnDateTimeSet(AlertDialog dialog, long date) {
                        txt.setText(getStringDate(date));
                        if (socketMessage == null || !MyApplication.sp.GetNetWorkState().equals("2")) {
                            ToastUtil.showToast(DealerActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                            return;
                        }
                        showLoadingDialog();
                        devices.timeInitialization(getStrin(date));
                        new Thread(new MyThread()).start();
                    }
                });
                d.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(lockMachineDays);
        unregisterReceiver(foreverOpenAdmin);
        unregisterReceiver(lockMachine);
        unregisterReceiver(temperatureCalibration);
        unregisterReceiver(timeInitialization);
        super.onDestroy();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean _run = true;
            while (_run) {
                try {
                    Thread.sleep(10000);// 线程暂停10秒，单位毫秒
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
}
