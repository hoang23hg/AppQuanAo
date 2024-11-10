package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuan17.Adapter.NhomSanPhamAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.NhomSanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Nhomsanpham_admin_Actvity extends AppCompatActivity {
    private Database database;
    private ListView lv;
    private FloatingActionButton addButton;
    private ArrayList<NhomSanPham> mangNSP;
    private NhomSanPhamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhomsanpham_admin_actvity);

        initializeViews();
        setupDatabase();
        loadData();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);


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
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ThemNhomSanPham_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        lv = findViewById(R.id.listtk);
        addButton = findViewById(R.id.btnthem);
        mangNSP = new ArrayList<>();

        adapter = new NhomSanPhamAdapter(Nhomsanpham_admin_Actvity.this, mangNSP, true);

        lv.setAdapter(adapter);
    }

    private void setupDatabase() {
        database = new Database(this, "banhang.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS nhomsanpham(maso INTEGER PRIMARY KEY AUTOINCREMENT, tennsp NVARCHAR(200), anh BLOB)");
    }

    private void loadData() {
        Cursor cursor = database.GetData("SELECT * FROM nhomsanpham");
        mangNSP.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String maso = cursor.getString(0);
                String tennsp = cursor.getString(1);
                byte[] blob = cursor.getBlob(2);
                mangNSP.add(new NhomSanPham(maso, tennsp, blob));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Null load dữ liuej", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
    private byte[] convertBitmapToByteArray(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}