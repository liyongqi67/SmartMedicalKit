package com.example.yyy.medicinekit.MyDrug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.yyy.medicinekit.R;

import java.util.ArrayList;

public class MainActivity1 extends FragmentActivity {

    MYDB mydb ;

    myApplication mapp;

    private ArrayList<View> pageViews;
    //private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private String username = " ";

    private ImageView imageView;
    private ImageView[] imageViews;
    private ViewGroup group;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapp = new myApplication();
        username = mapp.getid();
        mydb = new MYDB(this);


        pageViews = new ArrayList<View>();
        for (int i = 0; i < mydb.getArticlelist().size(); i++) {
            LinearLayout layout = new LinearLayout(this);
            ViewGroup.LayoutParams ltp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(15, 30, 15, 30);
            layout.addView(imageView, ltp);
            pageViews.add(layout);
        }


        setContentView(R.layout.activity_main1);


        imageViews = new ImageView[pageViews.size()];
        group = (ViewGroup) findViewById(R.id.viewGroup);

        for (int i = 0; i < pageViews.size(); i++) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            margin.setMargins(10, 0, 0, 0);
            imageView = new ImageView(MainActivity1.this);

            imageView.setLayoutParams(new ViewGroup.LayoutParams(15, 15));
            imageViews[i] = imageView;
            if (i == 0) {

                imageViews[i]
                        .setBackgroundResource(R.drawable.page_indicator_focused);
            } else {

                imageViews[i]
                        .setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            group.addView(imageViews[i], margin);
        }



        Button button=(Button) findViewById(R.id.mydaily);//我的日程
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity1.this,Daily.class);
                //i.putExtra("name",username);
                startActivity(i);

            }
        });

        Button button2=(Button) findViewById(R.id.kit);//药箱管理
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity1.this,Manage.class);
                //i.putExtra("name",username);
                startActivity(i);

            }
        });

        Button button3=(Button) findViewById(R.id.clock);//我的时刻表
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity1.this,Clock.class);
                startActivity(i);

            }
        });

        Button button4=(Button) findViewById(R.id.account);//账户管理
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity1.this,Account.class);
                //i.putExtra("name",username);
                startActivity(i);

            }
        });


        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[position]
                            .setBackgroundResource(R.drawable.page_indicator_focused);

                    if (position != i) {
                        imageViews[i]
                                .setBackgroundResource(R.drawable.page_indicator_unfocused);
                    }
                }
                invalidateOptionsMenu();
            }
        });




    }

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return mydb.getArticlelist().size();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Stop polling service
        //PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
    }
}



