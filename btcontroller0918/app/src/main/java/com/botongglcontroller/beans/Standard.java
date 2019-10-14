package com.botongglcontroller.beans;

import java.io.Serializable;

/**
 * Created by hasee on 2017/12/7.
 */

public class Standard implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    private String maintenance;
}
