package com.botongglcontroller.beans;

import java.io.Serializable;

/**
 * Created by hasee on 2017/12/5.
 */

public class GetBill implements Serializable {
    /**
     * [{
     * “id”：”账单编号”,
     * “bill”：”账单名称”,
     * “createdate”：”账单时间”,
     * “enddate”：”到期时间”,
     * “money”：”账单金额”,
     * “payway”:”支付方式”
     * “payed”:”是否已支付”
     * }]
     */

    private String id;
    private String bill;
    private String createdate;
    private String enddate;
    private String money;
    private String payway;
    private String payed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }
}
