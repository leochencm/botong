package com.botongglcontroller.Fragment;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonbubble.enums.LemonBubbleLayoutStyle;
import net.lemonsoft.lemonbubble.enums.LemonBubbleLocationStyle;
import net.lemonsoft.lemonhello.LemonHello;

import com.botongglcontroller.Api;
import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.Downloadservice;
import com.botongglcontroller.activity.AlertAdressActivity;
import com.botongglcontroller.activity.AlertcompanyActivity;
import com.botongglcontroller.activity.AlertcontactnameActivity;
import com.botongglcontroller.activity.AlertpwdActivity;
import com.botongglcontroller.activity.AlertteltphonenumOneActivity;
import com.botongglcontroller.activity.BillmanagementActivity;
import com.botongglcontroller.activity.ForhelpListActivity;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;
//import com.btcontroller.activity.AlertAdressActivity;
//import com.btcontroller.activity.AlertcompanyActivity;
//import com.btcontroller.activity.AlertcontactnameActivity;
//import com.btcontroller.activity.AlertpwdActivity;
//import com.btcontroller.activity.AlertteltphonenumOneActivity;
//import com.btcontroller.activity.BillmanagementActivity;
//import com.btcontroller.activity.ForhelpActivity;
//import com.btcontroller.activity.ForhelpListActivity;
import com.botongglcontroller.activity.LoginActivity;
//import com.btcontroller.activity.OperatingconditionsActivity;

import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by hasee on 2016/12/17.
 */

