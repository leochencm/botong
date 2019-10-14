package com.botongglcontroller.beans;

import java.io.Serializable;

/**
 * Created by hasee on 2017/12/5.
 */

public class GetBilldetail implements Serializable {
/**
 *
 * “boiler”：”锅炉编号”,
 “model”：”锅炉型号”,
 “money”：”账单金额”,
 */
private String boiler;
    private String model;
    private String money;
    private String billid;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getBoiler() {
        return boiler;
    }

    public void setBoiler(String boiler) {
        this.boiler = boiler;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
