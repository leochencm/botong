package com.botongglcontroller.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.botongglcontroller.beans.Boilers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BoilerManager {
    private BoilerHelper helper;
    private SQLiteDatabase db;

    public BoilerManager(Context context) {
        helper = new BoilerHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //�?以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate�?  
        db = helper.getWritableDatabase();
    }


    /**
     * 查询数据库中的�?�条�?.
     *
     * @return
     */
    public long allCaseNum() {
        String sql = "select count(*) from boiler";
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
        db.beginTransaction();
        try {
            for (Boilers boiler : BoilerData) {
                db.execSQL("INSERT INTO boiler VALUES(null,?, ?, ?, ?,?,?,?)",
                        new Object[]{boiler.serialnumber, boiler.model, boiler.workstate, boiler.connstate, boiler.image, boiler.oid, boiler.apikey});
            }
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {
            db.endTransaction();    //结束事务  
        }
    }

    public Boilers querymodel(String sid) {
        Boilers boiler = new Boilers();
        String model = "";
        Cursor cursor = db.query("boiler", new String[]{"*"}, "serialnumber = ?", new String[]{sid}, null, null, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
//                serialnumber = cursor.getString(0);
                boiler.serialnumber = cursor.getString(1);
                boiler.model = cursor.getString(2);
                boiler.workstate = cursor.getString(3);
                boiler.connstate = cursor.getString(4);
                boiler.image = cursor.getString(5);
                boiler.oid = cursor.getString(6);
                boiler.apikey = cursor.getString(7);

            }
            cursor.close();//关闭结果集
        } else {
            boiler.serialnumber = "none";
        }
        return boiler;
    }

    public String[] queryshown(Boilers person) {
        String serialnumber = null, showenable = null;
        String[] str = new String[0];
        Cursor cursor = db.query("boiler", new String[]{"*"}, "serialnumber = ?", new String[]{person.serialnumber}, null, null, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
//                serialnumber = cursor.getString(0);
                showenable = cursor.getString(0);
                Log.i("serialnumber", person.serialnumber + showenable);
                str = new String[]{person.serialnumber, showenable};
            }
            cursor.close();//关闭结果集
        }

        return str;
    }

    public void deletenews(String person) {
        db.delete("boiler", "serialnumber = ?", new String[]{person});
    }

    /**
     * delete old person
     *
     * @param
     */
    public void clearboiler() {
        db.delete("boiler", null, null);
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
            person.model = c.getString(c.getColumnIndex("model"));
            person.workstate = c.getString(c.getColumnIndex("workstate"));
            person.connstate = c.getString(c.getColumnIndex("connstate"));
            person.image = c.getString(c.getColumnIndex("image"));
            person.showenable = c.getString(c.getColumnIndex("showenable"));
            person.wifiname = c.getString(c.getColumnIndex("wifiname"));
            persons.add(person);
        }
        c.close();
        Set set = new LinkedHashSet<String>();
        set.addAll(persons);
        persons.clear();
        persons.addAll(set);
        return persons;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM boiler group by serialnumber", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}  