package com.example.tuan17;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class TrangCaNhanAdminFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan_admin, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button dangxuat = view.findViewById(R.id.btndangxuat);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView textTendn = view.findViewById(R.id.tendn);

        requireContext();
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            startActivity(new Intent(getContext(), Login_Activity.class));
            requireActivity().finish();
            return view;
        }

        
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button btntaikhoan = view.findViewById(R.id.btntaikhoan);
        btntaikhoan.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Taikhoan_admin_Activity.class);
            startActivity(intent);
        });

        dangxuat.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng Xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("tendn", null);
                        editor.apply();

                        Intent intent = new Intent(getContext(), TrangchuNgdung_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        return view;
    }
}
