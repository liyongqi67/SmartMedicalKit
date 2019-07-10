
package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yyy.medicinekit.R;


public class Account extends Activity {

    String username;
    TextView nametext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        nametext = (TextView)findViewById(R.id.nameText) ;
        Intent intent = getIntent();
        if(intent!=null) {
            username = intent.getStringExtra("name");
        }
        nametext.setText("用户名："+username);
    }
}
