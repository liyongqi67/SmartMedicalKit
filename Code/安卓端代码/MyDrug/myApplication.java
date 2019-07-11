package com.example.yyy.medicinekit.MyDrug;

import android.app.Application;

/**
 * Created by YYY on 2016/9/6.
 */

public class myApplication extends Application {

    private static String userid = " ";
    static String message = " ";
    static String article = " ";

    @Override
    public void onCreate()
    {
        super.onCreate();// 初始化全局变量
    }

    public void setid(String value)
    {
        this.userid = value;
    }

    public String getid()
    {
        return userid;
    }

    public void setmessage(String value)
    {
        this.message = value;
    }

    public String getmessage()
    {
        return message;
    }

    public void setarticle(String value)
    {
        this.article = value;
    }

    public String getarticle()
    {
        return article;
    }
}
