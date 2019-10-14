package com.botongglcontroller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
    private String filenbame = "answerking";

    private SharedPreferences ps;

    private Editor editor;

    public SharedPreferencesUtil(Context context) {
        ps = context.getSharedPreferences(filenbame, 0);
        editor = ps.edit();
    }

    public SharedPreferences getPs() {
        return ps;
    }

    public String GetScreatMsg() {
        return ps.getString("screatname", "");
    }

    public void setScreatMsg(String screatname) {
        editor.putString("screatname", screatname);
        editor.commit();
    }

    public String GetIdentity() {
        return ps.getString("Identity", "");
    }

    public void setId(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public String GetId() {
        return ps.getString("id", "");
    }

    public void setIdentity(String Identity) {
        editor.putString("Identity", Identity);
        editor.commit();
    }

    public String GetMobile() {
        return ps.getString("mobile", "");
    }

    public void setMobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String GetPwd() {
        return ps.getString("pwd", "");
    }

    public void setPwd(String pwd) {
        editor.putString("pwd", pwd);
        editor.commit();
    }

    public String GetSerialnumber() {
        return ps.getString("serialnumber", "");
    }

    public void setSerialnumber(String serialnumber) {
        editor.putString("serialnumber", serialnumber);
        editor.commit();
    }

    public String GetOid() {
        return ps.getString("oid", "");
    }

    public void SetOid(String oid) {
        editor.putString("oid", oid);
        editor.commit();
    }

    public String GetShowenable() {
        return ps.getString("showenable", "");
    }

    public void setShowenable(String serialnumber) {
        editor.putString("showenable", serialnumber);
        editor.commit();
    }

    public String GetCompanyname() {
        return ps.getString("companyname", "");
    }

    public void setCompanyname(String companyname) {
        editor.putString("companyname", companyname);
        editor.commit();
    }

    public String GetAddress() {
        return ps.getString("address", "");
    }

    public void setAddress(String address) {
        editor.putString("address", address);
        editor.commit();
    }

    public String GetContactname() {
        return ps.getString("contactname", "");
    }

    public void setContactname(String contactname) {
        editor.putString("contactname", contactname);
        editor.commit();
    }

    public String GetNetWorkState() {
        return ps.getString("netWorkState", "");
    }

    public void setnetWorkState(String netWorkState) {
        editor.putString("netWorkState", netWorkState);
        editor.commit();
    }

    public int GetUnreadNumber() {
        return ps.getInt("UnreadNumber", 0);
    }

    public void setUnreadNumber(int UnreadNumber) {
        editor.putInt("UnreadNumber", UnreadNumber);
        editor.commit();
    }

    public String Getlockstate() {
        return ps.getString("lockstate", "");
    }

    public void setlockstate(String lockstate) {
        editor.putString("lockstate", lockstate);
        editor.commit();
    }

    public String GetsocketData() {
        return ps.getString("setsocketData", "");
    }

    public void setsocketData(String setsocketData) {
        editor.putString("setsocketData", setsocketData);
        editor.commit();
    }

    public String Getfacopenstate() {
        return ps.getString("facopenstate", "");
    }

    public void setfacopenstate(String facopenstate) {
        editor.putString("facopenstate", facopenstate);
        editor.commit();
    }

    public String Getfaconstate() {
        return ps.getString("faconstate", "");
    }

    public void setfaconstate(String faconstate) {
        editor.putString("faconstate", faconstate);
        editor.commit();
    }

    public boolean GetsocketState() {
        return ps.getBoolean("socket", true);
    }


    public void setsocketState(boolean socket) {
        editor.putBoolean("socket", true);
        editor.commit();
    }

    public String Getisshoudong() {
        return ps.getString("isshoudong", "");
    }


    public void setisshoudong(String isshoudong) {
        editor.putString("isshoudong", isshoudong);
        editor.commit();
    }

    public String GetAccountName() {
        return ps.getString("accountName", "");
    }


    public void setAccountName(String accountName) {
        editor.putString("accountName", accountName);
        editor.commit();
    }

    public String GetIsOnline() {
        return ps.getString("isOnline", "");
    }


    public void setIsOnline(String isOnline) {
        editor.putString("isOnline", isOnline);
        editor.commit();
    }

    public String getWifiName() {
        return ps.getString("wifiName", "");
    }

    public void setWifiName(String wifiName) {
        editor.putString("wifiName", wifiName);
        editor.commit();
    }

    public String getWifiPassword() {
        return ps.getString("wifiPassword", "");
    }

    public void setWifiPassword(String wifiPassword) {
        editor.putString("wifiPassword", wifiPassword);
        editor.commit();
    }

    public String getParentTel() {
        return ps.getString("parentTel", "");
    }

    public void setParentTel(String parentTel) {
        editor.putString("parentTel", parentTel);
        editor.commit();
    }
}
