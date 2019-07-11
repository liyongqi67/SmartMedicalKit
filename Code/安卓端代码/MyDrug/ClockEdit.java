package com.example.yyy.medicinekit.MyDrug;

import android.animation.TimeAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.LauncherActivity;
import android.app.TimePickerDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;


import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;

public class ClockEdit extends Activity {

    MedicineAdapter mda ;
    private MYDB mydb;
    ListView listview;
    Calendar c;
    Button savebutton,gobackbutton;
    private TimePicker timePick1;
    List<Medicine> getDailylist;
    LauncherActivity.ListItem ll;
    myApplication mapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_edit);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        mydb = new MYDB (this);

        mapp = new myApplication();

        getDailylist = mydb.getDailylist();

        listview = (ListView)findViewById(R.id.clockeditListview);
        mda = new MedicineAdapter(this,R.layout.clockedit_item,getDailylist);
//        listview.setAdapter(mda);

        listview.setAdapter(mda);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                final Medicine medicine = getDailylist.get(i);
//                Calendar currentTime = Calendar.getInstance();
                c = Calendar.getInstance();
                new TimePickerDialog(ClockEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        //设置时间

                        c.setTimeInMillis(System.currentTimeMillis());
                        // 根据用户选择的时间来设置Calendar对象
                        c.set(Calendar.HOUR, hourOfDay);
                        Log.e("hourofDay", hourOfDay + "");

                        c.set(Calendar.MINUTE, minute);
                        Log.e("minute",minute+"");
                        // ②设置AlarmManager在Calendar对应的时间启动Activity


                        switch (hourOfDay) {
                            case 3:case 4:case 5:case 6:case 7:case 8:
                                mydb.changetime(medicine.getnumber(), "time1", hourOfDay + ":" + minute);
                                return;
                            case 9:case 10:case 11:case 12:case 13:case 14:
                                mydb.changetime(medicine.getnumber(),"time2",hourOfDay+":"+minute);
                                return;
                            case 15:case 16:case 17:case 18:case 19:case 20:
                                mydb.changetime(medicine.getnumber(),"time3",hourOfDay+":"+minute);
                                return;
                            case 21:case 22:case 23:case 0:case 1:case 2:
                                mydb.changetime(medicine.getnumber(),"time4",hourOfDay+":"+minute);
                                return;
                        }

                        request("changHour","P"+mapp.getid()+";m;drugHour;"+hourOfDay+";"+medicine.getnumber()+";");
                        request("changMinute","P"+mapp.getid()+";m;drugMinute;"+minute+";"+medicine.getnumber()+";");

                        mda = new MedicineAdapter(ClockEdit.this,R.layout.clockedit_item,getDailylist);
                        listview.setAdapter(mda);
                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show();
//                onCreate(null);
            }
        });


//        timePick1=(TimePicker)findViewById(R.id.timePic1);
//        //是否使用24小时制
//        timePick1.setIs24HourView(true);
//        TimeAnimator.TimeListener times=new TimeAnimator.TimeListener();
//        timePick1.setOnTimeChangedListener(times);

        gobackbutton = (Button)findViewById(R.id.button_backward);
        gobackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClockEdit.this,Clock.class);
                startActivity(intent);
                finish();
            }
        });

        savebutton = (Button)findViewById(R.id.save);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClockEdit.this,Clock.class);
                intent.putExtra("change","true");
                Bundle bundle = new Bundle();
                bundle.putSerializable("Calendar", c);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    String outcome = "";
    public void request(final String tag, final String str) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Socket clientSocket = new Socket("192.168.1.157", 8889);
                    BufferedWriter bufferedWriter = null;
                    try {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "gbk"));
                        bufferedWriter.write(str + "\r\n");
                        bufferedWriter.flush();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "gbk"));
                        Log.e("ClockEdit", "开始读取");
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


                        handler.sendEmptyMessage(0x121);

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

                    break;
            }
        }
    };


}
