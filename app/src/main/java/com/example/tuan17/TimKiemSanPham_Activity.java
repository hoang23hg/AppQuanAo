package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuan17.Adapter.SanPham_TimKiem_Adapter;
import com.example.tuan17.DbHelper.DatabaseHelper;
import com.example.tuan17.Model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TimKiemSanPham_Activity extends AppCompatActivity {

    private GridView grv;
    private ArrayList<SanPham> productList; // Change to ArrayList
    private SanPham_TimKiem_Adapter productAdapter;
    private DatabaseHelper dbHelper;
    String tendn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem_san_pham);
        EditText timkiem=findViewById(R.id.timkiem);
        timkiem.requestFocus();
        // Initialize the GridView and DatabaseHelper
        grv = findViewById(R.id.grv);
        dbHelper = new DatabaseHelper(this);
        productList = new ArrayList<>();
        // Initialize and set the adapter with the product list
        productAdapter = new SanPham_TimKiem_Adapter(this, productList, false);
        grv.setAdapter(productAdapter);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tendn = sharedPreferences.getString("tendn", null);

        // Nếu SharedPreferences không có, thử lấy từ Intent
        if (tendn == null) {
            tendn = getIntent().getStringExtra("tendn");
        }

        TextView textTendn = findViewById(R.id.tendn);
        // Kiểm tra giá trị tendn
        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            // Nếu không có tên đăng nhập, chuyển đến trang login
            Intent intent = new Intent(TimKiemSanPham_Activity.this, Login_Activity.class);
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

        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = timkiem.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Gọi phương thức tìm kiếm trong DatabaseHelper
                    productList.clear(); // Xóa danh sách trước khi thêm kết quả mới
                    ArrayList<SanPham> foundProducts = dbHelper.searchSanPhamByName(query);
                    if (foundProducts.isEmpty()) {
                        Toast.makeText(TimKiemSanPham_Activity.this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                    } else {
                        productList.addAll(foundProducts);
                    }
                    productAdapter.notifyDataSetChanged(); // Cập nhật adapter
                } else {
                    Toast.makeText(TimKiemSanPham_Activity.this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}