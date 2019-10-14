package com.botongglcontroller.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.botongglcontroller.adapter.BillManageAdapter;
import com.botongglcontroller.adapter.RecycleViewDivider;
import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.GsonTools;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.GetBill;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class BillmanagementActivity extends BaseActivity {

    ImageView mBack;
    RecyclerView recycleContent;
    RadioButton daizhifu, yizhifu;
    BillManageAdapter adapter;
    ArrayList<GetBill> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_billmanagement;
    }

    @Override
    public void initView() {
        mBack = (ImageView) findViewById(R.id.img_back);
        daizhifu = (RadioButton) findViewById(R.id.daizhifu);
        yizhifu = (RadioButton) findViewById(R.id.yizhifu);
        recycleContent = (RecyclerView) findViewById(R.id.swipe_target);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleContent.setLayoutManager(linearLayoutManager);
        recycleContent.setItemAnimator(new DefaultItemAnimator());
//        recycleContent.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
        recycleContent.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 6, getResources().getColor(R.color.grey_dark)));


    }

    @Override
    public void initData() {

        GetBill("0");
    }


    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        daizhifu.setOnClickListener(this);
        yizhifu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.daizhifu:
                GetBill("0");
                break;
            case R.id.yizhifu:
                GetBill("1");
                break;
        }
    }


    private void GetBill(String classify) {
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", MyApplication.sp.GetMobile());
            ps.put("classify", classify);
            Log.e("ps", ps.toString());
            showLoadingDialog();
            MyApplication.http.post(Api.GetBill, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.e("账户", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            list = new ArrayList<GetBill>();
                            JSONArray array = object.getJSONArray("data");
                            if (array.length() > 0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject ob = array.getJSONObject(i);
                                    GetBill getBill = GsonTools.changeGsonToBean(ob.toString(), GetBill.class);
                                    list.add(getBill);
                                }

                            } else {
                            }
                            initContentAdapter();
                        } else {

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

    //设置数据
    private void initContentAdapter() {
        if (adapter == null) {
            adapter = new BillManageAdapter(this, list);
            recycleContent.setAdapter(adapter);
        } else {
            adapter.setDatas(list);
        }
    }
}
