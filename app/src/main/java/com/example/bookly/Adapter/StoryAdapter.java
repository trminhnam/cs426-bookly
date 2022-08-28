package com.example.bookly.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        void onAddStoryClick(RoundedImageView addStoryImage);
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
            View view = LayoutInflater.from(context).inflate(R.layout.rv_item_add_story, parent, false);
            return new viewHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.rv_item_story, parent, false);
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
            holder.storyImg.setImageResource(model.getStory());
            holder.profile.setImageResource(model.getProfile());
            holder.storyType.setImageResource(model.getStoryType());
            holder.name.setText(model.getName());
            holder.status.setPortionsCount(model.getStories().size());

            UserStory latestStory = model.getStories().get(model.getStories().size() - 1);
            Picasso.get()
                    .load(latestStory.getImage())
                    .into(holder.storyImg);

            FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference()
                    .child("Users")
                    .child(model.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(holder.profile);
                            holder.name.setText(user.getName());

                            holder.storyImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ArrayList<MyStory> myStories = new ArrayList<>();
                                    for (UserStory stories : model.getStories()){
                                        myStories.add( new MyStory(stories.getImage()));
                                        new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                                .setStoriesList(myStories) // Required
                                                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                                .setTitleText(user.getName()) // Default is Hidden
                                                .setSubtitleText("") // Default is Hidden
                                                .setTitleLogoUrl(user.getProfileImage()) // Default is Hidden
                                                .setStoryClickListeners(new StoryClickListeners() {
                                                    @Override
                                                    public void onDescriptionClickListener(int position) {
                                                        //your action
                                                    }

                                                    @Override
                                                    public void onTitleIconClickListener(int position) {
                                                        //your action
                                                    }
                                                }) // Optional Listeners
                                                .build() // Must be called before calling show method
                                                .show();
                                    }
                                }
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
        ImageView storyImg, profile, storyType;
        TextView name;
        CircularStatusView status;

        // for add story
        RoundedImageView addStoryImage;

        public viewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            // set view holder link to view
            if (viewType == TYPE_ADD_STORY) {
                // set onclick listener for add story button
                addStoryImage = itemView.findViewById(R.id.story_image);
            } else {
                storyImg = itemView.findViewById(R.id.story_image);
                profile = itemView.findViewById(R.id.profile_image);
                storyType = itemView.findViewById(R.id.story_type);
                name = itemView.findViewById(R.id.name);
                status = itemView.findViewById(R.id.circular_status_view);
            }
        }
    }
}















