package com.example.bookly.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.CommentActivity;
import com.example.bookly.Model.Post;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostAdapter extends  RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<Post> dashboardList;
    Context context;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    public PostAdapter(ArrayList<Post> dashboardList, Context context) {
        this.dashboardList = dashboardList;
        this.context = context;

        // init Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_dashboard, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post model = dashboardList.get(position);

        Picasso.get()
                .load(model.getPostImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.postImageIv);
        holder.likeTv.setText(model.getPostLike() + "");
        holder.commentTv.setText(model.getCommentCount() + "");
        String content = model.getPostContent();
        if (content.equals("")){
            holder.postContentTv.setVisibility(View.GONE);
        } else {
            holder.postContentTv.setVisibility(View.VISIBLE);
            holder.postContentTv.setText(content);
        }

        database.getReference()
                .child("Users")
                .child(model.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(holder.profileIv);
                        holder.nameTv.setText(user.getName());
                        holder.aboutTv.setText(user.getAddress());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child("Posts")
                .child(model.getPostID())
                .child("likes")
                .child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_active_svgrepo_com, 0, 0, 0);
                        } else {
                            holder.likeTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database.getReference()
                                            .child("Posts")
                                            .child(model.getPostID())
                                            .child("likes")
                                            .child(auth.getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference()
                                                            .child("Posts")
                                                            .child(model.getPostID())
                                                            .child("postLike")
                                                            .setValue(model.getPostLike() + 1) // increase likes
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_active_svgrepo_com, 0, 0, 0);

                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postID", model.getPostID());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


//        holder.profile.setImageResource(model.getProfile());
//        holder.postImage.setImageResource(model.getPostImage());
//        holder.save.setImageResource(model.getSave());
//        holder.name.setText(model.getName());
//        holder.about.setText(model.getAbout());
//        holder.like.setText(model.getLike());
//        holder.comment.setText(model.getComment());
//        holder.share.setText(model.getShare());

    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profileIv, postImageIv, saveIv;
        TextView nameTv, aboutTv, likeTv, commentTv, shareTv;
        TextView postContentTv;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profile_image);
            postImageIv = itemView.findViewById(R.id.postImg);
            saveIv = itemView.findViewById(R.id.save);
            nameTv = itemView.findViewById(R.id.userName);
            aboutTv = itemView.findViewById(R.id.about);
            likeTv = itemView.findViewById(R.id.like);
            commentTv = itemView.findViewById(R.id.comment);
            shareTv = itemView.findViewById(R.id.share);
            postContentTv = itemView.findViewById(R.id.postContentTv);

        }
    }
}

























