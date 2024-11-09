package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrangCaNhan_nguoidung_Activity extends AppCompatActivity {
    String tendn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan_nguoidung);

        Button dangxuat = findViewById(R.id.btndangxuat);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Lấy giá trị tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
       tendn = sharedPreferences.getString("tendn", null);

        // Nếu SharedPreferences không có, lấy từ Intent
        if (tendn == null) {
            tendn = getIntent().getStringExtra("tendn");
        }

        // Kiểm tra giá trị tendn
        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            // Chưa đăng nhập, chuyển đến trang login
            Intent intent = new Intent(TrangCaNhan_nguoidung_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish(); // Kết thúc activity nếu chưa đăng nhập
            return;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), TrangchuNgdung_Activity.class));
                    return true;

                case R.id.nav_search:
                    startActivity(new Intent(getApplicationContext(), TimKiemSanPham_Activity.class));
                    return true;

                case R.id.nav_cart:
                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
                    if (!isLoggedIn) {
                        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), GioHang_Activity.class));
                    }
                    return true;

                case R.id.nav_order:
                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                        Intent intent = new Intent(getApplicationContext(), DonHang_User_Activity.class);
                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                    }
                    return true;

                case R.id.nav_profile:
                    Intent intent = new Intent(getApplicationContext(), TrangCaNhan_nguoidung_Activity.class);
                    intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
                    startActivity(intent);
                    return true;
            }
            return false;
        });

        dangxuat.setOnClickListener(v -> {
            new AlertDialog.Builder(TrangCaNhan_nguoidung_Activity.this)
                    .setTitle("Đăng Xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xóa trạng thái đăng nhập
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("tendn", null);
                        editor.apply();

                        // Quay lại Activity chính
                        Intent intent = new Intent(getApplicationContext(), TrangchuNgdung_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Kết thúc activity
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }
}
