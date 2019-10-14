package com.botongglcontroller.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hasee on 2016/12/26.
 */

public class Boilers implements Serializable {

    public String serialnumber, model, workstate, connstate, image, showenable, wifiname, wifipwd, oid, apikey;
    ArrayList<BoilersPara> para = new ArrayList<>();

    public Boilers(String serialnumber, String model, String workstate, String connstate, String image, String showenable) {

        this.serialnumber = serialnumber;
        this.model = model;
        this.workstate = workstate;
        this.connstate = connstate;
        this.image = image;
        this.showenable = showenable;
    }

    public Boilers(String serialnumber, String wifiname, String wifipwd) {
        this.serialnumber = serialnumber;
        this.wifiname = wifiname;
        this.wifipwd = wifipwd;
    }

    public Boilers() {

    }

    public String getShowenable() {
        return showenable;
    }

    public void setShowenable(String showenable) {
        this.showenable = showenable;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {

        this.wifiname = wifiname;
    }

    public String getApikey() {
        return apikey;
    }//onenet平台产品apikey

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getWifipwd() {
        return wifipwd;
    }

    public void setWifipwd(String wifipwd) {
        this.wifipwd = wifipwd;
    }

    public ArrayList getPara() {
        return para;
    }

    public void setPara(ArrayList para) {
        this.para = para;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getWorkstate() {
        return workstate;
    }

    public void setWorkstate(String workstate) {
        this.workstate = workstate;
    }

    public String getConnstate() {
        return connstate;
    }

    public void setConnstate(String connstate) {
        this.connstate = connstate;
    }

    public String getSerialnumber() {

        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }
}
