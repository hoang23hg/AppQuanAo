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

import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.DonHang_Adapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class DonHangUserFragment extends Fragment {
    private Database database;
    private ListView listView;
    private DonHang_Adapter donHangAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_don_hang_user, container, false);

        listView = rootView.findViewById(R.id.listViewChiTiet);
        database = new Database(getContext(), "banhang.db", null, 1);

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = donHangAdapter.getItem(position);

                if (order != null) {
                    // Hiển thị Toast với ID đơn hàng
                    Toast.makeText(getContext(), "ID đơn hàng: " + order.getId(), Toast.LENGTH_SHORT).show();

                    // Gửi thông tin đơn hàng qua Intent
                    Intent intent = new Intent(getContext(), ChiTietDonHangFragment.class);
                    intent.putExtra("donHangId", String.valueOf(order.getId())); // Đảm bảo rằng ID là chuỗi
                    startActivity(intent);
                }
            }
        });

        // Tạo bảng nếu chưa tồn tại
        createTableIfNotExists();

        // Lấy tên đăng nhập từ Intent
        Bundle bundle = getArguments();
        String tenDN = null; // Giá trị mặc định nếu không có đối tượng trong Bundle
        if (bundle != null) {
            tenDN = bundle.getString("tendn");
        }

        // Kiểm tra giá trị tenDN
        if (tenDN == null || tenDN.isEmpty()) {
            Toast.makeText(getContext(), "Tên đăng nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            return null; // Không tiếp tục nếu không có tên đăng nhập
        }

        loadDonHang(tenDN); // Gọi phương thức loadDonHang với tenDN

//        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    startActivity(new Intent(getContext(), TrangchuNgdung_Activity.class));
//                    return true;
//                case R.id.nav_search:
//                    startActivity(new Intent(getContext(), TimKiemSanPham_Activity.class));
//                    return true;
//                case R.id.nav_cart:
//                    boolean isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
//                    if (!isLoggedIn) {
//                        startActivity(new Intent(getContext(), Login_Activity.class));
//                    } else {
//                        startActivity(new Intent(getContext(), GioHang_Activity.class));
//                    }
//                    return true;
//                case R.id.nav_order:
//                    if (sharedPrefs.getBoolean("isLoggedIn", false)) {
//                        Intent intent = new Intent(getContext(), DonHang_User_Activity.class);
//                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getContext(), Login_Activity.class));
//                    }
//                    return true;
//                case R.id.nav_profile:
//                    Intent profileIntent = new Intent(getContext(), TrangCaNhan_nguoidung_Activity.class);
//                    profileIntent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                    startActivity(profileIntent);
//                    return true;
//            }
//            return false;
//        });

        return rootView;
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

    private void loadDonHang(String tenKh) {
        // Kiểm tra tên khách hàng trước khi truy vấn
        if (tenKh == null || tenKh.isEmpty()) {
            Toast.makeText(getContext(), "Tên khách hàng không hợp lệ!", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu tên khách hàng là null hoặc rỗng
        }

        // Lấy danh sách đơn hàng từ cơ sở dữ liệu
        List<Order> orders = database.getDonHangByTenKh(tenKh);
        if (orders.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy đơn hàng cho khách hàng này!", Toast.LENGTH_SHORT).show();
        } else {
            // Sử dụng DonHangAdapter để hiển thị danh sách đơn hàng
            donHangAdapter = new DonHang_Adapter(getContext(), orders);
            listView.setAdapter(donHangAdapter); // Gán adapter cho ListView
        }
    }
}
