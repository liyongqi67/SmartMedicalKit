package com.example.yyy.medicinekit.MyDrug;

import android.provider.ContactsContract;

import java.io.Serializable;

/**
 * Created by YYY on 2016/8/27.
 */

public class ArticalClass implements Serializable {
    int ImageId;
    String name,intro,contain,suit;

    public ArticalClass(int a,String b,String c,String d){
        ImageId=a;
        name=b;
        intro=c;
        contain=d;

    }

//    public void setImageId(int a) {
//        this.ImageId=a;
//    }

//    public void setName(String a) {
//        this.name=a;
//    }

//    public void setintro(String a) {
//        this.intro=a;
//    }

    public int getImageId() {
        return ImageId;
    }

    public String getName() {
        return name;
    }

    public String getintro() {
        return intro;
    }

    public String getcontain() {return contain;}

    public String suit() {return suit;}


}
