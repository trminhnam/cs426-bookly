package com.example.bookly.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.StoryModel;
import com.example.bookly.R;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    ArrayList<StoryModel> storyList;
    Context context;
    private static final int TYPE_ADD_STORY = 0;
    private static final int TYPE_STORY = 1;

    public StoryAdapter(ArrayList<StoryModel> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
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
//            holder.addStory.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("TAG", "onClick: add story");
//                }
//            });
//          }
        } else {
            StoryModel model = storyList.get(position);
            Log.d("story", model.getName());
            holder.storyImg.setImageResource(model.getStory());
            holder.profile.setImageResource(model.getProfile());
            holder.storyType.setImageResource(model.getStoryType());
            holder.name.setText(model.getName());
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

        ImageView storyImg, profile, storyType;
        TextView name;
        public viewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            // set view holder link to view
            if (viewType == TYPE_ADD_STORY) {
                // set onclick listener for add story button
//                storyImg = itemView.findViewById(R.id.storyImg);
//                profile = itemView.findViewById(R.id.profile);
//                storyType = itemView.findViewById(R.id.storyType);
//                name = itemView.findViewById(R.id.name);
            } else {
                storyImg = itemView.findViewById(R.id.story_image);
                profile = itemView.findViewById(R.id.profile_image);
                storyType = itemView.findViewById(R.id.story_type);
                name = itemView.findViewById(R.id.name);
            }
        }
    }
}















