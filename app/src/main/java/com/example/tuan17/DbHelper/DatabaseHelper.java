package com.example.tuan17.DbHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tuan17.Model.ChiTietDonHang;
import com.example.tuan17.Model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "banhang.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Dathang
        db.execSQL("CREATE TABLE IF NOT EXISTS Dathang (" +
                "id_dathang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenkh TEXT, " +
                "diachi TEXT, " +
                "sdt TEXT, " +
                "tongthanhtoan REAL);");
        // Tạo bảng Chitietdonhang
        db.execSQL("CREATE TABLE IF NOT EXISTS Chitietdonhang (" +
                "id_chitiet INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_dathang INTEGER, " +
                "masp INTEGER, " +
                "soluong INTEGER, " +
                "dongia REAL, " +
                "anh TEXT, " +
                "FOREIGN KEY(id_dathang) REFERENCES Dathang(id_dathang));");

        Log.d("DatabaseHelper", "Tables created successfully");
        // Tạo bảng sanpham
        db.execSQL("CREATE TABLE IF NOT EXISTS sanpham (" +
                "masp INTEGER PRIMARY KEY, " +
                "tensp TEXT, " +
                "dongia REAL, " +
                "mota TEXT, " +
                "ghichu TEXT, " +
                "soluongkho INTEGER, " +
                "maso TEXT, " +
                "anh BLOB);");

        Log.d("DatabaseHelper", "Tables created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Chitietdonhang");
        db.execSQL("DROP TABLE IF EXISTS sanpham");
        onCreate(db);
        Log.d("DatabaseHelper", "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    // Lấy danh sách ChiTietDonHang theo orderId
    public List<ChiTietDonHang> getChiTietByOrderId(int orderId) {
        List<ChiTietDonHang> chiTietList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT Chitietdonhang.* FROM Chitietdonhang INNER JOIN sanpham ON sanpham.masp = Chitietdonhang.masp INNER JOIN Dathang ON Dathang.id_dathang = Chitietdonhang.id_dathang WHERE Dathang.id_dathang = ?", new String[]{String.valueOf(orderId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex("id_chitiet");
                int dathangIdIndex = cursor.getColumnIndex("id_dathang");
                int maSpIndex = cursor.getColumnIndex("masp");
                int soLuongIndex = cursor.getColumnIndex("soluong");
                int donGiaIndex = cursor.getColumnIndex("dongia");
                int anhIndex = cursor.getColumnIndex("anh");

                if (idIndex != -1 && maSpIndex != -1 && soLuongIndex != -1 && donGiaIndex != -1 && anhIndex != -1 && dathangIdIndex != -1) {
                    ChiTietDonHang chiTiet = new ChiTietDonHang(
                            cursor.getInt(idIndex),
                            cursor.getInt(dathangIdIndex),
                            cursor.getInt(maSpIndex),
                            cursor.getInt(soLuongIndex),
                            cursor.getFloat(donGiaIndex),
                            cursor.getBlob(anhIndex)
                    );
                    chiTietList.add(chiTiet);
                } else {
                    Log.w("Database", "One of the column indexes is -1. Check if the column exists in the database.");
                }
            }
            cursor.close();
        }
        return chiTietList;
    }

    // Lấy tất cả các ChiTietDonHang
    public List<ChiTietDonHang> getAllChiTietDonHang() {
        List<ChiTietDonHang> chiTietList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Chitietdonhang", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idChitietIndex = cursor.getColumnIndex("id_chitiet");
                int idDathangIndex = cursor.getColumnIndex("id_dathang");
                int maspIndex = cursor.getColumnIndex("masp");
                int soluongIndex = cursor.getColumnIndex("soluong");
                int dongiaIndex = cursor.getColumnIndex("dongia");
                int anhIndex = cursor.getColumnIndex("anh");

                if (idChitietIndex != -1 && idDathangIndex != -1 && maspIndex != -1 && soluongIndex != -1 && dongiaIndex != -1 && anhIndex != -1) {
                    ChiTietDonHang chiTiet = new ChiTietDonHang(
                            cursor.getInt(idChitietIndex),
                            cursor.getInt(idDathangIndex),
                            cursor.getInt(maspIndex),
                            cursor.getInt(soluongIndex),
                            cursor.getFloat(dongiaIndex),
                            cursor.getBlob(anhIndex)
                    );
                    chiTietList.add(chiTiet);
                    Log.d("Chitietdonhang", "ID: " + chiTiet.getId_chitiet() + ", Số lượng: " + chiTiet.getSoLuong());
                } else {
                    Log.w("Chitietdonhang", "One of the column indexes is -1. Check if the column exists in the database.");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return chiTietList;
    }

    // Lấy tên sản phẩm theo mã sản phẩm
    public String getTenSanPhamByMaSp(int masp) {
        String tensp = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tensp FROM sanpham WHERE masp = ?", new String[]{String.valueOf(masp)});
        if (cursor != null && cursor.moveToFirst()) {
            int tenspIndex = cursor.getColumnIndex("tensp");
            if (tenspIndex != -1) {
                tensp = cursor.getString(tenspIndex);
            } else {
                Log.e("Database Error", "Column 'tensp' not found.");
            }
            cursor.close();
        }
        return tensp;
    }

    // Lấy danh sách sản phẩm theo nhomSpId
    public List<SanPham> getProductsByNhomSpId(String nhomSpId) {
        List<SanPham> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM sanpham WHERE maso = ?", new String[]{nhomSpId});
        if (cursor.moveToFirst()) {
            do {
                String masp = cursor.getString(cursor.getColumnIndexOrThrow("masp"));
                String tensp = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
                Float dongia = cursor.getFloat(cursor.getColumnIndexOrThrow("dongia"));
                String mota = cursor.getString(cursor.getColumnIndexOrThrow("mota"));
                String ghichu = cursor.getString(cursor.getColumnIndexOrThrow("ghichu"));
                int soluongkho = cursor.getInt(cursor.getColumnIndexOrThrow("soluongkho"));
                String mansp = cursor.getString(cursor.getColumnIndexOrThrow("maso"));
                byte[] anh = cursor.getBlob(cursor.getColumnIndexOrThrow("anh"));

                SanPham sanPham = new SanPham(masp, tensp, dongia, mota, ghichu, soluongkho, mansp, anh);
                productList.add(sanPham);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Tìm kiếm sản phẩm theo tên
    public ArrayList<SanPham> searchSanPhamByName(String name) {
        ArrayList<SanPham> sanPhamList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM sanpham WHERE tensp LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + name + "%"});

        if (cursor.moveToFirst()) {
            do {
                int maspIndex = cursor.getColumnIndex("masp");
                int tenspIndex = cursor.getColumnIndex("tensp");
                int dongiaIndex = cursor.getColumnIndex("dongia");
                int motaIndex = cursor.getColumnIndex("mota");
                int ghichuIndex = cursor.getColumnIndex("ghichu");
                int soluongkhoIndex = cursor.getColumnIndex("soluongkho");
                int manhomsanphamIndex = cursor.getColumnIndex("maso");
                int anhIndex = cursor.getColumnIndex("anh");

                if (maspIndex != -1 && tenspIndex != -1) {
                    String masp = cursor.getString(maspIndex);
                    String tensp = cursor.getString(tenspIndex);
                    float dongia = (dongiaIndex != -1) ? cursor.getFloat(dongiaIndex) : 0.0f;
                    String mota = (motaIndex != -1) ? cursor.getString(motaIndex) : "";
                    String ghichu = (ghichuIndex != -1) ? cursor.getString(ghichuIndex) : "";
                    int soluongkho = (soluongkhoIndex != -1) ? cursor.getInt(soluongkhoIndex) : 0;
                    String manhomsanpham = (manhomsanphamIndex != -1) ? cursor.getString(manhomsanphamIndex) : "";
                    byte[] anh = (anhIndex != -1) ? cursor.getBlob(anhIndex) : null;

                    SanPham sanPham = new SanPham(masp, tensp, dongia, mota, ghichu, soluongkho, manhomsanpham, anh);
                    sanPhamList.add(sanPham);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sanPhamList; // Trả về danh sách sản phẩm tìm kiếm
    }

}

