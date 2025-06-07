package com.example.gamequeue.ui.adapter;

import com.example.gamequeue.ui.fragment.FormOneFragment;
import com.example.gamequeue.ui.fragment.FormTwoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ReservationPagerAdapter extends FragmentPagerAdapter {
    public ReservationPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FormOneFragment();
            case 1:
                return new FormTwoFragment();
            default:
                return new FormTwoFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
