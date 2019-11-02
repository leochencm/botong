package com.botongglcontroller.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by hasee on 2016/12/26.
 */

public class BoilersPara implements Serializable {

    public String addr;
    public String name;
    public String addr_int;
    public String kind;
    public String kindname;
    public String visiable;
    public String unit;
    public String len;
    public String value;
    public String model;
    public String type;
    long time;

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getTime() {
        return time;
    }

    public void setTime(String strtime)
            throws ParseException {
        String formatType = "yyyy-MM-dd HH:mm:ss";
        this.time = stringToLong(strtime, formatType);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr_int() {
        return addr_int;
    }

    public void setAddr_int(String addr_int) {
        this.addr_int = addr_int;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
        switch (Integer.parseInt(kind)) {
            case 1:
                kindname = "开关量";
                break;
            case 2:
                kindname = "模拟量";
                break;
            case 3:
                kindname = "参数";
                break;
            case 4:
                kindname = "状态";
                break;
            case 5:
                kindname = "报警";
                break;

        }
    }

    public String getKindname() {
        return kindname;
    }

    public String getVisiable() {
        return visiable;
    }

    public void setVisiable(String visiable) {
        this.visiable = visiable;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
