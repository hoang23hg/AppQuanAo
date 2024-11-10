package com.example.tuan17;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tuan17.Adapter.ViewPagerAdapter;
import com.example.tuan17.Adapter.ViewPagerAdapterUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationViewuser;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Lấy giá trị userRole từ SharedPreferences dưới dạng chuỗi
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", "user");  // Mặc định là "user" nếu không có giá trị
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userRole", "admin");  // Lưu quyền là "admin"
        editor.apply();
        // Cập nhật isAdmin dựa trên giá trị chuỗi của userRole
        isAdmin = userRole.equals("admin");  // Kiểm tra nếu quyền là "admin"

        initializeViews();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationViewuser = findViewById(R.id.bottom_navigationuser);

        // Ẩn hoặc hiện BottomNavigationView tùy theo vai trò người dùng
        if (isAdmin) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationViewuser.setVisibility(View.GONE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavigationViewuser.setVisibility(View.VISIBLE);
        }
    }

    private void setupViewPager() {
        if (isAdmin) {
            // Sử dụng adapter cho admin
            viewPager.setAdapter(new ViewPagerAdapter(this));
        } else {
            // Sử dụng adapter cho user
            viewPager.setAdapter(new ViewPagerAdapterUser(this));
        }

        // Đồng bộ ViewPager với BottomNavigationView khi chuyển trang
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isAdmin) {
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                } else {
                    bottomNavigationViewuser.getMenu().getItem(position).setChecked(true);
                }
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int position;
            switch (item.getItemId()) {
                case R.id.nav_homeadmin:
                    position = 0;
                    break;
                case R.id.nav_dm:
                    position = 1;
                    break;
                case R.id.nav_sp:
                    position = 2;
                    break;
                case R.id.nav_orderadmin:
                    position = 3;
                    break;
                case R.id.nav_profileadmin:
                    position = 4;
                    break;
                default:
                    return false;
            }
            viewPager.setCurrentItem(position);
            return true;
        });

        bottomNavigationViewuser.setOnItemSelectedListener(item -> {
            int position;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    position = 0;
                    break;
                case R.id.nav_search:
                    position = 1;
                    break;
                case R.id.nav_cart:
                    position = 2;
                    break;
                case R.id.nav_order:
                    position = 3;
                    break;
                case R.id.nav_profile:
                    position = 4;
                    break;
                default:
                    return false;
            }
            viewPager.setCurrentItem(position);
            return true;
        });
    }
}
