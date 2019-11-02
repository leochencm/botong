package com.botongglcontroller.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.botongglcontroller.adapter.GuoluSeetingAdapter;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.beans.Str2Para;
import com.botongglcontroller.beans.String2Date;
import com.botongglcontroller.onenetdb.Sendcommand;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.beans.BoilersName;
import com.botongglcontroller.dialog.CustomDialog;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.Command;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.Thread;

public class SettingcanshuActivity extends BaseActivity {
    //    CheckBox btn;
    public static SettingcanshuActivity instance = null;
    static List<BoilersName> name = new ArrayList<BoilersName>();
    RelativeLayout mWifi, mLinkpwd, mLock, mDealer;
    ListView mListView, mListView1;
    ImageView mBack;
    List arraylist = new ArrayList();
    ArrayList<BoilersPara> list3 = new ArrayList<BoilersPara>();
    GuoluSeetingAdapter mAdapter1, mAdapter2;
    ArrayList<BoilersPara> list_bool = new ArrayList<BoilersPara>();//开关
    ArrayList<BoilersPara> list_analog = new ArrayList<BoilersPara>();//可修改的参数值
    String iswork;
    String mDevice;
    Boolean isclosed = false;
    int param[] = new int[106];
    SocketMessage socketMessage;
    boolean issend = false, suc = false;
    String intention = "";//判断是哪个广播
    String serialnumber;
    String addr = "";
    String apikey;
    MyThread mytr;
    BroadcastReceiver Refresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");
            String s = "refresh";
            //getDataStreams(s);
            list3.clear();
            list_analog.clear();
            list_bool.clear();
            list3 = (ArrayList<BoilersPara>) intent.getSerializableExtra("para");
            for (int i = 0; i < list3.size(); i++) {
                if (list3.get(i).getKind().equals("4") && list3.get(i).getVisiable().equals("1")) {
                    if (MyApplication.sp.Getisshoudong().equals("0")) {
                        if (Integer.parseInt(list3.get(i).getAddr_int()) < 16386)
                            list_bool.add(list3.get(i));
                    } else {
                        if (Integer.parseInt(list3.get(i).getAddr_int()) < 16386 || Integer.parseInt(list3.get(i).getAddr_int()) > 16388)
                            list_bool.add(list3.get(i));
                    }
                } else if (list3.get(i).getKind().equals("3") && list3.get(i).getVisiable().equals("1"))
                    list_analog.add(list3.get(i));
            }
            mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_bool, mDevice, apikey);
            mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_analog, mDevice, apikey);
            mListView.setAdapter(mAdapter1);
            mListView1.setAdapter(mAdapter2);
            //计算两个ListView的高度
            setListViewHeightBasedOnChildren(mListView);
            setListViewHeightBasedOnChildren(mListView1);

        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
            if (msg.what == 1) {
                if (!(progressDialg == null)) {
                    if (intention.equals("lockMachine")) {
                        hideLoadingDialog();
                        LemonBubble.showError(SettingcanshuActivity.this, "链接超时", 1000);
                    } else {
                        hideLoadingDialog();
                        LemonBubble.showError(SettingcanshuActivity.this, "链接超时,请重新连接锅炉！", 1000);
                        if (!MyApplication.sp.GetIdentity().equals("user")) {
                            finish();
                        }
                    }
                }
            } else if (msg.what == 2) {
                hideLoadingDialog();
            } else if (msg.what == 3) {
                if (!(progressDialg == null)) hideLoadingDialog();
                mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_bool, mDevice, apikey);
                mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_analog, mDevice, apikey);
                mListView.setAdapter(mAdapter1);
                mListView1.setAdapter(mAdapter2);
                //计算两个ListView的高度
                setListViewHeightBasedOnChildren(mListView);
                setListViewHeightBasedOnChildren(mListView1);
            }
            super.handleMessage(msg);
        }
    };
    private Devices devices;
    private Str2Para str2Para = new Str2Para();

