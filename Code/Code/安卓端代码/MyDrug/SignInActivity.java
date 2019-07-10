package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;

import android.app.AlarmManager;
import android.content.Intent;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class SignInActivity extends Activity {
//藥品列表和各階段服用時間都加進了數據庫裏面
    TextView accounttext;
    EditText keyedittext;
    Button signinbutton, signupbutton;
    String outcome = "";

    private MYDB mydb;
    private myApplication mapp;

    AlarmManager alarmManager;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x121://登陸
                    Log.e("outcome登陸", outcome);
                    if ("s".equals(outcome)) {
                        Toast.makeText(SignInActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignInActivity.this, MainActivity1.class);
                        SignInActivity.this.startActivity(i);
                    }

                    //藥品列表
                    request("藥品", "P" + accounttext.getText().toString() + ";flash;",null);
                    break;
                case 0x122://文章
                    Log.e("outcome文章", outcome);
                    //mapp.setarticle(outcome);
                    if (!(outcome.equals("none") || outcome.equals("")))
                        operateArticle(outcome);

                    //登陆
                    request("登陸", "Psdu;1;" + accounttext.getText().toString() + "-" + keyedittext.getText() + ";",null);

                    break;
                case 0x123://藥品
                    //boolean ff = (boolean) msg.obj;
                    Log.e("outcome藥品", outcome);
                    //mapp.setmessage(outcome);
                    if (!(outcome == null || outcome.equals("none")))
                        operateMedicine(outcome);
                    break;
                case 0x125://request detail之後
                    String[] infor = outcome.split("-");
                    Medicine medicine = (Medicine) msg.obj;
                    if (infor.length == 4&&medicine != null) {
                        switch (infor[0]) {
                            case "3":
                            case "4":
                            case "5":
                            case "6":
                            case "7":
                            case "8":
                                mydb.changetime(medicine.getnumber(), "time1", infor[0] + ":" + infor[1]);
                                break;
                            case "9":
                            case "10":
                            case "11":
                            case "12":
                            case "13":
                            case "14":
                                mydb.changetime(medicine.getnumber(), "time2", infor[0] + ":" + infor[1]);
                                break;
                            case "15":
                            case "16":
                            case "17":
                            case "18":
                            case "19":
                            case "20":
                                mydb.changetime(medicine.getnumber(), "time3", infor[0] + ":" + infor[1]);
                                break;
                            case "21":
                            case "22":
                            case "23":
                            case "0":
                            case "1":
                            case "2":
                                mydb.changetime(medicine.getnumber(), "time4", infor[0] + ":" + infor[1]);
                                break;
                        }
                        mydb.changeTimepiece(medicine.getnumber(), infor[2]);//用藥量
                    }
                   
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        mydb = new MYDB(this);


        mapp = new myApplication();

        mydb.clearTable("MEDICINE");
        mydb.clearTable("DAILY");
        mydb.clearTable("ACCOUNT");
        mydb.clearTable("ARTICLE");

        accounttext = (EditText) findViewById(R.id.accounttext);
        keyedittext = (EditText) findViewById(R.id.key);
        signinbutton = (Button) findViewById(R.id.signinbutton);
        signupbutton = (Button) findViewById(R.id.signupbutton);

        signinbutton.setOnClickListener(new View.OnClickListener() {//登录
            @Override
            public void onClick(View view) {

                //文章
                request("article", "P" + accounttext.getText().toString() + ";a;", null);

                mapp.setid(accounttext.getText().toString());

                mydb.clearTable("ARTICLE");
                //mydb.closedatabase();

            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {//登录
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void request(final String tag, final String str,final Medicine medicine) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Socket clientSocket = new Socket("192.168.1.156", 8889);
                    BufferedWriter bufferedWriter = null;
                    Log.e("tag",tag);
                    try {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "gbk"));
                        bufferedWriter.write(str + "\r\n");
                        bufferedWriter.flush();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "gbk"));
                        Log.e("Client", "开始读取");
                        //String str = "";
                        String temp;
                        outcome = "";
                        while ((temp = in.readLine()) != null) {
                            // str += temp;
                            outcome += temp;
                        }
//                        Log.e("结果", outcome);
                        bufferedWriter.close();
                        in.close();
                        clientSocket.close();

                        if ("登陸".equals(tag))
                            handler.sendEmptyMessage(0x121);
                        else if("article".equals(tag)) {
                            handler.sendEmptyMessage(0x122);
                        }
                        else if("藥品".equals(tag)){
                            //handler.sendEmptyMessage(0x123);
                            Message message = new Message();
                            message.obj = false;
                            message.what = 0x123;
                            handler.sendMessage(message);
                        }
                        else if("4".equals(tag))
                            handler.sendEmptyMessage(0x124);
                        else if("detail".equals(tag))
                            handler.sendEmptyMessage(0x125);

                    } catch (IOException e) {
                        Log.d("Client", "BufferedWriter出错");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void operateArticle(String article) {
        String[] Articlelist = article.split(";");
        String[] infor;
        for(int i=0;i<Articlelist.length;i++) {
            infor = Articlelist[i].split("-");
            Log.e("infor.articlesize", infor.length + "");
            if(infor.length==4){
                mydb.setArticle(infor[0],infor[1],infor[2],1);
                mydb.setArticle(infor[0],infor[1],infor[2],1);
            }
        }
        Log.e("article.size", "" + mydb.getArticlelist().size());
        //mydb.closedatabase();
    }

    public void operateMedicine(String medicine) {
        String[] medicinelist = medicine.split(";");
        String[] infor;
        for(int i=0;i<medicinelist.length;i++) {

            infor = medicinelist[i].split("-");
            if (infor.length>14&&!mydb.checkExist(infor[14])){
                mydb.insertmedicine(infor[1],infor[2],infor[3],infor[4],
                        infor[5],infor[6], infor[7],infor[8],infor[9],infor[10],infor[11],infor[12],infor[13],infor[14],
                        0,0,"--","--","--","--", "--", "--", "--", "--");
                mydb.setTimeTable(infor[13]);//初始化每个时段没有用药
            }


            for(int a=0;a<infor.length;a++){
                Log.e("medicine",infor[a]);
            }

        }


        Log.e("medicinelist.length",medicinelist.length+"");


        List<Medicine> list = mydb.getDailylist();//eaten=yes;

        for(int i=0;i<list.size();i++) {
            request("detail", "P" + mapp.getid() + ";drugEate;" + list.get(i).getnumber() + ";",list.get(i));
        }
    }


}
