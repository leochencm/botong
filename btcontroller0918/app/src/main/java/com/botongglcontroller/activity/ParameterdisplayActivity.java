package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.botongglcontroller.adapter.GuoluCanshuAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.beans.Str2Para;
import com.botongglcontroller.utils.GsonTools;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.BoilersName;
import com.botongglcontroller.beans.BoilersPara;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
//import com.btcontroller.beans.Name;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParameterdisplayActivity extends BaseActivity {
    TextView mP;
    ImageView mBack;
    ListView mListview;
    List<BoilersName> name = new ArrayList<BoilersName>();
    ArrayList<BoilersPara> list3 = new ArrayList<BoilersPara>();
    GuoluCanshuAdapter mAdapter;
    String mDevice;
    int param[]=new int[105];
    String sendparam="";
    String apikey;
    private Str2Para str2Para=new Str2Para();
    @Override
    public int getLayoutId() {
        return R.layout.settingcanshu;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        mListview = (ListView) findViewById(R.id.lv_canshu);
        IntentFilter filter2 = new IntentFilter("com.broadcast.mymyalldata");
        registerReceiver(alldata, filter2);
        IntentFilter filter8 = new IntentFilter("com.broadcast.refresh");
        registerReceiver(Refresh, filter8);
        IntentFilter filter9 = new IntentFilter("com.broadcast.refresh2");
        registerReceiver(Refresh, filter9);
    }

    @Override
    public void initData() {
        if (MyApplication.sp.GetNetWorkState().equals("-1")) {

        } else if (MyApplication.sp.GetNetWorkState().equals("2")) {
///            mAdapter = new GuoluCanshuAdapter(ParameterdisplayActivity.this, OperatingconditionsActivity.list3, OperatingconditionsActivity.name);
            mListview.setAdapter(mAdapter);
        } else if (MyApplication.sp.GetNetWorkState().equals("0") || MyApplication.sp.GetNetWorkState().equals("1")) {
            Intent intent = getIntent();
            mDevice=intent.getStringExtra("mDevice");
            apikey=intent.getStringExtra("apikey");
            list3=(ArrayList<BoilersPara>)intent.getSerializableExtra("para");
           // getCnshu("");
           // getDataStreams("");
            mAdapter = new GuoluCanshuAdapter(ParameterdisplayActivity.this, list3);
            mListview.setAdapter(mAdapter);

        }
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_setting:
                Intent intent1 = new Intent(ParameterdisplayActivity.this, SettingcanshuActivity.class);
                intent1.putExtra("id", "2");
                startActivity(intent1);
                break;
        }

    }
    private void getDataStreams(final String s) {
        try {
            if (!s.equals("refresh")) {
                showLoadingDialog();
            }
            OneNetApi.querySingleDataStream(mDevice,"send_param", new OneNetApiCallback(){
                public void onSuccess(String response) {
                    try {
                        JSONObject resp = new JSONObject(response);
                        int errno = resp.getInt("errno");
                        if (0 == errno) {
                            JSONObject json = resp.getJSONObject("data");
                            sendparam=json.getString("current_value");
                            JSONObject jb=new JSONObject(sendparam);
                            sendparam=jb.getString("index");
                            OneNetApi.queryBinaryData(sendparam, new OneNetApiCallback() {
                                @Override
                                public void onSuccess(String response) {
                                    hideLoadingDialog();
                                    if(str2Para.S2P(response))
                                        param=str2Para.getPara();
                                    for(int i=0;i<105;i++)
                                    {
                                        //list3.get(i).setParavalue(String.valueOf(param[i]));
                                    }
                                    if (s.equals("refresh")) {
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                      ///  mAdapter = new GuoluCanshuAdapter(ParameterdisplayActivity.this, list3, OperatingconditionsActivity.name);
                                        mListview.setAdapter(mAdapter);
                                    }

                                }



                                @Override
                                public void onFailed(Exception e) {
                                    hideLoadingDialog();

                                }
                            });
                        }
                    }catch (JSONException e)
                    {

                    }
                }
                @Override
                public void onFailed(Exception e) {
                    hideLoadingDialog();
                    }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getCnshu(final String s) {
        list3.clear();
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("serialnumber", MyApplication.sp.GetSerialnumber());
            Log.i("params", params.toString());
            showLoadingDialog();
            MyApplication.http.post(Api.GetBoilersPara, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("canshu", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            JSONObject ob = object.getJSONObject("data");
                            JSONArray bolierparas = ob.getJSONArray("bolierparas");
                            JSONObject boilerstate = ob.getJSONObject("boilerstate");

                          //  MyApplication.sp.setfacopenstate(boilerstate.getString("facopenstate"));
                          //  MyApplication.sp.setfaconstate(boilerstate.getString("faconstate"));
                          //  MyApplication.sp.setisshoudong(boilerstate.getString("handenable"));
                            for (int i = 0; i < bolierparas.length(); i++) {
                                JSONObject obj = (JSONObject) bolierparas.get(i);
                                BoilersName boilers = GsonTools.changeGsonToBean(obj.toString(), BoilersName.class);
                                //list3.add(boilers);
                            }
                            if (s.equals("refresh")) {
                               mAdapter.notifyDataSetChanged();
                           } else {
///                                mAdapter = new GuoluCanshuAdapter(ParameterdisplayActivity.this, list3, OperatingconditionsActivity.name);
                                mListview.setAdapter(mAdapter);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideLoadingDialog();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    BroadcastReceiver alldata = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //链接SOCKET后才有SocketMessage
            ToastUtil.showToast(ParameterdisplayActivity.this, "已获取到最新数据");
            hideLoadingDialog();
//            UID = intent.getStringExtra("UID");
            Log.i("已获取广播alldata", intent.getStringExtra("serialnumber"));
            list3 = (ArrayList<BoilersPara>) intent.getSerializableExtra("para");
///            mAdapter.updateView(ParameterdisplayActivity.this, list3, OperatingconditionsActivity.name);
        }
    };
    BroadcastReceiver Refresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("刷新", "刷新");
            getCnshu("refresh");

        }
    };

    protected void onDestroy() {
        unregisterReceiver(alldata);
        unregisterReceiver(Refresh);
        super.onDestroy();
    }

}
