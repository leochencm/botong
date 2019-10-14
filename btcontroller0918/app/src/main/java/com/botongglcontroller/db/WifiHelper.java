package com.botongglcontroller.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WifiHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wifi.db";
    private static final int DATABASE_VERSION = 1;

    public WifiHelper(Context context) {
        //CursorFactory设置为null,使用默认�?
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第�?次被创建时onCreate会被调用  
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS wifi" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT , serialnumber VARCHAR,wifiname VARCHAR)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不�?,即会调用onUpgrade  
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE news ADD COLUMN other STRING");
    }
}  