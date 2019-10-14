package com.botongglcontroller.onenetdb;

import java.io.Serializable;

public class DsByte implements Serializable {
    private  String index;
    //@SerializedName("index")

    public String getByteDate() {
        return index;
    }
    public void setByteDate(String index) {
        this.index = index;
    }
}
