package com.example.collegeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.collegeconnect.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class OnBoardingScreenm extends AppCompatActivity {

    private int[] layouts={R.layout.first_slide, R.layout.second_slide,R.layout.third_slide,R.layout.fourth_slide};
    private Button back,next;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screenm);

//        if (new PreferenceManager(this).checkPreference()){
//            startActivity(new Intent(OnBoardingScreenm.this, navigation .class));
//            finish();
//        }

        final ViewPager pager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new ViewPagerAdapter(layouts,OnBoardingScreenm.this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                createDots(position);
                currentPage=position;
                if (currentPage==0){
                    back.setEnabled(false);
                    next.setEnabled(true);
                    back.setVisibility(View.INVISIBLE);
                }
                else if (currentPage==layouts.length-1){
                    back.setEnabled(true);
                    next.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    next.setText("FINISH");
                }
                else{
                    back.setEnabled(true);
                    next.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    next.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        back=findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(currentPage-1);
            }
        });

        next=findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage==layouts.length-1){
//                    new PreferenceManager(getApplicationContext()).writeSharedPrefernce();
//                    startActivity(new Intent(OnBoardingScreenm.this,MainActivity.class));
                }
                else
                    pager.setCurrentItem(currentPage+1);
            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
    }
}
