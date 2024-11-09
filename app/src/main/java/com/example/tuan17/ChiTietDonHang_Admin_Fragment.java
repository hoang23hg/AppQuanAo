package com.example.tuan17;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.ChiTietDonHangAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.DbHelper.DatabaseHelper;
import com.example.tuan17.Model.ChiTietDonHang;

import java.util.List;

public class ChiTietDonHang_Admin_Fragment extends Fragment {

    DatabaseHelper dbdata;
    Database database;
    ListView listViewChiTiet;
    ChiTietDonHangAdapter chiTietAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_don_hang__admin_, container, false);

        // Khởi tạo cơ sở dữ liệu
        dbdata = new DatabaseHelper(getContext());
        database = new Database(getContext(), "banhang.db", null, 1);

        createTableIfNotExists();

        // Khởi tạo ListView để hiển thị chi tiết đơn hàng
        listViewChiTiet = view.findViewById(R.id.listtk);

        // Lấy ID đơn hàng từ Bundle
        Bundle args = getArguments();
        String donHangIdStr = args != null ? args.getString("donHangId") : null;

        if (donHangIdStr != null) {
            try {
                int donHangId = Integer.parseInt(donHangIdStr);

                // Lấy chi tiết đơn hàng từ database
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

        // Setup button listeners
        setupButtons(view);

        return view;
    }

    private void setupButtons(View view) {
        // Set up button listeners inside this method
        ImageButton btntrangchu = view.findViewById(R.id.btntrangchu);
        btntrangchu.setOnClickListener(v -> {
            if (getContext() != null) {
                startActivity(new Intent(getContext(), TrangchuAdmin_Activity.class));
            }
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
