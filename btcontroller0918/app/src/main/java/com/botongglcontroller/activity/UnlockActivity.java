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

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.beans.String2Date;
import com.botongglcontroller.onenetdb.Sendcommand;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.Command;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class UnlockActivity extends BaseActivity {

    ImageView mBack;
    Button mConfirm;
    EditText mPwd;
    private Devices devices;
    SocketMessage socketMessage;
    boolean isconnect = false;
    String intention = "";//判断是哪个广播
    CheckBox mBox;
    MyThread mytr;
    String oid,mima;
    @Override
    public int getLayoutId() {
        return R.layout.activity_unclock;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        mPwd = (EditText) findViewById(R.id.edt_pwd);
        mConfirm = (Button) findViewById(R.id.btn_confirm);
        mBox = (CheckBox) findViewById(R.id.cb);
    }

    @Override
    public void initData() {
        socketMessage = new SocketService().getSocketMessage();
        devices = new Devices(new SocketService().getSocketMessage());
        IntentFilter filter = new IntentFilter("com.broadcast.foreverOpen");
        registerReceiver(foreverOpen, filter);
        Intent it=getIntent();
        oid=it.getStringExtra("oid");
        OneNetApi.setAppKey(it.getStringExtra("apikey"));
    }

    BroadcastReceiver foreverOpen = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isconnect = true;
//            ToastUtil.showToast(UnlockActivity.this, "已获取广播foreverOpen" + intent.getStringExtra("state"));
            hideLoadingDialog();
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(UnlockActivity.this, "解锁成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(UnlockActivity.this, "解锁失败", 2000);
            }
        }
    };

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int passwordLength = mPwd.getText().length();
                mPwd.setInputType(isChecked ?
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) :
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                mPwd.setSelection(passwordLength);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if(mPwd.getText().toString()!="") {
                    Intent it = new Intent();
                    it.putExtra("mm", mPwd.getText().toString());
                    setResult(2, it);
                }
                finish();
                break;
            case R.id.btn_confirm:
                if (mPwd.getText().equals("") || TextUtils.isEmpty(mPwd.getText())) {
                    ToastUtil.showToast(UnlockActivity.this, "您还未填写解锁密码");
                    return;
                }
                if (!(MyApplication.sp.GetNetWorkState().equals("2"))) {
                    showLoadingDialog();
                    int str = Integer.parseInt(mPwd.getText().toString());
                    sendCommand(Sendcommand.devicePara(0,str),"12288","修改成功","修改失败",str);
                }   else {
                    if (socketMessage == null ||! MyApplication.sp.GetNetWorkState().equals("2")) {
                        ToastUtil.showToast(UnlockActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                        return;
                    }
                    //showLoadingDialog();
                    //int str = Integer.parseInt(mPwd.getText().toString());
                    //sendCommand(Sendcommand.devicePara(0,str),"12288","修改成功","修改失败");

                    //devices.foreverOpen(mPwd.getText().toString());
                   // new Thread(new MyThread()).start();
                }



                break;
        }
    }
    //修改参数值
    private void sendCommand(byte[] by, final String str, final String toasttrue, final String toastfalse,int flag) {
        OneNetApi.sendCmdToDevice(
                oid,
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

    public class MyThread implements Runnable {
        String str, toasttrue, toastfalse;
        int dt;
        boolean _run = true;

        public void setStr(String str) {
            this.str = str;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public void stop() {
            _run = false;
        }

        public void setToasttrue(String toasttrue) {
            this.toasttrue = toasttrue;
        }

        public void setToastfalse(String toastfalse) {
            this.toastfalse = toastfalse;
        }

        @Override
        public void run() {

            int count = 0;
            while (_run) {
                try {
                    if (_run) {
                        getDataPoint(str, dt);
                    }
                    count++;

                    if (count > 5) {

                        Message msg = new Message();
                        msg.obj = str;//message的内容
                        msg.what = 3;//指定message
                        handler.sendMessage(msg);//handler发送message
                        _run = false;
                    }
                    if (_run) Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    //progressDialogwating.incrementProgressBy(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void getDataPoint(final String streamid, final long dt) {
        //final boolean[] succ=new boolean[1];
        try {
            OneNetApi.querySingleDataStream(oid, streamid, new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONObject json = resp.getJSONObject("data");
                            try {
                                if(dt==0&&json.getString("current_value").equals("0")||dt==255&&json.getString("current_value").equals("255"))
                                {
                                    mytr.stop();
                                    hideLoadingDialog();

                                    LemonBubble.showRight(UnlockActivity.this, "修改参数成功", 1000);
                                }

                            } catch (Exception e) {
                            }
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailed(Exception e) {
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void OpenLock(String  pwd) {
        showLoadingDialog();
        AjaxParams params = new AjaxParams();
        try {
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("serialnumber", MyApplication.sp.GetSerialnumber());
            params.put("code", pwd);
            Log.i("params",""+params.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyApplication.http.configTimeout(12000);
        MyApplication.http.post(Api.OpenLock, params, new AjaxCallBack<Object>(){
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("params",""+o.toString());
                hideLoadingDialog();
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        LemonBubble.showRight(UnlockActivity.this,object.getString("message") , 2000);
                    }
                    else if(object.getString("resultcode").equals("-1")){
                        LemonBubble.showError(UnlockActivity.this, object.getString("message"), 2000);
                    }
                    else if(object.getString("resultcode").equals("1")){
                        LemonBubble.showError(UnlockActivity.this, object.getString("message"), 2000);
                    }
                    else if(object.getString("resultcode").equals("2")){
                        LemonBubble.showError(UnlockActivity.this, object.getString("message"), 2000);
                    }
                    else if(object.getString("resultcode").equals("3")){
                        LemonBubble.showError(UnlockActivity.this, object.getString("message"), 2000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                hideLoadingDialog();
                LemonBubble.showError(UnlockActivity.this, "设置失败,请检查网络！", 2000);
            }
        });


        }


    @Override
    protected void onDestroy() {
        unregisterReceiver(foreverOpen);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(mPwd.getText().toString()!="") {
            Intent it = new Intent();
            it.putExtra("mm", mPwd.getText().toString());
            setResult(2, it);
            finish();
           // super.onBackPressed();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                if (!(progressDialg == null)) {
                    hideLoadingDialog();
                    LemonBubble.showError(UnlockActivity.this, "链接超时", 1000);
                }
            } else if (msg.what == 2) {
                hideLoadingDialog();
            }
            else if(msg.what==3){
                hideLoadingDialog();
                mPwd.setText("");
            }
            super.handleMessage(msg);
        }
    };
}

