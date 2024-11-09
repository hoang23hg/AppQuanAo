package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuan17.Adapter.DonHang_Adapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DonHang_admin_Activity extends AppCompatActivity {

    private Database database;
    private ListView listView;
    private DonHang_Adapter donHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_admin);

        // Khởi tạo các thành phần
        listView = findViewById(R.id.listViewChiTiet);
        database = new Database(this, "banhang.db", null, 1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = donHangAdapter.getItem(position);

                if (order != null) {
                    // Hiển thị Toast với ID đơn hàng
                    Toast.makeText(DonHang_admin_Activity.this, "ID đơn hàng: " + order.getId(), Toast.LENGTH_SHORT).show();

                    // Gửi thông tin đơn hàng qua Intent
                    Intent intent = new Intent(DonHang_admin_Activity.this, ChiTietDonHang_Admin_Fragment.class);
                    intent.putExtra("donHangId", String.valueOf(order.getId())); // Đảm bảo rằng ID là chuỗi
                    startActivity(intent);
                }
            }


        });
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String tendn = sharedPreferences.getString("tendn", null);
//
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_homeadmin:
//                    startActivity(new Intent(getApplicationContext(), TrangchuAdmin_Activity.class));
//                    return true;
//
//                case R.id.nav_dm:
//                    startActivity(new Intent(getApplicationContext(), Nhomsanpham_admin_Actvity.class));
//                    return true;
//
//                case R.id.nav_sp:
//                    startActivity(new Intent(getApplicationContext(), Sanpham_admin_Activity.class));
//                    return true;
//                case R.id.nav_orderadmin:
//                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        Intent intent = new Intent(getApplicationContext(), DonHang_admin_Activity.class);
//                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
//                    }
//                    return true;
//
//                case R.id.nav_profileadmin:
//                    Intent intent = new Intent(getApplicationContext(), TrangCaNhan_admin_Activity.class);
//                    intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        });


        // Tạo bảng nếu chưa tồn tại
        createTableIfNotExists();



        loadDonHang(); // Gọi phương thức loadDonHang với tenDN
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

    private void loadDonHang() {

        // Lấy danh sách đơn hàng từ cơ sở dữ liệu
        List<Order> orders = database.getAllDonHang();
        if (orders.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy đơn hàng nào!", Toast.LENGTH_SHORT).show();
        } else {
            // Sử dụng DonHangAdapter để hiển thị danh sách đơn hàng
            donHangAdapter = new DonHang_Adapter(this, orders);
            listView.setAdapter(donHangAdapter); // Gán adapter cho ListView
        }
    }
}