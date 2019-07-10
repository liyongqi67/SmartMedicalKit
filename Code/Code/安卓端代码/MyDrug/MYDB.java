package com.example.yyy.medicinekit.MyDrug;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MYDB {

    private Context context;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private ContentValues values;
    private Cursor medicinecursor,Accountcursor;

    private List<Medicine> Dailylist = new ArrayList<Medicine>();
    private List<String> Managelist = new ArrayList<String>();
    private List<String> Accountlist = new ArrayList<String>();
    private List<Integer> KeyList = new ArrayList<Integer>();

    private ArrayList<ArticalClass> articlelist = new ArrayList<>();

    public MYDB(Context context) {
        this.context = context;
        initDB();
    }

    public void initDB() {
        dbHelper = new MyDatabaseHelper(context,"MedicineStore.db",null,2);//构造函数，中间为数据库名字，版本号指定为1

        db = dbHelper.getWritableDatabase();//创建或打开数据库
        values=new ContentValues();
    }

    public void insertmedicine(String name, String type, String intro, String price, String date, String period, String  piece,String form,String function,String usage,String warning,String size,String number,String eaten,int frequency,int timepiece,String time1a,String time1b,String time2a,String time2b,String time3a,String time3b,String time4a,String time4b) {

//        开始组装第一条数据
        values.put("name",name);
        values.put("intro",intro);
        values.put("date",date);
        values.put("price",price);
        values.put("period",period);
        values.put("piece",piece);
        values.put("type",type);
        values.put("form",form);
        values.put("function",function);
        values.put("usage",usage);
        values.put("warning",warning);
        values.put("size",size);
        values.put("eaten",eaten);
        values.put("number",number);
        values.put("frequency",frequency);
        values.put("timepiece",timepiece);
        values.put("name",name);
        values.put("time1a",time1a);
        values.put("time1b",time1b);
        values.put("time2a",time2a);
        values.put("time2b",time2b);
        values.put("time3a",time3a);
        values.put("time3b",time3b);
        values.put("time4a",time4a);
        values.put("time4b",time4b);

        db.insert("MEDICINE", null, values);//表名
        values.clear();
    }

    public List<Medicine> getDailylist(){

        medicinecursor = db.query("MEDICINE",null, "eaten = ?", new String[]{"yes"}, null, null, null);

        if (medicinecursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据
                String name = medicinecursor.getString(medicinecursor.getColumnIndex("name"));
                String intro = medicinecursor.getString(medicinecursor.getColumnIndex("intro"));
                String date= medicinecursor.getString(medicinecursor.getColumnIndex("date"));
                String price = medicinecursor.getString(medicinecursor.getColumnIndex("price"));
                String period=medicinecursor.getString(medicinecursor.getColumnIndex("period"));
                String piece=medicinecursor.getString(medicinecursor.getColumnIndex("piece"));
                String type =  medicinecursor.getString(medicinecursor.getColumnIndex("type"));
                String form = medicinecursor.getString(medicinecursor.getColumnIndex("form"));
                String function = medicinecursor.getString(medicinecursor.getColumnIndex("function"));
                String usage = medicinecursor.getString(medicinecursor.getColumnIndex("usage"));
                String warning = medicinecursor.getString(medicinecursor.getColumnIndex("warning"));
                String size = medicinecursor.getString(medicinecursor.getColumnIndex("size"));
                String eaten = medicinecursor.getString(medicinecursor.getColumnIndex("eaten"));
                String number = medicinecursor.getString(medicinecursor.getColumnIndex("number"));
                int frequency = medicinecursor.getInt(medicinecursor.getColumnIndex("frequency"));
                int timepiece = medicinecursor.getInt(medicinecursor.getColumnIndex("timepiece"));
                String time1a = medicinecursor.getString(medicinecursor.getColumnIndex("time1a"));
                String time2a = medicinecursor.getString(medicinecursor.getColumnIndex("time2a"));
                String time3a = medicinecursor.getString(medicinecursor.getColumnIndex("time3a"));
                String time4a = medicinecursor.getString(medicinecursor.getColumnIndex("time4a"));
                String time1b = medicinecursor.getString(medicinecursor.getColumnIndex("time1b"));
                String time2b = medicinecursor.getString(medicinecursor.getColumnIndex("time2b"));
                String time3b = medicinecursor.getString(medicinecursor.getColumnIndex("time3b"));
                String time4b = medicinecursor.getString(medicinecursor.getColumnIndex("time4b"));

                Medicine med = new Medicine(name, price,date,period,intro,type,piece,frequency,timepiece,form,size,function,usage,warning,eaten,number,time1a,time1b,time2a,time2b,time3a,time3b,time4a,time4b);
                //遍历Cursor对象，取出数据
                Dailylist.add(med);
//                Log.e("time1", time1 + "");
//                Log.e("time1",time2+"");
//                Log.e("time1",time3+"");
//                Log.e("time1",time4+"");
            } while (medicinecursor.moveToNext());
        }
        medicinecursor.close();

        return Dailylist;
    }


    public List<String> getManagelist(){
        return Managelist;
    }

    public void setManagelist(int i,String str) {//更改
        Managelist.set(i,str);
        values.put("piece",str);
        db.update("MEDICINE",values,"name=?", new String[]{"thename"});
        values.clear();
        Toast.makeText(context,"修改成功", Toast.LENGTH_SHORT).show();
    }

    public void setAccountlist(String account, Integer key) {
        Accountlist.add(account);
        KeyList.add(key);
        values.put("account",account);
        values.put("key",key);
        values.put("Signin",1);
        db.insert("ACCOUNT", null, values);//表名
        values.clear();
    }


    public List<String> getAccountList() {
        Accountlist.clear();
        Accountcursor = dbHelper.getWritableDatabase().query("ACCOUNT", null, null, null, null, null,null);
        if (Accountcursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据
                String account = Accountcursor.getString(Accountcursor.getColumnIndex("account"));
                Accountlist.add(account);

            } while (Accountcursor.moveToNext());
        }
        Accountcursor.close();


        return Accountlist;
    }

    public List<Integer> getKeyList() {
        KeyList.clear();
        Accountcursor = db.query("ACCOUNT", null, null, null, null, null, null);
        if (Accountcursor.moveToFirst()) {
            do {
                //遍历Cursor对象，取出数据
                int key = Accountcursor.getInt(Accountcursor.getColumnIndex("key"));
                KeyList.add(key);

            } while (Accountcursor.moveToNext());
        }
        Accountcursor.close();

        return KeyList;
    }

