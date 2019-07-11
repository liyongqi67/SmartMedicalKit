package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Clock extends Activity {

    MYDB mydb;

    TextView dateText;
    Button addbutton;
    LinearLayout l1,l2,l3,l4;

    NotificationManager manager;
    Notification myNotication;

    String text1;


    boolean boodone = false;

    //LinearLayout linearLayout;

    Spinner spinner,spinner1;
    private ArrayAdapter<String> adapter = null;

    private static final String [] frequency = {"1","2","3","4","5"};
    private static final String [] interval = {"五分鐘","十分鐘","一刻鐘","半小時"};

    private AlarmManager alarmManager;
    private PendingIntent pi;

    private int times = 0 , interval1  = 0 , lasttime = 0;

    ArrayList<Medicine> list;

    myApplication mapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);


        mapp = new myApplication();
        mydb = new MYDB(this);
        list = mydb.getTimeTable();//有time的medicinelist
//按理説衹要不是新加進去的藥品，并且設置過時間，第二次登陸仍然能在Clock Activity中看見

        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,frequency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);

//左
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        //showToast("Spinner1: position="position" id="id);
                        switch (position) {
                            case 0:
                                times = 1;

                                break;
                            case 1:
                                times = 2;

                                break;
                            case 2:
                                times = 3;
                                break;
                            case 3:
                                times = 4;
                                break;
                            case 4:
                                times = 5;
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );


//右
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,interval);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(adapter);
        spinner1.setVisibility(View.VISIBLE);

        spinner1.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {
                    Log.e(position + "", id + "");
                    switch (position) {
                        case 0:
                            interval1 = 5;
                            boodone=true;
                            //onCreate(null);
                            break;
                        case 1:
                            interval1 = 10;
                            break;
                        case 2:
                            interval1 = 15;
                            break;
                        case 3:
                            interval1 = 30;
                            break;
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            }
        );


        lasttime = times*interval1;


        text1=" ";//推送消息
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Intent intent = new Intent(Clock.this, ClockActivity.class);
        pi = PendingIntent.getActivity(Clock.this, 0, intent, 0);


        dateText = (TextView)findViewById(R.id.dateText);
        addbutton = (Button)findViewById(R.id.addbutton);

        l1 = (LinearLayout)findViewById(R.id.tableMorning);
        l2 = (LinearLayout)findViewById(R.id.tableNoon);
        l3 = (LinearLayout)findViewById(R.id.tableAfternoon);
        l4 = (LinearLayout)findViewById(R.id.tableNight);


        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Clock.this, ClockEdit.class);
                startActivity(intent);
                finish();
            }
        });


        change();

        if(!boodone)//已經在chang()裏面更改了text1
            show(text1);//顯示notification


        //PollingUtils.startPollingService(this, 5, PollingService.class, PollingService.ACTION);//輪詢
    }




    public void change() {

        String hour="";
        String minute="";
        Log.e("list.size",list.size()+"");
        for(int i=0;i<list.size();i++) {
            Medicine md = list.get(i);

            if(!md.gettime1a().equals("--")) {
                //發送每個藥的持續時間
                request("lasttime", "P"+mapp.getid()+";m;"+"attentionTime;"+lasttime+";"+md.getnumber()+";",md);

                //吃了沒
                request("time1", "P" + mapp.getid() + ";done;" + md.getnumber() + ";",md);

                //request changeTime text
                hour+=md.gettime1a();
                minute+=md.gettime1b();

                //notification的text
                text1 += md.getName() +"     " + md.gettime1a()+":"+md.gettime1b() + "";
                TextView tx = new TextView(this);
                tx.setText(md.getName() +"     "+ md.gettime1a()+":"+md.gettime1b());


                LinearLayout linearLayout = new LinearLayout(Clock.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);//子橫向


                if(boodone){//檢查吃了沒
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.done);
                    linearLayout.addView(imageView);
                }

                linearLayout.addView(tx);

                l1.addView(linearLayout);


//                if(c!=null)
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
            if(!md.gettime2a().equals("--")) {
                request("lasttime", "P"+mapp.getid()+";m;"+"attentionTime;"+lasttime+";"+md.getnumber()+";",md);//持續時間一樣

                text1 += md.getName() +"     " + md.gettime2a()+":"+md.gettime2b() + "";
                TextView tx = new TextView(this);
                tx.setText(md.getName() + "     " + md.gettime2a() + ":" + md.gettime2b());

                request("time2", "P" + mapp.getid() + ";done;" + md.getnumber() + ";", md);
                //request changeTime text
                hour+="["+md.gettime2a();
                minute+="["+md.gettime2b();

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);//子橫向
                if(boodone) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.done);
                    linearLayout.addView(imageView);
                }

                linearLayout.addView(tx);

                l2.addView(linearLayout);

