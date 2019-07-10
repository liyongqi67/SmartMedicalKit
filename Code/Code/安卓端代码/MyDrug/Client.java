package com.example.yyy.medicinekit.MyDrug;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//注册 Psdu;r;用户名-密码-设备号;
//登陆 Psdu;1;用户名-密码;
//查询药品信息 P用户名；flash;      返回字符串
//删除 P用户名;delete;第几个;
//更改 P用户名;m;要修改的英文名/eaten;值;number;
//添加 P用户名;n;code-生产日期-保质期-剩余量;

//详细信息
//扫码

//吃藥 P用戶名;done;number;    返回 0/1 吃了/沒吃

//小時，分鐘，持續時間，藥名，用藥量
public class Client extends AsyncTask<Void, Integer, String> {
    private Socket clientSocket = null;
    private OutputStream outStream = null;
    private Handler mHandler = null;
    //    private ReceiveThread mReceiveThread = null;
    private boolean stop = true;
    private String text = "";
    private String str;
    private Context context;
    char[] store;
    int[] bult;
    //private MYDB mydb;
    char[] get;
    int[] gn;
    private String style = " ";
    private myApplication mapp = new myApplication();

    boolean request_ok = false;

    public Client(Context context) {
        this.context = context;
    }


    public void setText(String t) {
        text = t;
    }


    public boolean Send(String style, String text) {
        connection();
//        mydb = new MYDB(context);

        // 连接按钮监听
        try {
            // 实例化对象并连接到服务器
            clientSocket = new Socket("192.168.1.157", 8889);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Client", "连接成功");

        this.text = null;
        this.text = text;
        this.style = null;
        this.style = style;
        this.execute();
        return request_ok;
    }

    public void SendMain(String text) {
        //发送数据按钮监听
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            Log.d("Client", "BufferedWriter出错");
            e.printStackTrace();
        }
        // 获得EditTex的内容
        //text = mEditText.getText().toString();
        Log.e("Client", "即将发送的信息为：" + text);
        try {
            // 发送数据
            bufferedWriter.write(text + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            Log.d("Client", "发送数据出错");
            e.printStackTrace();
        }
        Log.e("Client", "即将接收数据");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            Log.e("Client", "in已经构建");
//-----------------------------------------------第二次连接
            str = in.readLine();
            Log.e("Client", "读取完毕"+str);
            bufferedWriter.close();
            in.close();
            clientSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            Log.e("Client", "读取错误" + str);
        }
    }


    public static void connection() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork() // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

//    public String getSTR() {
//        return str;
//    }
//
//    public void setSTR(String str) {
//        this.str = str;
//    }


    @Override
    protected void onPreExecute() {
        //Toast.makeText(context,"开始执行",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Void... params) {
        //发送数据按钮监听
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "gbk"));
        } catch (IOException e) {
            Log.d("Client", "BufferedWriter出错");
            e.printStackTrace();
        }
        // 获得EditTex的内容
        //text = mEditText.getText().toString();
        Log.e("Client", "即将发送的信息为：" + text);
        try {
            // 发送数据
            bufferedWriter.write(text + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            Log.d("Client", "发送数据出错");
            e.printStackTrace();
        }
        Log.e("Client", "即将接收数据");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "gbk"));
            Log.e("Client", "in已经构建");
            String str = "";
            String temp;
            while ((temp=in.readLine())!=null){
                str += temp;
            }
            Log.e("结果", "读取完毕");
            bufferedWriter.close();
            in.close();
            clientSocket.close();
            Log.e("close", "close");
            return str;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
