package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class Addpage extends Activity {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"MedicineStore.db",null,2);
    private ArrayList<Medicine> medicineList;

    private ArrayList<Medicine> selectList;

    private Button saveButton,goback;
    private ListView listView;
    private MedicineAdapter adapter;

    private String username;
    MYDB mydb;
    String outcome = "";

    private myApplication mapp;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x121:
                    if(listView!=null&&!outcome.equals(""))
                        listView.invalidate();
                    break;
            }
        }
    };

    public void request(final String tag, final String str) {
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
                        String temp;
                        outcome = "";
                        while ((temp = in.readLine()) != null) {

                        }
//                        Log.e("结果", outcome);
                        bufferedWriter.close();
                        in.close();
                        clientSocket.close();

                        if ("添加".equals(tag))
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpage);
        saveButton= (Button)findViewById(R.id.save);
        goback = (Button)findViewById(R.id.button_backward);

        medicineList=new ArrayList<>();
        selectList=new ArrayList<Medicine>();

        initMdeicine();//從數據庫讀取

        adapter = new MedicineAdapter(Addpage.this, R.layout.addpageitem,medicineList);
        mydb = new MYDB(this);
        //mydb.insertmedicine("name","price","date","price","3","3","30","1","1","1","1","1","d","yes",2,3,"--","--","--","--");

        mapp = new myApplication();

        username = mapp.getid();

        listView= (ListView)findViewById(R.id.addlistview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Medicine medicine = medicineList.get(i);
//                Log.e("medicineNumber",medicine.getnumber());
                selectList.add(medicine);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Addpage.this,Daily.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("selectList", selectList);
//                intent.putExtras(bundle);


                if(selectList != null)
                    for (int i = 0; i < selectList.size(); i++) {

                        //开始组装第一条数据
                        mydb.changeEaten("yes",selectList.get(i).getnumber());


                        request("添加", "P" + username + ";m;eaten;" + "yes;" + selectList.get(i).getnumber() + ";");

                    }
                selectList.clear();

                startActivity(intent);
                finish();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Addpage.this,Daily.class);
                Intent intent = new Intent(Addpage.this,Clock.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initMdeicine();
        listView.setAdapter(adapter);
    }


    public void initMdeicine(){
        medicineList.clear();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("MEDICINE",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String intro = cursor.getString(cursor.getColumnIndex("intro"));
                    String date= cursor.getString(cursor.getColumnIndex("date"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String period=cursor.getString(cursor.getColumnIndex("period"));
                    String piece=cursor.getString(cursor.getColumnIndex("piece"));
                    String type =  cursor.getString(cursor.getColumnIndex("type"));
                    String form = cursor.getString(cursor.getColumnIndex("form"));
                    String function = cursor.getString(cursor.getColumnIndex("function"));
                    String usage = cursor.getString(cursor.getColumnIndex("usage"));
                    String warning = cursor.getString(cursor.getColumnIndex("warning"));
                    String size = cursor.getString(cursor.getColumnIndex("size"));
                    String eaten = cursor.getString(cursor.getColumnIndex("eaten"));
                    String number = cursor.getString(cursor.getColumnIndex("number"));
                    int frequency = cursor.getInt(cursor.getColumnIndex("frequency"));
                    int timepiece = cursor.getInt(cursor.getColumnIndex("timepiece"));
                    String time1a = cursor.getString(cursor.getColumnIndex("time1a"));
                    String time2a = cursor.getString(cursor.getColumnIndex("time2a"));
                    String time3a = cursor.getString(cursor.getColumnIndex("time3a"));
                    String time4a = cursor.getString(cursor.getColumnIndex("time4a"));
                    String time1b = cursor.getString(cursor.getColumnIndex("time1b"));
                    String time2b = cursor.getString(cursor.getColumnIndex("time2b"));
                    String time3b = cursor.getString(cursor.getColumnIndex("time3b"));
                    String time4b = cursor.getString(cursor.getColumnIndex("time4b"));
                    Medicine med = new Medicine(name, price,date,period,intro,type,piece,frequency,timepiece,form,size,function,usage,warning,eaten,number, time1a,time1b,time2a,time2b,time3a,time3b,time4a,time4b);
                    //遍历Cursor对象，取出数据
                    medicineList.add(med);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

    }

}
