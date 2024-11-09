package com.example.tuan17;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.SanPham_TimKiem_Adapter;
import com.example.tuan17.DbHelper.DatabaseHelper;
import com.example.tuan17.Model.SanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TimKiemSanPhamFragment extends Fragment {

    private GridView grv;
    private ArrayList<SanPham> productList; // Change to ArrayList
    private SanPham_TimKiem_Adapter productAdapter;
    private DatabaseHelper dbHelper;
    private String tendn;

    public TimKiemSanPhamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_tim_kiem_san_pham, container, false);

        EditText timkiem = rootView.findViewById(R.id.timkiem);
        timkiem.requestFocus();

        // Initialize the GridView and DatabaseHelper
        grv = rootView.findViewById(R.id.grv);
        dbHelper = new DatabaseHelper(getContext());
        productList = new ArrayList<>();

        // Initialize and set the adapter with the product list
        productAdapter = new SanPham_TimKiem_Adapter(getContext(), productList, false);
        grv.setAdapter(productAdapter);

//        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", requireContext().MODE_PRIVATE);
        tendn = sharedPreferences.getString("tendn", null);

        // Nếu SharedPreferences không có, thử lấy từ Intent
        if (tendn == null) {
            tendn = getActivity().getIntent().getStringExtra("tendn");
        }

        TextView textTendn = rootView.findViewById(R.id.tendn);
        // Kiểm tra giá trị tendn
        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            // Nếu không có tên đăng nhập, chuyển đến trang login
            Intent intent = new Intent(getActivity(), Login_Activity.class);
            startActivity(intent);
            requireActivity().finish(); // Kết thúc activity nếu chưa đăng nhập
            return rootView;
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    startActivity(new Intent(getActivity(), TrangchuNgdung_Activity.class));
//                    return true;
//
//                case R.id.nav_search:
//                    startActivity(new Intent(getActivity(), TimKiemSanPham_Activity.class));
//                    return true;
//
//                case R.id.nav_cart:
//                    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
//                    if (!isLoggedIn) {
//                        startActivity(new Intent(getActivity(), Login_Activity.class));
//                    } else {
//                        startActivity(new Intent(getActivity(), GioHang_Activity.class));
//                    }
//                    return true;
//
//                case R.id.nav_order:
//                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        Intent intent = new Intent(getActivity(), DonHang_User_Activity.class);
//                        intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getActivity(), Login_Activity.class));
//                    }
//                    return true;
//
//                case R.id.nav_profile:
//                    Intent intent = new Intent(getActivity(), TrangCaNhan_nguoidung_Activity.class);
//                    intent.putExtra("tendn", tendn);  // Truyền tendn qua Intent
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        });

        timkiem.setOnClickListener(v -> {
            String query = timkiem.getText().toString().trim();
            if (!query.isEmpty()) {
                // Gọi phương thức tìm kiếm trong DatabaseHelper
                productList.clear(); // Xóa danh sách trước khi thêm kết quả mới
                ArrayList<SanPham> foundProducts = dbHelper.searchSanPhamByName(query);
                if (foundProducts.isEmpty()) {
                    Toast.makeText(getActivity(), "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    productList.addAll(foundProducts);
                }
                productAdapter.notifyDataSetChanged(); // Cập nhật adapter
            } else {
                Toast.makeText(getActivity(), "Vui lòng nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
