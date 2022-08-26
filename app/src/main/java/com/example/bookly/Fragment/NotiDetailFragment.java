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

    RecyclerView notificationRv;
    ArrayList<NotificationModel> notificationList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotiDetailFragment() {
        // Required empty public constructor
    }


    public static NotiDetailFragment newInstance(String param1, String param2) {
        NotiDetailFragment fragment = new NotiDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRv.setLayoutManager(layoutManager);
        notificationRv.setAdapter(notificationAdapter);

        return view;
    }
}