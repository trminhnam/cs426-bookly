package com.example.bookly.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Adapter.DashboardAdapter;
import com.example.bookly.Adapter.StoryAdapter;
import com.example.bookly.Model.DashboardModel;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView storyRv, dashboardRv;
    ArrayList<StoryModel> storyList;
    ArrayList<DashboardModel> dashboardList;


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

        // Add story recycle view
        storyRv = view.findViewById(R.id.storyRecycleView);
        storyList = new ArrayList<>();
        addTestStoryData();

        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(true);
        storyRv.setAdapter(adapter);

        // Add dashboard recycle view
        dashboardRv = view.findViewById(R.id.dashboardRv);
        dashboardList = new ArrayList<>();
        addTestDashboardData();
        DashboardAdapter dashboardAdapter = new DashboardAdapter(dashboardList, getContext());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        dashboardRv.setLayoutManager(linearLayoutManager1);
        dashboardRv.setNestedScrollingEnabled(true);
        dashboardRv.setAdapter(dashboardAdapter);


        return view;
    }

    private void addTestDashboardData() {
        dashboardList.add(new DashboardModel(
                R.drawable.ic_baseline_person_24,
                R.drawable.resource_default,
                R.drawable.ic_baseline_bookmark_border_24,
                "User 1",
                "Developer",
                "123",
                "456",
                "789"
        ));

        dashboardList.add(new DashboardModel(
                R.drawable.ic_baseline_person_24,
                R.drawable.resource_default,
                R.drawable.ic_baseline_bookmark_border_24,
                "User 2",
                "Developer",
                "123",
                "456",
                "789"
        ));

        dashboardList.add(new DashboardModel(
                R.drawable.ic_baseline_person_24,
                R.drawable.resource_default,
                R.drawable.ic_baseline_bookmark_border_24,
                "User 3",
                "Developer",
                "123",
                "456",
                "789"
        ));

        dashboardList.add(new DashboardModel(
                R.drawable.ic_baseline_person_24,
                R.drawable.resource_default,
                R.drawable.ic_baseline_bookmark_border_24,
                "User 4",
                "Developer",
                "123",
                "456",
                "789"
        ));

        dashboardList.add(new DashboardModel(
                R.drawable.ic_baseline_person_24,
                R.drawable.resource_default,
                R.drawable.ic_baseline_bookmark_border_24,
                "User 5",
                "Developer",
                "123",
                "456",
                "789"
        ));

    }

    private void addTestStoryData() {
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
    }
}


























