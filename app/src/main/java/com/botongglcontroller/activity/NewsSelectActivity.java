package com.botongglcontroller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.botongglcontroller.Api;
import com.botongglcontroller.db.DBManager;
import com.botongglcontroller.db.NewsData;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class NewsSelectActivity extends BaseActivity {

    private TextView msg_info;
    private ImageView mBack;
    private Button ok_info, cacel_info;
    private String id, UID, title, msg, date, type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_select;
    }

    @Override
    public void initView() {
        msg_info = (TextView) findViewById(R.id.msg_info);
        mBack = (ImageView) findViewById(R.id.img_back);
        ok_info = (Button) findViewById(R.id.ok_info);
        cacel_info = (Button) findViewById(R.id.cacel_info);


    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        UID = intent.getStringExtra("UID");
        title = intent.getStringExtra("title");
        msg = intent.getStringExtra("msg");
        date = intent.getStringExtra("date");
        type = intent.getStringExtra("type");

        msg_info.setText(msg);

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        ok_info.setOnClickListener(this);
        cacel_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ok_info:
                // 更新消息已读状态
                try {
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("Id", id);
                    MyApplication.http.post(Api.ReadInform, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    // 成功更新服务器后，再更新本地状态
                                    DBManager mgr = new DBManager(NewsSelectActivity.this);
                                    ArrayList<NewsData> persons = new ArrayList<NewsData>();
                                    NewsData person1 = new NewsData(id, UID, title, msg, date, type, "1");
                                    persons.add(person1);
                                    mgr.add(persons);
                                    showStringToastMsg(object.getString("message"));
                                } else {
                                    showStringToastMsg(object.getString("message"));
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

                try {
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("messageid", id);
                    ps.put("result", "1");
                    MyApplication.http.post(Api.ReplyAuthority, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                        }

                        @Override
                        public void onFailure(Throwable t, String strMsg) {
                            super.onFailure(t, strMsg);
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                finish();
                break;
            case R.id.cacel_info:
                try {
                    AjaxParams ps = new AjaxParams();
                    ps.put("screatname", MyApplication.sp.GetScreatMsg());
                    ps.put("screatword", UserHelp.getPosttime());
                    ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
                    ps.put("messageid", id);
                    ps.put("result", "2");
                    MyApplication.http.post(Api.ReplyAuthority, ps, new AjaxCallBack<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            super.onSuccess(o);
                            try {
                                JSONObject object = new JSONObject(o.toString());
                                if (object.getString("resultcode").equals("0")) {
                                    showStringToastMsg(object.getString("message"));
                                } else {
                                    showStringToastMsg(object.getString("message"));
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
                finish();
                break;
        }
    }
}
