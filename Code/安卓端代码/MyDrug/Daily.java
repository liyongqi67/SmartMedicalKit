package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class Daily extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener,SlideView.OnSlideListener{

    private DailyListViewCompat mListView;
    private SlideView mLastSlideViewWithStatusOn;
    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"MedicineStore.db",null,2);
    private ArrayList<Medicine> medicineList = new ArrayList<>();
    private Button goback,addbutton;
    private ArrayList<Medicine> List;
    private MYDB mydb;
    private int position;

    private String username;
    private int[] color1 = new int[]{ R.color.color2,R.color.color3,R.color.color4,R.color.color5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daily);

        addbutton = (Button)findViewById(R.id.add);
        goback = (Button)findViewById(R.id.button_backward);
        mydb = new MYDB(this);


        Intent intent = getIntent();
        if(intent!=null) {
            username = intent.getStringExtra("name");
        }
//        Intent intent = getIntent();
//
//        if(intent!=null) {
//           List = (ArrayList<Medicine>)intent.getSerializableExtra("selectList");
//            if(List != null)
//                for (int i = 0; i < List.size(); i++) {
//
//                    //开始组装第一条数据
//                    mydb.changeEaten("yes",List.get(i).getnumber());
//                    List.clear();
//                }
//        }

        readall();

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Daily.this,Addpage.class);
                i.putExtra("name",username);
                startActivity(i);
                finish();
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Daily.this,MainActivity1.class);
                startActivity(intent);
                finish();
            }
        });

        initView();
    }


    public void readall(){//读yes
        medicineList.clear();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("MEDICINE",null,"eaten=?",new String[]{"yes"},null,null,null);
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


                    Medicine med = new Medicine(name, price,date,period,intro,type,piece,frequency,timepiece,form,size,function,
                            usage,warning,eaten,number,time1a,time1b,time2a,time2b,time3a,time3b,time4a,time4b);
                    //遍历Cursor对象，取出数据
                    medicineList.add(med);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }


    private void initView() {
        mListView = (DailyListViewCompat) findViewById(R.id.list);
        mMessageItems.clear();
        for (int i = 0; i < medicineList.size(); i++) {
            MessageItem item = new MessageItem();
            item.itemimage = 1;
            item.itemname = medicineList.get(i).getName();
            item.itemtype =  medicineList.get(i).gettype();
            item.itemintro = medicineList.get(i).getintro();
            item.itempiece = medicineList.get(i).getpiece()+"";

            mMessageItems.add(item);
        }
        mListView.setAdapter(new SlideAdapter());
        mListView.setOnItemClickListener(this);
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
                View itemView = mInflater.inflate(R.layout.daily_item, null);

                slideView = new SlideView(Daily.this);
                slideView.setContentView(itemView);

                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(Daily.this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }

            MessageItem item = mMessageItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            holder.medImage.setImageResource(item.itemimage);
            holder.medName.setText("药品名称 ："+item.itemname);
            holder.timepieceText.setText("服用次数 ："+item.itemtype);
            holder.frequencyText.setText("频率 ："+item.itemintro);
            holder.pieceText.setText("剩余量 ："+item.itempiece);
            holder.deleteHolder.setOnClickListener(Daily.this);
            holder.editholder.setOnClickListener(Daily.this);

            holder.view1.setBackgroundResource(color1[position%5]);
            return slideView;
        }

    }

    public class MessageItem {
        public int itemimage;
        public String itemname;
        public String itemtype;
        public String itemintro;
        public String itempiece;
        public SlideView slideView;
    }

    private static class ViewHolder {
        public ImageView medImage;
        public TextView medName,frequencyText,timepieceText,pieceText;
        public ViewGroup deleteHolder,editholder;
        public View view1;

        ViewHolder(View view) {
            medImage = (ImageView) view.findViewById(R.id.medImage);
            medName = (TextView) view.findViewById(R.id.medName);
            frequencyText = (TextView) view.findViewById(R.id.frequencyText);
            timepieceText = (TextView) view.findViewById(R.id.timepieceText);
            pieceText = (TextView) view.findViewById(R.id.pieceText);
            editholder = (ViewGroup)view.findViewById(R.id.editholder);
            deleteHolder = (ViewGroup)view.findViewById(R.id.deleteholder);
            view1 = (View)view.findViewById(R.id.view);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {  //列表单击
        this.position=position;
        Intent intent = new Intent(Daily.this, Detail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("medicine", medicineList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);

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
            if(medicineList.size()!=0) {

                request("eaten","P"+username+";m;eaten;"+"no;"+medicineList.get(position).getnumber()+";");

                mydb.changeEaten("no", medicineList.get(position).getnumber());
                medicineList.remove(position);

                onCreate(null);
                mListView.setAdapter(new SlideAdapter());

                Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.editholder) {

            Medicine medicine = medicineList.get(position);

            Intent intent = new Intent(Daily.this, FreChangeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("medicine", medicine);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

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
                        while ((temp = in.readLine()) != null) {

                        }
//                        Log.e("结果", outcome);
                        bufferedWriter.close();
                        in.close();
                        clientSocket.close();

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
}
