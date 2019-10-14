package com.botongglcontroller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.botongglcontroller.R;
import com.botongglcontroller.Service.SocketService;
import com.botongglcontroller.utils.ToastUtil;
import com.botongglcontroller.interfaces.Devices;
import com.botongglcontroller.socket.SocketMessage;

import net.lemonsoft.lemonbubble.LemonBubble;

public class SwitchActivity extends BaseActivity {

    RadioButton mButton1, mButton2;
    ImageView mBack;
    Button mConfim;
    SocketMessage socketMessage;
    BroadcastReceiver changeNetwork = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.showToast(SwitchActivity.this, "已获取广播changeNetwork" + intent.getStringExtra("state"));
            hideLoadingDialog();
            if (intent.getStringExtra("state").equals("成功")) {
                LemonBubble.showRight(SwitchActivity.this, "切换成功", 2000);
            } else if (intent.getStringExtra("state").equals("失败")) {
                LemonBubble.showError(SwitchActivity.this, "切换失败", 2000);
            }
        }
    };
    private Devices devices;

    @Override
    public int getLayoutId() {
        return R.layout.activity_switch;
    }

    @Override
    public void initView() {
        socketMessage = new SocketService().getSocketMessage();
        devices = new Devices(new SocketService().getSocketMessage());
        mBack = (ImageView) findViewById(R.id.img_back);
        mConfim = (Button) findViewById(R.id.btn_confirm);
        mButton1 = (RadioButton) findViewById(R.id.button1);
        mButton2 = (RadioButton) findViewById(R.id.button2);
        IntentFilter filter = new IntentFilter("com.broadcast.changeNetwork");
        registerReceiver(changeNetwork, filter);
    }

    @Override
    public void initData() {

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
            case R.id.btn_confirm:
                if (socketMessage == null) {
                    ToastUtil.showToast(SwitchActivity.this, "您还未链接锅炉，请连接锅炉再操作！");
                    return;
                }
//切换到AP模式
                if (mButton1.isChecked()) {
                    devices.changeNetwork(0x01);
                }
                //切换到路由器模式
                if (mButton2.isChecked()) {
                    devices.changeNetwork(0x02);
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeNetwork);
    }
}
