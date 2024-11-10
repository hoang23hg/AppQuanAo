package com.example.tuan17;

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
import com.example.tuan17.R;

import java.util.List;

public class ChiTietDonHangFragment extends Fragment {

    private DatabaseHelper dbdata;
    private Database database;
    private ListView listViewChiTiet;
    private ChiTietDonHangAdapter chiTietAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_chi_tiet_don_hang, container, false);


        dbdata = new DatabaseHelper(getContext());
        database = new Database(getContext(), "banhang.db", null, 1);

        createTableIfNotExists();

        listViewChiTiet = view.findViewById(R.id.listtk);

        // Lấy ID đơn hàng từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            String donHangIdStr = args.getString("donHangId");

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



        return view;
    }

    private void createTableIfNotExists() {
        // Tạo bảng Chitietdonhang nếu chưa tồn tại
        database.QueryData("CREATE TABLE IF NOT EXISTS Chitietdonhang (" +
                "id_chitiet INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_dathang INTEGER, " +
                "masp INTEGER, " +
                "soluong INTEGER, " +
                "dongia REAL, " +
                "anh TEXT, " +
                "FOREIGN KEY(id_dathang) REFERENCES Dathang(id_dathang));");
    }

    private void checkLoginAndNavigate(Class<?> destinationClass) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        Intent intent = new Intent(getContext(), isLoggedIn ? destinationClass : Login_Activity.class);
        startActivity(intent);
    }

    private void navigateToActivity(Class<?> destinationClass) {
        Intent intent = new Intent(getContext(), destinationClass);
        startActivity(intent);
    }
}
