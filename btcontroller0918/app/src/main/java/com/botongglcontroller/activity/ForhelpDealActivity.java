package com.botongglcontroller.activity;

import android.content.Intent;
import android.text.Editable;
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
 *
 * 求助处理
 */
public class ForhelpDealActivity extends BaseActivity {
    private EditText mSuggest;
    private ImageView img_back;
    private TextView boilernumber,mNum,txt_publish;
    private String messageid;

    private static final int MAX_COUNT = 250;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forhelpdeal;
    }

    @Override
    public void initView() {
        txt_publish = (TextView) findViewById(R.id.txt_publish);
        img_back = (ImageView) findViewById(R.id.img_back);
        mNum = (TextView) findViewById(R.id.txt_num);
        mSuggest = (EditText) findViewById(R.id.edt_suggest);
        boilernumber = (TextView) findViewById(R.id.boilernumber);

        mSuggest.addTextChangedListener(mTextWatcher);
        mSuggest.setSelection(mSuggest.length()); // 将光标移动最后一个字符后
        setLeftCount();

        Intent intent = getIntent();
        boilernumber.setText(intent.getStringExtra("boilerNo"));
        messageid = intent.getStringExtra("id");
    }


    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        img_back.setOnClickListener(this);
        txt_publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_publish:
                try {
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("account", MyApplication.sp.GetAccountName());
                    ps.put("helpid", messageid);
                    ps.put("handresult", mSuggest.getText().toString());

                    showLoadingDialog();
                    MyApplication.http.post(Api.HandleHelp, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            hideLoadingDialog();
                            Log.i("求助提交", o.toString());
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    ToastUtil.showToast(ForhelpDealActivity.this, object.getString("message"));
                                } else {
                                    ToastUtil.showToast(ForhelpDealActivity.this, object.getString("message"));
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

                finish();
                break;
        }
    }

    private void setLeftCount() {
        mNum.setText(String.valueOf((MAX_COUNT - getInputCount())) + "/250");
    }
    private long getInputCount() {
        return calculateLength(mSuggest.getText().toString());
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

}
