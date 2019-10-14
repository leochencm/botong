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

public class RegisterActivity extends BaseActivity {
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";
    Button mGetyzm, mReg;
    ImageView mBack;
    EditText mCompanyname, mUsername, mAdress, mTelephoneNum, mYzm, mPwd;
    private CountDownTimer time;

    @Override
    public int getLayoutId() {
        return R.layout.register;
    }

    @Override
    public void initView() {
        mGetyzm = (Button) findViewById(R.id.btn_yzm);
        mReg = (Button) findViewById(R.id.btn_reg);
        mBack = (ImageView) findViewById(R.id.img_back);
        mCompanyname = (EditText) findViewById(R.id.edt_companyname);
        mUsername = (EditText) findViewById(R.id.edt_username);
        mAdress = (EditText) findViewById(R.id.edt_adress);
        mTelephoneNum = (EditText) findViewById(R.id.edt_telephonenum);
        mYzm = (EditText) findViewById(R.id.edt_yzm);
        mPwd = (EditText) findViewById(R.id.edt_pwd);

        time = new TimeCount(60000, 1000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mGetyzm.setOnClickListener(this);
        mReg.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yzm:
//获取验证码
                getYzm();
                break;
            case R.id.btn_reg:
                //注册
                if (TextUtils.isEmpty(mCompanyname.getText())) {
                    ToastUtil.showToast(this, "请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(mUsername.getText())) {
                    ToastUtil.showToast(this, "请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(mAdress.getText())) {
                    ToastUtil.showToast(this, "请输入联系地址");
                    return;
                }
                if (TextUtils.isEmpty(mTelephoneNum.getText())) {
                    ToastUtil.showToast(this, "请输入手机号码");
                    return;
                }
                if (TextUtils.isEmpty(mYzm.getText())) {
                    ToastUtil.showToast(this, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(mPwd.getText())) {
                    ToastUtil.showToast(this, "请输入密码");
                    return;
                }

                if (UserHelp.isNetworkConnected(RegisterActivity.this)) {
                    Register();
                } else {
                    ToastUtil.showToast(RegisterActivity.this, "当前网络不可用!");
                }

                break;
            case R.id.img_back:
                finish();
                break;


        }
    }

    private void Register() {
        AjaxParams params = new AjaxParams();
        params.put("screatname", MyApplication.sp.GetScreatMsg());
        params.put("screatword", UserHelp.getPosttime());
        try {
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("mobile", mTelephoneNum.getText().toString());
            params.put("code", mYzm.getText().toString());
            params.put("password", mPwd.getText().toString());
            params.put("companyname", mCompanyname.getText().toString());
            params.put("contactname", mUsername.getText().toString());
            params.put("address", mAdress.getText().toString());
            MyApplication.http.post(Api.Register_Password, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("注册", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(RegisterActivity.this, "注册成功");
                            MyApplication.sp.setCompanyname(mCompanyname.getText().toString());
                            MyApplication.sp.setContactname(mUsername.getText().toString());
                            MyApplication.sp.setAddress(mAdress.getText().toString());
                            MyApplication.sp.setMobile(mTelephoneNum.getText().toString());

                            finish();
                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(RegisterActivity.this, "验证码失效");
                        } else if (object.getString("resultcode").equals("2")) {
                            ToastUtil.showToast(RegisterActivity.this, "验证码错误");
                        } else if (object.getString("resultcode").equals("3")) {
                            ToastUtil.showToast(RegisterActivity.this, "注册失败");
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
        if (TextUtils.isEmpty(mTelephoneNum.getText())) {
            ToastUtil.showToast(RegisterActivity.this, "手机号不能为空");
            return;
        }

        if (UserHelp.isNetworkConnected(RegisterActivity.this)) {

            if (UserHelp.checkzhengze(mTelephoneNum, "^1\\d{10}$")) {
                AjaxParams ps = new AjaxParams();
                try {
                    Log.i("验证码sp", MyApplication.sp.GetScreatMsg() + "aaaaa");
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("mobile", mTelephoneNum.getText().toString());
                    MyApplication.http.post(Api.Register_Mobile, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            Log.i("验证码", o.toString());
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    Log.i("验证码", "发送成功");
                                    ToastUtil.showToast(RegisterActivity.this, "发送成功");
                                    time.start();
                                } else if (object.getString("message") != null) {
                                    ToastUtil.showToast(RegisterActivity.this, object.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtil.showToast(RegisterActivity.this, e.toString());
                            }

                        }

                        @Override
                        public void onFailure(Throwable t, String strMsg) {
                            super.onFailure(t, strMsg);
                        }

                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(RegisterActivity.this, e.toString());
                }
            } else {
                ToastUtil.showToast(RegisterActivity.this, "请输入正确的手机号码!");
            }
        } else {
            ToastUtil.showToast(RegisterActivity.this, "当前网络不可用!");
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
