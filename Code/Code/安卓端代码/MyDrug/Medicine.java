package com.example.yyy.medicinekit.MyDrug;

import java.io.Serializable;

/**
 * Created by YYY on 2016/8/9.
 */

public class Medicine implements Serializable {
    String name,intro,type,price,form,size,function,usage,warning,eaten,number;
    String date,period,piece;
    int imageId,frequency,timepiece;
    String time1a ,time1b ,time2a ,time2b,time3a,time3b,time4a,time4b;
//    public enum Type {
//        RED, GREEN, BLANK, YELLOW
//    }

    public Medicine(String name, String price, String date, String period,
                    String intro, String type, String piece, int frequency, int timepiece,
                    String form,String size,String function,String usage,String warning,
                    String eaten,String number,String time1a,String time1b,String time2a,String time2b,String time3a,String time3b,String time4a,String time4b){
        this.name=name;
        this.type=type;
        this.intro=intro;
        this.price=price;
        this.date=date;
        this.period=period;
        this.piece=piece;
        this.form=form;
        this.function=function;
        this.usage=usage;
        this.warning=warning;
        this.size=size;
        this.eaten=eaten;
        this.number=number;
        this.frequency=frequency;
        this.timepiece=timepiece;
        this.time1a = time1a;
        this.time1b = time1b;
        this.time2a = time2a;
        this.time2b = time2b;
        this.time3a = time3a;
        this.time3b = time3b;
        this.time4a = time4a;
        this.time4b = time4b;
    }


    public String getName() {
        return this.name;
    }
    public String getintro() {
        return this.intro;
    }
    public String getdate() {
        return this.date;
    }
    public String getprice() {
        return this.price;
    }
    public String getperiod() {
        return this.period;
    }
    public String getpiece() {
        return this.piece;
    }
    public String gettype() {
        return this.type;
    }

    public int getImageId() {
        return imageId;
    }

    public int getfrequency() {
        return frequency;
    }

    public int gettimepiece() {
        return timepiece;
    }

    public String getnumber() {return number;}

    public String getform() {return form;}
    public String getsize() {return size;}
    public String getfunction() {return function;}
    public String getusage() {return usage;}
    public String getWarning() {return warning;}

    public String gettime1a() {return time1a;}
    public String gettime1b() {return time1b;}
    public String gettime2a() {return time2a;}
    public String gettime2b() {return time2b;}
    public String gettime3a() {return time3a;}
    public String gettime3b() {return time3b;}
    public String gettime4a() {return time4a;}
    public String gettime4b() {return time4b;}
}
