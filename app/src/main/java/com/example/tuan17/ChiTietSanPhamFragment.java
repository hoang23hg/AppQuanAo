package com.example.tuan17;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tuan17.Model.ChiTietSanPham;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class ChiTietSanPhamFragment extends Fragment {

    private String masp, tendn;
    private Button btndathang, btnaddcart;
    private ChiTietSanPham chiTietSanPham;
    private GioHangManager gioHangManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chi_tiet_san_pham, container, false);

        // Khởi tạo các thành phần giao diện
        btndathang = view.findViewById(R.id.btndathang);
        btnaddcart = view.findViewById(R.id.btnaddcart);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tensp = view.findViewById(R.id.tensp);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})ImageView imgsp = view.findViewById(R.id.imgsp);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView dongia = view.findViewById(R.id.dongia);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView mota = view.findViewById(R.id.mota);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView soluongkho = view.findViewById(R.id.soluongkho);
        gioHangManager = GioHangManager.getInstance(); // Sử dụng singleton
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView textTendn = view.findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập

        // Lấy tendn từ SharedPreferences
        SharedPreferences sharedPre = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        tendn = sharedPre.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(getActivity(), Login_Activity.class);
            startActivity(intent);
            getActivity().finish(); // Kết thúc activity nếu chưa đăng nhập
            return null;
        }

        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            chiTietSanPham = bundle.getParcelable("chitietsanpham");
            if (chiTietSanPham != null) {
                masp = chiTietSanPham.getMasp(); // Lấy mã sản phẩm từ chi tiết
                tensp.setText(chiTietSanPham.getTensp());
                dongia.setText(chiTietSanPham.getDongia() != null ? String.valueOf(chiTietSanPham.getDongia()) : "Không có dữ liệu");
                mota.setText(chiTietSanPham.getMota() != null ? chiTietSanPham.getMota() : "Không có dữ liệu");
                soluongkho.setText(String.valueOf(chiTietSanPham.getSoluongkho()));
                byte[] anhByteArray = chiTietSanPham.getAnh();
                if (anhByteArray != null && anhByteArray.length > 0) {
                    Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
                    imgsp.setImageBitmap(imganhbs);
                } else {
                    imgsp.setImageResource(R.drawable.vest); // Ảnh mặc định
                }
            } else {
                tensp.setText("Không có dữ liệu");
            }
        }

        // Kiểm tra trạng thái đăng nhập và thêm sản phẩm vào giỏ hàng
        btnaddcart.setOnClickListener(view1 -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (!isLoggedIn) {
                Intent loginIntent = new Intent(getActivity(), Login_Activity.class);
                startActivity(loginIntent);
            } else {
                gioHangManager.addItem(chiTietSanPham); // Thêm vào giỏ hàng
                Toast.makeText(getActivity(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();

                // Quay về trang chủ
                replaceFragment(new TrangchuNgdungFragment());
            }
        });

        btndathang.setOnClickListener(view12 -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (!isLoggedIn) {
                Intent loginIntent = new Intent(getActivity(), Login_Activity.class);
                startActivity(loginIntent);
            } else {
                gioHangManager.addItem(chiTietSanPham); // Thêm vào giỏ hàng
                // Chuyển đến fragment xác nhận đặt hàng
                replaceFragment(new DonHangUserFragment());
            }
        });


        return view;
    }

    private void replaceFragment(Fragment fragment) {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return; // Nếu fragment mới giống với fragment hiện tại thì không thay thế
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
