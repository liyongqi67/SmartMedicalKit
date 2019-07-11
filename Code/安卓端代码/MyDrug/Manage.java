package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Manage extends Activity implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener, View.OnClickListener, SlideView.OnSlideListener {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "MedicineStore.db", null, 2);

    private ArrayList<Medicine> medicineList;

    private ListViewCompat mListView;

    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();

    private SlideView mLastSlideViewWithStatusOn;

    private int position;

    private Button goback, scanaddbutton;
    private MYDB mydb;

    private SwipeRefreshLayout mSwipeLayout;


    private String username;

    private myApplication mapp;

    private int[] color1 = new int[]{R.color.color2, R.color.color3, R.color.color4, R.color.color5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage);
        medicineList = new ArrayList<>();
        goback = (Button) findViewById(R.id.button_backward);
        scanaddbutton = (Button) findViewById(R.id.button_Scanadd);

        mapp = new myApplication();
        username = mapp.getid();


        mydb = new MYDB(this);

//        mydb.insertmedicine("name","price","date","price","3","3","30","1","1","1","1","1","d","yes",2,3,"--","--","--","--");
        //mydb.clearTable("MEDICINE");

        mListView = (ListViewCompat) findViewById(R.id.list);

        request("getmed", "P" + "li" + ";flash;");
        //operatemedicine,吧药存进数据库，加进medicineList里面

