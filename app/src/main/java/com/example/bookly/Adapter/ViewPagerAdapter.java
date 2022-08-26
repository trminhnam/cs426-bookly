package com.example.bookly.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bookly.Fragment.NotiDetailFragment;
import com.example.bookly.Fragment.RequestFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public static NotiDetailFragment notiDetailFragment = null;
    public static RequestFragment requestFragment = null;

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (notiDetailFragment == null) {
                    notiDetailFragment = new NotiDetailFragment();
                }
                return notiDetailFragment;
            case 1:
                if (requestFragment == null) {
                    requestFragment = new RequestFragment();
                }
                return requestFragment;
            default:
                if (notiDetailFragment == null) {
                    notiDetailFragment = new NotiDetailFragment();
                }
                return notiDetailFragment;
        }
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
