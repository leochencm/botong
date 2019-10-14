package com.botongglcontroller.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.botongglcontroller.MyApplication;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public ProgressDialog progressDialg = null;
    public ProgressDialog progressDialogwating = null;
    public ProgressDialog Dialg = null;
    protected InputMethodManager inputMethodManager;
    private MyApplication application;
    private BaseActivity oContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (application == null) {
            // 得到Application对象
            application = (MyApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
        initView();
        initData();
        initListener();

    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用myApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    @Override
    public void onAttachFragment(Fragment fragment) {

        super.onAttachFragment(fragment);
    }

    /**
     * 给当前activity给予布局文件
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化
     */
    public abstract void initView();

    /**
     * 获取数据
     */
    public abstract void initData();

    /**
     * 设置监听
     */
    public abstract void initListener();

    public void showToastMsg(int stringId) {
        Toast.makeText(this, getResources().getString(stringId),
                Toast.LENGTH_LONG).show();
    }

    public void showStringToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongStringToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示加载Dialog
     */
    protected void showLoadingDialog() {
        if (progressDialg == null) {
            progressDialg = new ProgressDialog(BaseActivity.this);
            progressDialg.setCancelable(false);
            progressDialg.setMessage("加载中...");
            //progressDialg.set
        }
        progressDialg.show();

    }

    /**
     * 隐藏加载Dialog
     */
    protected void hideLoadingDialog() {
        if (progressDialg != null) {
            progressDialg.dismiss();
        }
    }

    //显示等待进度条
    protected void showWaitingCmdDialog() {
        if (progressDialogwating == null) {
            progressDialogwating = new ProgressDialog(BaseActivity.this);
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

    /**
     * 显示加载Dialog
     */
    protected void showStringLoadingDialog(String s) {
        if (Dialg == null) {
            Dialg = new ProgressDialog(BaseActivity.this);
            Dialg.setCancelable(false);
            Dialg.setMessage(s);
        }
        Dialg.show();

    }

    /**
     * 隐藏加载Dialog
     */
    protected void hideStringLoadingDialog() {
        if (Dialg != null) {
            Dialg.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
        hideStringLoadingDialog();
    }
}

