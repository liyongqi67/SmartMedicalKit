package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yyy.medicinekit.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class SignUpActivity extends Activity {

    private MYDB mydb;
    EditText accounttext,keytext;
    Button signupbutton,signinbutton;

    myApplication mapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        mapp = new myApplication();
        accounttext=(EditText)findViewById(R.id.accounttext);
        keytext=(EditText)findViewById(R.id.key);
        signupbutton=(Button)findViewById(R.id.signupbutton);
        signinbutton=(Button)findViewById(R.id.signinbutton);
        mydb=new MYDB(this);

        signupbutton.setOnClickListener(new View.OnClickListener() {//注册
            @Override
            public void onClick(View view) {
                request("signup", "Psdu;r;" + accounttext.getText().toString() + "-" + Integer.valueOf(keytext.getText().toString()) + "-" + mydb.getAccountList().size() + 2 + ";");
                mapp.setid(accounttext.getText().toString());

                Intent i = new Intent(SignUpActivity.this, MainActivity1.class);

                startActivity(i);
                finish();
            }
        });

        signinbutton.setOnClickListener(new View.OnClickListener() {//注册
            @Override
            public void onClick(View view) {

            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);

            startActivity(i);
            finish();
            }
        });
    }



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
