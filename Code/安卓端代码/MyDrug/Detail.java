package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yyy.medicinekit.R;


public class Detail extends Activity {

    Medicine medicine;

    TextView name,type,intro,price,date,period,piece,frequency,timepiece,form,size,function,usage,warning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra("medicine");

        name = (TextView)findViewById(R.id.name);
        type = (TextView)findViewById(R.id.type);
        intro = (TextView)findViewById(R.id.intro);
        price = (TextView)findViewById(R.id.price);
        date = (TextView)findViewById(R.id.date);
        period = (TextView)findViewById(R.id.period);
        piece = (TextView)findViewById(R.id.piece);
        frequency = (TextView)findViewById(R.id.frequency);
        timepiece = (TextView)findViewById(R.id.timepiece);
        form = (TextView)findViewById(R.id.form);
        size = (TextView)findViewById(R.id.size);
        function = (TextView)findViewById(R.id.function);
        usage = (TextView)findViewById(R.id.usage);
        warning = (TextView)findViewById(R.id.warning);

        name.setText(medicine.getName());
        type.setText("种类："+medicine.gettype());
        intro.setText("简介："+medicine.getintro());
        price.setText("价格："+medicine.getprice());
        date.setText("生产日期："+medicine.getdate()+"");
        period.setText("保质期："+medicine.getperiod()+"");
        piece.setText("剩余量："+medicine.getpiece()+"");
        frequency.setText("服用次数/日："+medicine.getfrequency()+"");
        timepiece.setText("每次服用/片："+medicine.gettimepiece()+"");
        form.setText("状态："+medicine.getform());
        size.setText("规格："+medicine.getsize());
        function.setText("功能："+medicine.getfunction());
        usage.setText("用法："+medicine.getusage());
        warning.setText("注意事项："+medicine.getWarning());

    }
}
