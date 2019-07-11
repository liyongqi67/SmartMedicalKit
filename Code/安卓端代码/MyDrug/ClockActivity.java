package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yyy.medicinekit.R;


public class ClockActivity extends Activity {

    private AlarmManager alarmManager;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        Intent intent = getIntent();
        if(intent!=null)
//            alarmManager = intent.get

        new AlertDialog.Builder(ClockActivity.this).setTitle("用药提醒").setMessage("该吃药了~")
                .setPositiveButton("关闭提醒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //alarmManager.cancel(pi);
                        Toast.makeText(ClockActivity.this, "闹钟已取消", Toast.LENGTH_SHORT)
                                .show();
                        ClockActivity.this.finish();
                    }
                }).show();
    }
}
