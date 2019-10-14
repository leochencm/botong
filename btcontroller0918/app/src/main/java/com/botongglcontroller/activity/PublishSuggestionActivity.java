package com.botongglcontroller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
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

public class PublishSuggestionActivity extends Fragment implements View.OnClickListener {
    ImageView mBack;
    EditText mSuggest;
    TextView mPublish, mNum;
    private static final int MAX_COUNT = 150;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.publish_suggestion, container, false);

        mBack = (ImageView) root.findViewById(R.id.img_back);
        mSuggest = (EditText) root.findViewById(R.id.edt_suggest);
        mPublish = (TextView) root.findViewById(R.id.txt_publish);
        mNum = (TextView) root.findViewById(R.id.txt_num);

        mBack.setOnClickListener(this);

        mBack.setOnClickListener(this);
        mPublish.setOnClickListener(this);

        mSuggest.addTextChangedListener(mTextWatcher);
        mSuggest.setSelection(mSuggest.length()); // 将光标移动最后一个字符后面
        setLeftCount();
        return root;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                Intents.getIntents().Intent(getActivity(), HomeActivity.class);
                break;
            case R.id.txt_publish:

                publish();
                break;


        }
    }

    private void publish() {
        if (TextUtils.isEmpty(mSuggest.getText())) {
            ToastUtil.showToast(getActivity(), "意见反馈不可为空！");
            return;
        }
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", MyApplication.sp.GetContactname());
            ps.put("suggestion", mSuggest.getText().toString());
            ps.put("contact", MyApplication.sp.GetMobile());
            MyApplication.http.post(Api.UploadSuggestion, ps, new AjaxCallBack<Object>() {

                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("意见反馈", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
//                            LemonBubble.showRight(PublishSuggestionActivity.this, "上传成功！", 2000);
                            ToastUtil.showToast(getActivity(), "发布成功！");
                            Intents.getIntents().Intent(getActivity(), HomeActivity.class);
                        } else {
                            ToastUtil.showToast(getActivity(), "发布失败！");
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
        mNum.setText(String.valueOf((MAX_COUNT - getInputCount())) + "/150");
    }

    private long getInputCount() {
        return calculateLength(mSuggest.getText().toString());
    }

}
