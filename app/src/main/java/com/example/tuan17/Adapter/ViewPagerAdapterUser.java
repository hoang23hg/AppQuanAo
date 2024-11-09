package com.example.tuan17.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.tuan17.DonHangUserFragment;
import com.example.tuan17.GioHangFragment;
import com.example.tuan17.TimKiemSanPhamFragment;


import com.example.tuan17.TrangCaNhanNguoiDungFragment;
import com.example.tuan17.TrangchuNgdungFragment;

public class ViewPagerAdapterUser extends FragmentStateAdapter {
    public ViewPagerAdapterUser(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrangchuNgdungFragment();
            case 1:
                return new TimKiemSanPhamFragment();
            case 2:
                return new GioHangFragment();
            case 3:
                return new DonHangUserFragment();
            case 4:
                return new TrangCaNhanNguoiDungFragment();
            default:
                return new TrangchuNgdungFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
