package com.example.tuan17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.SanPhamAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SanphamAdminFragment extends Fragment {

    private Database database;
    private ListView lv;
    private FloatingActionButton addButton;
    private ArrayList<SanPham> mangSP;
    private SanPhamAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sanpham_admin, container, false);

        initializeViews(view);
        setupDatabase();
        loadData();

        addButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ThemSanPham_Activity.class);
            startActivity(intent);
        });

//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
//        String tendn = sharedPreferences.getString("tendn", null);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_homeadmin:
//                    startActivity(new Intent(getActivity(), TrangchuAdminFragment.class));
//                    return true;
//
//                case R.id.nav_dm:
//                    startActivity(new Intent(getActivity(), NhomsanphamAdminFragment.class));
//                    return true;
//
//                case R.id.nav_sp:
//                    return true;
//
////                case R.id.nav_orderadmin:
////                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
////                        Intent intent = new Intent(getActivity(), DonHang_admin_Activity.class);
////                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
////                        startActivity(intent);
////                    } else {
////                        startActivity(new Intent(getActivity(), Login_Activity.class));
////                    }
////                    return true;
////
////                case R.id.nav_profileadmin:
////                    Intent intent = new Intent(getActivity(), TrangCaNhan_admin_Activity.class);
////                    intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
////                    startActivity(intent);
////                    return true;
//            }
//            return false;
//        });

        return view;
    }

    private void initializeViews(View view) {
        lv = view.findViewById(R.id.listtk);
        addButton = view.findViewById(R.id.btnthem);
        mangSP = new ArrayList<>();

        adapter = new SanPhamAdapter(getActivity(), mangSP, true);

        lv.setAdapter(adapter);
    }

    private void setupDatabase() {
        database = new Database(getActivity(), "banhang.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS sanpham(masp INTEGER PRIMARY KEY AUTOINCREMENT, tensp NVARCHAR(200),dongia FLOAT,mota TEXT,ghichu TEXT,soluongkho INTEGER,maso INTEGER , anh BLOB)");
    }

    private void loadData() {
        Cursor cursor = database.GetData("SELECT * FROM sanpham");
        mangSP.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String masp = cursor.getString(0);
                String tensp = cursor.getString(1);
                float dongia = cursor.getFloat(2); // Giữ nguyên là float
                String mota = cursor.getString(3);
                String ghichu = cursor.getString(4);
                int soluongkho = cursor.getInt(5); // Giữ nguyên là int
                String maso = cursor.getString(6);
                byte[] blob = cursor.getBlob(7);
                mangSP.add(new SanPham(masp,tensp,dongia,mota,ghichu,soluongkho,maso,blob));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getActivity(), "Null load dữ liệu", Toast.LENGTH_SHORT).show();
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
