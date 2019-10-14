package com.botongglcontroller.beans;

import java.io.Serializable;

public class DealerList implements Serializable {
    private String dealertel, dealercompany;

    public DealerList(String dealertel, String dealercompany) {
        this.dealertel = dealertel;
        this.dealercompany = dealercompany;
    }

    public DealerList() {
    }

    public String getDealertel() {
        return dealertel;
    }

    public void setDealertel(String dealertel) {
        this.dealertel = dealertel;
    }

    public String getDealercompany() {
        return dealercompany;
    }

    public void setDealercompany(String dealercompany) {
        this.dealercompany = dealercompany;
    }
}
