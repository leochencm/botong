package com.botongglcontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * 求助
 */
public class ForhelpActivity extends BaseActivity {
    private static final int MAX_COUNT = 250;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    ImageView mBack;
    ImageView mScan;
    EditText mSuggest;
    TextView mPublish;
    TextView mBoilerNumber, mNum;
    private TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = mSuggest.getSelectionStart();
            editEnd = mSuggest.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            mSuggest.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // mEditText.setText(s);将这行代码注释掉就不会出现后面所说的输入法在数字界面自动跳转回主界面的问题了，多谢@ainiyidiandian的提醒
            mSuggest.setSelection(editStart);

            // 恢复监听器
            mSuggest.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_forhelp;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        mScan = (ImageView) findViewById(R.id.img_scan);
        mSuggest = (EditText) findViewById(R.id.edt_suggest);
        mPublish = (TextView) findViewById(R.id.txt_publish);
        mBoilerNumber = (TextView) findViewById(R.id.boilernumber);
        mNum = (TextView) findViewById(R.id.txt_num);

        mBoilerNumber.setText(getIntent().getStringExtra("Serialnumber"));
        mSuggest.addTextChangedListener(mTextWatcher);
        mSuggest.setSelection(mSuggest.length()); // 将光标移动最后一个字符后
        setLeftCount();
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_scan:
                Intent intent = new Intent();
                intent.setClass(this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.img_back:
                finish();
                break;

            case R.id.txt_publish:
                publish();

                break;

        }
    }

    private void publish() {
        if (TextUtils.isEmpty(mSuggest.getText())) {
            ToastUtil.showToast(this, "求助问题描述不可为空！");
            return;
        }
        if (TextUtils.isEmpty(mBoilerNumber.getText())) {
            ToastUtil.showToast(this, "请扫描获取锅炉编号！");
            return;
        }
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", MyApplication.sp.GetAccountName());
            ps.put("boiler", mBoilerNumber.getText().toString());
            ps.put("remark", mSuggest.getText().toString());
            showLoadingDialog();
            MyApplication.http.post(Api.SeekHelp, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("意见反馈", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            ToastUtil.showToast(ForhelpActivity.this, "上传成功！");
                            finish();
                        } else {
                            ToastUtil.showToast(ForhelpActivity.this, object.getString("message"));
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

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private void setLeftCount() {
        mNum.setText(String.valueOf((MAX_COUNT - getInputCount())) + "/250");
    }

    private long getInputCount() {
        return calculateLength(mSuggest.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    Log.e("扫描", bundle.getString("result"));
                    mBoilerNumber.setText(bundle.getString("result").split("\"")[0].trim());
                }
                break;
        }
    }
}
