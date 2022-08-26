package com.example.bookly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.NotificationModel;
import com.example.bookly.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    ArrayList<NotificationModel> notificationList;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_notification, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        NotificationModel notificationModel = notificationList.get(position);

        holder.profileIv.setImageResource(notificationModel.getProfile());
        holder.notificationTv.setText(notificationModel.getNotification());
        holder.timeTv.setText(notificationModel.getTime());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView notificationTv, timeTv;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            profileIv = itemView.findViewById(R.id.profile_image);
            notificationTv = itemView.findViewById(R.id.notificationTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