//    BroadcastReceiver lock = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //锁机天数
//            isconnect = true;
//            MyApplication.sp.setsocketData(intent.getStringExtra("lockdate"));
//            MyApplication.sp.setfaconstate(intent.getStringExtra("isoperation"));
//            MyApplication.sp.setfacopenstate(intent.getStringExtra("isopen"));
//            MyApplication.sp.setlockstate(intent.getStringExtra("lockstate"));
//            MyApplication.sp.setisshoudong(intent.getStringExtra("isshoudong"));
//            if (MyApplication.sp.GetIdentity().equals("user")) {
//                //  mAdapter1.updateView(SettingcanshuActivity.this, list3, list1);
//                //  mAdapter2.updateView(SettingcanshuActivity.this, list3, list2);
//            }
//        }
//    };
//    BroadcastReceiver failure = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //锁机天数
//            isconnect = true;
//
//            if (MyApplication.sp.GetIdentity().equals("user")) {
//                // mAdapter1.updateView(SettingcanshuActivity.this, list3, list1);
//                // mAdapter2.updateView(SettingcanshuActivity.this, list3, list2);
//                Log.e("failure", "sfailurea" + list3.size() + name.size());
//            }
//        }
//    };
//    BroadcastReceiver alldata = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            isconnect = true;
//            ToastUtil.showToast(SettingcanshuActivity.this, "获取到锅炉最新数据");
//            hideLoadingDialog();
//            Log.i("已获取广播alldata", intent.getStringExtra("serialnumber"));
//            Intent intent2 = getIntent();
////            经销商界面无需显示开关
//
//        }
//    };
    //    BroadcastReceiver  changeSwichState = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            isconnect = true;
