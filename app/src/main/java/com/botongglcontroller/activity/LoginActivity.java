package com.botongglcontroller.activity;

import com.botongglcontroller.Intents;
import com.botongglcontroller.MyApplication;
import com.botongglcontroller.TagAliasOperatorHelper;
import com.botongglcontroller.utils.MD5Utils;
import com.botongglcontroller.utils.ToastUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.botongglcontroller.R;
import com.botongglcontroller.utils.UserHelp;

import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import com.botongglcontroller.TagAliasOperatorHelper.TagAliasBean;

import static com.botongglcontroller.TagAliasOperatorHelper.sequence;

import com.botongglcontroller.Api;

public class LoginActivity extends BaseActivity {
    static String verName;
    EditText mTelephone, mPwd;
    CheckBox mBox;
    String nnnn;
    private Button btn;
    private TextView register, forgetpwd;

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
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.btcontroller", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return verName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.login;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//    }
    @Override
    public void initView() {
        btn = (Button) findViewById(R.id.btn_login);
        register = (TextView) findViewById(R.id.txt_register);
        forgetpwd = (TextView) findViewById(R.id.txt_forgetpwd);
        mTelephone = (EditText) findViewById(R.id.edt_telephonenum);
        mPwd = (EditText) findViewById(R.id.edt_pwd);
        mBox = (CheckBox) findViewById(R.id.cb);
    }

    public void initData() {
        mTelephone.setText(MyApplication.sp.GetMobile());
        mPwd.setText(MyApplication.sp.GetPwd());
        // DisplayMetrics dm=new DisplayMetrics();
        // getWindowManager().getDefaultDisplay()

        //  int ss=dm.densityDpi;
        // getVersion();
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
                            if (Double.valueOf(ob.getString("version")) > Double.valueOf(getVerName(LoginActivity.this))) {

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
                                                    openBrowser(LoginActivity.this, ob.getString("url"));
                                                } catch (Exception ex) {

                                                }
                                            }
                                        }));
                                info1.show(LoginActivity.this);
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

    @Override
    public void initListener() {
        btn.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetpwd.setOnClickListener(this);

        mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int passwordLength = mPwd.getText().length();
                mPwd.setInputType(isChecked ?
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) :
                        (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
                mPwd.setSelection(passwordLength);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();

                break;
            case R.id.txt_register:
                Intents.getIntents().Intent(this, RegisterActivity.class);

                break;
            case R.id.txt_forgetpwd:
                Intents.getIntents().Intent(this, ForgetpwdActivity.class);
                break;

        }
    }


    private void login() {
        if (TextUtils.isEmpty(mTelephone.getText())) {
            ToastUtil.showToast(LoginActivity.this, "请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(mPwd.getText())) {
            ToastUtil.showToast(LoginActivity.this, "请输入密码");
            return;
        }
        //if (MyApplication.sp.GetScreatMsg().equals("")) {
        //    SetScreatMsg();
        // } else {
        Login();
        // }


    }

    /**
     * /**
     * TagAliasCallback类是JPush开发包jar中的类，用于
     * 设置别名和标签的回调接口，成功与否都会回调该方法
     * 同时给定回调的代码。如果code=0,说明别名设置成功。
     * /**
     * 6001   无效的设置，tag/alias 不应参数都为 null
     * 6002   设置超时    建议重试
     * 6003   alias 字符串不合法    有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     * 6004   alias超长。最多 40个字节    中文 UTF-8 是 3 个字节
     * 6005   某一个 tag 字符串不合法  有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字。
     * 6006   某一个 tag 超长。一个 tag 最多 40个字节  中文 UTF-8 是 3 个字节
     * 6007   tags 数量超出限制。最多 100个 这是一台设备的限制。一个应用全局的标签数量无限制。
     * 6008   tag/alias 超出总长度限制。总长度最多 1K 字节
     * 6011   10s内设置tag或alias大于3次 短时间内操作过于频繁
     **/

    private void Login() {
        AjaxParams params = new AjaxParams();
        try {
            params.put("screatname", MyApplication.sp.GetScreatMsg());
            params.put("screatword", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode(MyApplication.sp.GetScreatMsg()) + UserHelp.dateToStamp(UserHelp.getPosttime())));
            params.put("username", mTelephone.getText().toString());
            params.put("password", mPwd.getText().toString());
            Log.i("登录", String.valueOf(params));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyApplication.http.configTimeout(8000);
        MyApplication.http.post(Api.LoginA, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                hideLoadingDialog();
                Log.i("登录", o.toString());
                try {
                    JSONObject object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        JSONObject ob = object.getJSONObject("data");
                        MyApplication.sp.setIdentity(ob.getString("identity"));
                        MyApplication.sp.setId(ob.getString("id"));
                        MyApplication.sp.setMobile(mTelephone.getText().toString());
                        MyApplication.sp.setPwd(mPwd.getText().toString());
                        MyApplication.sp.setParentTel(ob.getString("authority"));
                        //nnnn=mTelephone.getText().toString();
                        // JPushInterface.setAlias(LoginActivity.this, 1222,mTelephone.getText().toString());
                        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasBean();
                        tagAliasBean.action = 2;
                        tagAliasBean.alias = mTelephone.getText().toString();
                        ;
                        sequence++;
                        tagAliasBean.isAliasAction = true;
                        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);


                        MyApplication.sp.setAccountName(mTelephone.getText().toString());
                        GetAccount();
                        Intents.getIntents().Intent(LoginActivity.this, HomeActivity.class);
                        finish();
                    } else if (object.getString("resultcode").equals("1")) {
                        ToastUtil.showToast(LoginActivity.this, "用户名错误");
                    } else if (object.getString("resultcode").equals("2")) {
                        ToastUtil.showToast(LoginActivity.this, "密码错误");
                    } else if (object.getString("resultcode").equals("3")) {
                        ToastUtil.showToast(LoginActivity.this, "账户已被冻结");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, String strMsg) {
                super.onFailure(t, strMsg);
                ToastUtil.showToast(LoginActivity.this, "登录失败，请检查网络！");
                hideLoadingDialog();
            }
        });
        showLoadingDialog();
    }

    private void SetScreatMsg() {
        AjaxParams params = new AjaxParams();
        try {
            params.put("time", UserHelp.getPosttime());
            params.put("sign", MD5Utils.encode(MD5Utils.encode("mantoo") + UserHelp.dateToStamp(UserHelp.getPosttime())));
            Log.i("params", params.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyApplication.http.post(Api.GetScreatMsg, params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                Log.i("SSSSSSSSSSSSS", o.toString());
                try {
                    JSONObject object = new JSONObject(o.toString());
                    if (object.getString("resultcode").equals("0")) {
                        JSONObject ob = object.getJSONObject("data");
                        MyApplication.sp.setScreatMsg(ob.getString("screatname"));
                        Login();
                        Log.i("screeb", ob.getString("screatname"));
                    }
                } catch (JSONException e) {
                }
            }
        });
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
                            // MyApplication.sp.setParentTel(ob.getString("authority"));
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
}