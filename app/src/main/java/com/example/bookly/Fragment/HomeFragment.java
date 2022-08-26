package com.example.bookly.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Adapter.StoryAdapter;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView storyRv;
    ArrayList<StoryModel> storyList;


    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        storyRv = view.findViewById(R.id.storyRecycleView);

        storyList = new ArrayList<>();
        storyList.add(new StoryModel(
                R.drawable.resource_default,
                R.drawable.bg_gradient,
                R.drawable.ic_baseline_person_24,
                "User Name 1"));

        storyList.add(new StoryModel(
                R.drawable.resource_default,
                R.drawable.bg_gradient,
                R.drawable.ic_baseline_person_24,
                "User Name 2"));

        storyList.add(new StoryModel(
                R.drawable.resource_default,
                R.drawable.bg_gradient,
                R.drawable.ic_baseline_person_24,
                "User Name 3"));

        storyList.add(new StoryModel(
                R.drawable.resource_default,
                R.drawable.bg_gradient,
                R.drawable.ic_baseline_person_24,
                "User Name 4"));

        storyList.add(new StoryModel(
                R.drawable.resource_default,
                R.drawable.bg_gradient,
                R.drawable.ic_baseline_person_24,
                "User Name 5"));

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);

        storyRv.setAdapter(adapter);

        return view;
    }
}


