public class PersonalFragment extends Fragment {
    static String verName;
    static int verCode;
    private static float fnow, fmax;
    RelativeLayout mTelephone, mPwd, mCompany, mAdress, mContactname, Version, ll_billmanage, ll_help;
    Button mExit;
    private TextView mVersion, order_new, help_news;
    private int missionnos;
    private ProgressDialog progress;
    private boolean isBindService;
    private ServiceConnection conn = new ServiceConnection() { //通过ServiceConnection间接可以拿到某项服务对象

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Downloadservice.DownloadBinder binder = (Downloadservice.DownloadBinder) service;
            Downloadservice downloadServise = binder.getService();

            //接口回调，下载进度
            downloadServise.setOnProgressListener(new Downloadservice.OnProgressListener() {
                @Override
                public void onProgress(int fraction, int max) {
                    fnow = (float) fraction / 1024 / 1024;
                    fmax = (float) max / 1024 / 1024;
                    progress.setProgress(fraction);
                    progress.setMax(max);
                    progress.setProgressNumberFormat(String.format("%.2fM/%.2fM", fnow, fmax));
                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (fraction == max && isBindService) {
                        // fnow=max/1024/1024;
                        // progress.setProgress(max);
                        progress.dismiss();
                        getActivity().getApplicationContext().unbindService(conn);
                        isBindService = false;
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public View.OnClickListener listrner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.ll_company:
                    Intents.getIntents().Intent(getActivity(), AlertcompanyActivity.class);
                    break;
                case R.id.ll_billmanage:
                    Intents.getIntents().Intent(getActivity(), BillmanagementActivity.class);
                    break;
                case R.id.ll_help:
                    Intents.getIntents().Intent(getActivity(), ForhelpListActivity.class);
                    break;
                case R.id.ll_pwd:
                    Intents.getIntents().Intent(getActivity(), AlertpwdActivity.class);
                    break;
                case R.id.ll_contactname:
                    Intents.getIntents().Intent(getActivity(), AlertcontactnameActivity.class);
                    break;
                case R.id.ll_adress:
                    Intent intent = new Intent(getActivity(), AlertAdressActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_telephone:
                    Intents.getIntents().Intent(getActivity(), AlertteltphonenumOneActivity.class);
                    break;
                case R.id.ll_version:
                    getVersion();
                    break;
                case R.id.btn_exit:
                    LemonHello.getInformationHello("您确定要注销吗？", "注销登录后您将无法查看到当前用户的锅炉信息。")
                            .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                }
                            }))
                            .addAction(new LemonHelloAction("我要注销", Color.RED, new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
//                                     提示框使用了LemonBubble，请您参考：https://github.com/1em0nsOft/LemonBubble4Android
                                    LemonBubble.getRoundProgressBubbleInfo()
                                            .setLocationStyle(LemonBubbleLocationStyle.BOTTOM)
                                            .setLayoutStyle(LemonBubbleLayoutStyle.ICON_LEFT_TITLE_RIGHT)
                                            .setBubbleSize(200, 50)
                                            .setProportionOfDeviation(0.1f)
                                            .setTitle("正在请求服务器...")
                                            .show(getContext());
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            LemonBubble.showRight(getActivity(), "注销成功，欢迎您下次登录", 1000);
                                            LemonBubble.hide();
                                            Intents.getIntents().Intent(getActivity(), LoginActivity.class);
                                            getActivity().finish();

                                        }
                                    }, 1500);

                                }

                            }

                            ))
                            .show(getActivity());

                    break;
            }
        }

    };

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
        }
    }

    //获取版本号
    public static String getVerName(Context context) {
        verName = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            verName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    //获取版本号
    public static int getVerCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 2;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.personalcenter, container, false);

        mTelephone = (RelativeLayout) root.findViewById(R.id.ll_telephone);
        Version = (RelativeLayout) root.findViewById(R.id.ll_version);
        mPwd = (RelativeLayout) root.findViewById(R.id.ll_pwd);
        mCompany = (RelativeLayout) root.findViewById(R.id.ll_company);
        ll_help = (RelativeLayout) root.findViewById(R.id.ll_help);
        mAdress = (RelativeLayout) root.findViewById(R.id.ll_adress);
        ll_billmanage = (RelativeLayout) root.findViewById(R.id.ll_billmanage);
        mContactname = (RelativeLayout) root.findViewById(R.id.ll_contactname);
        mExit = (Button) root.findViewById(R.id.btn_exit);
        order_new = (TextView) root.findViewById(R.id.order_new);
        help_news = (TextView) root.findViewById(R.id.help_news);
        mVersion = (TextView) root.findViewById(R.id.tv_version);

        mVersion.setText("当前版本：" + getVerName(getActivity()));

        // 获取消息数量
        AjaxParams ps = new AjaxParams();
        ps.put("screatname", MyApplication.sp.GetScreatMsg());
        ps.put("screatword", UserHelp.getPosttime());
        try {
            ps.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ps.put("account", MyApplication.sp.GetMobile());
        ps.put("identity", MyApplication.sp.GetIdentity());
        MyApplication.http.post(Api.GetInformNum, ps, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                JSONObject object = null;
                try {
                    object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        missionnos = Integer.parseInt(object.getString("missionno"));
                        if (missionnos > 0) {
                            if (MyApplication.sp.GetIdentity().equals("seller")) {
                                help_news.setVisibility(View.VISIBLE);
                            } else {
                                order_new.setVisibility(View.VISIBLE);
                            }
                        }
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


        GetAccount();
        mTelephone.setOnClickListener(listrner);
        mAdress.setOnClickListener(listrner);
        mPwd.setOnClickListener(listrner);
        mCompany.setOnClickListener(listrner);
        ll_billmanage.setOnClickListener(listrner);
        ll_help.setOnClickListener(listrner);
        Version.setOnClickListener(listrner);
        mContactname.setOnClickListener(listrner);
        mExit.setOnClickListener(listrner);
        return root;
    }

    private void GetAccount() {
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("mobile", MyApplication.sp.GetMobile());
            MyApplication.http.post(Api.GetAccountMsg, params, new AjaxCallBack<Object>() {

                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            JSONObject ob = object.getJSONObject("data");
                            MyApplication.sp.setCompanyname(ob.getString("companyname"));
                            MyApplication.sp.setContactname(ob.getString("contactname"));
                            MyApplication.sp.setAddress(ob.getString("address"));
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

    private void getVersion() {
        try {
            AjaxParams params = new AjaxParams();
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("Platform", "Android");
            MyApplication.http.post(Api.GetVersion, params, new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    Log.i("版本", o.toString());
                    try {
                        JSONObject object = new JSONObject(o.toString());
                        if (object.getString("resultcode").equals("0")) {
                            final JSONObject ob = object.getJSONObject("data");
                            if (Integer.valueOf(ob.getString("version")) > Integer.valueOf(getVerCode(getActivity()))) {

                                LemonHelloInfo info1 = new LemonHelloInfo()
                                        .setTitle("软件更新")
                                        .setContent("有最新版本，是否更新版本？")
                                        .addAction(new LemonHelloAction("暂不更新", Color.RED, new LemonHelloActionDelegate() {
                                            @Override
                                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                                helloView.hide();
                                            }
                                        }))
                                        .addAction(new LemonHelloAction("更新", new LemonHelloActionDelegate() {
                                            @Override
                                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                                helloView.hide();

                                                try {
                                                    downLoadCs(ob.getString("url"));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }));
                                info1.show(getActivity());
                            } else {
                                LemonBubble.showRight(getActivity(), "已经是最新版本！", 2000);
                            }

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

    private void initProgressBar() {
        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(1);
        progress.setTitle("正在下载……");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    private void downLoadCs(String url) {  //下载调用
        initProgressBar();
        removeOldApk();
        Intent intent = new Intent(getActivity(), Downloadservice.class);
        intent.putExtra(Downloadservice.BUNDLE_KEY_DOWNLOAD_URL, url);
        isBindService = getActivity().getApplicationContext().bindService(intent, conn, BIND_AUTO_CREATE); //绑定服务即开始下载 调用onBind()
    }

    /**
     * 删除上次更新存储在本地的apk
     */
    private void removeOldApk() {
        //获取老ＡＰＫ的存储路径
        String f = getActivity().getExternalFilesDir("botong/") + "/botong.apk";
        File fileName = new File(f);
        if (fileName != null && fileName.exists() && fileName.isFile()) {
            fileName.delete();
        }
    }

}
