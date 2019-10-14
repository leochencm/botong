package com.botongglcontroller.beans;

public class SellerBoilers {
    private String sid;
    private String seller;

    public SellerBoilers(String sid,String seller) {
        this.sid = sid;
        this.seller=seller;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
