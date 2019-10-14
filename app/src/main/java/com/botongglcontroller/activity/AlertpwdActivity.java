package com.botongglcontroller.activity;

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

public class AlertpwdActivity extends BaseActivity {
    EditText mNewpwd, mOldpwd, mConfirmpwd;
    Button mConfim;
    ImageView mBack;

    @Override
    public int getLayoutId() {
        return R.layout.alertpwd;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        mConfim = (Button) findViewById(R.id.btn_confirm);
        mNewpwd = (EditText) findViewById(R.id.edt_newpwd);
        mOldpwd = (EditText) findViewById(R.id.edt_oldpwd);
        mConfirmpwd = (EditText) findViewById(R.id.edt_confirmpwd);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mConfim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_confirm:
                changePwd();
                break;
        }
    }

    private void changePwd() {
        if (TextUtils.isEmpty(mOldpwd.getText())) {
            ToastUtil.showToast(AlertpwdActivity.this, "您还未输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(mNewpwd.getText())) {
            ToastUtil.showToast(AlertpwdActivity.this, "您还未输入新密码！");
            return;
        }
        if (TextUtils.isEmpty(mConfirmpwd.getText())) {
            ToastUtil.showToast(AlertpwdActivity.this, "您还未输入确认密码！");
            return;
        }
        if (!TextUtils.equals(mNewpwd.getText(), mConfirmpwd.getText())) {
            ToastUtil.showToast(AlertpwdActivity.this, "您两次输入的密码不一致！");
            return;
        }

        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("mobile", MyApplication.sp.GetMobile());
            params.put("oldpassword", mOldpwd.getText().toString());
            params.put("newpassword", mNewpwd.getText().toString());
            showLoadingDialog();
            MyApplication.http.post(Api.ChangePassword, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    hideLoadingDialog();
                    super.onSuccess(o);
                    Log.i("修改密码", o.toString());
                    JSONObject object = null;
                    try {
                        object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(AlertpwdActivity.this, "修改成功");
                            finish();
                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(AlertpwdActivity.this, "无此用户");
                        } else if (object.getString("resultcode").equals("2")) {
                            ToastUtil.showToast(AlertpwdActivity.this, "旧密码错误");
                        } else if (object.getString("resultcode").equals("3")) {
                            ToastUtil.showToast(AlertpwdActivity.this, "保存失败");
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
}


