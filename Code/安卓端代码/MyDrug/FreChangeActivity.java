package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yyy.medicinekit.R;


//import android.support.v7.app.AppCompatActivity;

public class FreChangeActivity extends Activity {

    private TextView nameText;
    private EditText freEdit,timEdit,pieceEdit;
    private Medicine medicine;

    //private Client client;
    private MYDB mydb;

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"MedicineStore.db",null,2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fre_change);

        nameText = (TextView)findViewById(R.id.nameText);
        freEdit = (EditText)findViewById(R.id.freEdit);
        timEdit = (EditText)findViewById(R.id.timEdit);
        pieceEdit = (EditText)findViewById(R.id.pieceEdit);
//        Intent intent = getIntent();
//        name = intent.getStringExtra("name");
//        int frequency = intent.getIntExtra("fre",-1);
//        int timepie = intent.getIntExtra("timepie",-1);

        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra("medicine");

        mydb = new MYDB(this);

        nameText.setText("药品名 ："+medicine.getName());
        freEdit.setText(""+medicine.getfrequency());
        timEdit.setText(""+medicine.gettimepiece());
        pieceEdit.setText(""+medicine.getprice());

        Button savebutton = (Button)findViewById(R.id.save);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();

                values.put("frequency", Integer.valueOf(freEdit.getText().toString()).intValue());
                db.update("MEDICINE",values,"number = ?",new String[] {medicine.getnumber()});
                values.clear();

                values.put("timepiece", Integer.valueOf(timEdit.getText().toString()).intValue());
                db.update("MEDICINE",values,"number = ?",new String[] {medicine.getnumber()});
                values.clear();

                values.put("piece", Integer.valueOf(pieceEdit.getText().toString()).intValue());
                db.update("MEDICINE",values,"number = ?",new String[] {medicine.getnumber()});
                values.clear();

                //P用户名;set;药品名-剩余量;
                //client.Send("P"+mydb.getAccountList().get(0)+";set;"+medicine.getName()+"-"+pieceEdit.getText().toString()+";");

                Intent intent = new Intent(FreChangeActivity.this,Daily.class);
                startActivity(intent);
                FreChangeActivity.this.finish();
            }
        });

    }





}
