package com.example.tuan17;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.ChiTietDonHangAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.DbHelper.DatabaseHelper;
import com.example.tuan17.Model.ChiTietDonHang;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChiTietDonHang_Admin_Fragment extends Fragment {

    private DatabaseHelper dbdata;
    private Database database;
    private ListView listViewChiTiet;
    private ChiTietDonHangAdapter chiTietAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_don_hang__admin_, container, false);

        dbdata = new DatabaseHelper(getContext());
        database = new Database(getContext(), "banhang.db", null, 1);
        createTableIfNotExists();

        listViewChiTiet = view.findViewById(R.id.listtk);

        Bundle args = getArguments();
        String donHangIdStr = args != null ? args.getString("donHangId") : null;

        if (donHangIdStr != null) {
            try {
                int donHangId = Integer.parseInt(donHangIdStr);
                List<ChiTietDonHang> chiTietList = dbdata.getChiTietByOrderId(donHangId);
                if (chiTietList != null && !chiTietList.isEmpty()) {
                    chiTietAdapter = new ChiTietDonHangAdapter(getContext(), chiTietList);
                    listViewChiTiet.setAdapter(chiTietAdapter);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy chi tiết cho đơn hàng!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "ID đơn hàng không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        } else {
            List<ChiTietDonHang> allChiTietList = dbdata.getAllChiTietDonHang();
            if (allChiTietList != null && !allChiTietList.isEmpty()) {
                chiTietAdapter = new ChiTietDonHangAdapter(getContext(), allChiTietList);
                listViewChiTiet.setAdapter(chiTietAdapter);
            } else {
                Toast.makeText(getContext(), "Không tìm thấy bất kỳ chi tiết đơn hàng nào!", Toast.LENGTH_SHORT).show();
            }
        }

        setupBottomNavigation(view);

        return view;
    }

    private void setupBottomNavigation(View view) {
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_homeadmin:
                    startActivity(new Intent(getContext(), TrangchuAdmin_Activity.class));
                    return true;

                case R.id.nav_dm:
                    startActivity(new Intent(getContext(), Nhomsanpham_admin_Actvity.class));
                    return true;

                case R.id.nav_sp:
                    startActivity(new Intent(getContext(), Sanpham_admin_Activity.class));
                    return true;

                case R.id.nav_orderadmin:
                    if (requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            .getBoolean("isLoggedIn", false)) {
                        startActivity(new Intent(getContext(), DonHang_admin_Activity.class));
                    } else {
                        startActivity(new Intent(getContext(), Login_Activity.class));
                    }
                    return true;

                case R.id.nav_profileadmin:
                    Intent intent = new Intent(getContext(), TrangCaNhan_admin_Activity.class);
                    intent.putExtra("tendn", requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                            .getString("tendn", null));
                    startActivity(intent);
                    return true;
            }
            return false;
        });
    }

    private void createTableIfNotExists() {
        database.QueryData("CREATE TABLE IF NOT EXISTS Chitietdonhang (" +
                "id_chitiet INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_dathang INTEGER, " +
                "masp INTEGER, " +
                "soluong INTEGER, " +
                "dongia REAL, " +
                "anh TEXT, " +
                "FOREIGN KEY(id_dathang) REFERENCES Dathang(id_dathang));");
    }
}
