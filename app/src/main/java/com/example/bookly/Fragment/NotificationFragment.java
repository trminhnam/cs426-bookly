package com.example.bookly.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bookly.Adapter.ViewPagerAdapter;
import com.example.bookly.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationFragment extends Fragment {
    private static NotificationFragment instance;

    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter _adapter;


    public static NotificationFragment getInstance() {
        if (instance == null) {
            instance = new NotificationFragment();
        }
        return instance;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        this._adapter = new ViewPagerAdapter(getChildFragmentManager());
        this._adapter.add(new NotiDetailFragment());


        viewPager.setAdapter(this._adapter);


        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}