//        initMdeicine();
//        initView();


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manage.this, MainActivity1.class);
                startActivity(intent);
                finish();
            }
        });


        scanaddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Manage.this, QRScanActivity.class);
                intent.putExtra("name", username);
                startActivityForResult(intent,0x111);
            }
        });


        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    public void initMdeicine() {

        medicineList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(true, "MEDICINE", null, null, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String intro = cursor.getString(cursor.getColumnIndex("intro"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String period = cursor.getString(cursor.getColumnIndex("period"));
                    String piece = cursor.getString(cursor.getColumnIndex("piece"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
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

                    Medicine med = new Medicine(name, price, date, period, intro,
                            type, piece, frequency, timepiece, form, size, function, usage,
                            warning, eaten, number, time1a,time1b,time2a,time2b,time3a,time3b,time4a,time4b);

                    medicineList.add(med);
                } while (cursor.moveToNext());
            }
        }


        Log.e("initmedicinemedListsize", medicineList.size() + "");
        cursor.close();
    }

    private void initView() {
        mListView = (ListViewCompat) findViewById(R.id.list);
        mMessageItems.clear();
        for (int i = 0; i < medicineList.size(); i++) {
            MessageItem item = new MessageItem();
            item.itemimage = 1;
            item.itemname = medicineList.get(i).getName();
            item.itemtype = medicineList.get(i).gettype();
            item.itemintro = medicineList.get(i).getintro();
            item.itemprice = medicineList.get(i).getprice();

            mMessageItems.add(item);
        }
        mListView.setAdapter(new SlideAdapter());
        mListView.setOnItemClickListener(this);
        Log.e("initView","initView");
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {  //列表单击
        this.position = position;
        Intent intent = new Intent(Manage.this, Detail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("medicine", medicineList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
        Log.e("onItemClick", position + "");
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) { //被拉开
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    @Override
    public void onClick(View v) { //按钮单击
        if (v.getId() == R.id.deleteholder) {
//            Log.e("position",position+"");
//            Log.e("number",medicineList.get(position).getnumber()+"");


            Log.e("medicinlist", medicineList.size() + "");
            request("delete", "P" + "li" + ";delete;" + medicineList.get(position).getnumber() + ";");

            mydb.deleteMedicine("MEDICINE", medicineList.get(position).getnumber());

            //medicineList.remove(position);
            Log.e("position", position + "");
//            initMdeicine();
//            initView();//operate里面有

            //Log.e("size222",medicineList.size()+"");
            Toast.makeText(Manage.this, "删除", Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.editholder) {
            //Log.e("size",medicineList.size()+"");
            Medicine medicine = medicineList.get(position);

            Intent intent = new Intent(Manage.this, FreChangeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("medicine", medicine);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        onCreate(null);
//    }

    String outcome = "";

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

                        if ("getmed".equals(tag))
                            handler.sendEmptyMessage(0x121);
                        else if ("delete".equals(tag)) {

                            handler.sendEmptyMessage(0x122);
                        }else if("detail".equals(tag))
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x121:
                    if (mListView != null && !outcome.equals(""))
                        operateMedicine(outcome);
                        mListView.invalidate();
                    Log.e("Managechange", "change");
                    break;
                case 0x122:
                    onCreate(null);
                    break;
                case 0x125:
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



    public void operateMedicine(String medicine) {//request("getmed")
        mydb.clearTable("MEDICINE");
        //medicineList.clear();

        if(!medicine.equals("none")) {
            String[] medicinelist = medicine.split(";");
            String[] infor;
            for (int i = 0; i < medicinelist.length; i++) {
                infor = medicinelist[i].split("-");
                if (infor.length > 14 && !mydb.checkExist(infor[14])) {
                    mydb.insertmedicine(infor[1], infor[2], infor[3], infor[4],
                            infor[5], infor[6], infor[7], infor[8], infor[9], infor[10], infor[11], infor[12], infor[13], infor[14],
                            0, 0, "--", "--", "--", "--", "--", "--", "--", "--");

                    Medicine med = new Medicine(infor[1], infor[4], infor[5], infor[6], infor[3],
                            infor[2], infor[7], 0, 0, infor[8], infor[12], infor[9], infor[10],
                            infor[11], infor[14], infor[13], "--", "--", "--", "--", "--", "--", "--", "--");

                    medicineList.add(med);
                    mydb.setTimeTable(infor[13]);
                }
            }

            //添加藥品后需要
            List<Medicine> list = mydb.getDailylist();//eaten=yes;

            for(int i=0;i<list.size();i++) {
                request("detail", "P" + mapp.getid() + ";drugEate;" + list.get(i).getnumber() + ";");
            }
        }
//        Log.e("operate+medlength", medicinelist.length + "");
        initMdeicine();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0x111&&resultCode==0x112) {
            onCreate(null);
            //request("getmed", "P" + "li" + ";" + "flash;");

//            initMdeicine();
//            initView();
        }
    }



    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
//                Toast.makeText(Manage.this, "fadlfjal;kdfja", Toast.LENGTH_SHORT).show();                mSwipeLayout.setRefreshing(false);

                request("getmed", "P" + "li" + ";flash;");//operatemedicine


                //mListView.setAdapter(new SlideAdapter());
                Log.e("refresh", "true");
            }
        }, 3000);
    }

    private class SlideAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.manageitem, null);

                slideView = new SlideView(Manage.this);
                slideView.setContentView(itemView);

                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(Manage.this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }
            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            holder.medImage.setImageResource(item.itemimage);
            holder.medName.setText("药品名称 ：" + item.itemname);
            holder.typeText.setText("药品类型 ：" + item.itemtype);
            holder.introText.setText("简介 ：" + item.itemintro);
            holder.priceText.setText("价格 ：" + item.itemprice);
            holder.deleteHolder.setOnClickListener(Manage.this);
            holder.editholder.setOnClickListener(Manage.this);

            holder.view1.setBackgroundResource(color1[position % 4]);
            return slideView;
        }

    }

    public class MessageItem {
        public int itemimage;
        public String itemname;
        public String itemtype;
        public String itemintro;
        public String itemprice;
        public SlideView slideView;
    }

    private static class ViewHolder {
        public ImageView medImage;
        public TextView medName, typeText, introText, priceText;
        public ViewGroup deleteHolder, editholder;
        public View view1;

        ViewHolder(View view) {
            medImage = (ImageView) view.findViewById(R.id.medImage);
            medName = (TextView) view.findViewById(R.id.medName);
            typeText = (TextView) view.findViewById(R.id.typeText);
            introText = (TextView) view.findViewById(R.id.introText);
            priceText = (TextView) view.findViewById(R.id.priceText);
            editholder = (ViewGroup) view.findViewById(R.id.editholder);
            deleteHolder = (ViewGroup) view.findViewById(R.id.deleteholder);
            view1 = (View) view.findViewById(R.id.view);
        }
    }
}
