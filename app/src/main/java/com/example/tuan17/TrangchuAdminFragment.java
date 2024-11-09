package com.example.tuan17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tuan17.Adapter.NhomSanPham_trangChuadmin_Adapter;
import com.example.tuan17.Adapter.SanPham_TrangChuAdmin_Adapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.NhomSanPham;
import com.example.tuan17.Model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TrangchuAdminFragment extends Fragment {

    GridView grv2, grv1;
    ArrayList<SanPham> mangSPgrv1; // Danh sách cho GridView sản phẩm
    ArrayList<NhomSanPham> mangNSPgrv2; // Danh sách cho GridView danh mục
    NhomSanPham_trangChuadmin_Adapter adapterGrv2;
    SanPham_TrangChuAdmin_Adapter adapterGrv1;
    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trangchu_admin, container, false);

        // Ánh xạ các view
        grv2 = view.findViewById(R.id.grv2);
        grv1 = view.findViewById(R.id.grv1);

        // Khởi tạo danh sách
        mangNSPgrv2 = new ArrayList<>();
        mangSPgrv1 = new ArrayList<>();

        // Khởi tạo adapters
        adapterGrv2 = new NhomSanPham_trangChuadmin_Adapter(getContext(), mangNSPgrv2, false);
        grv2.setAdapter(adapterGrv2);
        adapterGrv1 = new SanPham_TrangChuAdmin_Adapter(getContext(), mangSPgrv1, false);
        grv1.setAdapter(adapterGrv1);

        // Khởi tạo Database
        database = new Database(getContext(), "banhang.db", null, 1);

        // Tải dữ liệu cho GridView
        Loaddulieubacsigridview2();
        Loaddulieubacsigridview1();

        // Quản lý BottomNavigationView
//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        @SuppressLint("UseRequireInsteadOfGet")
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
//        String tendn = sharedPreferences.getString("tendn", null);

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_homeadmin:
//                    return true;
//
//                case R.id.nav_dm:
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, new NhomsanphamAdminFragment())
//                            .commit();
//                    return true;

//                case R.id.nav_sp:
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, new SanphamAdminFragment())
//                            .commit();
//                    return true;
//
//                case R.id.nav_orderadmin:
//                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        Intent intent = new Intent(getContext(), DonHang_admin_Activity.class);
//                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getContext(), Login_Activity.class));
//                    }
//                    return true;
//
//                case R.id.nav_profileadmin:
//                    Intent profileIntent = new Intent(getContext(), TrangCaNhan_admin_Activity.class);
//                    profileIntent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                    startActivity(profileIntent);
//                    return true;
//            }
//            return false;
//        });
//
        return view;
    }

    private void Loaddulieubacsigridview2() {
        Cursor dataCongViec = database.GetData("SELECT * from nhomsanpham order by random() limit 10 ");
        mangNSPgrv2.clear();

        while (dataCongViec.moveToNext()) {
            String ma = dataCongViec.getString(0);
            String ten = dataCongViec.getString(1);
            byte[] blob = dataCongViec.getBlob(2); // Lấy mảng byte từ cột chứa ảnh
            mangNSPgrv2.add(new NhomSanPham(ma, ten, blob));
        }

        adapterGrv2.notifyDataSetChanged(); // Cập nhật adapter
    }

    private void Loaddulieubacsigridview1() {
        Cursor cursor = database.GetData("SELECT * FROM sanpham order by random() limit 8");
        mangSPgrv1.clear();

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
                mangSPgrv1.add(new SanPham(masp, tensp, dongia, mota, ghichu, soluongkho, maso, blob));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getContext(), "Null load dữ liệu", Toast.LENGTH_SHORT).show();
        }

        adapterGrv1.notifyDataSetChanged();
    }
}
