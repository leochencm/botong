package com.botongglcontroller.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.botongglcontroller.adapter.WifiAdapter;
import com.botongglcontroller.R;
import com.botongglcontroller.wifi.WIFIContron;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.util.List;

public class WifiListActivity extends BaseActivity {
    private static final int BAIDU_READ_PHONE_STATE = 100;
    List<ScanResult> wifiList;
    WIFIContron wifiContron;
    ListView mList;
    WifiAdapter itemAdapter;
    ImageView mBack;
    private WifiManager wifiManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wifi_list;
    }

    @Override
    public void initView() {
        mList = (ListView) findViewById(R.id.wifi);
    }

    @Override
    public void initData() {

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

    @Override
    public void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.wifiname);
                Intent mIntent = new Intent();
                mIntent.putExtra("wifi", text.getText().toString());
                // 设置结果，并进行传送
                setResult(1, mIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra("wifi", "");
        // 设置结果，并进行传送
        setResult(1, mIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent mIntent = new Intent();
                mIntent.putExtra("wifi", "");
                // 设置结果，并进行传送
                setResult(1, mIntent);
                finish();
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    wifiContron = new WIFIContron(WifiListActivity.this);
                    if (!wifiContron.mWifiManager.isWifiEnabled()) {
                        LemonBubble.showError(this, "请开启WIFI!", 2000);
                        return;
                    }
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    List<ScanResult> wifiList = wifiManager.getScanResults();
                    itemAdapter = new WifiAdapter(this, wifiList);
                    mList.setAdapter(itemAdapter);
                } else {
                    // 没有获取到权限，做特殊处理
                }
                break;
            default:
                break;
        }
    }
}
