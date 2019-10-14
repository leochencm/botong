package com.botongglcontroller.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.botongglcontroller.BroadCastManager;
import com.botongglcontroller.adapter.GuoluCanshuAdapter;
import com.botongglcontroller.adapter.YichangAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.beans.Str2Para;
import com.botongglcontroller.beans.String2Date;
import com.botongglcontroller.db.BoilerManager;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.onenetdb.DSItem;
import com.botongglcontroller.onenetdb.DeviceItem;
import com.botongglcontroller.onenetdb.Sendcommand;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.beans.BoilersName;
//import com.btcontroller.beans.Name;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.botongglcontroller.view.CounterView;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.Command;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
//import com.kyleduo.switchbutton.SwitchButton;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.Socket;
import java.text.ParseException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 运行工况
 */
public class OperatingconditionsActivity extends BaseActivity {

    /**
     * The constant instance.
     */
    public static OperatingconditionsActivity instance = null;
    /**
     * The Uid.
     */
    static String UID = "";//链接的锅炉
    //private SocketMessage socketMessage;
    private final Timer timer = new Timer();
    /**
     * The M back.
     */
    ImageView mBack, /**
     * The M abnormal.
     */
    mAbnormal, /**
     * The M importpara.
     */
    mImportpara, /**
     * The Img.
     */
    img, /**
     * The Imgzz.
     */
    imgzz, /**
     * The Imgzzs.
     */
    imgzzs;
    /**
     * The M setting.
     */
    TextView mSetting, /**
     * The Guolu version.
     */
    guoluVersion, /**
     * The New version.
     */
    newVersion, /**
     * The Update red.
     */
    update_red;
    /**
     * The Layout 1.
     */
    RelativeLayout layout1, /**
     * The Layout 2.
     */
    layout2;
    /**
     * The Layout 3.
     */
    LinearLayout layout3, /**
     * The Layout 4.
     */
    layout4, /**
     * The Yichang.
     */
    yichang, /**
     * The Version update.
     */
    versionUpdate, /**
     * The Update version id.
     */
    update_version_id;
    /**
     * The Listview 1.
     */
    ListView listview1, /**
     * The Listview 2.
     */
    listview2;
    /**
     * The Is open.
     */
    Boolean isOpen = false, /**
     * The Is open 2.
     */
    isOpen2 = false;
    /**
     * The Ispost.
     */
    boolean ispost = false, /**
     * The Suc 1.
     */
    suc1 = false;
    /**
     * The Serialnumber.
     */
    String serialnumber;
    /**
     * The M device.
     */
    String mDevice;
    /**
     * The Apikey.
     */
    String apikey;
    /**
     * The List.
     */
    List list = new ArrayList();
    /**
     * The List alert.
     */
    ArrayList<BoilersPara> list_alert = new ArrayList<BoilersPara>();//
    /**
     * The List para.
     */
    ArrayList<BoilersPara> list_para = new ArrayList<BoilersPara>();//总的参数
    /**
     * The Yclist.
     */
    List<HashMap<String, String>> yclist = new ArrayList<HashMap<String, String>>();//异常参数的数列
    /**
     * The Adapter.
     */
    GuoluCanshuAdapter adapter;
    /**
     * The Ycadapter.
     */
    YichangAdapter ycadapter;
    /**
     * The M degree.
     */
    CounterView mDegree;
    /**
     * The Aa.
     */
    float aa;
    /**
     * The Param.
     */
    int param[] = new int[106];
    /**
     * The Colornum.
     */
    String colornum = "";
    /**
     * The Parkey.
     */
    String parkey = null;
    /**
     * The Hpa.
     */
    String hpa = "255", /**
     * The Spa.
     */
    spa = "255", /**
     * The Mpa.
     */
    mpa = "255", /**
     * The Lpa.
     */
    lpa = "255";
    /**
     * The Syali.
     */
    String syali = null;
    /**
     * The Min.
     */
    String min = "", /**
     * The Max.
     */
    max = "";
    /**
     * The Wendu.
     */
    TextView wendu, /**
     * The Temp min.
     */
    temp_min, /**
     * The Temp mid.
     */
    temp_mid, /**
     * The Temp max.
     */
    temp_max;
    /**
     * The Sendparam.
     */
    String sendparam = "";
    /**
     * The Remotelock.
     */
//    SwitchButton remotelock, operation, shoudong;
    CheckBox remotelock, /**
     * The Operation.
     */
    operation, /**
     * The Shoudong.
     */
    shoudong;
    /**
     * The Socket.
     */
    Socket socket;
    /**
     * The Toasttrue.
     */
    String toasttrue = "", /**
     * The Toastfalse.
     */
    toastfalse = "";
    /**
     * The Mg.
     */
    BoilerManager mg;
    /**
     * The Intention.
     */
    String intention = "";//判断是哪个广播
    /**
     * The Mytr.
     */
    MyThread mytr;
    /**
     * The Thread back.
     */
    Thread_back threadBack;
    /**
     * The Issend.
     */
    BroadcastReceiver issend = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");
            boolean send = intent.getBooleanExtra("issend", false);
            if (send) ispost = true;
            else ispost = false;

        }
    };
    /**
     * The Newsmsg.
     */
    BroadcastReceiver newsmsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");
            int msg = intent.getIntExtra("msg", 0);
            switch (msg) {
                case 1:
                    ToastUtil.showToast(OperatingconditionsActivity.this, "未绑定锅炉，请绑定锅炉！");
                    break;
                case 2:
                    ToastUtil.showToast(OperatingconditionsActivity.this, "锅炉不在线！");
                    break;

            }

        }
    };
    /**
     * 开启查询线程
     */
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            getCnshu("");
            handler.postDelayed(this, 5000);
        }
    };
    /**
     * The Runnable.
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);// 发送消息
        }
    };
    //BoilersPara boiler=new Boilers();
    private boolean isfirst = true;
    /**
     * The Handler.
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            switch (msg.what) {
                case 1:
                    hideLoadingDialog();
                    break;
                case 2:
                    if (!(progressDialg == null)) {
                        if (intention.equals("alldata")) {
                            hideLoadingDialog();
                            showStringToastMsg("链接超时,请重新连接！");
                            Intent in = new Intent();
                            in.putExtra("UID", UID);
                            setResult(RESULT_OK, in);
                            finish();
                        } else if (intention.equals("remoteBootlock")) {
                            hideLoadingDialog();
                            showStringToastMsg("指令超时,请重新操作！");
                            Intent in = new Intent();
                            in.putExtra("UID", UID);
                            setResult(RESULT_OK, in);
                            finish();
                        } else if (intention.equals("deviceOperation")) {
                            hideLoadingDialog();
                            showStringToastMsg("指令超时,请重新连接！");
                            Intent in = new Intent();
                            in.putExtra("UID", UID);
                            setResult(RESULT_OK, in);
                            finish();
                        } else if (intention.equals("shoudong")) {
                            hideLoadingDialog();
                            showStringToastMsg("指令超时,请重新连接！");
                            Intent in = new Intent();
                            in.putExtra("UID", UID);
                            setResult(RESULT_OK, in);
                            finish();
                        } else {
                            hideLoadingDialog();
                            showStringToastMsg("链接超时,请重新连接！");
                        }
                    }
                    break;
                case 3:
                    hideLoadingDialog();
                    //timer.cancel();
                    ispost = false;
                    //if(msg.obj.toString()=="16386"){
                    //if(remotelock.isChecked())remotelock.setChecked(false);
                    // else remotelock.setChecked(true);
                    LemonBubble.showError(OperatingconditionsActivity.this, toastfalse, 1000);
                    handler.removeCallbacks(runnable1);
                    //
// }
//                    else if(msg.obj.toString()=="16387"){
//                        //if(operation.isChecked())operation.setChecked(false);
//                       // else operation.setChecked(true);
//                        LemonBubble.showError(OperatingconditionsActivity.this, toastfalse, 1000);
//                    }
//                    else if(msg.obj.toString()=="16388"){
//                        //if(shoudong.isChecked())shoudong.setChecked(false);
//                        //else shoudong.setChecked(true);
//                        LemonBubble.showError(OperatingconditionsActivity.this, toastfalse, 1000);
//                    }
                    break;
                case 4:
                    showStringToastMsg("获取数据失败，请重新连接！");
                    break;
                case 5:
                    //刷新数据
                    if (isfirst) {
                        hideLoadingDialog();
                        isfirst = false;
                    }
                    for (int i = 0; i < list_para.size(); i++) {
                        if (list_para.get(i).getAddr().equals("4002")) {
                            if (list_para.get(i).getValue().equals("0"))
                                remotelock.setChecked(false);
                            else remotelock.setChecked(true);
                            MyApplication.sp.setfaconstate(list_para.get(i).getValue());
                        } else if (list_para.get(i).getAddr().equals("4003")) {
                            if (list_para.get(i).getValue().equals("0"))
                                operation.setChecked(false);
                            else operation.setChecked(true);
                            MyApplication.sp.setfacopenstate(list_para.get(i).getValue());

                        } else if (list_para.get(i).getAddr().equals("4004")) {
                            if (list_para.get(i).getValue().equals("0"))
                                shoudong.setChecked(false);
                            else shoudong.setChecked(true);
                            MyApplication.sp.setisshoudong(list_para.get(i).getValue());
                        }
                    }
                    setImage();
                    adapter = new GuoluCanshuAdapter(OperatingconditionsActivity.this, list_para);
                    listview2.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listview2);
                    ycadapter = new YichangAdapter(OperatingconditionsActivity.this, list_alert);
                    listview1.setAdapter(ycadapter);
                    setListViewHeightBasedOnChildren(listview1);
                    Bundle bundle1 = new Bundle();
                    //bundle.putSerializable("alert", (Serializable) list_alert);
                    bundle1.putSerializable("para", (Serializable) list_para);
                    bundle1.putString("mDevice", mDevice);
                    bundle1.putString("apikey", apikey);
                    bundle1.putString("id", "2");
                    bundle1.putString("serialnumber", serialnumber);
                    Intent intent = new Intent("com.broadcast.refresh");
                    intent.putExtras(bundle1);
                    sendBroadcast(intent);
                    break;
                case 6:
                    hideWaitingDialog();

                    ispost = false;
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Str2Para str2Para = new Str2Para();
    private Devices devices;
    private TimerTask task;
    private Button update_button;
    private DeviceItem mDeviceItem;
    private List<DSItem> mDsItem = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.operatingconditions;
    }

    @Override
    public void initView() {
        img = (ImageView) findViewById(R.id.imageback);
        mBack = (ImageView) findViewById(R.id.img_back);
        layout1 = (RelativeLayout) findViewById(R.id.rl_yichang);
        layout2 = (RelativeLayout) findViewById(R.id.rl_zycs);
        layout4 = (LinearLayout) findViewById(R.id.ll_zycs);
        yichang = (LinearLayout) findViewById(R.id.ll_yc);
        mAbnormal = (ImageView) findViewById(R.id.img_yichang);
        mImportpara = (ImageView) findViewById(R.id.img_zycs);
        listview1 = (ListView) findViewById(R.id.lv_yichang);
        listview2 = (ListView) findViewById(R.id.lv_zycs);
        imgzz = (ImageView) findViewById(R.id.imagezhizhen);
        imgzzs = (ImageView) findViewById(R.id.imagezhizhens);
        temp_min = (TextView) findViewById(R.id.texttemp0);
        temp_mid = (TextView) findViewById(R.id.texttemp1);
        temp_max = (TextView) findViewById(R.id.texttemp2);
        // mDegree = (CounterView) findViewById(R.id.secondCounter);
        wendu = (TextView) findViewById(R.id.txt_wendu);
        //state = (TextView) findViewById(R.id.state);
        //yali=(TextView)findViewById(R.id.txt_yali);
        //yl=(TextView)findViewById(R.id.ylstate);

        mSetting = (TextView) findViewById(R.id.txt_setting);
        remotelock = (CheckBox) findViewById(R.id.btn_remotelock);
        operation = (CheckBox) findViewById(R.id.btn_operation);
        shoudong = (CheckBox) findViewById(R.id.btn_shoudong);
        guoluVersion = (TextView) findViewById(R.id.guolu_version);
        newVersion = (TextView) findViewById(R.id.new_version);
        versionUpdate = (LinearLayout) findViewById(R.id.version_update);
        update_version_id = (LinearLayout) findViewById(R.id.update_version_id);
        update_red = (TextView) findViewById(R.id.update_red);
        update_button = (Button) findViewById(R.id.update_button);
        //getParameter();
//        IntentFilter filter1 = new IntentFilter("com.broadcast.alertState");
//        registerReceiver(alertState, filter1);
        IntentFilter filter2 = new IntentFilter("com.broadcast.newsmsg");
        BroadCastManager.getInstance().registerReceiver(this, newsmsg, filter2);//注册广播接收者
        IntentFilter filter3 = new IntentFilter("com.broadcast.issend");
        registerReceiver(issend, filter3);

//        IntentFilter filter4 = new IntentFilter("com.broadcast.remoteBootlock");
//        registerReceiver(remoteBootlock, filter4);
//        IntentFilter filter5 = new IntentFilter("com.broadcast.deviceOperation");
//        registerReceiver(deviceOperation, filter5);
//        IntentFilter filter6 = new IntentFilter("com.broadcast.lock");
//        registerReceiver(lock, filter6);
//        IntentFilter filter7 = new IntentFilter("com.broadcast.shoudong");
//        registerReceiver(setshoudong, filter7);
        mg = new BoilerManager(OperatingconditionsActivity.this);
        //    IntentFilter filter8 = new IntentFilter("com.broadcast.refresh");
        //    registerReceiver(Refresh, filter8);
//        IntentFilter filter9 = new IntentFilter("com.broadcast.refresh2");
//        registerReceiver(Refresh, filter9);
//        IntentFilter filter10 = new IntentFilter("com.broadcast.cancelalertState");
//        registerReceiver(cancelalertState, filter10);
        instance = this;
    }
//    public class MyThread implements Runnable {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            boolean _run = true;
//            while (_run) {
//                try {
//                    Thread.sleep(8000);// 线程暂停10秒，单位毫秒
////                    hideLoadingDialog();
//                    Message message = new Message();
//                    Log.e("isconnect",""+MyApplication.isconnect);
//                    if (MyApplication.isconnect) {
//                        message.what = 1;
//                        handler.sendMessage(message);// 发送消息
//                    } else {
//                        message.what = 2;
//                        handler.sendMessage(message);// 发送消息
//                    }
//                    _run = false;
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public void initData() {
        // mDegree.setAutoFormat(false);
        // mDegree.setAutoStart(true);
        Intent it = getIntent();
        list_para = (ArrayList<BoilersPara>) it.getSerializableExtra("boiler");
        serialnumber = it.getStringExtra("serialnumber");
        mDevice = it.getStringExtra("oid");
        apikey = it.getStringExtra("apikey");

        //1表示链接硬件时从锅炉列表进入,2表示链接硬件时从绑定锅炉进入,3表示链接后台
        if (getIntent().getStringExtra("id").equals("1")) {
            CheckNetWork();
        } else if (getIntent().getStringExtra("id").equals("2")) {
            CheckNetWork();
        } else if (getIntent().getStringExtra("id").equals("3")) {
            CheckNetWork();
        }
        MyApplication.sp.setSerialnumber(serialnumber);
        MyApplication.sp.SetOid(mDevice);
    }

    private void getName() {
        //当链接硬件时获取的参数
        Log.i("查询锅炉编号", "guolu" + serialnumber);
        Boilers boiler = new Boilers();
        boiler.serialnumber = serialnumber;
        if (!(mg.queryshown(boiler) == null) && mg.queryshown(boiler).length > 0) {
            String showenable = mg.queryshown(boiler)[1];
            Gson gson = new Gson();
            String result = showenable;
            Type listType = new TypeToken<List<BoilersName>>() {
            }.getType();
            list_para = gson.fromJson(result, listType);
        }
    }

    private void getParameter() {
//        String showenable = getIntent().getStringExtra("showenable");
//        Gson gson = new Gson();
//        String result = showenable;
//        Type listType = new TypeToken<List<BoilersName>>() {
//        }.getType();
        //name = boiler.getPara();
    }

    private void CheckNetWork() {
        Log.i("GetNetWorkState()", MyApplication.sp.GetNetWorkState());
//        NetUtil.CheckNetWork(OperatingconditionsActivity.this);
        if (MyApplication.sp.GetNetWorkState().equals("-1")) {
            showLongStringToastMsg("网络断开或不稳定，请检查网络！");
            finish();

        } else if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
            Boilers boiler = new Boilers();
            boiler.serialnumber = serialnumber;
            if (!(mg.queryshown(boiler).length == 0)) {
                String showenable = mg.queryshown(boiler)[1];
                Gson gson = new Gson();
                String result = showenable;
                Type listType = new TypeToken<List<BoilersName>>() {
                }.getType();
                // name = gson.fromJson(result, listType);
                // getDataStreams("");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getCnshu("");
//                    }
//                }).start();
                showLoadingDialog();
                handler.postDelayed(runnable1, 2000); // 延迟2秒，;
//                task=new TimerTask() {
//                    @Override
//                    public void run() {
//                        getCnshu("");
//                    }
//                };
//                timer.schedule(task,0,5000);


            }

        } else if (MyApplication.sp.GetNetWorkState().equals("2")) {

            //判断是否已连接，如无链接则链接
            getName();
            Intent bindIntent = new Intent(OperatingconditionsActivity.this, SocketService.class);
            startService(bindIntent);
            intention = "alldata";
            showLoadingDialog();
            handler.postDelayed(runnable, 8000); // 延迟2秒，;

        }

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout4.setOnClickListener(this);
        yichang.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        remotelock.setOnClickListener(this);
        operation.setOnClickListener(this);
        shoudong.setOnClickListener(this);
        update_button.setOnClickListener(this);
    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        Intent in = new Intent();
        in.putExtra("UID", UID);
        setResult(RESULT_OK, in);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Intent in = new Intent();
                in.putExtra("UID", UID);
                setResult(RESULT_OK, in);
                finish();
                break;
            case R.id.rl_yichang:
                if (isOpen) {
                    mAbnormal.setImageResource(R.mipmap.arrow_down);
                    listview1.setVisibility(View.GONE);
                    isOpen = false;
                } else {
                    listview1.setVisibility(View.VISIBLE);
                    mAbnormal.setImageResource(R.mipmap.arrow_up);
                    isOpen = true;
                }
                break;
            case R.id.rl_zycs:
                if (isOpen2) {
                    mImportpara.setImageResource(R.mipmap.arrow_down);
                    listview2.setVisibility(View.GONE);
                    isOpen2 = false;
                } else {
                    listview2.setVisibility(View.VISIBLE);
                    mImportpara.setImageResource(R.mipmap.arrow_up);
                    isOpen2 = true;
                }
                break;
            case R.id.ll_zycs:
                Bundle bundle = new Bundle();
                //bundle.putSerializable("alert", (Serializable) list_alert);
                bundle.putSerializable("para", (Serializable) list_para);
                bundle.putString("mDevice", mDevice);
                bundle.putString("apikey", apikey);
                Intents.getIntents().Intent(OperatingconditionsActivity.this, ParameterdisplayActivity.class, bundle);
                //threadBack.stop();
                break;
            case R.id.ll_yc:
                if (!MyApplication.sp.GetNetWorkState().equals("2")) {
                    Intent intent = new Intent(OperatingconditionsActivity.this, MainActivity.class);
                    intent.putExtra("news", 2);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.txt_setting:
//                if (!remotelock.isChecked()) {
//                    ToastUtil.showToast(this, "开机状态才可进入！");
//                    return;
//                }
                Bundle bundle1 = new Bundle();
                //bundle.putSerializable("alert", (Serializable) list_alert);
                bundle1.putSerializable("para", (Serializable) list_para);
                bundle1.putString("mDevice", mDevice);
                bundle1.putString("apikey", apikey);
                bundle1.putString("id", "2");
                bundle1.putString("serialnumber", serialnumber);
                Intents.getIntents().Intent(OperatingconditionsActivity.this, SettingcanshuActivity.class, bundle1);
                //threadBack.stop();
                break;

            case R.id.btn_remotelock:
                String title = "", msg = "", action = "", order = "", select = "remotelock";
                int ord;
                if (remotelock.isChecked()) {
                    title = "您确定要远程开机么？";
                    msg = "开机后您将可以操作锅炉";
                    action = "我要开机";
                    toasttrue = "开机成功";
                    toastfalse = "开机失败";
                    order = "1";
                    ord = 0xff;
                } else {
                    title = "您确定要远程关机么？";
                    msg = "关机后您将不可以操作锅炉";
                    action = "我要关机";
                    toasttrue = "关机成功";
                    toastfalse = "关机失败";
                    order = "0";
                    ord = 0x00;
                }
                ispost = true;
                getHellow(title, msg, action, toasttrue, toastfalse, select, order, ord);
                break;

            case R.id.btn_operation:
                select = "operation";
                if (!remotelock.isChecked()) {
                    ToastUtil.showToast(this, "未开机，运行开关不可打开！");
                    operation.setChecked(!operation.isChecked());
                    return;
                }
                if (shoudong.isChecked()) {
                    ToastUtil.showToast(this, "手动开关已打开，运行开关不可打开！");
                    operation.setChecked(!operation.isChecked());
                    return;
                }
                if (operation.isChecked()) {
                    title = "您确定要让设备运行么？";
                    msg = "运行后锅炉将会开启运行";
                    action = "我要运行";
                    toasttrue = "运行成功";
                    toastfalse = "运行失败";
                    order = "1";
                    ord = 0xff;
                } else {
                    title = "您确定要要让设备待机么？";
                    msg = "待机后设备将不进行操作";
                    action = "我要待机";
                    toasttrue = "待机成功";
                    toastfalse = "待机失败";
                    order = "0";
                    ord = 0x00;
                }
                ispost = true;
                getHellow(title, msg, action, toasttrue, toastfalse, select, order, ord);
                break;
            case R.id.btn_shoudong:
                select = "shoudong";
//                int ord;
                if (!remotelock.isChecked()) {
                    ToastUtil.showToast(this, "未开机，手动开关不可打开！");
                    shoudong.setChecked(!shoudong.isChecked());
                    return;
                }
                if (operation.isChecked()) {
                    ToastUtil.showToast(this, "运行开关已打开，手动开关不可打开");
                    shoudong.setChecked(!shoudong.isChecked());
                    return;
                }
                if (shoudong.isChecked()) {
                    title = "您确定要手动么？";
                    msg = "手动后您将可以控制锅炉";
                    action = "我要手动";
                    toasttrue = "手动成功";
                    toastfalse = "手动失败";
                    order = "1";
                    ord = 0xff;
                } else {
                    title = "您确定要关闭手动么？";
                    msg = "关闭后您将不可以控制锅炉";
                    action = "我要关闭";
                    toasttrue = "关闭成功";
                    toastfalse = "关闭失败";
                    order = "0";
                    ord = 0x00;
                }
                ispost = true;
                getShoudong(title, msg, action, toasttrue, toastfalse, select, order, ord);
                break;
            case R.id.update_button:

                new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("升级设备需要重启，再此过程中，锅炉需要保持有网有电状态！")
                        .setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                updateVersion();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                break;
        }
    }

    /**
     * 升级
     */
    private void updateVersion() {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
        ps.put("serialnumber", serialnumber);
        showLoadingDialog();
        MyApplication.http.configTimeout(10000);
        MyApplication.http.post(Api.InformUpdate, ps, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        hideLoadingDialog();
                        new AlertDialog.Builder(OperatingconditionsActivity.this).setTitle("温馨提示").setMessage(object.getString("message"))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(OperatingconditionsActivity.this, GuolulistActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                    } else {
                        hideLoadingDialog();
                        new AlertDialog.Builder(OperatingconditionsActivity.this).setTitle("温馨提示").setMessage(object.getString("message"))
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                hideLoadingDialog();
                new AlertDialog.Builder(OperatingconditionsActivity.this).setTitle("温馨提示").setMessage("请求失败，请检查网络!")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    //shoudong
    private void getShoudong(String title, String msg, final String action, final String toasttrue, final String toastfalse, final String select, final String order, final int ord) {
        LemonHello.getInformationHello(title, msg)
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        ispost = false;
                        if (select.equals("shoudong")) {
                            shoudong.setChecked(!shoudong.isChecked());
                        }
                    }
                }))
                .addAction(new LemonHelloAction(action, Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        // 提示框使用了LemonBubble，请您参考：https://github.com/1em0nsOft/LemonBubble4Android
                        if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
                            postOperation("hand", order, toasttrue, toastfalse, ord);
                            Log.i("手动", "shoudong");
                        } else {
                            if (!MyApplication.sp.GetsocketState()) {
                                ToastUtil.showToast(OperatingconditionsActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                                if (select.equals("shoudong")) {
                                    shoudong.setChecked(!shoudong.isChecked());
                                } else {
                                    shoudong.setChecked(!shoudong.isChecked());
                                }
                                return;
                            }
                            Log.i("shoudong", "" + ord);
                            devices.shoudong(ord);
                            MyApplication.isconnect = false;
                            intention = "shoudong";
                            showLoadingDialog();
                            handler.postDelayed(runnable, 8000); // 延迟2秒，
                        }

                    }
                }))
                .show(OperatingconditionsActivity.this);
    }

    //开关机运行指令
    private void getHellow(String title, String msg, final String action, final String toasttrue, final String toastfalse, final String select, final String order, final int ord) {
        LemonHello.getInformationHello(title, msg)
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        if (select.equals("remotelock")) {
                            remotelock.setChecked(!remotelock.isChecked());
                        } else {
                            operation.setChecked(!operation.isChecked());
                        }
                        ispost = false;
                    }
                }))
                .addAction(new LemonHelloAction(action, Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();

                        if (select.equals("remotelock")) {
                            //一键开机
                            if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
                                postOperation("power", order, toasttrue, toastfalse, ord);
                            }
                        } else {
                            //设备运行
                            if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
                                postOperation("run", order, toasttrue, toastfalse, ord);
                            }
                        }
                    }
                }))
                .show(OperatingconditionsActivity.this);
    }

    private void postOperation(String function, String order, final String toasttrue, final String toastfalse, final int select) {
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("serialnumber", MyApplication.sp.GetSerialnumber());
            params.put("function", function);
            params.put("order", order);
            Log.i("params", params.toString());
            showWaitingCmdDialog();

            if (function == "power")
                sendCommand(Sendcommand.devicePower(select), select, "16386", toasttrue, toastfalse);
            if (function == "run")
                sendCommand(Sendcommand.deviceRun(select), select, "16387", toasttrue, toastfalse);
            if (function == "hand")
                sendCommand(Sendcommand.deviceHand(select), select, "16388", toasttrue, toastfalse);

//            MyApplication.http.configTimeout(10000);
//            MyApplication.http.post(Api.FacilityWorkControl, params, new AjaxCallBack<Object>() {
//                @Override
//                public void onSuccess(Object o) {
//                    super.onSuccess(o);
//                    hideLoadingDialog();
//                    Log.i("设备工作指令", o.toString());
//                    try {
//                        JSONObject object = new JSONObject(o.toString());
//                        if (object.getString("resultcode").equals("0")) {
//                            MyApplication.sp.setisshoudong(shoudong.isChecked() ? "1" : "0");
//                           LemonBubble.showRight(OperatingconditionsActivity.this, toasttrue, 1000);
//                        } else {
//                            if (object.getString("resultcode").equals("1")) {
//                                LemonBubble.showError(OperatingconditionsActivity.this, object.getString("message"), 1000);
//                            }
//                            if (select.equals("remotelock")) {
//                                remotelock.setChecked(!remotelock.isChecked());
//                            } else if (select.equals("shoudong")) {
//                                shoudong.setChecked(!shoudong.isChecked());
//                            } else {
//                                operation.setChecked(!operation.isChecked());
//                            }
//                            LemonBubble.showError(OperatingconditionsActivity.this, object.getString("message"), 1000);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//                @Override
//                public void onFailure(Throwable t, String strMsg) {
//                    super.onFailure(t, strMsg);
//                    hideLoadingDialog();
//                    if (select.equals("remotelock")) {
//                        remotelock.setChecked(!remotelock.isChecked());
//                    } else if (select.equals("shoudong")) {
//                        shoudong.setChecked(!shoudong.isChecked());
//                    } else {
//                        operation.setChecked(!operation.isChecked());
//                    }
//                    LemonBubble.showError(OperatingconditionsActivity.this, toastfalse, 1000);
//                }
//            });

        } catch (ParseException e) {

            e.printStackTrace();
        }
    }

    private void sendCommand(byte[] by, final int flag, final String str, final String toasttrue, final String toastfalse) {
        OneNetApi.setAppKey(apikey);
        OneNetApi.sendCmdToDevice(
                mDevice,
                true,
                4000,
                Command.CommandType.TYPE_CMD_REQ,
                by,
                new OneNetApiCallback() {
                    @Override
                    public void onSuccess(String response) {
                        JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                        int errno = resp.get("errno").getAsInt();
                        if (0 == errno) {
                            mytr = new MyThread();
                            mytr.setStr(str);
                            mytr.setToasttrue(toasttrue);
                            mytr.setToastfalse(toastfalse);
                            Date date = new Date(System.currentTimeMillis());
                            mytr.setDt(flag);
                            new Thread(mytr).start();
                            //getDataPoint(str,str);
                        }

                    }

                    @Override
                    public void onFailed(Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    private void getDataPoint(final String streamid, final long dt) {
        //final boolean[] succ=new boolean[1];
        try {
            OneNetApi.querySingleDataStream(mDevice, streamid, new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONObject json = resp.getJSONObject("data");
                            try {
                                if (dt == 0 && json.getString("current_value").equals("0") || dt == 255 && json.getString("current_value").equals("255")) {
                                    mytr.stop();
                                    hideWaitingDialog();
                                    ispost = false;
                                    if (streamid.equals("16386")) {
                                        if (json.getString("current_value").equals("0"))
                                            remotelock.setChecked(false);
                                        else remotelock.setChecked(true);
                                        LemonBubble.showRight(OperatingconditionsActivity.this, toasttrue, 1000);
                                    } else if (streamid.equals("16387")) {
                                        if (json.getString("current_value").equals("0"))
                                            operation.setChecked(false);
                                        else operation.setChecked(true);
                                        LemonBubble.showRight(OperatingconditionsActivity.this, toasttrue, 1000);
                                    } else if (streamid.equals("16388")) {
                                        if (json.getString("current_value").equals("0"))
                                            shoudong.setChecked(false);
                                        else shoudong.setChecked(true);
                                        LemonBubble.showRight(OperatingconditionsActivity.this, toasttrue, 1000);
                                    }
                                    for (int i = 0; i < list_para.size(); i++)
                                        if (list_para.get(i).getAddr_int().equals(streamid))
                                            list_para.get(i).setValue(json.getString("current_value"));
                                }
                            } catch (Exception e) {
                            }
                        }
                    } catch (JSONException e) {

                    }
                    mytr.setflag = 1;
                }

                @Override
                public void onFailed(Exception e) {
                    mytr.setflag = 1;
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param s 指示是否刷新
     */
    private void getDataStreams(String s) {
        try {
            if (!s.equals("refresh")) {
                showLoadingDialog();
            }
            //name.clear();
            String[] streamid = new String[list_para.size()];
            for (int i = 0; i < list_para.size(); i++)
                streamid[i] = list_para.get(i).getAddr_int();
            OneNetApi.setAppKey(apikey);
            OneNetApi.queryMultiDataStreams(mDevice, streamid, new OneNetApiCallback() {
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONArray json = resp.getJSONArray("data");
                            for (int i = 0; i < list_para.size(); i++) {
                                list_alert.clear();
                                JSONObject ob = json.getJSONObject(i);
                                if (list_para.get(i).getAddr().equals("4002")) {
                                    if (ob.getString("current_value").equals("0"))
                                        remotelock.setChecked(false);
                                    else remotelock.setChecked(true);
                                    MyApplication.sp.setfaconstate(ob.getString("current_value"));
                                } else if (list_para.get(i).getAddr().equals("4003")) {
                                    if (ob.getString("current_value").equals("0"))
                                        operation.setChecked(false);
                                    else operation.setChecked(true);
                                    MyApplication.sp.setfacopenstate(ob.getString("current_value"));
                                } else if (list_para.get(i).getAddr().equals("4004")) {
                                    if (ob.getString("current_value").equals("0"))
                                        shoudong.setChecked(false);
                                    else shoudong.setChecked(true);
                                    MyApplication.sp.setisshoudong(ob.getString("current_value"));
                                }
                                try {
                                    list_para.get(i).setTime(ob.getString("update_at"));
                                } catch (ParseException e) {

                                }
                                list_para.get(i).setValue(ob.getString("current_value"));
                                if (Integer.parseInt(list_para.get(i).getAddr_int()) >= 0x5000 && Integer.parseInt(list_para.get(i).getAddr_int()) <= 0x50ff) {
                                    if (!list_para.get(i).getValue().equals("0"))
                                        list_alert.add(list_para.get(i));
                                }

                            }

                            setImage();
                            adapter = new GuoluCanshuAdapter(OperatingconditionsActivity.this, list_para);
                            listview2.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(listview2);
                            ycadapter = new YichangAdapter(OperatingconditionsActivity.this, list_alert);
                            listview1.setAdapter(ycadapter);
                            setListViewHeightBasedOnChildren(listview1);
                            hideLoadingDialog();
                        } else {
                            Message message = new Message();
                            message.what = 6;
                            handler.sendMessage(message);// 发送消息
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailed(Exception e) {
                    hideLoadingDialog();
                    LemonBubble.showError(OperatingconditionsActivity.this, "链接失败,请检查网络！", 2000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCnshu(String s) {

        try {
            if (!s.equals("refresh")) {
                //  showLoadingDialog();
            }
            if (ispost) return;
            //name.clear();
            String[] streamid = new String[list_para.size()];
            for (int i = 0; i < list_para.size(); i++)
                streamid[i] = list_para.get(i).getAddr_int();
            OneNetApi.setAppKey(apikey);
            OneNetApi.queryMultiDataStreams(mDevice, streamid, new OneNetApiCallback() {
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONArray json = resp.getJSONArray("data");
                            list_alert.clear();
                            for (int i = 0; i < list_para.size(); i++) {
                                JSONObject ob = json.getJSONObject(i);

//                                if (list_para.get(i).getAddr().equals("4002")) {
//                                    if (ob.getString("current_value").equals("0"))
//                                        remotelock.setChecked(false);
//                                    else remotelock.setChecked(true);
//                                    MyApplication.sp.setfaconstate(ob.getString("current_value"));
//                                } else if (list_para.get(i).getAddr().equals("4003")) {
//                                    if (ob.getString("current_value").equals("0"))
//                                        operation.setChecked(false);
//                                    else operation.setChecked(true);
//
//                                } else if (list_para.get(i).getAddr().equals("4004")) {
//                                    if (ob.getString("current_value").equals("0"))
//                                        shoudong.setChecked(false);
//                                    else shoudong.setChecked(true);
//                                }
                                try {
                                    list_para.get(i).setTime(ob.getString("update_at"));
                                } catch (ParseException e) {

                                }
                                list_para.get(i).setValue(ob.getString("current_value"));
                                if (Integer.parseInt(list_para.get(i).getAddr_int()) >= 0x5000 && Integer.parseInt(list_para.get(i).getAddr_int()) <= 0x50ff) {
                                    if (!list_para.get(i).getValue().equals("0")) {
                                        list_alert.add(list_para.get(i));
                                    }
                                }

                            }
                            Message message = new Message();
                            message.what = 5;
                            handler.sendMessage(message);
                            // threadBack =new Thread_back();
                            // new Thread(threadBack).start();
                        } else {
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);// 发送消息
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailed(Exception e) {
                    hideLoadingDialog();
                    LemonBubble.showError(OperatingconditionsActivity.this, "链接失败,请检查网络！", 2000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage() {
        Log.i("list3+namee+isVisible", "" + list_para.size());
        Boolean isWendu = false;
        Boolean isyali = false;
        Boolean flag_spa = false;
        Boolean flag_hpa = false;
        Boolean flag_mpa = false;
        Boolean flag_lpa = false;
        float yali = 22.5f;
        float maxtemp = 150;
        //float wendu;
        for (int i = 0; i < list_para.size(); i++) {
            if (list_para.get(i).getAddr().equals("2000") && list_para.get(i).getVisiable().equals("1")) {
                parkey = list_para.get(i).getValue();
                isWendu = true;
            } else if (list_para.get(i).getAddr().equals("2001") && list_para.get(i).getVisiable().equals("0")) {
                maxtemp = Float.parseFloat(list_para.get(i).getValue());
                //isWendu = true;
            } else if (list_para.get(i).getAddr().equals("1000") && list_para.get(i).getVisiable().equals("1")) {
                spa = list_para.get(i).getValue();
                isyali = true;
                flag_spa = true;
            } else if (list_para.get(i).getAddr().equals("1001") && list_para.get(i).getVisiable().equals("1")) {
                hpa = list_para.get(i).getValue();
                isyali = true;
                flag_hpa = true;
            } else if (list_para.get(i).getAddr().equals("1002") && list_para.get(i).getVisiable().equals("1")) {
                mpa = list_para.get(i).getValue();
                isyali = true;
                flag_mpa = true;
            } else if (list_para.get(i).getAddr().equals("1003") && list_para.get(i).getVisiable().equals("1")) {
                lpa = list_para.get(i).getValue();
                isyali = true;
                flag_lpa = true;
            }
        }

        //判断温度跟气压，如果有温度显示温度，没有温度显示气压，气压只显示高低压
        if (isWendu) {
            Log.i("温度", "温度");
            Log.i("温度", parkey);
            if (img.getWidth() < 1080) {
                temp_min.setTranslationY(20);
                temp_max.setTranslationY(20);
            }
            int yy = img.getHeight();
            int zz = (int) maxtemp / 2;
            temp_min.setText("0℃");
            temp_mid.setText(Integer.toString(zz) + "℃");
            zz = (int) maxtemp;
            temp_max.setText(Integer.toString(zz) + "℃");
            int temp = Integer.parseInt(parkey);
            temp_min.setTranslationX(-img.getWidth() / 2 * 0.74f);
            temp_max.setTranslationX(img.getWidth() / 2 * 0.74f);
            temp_mid.setTranslationY(-img.getHeight() * 0.82f);
            wendu.setText(parkey + "℃");
            // state.setText("锅炉当前运行温度");
            //imgzz.setX(img.getWidth()/2);
            //   imgzz.setY(img.getHeight()-imgzz.getHeight());
            //  int li = img.getHeight();
            imgzz.setY(img.getHeight() * 0.8f - imgzz.getHeight() * 0.946f);
            imgzz.setPivotX(imgzz.getWidth() / 2);
            imgzz.setPivotY(imgzz.getHeight() * 0.946f);//支点在图片中心
            float tempp = Float.parseFloat(parkey);
            imgzz.setRotation(tempp * 188 / maxtemp - 94);
            // xxxx+=3;
            // imgzzs.setTranslationX(img.getWidth()/2);
            //  imgzzs.setY(img.getHeight()-imgzzs.getHeight());

        } else {
            imgzz.setVisibility(View.GONE);
            temp_mid.setVisibility(View.GONE);
            temp_max.setVisibility(View.GONE);
            temp_min.setVisibility(View.GONE);
            wendu.setVisibility(View.GONE);
        }
        if (isyali) {
            imgzzs.setY(img.getHeight() * 0.8f - imgzzs.getHeight() * 0.928f);
            imgzzs.setPivotX(imgzzs.getWidth() / 2);
            imgzzs.setPivotY(imgzzs.getHeight() * 0.928f);//支点在图片中心
            if (flag_lpa && lpa.equals("0")) yali = 22f;
            if (flag_mpa && mpa.equals("0")) yali = 67f;
            if (flag_hpa && hpa.equals("0")) yali = 112f;
            if (flag_spa && spa.equals("0")) yali = 157f;
            imgzzs.setRotation(yali - 90);
        } else {
            imgzzs.setVisibility(View.GONE);
        }
    }

    private void setWendu() {
        //设置温度数字特效
        if (isfirst) {
            mDegree.setStartValue(0f);
            isfirst = false;
        } else {
            mDegree.setStartValue(aa);
        }
        mDegree.setStartValue(aa);
        aa = Float.parseFloat(colornum);
        mDegree.setEndValue(aa);
        mDegree.setIncrement(1f);
        mDegree.setTimeInterval(100);
        mDegree.start();

    }

    /**
     * Sets list view height based on children.
     *
     * @param listView the list view
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {

        //unregisterReceiver(lock);
        // unregisterReceiver(Refresh);
        BroadCastManager.getInstance().unregisterReceiver(this, newsmsg);//注销广播接收者
        //if(timer!=null)timer.cancel();
        handler.removeCallbacks(runnable1);
        super.onDestroy();
    }
//    BroadcastReceiver lock = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MyApplication.isconnect = true;
//            MyApplication.sp.setsocketData(intent.getStringExtra("lockdate"));
//            MyApplication.sp.setfaconstate(intent.getStringExtra("isoperation"));
//            MyApplication.sp.setfacopenstate(intent.getStringExtra("isopen"));
//            MyApplication.sp.setlockstate(intent.getStringExtra("lockstate"));
//            MyApplication.sp.setisshoudong(intent.getStringExtra("isshoudong"));
//            if (intent.getStringExtra("state").equals("0")) {
////                ToastUtil.showToast(OperatingconditionsActivity.this,"设备正常工作");
//            } else if (intent.getStringExtra("state").equals("1")) {
//                ToastUtil.showToast(OperatingconditionsActivity.this, "设备已锁");
//            }
//            if (intent.getStringExtra("isopen").equals("1")) {
//                remotelock.setChecked(true);
//            } else {
//                remotelock.setChecked(false);
//            }
//            if (intent.getStringExtra("isoperation").equals("1")) {
//                operation.setChecked(true);
//            } else {
//                operation.setChecked(false);
//            }
//            if (intent.getStringExtra("isshoudong").equals("1")) {
//                shoudong.setChecked(true);
//            } else {
//                shoudong.setChecked(false);
//            }
//            if (MyApplication.sp.Getfacopenstate().equals("1")) {
//                remotelock.setChecked(true);
//                operation.setEnabled(true);
//                shoudong.setEnabled(true);
//            } else {
//                remotelock.setChecked(false);
//                operation.setEnabled(false);
//                shoudong.setEnabled(false);
//            }
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        GetFirmwareVersion();
    }

    //  获取版本号
    private void GetFirmwareVersion() {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
        ps.put("boiler", serialnumber);
        MyApplication.http.post(Api.GetFirmwareVersion, ps, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        update_version_id.setVisibility(View.VISIBLE);
                        guoluVersion.setText(object.getString("currentversion"));
                        if (!object.getString("currentversion").equals(object.getString("newversion"))) {
                            newVersion.setText(object.getString("newversion"));
                            update_red.setVisibility(View.VISIBLE);
                            versionUpdate.setVisibility(View.VISIBLE);
                        }
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

    /**
     * The type Thread back.
     */
    public class Thread_back implements Runnable {
        /**
         * The Run.
         */
        boolean _run = true;
        /**
         * The Count.
         */
        int count = 0;

        /**
         * Sets count.
         *
         * @param count the count
         */
        public void setCount(int count) {
            this.count = count;
        }

        /**
         * Stop.
         */
        public void stop() {
            _run = false;
        }

        @Override
        public void run() {
            while (_run) {
                try {
                    // if(count>3)_run=false;
                    // count++;
                    if (_run) Thread.sleep(6000);
                    hideLoadingDialog();
                    getDataStreams("refresh");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The type My thread.
     */
    public class MyThread implements Runnable {
        /**
         * The Str.
         */
        String str, /**
         * The Toasttrue.
         */
        toasttrue, /**
         * The Toastfalse.
         */
        toastfalse;
        /**
         * The Dt.
         */
        int dt;
        /**
         * The Run.
         */
        boolean _run = true;
        /**
         * The Setflag.
         */
        int setflag = 1;

        /**
         * Sets str.
         *
         * @param str the str
         */
        public void setStr(String str) {
            this.str = str;
        }

        /**
         * Set dt.
         *
         * @param dt the dt
         */
        public void setDt(int dt) {
            this.dt = dt;
        }

        /**
         * Stop.
         */
        public void stop() {
            _run = false;
        }

        /**
         * Set toasttrue.
         *
         * @param toasttrue the toasttrue
         */
        public void setToasttrue(String toasttrue) {
            this.toasttrue = toasttrue;
        }

        /**
         * Set toastfalse.
         *
         * @param toastfalse the toastfalse
         */
        public void setToastfalse(String toastfalse) {
            this.toastfalse = toastfalse;
        }

        @Override
        public void run() {

            int count = 0;
            while (_run) {
                try {
                    if (_run) {
                        Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                        if (setflag > 0 && _run) {
                            getDataPoint(str, dt);
                            setflag = 0;
                        }
                    }
                    count++;

                    if (count > 9) {
                        Message msg = new Message();
                        msg.obj = str;//message的内容
                        msg.what = 6;//指定message
                        handler.sendMessage(msg);//handler发送message
                        _run = false;
                    }
                    //if(_run)Thread.sleep(5000);// 线程暂停10秒，单位毫秒
                    //progressDialogwating.incrementProgressBy(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
