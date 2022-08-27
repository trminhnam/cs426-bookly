package com.example.bookly.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        ImageView ivChangeCoverPhoto = view.findViewById(R.id.ivchangeCoverPhoto);
        ivChangeCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri uri = data.getData();
            ImageView ivCoverPhoto = getView().findViewById(R.id.dlivProfileFragment);
            ivCoverPhoto.setImageURI(uri);
        }
    }
}