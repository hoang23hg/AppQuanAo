package com.example.tuan17;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrangCaNhanNguoiDungFragment extends Fragment {
    private String tendn;
    private SharedPreferences sharedPreferences;

    @SuppressLint("UseRequireInsteadOfGet")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan_nguoi_dung, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button dangxuat = view.findViewById(R.id.btndangxuat);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView textTendn = view.findViewById(R.id.tendn);
//        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        tendn = sharedPreferences.getString("tendn", null);

        if (tendn == null) {
            tendn = getActivity().getIntent().getStringExtra("tendn");
        }

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            startActivity(new Intent(getActivity(), Login_Activity.class));
            getActivity().finish();
            return view;
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            Intent intent;
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    intent = new Intent(getActivity(), TrangchuNgdung_Activity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.nav_search:
//                    intent = new Intent(getActivity(), TimKiemSanPham_Activity.class);
//                    startActivity(intent);
//                    return true;
//                case R.id.nav_cart:
//                    if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        startActivity(new Intent(getActivity(), Login_Activity.class));
//                    } else {
//                        startActivity(new Intent(getActivity(), GioHang_Activity.class));
//                    }
//                    return true;
//                case R.id.nav_order:
//                    if (sharedPreferences.getBoolean("isLoggedIn", false)) {
//                        intent = new Intent(getActivity(), DonHang_User_Activity.class);
//                        intent.putExtra("tendn", tendn);
//                        startActivity(intent);
//                    } else {
//                        startActivity(new Intent(getActivity(), Login_Activity.class));
//                    }
//                    return true;
//                case R.id.nav_profile:
//                    intent = new Intent(getActivity(), TrangCaNhanNguoiDungFragment.class);
//                    intent.putExtra("tendn", tendn);
//                    startActivity(intent);
//                    return true;
//            }
//            return false;
//        });

        dangxuat.setOnClickListener(v -> new AlertDialog.Builder(getActivity())
                .setTitle("Đăng Xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialog, which) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.putString("tendn", null);
                    editor.apply();

                    Intent intent = new Intent(getActivity(), TrangchuNgdung_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                })
                .setNegativeButton("Không", null)
                .show());

        return view;
    }
}
