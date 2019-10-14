package com.botongglcontroller.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.botongglcontroller.adapter.GuoluAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.db.BoilerManager;
import com.botongglcontroller.db.WifiManager;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.db.paraManager;
import com.botongglcontroller.onenetdb.DeviceItem;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.NetUtil;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.utils.WifiAdmin;
import com.botongglcontroller.activity.BaseActivity;
import com.botongglcontroller.activity.BindGuoluActivity;
import com.botongglcontroller.activity.ForhelpActivity;
import com.botongglcontroller.activity.GuolulinkActivity;
import com.botongglcontroller.activity.HomeActivity;
import com.botongglcontroller.activity.MainActivity;
import com.botongglcontroller.activity.MipcaActivityCapture;
import com.botongglcontroller.activity.OperatingconditionsActivity;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.dialog.Popwindow;
import com.botongglcontroller.view.PullToRefreshView;
import com.botongglcontroller.wifi.WIFIContron;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.botongglcontroller.view.PullToRefreshView.OnFooterRefreshListener;
import com.botongglcontroller.view.PullToRefreshView.OnHeaderRefreshListener;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuolulistActivity extends BaseActivity implements
        OnHeaderRefreshListener, OnFooterRefreshListener {
    private final String TAG = getClass().getName();
    ListView mListView;
    GuoluAdapter mAdapter;
    ImageView mAdd;
    private long touchTime = 0;
    private final static int SCANNIN_GREQUEST_CODE = 2;
    private List<Boilers> list ;
    private View parentView;
    private Popwindow pop = null;
    private PopupWindow pop3;
    private WIFIContron wifiContron;
    private WifiAdmin admin;
    WifiManager mg;
    private BoilerManager mgr;
    private paraManager bpara;
    String serialnumber;
    RadioButton mGuolu, mNews, mSuggestion, mPersonal;
    RadioGroup MyrGroup;
    final boolean isconnect = false;
    String UID = "";
    PullToRefreshView main_pull_refresh_view;
    public boolean onenetstat=false;
    public static int REFRESH = 1;
    int currentModel = REFRESH;
    public static GuolulistActivity instance = null;
    private List<DeviceItem> mDeviceItem=new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.guolulist;
    }

    @Override
    public void initView() {
        main_pull_refresh_view = (PullToRefreshView) findViewById(R.id.system_main_pull_refresh_view);
        main_pull_refresh_view.setOnHeaderRefreshListener(this);
        main_pull_refresh_view.setOnFooterRefreshListener(this);
        instance = this;
        MyrGroup = (RadioGroup) findViewById(R.id.rg_main);
        mGuolu = (RadioButton) findViewById(R.id.rb_guolu);
        mNews = (RadioButton) findViewById(R.id.rb_news);
        mSuggestion = (RadioButton) findViewById(R.id.rb_suggestion);
        mPersonal = (RadioButton) findViewById(R.id.rb_personal);
    }

    @Override
    public void initData() {
        mGuolu.setChecked(true);
        IntentFilter filter8 = new IntentFilter("com.broadcast.refresh3");
        registerReceiver(Refresh, filter8);
        //mg = new WifiManager(this);
        mgr = new BoilerManager(this);
        bpara=new paraManager(this);
        wifiContron = new WIFIContron(this);
        admin = new WifiAdmin(this);
        mListView = (ListView) findViewById(R.id.lv_guolu);
        mAdd = (ImageView) findViewById(R.id.img_add);
        //检查网络状况，如果有网则链接后台数据
        list=new ArrayList<Boilers>();
        mAdapter = new GuoluAdapter(GuolulistActivity.this, list);
        mListView.setAdapter(mAdapter);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(GuolulistActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String wifiname = "", conwifi = "";
                Bundle bd=new Bundle();
                conwifi = NetUtil.getSSID(GuolulistActivity.this).substring(1, NetUtil.getSSID(GuolulistActivity.this).length() - 1);
                final TextView sonview = (TextView) view.findViewById(R.id.txt_serialnumber);
                TextView txt_workstate = (TextView) view.findViewById(R.id.txt_workstate);
                //  CRC IBM校验唯一ID号,判断成功之后才允许进入
                //wifiname = "BT-" + CRC16Standard.DoCRCIBM(sonview.getText().toString());
                wifiname = list.get(i).getWifiname();
                System.out.println("wifi名称：" + conwifi + "  wifiname:  " + wifiname);
                //Log.i("点击的锅炉", sonview.getText().toString() + "\\" + list.get(i).getShowenable());
                //链接硬件是才进行判断，并且通过校验判断当前WIFI是当前的锅炉
                if(wifiname == null)
                {
                    wifiname = "";
                }
                if (txt_workstate.getText().equals("不在线") && (!wifiname.equals(conwifi)))
                {
                    showStringToastMsg("设备不在线，请确认设备在线再操作!");
                }
                else if (txt_workstate.getText().equals("不在线") && (wifiname.equals(conwifi)))
                {
                    if (!MyApplication.sp.GetNetWorkState().equals("2"))
                    {
                        showStringToastMsg("请检查网络或等待网络稳定!");
                        return;
                    }
                    bd.putSerializable("boiler",(Serializable) list.get(i).getPara());
                    Intent intent = new Intent(GuolulistActivity.this, OperatingconditionsActivity.class);

                    intent.putExtra("id", "3");
                    //intent.putExtra("serialnumber", sonview.getText().toString());
                    //intent.putExtra("showenable", list.get(i).getShowenable());
                    intent.putExtras(bd);
                    //intent.putExtra("mDevice",mDeviceItem.get(i).getId());
                    startActivityForResult(intent, 0);
                    MyApplication.sp.setShowenable(sonview.getText().toString());

                } else {
                    Intent intent = new Intent(GuolulistActivity.this, OperatingconditionsActivity.class);
                    intent.putExtra("id", "3");
                    bd.putSerializable("boiler",list.get(i).getPara());
                    intent.putExtras(bd);
                    intent.putExtra("serialnumber", list.get(i).getSerialnumber());
                    //intent.putExtra("showenable", list.get(i).getShowenable());
                    intent.putExtra("oid",list.get(i).getOid());
                    intent.putExtra("apikey",list.get(i).getApikey());
                    intent.putExtra("connstate",list.get(i).getConnstate());
                    intent.putExtra("model",list.get(i).getModel());
                    intent.putExtra("image",list.get(i).getImage());
                    startActivityForResult(intent, 0);
                    //MyApplication.sp.setShowenable(sonview.getText().toString());
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //final TextView sonview = (TextView) view.findViewById(R.id.txt_serialnumber);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View popupWindowView = inflater.inflate(R.layout.pop_3, null);
                pop3 = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                pop3.setBackgroundDrawable(new ColorDrawable());
                pop3.setOutsideTouchable(true);
                pop3.setAnimationStyle(R.style.PopupWindowAnimation);
                pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                });
                final Button unbind = (Button) popupWindowView.findViewById(R.id.unbind);
                final Button cancel = (Button) popupWindowView.findViewById(R.id.cancel);
                final Button forhelp = (Button) popupWindowView.findViewById(R.id.forhelp);
                unbind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(GuolulistActivity.this).setTitle("温馨提示").setMessage("确定解除绑定？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which){
                                        dialog.dismiss();
                                        unBind(i);
                                        pop3.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which){
                                        dialog.dismiss();
                                    }
                        }).show();

                    }
                });
                forhelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop3.dismiss();
                        Bundle  bundle=new Bundle();
                        bundle.putString("Serialnumber", list.get(i).getSerialnumber());
                        Intents.getIntents().Intent(GuolulistActivity.this, ForhelpActivity.class,bundle);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop3.dismiss();
                    }
                });
                pop3.showAsDropDown(view);

                return true;
            }
        });
    }

    private void changeWifi(final String s, final String showenable) {
        LemonHello.getInformationHello("链接锅炉，需要切换到锅炉网络", "")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("切换", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        Intent intent = new Intent();
                        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                        startActivityForResult(intent, 1);
                        helloView.hide();
                    }

                }
                ))
                .show(GuolulistActivity.this);
    }

    @Override
    public void initListener() {
        MyrGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                Bundle bundle = new Bundle();
                switch (arg1) {
                    case R.id.rb_home:
                        Intents.getIntents().Intent(GuolulistActivity.this, HomeActivity.class);
                        finish();
                        break;
                    case R.id.rb_guolu:
                        Intents.getIntents().Intent(GuolulistActivity.this, GuolulistActivity.class);
                        break;
                    case R.id.rb_news:
                        bundle.putInt("news", 2);
                        Intents.getIntents().Intent(GuolulistActivity.this, MainActivity.class, bundle);
                        finish();
                        break;
                    case R.id.rb_suggestion:
                        bundle.putInt("suggestion", 3);
                        Intents.getIntents().Intent(GuolulistActivity.this, MainActivity.class, bundle);
                        finish();
                        break;
                    case R.id.rb_personal:
                        bundle.putInt("personal", 4);
                        Intents.getIntents().Intent(GuolulistActivity.this, MainActivity.class, bundle);
                        finish();
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    BroadcastReceiver Refresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");
            CheckNetWork();

        }
    };

    private void CheckNetWork() {
        AjaxParams params = new AjaxParams();
        MyApplication.http.configTimeout(2000);
        MyApplication.http.post(Api.TestNetWork, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.e("Internet status=", o.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        MyApplication.sp.setnetWorkState("1");
                        //mgr.clearboiler();
                        showLoadingDialog();
                        getDate();
                    } else {
                        query();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                MyApplication.sp.setnetWorkState("2");
                query();
            }
        });

    }
    private void getDeviceItems() {
        Map<String, String> urlParams = new HashMap<>();
        for(int i=0;i<list.size();i++)
            urlParams.put("devIds",list.get(i).oid);
        //urlParams.put("devIds", "47330277");//47330277,503445994
        //urlParams.put("devIds", "503445994");
        OneNetApi.queryMulDevice(urlParams, new OneNetApiCallback() {
            @Override
            public void onSuccess(String response) {
                mDeviceItem.clear();
                JsonObject resp = new JsonParser().parse(response).getAsJsonObject();
                int errno = resp.get("errno").getAsInt();
                if (0 == errno) {
                    boolean a= resp.get("data").isJsonObject();
                    JsonObject ss=resp.get("data").getAsJsonObject();
                   // JsonObject resp1 = new JsonParser().parse(ss).getAsJsonObject();

                    JsonElement dataElement = ((JsonObject) ss).get("devices");//.get("devices");
                    if (dataElement != null) {
                        JsonArray jsonArray = dataElement.getAsJsonArray();
                        Gson gson = new Gson();
                        for (JsonElement element : jsonArray) {
                            mDeviceItem.add(gson.fromJson(element, DeviceItem.class));
                        }
                        if(mDeviceItem.get(0).isOnline())list.get(0).setConnstate("1");
                        else list.get(0).setConnstate("0");

                    }
                } else {
                    String error = resp.get("error").getAsString();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
                mgr.add(list);
                mAdapter.updateView(GuolulistActivity.this, list);
                main_pull_refresh_view.onHeaderRefreshComplete();
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }
//后台获取锅炉列表
    private void getDate() {
        //mgr.clearboiler();
        showLoadingDialog();
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("account", MyApplication.sp.GetMobile());
            MyApplication.http.post(Api.GetBoilers, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    hideLoadingDialog();
                    super.onSuccess(o);
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            list.clear();
                            //bpara.clearboiler();
                            JSONArray oba = object.getJSONArray("data");
                            for (int i = 0; i < oba.length(); i++) {
                                JSONObject ob = oba.getJSONObject(i);
                                Boilers boilers = new Boilers();
                                ArrayList<BoilersPara> listpara=new ArrayList<BoilersPara>();
                                boilers.setSerialnumber(ob.getString("serialnumber"));
                                boilers.setModel(ob.getString("model"));
                                //boilers.setWorkstate(ob.getString("workstate"));
                                boilers.setOid(ob.getString("oid"));
                               boilers.setConnstate(ob.getString("connstate"));
                               boilers.setApikey(ob.getString("apikey"));
                                //ob.getString()
                                //if(onenetstat)boilers.setConnstate("在线");
                                //else boilers.setConnstate("不在线");
                                boilers.setImage(ob.getString("image"));
                                //boilers.setShowenable(ob.getJSONArray("showenable").toString());
                                //boilers.setWifiname(ob.getString("wifiname"));
                                JSONArray para = oba.getJSONObject(i).getJSONArray("para");
                                for(int j=0;j<para.length();j++)
                                {
                                    JSONObject op = para.getJSONObject(j);
                                    BoilersPara bp = new BoilersPara();
                                    bp.setAddr(op.getString("addr"));
                                    bp.setAddr_int(op.getString("paraaddr"));
                                    bp.setName(op.getString("name"));
                                    bp.setKind(op.getString("parakind"));
                                    bp.setLen(op.getString("paralen"));
                                    bp.setVisiable(op.getString("visiable"));
                                    bp.setUnit(op.getString("unit"));
                                    bp.setModel(boilers.getModel());
                                    listpara.add(bp);
                                }
                                bpara.deletepara(boilers.model);
                                bpara.add(listpara);
                                boilers.setPara(listpara);
                                list.add(boilers);
                                mgr.deletenews(boilers.serialnumber);
                            }
                            mgr.add(list);
                            mAdapter.updateView(GuolulistActivity.this, list);
                            main_pull_refresh_view.onHeaderRefreshComplete();
                        }
                        hideLoadingDialog();
                       // getDeviceItems();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideLoadingDialog();
                    query();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }





    }
//从本地数据库中查询保存的锅炉列表
    public void query() {
        if (!mgr.query().isEmpty()) {
            list.clear();
            list = mgr.query();
            mAdapter.updateView(GuolulistActivity.this, list);
            main_pull_refresh_view.onHeaderRefreshComplete();
        }
    }

    private void unBind(final int i) {
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("account", MyApplication.sp.GetMobile());
            params.put("serialnumber", list.get(i).getSerialnumber());
            MyApplication.http.post(Api.RelieveBindBoiler, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("解绑", o.toString());
                    JSONObject object = null;
                    try {
                        object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(GuolulistActivity.this, "解绑成功");
                            Boilers boiler = new Boilers();
                            boiler.serialnumber = list.get(i).getSerialnumber();
//                            mg.deletewifi(boiler);
                            mgr.deletenews(boiler.serialnumber);
                            list.remove(i);
                            mAdapter.updateView(GuolulistActivity.this, list);
                        } else {
                            ToastUtil.showToast(GuolulistActivity.this, "解绑失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    ToastUtil.showToast(GuolulistActivity.this, "解绑失败");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(Refresh);
//        getActivity().unregisterReceiver(alldata);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - touchTime > 1500) {
                ToastUtil.showToast(GuolulistActivity.this, "再按一次退出应用");
                touchTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
//                //来自按钮1的请求，作相应业务处理
//                Log.i("qwqwq","wqwqw");
//                Log.e("serialnumber", "" + serialnumber);
//                Intent intent1 = new Intent(GuolulistActivity.this, OperatingconditionsActivity.class);
////                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent1.putExtra("id", "1");
//                intent1.putExtra("serialnumber", serialnumber);
//                startActivityForResult(intent1, 0);
                break;
            case 0:
                Log.i("default", "wqwqw");
//                UID = bundle.getString("UID");
//                Log.i("返回的UID", UID);
//                CheckNetWork();
                break;
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    Log.i("扫描", bundle.getString("result"));
                    Log.i("扫描", MyApplication.sp.GetIdentity());
                    bundle.putString("result", bundle.getString("result"));
                    if (MyApplication.sp.GetIdentity().equals("seller")) {
                        String[] info = bundle.getString("result").split("\\s+");
                        bundle.putString("serialnumber", info[0].toString());
                        bundle.putString("name", info[1].substring(1, info[1].length() - 1));
                        Intents.getIntents().Intent(GuolulistActivity.this, GuolulinkActivity.class, bundle);
                    } else if (MyApplication.sp.GetIdentity().equals("user")) {
                        Intents.getIntents().Intent(GuolulistActivity.this, BindGuoluActivity.class, bundle);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckNetWork();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        CheckNetWork();
        main_pull_refresh_view.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        CheckNetWork();
        currentModel = REFRESH;
    }

  /*  public void updataui() {
        main_pull_refresh_view.onHeaderRefreshComplete();
        main_pull_refresh_view.onFooterRefreshComplete();
    }*/
}
