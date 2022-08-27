package com.example.bookly.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Adapter.UserAdapter;
import com.example.bookly.Model.User;
import com.example.bookly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<User> userList = new ArrayList<>();

    RecyclerView usersRv;;


    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(
                "https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        usersRv = view.findViewById(R.id.usersRv);

        UserAdapter adapter = new UserAdapter(getContext(), userList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        usersRv.setLayoutManager(layoutManager);
        usersRv.setAdapter(adapter);

        database.getReference()
                .child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setUserID(dataSnapshot.getKey());
                            userList.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}