package com.example.tuan17;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tuan17.Adapter.NhomSanPhamAdapter;
import com.example.tuan17.Adapter.SanPhamAdapter;
import com.example.tuan17.Db.Database;
import com.example.tuan17.Model.NhomSanPham;
import com.example.tuan17.Model.SanPham;

import java.util.ArrayList;

public class TrangchuNgdungFragment extends Fragment {
    GridView grv2;
    GridView grv1;
    ArrayList<SanPham> mangSPgrv1; // Danh sách cho GridView
    ArrayList<NhomSanPham> mangNSPgrv2; // Danh sách cho ListView
    NhomSanPhamAdapter adapterGrv2;
    SanPhamAdapter adapterGrv1;
    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_trangchu_ngdung, container, false);

        EditText timkiem = rootView.findViewById(R.id.timkiem);
        TextView textTendn = rootView.findViewById(R.id.tendn); // TextView hiển thị tên đăng nhập
        grv2 = rootView.findViewById(R.id.grv2);
        grv1 = rootView.findViewById(R.id.grv1);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", requireContext().MODE_PRIVATE);
        String tendn = sharedPreferences.getString("tendn", null);

        // Kiểm tra tên đăng nhập
        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(getActivity(), Login_Activity.class);
            startActivity(intent);
            requireActivity().finish(); // Kết thúc activity nếu chưa đăng nhập
            return rootView;
        }

        grv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng nhóm sản phẩm từ adapter
                NhomSanPham nhomSanPham = mangNSPgrv2.get(position);

                if (nhomSanPham != null) {
                    // Chuyển đến DanhMucSanPham_Activity và truyền mã của nhóm sản phẩm
                    Intent intent = new Intent(getActivity(), DanhMucSanPham_Activity.class);
                    intent.putExtra("nhomSpId", nhomSanPham.getMa()); // Gửi mã nhóm sản phẩm
                    startActivity(intent);
                }
            }
        });

        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimKiemSanPham_Activity.class);
                intent.putExtra("tendn", tendn); // Sử dụng biến tendn đã được lấy từ SharedPreferences
                startActivity(intent);
            }
        });

        // Khởi tạo danh sách và adapter
        mangNSPgrv2 = new ArrayList<>();
        mangSPgrv1 = new ArrayList<>();
        adapterGrv2 = new NhomSanPhamAdapter(getContext(), mangNSPgrv2, false);
        adapterGrv1 = new SanPhamAdapter(getContext(), mangSPgrv1, false);
        grv2.setAdapter(adapterGrv2);
        grv1.setAdapter(adapterGrv1);

        database = new Database(getContext(), "banhang.db", null, 1);

        Loaddulieubacsigridview2();
        Loaddulieubacsigridview1();

        return rootView;
    }

    private void Loaddulieubacsigridview2() {
        Cursor dataCongViec = database.GetData("SELECT * from nhomsanpham order by random() limit 10 ");
        mangNSPgrv2.clear();

        while (dataCongViec.moveToNext()) {
            String ma = dataCongViec.getString(0);
            String ten = dataCongViec.getString(1);
            byte[] blob = dataCongViec.getBlob(2); // Lấy mảng byte từ cột chứa ảnh
            mangNSPgrv2.add(new NhomSanPham(ma, ten, blob));
        }

        adapterGrv2.notifyDataSetChanged(); // Cập nhật adapter
    }

    private void Loaddulieubacsigridview1() {
        Cursor cursor = database.GetData("SELECT * FROM sanpham order by random() limit 8");
        mangSPgrv1.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String masp = cursor.getString(0);
                String tensp = cursor.getString(1);
                float dongia = cursor.getFloat(2); // Giữ nguyên là float
                String mota = cursor.getString(3);
                String ghichu = cursor.getString(4);
                int soluongkho = cursor.getInt(5); // Giữ nguyên là int
                String maso = cursor.getString(6);
                byte[] blob = cursor.getBlob(7);
                mangSPgrv1.add(new SanPham(masp, tensp, dongia, mota, ghichu, soluongkho, maso, blob));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getActivity(), "Null load dữ liệu", Toast.LENGTH_SHORT).show();
        }

        adapterGrv1.notifyDataSetChanged();
    }
}
