package com.example.bookly.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.bookly.R;
import com.example.bookly.Adapter.FriendAdapter;
import com.example.bookly.Model.FriendModel;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<FriendModel> list;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.rv_friend);

        list = new ArrayList<>();
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));
        list.add(new FriendModel(R.drawable.cartoon_penguin_dressed));

        FriendAdapter adapter = new FriendAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}