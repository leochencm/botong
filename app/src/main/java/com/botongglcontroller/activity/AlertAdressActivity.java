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

public class AlertAdressActivity extends BaseActivity {

    ImageView mBack;
    Button mConfim;
    EditText mAdress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_alert_adress;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        mConfim = (Button) findViewById(R.id.btn_confirm);
        mAdress = (EditText) findViewById(R.id.edt_company);

    }

    @Override
    public void initData() {
        mAdress.setText(MyApplication.sp.GetAddress());
        mAdress.setSelection(mAdress.getText().length());
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
                changeAdress();
                break;

        }
    }

    private void changeAdress() {
        if (TextUtils.isEmpty(mAdress.getText())) {
            ToastUtil.showToast(AlertAdressActivity.this, "您还未输入联系地址！");
            return;
        }

        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("mobile", MyApplication.sp.GetMobile());
            params.put("address", mAdress.getText().toString());
            showLoadingDialog();
            MyApplication.http.post(Api.ChangeAddress, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("地址", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(AlertAdressActivity.this, "修改成功");
                            MyApplication.sp.setAddress(mAdress.getText().toString());
                            finish();
                        } else if (object.getString("resultcode").equals("1")) {
                            ToastUtil.showToast(AlertAdressActivity.this, "修改失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideLoadingDialog();
                    ToastUtil.showToast(AlertAdressActivity.this, "修改失败");
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
