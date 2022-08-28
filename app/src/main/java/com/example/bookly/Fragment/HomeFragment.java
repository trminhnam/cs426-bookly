package com.example.bookly.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Adapter.PostAdapter;
import com.example.bookly.Adapter.StoryAdapter;
import com.example.bookly.Model.Post;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView storyRv, dashboardRv;
    ArrayList<StoryModel> storyList;
    ArrayList<Post> postList;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");



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
        postList = new ArrayList<>();
//        addTestDashboardData();
        PostAdapter postAdapter = new PostAdapter(postList, getContext());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        dashboardRv.setLayoutManager(linearLayoutManager1);
        dashboardRv.setNestedScrollingEnabled(true);
        dashboardRv.setAdapter(postAdapter);

        database.getReference().child("Posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            postList.add(post);

                        }
                        postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }

//    private void addTestDashboardData() {
//        dashboardList.add(new Post(
//                R.drawable.ic_baseline_person_24,
//                R.drawable.resource_default,
//                R.drawable.ic_baseline_bookmark_border_24,
//                "User 1",
//                "Developer",
//                "123",
//                "456",
//                "789"
//        ));
//
//        dashboardList.add(new Post(
//                R.drawable.ic_baseline_person_24,
//                R.drawable.resource_default,
//                R.drawable.ic_baseline_bookmark_border_24,
//                "User 2",
//                "Developer",
//                "123",
//                "456",
//                "789"
//        ));
//
//        dashboardList.add(new Post(
//                R.drawable.ic_baseline_person_24,
//                R.drawable.resource_default,
//                R.drawable.ic_baseline_bookmark_border_24,
//                "User 3",
//                "Developer",
//                "123",
//                "456",
//                "789"
//        ));
//
//        dashboardList.add(new Post(
//                R.drawable.ic_baseline_person_24,
//                R.drawable.resource_default,
//                R.drawable.ic_baseline_bookmark_border_24,
//                "User 4",
//                "Developer",
//                "123",
//                "456",
//                "789"
//        ));
//
//        dashboardList.add(new Post(
//                R.drawable.ic_baseline_person_24,
//                R.drawable.resource_default,
//                R.drawable.ic_baseline_bookmark_border_24,
//                "User 5",
//                "Developer",
//                "123",
//                "456",
//                "789"
//        ));
//
//    }

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


























