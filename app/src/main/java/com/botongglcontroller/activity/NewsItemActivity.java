package com.botongglcontroller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.R;

public class NewsItemActivity extends BaseActivity {

    TextView mTitle, mMsg;
    ImageView mBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_item;
    }

    @Override
    public void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mMsg = (TextView) findViewById(R.id.msg);
        mBack = (ImageView) findViewById(R.id.img_back);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");
        mTitle.setText(title);
        mMsg.setText(msg);

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();

                break;
        }
    }
}
