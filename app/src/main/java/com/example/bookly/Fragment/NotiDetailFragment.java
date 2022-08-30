package com.example.bookly.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookly.Adapter.NotificationAdapter;
import com.example.bookly.Model.Notification;
import com.example.bookly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class NotiDetailFragment extends Fragment {

    private static final String NOTIFICATION_LIST_KEY = "notification_list_key";
    RecyclerView notificationRv;
    ArrayList<Notification> notificationList;
    NotificationAdapter notificationAdapter = null;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    public NotiDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noti_detail, container, false);

        notificationRv = view.findViewById(R.id.notificationRv);

        notificationList = new ArrayList<>();
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));
//        notificationList.add(new Notification(R.drawable.resource_default, "Tran Minh Nam likes your book", "just now"));

        notificationAdapter = new NotificationAdapter(notificationList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        notificationRv.setLayoutManager(layoutManager);
        notificationRv.setAdapter(notificationAdapter);

        database.getReference()
                .child("Notifications")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notificationList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationID(dataSnapshot.getKey());
                            notificationList.add(notification);
                        }
                        notificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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