//            hideLoadingDialog();
//            if (intent.getStringExtra("state").equals("成功")) {
//                LemonBubble.showRight(context, "修改开关成功", 2000);
//            } else if (intent.getStringExtra("state").equals("失败")) {
//                LemonBubble.showError(context, "修改开关失败", 2000);
//                btn.setChecked(!btn.isChecked());
//            }
//        }
//    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
//    BroadcastReceiver notSwichStateChange = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            isconnect = true;
//            hideLoadingDialog();
//            if (intent.getStringExtra("state").equals("成功")) {
//                LemonBubble.showRight(SettingcanshuActivity.this, "修改参数成功", 2000);
//            } else if (intent.getStringExtra("state").equals("失败")) {
//                LemonBubble.showError(SettingcanshuActivity.this, "修改参数失败", 2000);
//            }
//
//        }
//    };

    @Override
    public int getLayoutId() {
        return R.layout.parameterdisplay;
    }

    @Override
    public void initView() {
        instance = this;
        //  socketMessage = new SocketService().getSocketMessage();
        //  devices = new Devices(new SocketService().getSocketMessage());
//        IntentFilter filter = new IntentFilter("com.broadcast.changeSwichState");
//        registerReceiver(changeSwichState, filter);
        //   IntentFilter filter2 = new IntentFilter("com.broadcast.notSwichStateChange");
        //  registerReceiver(notSwichStateChange, filter2);
        //   IntentFilter filter3 = new IntentFilter("com.broadcast.mymyalldata");
        //   registerReceiver(alldata, filter3);
//        IntentFilter filter4 = new IntentFilter("com.broadcast.locked");
//        registerReceiver(locked, filter4);
        IntentFilter filter5 = new IntentFilter("com.broadcast.refresh");
        registerReceiver(Refresh, filter5);

        //   IntentFilter filter7 = new IntentFilter("com.broadcast.failure");
        //   registerReceiver(failure, filter7);
        // mWifi = (RelativeLayout) findViewById(R.id.rl_wifi);
        //  mLinkpwd = (RelativeLayout) findViewById(R.id.rl_linkpwd);
        // mLock = (RelativeLayout) findViewById(R.id.rl_lock);
        //  mDealer = (RelativeLayout) findViewById(R.id.rl_dealer);
//        Switch = (RelativeLayout) findViewById(R.id.Switch);
        mListView = (ListView) findViewById(R.id.lv_kaiguan);
        mListView.addHeaderView(new ViewStub(this));
        mListView1 = (ListView) findViewById(R.id.lv_para);
        mListView1.addHeaderView(new ViewStub(this));
        mBack = (ImageView) findViewById(R.id.img_back);
        //设置焦点
        //  mWifi.setFocusable(true);
        // mWifi.setFocusableInTouchMode(false);
        // mWifi.requestFocus();


        //name=OperatingconditionsActivity.name;
        ///   name=OperatingconditionsActivity.list3;


    }

    private void getCanshunonet() {
        name = (ArrayList) getIntent().getSerializableExtra("list");
        /// name = OperatingconditionsActivity.name;
        ///  list3 = OperatingconditionsActivity.list3;
//        //list1为开关 list2为参数值
//         list1 = new ArrayList<BoilersName>();
//        list2 = new ArrayList<BoilersName>();

        // mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list3, list1);
        //  mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list3, list2);
        //  mListView.setAdapter(mAdapter1);
        //  mListView1.setAdapter(mAdapter2);
        //计算两个ListView的高度
        setListViewHeightBasedOnChildren(mListView);
        setListViewHeightBasedOnChildren(mListView1);
    }

    @Override
    public void initData() {
//设置经销商权限
        Log.e("tIdentity()", MyApplication.sp.GetIdentity());
//        if (MyApplication.sp.GetIdentity().equals("user")) {
//            mDealer.setVisibility(View.GONE);
//        } else if (MyApplication.sp.GetIdentity().equals("seller")) {
//            mDealer.setVisibility(View.VISIBLE);
//            mLinkpwd.setVisibility(View.GONE);
//        }
        Intent intent = getIntent();
        mDevice = intent.getStringExtra("mDevice");
        apikey = intent.getStringExtra("apikey");
        OneNetApi.setAppKey(apikey);
        serialnumber = intent.getStringExtra("serialnumber");
        list3 = (ArrayList<BoilersPara>) intent.getSerializableExtra("para");
        for (int i = 0; i < list3.size(); i++) {
            if (list3.get(i).getKind().equals("4") && list3.get(i).getVisiable().equals("1")) {
                if (MyApplication.sp.Getisshoudong().equals("0")) {
                    if (Integer.parseInt(list3.get(i).getAddr_int()) < 16386)
                        list_bool.add(list3.get(i));
                } else {
                    if (Integer.parseInt(list3.get(i).getAddr_int()) < 16386 || Integer.parseInt(list3.get(i).getAddr_int()) > 16388)
                        list_bool.add(list3.get(i));
                }
            } else if (list3.get(i).getKind().equals("3") && list3.get(i).getVisiable().equals("1"))
                list_analog.add(list3.get(i));
        }
        iswork = MyApplication.sp.GetNetWorkState();
        if (iswork.equals("2")) {
            //1为经销商权限时直接进入设置界面，2为其他界面
            if (intent.getStringExtra("id").equals("1")) {
                isclosed = true;
            } else {
                getCanshunonet();
            }
            if (!intent.getStringExtra("serialnumber").isEmpty()) {
                serialnumber = intent.getStringExtra("serialnumber");
            }
        } else {
            // showLoadingDialog();
            // getDataStreams("");
            mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_bool, mDevice, apikey);
            mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_analog, mDevice, apikey);
            mListView.setAdapter(mAdapter1);
            mListView1.setAdapter(mAdapter2);
            //计算两个ListView的高度
            setListViewHeightBasedOnChildren(mListView);
            setListViewHeightBasedOnChildren(mListView1);
        }
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        //  mWifi.setOnClickListener(this);
        //   mLock.setOnClickListener(this);
        //  mLinkpwd.setOnClickListener(this);
        //    mDealer.setOnClickListener(this);
//        Switch.setOnClickListener(this);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                //dialog参数设置
                final CustomDialog dialog = new CustomDialog(SettingcanshuActivity.this);
                final EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
                dialog.setTitle("是否修改参数 :" + list_analog.get(i - 1).getName());
                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int point = 0;
                        Log.i(".list3.size()", "" + list3.size());
                        int str = Integer.parseInt(editText.getText().toString());
                        if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
                            TextView text = (TextView) view.findViewById(R.id.txt_num);
                            if (!editText.getText().toString().equals(text.getText().toString())) {
                                text.setText(editText.getText());
                                int index = Integer.parseInt(list_analog.get(i - 1).getAddr_int()) - 12288;
                                showLoadingDialog();
                                sendCommand(Sendcommand.devicePara(index, str), list_analog.get(i - 1).getAddr_int(), "修改成功", "修改失败", text.getText().toString());
                            } else {
                                ToastUtil.showToast(SettingcanshuActivity.this, "参数未作修改，请修改后再操作！");
                            }
                            //changePara(String.valueOf(point), str, text);

                        } else if (MyApplication.sp.GetNetWorkState().equals("2")) {
                            if (socketMessage == null) {
                                ToastUtil.showToast(SettingcanshuActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                                return;
                            }
                            //devices.notSwichStateChange(point, Integer.valueOf(str));
                            showLoadingDialog();
                            new Thread(new MyThread()).start();
                        }
                        dialog.dismiss();
                    }

                });
                dialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                showStringToastMsg("item"+"第"+i+"个");
