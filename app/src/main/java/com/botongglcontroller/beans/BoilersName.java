package com.botongglcontroller.beans;

import java.io.Serializable;

/**
 * Created by hasee on 2016/12/26.
 */

public class BoilersName implements Serializable {

    String parakey;
    String paravalue;
    String paraunit;
    String paratypes;
    String parakind;
    String pararele;
    String paraonly;
    String name;
    String unit;
    String visiable;
    String alert;
    String value;

    public String getParakey() {
        return parakey;
    }

    public void setParakey(String parakey) {
        this.parakey = parakey;
    }

    public String getParavalue() {
        return paravalue;
    }

    public void setParavalue(String paravalue) {
        this.paravalue = paravalue;
    }

    public String getParaunit() {
        return paraunit;
    }

    public void setParaunit(String paraunit) {
        this.paraunit = paraunit;
    }

    public String getParatypes() {
        return paratypes;
    }

    public void setParatypes(String paratypes) {
        this.paratypes = paratypes;
    }

    public String getParakind() {
        return parakind;
    }

    public void setParakind(String parakind) {
        this.parakind = parakind;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getVisiable() {
        return visiable;
    }

    public void setVisiable(String visiable) {
        this.visiable = visiable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPararele() {
        return pararele;
    }

    public void setPararele(String pararele) {
        this.pararele = pararele;
    }

    public String getParaonly() {
        return paraonly;
    }

    public void setParaonly(String paraonly) {
        this.paraonly = paraonly;
    }

//    public BoilersName(Parcel in) {
//        parakey = in.readString();
//        paravalue = in.readString();
//        paraunit = in.readString();
//        paratypes = in.readString();
//        parakind = in.readString();
//    }
//
//    public static final Creator<BoilersName> CREATOR = new Creator<BoilersName>() {
//        @Override
//        public BoilersName createFromParcel(Parcel in) {
//            return new BoilersName(in);
//        }
//
//        @Override
//        public BoilersName[] newArray(int size) {
//            return new BoilersName[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(parakey);
//        parcel.writeString(paravalue);
//        parcel.writeString(paraunit);
//        parcel.writeString(paratypes);
//        parcel.writeString(parakind);
//    }
}
