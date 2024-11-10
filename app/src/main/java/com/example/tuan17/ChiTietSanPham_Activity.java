package com.example.tuan17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuan17.Model.ChiTietSanPham;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChiTietSanPham_Activity extends AppCompatActivity {

    String masp, tendn;
    Button btndathang, btnaddcart;
    private ChiTietSanPham chiTietSanPham;
    private GioHangManager gioHangManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        btndathang = findViewById(R.id.btndathang);
        btnaddcart = findViewById(R.id.btnaddcart);

        TextView tensp = findViewById(R.id.tensp);
        ImageView imgsp = findViewById(R.id.imgsp);
        TextView dongia = findViewById(R.id.dongia);
        TextView mota = findViewById(R.id.mota);
        TextView soluongkho = findViewById(R.id.soluongkho);
        TextView textTendn = findViewById(R.id.tendn);

        SharedPreferences sharedPre = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String tendn = sharedPre.getString("tendn", null);

        if (tendn != null) {
            textTendn.setText(tendn);
        } else {
            Intent intent = new Intent(ChiTietSanPham_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();
            return;
        }

        Intent intent = getIntent();
        chiTietSanPham = intent.getParcelableExtra("chitietsanpham");

        if (chiTietSanPham != null) {
            masp = chiTietSanPham.getMasp();
            tensp.setText(chiTietSanPham.getTensp());
            dongia.setText(chiTietSanPham.getDongia() != null ? String.valueOf(chiTietSanPham.getDongia()) : "Không có dữ liệu");
            mota.setText(chiTietSanPham.getMota() != null ? chiTietSanPham.getMota() : "Không có dữ liệu");
            soluongkho.setText(String.valueOf(chiTietSanPham.getSoluongkho()));
            byte[] anhByteArray = chiTietSanPham.getAnh();
            if (anhByteArray != null && anhByteArray.length > 0) {
                Bitmap imganhbs = BitmapFactory.decodeByteArray(anhByteArray, 0, anhByteArray.length);
                imgsp.setImageBitmap(imganhbs);
            } else {
                imgsp.setImageResource(R.drawable.vest);
            }
        } else {
            tensp.setText("Không có dữ liệu");
        }

        gioHangManager = GioHangManager.getInstance();

        btnaddcart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (!isLoggedIn) {
                Intent loginIntent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(loginIntent);
            } else {
                gioHangManager.addItem(chiTietSanPham);
                Toast.makeText(ChiTietSanPham_Activity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        btndathang.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (!isLoggedIn) {
                Intent loginIntent = new Intent(getApplicationContext(), Login_Activity.class);
                startActivity(loginIntent);
            } else {
                gioHangManager.addItem(chiTietSanPham); // Gọi phương thức addItem
                String imagePath = saveImageToInternalStorage(imgsp.getDrawable()); // Lưu ảnh vào bộ nhớ trong và lấy đường dẫn

                Intent intent1 = new Intent(ChiTietSanPham_Activity.this, GioHang_Activity.class);
                intent1.putExtra("imagePath", imagePath); // Truyền đường dẫn ảnh vào GioHang_Activity
                startActivity(intent1);
            }
        });
    }

    // Hàm lưu ảnh vào bộ nhớ trong và trả về đường dẫn ảnh
    private String saveImageToInternalStorage(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File path = new File(directory, "image.jpg");

        try (FileOutputStream fos = new FileOutputStream(path)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // Lưu ảnh dưới dạng JPEG
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path.getAbsolutePath(); // Trả về đường dẫn của ảnh
    }
}
