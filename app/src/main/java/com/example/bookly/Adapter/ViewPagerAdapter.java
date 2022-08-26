package com.example.bookly.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bookly.Fragment.NotiDetailFragment;
import com.example.bookly.Fragment.RequestFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> _fragments;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this._fragments = new ArrayList<Fragment>();
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new NotiDetailFragment();
            case 1: return new RequestFragment();
            default: return new NotiDetailFragment();
        }
    }

    public void add(Fragment fragment) {
        this._fragments.add(fragment);
    }


    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position){
        String title = null;

        if (position == 0){
            title = "Detail";
        } else if (position == 1){
            title = "Request";
        }

        return title;
    }

}
