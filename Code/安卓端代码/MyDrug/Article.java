package com.example.yyy.medicinekit.MyDrug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yyy.medicinekit.R;


/**
 * Created by YYY on 2016/8/28.
 */

public class Article extends Activity {
    ArticalClass articalClass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artical);

        Intent intent = getIntent();
        articalClass = (ArticalClass) intent.getSerializableExtra("artical");

        TextView nameText = (TextView)findViewById(R.id.nametext);
        TextView introText = (TextView)findViewById(R.id.introText);
        TextView contain = (TextView)findViewById(R.id.contain);
        TextView suit = (TextView)findViewById(R.id.suit);
        ImageView image = (ImageView)findViewById(R.id.image);

        nameText.setText(articalClass.getName());
        introText.setText(articalClass.getintro());
        contain.setText(articalClass.getcontain());
        suit.setText(articalClass.getName());
        image.setImageResource(articalClass.getImageId());


    }

}
