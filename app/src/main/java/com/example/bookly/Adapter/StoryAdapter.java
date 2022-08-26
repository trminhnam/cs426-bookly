package com.example.bookly.Adapter;

import android.content.Context;
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

    public StoryAdapter(ArrayList<StoryModel> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_story, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        StoryModel model = storyList.get(position);
        holder.storyImg.setImageResource(model.getStory());
        holder.profile.setImageResource(model.getProfile());
        holder.storyType.setImageResource(model.getStoryType());
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView storyImg, profile, storyType;
        TextView name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            storyImg = itemView.findViewById(R.id.story_image);
            profile = itemView.findViewById(R.id.profile_image);
            storyType = itemView.findViewById(R.id.story_type);
            name = itemView.findViewById(R.id.name);
        }
    }
}















