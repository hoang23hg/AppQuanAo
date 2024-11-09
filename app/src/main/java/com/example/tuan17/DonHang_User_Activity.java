package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuan17.Adapter.DonHang_Adapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DonHang_User_Activity extends AppCompatActivity {
    private Database database;
    private ListView listView;
    private DonHang_Adapter donHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_user);


        listView = findViewById(R.id.listViewChiTiet);
        database = new Database(this, "banhang.db", null, 1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = donHangAdapter.getItem(position);

                if (order != null) {
                    // Hiển thị Toast với ID đơn hàng
                    Toast.makeText(DonHang_User_Activity.this, "ID đơn hàng: " + order.getId(), Toast.LENGTH_SHORT).show();

                    // Gửi thông tin đơn hàng qua Intent
                    Intent intent = new Intent(DonHang_User_Activity.this, ChiTietDonHangFragment.class);
                    intent.putExtra("donHangId", String.valueOf(order.getId())); // Đảm bảo rằng ID là chuỗi
                    startActivity(intent);
                }
            }


        });


        // Tạo bảng nếu chưa tồn tại
        createTableIfNotExists();

        // Lấy tên đăng nhập từ Intent
        String tenDN = getIntent().getStringExtra("tendn");

// Kiểm tra giá trị tenDN
        if (tenDN == null || tenDN.isEmpty()) {
            Toast.makeText(this, "Tên đăng nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc activity nếu không có tên đăng nhập
            return;
        }

        loadDonHang(tenDN); // Gọi phương thức loadDonHang với tenDN

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

    }

    private void createTableIfNotExists() {
        // Tạo bảng đơn hàng nếu chưa tồn tại
        database.QueryData("CREATE TABLE IF NOT EXISTS Dathang (" +
                "id_dathang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenkh TEXT, " +
                "diachi TEXT, " +
                "sdt TEXT, " +
                "tongthanhtoan REAL, " +
                "ngaydathang DATETIME DEFAULT CURRENT_TIMESTAMP);");
    }

    private void loadDonHang(String tenKh) {
        // Kiểm tra tên khách hàng trước khi truy vấn
        if (tenKh == null || tenKh.isEmpty()) {
            Toast.makeText(this, "Tên khách hàng không hợp lệ!", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu tên khách hàng là null hoặc rỗng
        }

        // Lấy danh sách đơn hàng từ cơ sở dữ liệu
        List<Order> orders = database.getDonHangByTenKh(tenKh);
        if (orders.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy đơn hàng cho khách hàng này!", Toast.LENGTH_SHORT).show();
        } else {
            // Sử dụng DonHangAdapter để hiển thị danh sách đơn hàng
            donHangAdapter = new DonHang_Adapter(this, orders);
            listView.setAdapter(donHangAdapter); // Gán adapter cho ListView
        }
    }
}