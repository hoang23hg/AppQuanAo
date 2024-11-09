package com.example.tuan17.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.tuan17.DonHangAdminFragment;
import com.example.tuan17.NhomsanphamAdminFragment;
import com.example.tuan17.SanphamAdminFragment;
import com.example.tuan17.TrangCaNhanAdminFragment;
import com.example.tuan17.TrangchuAdminFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrangchuAdminFragment();
            case 1:
               return new NhomsanphamAdminFragment();
            case 2:
                return new SanphamAdminFragment();
            case 3:
                return new DonHangAdminFragment();
            case 4:
                return new TrangCaNhanAdminFragment();
            default:
                return new TrangchuAdminFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
