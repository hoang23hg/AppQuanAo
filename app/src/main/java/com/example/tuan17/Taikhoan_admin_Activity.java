package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.tuan17.Adapter.TaiKhoanAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.TaiKhoan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Taikhoan_admin_Activity extends AppCompatActivity {


    Database database;
    ListView lv;
    int vitri;
    ArrayList<TaiKhoan> mangTK;
    TaiKhoanAdapter adapter;
    FloatingActionButton dauconggocphai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taikhoan_admin);
        dauconggocphai = findViewById(R.id.btnthem);
        lv = findViewById(R.id.listtk);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        @SuppressLint("WrongViewCast")
        ImageButton btnTroVe = findViewById(R.id.trove);
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dauconggocphai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),ThemTaiKhoan_Activity.class);
                startActivity(a);
            }
        });
        mangTK = new ArrayList<>();
        adapter = new TaiKhoanAdapter(getApplicationContext(), R.layout.ds_taikhoan, mangTK);
        lv.setAdapter(adapter);
        database = new Database(this, "banhang.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS taikhoan(tendn VARCHAR(20) PRIMARY KEY, matkhau VARCHAR(50), quyen VARCHAR(50))");
        // Thêm 2 dòng dữ liệu
//        database.QueryData("INSERT  INTO taikhoan VALUES ('admin', '1234', 'admin')");
//        database.QueryData("INSERT  INTO taikhoan VALUES ('bac2', '1111', 'user')");
//        database.QueryData("INSERT  INTO taikhoan VALUES ('bac3', '1111', 'user')");
//        database.QueryData("INSERT  INTO taikhoan VALUES ('bac4', '1111', 'bacsi')");
        Loaddulieutaikhoan();


    }
    private void Loaddulieutaikhoan() {
        Cursor dataCongViec = database.GetData("SELECT * FROM taikhoan");
        mangTK.clear();
        while (dataCongViec.moveToNext()) {
            String tdn = dataCongViec.getString(0);
            String mk= dataCongViec.getString(1);
            String q = dataCongViec.getString(2);
            mangTK.add(new TaiKhoan(tdn, mk, q));
        }
        adapter.notifyDataSetChanged();
    }
}