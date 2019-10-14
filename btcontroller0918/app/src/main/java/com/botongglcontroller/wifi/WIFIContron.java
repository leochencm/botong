package com.botongglcontroller.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Author jonathan
 * Created by Administrator on 2017/1/16.
 * Effect wifi控制类
 */

public class WIFIContron {
    private final String TAG = getClass().getName();
    public WifiManager mWifiManager;
    private WifiInfo mWifiInfo;// 定义WifiInfo对象
    private WifiManager.WifiLock mWifiLock;//wifi锁
    private List<WifiConfiguration> mWifiConfiguration;//网络连接列表
    private List<ScanResult> mWifiList; // 扫描出的网络连接列表
    private GetResult result;

    /**
     * 构造
     * @param context
     */
    public WIFIContron(Context context , GetResult result){
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
        this.result = result;
    }
    public WIFIContron(Context context ){
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    // 添加一个新网络并连接
    public void add( String  ssd,String pwd) {
        // 连接到外网
        WifiConfiguration mWifiConfiguration;
        String SSID = ssd;
        String password = pwd;

            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    Log.i("existingConfig", "" + existingConfig.SSID);
                    connectNetwork(existingConfig);
                }
            }
    }

//    // 添加一个新网络并连接
//    public void addNetwork(WifiConfiguration wcg) {
//        int wcgID = mWifiManager.addNetwork(wcg);
//        boolean b = mWifiManager.enableNetwork(wcgID, true);
//    }

    //连接一个曾经连接过得WiFi
    public void connectNetwork(WifiConfiguration wcg){
        boolean b = mWifiManager.enableNetwork(wcg.networkId, true);
    }

    /**
     * 打开WIFI
     * @param context
     */
    public void openWifi(Context context){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }else if(mWifiManager.getWifiState() == 2){
            showT(context,"正在开启！");
        }else {
            showT(context,"已经开启！");
        }
    }

    /**
     * 关闭WIFI
     * @param context
     */
    public void colseWifi(Context context){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }else if (mWifiManager.getWifiState() == 1){
            showT(context,"已经关闭");
        }else if(mWifiManager.getWifiState() == 0){
            showT(context,"正在关闭");
        }else{
            showT(context,"请重新关闭");
        }
    }

    /**
     * 获取WIFI状态
     * @param context
     */
    public int checkState(Context context){
        if(mWifiManager.getWifiState() == 0){
            showT(context,"Wifi正在关闭");
        }else if(mWifiManager.getWifiState() == 1){
            showT(context,"Wifi已经关闭");
        }else if(mWifiManager.getWifiState() == 2){
            showT(context,"Wifi正在开启");
        }else if(mWifiManager.getWifiState() == 3){
            showT(context,"Wifi已经开启");
        }else{
            showT(context,"没有获取到WiFi状态");
        }
return  mWifiManager.getWifiState();
    }
    public int getState(Context context){
        return mWifiManager.getWifiState();
    }

    /**
     * toast
     * @param context
     * @param str
     */
    private void showT(Context context , String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建wifiLock
     */
    private void creatWifiLock(){
        mWifiLock = mWifiManager.createWifiLock("wifi_lock");
    }

    /**
     * 锁定wifi
     */
    private void acquireWifiLock(){
        mWifiLock.acquire();
    }

    /**
     * 解锁wifi
     */
    private void releaseWifiLock(){
        if(mWifiLock.isHeld()){
            mWifiLock.acquire();
            Log.i(TAG,"wifi锁定，正在解锁");
        }else{
            Log.i(TAG,"wifi未锁");
        }
    }

    /**
     * 得到配置好的网络
     * @return
     */
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    /**
     * 指定配置好的网络进行连接
     * @param index
     */
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    /**
     * 扫描
     * @param context
     */
    public void startScan(Context context) {
        mWifiManager.startScan();
        //得到扫描结果
        List<ScanResult> results = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        if (results == null) {
            if(mWifiManager.getWifiState()==3){
                Toast.makeText(context,"当前区域没有无线网络",Toast.LENGTH_SHORT).show();
            }else if(mWifiManager.getWifiState()==2){
                Toast.makeText(context,"wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            }else{Toast.makeText(context,"WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        } else {
            mWifiList = new ArrayList<ScanResult>();
            for(ScanResult result : results){
                if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for(ScanResult item:mWifiList){
                    if(item.SSID.equals(result.SSID)&&item.capabilities.equals(result.capabilities)){
                        found = true;break;
                    }
                }
                if(!found){
                    mWifiList.add(result);
                }
            }
            result.resultValue(lookUpScan(),mWifiList);
        }
    }

    /**
     * 得到网络列表
     * @return List<ScanResult>
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    /**
     * 查看扫描结果
     * @return StringBuilder
     */
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }


    /**
     * 得到MAC地址
      * @return String
     */
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     * 得到接入点的BSSID
      * @return String
     */
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }
    /**
     * 得到接入点的BSSID
      * @return String
     */
    public String getSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    /**
     * 得到IP地址
     * ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
     *+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
     * @return int
     */
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 得到连接的ID
      * @return int
     */
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 得到WifiInfo的所有信息包
      * @return String
     */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
      * @param wcg
     */
    public boolean  addNetwork(WifiConfiguration wcg) {
//        int wcgID = mWifiManager.addNetwork(wcg);
//        boolean b =  mWifiManager.enableNetwork(wcgID, true);
//        System.out.println("a--" + wcgID);
//        System.out.println("b--" + b);

        mWifiManager.disconnect();
        int networkId = mWifiManager.addNetwork(wcg);
        boolean res = mWifiManager.enableNetwork(networkId, true);
        mWifiManager.saveConfiguration();
        mWifiManager.reconnect();
        Log.i("addNetwork","addNetwork"+res);
        return res;

    }

    /**
     * 断开指定ID的网络
     * @param netId
     */
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 删除指定ID的网络
     * @param netId
     */
    public void removeWifi(int netId) {
        disconnectWifi(netId);
        mWifiManager.removeNetwork(netId);
    }

    /**
     * 然后是一个实际应用方法，只验证过没有密码的情况：
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
            Log.i("bukongh","kong");
        }else{
            Log.i("kong","kong");
        }

        if(Type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }


    public  interface GetResult{
         void resultValue(StringBuilder str, List<ScanResult> mWifiList);
    }

}
