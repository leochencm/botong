package com.botongglcontroller.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.botongglcontroller.Api;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.R;
import com.botongglcontroller.Service.Downloadservice;
import com.botongglcontroller.beans.SPTools;
import com.botongglcontroller.utils.DownloadApk;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.UserHelp;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import static com.botongglcontroller.utils.DownloadApk.downLoadApk;
import static com.botongglcontroller.utils.DownloadApk.downloadApk;

public class WelcomeActivity extends Activity {
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static float fnow, fmax;
    private TextView tv_welcome_getversionname;
    private String vname;
    private long Id;
    private ProgressDialog progressDialog;
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
                        unbindService(conn);
                        isBindService = false;
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        tv_welcome_getversionname = (TextView) findViewById(R.id.tv_welcome_getversionname);
        vname = getversionname();
        tv_welcome_getversionname.setText("版本号：" + vname);
        update();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
// 获取wifi连接需要定位权限,没有获取权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
            }, BAIDU_READ_PHONE_STATE);
            return;
        }


    }

    /**
     * 查询更新
     */
    private void update() {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                getversion();
                // super.run();
            }
        }.start();
    }

    /**
     * 获取版本
     */
    private void getversion() {
        try {
            long start = System.currentTimeMillis();
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
                            if (Integer.valueOf(ob.getString("version")) > getversioncode()) {

                                LemonHelloInfo info1 = new LemonHelloInfo()
                                        .setTitle("软件更新")
                                        .setContent("有最新版本，是否更新版本？")
                                        .addAction(new LemonHelloAction("暂不更新", Color.RED, new LemonHelloActionDelegate() {
                                            @Override
                                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                                helloView.hide();
                                                Intent intent = new Intent(WelcomeActivity.this,
                                                        LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }))
                                        .addAction(new LemonHelloAction("更新", new LemonHelloActionDelegate() {
                                            @Override
                                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                                helloView.hide();
                                                try {
                                                    downLoadCs(ob.getString("url"));
                                                    //  Id=DownloadApk.downLoadApk(WelcomeActivity.this,"博通电子v"+vname,ob.getString("url"));
                                                    // openBrowser(WelcomeActivity.this,ob.getString("url"));
                                                } catch (Exception ex) {

                                                }
                                            }
                                        }));


                                info1.show(WelcomeActivity.this);
                            } else {

                                Intent intent = new Intent(WelcomeActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                                finish();
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
        progress = new ProgressDialog(this);
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
        Intent intent = new Intent(this, Downloadservice.class);
        intent.putExtra(Downloadservice.BUNDLE_KEY_DOWNLOAD_URL, url);
        isBindService = bindService(intent, conn, BIND_AUTO_CREATE); //绑定服务即开始下载 调用onBind()
    }

    /**
     * 删除上次更新存储在本地的apk
     */
    private void removeOldApk() {
        //获取老ＡＰＫ的存储路径
        String f = getExternalFilesDir("botong/") + "/botong.apk";
        File fileName = new File(f);
        if (fileName != null && fileName.exists() && fileName.isFile()) {
            fileName.delete();
        }
    }

    private String getversionname() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getversioncode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                } else {

                }
                break;
            default:
                break;
        }
    }

}