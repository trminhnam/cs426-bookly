package com.example.bookly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.FollowModel;
import com.example.bookly.Model.Notification;
import com.example.bookly.Model.User;
import com.example.bookly.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<User> userList;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
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
                .placeholder(R.drawable.cartoon_penguin_dressed)
                .into(holder.userImageIv);
        holder.userNameTv.setText(user.getName());
        holder.userAboutTv.setText(user.getAddress());

        // Check if the user is already followed
        FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference()
                .child("Users")
                .child(user.getUserID())
                .child("Followers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // already following, disable button
                            holder.followBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                            holder.followBtn.setText("Followed");
                            holder.followBtn.setTextColor(ContextCompat.getColor(context, R.color.gray));
                            holder.followBtn.setEnabled(false);
                        }
                        else{
                            // Follow button for each other user
                            holder.followBtn.setText("Follow");
                            holder.followBtn.setEnabled(true);
                            holder.followBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_btn_background));
                            holder.followBtn.setTextColor(ContextCompat.getColor(context, R.color.black));
                            holder.followBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FollowModel followModel = new FollowModel();
                                    followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    followModel.setFollowedAt(new Date().getTime());

                                    // add current user to the followed user's followers list
                                    FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app")
                                            .getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("Followers")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                            .setValue(followModel)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // increase number of followers for the followed user
                                                    FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app")
                                                            .getReference()
                                                            .child("Users")
                                                            .child(user.getUserID())
                                                            .child("followerCount")
                                                            .setValue(user.getFollowerCount() + 1) // increase follower count
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.followBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                                                                    holder.followBtn.setText("Followed");
                                                                    holder.followBtn.setTextColor(ContextCompat.getColor(context, R.color.gray));
                                                                    holder.followBtn.setEnabled(false);
                                                                    Toast.makeText(context, "Followed " + user.getName(), Toast.LENGTH_SHORT).show();

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(auth.getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("Follow");

                                                                    database.getReference()
                                                                            .child("Notifications")
                                                                            .child(user.getUserID())
                                                                            .push()
                                                                            .setValue(notification);


                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView userImageIv;
        TextView userNameTv, userAboutTv;
        Button followBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userImageIv = itemView.findViewById(R.id.userImageIv);
            userNameTv = itemView.findViewById(R.id.userNameTv);
            userAboutTv = itemView.findViewById(R.id.userAboutTv);
            followBtn = itemView.findViewById(R.id.followBtn);
        }
    }
}
