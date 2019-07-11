package com.example.yyy.medicinekit.MyDrug;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    //建表语句
    public static final String CREATE_MEDICINE="create table MEDICINE("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"type text,"
            +"intro text,"
            +"price text,"
            +"date text,"
            +"period text,"
            +"piece text,"
            +"form text,"
            +"function text,"
            +"usage text,"
            +"warning text,"
            +"size text,"
            +"eaten text,"
            +"number text,"
            +"frequency integer,"
            +"timepiece integer,"
            +"time1a text,"
            +"time1b text,"
            +"time2a text,"
            +"time2b text,"
            +"time3a text,"
            +"time3b text,"
            +"time4a text,"
            +"time4b text)";

    public static final String CREATE_DAILY="create table DAILY("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"frequency integer,"
            +"timepiece integer,"
            +"piece integer)";

    public static final String CREATE_ACCOUNT="create table ACCOUNT("
            +"id integer primary key autoincrement,"
            +"account text,"
            +"key integer,"
            +"Signin integer)";

    public static final String CREATE_ARTICLE="create table ARTICLE("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"intro integer,"
            +"contain text,"
            +"ImageId integer)";

    public static final String CREATE_TIMETABLE="create table TIMETABLE("//各段吃了沒
            +"id integer primary key autoincrement,"
            +"number text,"
            +"time1 text,"
            +"time2 text,"
            +"time3 text,"
            +"time4 text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_MEDICINE);
        db.execSQL(CREATE_ACCOUNT);
        db.execSQL(CREATE_DAILY);
        db.execSQL(CREATE_ARTICLE);
        db.execSQL(CREATE_TIMETABLE);
        //Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists MEDICINE");
        db.execSQL("drop table if exists ACCOUNT");
        db.execSQL("drop table if exists DAILY");
        db.execSQL("drop table if exists ARTICLE");
        db.execSQL("drop table if exists TIMETABLE");
//        db.execSQL("drop table if exists TIMETABLE");
        onCreate(db);
    }


}
