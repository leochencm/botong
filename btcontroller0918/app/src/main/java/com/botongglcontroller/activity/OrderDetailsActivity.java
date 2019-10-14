package com.botongglcontroller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.botongglcontroller.adapter.OrderDetailsAdapter;
import com.botongglcontroller.adapter.RecycleViewDivider;
import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.GsonTools;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.GetBilldetail;
import com.botongglcontroller.beans.Standard;
import com.botongglcontroller.pay.AuthResult;
import com.botongglcontroller.pay.PayResult;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 订单详情
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    ImageView mBack;
    RecyclerView recycleContent;
    ArrayList<GetBilldetail> list;
    ArrayList<Standard> standardslist;
    OrderDetailsAdapter adapter;
    TextView chargestandard, lijizhifu;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String rsaPrivate = "";
    private String subject,orderNo,totalAmount;

    RadioButton rb_wx,rb_zfb;
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        chargestandard = (TextView) findViewById(R.id.chargestandard);
        recycleContent = (RecyclerView) findViewById(R.id.swipe_target);
        lijizhifu = (TextView) findViewById(R.id.lijizhifu);
        rb_wx = (RadioButton) findViewById(R.id.rb_wx);
        rb_zfb = (RadioButton) findViewById(R.id.rb_zfb);
        findViewById(R.id.lijizhifu).setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleContent.setLayoutManager(linearLayoutManager);
        recycleContent.setItemAnimator(new DefaultItemAnimator());
        recycleContent.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 6, getResources().getColor(R.color.grey_dark)));
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        orderNo = intent.getStringExtra("orderNo");
        totalAmount = intent.getStringExtra("totalAmount");
        getGetBilldetail(intent.getStringExtra("billid"));
        initKey();
    }

    private void getGetBilldetail(String billid) {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", MyApplication.sp.GetMobile());
            ps.put("billid", billid);
            MyApplication.http.post(Api.GetBilldetail, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.e("账户账单信息", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            list = new ArrayList<GetBilldetail>();
                            JSONArray array = object.getJSONArray("data");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject ob = array.getJSONObject(i);
                                    GetBilldetail getBill = GsonTools.changeGsonToBean(ob.toString(), GetBilldetail.class);
                                    list.add(getBill);
                                }
                                initContentAdapter();
                            } else {

                            }
                            JSONArray standard = object.getJSONArray("standard");
                            standardslist=new ArrayList<Standard>();
                            if (standard.length() > 0) {
                                for (int i = 0; i < standard.length(); i++) {
                                    JSONObject ob = standard.getJSONObject(i);
                                    Standard standard1 = GsonTools.changeGsonToBean(ob.toString(), Standard.class);
                                    standardslist.add(standard1);
                                }
                                Log.e("standardslist",""+standardslist.size());
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

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        chargestandard.setOnClickListener(this);
        rb_zfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb_wx.setChecked(false);
                }

            }
        });
        rb_wx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb_zfb.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.chargestandard:
                Bundle bundle=new Bundle();
                bundle.putSerializable("standardslist",standardslist);
                Intents.getIntents().Intent(this,ChargestandardActivity.class,bundle);

                break;
            case R.id.lijizhifu:
                if(rb_zfb.isChecked())
                {
                    //支付宝支付
                    payV2();
                }
                if(rb_wx.isChecked())
                {
                    Toast.makeText(OrderDetailsActivity.this, "weixin", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    /**
     * 支付宝支付业务
     *
     */
    public void payV2() {
        if (TextUtils.isEmpty(rsaPrivate) || (TextUtils.isEmpty(rsaPrivate) && TextUtils.isEmpty(rsaPrivate))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();
                        }
                    }).show();
            return;
        }

        final String orderInfo = rsaPrivate;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailsActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void initKey()
    {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("body", "锅炉云管家维护费用");
            ps.put("subject", subject);
            ps.put("orderNo", orderNo);
            ps.put("totalAmount", totalAmount);
            showLoadingDialog();
            MyApplication.http.post(Api.aliPayOrder, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            JSONObject object1 = object.getJSONObject("data");
                            rsaPrivate = object1.getString("data").toString();
                        } else {

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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderDetailsActivity.this, BillmanagementActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(OrderDetailsActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(OrderDetailsActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    //设置数据
    private void initContentAdapter() {
        if (adapter == null) {
            adapter = new OrderDetailsAdapter(this, list);
            recycleContent.setAdapter(adapter);
        } else {
            adapter.setDatas(list);
        }
    }
//支付回掉
    private void PayBill() {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
            ps.put("billid", "");
            ps.put("payway", "");
            showLoadingDialog();
            MyApplication.http.post(Api.PayBill, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                }
            });
    }


}
