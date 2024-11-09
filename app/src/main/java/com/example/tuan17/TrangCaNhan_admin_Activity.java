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

public class TrangCaNhan_admin_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan_admin);
        Button dangxuat = findViewById(R.id.btndangxuat);
        TextView textTendn = findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(TrangCaNhan_admin_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish(); // Kết thúc activity nếu chưa đăng nhập
            return;
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_homeadmin:
                    startActivity(new Intent(getApplicationContext(), TrangchuAdmin_Activity.class));
                    return true;

                case R.id.nav_dm:
                    startActivity(new Intent(getApplicationContext(), Nhomsanpham_admin_Actvity.class));
                    return true;

                case R.id.nav_sp:
                    startActivity(new Intent(getApplicationContext(), Sanpham_admin_Activity.class));
                    return true;
                case R.id.nav_orderadmin:
                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                        Intent intent = new Intent(getApplicationContext(), DonHang_admin_Activity.class);
                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                    }
                    return true;

                case R.id.nav_profileadmin:
                    Intent intent = new Intent(getApplicationContext(), TrangCaNhan_admin_Activity.class);
                    intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
                    startActivity(intent);
                    return true;
            }
            return false;
        });
        Button btntaikhoan =findViewById(R.id.btntaikhoan);
        btntaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),Taikhoan_admin_Activity.class);
                startActivity(a);
            }
        });


        dangxuat.setOnClickListener(v -> {
            new AlertDialog.Builder(TrangCaNhan_admin_Activity.this)
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