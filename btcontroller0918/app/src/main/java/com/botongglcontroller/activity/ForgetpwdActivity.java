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

public class ForgetpwdActivity extends BaseActivity {
    EditText mTelephone, mYzm, mNewPwd, mConfirmPwd;
    Button mGetyzm, mForgotpwd;
    private CountDownTimer time;
    ImageView mBack;
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @Override
    public int getLayoutId() {
        return R.layout.forgetpwd;
    }

    @Override
    public void initView() {
        mTelephone = (EditText) findViewById(R.id.edt_telephonenum);
        mYzm = (EditText) findViewById(R.id.edt_yzm);
        mNewPwd = (EditText) findViewById(R.id.edt_newpwd);
        mConfirmPwd = (EditText) findViewById(R.id.edt_confirmpwd);
        mGetyzm = (Button) findViewById(R.id.btn_yzm);
        mForgotpwd = (Button) findViewById(R.id.btn_forgotpwd);
mBack= (ImageView) findViewById(R.id.img_back);
        time = new TimeCount(60000, 1000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mGetyzm.setOnClickListener(this);
        mForgotpwd.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yzm:
                getYzm();


                break;
            case R.id.btn_forgotpwd:
                Register();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void getYzm() {
        if (TextUtils.isEmpty(mTelephone.getText())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "手机号不能为空");
        }
        if (UserHelp.isNetworkConnected(ForgetpwdActivity.this)) {

            if (UserHelp.checkzhengze(mTelephone, "^1\\d{10}$")) {
                try {
                    Log.i("验证码sp", MyApplication.sp.GetScreatMsg() + "aaaaa");
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("mobile", mTelephone.getText().toString());
                    MyApplication.http.post(Api.Recover_Mobile, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            Log.i("验证码", o.toString());
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    Log.i("验证码", "发送成功");
                                    time.start();
                                } else if (object.getString("resultcode").equals("1")) {
                                    ToastUtil.showToast(ForgetpwdActivity.this, "无此用户!");
                                } else if (object.getString("resultcode").equals("2")) {
                                    ToastUtil.showToast(ForgetpwdActivity.this, "保存失败!");
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
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showToast(ForgetpwdActivity.this, "请输入正确的手机号码!");
            }
        } else {
            ToastUtil.showToast(ForgetpwdActivity.this, "当前网络不可用!");
        }

    }

    private void Register() {

        if (TextUtils.isEmpty(mTelephone.getText().toString())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(mYzm.getText().toString())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(mNewPwd.getText().toString())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(mConfirmPwd.getText().toString())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "请输入确认密码");
            return;
        }
        if (!mNewPwd.getText().toString().equals(mConfirmPwd.getText().toString())) {
            ToastUtil.showToast(ForgetpwdActivity.this, "两次新密码不一致!");
            return;
        }
        if (UserHelp.isNetworkConnected(ForgetpwdActivity.this)) {
            AjaxParams params = new AjaxParams();
            try {
                params.put("screatname", MyApplication.sp.GetScreatMsg());
                params.put("screatword", UserHelp.getPosttime());
                params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                params.put("mobile", mTelephone.getText().toString());
                params.put("code", mYzm.getText().toString());
                params.put("password", mConfirmPwd.getText().toString());
                MyApplication.http.post(Api.Recover_Password, params, new AjaxCallBack<Object>() {

                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        hideLoadingDialog();
                        Log.i("找回密码", o.toString());
                        try {
                            JSONObject object = new JSONObject(o.toString());
                            if (object.getString("resultcode").equals("0")) {
                                ToastUtil.showToast(ForgetpwdActivity.this, "修改成功");
                                finish();
                            } else if (object.getString("resultcode").equals("1")) {
                                ToastUtil.showToast(ForgetpwdActivity.this, "验证码失效");
                            } else if (object.getString("resultcode").equals("2")) {
                                ToastUtil.showToast(ForgetpwdActivity.this, "验证码错误");
                            } else if (object.getString("resultcode").equals("3")) {
                                ToastUtil.showToast(ForgetpwdActivity.this, "设置失败");
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


        } else {
            ToastUtil.showToast(ForgetpwdActivity.this, "当前网络不可用!");
        }
        showLoadingDialog();


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
