package com.example.bookly.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookly.Adapter.RequestAdapter;
import com.example.bookly.Model.RequestModel;
import com.example.bookly.R;

import java.util.ArrayList;

public class RequestFragment extends Fragment {

    RecyclerView requestRv;
    ArrayList<RequestModel> requestList;
    RequestAdapter requestAdapter = null;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        requestRv = view.findViewById(R.id.requestRv);

        requestList = new ArrayList<>();
        requestList.add(new RequestModel(R.drawable.resource_default, "Love/Romance", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Science/Fiction", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Novel", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Comic", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Comic", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Self-help", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Self-help", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Science/Fiction", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Novel", "just now"));
        requestList.add(new RequestModel(R.drawable.resource_default, "Novel", "just now"));

        requestAdapter = new RequestAdapter(requestList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        requestRv.setLayoutManager(layoutManager);
        requestRv.setAdapter(requestAdapter);

        return view;
    }
}