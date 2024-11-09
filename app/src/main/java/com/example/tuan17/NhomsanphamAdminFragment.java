package com.example.tuan17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tuan17.Adapter.NhomSanPhamAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.NhomSanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NhomsanphamAdminFragment extends Fragment {

    private Database database;
    private ListView lv;
    private FloatingActionButton addButton;
    private ArrayList<NhomSanPham> mangNSP;
    private NhomSanPhamAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nhomsanpham_admin, container, false);

        // Khởi tạo các view
        initializeViews(view);
        setupDatabase();
        loadData();

        // Quản lý BottomNavigationView
//        @SuppressLint({"LocalSuppress", "MissingInflatedId"})
//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
//        String tendn = sharedPreferences.getString("tendn", null);

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_homeadmin:
//
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, new TrangchuAdminFragment())
//                            .commit();
//                    return true;
//
//                case R.id.nav_dm:
//                    return true;
//
////                case R.id.nav_sp:
////                    getFragmentManager().beginTransaction()
////                            .replace(R.id.fragment_container, new SanphamAdminFragment())
////                            .commit();
////                    return true;
////
////                case R.id.nav_orderadmin:
////                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
////                        Intent intent = new Intent(getContext(), DonHang_admin_Activity.class);
////                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
////                        startActivity(intent);
////                    } else {
////                        startActivity(new Intent(getContext(), Login_Activity.class));
////                    }
////                    return true;
////
////                case R.id.nav_profileadmin:
////                    Intent profileIntent = new Intent(getContext(), TrangCaNhan_admin_Activity.class);
////                    profileIntent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
////                    startActivity(profileIntent);
////                    return true;
//            }
//            return false;
//        });

        // Xử lý sự kiện thêm nhóm sản phẩm
        addButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ThemNhomSanPham_Activity.class);
            startActivity(intent);
        });

        return view;
    }

    private void initializeViews(View view) {
        lv = view.findViewById(R.id.listtk);
        addButton = view.findViewById(R.id.btnthem);
        mangNSP = new ArrayList<>();

        adapter = new NhomSanPhamAdapter(getContext(), mangNSP, true);
        lv.setAdapter(adapter);
    }

    private void setupDatabase() {
        database = new Database(getContext(), "banhang.db", null, 1);
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
            Toast.makeText(getContext(), "Null load dữ liệu", Toast.LENGTH_SHORT).show();
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
