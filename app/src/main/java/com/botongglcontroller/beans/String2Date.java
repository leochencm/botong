package com.botongglcontroller.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class String2Date {
    long time;
    String formatType = "yyyy-MM-dd HH:mm:ss";

    public long stringToLong(String strTime, String formatType)
            throws ParseException {
        this.formatType = formatType;
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

    public long stringToLong(String strTime)
            throws ParseException {
        this.formatType = formatType;
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        if (date == null) {
            return 0;
        } else {
            return date.getTime();
        }
    }

}
