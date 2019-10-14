package com.botongglcontroller.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.activity.BaseActivity;
import com.botongglcontroller.activity.BindGuoluActivity;
import com.botongglcontroller.activity.GuolulinkActivity;
import com.botongglcontroller.activity.HomeActivity;
import com.botongglcontroller.activity.MipcaActivityCapture;
import com.botongglcontroller.activity.SellerBoilersListActivity;
import com.botongglcontroller.activity.SetSellerActivity;
import com.botongglcontroller.activity.test;
import com.botongglcontroller.adapter.ScrollPickerAdapter;
import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.beans.BoilersPara;
import com.botongglcontroller.beans.DealerList;
import com.botongglcontroller.beans.MyData;
import com.botongglcontroller.provider.MyViewProvider;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.utils.UserHelp;
import com.botongglcontroller.view.MyImageView;
import com.botongglcontroller.view.ScrollPickerView;
import com.readystatesoftware.viewbadger.BadgeView;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HomemanageActivity extends Fragment {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static int GETSELLER_GREQUEST_CODE = 2;
    public ProgressDialog progressDialogwating = null;
    MyImageView mSuggestion, mPersonal;
    //    Button mGuolu;
    ImageButton imageButton;
    MyImageView mScan, mNews;
    BadgeView bv, bv1;
    MyImageView mGuolu;
    Bundle bundle;
    private long touchTime = 0;
    private TextView mTvGYS;
    private String mAcc = "";
    private ScrollPickerView mScrollPickerView;
    private ScrollPickerAdapter mScrollPickerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.homemanager, null);
        mScan = (MyImageView) rootview.findViewById(R.id.img_scan_fp);
        mTvGYS = (TextView) rootview.findViewById(R.id.gys_name);
        mScrollPickerView = (ScrollPickerView) rootview.findViewById(R.id.scroll_seller_view);
        mScrollPickerView.setLayoutManager(new LinearLayoutManager(getContext()));

        imageButton = rootview.findViewById(R.id.btn_search);
        // 获取经销商列表
        List<DealerList> list = new ArrayList<>();
        //DealerList dl = new DealerList();
        //dl.setDealertel("111");
        //dl.setDealercompany("222");
        //list.add(dl);
        ScrollPickerAdapter.ScrollPickerAdapterBuilder<DealerList> builder =
                new ScrollPickerAdapter.ScrollPickerAdapterBuilder<DealerList>(getActivity())
                        .setDataList(list)
                        .selectedItemOffset(2)
                        .visibleItemNumber(5)
                        .setDivideLineColor("#ffffff")
                        .setItemViewProvider(new MyViewProvider())
                        .setOnScrolledListener(new ScrollPickerAdapter.OnScrollListener() {
                            @Override


                            public void onScrolled(View currentItemView) {
                                DealerList dl = (DealerList) currentItemView.getTag();
                                if (dl != null) {
                                    mTvGYS.setText(dl.getDealercompany());
                                    mAcc = dl.getDealertel();
                                    // Intent intent = new Intent(getActivity(), SetSellerActivity.class);
                                    //  startActivity(intent);
                                }
                            }
                        });
        mScrollPickerAdapter = builder.build();
        mScrollPickerView.setAdapter(mScrollPickerAdapter);


        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
        MyApplication.http.post(Api.GetRuleBoilerlogin, ps, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        list.clear();
                        JSONArray oba = object.getJSONArray("data");
                        for (int i = 0; i < oba.length(); i++) {
                            JSONObject ob = oba.getJSONObject(i);
                            DealerList dl = new DealerList();
                            dl.setDealertel(ob.getString("account"));
                            dl.setDealercompany(ob.getString("companyname"));
                            list.add(dl);
                        }

                        // if(list.size()<5)
                        {
                            for (int i = 0; i < 2; i++) {
                                DealerList dl = new DealerList();
                                dl.setDealercompany("");
                                dl.setDealertel("");
                                list.add(0, dl);
                                list.add(dl);
                            }

                        }
                        builder.setDataList(list);
                        //mScrollPickerView.setAdapter(mScrollPickerAdapter);
                        //  builder.build();
                        mScrollPickerAdapter.notifyDataSetChanged();
                        // mScrollPickerAdapter = builder.build();
                        // mScrollPickerView.setAdapter(mScrollPickerAdapter);
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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellerBoilersListActivity.class);
                intent.putExtra("sellername", mTvGYS.getText());
                intent.putExtra("parent", MyApplication.sp.getParentTel());
                startActivity(intent);
            }
        });
        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, GETSELLER_GREQUEST_CODE);
            }
        });
        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == -1) {
                    bundle = data.getExtras();
                    //显示扫描到的内容
                    Log.i("扫描", bundle.getString("result"));
                    Log.i("扫描", MyApplication.sp.GetIdentity());
                    bundle.putString("result", bundle.getString("result"));
                    //bundle.putString("companyname",mTvGYS.getText().toString());
                    //bundle.putString("account",mAcc);
                    //判断客户类型，为seller时为管理员可以不绑定锅炉就能进去
                    //if(MyApplication.sp.GetIdentity().equals("seller")){
                    //    String[] info =  bundle.getString("result").split("\\s+");
                    //    VerifyConn(info[0].toString(),info[1].substring(1,info[1].length()-1));
                    // }else if(MyApplication.sp.GetIdentity().equals("user")){
                    Intents.getIntents().Intent(getActivity(), BindGuoluActivity.class, bundle);
                    //}
                }
                break;
            case GETSELLER_GREQUEST_CODE:
                if (resultCode == -1) {
                    bundle = data.getExtras();
                    //显示扫描到的内容
                    Log.i("扫描", bundle.getString("result"));
                    Log.i("扫描", MyApplication.sp.GetIdentity());
                    bundle.putString("result", bundle.getString("result"));
                    bundle.putString("companyname", mTvGYS.getText().toString());
                    bundle.putString("account", mAcc);
                    Intents.getIntents().Intent(getActivity(), SetSellerActivity.class, bundle);

                    //判断客户类型，为seller时为管理员可以不绑定锅炉就能进去
                    //if(MyApplication.sp.GetIdentity().equals("seller")){
                }
                break;


        }
    }

    private void VerifyConn(final String serialnumber, final String name) {
        showWaitingCmdDialog();
        try {
            AjaxParams ps = new AjaxParams();
            ps.put("screatname", MyApplication.sp.GetScreatMsg());
            ps.put("screatword", UserHelp.getPosttime());
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            ps.put("serialnumber", serialnumber);
            ps.put("account", MyApplication.sp.GetMobile());
            MyApplication.http.post(Api.VerifyConn, ps, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    hideWaitingDialog();
                    Log.i("验证", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            bundle.putString("serialnumber", serialnumber);
                            bundle.putString("name", name);
                            Intents.getIntents().Intent(getActivity(), GuolulinkActivity.class, bundle);
                        } else {
                            LemonBubble.showError(getActivity(), object.getString("message"), 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t, String strMsg) {
                    super.onFailure(t, strMsg);
                    hideWaitingDialog();
                    ToastUtil.showToast(getActivity(), "扫描失败，请检查网络！");
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //显示等待进度条
    protected void showWaitingCmdDialog() {
        if (progressDialogwating == null) {
            progressDialogwating = new ProgressDialog(getActivity());
            progressDialogwating.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialogwating.setCancelable(false);
            progressDialogwating.setIndeterminate(false);
            progressDialogwating.setMessage("等待设备响应...");

        }
        progressDialogwating.show();

    }

    //隐藏等待进度条
    protected void hideWaitingDialog() {
        if (progressDialogwating != null) {
            progressDialogwating.dismiss();
        }
    }

    private void getBadgeView() {

    }


}
