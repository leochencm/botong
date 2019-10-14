package com.botongglcontroller.db;


public class NewsData {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String msg, title, time, type, date, isRead;

    public String getIsread() {
        return isRead;
    }

    public void setIsread(String isread) {
        this.isRead = isread;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String id, UID, point;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NewsData() {
    }

    public NewsData(String id, String UID, String title, String msg, String date, String type,  String point, String isRead) {
        this.UID = UID;
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.date = date;
        this.type = type;
        this.point = point;
        this.isRead = isRead;
    }

    public NewsData(String id, String UID, String title, String msg, String date, String type, String isRead) {
        this.UID = UID;
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.date = date;
        this.type = type;
        this.isRead = isRead;
    }




}
