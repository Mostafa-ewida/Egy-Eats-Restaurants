package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.itechpro.egyeatsrestaurant.Model.Category;

public class MainActivity extends AppCompatActivity {

    TabLayout main_tablayout;
    ViewPager main_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_tablayout = findViewById(R.id.main_tablayout);
        main_viewPager = findViewById(R.id.main_viewPager);

        main_tablayout.addTab(main_tablayout.newTab().setText("Profile"));
        main_tablayout.addTab(main_tablayout.newTab().setText("Menu"));
        main_tablayout.addTab(main_tablayout.newTab().setText("Cart"));
        main_tablayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), this, main_tablayout.getTabCount());
        main_viewPager.setAdapter(adapter);
        if (getIntent().getStringExtra("tabID") != null) {
            if (getIntent().getStringExtra("tabID").equals("1")){
                main_viewPager.setCurrentItem(1);
            }else if (getIntent().getStringExtra("tabID").equals("2")){
                main_viewPager.setCurrentItem(2);
            }else if (getIntent().getStringExtra("tabID").equals("0")){
                main_viewPager.setCurrentItem(0);
            }
        }else{
            main_viewPager.setCurrentItem(1);
        }


        main_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(main_tablayout));
        main_tablayout.setupWithViewPager(main_viewPager);




    }

}