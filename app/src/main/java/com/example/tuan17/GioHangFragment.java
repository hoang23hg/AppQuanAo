package com.example.tuan17;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tuan17.Adapter.GioHangAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.GioHang;

import java.util.List;

public class GioHangFragment extends Fragment {
    private ListView listView;
    private GioHangAdapter adapter;
    private GioHangManager gioHangManager;
    private Button thanhtoan;
    private Database database;
    private OrderManager orderManager;
    private TextView txtTongTien;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_gio_hang, container, false);

        thanhtoan = rootView.findViewById(R.id.btnthanhtoan);
        listView = rootView.findViewById(R.id.listtk);
        TextView textTendn = rootView.findViewById(R.id.tendn);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            startActivity(new Intent(getActivity(), Login_Activity.class));
            getActivity().finish();
            return rootView;
        }

        txtTongTien = rootView.findViewById(R.id.tongtien);
        database = new Database(getActivity(), "banhang.db", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Dathang (" +
                "id_dathang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenkh TEXT, " +
                "diachi TEXT, " +
                "sdt TEXT, " +
                "tongthanhtoan REAL, " +
                "ngaydathang DATETIME DEFAULT CURRENT_TIMESTAMP);");

        gioHangManager = GioHangManager.getInstance();
        orderManager = new OrderManager(getActivity());

        List<GioHang> gioHangList = gioHangManager.getGioHangList();
        adapter = new GioHangAdapter(getActivity(), gioHangList, txtTongTien);
        listView.setAdapter(adapter);

        txtTongTien.setText(String.valueOf(gioHangManager.getTongTien()));

        thanhtoan.setOnClickListener(v -> showPaymentDialog());

        return rootView;
    }

    private void showPaymentDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_thong_tin_thanh_toan);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        EditText edtTenKh = dialog.findViewById(R.id.tenkh);
        EditText edtDiaChi = dialog.findViewById(R.id.diachi);
        EditText edtSdt = dialog.findViewById(R.id.sdt);
        Button btnLuu = dialog.findViewById(R.id.btnxacnhandathang);
        TextView tvTongTien = dialog.findViewById(R.id.tienthanhtoan);

        String tongTien = txtTongTien.getText().toString();
        tvTongTien.setText(tongTien);

        btnLuu.setOnClickListener(v -> {
            String tenKh = edtTenKh.getText().toString().trim();
            String diaChi = edtDiaChi.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();

            if (tenKh.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                float tongThanhToan;
                try {
                    tongThanhToan = Float.parseFloat(tongTien.replace(",", ""));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Có lỗi xảy ra với tổng tiền!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (orderManager != null) {
                    long orderId = orderManager.addOrder(tenKh, diaChi, sdt, tongThanhToan);
                    if (orderId > 0) {
                        List<GioHang> gioHangList = gioHangManager.getGioHangList();
                        for (GioHang item : gioHangList) {
                            String masp = item.getSanPham().getMasp();
                            int soluong = item.getSoLuong();
                            float dongia = item.getSanPham().getDongia();
                            byte[] anhByteArray = item.getSanPham().getAnh();
                            orderManager.addOrderDetails((int) orderId, masp, soluong, dongia, anhByteArray);
                        }

                        Toast.makeText(getActivity(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        gioHangManager.clearGioHang();
                        txtTongTien.setText("0");

                        adapter.notifyDataSetChanged();

                        // Sử dụng FragmentTransaction để chuyển về TrangchuNgdungFragment
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new TrangchuNgdungFragment());
                        transaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Không thể xử lý đơn hàng, hãy thử lại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
