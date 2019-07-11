package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yyy.medicinekit.R;


//import android.support.v7.app.AppCompatActivity;

public class ManageChangeActivity extends Activity {
    Medicine medicine;
    TextView nameText,introText,dateText,priceText,periodText,pieceText,typeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_change);

        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra("medicine");

        nameText = (TextView)findViewById(R.id.nameText);
        introText = (TextView)findViewById(R.id.introText);
        dateText = (TextView)findViewById(R.id.dateText);
        priceText = (TextView)findViewById(R.id.priceText);
        periodText = (TextView)findViewById(R.id.periodText);
        pieceText = (TextView)findViewById(R.id.pieceText);
        typeText = (TextView)findViewById(R.id.typeText);

        nameText.setText(medicine.getName());
        introText.setText(medicine.getintro());
        dateText.setText(medicine.getdate()+"");
        priceText.setText(medicine.getprice()+"");
        periodText.setText(medicine.getperiod()+"");
        pieceText.setText(medicine.getpiece()+"");
        typeText.setText(medicine.gettype());



    }
}
