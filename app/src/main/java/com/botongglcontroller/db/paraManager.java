package com.botongglcontroller.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.botongglcontroller.beans.Boilers;
import com.botongglcontroller.beans.BoilersPara;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class paraManager {
    private BoilerparaHelper helper;
    private SQLiteDatabase db;

    public paraManager(Context context) {
        helper = new BoilerparaHelper(context);
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
        String sql = "select count(*) from brpara";
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
    public void add(List<BoilersPara> Boilerpara) {
        db.beginTransaction();
        try {
            for (BoilersPara para : Boilerpara) {
                db.execSQL("INSERT INTO brpara VALUES(null,?,?, ?, ?,?,?,?,?)",
                        new Object[]{para.addr, para.name, para.model, para.visiable, para.kind, para.unit, para.len, para.addr_int});
            }
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {
            db.endTransaction();    //结束事务  
        }
    }

    public ArrayList<BoilersPara> queryshown(String model) {
        ArrayList<BoilersPara> listpara = new ArrayList<BoilersPara>();
        Cursor cursor = db.query("brpara", new String[]{"*"}, "model = ?", new String[]{model}, null, null, null);
        //解析Cursor中的数据
        if (cursor != null && cursor.getCount() > 0) {//判断cursor中是否存在数据
            //循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {//条件，游标能否定位到下一行
                //获取数据
//                serialnumber = cursor.getString(0);
                BoilersPara bp = new BoilersPara();
                bp.setAddr(cursor.getString(1));
                bp.setName(cursor.getString(2));
                bp.setModel(cursor.getString(3));
                bp.setVisiable(cursor.getString(4));
                bp.setKind(cursor.getString(5));
                bp.setUnit(cursor.getString(6));
                bp.setLen(cursor.getString(7));
                bp.setAddr_int(cursor.getString(8));
                listpara.add(bp);
            }
            cursor.close();//关闭结果集
        }

        return listpara;
    }

    public void deletepara(String sid) {
        db.delete("brpara", "model = ?", new String[]{sid});
    }

    /**
     * delete old person
     *
     * @param
     */
    public void clearpara() {
        db.delete("brpara", null, null);
    }


    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM brpara group by model", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}  