package com.example.tuan17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.DonHang_Adapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DonHangAdminFragment extends Fragment {

    private Database database;
    private ListView listView;
    private DonHang_Adapter donHangAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don_hang_admin, container, false);

        // Khởi tạo các thành phần
        listView = view.findViewById(R.id.listViewChiTiet);
        database = new Database(requireContext(), "banhang.db", null, 1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = donHangAdapter.getItem(position);

                if (order != null) {
                    // Hiển thị Toast với ID đơn hàng
                    Toast.makeText(requireContext(), "ID đơn hàng: " + order.getId(), Toast.LENGTH_SHORT).show();

                    // Gửi thông tin đơn hàng qua Intent
                    Intent intent = new Intent(requireContext(), ChiTietDonHang_Admin_Fragment.class);
                    intent.putExtra("donHangId", String.valueOf(order.getId())); // Đảm bảo rằng ID là chuỗi
                    startActivity(intent);
                }
            }
        });

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", requireContext().MODE_PRIVATE);
//        String tendn = sharedPreferences.getString("tendn", null);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_homeadmin:
//                    startActivity(new Intent(getContext(), TrangchuAdmin_Activity.class));
//                    return true;
//
//                case R.id.nav_dm:
//                    startActivity(new Intent(getContext(), Nhomsanpham_admin_Actvity.class));
//                    return true;
//
//                case R.id.nav_sp:
//                    startActivity(new Intent(getContext(), Sanpham_admin_Activity.class));
//                    return true;
//
//                case R.id.nav_orderadmin:
//                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        Intent intent = new Intent(getContext(), DonHang_admin_Activity.class);
//                        intent.putExtra("tendn", tendn);
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getContext(), Login_Activity.class));
//                    }
//                    return true;
//
//                case R.id.nav_profileadmin:
//                    Intent intent = new Intent(getContext(), TrangCaNhan_admin_Activity.class);
//                    intent.putExtra("tendn", tendn);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        });

        // Tạo bảng nếu chưa tồn tại
        createTableIfNotExists();

        // Gọi phương thức loadDonHang với tenDN
        loadDonHang();

        return view;
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
            Toast.makeText(getContext(), "Không tìm thấy đơn hàng nào!", Toast.LENGTH_SHORT).show();
        } else {
            // Sử dụng DonHangAdapter để hiển thị danh sách đơn hàng
            donHangAdapter = new DonHang_Adapter(getContext(), orders);
            listView.setAdapter(donHangAdapter); // Gán adapter cho ListView
        }
    }
}