//        mReceiveThread = new ReceiveThread(clientSocket);
//        stop = false;
//        // 开启线程
//        mReceiveThread.start();
//        Log.d("Client","成功发送数据");
//        // 清空内容
//        text = "";
        return str;
    }

    @Override
    protected void onPostExecute(String str) {
//        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
//        setSTR(str);
        Log.e("onPostExecute", "sss");

        Log.e("结果",str);

        if ("signin".equals(style)) {
            Log.e("signin", "sss");
            Log.e("style",style);
            if ("s".equals(str)) {
                Log.e("sss", "sss");
                Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainActivity1.class);
                context.startActivity(i);
            } else Toast.makeText(context, "账号和密码不匹配", Toast.LENGTH_SHORT).show();

        } else if ("signup".equals(style)) {
            if ("s".equals(str)) {
                Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MainActivity1.class);
                context.startActivity(i);
            }
        } else if ("getmed".equals(style)) {
            mapp.setmessage(str);
            if (!(str == null || str.equals("none")))
                operate(str);
        } else if ("getart".equals(style)) {//signin
            mapp.setarticle(str);
            if (!(str.equals("none") || str.equals(""))) {
                store = str.toCharArray();
                operateArtical(str);
            }
        } else if ("scan".equals(style)) {

        } else if ("eaten".equals(style)) {

        } else if ("add".equals(style)) {

        }
    request_ok = true;
    }

    /**
     * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
    }


    public void operateArtical(String artical) {

        bult = new int[5];
        int a = 0;

        for (int i = 0; i < store.length; i++) {
            if (store[i] == '-' && a < 4) {
                bult[a++] = i;
            } else if (store[i] == ';') {
                bult[4] = i;
                operateArtical2(artical, bult);

                if (i + 1 < store.length) {
                    operateArtical(artical.substring(i + 1, store.length + 1));
                    bult = new int[5];
                }
            }
        }

    }

    public void operateArtical2(String cutArtical, int[] bult) {

        String articalName = cutArtical.substring(0, bult[0]);
        String intro = cutArtical.substring(bult[0] + 1, bult[1]);
        String contain = cutArtical.substring(bult[1] + 1, bult[2]);
        Log.e("contain", contain);
        //int pic = Integer.valueOf(cutArtical.substring(bult[2]+1,bult[3])).intValue();

        MYDB mydb = new MYDB(context);
        mydb.setArticle(articalName, intro, contain, 1);
        mydb.setArticle(articalName, intro, contain, 1);
        //mydb.closedatabase();
    }

    public void operate(String m) {
        get = null;
        if (!(m == null || m.equals("none"))) {
            get = m.toCharArray();
        }

        gn = null;
        gn = new int[15];
        int a = 0;

        for (int i = 0; i < get.length; i++) {
            if (get[i] == '-' && a < 14) {
                gn[a++] = i;
            } else if (get[i] == ';') {
                gn[14] = i;
                operate2(m, gn);
                if (i + 2 < get.length) {
                    gn = null;
                    gn = new int[15];
                    Log.e("last", m.substring(i + 1, get.length));
                    operate(m.substring(i + 1, get.length));
                    return;
                }
            }
        }

    }


    public void operate2(String m, int[] gn) {
        String name = m.substring(gn[0] + 1, gn[1]);
        Log.e("name", name);
        String type = m.substring(gn[1] + 1, gn[2]);
        Log.e("type", type);
        String intro = m.substring(gn[2] + 1, gn[3]);
        Log.e("intro", intro);
        String price = m.substring(gn[3] + 1, gn[4]);
        Log.e("price", price);
        String date = m.substring(gn[4] + 1, gn[5]);
        Log.e("date", date);
        String period = m.substring(gn[5] + 1, gn[6]);
        Log.e("period", period);
        String piece = m.substring(gn[6] + 1, gn[7]);
        Log.e("piece", piece);
        String form = m.substring(gn[7] + 1, gn[8]);
        Log.e("form", form);
        String function = m.substring(gn[8] + 1, gn[9]);
        Log.e("function", function);
        String usage = m.substring(gn[9] + 1, gn[10]);
        Log.e("usage", usage);
        String warning = m.substring(gn[10] + 1, gn[11]);
        Log.e("warning", warning);
        String size = m.substring(gn[11] + 1, gn[12]);
        Log.e("size", size);
        String number = m.substring(gn[12] + 1, gn[13]);
        Log.e("number", number);
        String eaten = m.substring(gn[13] + 1, gn[14]);
        Log.e("eaten", eaten);

//            Log.e("bbbbbbbbbbbbb",name+type+intro+price+date+period+piece);


        MYDB mydb = new MYDB(context);


        if (!mydb.checkExist(number)) {
            mydb.insertmedicine(name, type, intro, price,
                    date, period, piece, form, function, usage, warning, size, eaten, number, 0, 0, "--", "--", "--", "--","--", "--", "--", "--");
        }

        // mydb.closedatabase();
    }
}

