package com.example.bookly.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookly.Adapter.NotificationAdapter;
import com.example.bookly.Model.NotificationModel;
import com.example.bookly.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class NotiDetailFragment extends Fragment {

    private static final String NOTIFICATION_LIST_KEY = "notification_list_key";
    RecyclerView notificationRv;
    ArrayList<NotificationModel> notificationList;
    NotificationAdapter notificationAdapter = null;

    public NotiDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noti_detail, container, false);

        notificationRv = view.findViewById(R.id.notificationRv);

        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
        notificationList.add(new NotificationModel(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));

        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRv.setLayoutManager(layoutManager);
        notificationRv.setAdapter(notificationAdapter);

        return view;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (notificationRv != null) {
//            notificationRv.setAdapter(null);
//        }
//        notificationAdapter = null;
//        notificationRv = null;
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (notificationRv != null) {
//            notificationRv.setAdapter(null);
//        }
//        notificationAdapter = null;
//        notificationRv = null;
//    }
}