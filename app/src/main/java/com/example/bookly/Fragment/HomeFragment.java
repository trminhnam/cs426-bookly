package com.example.bookly.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Adapter.DashboardAdapter;
import com.example.bookly.Adapter.StoryAdapter;
import com.example.bookly.Model.DashboardModel;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.Model.UserStory;
import com.example.bookly.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    RecyclerView storyRv, dashboardRv;
    ArrayList<StoryModel> storyList;
    ArrayList<DashboardModel> dashboardList;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    RoundedImageView addStoryImage;
    ActivityResultLauncher<String> galleryLauncher;

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
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
        auth = FirebaseAuth.getInstance();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        // Add story recycle view
        storyRv = view.findViewById(R.id.storyRecycleView);
        storyList = new ArrayList<>();

        storyList.add(new StoryModel());

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    addStoryImage.setImageURI(result);
                    dialog.show();

                    final StorageReference reference = storage.getReference()
                            .child("story")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(new Date().getTime() + "");
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    StoryModel story = new StoryModel();
                                    story.setStoryAt(new Date().getTime());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("postedBy")
                                            .setValue(story.getStoryAt())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    UserStory userStory = new UserStory(uri.toString(), story.getStoryAt());

                                                    database.getReference()
                                                            .child("stories")
                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("userStories")
                                                            .push()
                                                            .setValue(userStory).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                }
                                            });
                                    };
                            });
                        }
                    });
            });

        StoryAdapter adapter = new StoryAdapter(storyList, getContext(), addStoryCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(true);
        storyRv.setAdapter(adapter);

        database.getReference()
                .child("stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (int i = 1; i < storyList.size(); i++) {
                                storyList.remove(1);
                            }
                            for (DataSnapshot storySnapshot : snapshot.getChildren()){
                                StoryModel story = new StoryModel();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStory> stories = new ArrayList<>();
                                for (DataSnapshot userStorySnapshot : storySnapshot.child("userStories").getChildren()){
                                    UserStory userStory = userStorySnapshot.getValue(UserStory.class);
                                    stories.add(userStory);
                                }
                                story.setStories(stories);
                                storyList.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
}


























