package com.botongglcontroller.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.botongglcontroller.adapter.ChargestandardAdapter;
import com.botongglcontroller.adapter.RecycleViewDivider;
import com.botongglcontroller.R;
import com.botongglcontroller.beans.Standard;

import java.util.ArrayList;

public class ChargestandardActivity extends BaseActivity {
    ImageView mBack;
    RecyclerView recycleContent;
    ArrayList<Standard> standardslist;
    ChargestandardAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_chargestandard;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        recycleContent = (RecyclerView) findViewById(R.id.swipe_target);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleContent.setLayoutManager(linearLayoutManager);
        recycleContent.setItemAnimator(new DefaultItemAnimator());
        recycleContent.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 6, getResources().getColor(R.color.grey_dark)));
    }

    @Override
    public void initData() {
         standardslist= (ArrayList<Standard>) getIntent().getSerializableExtra("standardslist");

        adapter = new ChargestandardAdapter(this, standardslist);
        recycleContent.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
