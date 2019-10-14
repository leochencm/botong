package com.botongglcontroller.db;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {  
    private DBHelper helper;  
    private SQLiteDatabase db;  
      
    public DBManager(Context context) {  
        helper = new DBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //�?以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate�?  
        db = helper.getWritableDatabase();  
    }  
      
    
    /** 
     * 查询数据库中的�?�条�?. 
     * @return 
     */  
    public long allCaseNum( ){  
        String sql = "select count(*) from news";  
        Cursor cursor = db.rawQuery(sql, null);  
        cursor.moveToFirst();  
        long count = cursor.getLong(0);  
        cursor.close();  
        return count;  
    }  
    /** 
     * add persons 
     * @param
     */  
    public void add(List<NewsData> newsdata) {  
        db.beginTransaction();  //�?始事�?  
        try {  
            for (NewsData news : newsdata) {
                db.execSQL("INSERT INTO news VALUES(null,?,?,?,?,?,?,?,?)", new Object[]{ news.id,news.UID,news.title,news.msg, news.date,news.type,news.point,news.isRead});
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
      
    /** 
     * update person's age 
     * @param person 
     */  
    public void updateTye(NewsData person) {  
        ContentValues cv = new ContentValues();  
        cv.put("isRead", person.isRead);
        db.update("news", cv, "id = ?", new String[]{person.id});
    }  
      
	
    /** 
     * delete old person 
     * @param person 
     */  
    public void deletenews(NewsData person) {  
        db.delete("news", "id = ?", new String[]{String.valueOf(person.id)});
    }  
    /** 
     * delete old person 
     * @param
     */  
    public void clearnews() {  
    	db.delete("news", null,null);  
    }  
      
    /** 
     * query all persons, return list 
     * @return List<Person> 
     */  
    public List<NewsData> query() {  
        ArrayList<NewsData> persons = new ArrayList<NewsData>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
        	NewsData person = new NewsData();
            person.id = c.getString(c.getColumnIndex("id")) ;
            person.UID = c.getString(c.getColumnIndex("UID")) ;
            person.title = c.getString(c.getColumnIndex("title"));
            person.msg = c.getString(c.getColumnIndex("msg")) ;
            person.date = c.getString(c.getColumnIndex("date"));
            person.type = c.getString(c.getColumnIndex("type"));
            person.point = c.getString(c.getColumnIndex("point"));
            person.isRead = c.getString(c.getColumnIndex("isRead"));
            persons.add(person);
        }  
        c.close();  
        return persons;  
    }  
      
    /** 
     * query all persons, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM news", null);  
        return c;  
    }  
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        db.close();  
    }  
}  