//                Log.e("ssss","item"+"第"+i+"个");
//                showLoadingDialog();
//                 btn = (CheckBox) view.findViewById(R.id.btn_para);
//                int point = 0;
//                String d = list2.get(i).getParakey().substring(9, list2.get(i).getParakey().length());
//                point = ((Integer.valueOf(d) - 1) % 16);
//                Log.i("第几个值", list2.get(i).getParakey() + "+d:" + d + "point:" + point);
//                String function = "";
//                if (Integer.valueOf(d) <= 16) {
//                    function = "0";
//                } else if (Integer.valueOf(d) > 16 && Integer.valueOf(d) <= 32) {
//                    function = "1";
//                } else if (Integer.valueOf(d) > 32 && Integer.valueOf(d) <= 48) {
//                    function = "2";
//                } else if (Integer.valueOf(d) > 48 && Integer.valueOf(d) <= 64) {
//                    function = "3";
//                } else if (Integer.valueOf(d) > 64 && Integer.valueOf(d) <= 80) {
//                    function = "4";
//                } else if (Integer.valueOf(d) > 80 && Integer.valueOf(d) <= 96) {
//                    function = "5";
//                } else {
//                    function = "6";
//                }
//                Log.i("第几个值", list2.get(i).getParakey() + "+d:" + d + "point:" + point + "function" + function);
//                if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
//
//                    try {
//                        AjaxParams params = new AjaxParams();
//                        params.put("screatname", MyApplication.sp.GetScreatMsg());
//                        params.put("screatword", UserHelp.getPosttime());
//                        params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
//                        params.put("serialnumber", MyApplication.sp.GetSerialnumber());
//                        params.put("function", function);
//                        params.put("points", String.valueOf(point));
//                        params.put("parametervalue", btn
//                                .isChecked() ? "1"
//                                : "0");
//                        Log.i("", params.toString());
//                        MyApplication.http.configTimeout(10000);
//                        MyApplication.http.post(Api.SetBoilersSwitches, params, new AjaxCallBack<Object>() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                hideLoadingDialog();
//                                super.onSuccess(o);
//                                Log.i("开关", o.toString());
//                                JSONObject object = null;
//                                try {
//                                    object = new JSONObject(o.toString());
//                                    if (object.getString("resultcode").equals("0")) {
//                                        LemonBubble.showRight(SettingcanshuActivity.this, "修改成功", 2000);
//                                    }  else if (object.getString("resultcode").equals("1")) {
//                                        LemonBubble.showError(SettingcanshuActivity.this, object.getString("message"), 1000);
//                                    }else {
//                                        LemonBubble.showError(SettingcanshuActivity.this, "修改失败", 2000);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t, String strMsg) {
//                                super.onFailure(t, strMsg);
//                                hideLoadingDialog();
////                                                       btn.setChecked(!btn.isChecked());
//                                LemonBubble.showError(SettingcanshuActivity.this, "修改失败", 2000);
//                            }
//                        });
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else if (MyApplication.sp.GetNetWorkState().equals("2")) {
////                                           if (socketMessage == null) {
////                                               ToastUtil.showToast(context, "您还未链接锅炉，请连接锅炉再操作！");
////                                               return;
////                                           }
//                    Log.i("开关", "" + Integer.valueOf(btn.isChecked() ? "1" : "0"));
//                    devices.changeSwichState(Integer.valueOf(d), Integer.valueOf(btn.isChecked() ? "1" : "0"));
//                    new Thread(new MyThread()).start();
//                }
//
//
//            }
//        });

    }

    //修改参数值
    private void sendCommand(byte[] by, final String str, final String toasttrue, final String toastfalse, String select) {
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
                            mytr.select = select;
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

    private void getDataPoint(final String streamid, final long dt, final String select) {
        //final boolean[] succ=new boolean[1];
        try {
            final Date start = new Date(System.currentTimeMillis());
            OneNetApi.querySingleDataStream(mDevice, streamid, new OneNetApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONObject json = resp.getJSONObject("data");
                            String dtstr = json.getString("update_at");
                            try {
                                if (json.getString("current_value").equals(select)) {
                                    mytr.stop();
                                    for (int i = 0; i < list_analog.size(); i++)
                                        if (list_analog.get(i).getAddr_int().equals(streamid))
                                            list_analog.get(i).setValue(json.getString("current_value"));
                                    hideLoadingDialog();
                                    LemonBubble.showRight(SettingcanshuActivity.this, "修改参数成功", 1000);
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
            // end=new Date(System.currentTimeMillis());
            // return end.getTime()-start.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        //   onDestroy();
        super.onBackPressed();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                finish();
                // onDestroy();

                break;
//            case R.id.Switch:
//                Intents.getIntents().Intent(SettingcanshuActivity.this, SwitchActivity.class);
//                break;
//            case R.id.rl_lock:
//                if (MyApplication.sp.Getfacopenstate().equals("0")) {
//                    ToastUtil.showToast(SettingcanshuActivity.this, "关机状态下无法操作！");
//                    return;
//                }
//                Intent intent=new Intent(SettingcanshuActivity.this, UnlockActivity.class);
//                intent.putExtra("oid",mDevice);
//                intent.putExtra("apikey",apikey);
//                startActivityForResult(intent,1);
//                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
//            String m=data.getStringExtra("mm");
//            for(int i=0;i<list_analog.size();i++)
//                if(list_analog.get(i).getAddr().equals("3000"))list_analog.get(i).setValue(m);
//            mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_bool, mDevice, apikey);
//            mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_analog, mDevice, apikey);
//            mListView.setAdapter(mAdapter1);
//            mListView1.setAdapter(mAdapter2);
//            //计算两个ListView的高度
//            setListViewHeightBasedOnChildren(mListView);
//            setListViewHeightBasedOnChildren(mListView1);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(Refresh);
        super.onDestroy();
    }

    private void getDataStreams(String s) {
        try {
            if (!s.equals("refresh")) {
                showLoadingDialog();
            }
            //name.clear();
            String[] streamid = new String[list3.size()];
            for (int i = 0; i < list3.size(); i++)
                streamid[i] = list3.get(i).getAddr_int();
            OneNetApi.setAppKey(apikey);
            OneNetApi.queryMultiDataStreams(mDevice, streamid, new OneNetApiCallback() {
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONArray json = resp.getJSONArray("data");
                            for (int i = 0; i < list3.size(); i++) {
                                list_analog.clear();
                                list_bool.clear();
                                JSONObject ob = json.getJSONObject(i);
                                try {
                                    list3.get(i).setTime(ob.getString("update_at"));
                                } catch (ParseException e) {

                                }
                                list3.get(i).setValue(ob.getString("current_value"));
                                if (list3.get(i).getKind().equals("3"))
                                    list_analog.add(list3.get(i));
                                else if (list3.get(i).getKind().equals("4"))
                                    list_bool.add(list3.get(i));
                            }
                            mAdapter1 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_bool, mDevice, apikey);
                            mAdapter2 = new GuoluSeetingAdapter(SettingcanshuActivity.this, list_analog, mDevice, apikey);
                            mListView.setAdapter(mAdapter1);
                            mListView1.setAdapter(mAdapter2);
                            //计算两个ListView的高度
                            setListViewHeightBasedOnChildren(mListView);
                            setListViewHeightBasedOnChildren(mListView1);

                            hideLoadingDialog();
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onFailed(Exception e) {
                    hideLoadingDialog();
                    LemonBubble.showError(SettingcanshuActivity.this, "链接失败,请检查网络！", 2000);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyThread implements Runnable {
        String str, toasttrue, toastfalse;
        long dt;
        int setflag = 1;
        boolean _run = true;
        String select = "0";

        public void setStr(String str) {
            this.str = str;
        }

        public void setDt(long dt) {
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

                        Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                        if (setflag > 0 && _run) {
                            getDataPoint(str, dt, select);
                            setflag = 0;
                        }
                    }
                    count++;

                    if (count > 10) {

                        Message msg = new Message();
                        msg.obj = str;//message的内容
                        msg.what = 3;//指定message
                        handler.sendMessage(msg);//handler发送message
                        _run = false;
                    }
                    //  if (_run) Thread.sleep(4000);// 线程暂停10秒，单位毫秒
                    //progressDialogwating.incrementProgressBy(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}