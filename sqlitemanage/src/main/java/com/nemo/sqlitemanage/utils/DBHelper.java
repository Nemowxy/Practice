package com.nemo.sqlitemanage.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nemo on 2016/7/22 0022.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final int VERSION=1;
    private String tableName="user";
    private SQLiteDatabase database = null;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public DBHelper(Context context,String name){
        this(context,name,VERSION);
    }
    //带三个参数的构造函数，调用的是带所有参数的构造函数
    public DBHelper(Context context,String name,int version){
        this(context, name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("create OK");
    }

    public void createTable(String name){
        if (database == null){
            database = getReadableDatabase();
        } else {
            String sql = "create table "+name+"(id int,name varchar(20))";
            database.execSQL(sql);
        }
    }

    public SQLiteDatabase getDatabase(){
        database = this.getReadableDatabase();
        return database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
