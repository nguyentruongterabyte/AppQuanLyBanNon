package com.example.appquanly.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentTab1();
            case 1:
                return new FragmentTab2();
            case 2:
                return new FragmentTab3();
            case 3:
                return new FragmentTab4();
            case 4:
                return new FragmentTab5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5; // Số lượng tab
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Tiêu đề của mỗi tab
        switch (position) {
            case 0:
                return "Tất cả";
            case 1:
                return "Chờ xác nhận";
            case 2:
                return "Đang giao";
            case 3:
                return "Đã giao";
            case 4:
                return "Đã hủy";
            default:
                return null;
        }
    }

}



