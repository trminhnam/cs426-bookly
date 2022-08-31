package com.example.bookly.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.bookly.Adapter.PostAdapter;
import com.example.bookly.Adapter.StoryAdapter;
import com.example.bookly.Model.Post;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.Model.UserStory;
import com.example.bookly.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static HomeFragment instance;

    RecyclerView storyRv;
    ShimmerRecyclerView dashboardRv;
    ArrayList<StoryModel> storyList;
    ArrayList<Post> postList;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    ImageButton addStoryImage;
    FloatingActionButton fab;
    NestedScrollView nestedScrollView;
    ActivityResultLauncher<String> galleryLauncher;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    StoryAdapter.addStoryListener addStoryCallback = (v) -> {
        addStoryImage = v;
        galleryLauncher.launch("image/*");
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(getString(R.string.FirebaseDatabase));
        storage = FirebaseStorage.getInstance(getString(R.string.FirebaseStorage));

        dialog = new ProgressDialog(getContext());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_modern, container, false);

        database = FirebaseDatabase.getInstance(getString(R.string.FirebaseDatabase));
        storage = FirebaseStorage.getInstance(getString(R.string.FirebaseStorage));
        auth = FirebaseAuth.getInstance();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.StoryUploading));
        dialog.setMessage(getString(R.string.PleaseWait));
        dialog.setCancelable(false);

        // Add story recycle view
        storyRv = view.findViewById(R.id.storyRecycleView);
        storyList = new ArrayList<>();

        storyList.add(new StoryModel());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result == null) {
                        return;
                    }
                    addStoryImage.setImageURI(result);
                    dialog.show();

                    final StorageReference reference = storage.getReference()
                            .child("story")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child(String.valueOf(new Date().getTime()));
                    reference.putFile(result).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                        StoryModel story = new StoryModel();
                        story.setStoryAt(new Date().getTime());

                        database.getReference()
                                .child("stories")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("postedBy")
                                .setValue(story.getStoryAt())
                                .addOnSuccessListener(unused -> {
                                    UserStory userStory = new UserStory(uri.toString(), story.getStoryAt());

                                    database.getReference()
                                            .child(getString(R.string.child_stories))
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(getString(R.string.child_userStories))
                                            .push()
                                            .setValue(userStory).addOnSuccessListener(unused1 -> dialog.dismiss());
                                });
                        }));
            });

        StoryAdapter adapter = new StoryAdapter(storyList, getContext(), addStoryCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(true);
        storyRv.setAdapter(adapter);

        database.getReference()
                .child(getString(R.string.child_stories)).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            // keep only first item
                            if (storyList.size() > 1) {
                                storyList.subList(1, storyList.size()).clear();
                            }
                            for (DataSnapshot storySnapshot :snapshot.getChildren())
                            {
                                StoryModel story = new StoryModel();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child(getString(R.string.child_postedBy)).getValue(Long.class));

                                ArrayList<UserStory> userStories = new ArrayList<>();
                                for (DataSnapshot userStorySnapshot : storySnapshot.child(getString(R.string.child_userStories)).getChildren())
                                {
                                    UserStory userStory = userStorySnapshot.getValue(UserStory.class);
                                    userStories.add(userStory);
                                }
                                story.setStories(userStories);
                                storyList.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.smoothScrollTo(0,0, 250);
                fab.setVisibility(View.GONE);
            }
        });
        nestedScrollView = view.findViewById(R.id.homeNsv);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scrollOffset = scrollY - oldScrollY;
//                Log.d("nest scroll", "onScrollChange: " + scrollOffset);
                if (scrollOffset > 0) { // scrolling down
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            fab.setVisibility(View.GONE);
//                        }
//                    }, 2000); // delay of 2 seconds before hiding the fab
                    fab.setVisibility(View.GONE);
                } else if (scrollOffset < 0) { // scrolling up
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        // Add dashboard recycle view
        dashboardRv = view.findViewById(R.id.dashboardRv);
        dashboardRv.showShimmerAdapter();

        postList = new ArrayList<>();
        PostAdapter postAdapter = new PostAdapter(postList, getContext());

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        dashboardRv.setLayoutManager(linearLayoutManager1);
        dashboardRv.setNestedScrollingEnabled(true);

        database.getReference().child(getString(R.string.child_Posts))
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int index = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            assert post != null;

                            // old method
//                          post.setPostID(dataSnapshot.getKey());
//                          postList.add(post);

                            // update only post changed
                            if (index >= postList.size()){
                                post.setPostID(dataSnapshot.getKey());
                                postList.add(0, post);
                                postAdapter.notifyItemInserted(0);
                            }
                            else if(!post.isEqual(postList.get(index))){
                                post.setPostID(dataSnapshot.getKey());
                                postList.set(postList.size()-1-index, post);
                                postAdapter.notifyItemChanged(postList.size()-1-index);
                            }
                            index += 1;
                        }

//                        Collections.sort(postList, new Comparator<Post>() {
//                            @Override
//                            public int compare(Post o1, Post o2) {
//                                return Long.compare(o2.getPostedAt(), o1.getPostedAt());
//                            }
//                        });

//                        dashboardRv.setAdapter(postAdapter);
//                        dashboardRv.hideShimmerAdapter();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        dashboardRv.setAdapter(postAdapter);
        dashboardRv.hideShimmerAdapter();
        return view;
    }
}
