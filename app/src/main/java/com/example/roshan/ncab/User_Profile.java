package com.example.roshan.ncab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by roshan on 5/25/17.
 */

public class User_Profile extends SQLiteOpenHelper {

    public static final String database_name="nCab";

    //create table
    public static final String table_name="user_profile";
    public static final String col_1=" _id";
    public static final String col_2="username";
    public static final String col_3="password";
    public static final String col_4="state";

    public User_Profile(Context context) {
        super(context, database_name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + table_name + "( _id integer primary key autoincrement ,username text,password text,state text)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + table_name);
        // db.execSQL("ALTER TABLE "+ table_name +" ADD "+ col_1 +" INTEGER PRIMARY KEY AUTOINCREMENT");
        onCreate(db);
    }

    public void insertData(String username, String password, String state){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(col_2,username);
        values.put(col_3,password);
        values.put(col_4, state);
        int u = db.update("user_profile", values, "username=?", new String[]{username});
        if (u == 0) {
            db.insertWithOnConflict("user_profile", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public Cursor getUserData(){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cr =  db.rawQuery( "select _id as _id,  username, password, state from user_profile",null);

        if (cr != null) {
            cr.moveToFirst();
        }
        Log.d("Cursor Size ", cr.getCount() +"");
        cr.getCount();
        return cr;

    }
}
