package com.botongglcontroller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.botongglcontroller.R;


/**
 * 求助详细信息
 */
public class ForhelpInfoActivity extends BaseActivity {
    private ImageView mBack;
    private TextView contact_name, is_handler, submit_time, boilerNo, question_des, handresult, userid;
    private LinearLayout handresultL;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help_info;
    }

    @Override
    public void initView() {

        mBack = (ImageView) findViewById(R.id.img_back_info);
        contact_name = (TextView) findViewById(R.id.contact_name_info);
        is_handler = (TextView) findViewById(R.id.is_handler_info);
        submit_time = (TextView) findViewById(R.id.submit_time_info);
        boilerNo = (TextView) findViewById(R.id.boilerNo_info);
        question_des = (TextView) findViewById(R.id.question_des_info);
        handresult = (TextView) findViewById(R.id.handresult);
        userid = (TextView) findViewById(R.id.userid);
        handresultL = (LinearLayout) findViewById(R.id.handresultL);

        Intent intent = getIntent();
        userid.setText(intent.getStringExtra("userid"));
        contact_name.setText(intent.getStringExtra("contact_name"));
        is_handler.setText(intent.getStringExtra("is_handler"));
        submit_time.setText(intent.getStringExtra("submit_time"));
        boilerNo.setText(intent.getStringExtra("boilerNo"));
        question_des.setText(intent.getStringExtra("question_des"));
        String result = intent.getStringExtra("handresult");
        if (result != null && !result.equals("")) {
            handresult.setText(result);
            handresultL.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_info:
                finish();
                break;
        }
    }


}
