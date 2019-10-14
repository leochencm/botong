package com.botongglcontroller.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.botongglcontroller.adapter.HelpAdapter;
import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.beans.Helper;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 求助列表
 */
public class ForhelpListActivity extends BaseActivity {
    private ListView help_list;
    private List<Helper> dataList = new ArrayList<Helper>();
    private ImageView mBack;
    private TextView mPublish;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forhelplist;
    }

    @Override
    public void initView() {
        help_list = (ListView) findViewById(R.id.help_list);

        mBack = (ImageView) findViewById(R.id.img_back);
        mPublish = (TextView) findViewById(R.id.txt_publish);

        // 隐藏提报求助
        if (MyApplication.sp.GetIdentity().equals("seller")) {
            mPublish.setVisibility(View.GONE);
        }

        // 求助列表item被点击事件
        help_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Helper task = dataList.get(position);

                Intent intent = new Intent(ForhelpListActivity.this, ForhelpInfoActivity.class);

                // 如果是经销商，并且未处理的消息
                if (MyApplication.sp.GetIdentity().equals("seller") && !task.getHandle().equals("1")) {
                    intent = new Intent(ForhelpListActivity.this, ForhelpDealActivity.class);
                }

                intent.putExtra("id", task.getId());
                intent.putExtra("contact_name", task.getContactname());
                intent.putExtra("is_handler", task.getHandle().equals("1") ? "已处理" : "未处理");
                intent.putExtra("submit_time", task.getCreatedate());
                intent.putExtra("boilerNo", task.getBoilerid());
                intent.putExtra("question_des", task.getRemark());
                intent.putExtra("handresult", task.getHandresult());
                intent.putExtra("userid", task.getUserid());


                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mPublish.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataList.clear();
        // 请求求助列表
        try {

            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("account", MyApplication.sp.GetAccountName());

            showLoadingDialog();
            MyApplication.http.post(Api.GetHelpinfo, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    hideLoadingDialog();
                    Log.i("求助列表", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                Helper h = new Helper();
                                h.setBoilerid(obj.getString("boilerid"));
                                h.setContactname(obj.getString("contactname"));
                                h.setCreatedate(obj.getString("createdate"));
                                h.setHandle(obj.getString("handle"));
                                h.setHandresult(obj.getString("handresult"));
                                h.setHandtime(obj.getString("handtime"));
                                h.setId(obj.getString("id"));
                                h.setRemark(obj.getString("remark"));
                                h.setUserid(obj.getString("userid"));

                                dataList.add(h);
                            }

                            HelpAdapter adapter = new HelpAdapter(ForhelpListActivity.this, R.layout.item_helplist, dataList);
                            help_list.setAdapter(adapter);

                        } else {
                            ToastUtil.showToast(ForhelpListActivity.this, object.getString("message"));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_publish:
                Intents.getIntents().Intent(ForhelpListActivity.this, ForhelpActivity.class);
                break;
        }
    }


}
