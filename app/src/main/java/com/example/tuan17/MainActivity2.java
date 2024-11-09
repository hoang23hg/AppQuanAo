package com.example.tuan17;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tuan17.Adapter.ViewPagerAdapterUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapterUser viewPagerAdapterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initializeViews();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupViewPager() {
        viewPagerAdapterUser = new ViewPagerAdapterUser(this);
        viewPager.setAdapter(viewPagerAdapterUser);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_search:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_cart:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_order:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.nav_profile:
                    viewPager.setCurrentItem(4);
                    return true;
                default:
                    return false;
            }
        });
    }
}
