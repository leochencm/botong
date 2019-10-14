package com.botongglcontroller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.botongglcontroller.beans.Boilers;

import java.util.ArrayList;
import java.util.List;

public class WifiManager {
    private WifiHelper helper;
    private SQLiteDatabase db;

    public WifiManager(Context context) {
        helper = new WifiHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //�?以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate�?  
        db = helper.getWritableDatabase();
    }

    public void deletewifi(Boilers person) {
        db.delete("wifi", "serialnumber = ?", new String[]{String.valueOf(person.serialnumber)});
    }

    public void updateTye(Boilers person) {
        ContentValues cv = new ContentValues();
        cv.put("wifiname", person.wifiname);
        db.update("wifi", cv, "serialnumber = ?", new String[]{person.serialnumber});
    }

    public String[] querytewifi(Boilers person) {
        String serialnumber = null, wifiname = null;
        String[] str = new String[0];
        Cursor cursor = db.query("wifi", new String[]{"serialnumber", "wifiname"}, "serialnumber = ?", new String[]{person.serialnumber}, null, null, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
                serialnumber = cursor.getString(0);
                wifiname = cursor.getString(1);
                Log.i("serialnumber", serialnumber + wifiname);
                str = new String[]{serialnumber, wifiname,};
            }
            cursor.close();//关闭结果集
        }
        return str;
    }

    /**
     * 查询数据库中的�?�条�?.
     *
     * @return
     */
    public long allCaseNum() {
        String sql = "select count(*) from wifi";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * add persons
     *
     * @param
     */
    public void add(List<Boilers> BoilerData) {
        db.beginTransaction();  //�?始事�?  
        try {
            for (Boilers boiler : BoilerData) {
                db.execSQL("INSERT INTO wifi VALUES(null,?, ?)", new Object[]{boiler.serialnumber, boiler.wifiname});
            }
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {
            db.endTransaction();    //结束事务  
        }
    }


    /**
     * delete old person
     *
     * @param
     */
    public void clearboiler() {
        db.delete("wifi", null, null);
    }

    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<Boilers> query() {
        ArrayList<Boilers> persons = new ArrayList<Boilers>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            Boilers person = new Boilers();
            person.serialnumber = c.getString(c.getColumnIndex("serialnumber"));
            person.wifiname = c.getString(c.getColumnIndex("wifiname"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM wifi", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}  