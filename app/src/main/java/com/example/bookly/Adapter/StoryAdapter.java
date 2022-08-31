package com.example.bookly.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.circularstatusview.CircularStatusView;
import com.example.bookly.Model.StoryModel;
import com.example.bookly.Model.User;
import com.example.bookly.Model.UserStory;
import com.example.bookly.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    ArrayList<StoryModel> storyList;
    Context context;
    private static final int TYPE_ADD_STORY = 0;
    private static final int TYPE_STORY = 1;
    private addStoryListener addStoryCallback;

    public interface addStoryListener {
        void onAddStoryClick(ImageButton addStoryImage);
    }

    public StoryAdapter(ArrayList<StoryModel> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
    }

    public StoryAdapter(ArrayList<StoryModel> storyList, Context context, addStoryListener addStoryCallback) {
        this.storyList = storyList;
        this.context = context;
        this.addStoryCallback = addStoryCallback;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD_STORY) {
            View view = LayoutInflater.from(context).inflate(R.layout.rv_item_add_story_modern, parent, false);
            return new viewHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.rv_item_story_modern, parent, false);
            return new viewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // Set view holder data
        if (holder.getItemViewType() == TYPE_ADD_STORY) {
            holder.addStoryImage.setOnClickListener(v -> {
                if (addStoryCallback != null) {
                    addStoryCallback.onAddStoryClick(holder.addStoryImage);
                }
            });
        } else {
            StoryModel model = storyList.get(position);
            holder.profile.setImageResource(model.getProfile());
            holder.status.setPortionsCount(model.getStories().size());

//            if (model.getStories().size() > 0) {
//                UserStory latestStory = model.getStories().get(model.getStories().size() - 1);
//                Picasso.get()
//                        .load(latestStory.getImage())
//                        .into(holder.storyImg);
//            }

            FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference()
                    .child("Users")
                    .child(model.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            assert user != null;
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.cartoon_penguin_dressed)
                                    .into(holder.profile);

                            holder.profile.setOnClickListener(v -> {
                                ArrayList<MyStory> myStories = new ArrayList<>();
                                for (UserStory stories : model.getStories()){
                                    myStories.add( new MyStory(stories.getImage()));
                                }
                                new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                        .setStoriesList(myStories)
                                        .setStoryDuration(2000) // 2000 Millis (2 Seconds)
                                        .setTitleText(user.getName())
                                        .setSubtitleText("")
                                        .setTitleLogoUrl(user.getProfileImage())
//                                                .setStoryClickListeners(new StoryClickListeners() {
//                                                    @Override
//                                                    public void onDescriptionClickListener(int position) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onTitleIconClickListener(int position) {
//
//                                                    }
//                                                })
                                        .build() // Must be called before calling show method
                                        .show();
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)        // Add story item at the first position
            return TYPE_ADD_STORY;
        else
            return TYPE_STORY;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        // for story data
        ImageView  profile;
        CircularStatusView status;

        // for add story
        ImageButton addStoryImage;

        public viewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            // set view holder link to view
            if (viewType == TYPE_ADD_STORY) {
                // set onclick listener for add story button
                addStoryImage = itemView.findViewById(R.id.story_image_modern);
            } else {
                profile = itemView.findViewById(R.id.profile_image);
                status = itemView.findViewById(R.id.circular_status_view);
            }
        }
    }
}















