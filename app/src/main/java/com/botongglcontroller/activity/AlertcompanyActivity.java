package com.botongglcontroller.activity;

import android.text.TextUtils;
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

public class AlertcompanyActivity extends BaseActivity {

    Button mConfim;
    EditText mCompany;
    ImageView mBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_alertcompany;
    }

    @Override
    public void initView() {
        mCompany = (EditText) findViewById(R.id.edt_company);
        mConfim = (Button) findViewById(R.id.btn_confirm);
        mBack = (ImageView) findViewById(R.id.img_back);


    }

    @Override
    public void initData() {
        mCompany.setText(MyApplication.sp.GetCompanyname());
        mCompany.setSelection(mCompany.getText().length());
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
                changeCompany();
                break;

        }
    }

    private void changeCompany() {
        if (TextUtils.isEmpty(mCompany.getText())) {
            ToastUtil.showToast(AlertcompanyActivity.this, "您还未输入公司名称");
            return;
        }
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("mobile", MyApplication.sp.GetMobile());
            params.put("company", mCompany.getText().toString());
            showLoadingDialog();
            MyApplication.http.post(Api.ChangeCompany, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(AlertcompanyActivity.this, "修改成功");
                            MyApplication.sp.setCompanyname(mCompany.getText().toString());
                            finish();
                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(AlertcompanyActivity.this, "修改失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideLoadingDialog();
                    ToastUtil.showToast(AlertcompanyActivity.this, "修改失败");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
