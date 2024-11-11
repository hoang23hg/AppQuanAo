package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tuan17.Adapter.SanPham_DanhMuc_Adapter;
import com.example.tuan17.DbHelper.DatabaseHelper;
import com.example.tuan17.Model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DanhMucSanPham_Activity extends AppCompatActivity {
    private GridView grv;
    private ArrayList<SanPham> productList;
    private SanPham_DanhMuc_Adapter productAdapter;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private String tendn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_san_pham);

        // Initialize the GridView and DatabaseHelper
        grv = findViewById(R.id.grv);
        dbHelper = new DatabaseHelper(this);

        // Retrieve nhomSpId from the Intent
        String nhomSpId = getIntent().getStringExtra("nhomSpId");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        tendn = sharedPreferences.getString("tendn", "default_user");

        // Check if nhomSpId is not null
        if (nhomSpId != null) {
            // Get the list of products by nhomSpId
            List<SanPham> tempProductList = dbHelper.getProductsByNhomSpId(nhomSpId);
            if (tempProductList != null && !tempProductList.isEmpty()) {
                productList = new ArrayList<>(tempProductList);
                productAdapter = new SanPham_DanhMuc_Adapter(this, productList, false);
                grv.setAdapter(productAdapter);
            } else {
                Toast.makeText(this, "Không tìm thấy sản phẩm nào trong nhóm này!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ID nhóm sản phẩm không hợp lệ!", Toast.LENGTH_SHORT).show();
        }

        // Initialize and set listener for BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
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
                            intent.putExtra("tendn", tendn);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(getApplicationContext(), Login_Activity.class));
                        }
                        return true;
                    case R.id.nav_profile:
                        Intent intent = new Intent(getApplicationContext(), TrangCaNhan_nguoidung_Activity.class);
                        intent.putExtra("tendn", tendn);
                        startActivity(intent);
                        return true;
                }
                return false;
            });
        } else {
            Toast.makeText(this, "Không tìm thấy BottomNavigationView!", Toast.LENGTH_SHORT).show();
        }

        grv.setOnItemClickListener((parent, view, position, id) -> {
            SanPham sanPham = productList.get(position);

            Intent intent = new Intent(DanhMucSanPham_Activity.this, ChiTietSanPham_Activity.class);
            intent.putExtra("masp", sanPham.getMasp());
            intent.putExtra("tensp", sanPham.getTensp());
            intent.putExtra("dongia", sanPham.getDongia());
            intent.putExtra("mota", sanPham.getMota());
            intent.putExtra("ghichu", sanPham.getGhichu());
            intent.putExtra("soluongkho", sanPham.getSoluongkho());
            intent.putExtra("maso", sanPham.getMansp());
            intent.putExtra("anh", sanPham.getAnh());

            startActivity(intent);
        });
    }
}