//    public void setSignin(int b) {
//        values.put("Signin",b);
//        db.update("ACCOUNT",values,null,null);
//        values.clear();
//    }

    public int getSignin() {

        int s=2;
        Accountcursor = db.query("ACCOUNT", null, null, null, null, null, null);
        if (Accountcursor.moveToFirst()) {
            do {
//               遍历Cursor对象，取出数据
                s = Accountcursor.getInt(Accountcursor.getColumnIndex("Signin"));
            } while (Accountcursor.moveToNext());
        }
        Accountcursor.close();
        return  s;
    }


    public void deleteMedicine(String tablename,String number) {
        db.delete(tablename,"number = ?",new String[]{number});
//        String sql = "select count(*) from MEDICINE";
//        Cursor cursor = db.rawQuery(sql, null);
//        cursor.moveToFirst();
//        Log.e("long",cursor.getLong(0)+"");
//        cursor.close();
    }

    public void setArticle(String name,String intro,String contain,int ImageId) {
        values.put("name", name);
        values.put("intro",intro);
        values.put("contain",contain);
        values.put("ImageId", ImageId);
        db.insert("ARTICLE", null, values);//表名
        values.clear();
    }



    public ArrayList<ArticalClass> getArticlelist(){
        articlelist.clear();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("ARTICLE",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String intro = cursor.getString(cursor.getColumnIndex("intro"));
                    int ImageId= cursor.getInt(cursor.getColumnIndex("ImageId"));
                    String contain = cursor.getString(cursor.getColumnIndex("contain"));


                    ArticalClass med = new ArticalClass(ImageId,name,intro,contain);
                    //遍历Cursor对象，取出数据
                    articlelist.add(med);
                    Log.e("articlength",articlelist.size()+"");
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return articlelist;
    }

    public void clearTable(String tablename){
        db.delete(tablename, null, null);
    }


    public void changeEaten(String eaten,String number) {
        values.put("eaten",eaten);

        db.update("MEDICINE",values,"number=?", new String[]{number});
        values.clear();
    }


    public void changetime(String number,String frame,String time) {
        values.put(frame,time);

        db.update("MEDICINE", values, "number=?", new String[]{number});
        values.clear();

        Log.e("changetime", number);
    }


    public ArrayList<Medicine> getTimeTable() {

        ArrayList<Medicine> medicineList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("MEDICINE",null,"time1 <> ? OR time2 <> ? OR time3 <> ? OR time4 <> ?",new String[]{"--","--","--","--"},null,null,null);
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

                    Medicine med = new Medicine(name, price,date,period,intro,type,piece,frequency,timepiece,form,size,function,usage,warning,eaten,number,time1a,time1b,time2a,time2b,time3a,time3b,time4a,time4b);
                    //遍历Cursor对象，取出数据
                    medicineList.add(med);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return medicineList;
    }

    public boolean checkExist(String number) {
        boolean exist = false;
        medicinecursor = db.query("MEDICINE",null, "number = ?", new String[]{number}, null, null, null);
        if (medicinecursor.moveToFirst()) {
            do {
                exist = true;
            } while (medicinecursor.moveToNext());
        }
        medicinecursor.close();

        return exist;
    }


    public void closedatabase() {
        db.close();
    }

    public void changeTimepiece(String number,String timepiece) {
        values.put(number,timepiece);

        db.update("MEDICINE", values, "number=?", new String[]{number});
        values.clear();
    }

    public void setTimeTable(String number){//初始化默認全為no
        values.put("number", number);
        values.put("time1","no");
        values.put("time2","no");
        values.put("time3","no");
        values.put("time4","no");
        db.insert("TIMETABLE", null, values);//表名
        values.clear();
    }

    public void updateTimeTable(String number,String period,String yesorno) {//吃了設置yes，沒吃設置no
        values.put(period, yesorno);

        db.update("TIMETABLE", values, "number=?", new String[]{number});
        values.clear();
    }

    public String getTimeTable(String number,String period) {//返回number药某period的yesorno
        String yesorno = "未读取";
        Cursor cursor = db.query("MEDICINE",null,"number = ?",new String[]{number},null,null,null);
        if(cursor.moveToFirst()) {
            if (cursor.moveToFirst()) {
                do {
                    yesorno = cursor.getString(cursor.getColumnIndex(period));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return yesorno;
    }

}
