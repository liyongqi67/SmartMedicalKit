package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.yyy.medicinekit.R;


public class Splash extends Activity {

    MYDB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        mydb = new MYDB(this);
//        mydb.insertmedicine("name","type","intro","price",2015,3,30,"1","1","1","1","1","yes","1",1,1,"--","--","--","--");
//        mydb.setArticle("theArticle","theintro","thecontain",1);
//        mydb.setArticle("theArticle","theintro","thecontain",1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=null;
//                if(mydb.getAccountList().size()==0) {
//                    i = new Intent(Splash.this, SignUpActivity.class);

//                }else {
                  i = new Intent(Splash.this, SignInActivity.class);
//                i = new Intent(Splash.this, Addpage.class);
//                }
//
//                i = new Intent(Splash.this, MainActivity1.class);

//                i = new Intent(Splash.this, Clock.class);


                startActivity(i);

                //启动主Activity后销毁自身
                finish();
            }
        }, 1000);
    }
}
