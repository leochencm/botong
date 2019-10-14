package com.botongglcontroller.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class AlertteltphonenumTwoActivity extends BaseActivity {
    EditText mYzm, mTelephone;
    Button mGetyzm, mConfim;
    ImageView mBack;
    private CountDownTimer time;

    @Override
    public int getLayoutId() {
        return R.layout.altertelephonenumber2;
    }

    @Override
    public void initView() {
        mYzm = (EditText) findViewById(R.id.edt_yzm);
        mGetyzm = (Button) findViewById(R.id.btn_yzm);
        mConfim = (Button) findViewById(R.id.btn_confirm);
        mBack = (ImageView) findViewById(R.id.img_back);
        mTelephone = (EditText) findViewById(R.id.edt_telephone);
        time = new TimeCount(60000, 1000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mGetyzm.setOnClickListener(this);
        mConfim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:

                finish();
                break;
            case R.id.btn_yzm:

//获取验证码
                time.start();
                getYzm();

                break;
            case R.id.btn_confirm:

                confirm();

                break;


        }

    }

    private void confirm() {
        if (TextUtils.isEmpty(mYzm.getText())) {
            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "您还未输入验证码！");
            return;
        }
        AjaxParams params = new AjaxParams();
        try {
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("newMobile", mTelephone.getText().toString());
            params.put("id", MyApplication.sp.GetId());
            params.put("code", mYzm.getText().toString());
            showLoadingDialog();
            MyApplication.http.post(Api.ChangeAccount_NewVerifyCode, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("验证旧手机", o.toString());

                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "验证成功");
                            finish();

                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "验证码失效");
                        } else if (object.getString("resultcode").equals("2")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "验证码错误");
                        } else if (object.getString("resultcode").equals("3")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "设置失败");
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

    private void getYzm() {
        Log.i("新手机", "新手机");
        AjaxParams params = new AjaxParams();
        try {
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("id", MyApplication.sp.GetId());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("newMobile", mTelephone.getText().toString());
            MyApplication.http.post(Api.ChangeAccount_NewSendCode, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("旧手机", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "发送成功");

                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "发送失败");
                        } else if (object.getString("resultcode").equals("2")) {
                            ToastUtil.showToast(AlertteltphonenumTwoActivity.this, "手机号已被绑定");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mGetyzm.setText("重新获取");
            mGetyzm.setClickable(true);
            mGetyzm.setPressed(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mGetyzm.setClickable(false);
            mGetyzm.setPressed(true);
            mGetyzm.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }
    }
}
