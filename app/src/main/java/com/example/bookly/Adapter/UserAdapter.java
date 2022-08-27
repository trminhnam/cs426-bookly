package com.example.bookly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.User;
import com.example.bookly.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<User> userList;


    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_user, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user = userList.get(position);
        Picasso.get()
                .load(user.getProfileImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.userImageIv);
        holder.userNameTv.setText(user.getName());
        holder.userAboutTv.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView userImageIv;
        TextView userNameTv, userAboutTv;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userImageIv = itemView.findViewById(R.id.userImageIv);
            userNameTv = itemView.findViewById(R.id.userNameTv);
            userAboutTv = itemView.findViewById(R.id.userAboutTv);
        }
    }
}