//                if(c!=null)
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
            if(!md.gettime3a().equals("--")) {
                request("lasttime", "P"+mapp.getid()+";m;"+"attentionTime;"+lasttime+";"+md.getnumber()+";",md);//持續時間一樣

                text1 += md.getName() +"     " + md.gettime3a()+":"+md.gettime3b() + "";
                TextView tx = new TextView(this);
                tx.setText(md.getName() + "     " + md.gettime3a() + ":" + md.gettime3b());

                request("time3", "P" + mapp.getid() + ";done;" + md.getnumber() + ";",md);

                //request changeTime text
                hour+="["+md.gettime3a();
                minute+="["+md.gettime3b();

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);//子橫向
                if(boodone){

                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.done);
                    linearLayout.addView(imageView);
                }

                linearLayout.addView(tx);

                l3.addView(linearLayout);


//                if(c!=null)
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
            if(!md.gettime4a().equals("--")) {
                request("lasttime", "P"+mapp.getid()+";m;"+"attentionTime;"+lasttime+";"+md.getnumber()+";",md);//持續時間一樣

                text1 += md.getName() +"     " + md.gettime4a()+":"+md.gettime4b() + "";
                TextView tx = new TextView(this);
                tx.setText(md.getName() +"     "+ md.gettime4a()+":"+md.gettime4b());

                request("time4", "P" + mapp.getid() + ";done;" + md.getnumber() + ";", md);

                //request changeTime text
                hour+="["+md.gettime4a();
                minute+="["+md.gettime4b();

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);//子橫向
                if(boodone){
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.done);
                    linearLayout.addView(imageView);
                }

                linearLayout.addView(tx);

                l4.addView(linearLayout);


//                if(c!=null)
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
            // P用户名;m;要修改的英文名/eaten;值;number;
//            request("requestChangeHour", "P" + mapp.getid() + "m;drugHour;"+ + md.getnumber() + ";",md);
//            request("requestChangeMinute", "P" + mapp.getid() + "m;drugMinute;"+ + md.getnumber() + ";",md);

        }
        text1 = text1.replace("\\n","\n");
    }

    public void show(String text) { //推送
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //API level 11
        Intent intent = new Intent(this,ClockActivity.class);
        Bundle bundle = new Bundle();
       // bundle.putSerializable("alarmManager",alarmManager);
       // bundle.putSerializable("PendingIntent",pi);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(Clock.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(Clock.this);

        builder.setTicker("this is ticker text");
        builder.setContentTitle("用药提醒");
        builder.setContentText(text);
        builder.setNumber(2);
        builder.setSmallIcon(R.drawable.alarmclock);

        builder.setContentIntent(pendingIntent);

        builder.setOngoing(false);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        builder.build();

        myNotication = builder.getNotification();
        myNotication.defaults = Notification.DEFAULT_VIBRATE;//设置震动
        manager.notify(11, myNotication);// 第一个参数唯一的标识该Notification，第二个参数就是Notification对象。

    }


    String outcome = "";
    public void request(final String tag, final String str,final Medicine object) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Socket clientSocket = new Socket("192.168.1.156", 8889);
                    BufferedWriter bufferedWriter = null;
                    try {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "gbk"));
                        bufferedWriter.write(str + "\r\n");
                        bufferedWriter.flush();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "gbk"));
                        Log.e("Clock", "开始读取");
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

                        if("time1".equals(tag))
                            handler.sendEmptyMessage(0x121);
                        else if("time2".equals(tag))
                            handler.sendEmptyMessage(0x122);
                        else if("time3".equals(tag))
                            handler.sendEmptyMessage(0x123);
                        else if("time4".equals(tag))
                            handler.sendEmptyMessage(0x124);
                        else if("detail".equals(tag)) {

                            Message message = new Message();
                            message.obj = object;
                            message.what = 0x125;
                            handler.sendMessage(message);
                        }

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x121:
                    if(outcome.equals("0")){
                    Log.e("done","0");
                        boodone = true;
                    }else if(outcome.equals("1")) {
                        Log.e("done","1");
                        boodone = false;
                    }
                break;
                case 0x122:
                    if(outcome.equals("0")){
                        Log.e("done","0");
                        boodone = true;
                        onCreate(null);
                    }else if(outcome.equals("1")) {
                        boodone = false;
                        Log.e("done","1");
                    }
                    break;
                case 0x123:
                    if(outcome.equals("0")){
                        Log.e("done","0");
                        boodone = true;
                        onCreate(null);
                    }else if(outcome.equals("1")) {
                        boodone = false;
                        Log.e("done","1");
                    }
                    break;
                case 0x124:
                    if(outcome.equals("0")){
                        Log.e("done","0");
                        boodone = true;
                        onCreate(null);
                    }else if(outcome.equals("1")) {
                        boodone = false;
                    }
                    break;
                case 0x125:

                    break;
            }
            onCreate(null);
        }
    };

}
