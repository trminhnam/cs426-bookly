package com.example.bookly.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bookly.CommentActivity;
import com.example.bookly.MapsActivity;
import com.example.bookly.Model.Notification;
import com.example.bookly.Model.Post;
import com.example.bookly.Model.User;
import com.example.bookly.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

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
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_dashboard_modern, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Post model = dashboardList.get(position);

        holder.aboutTv.setText(TimeAgo.using(model.getPostedAt()));

        if (model.getPostImage().equals("")) {
            holder.postImageIv.setVisibility(View.GONE);
        } else {
            holder.postImageIv.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(model.getPostImage())
                    .placeholder(R.drawable.ic_blank_image)
                    .into(holder.postImageIv);
        }

        holder.likeTv.setText(model.getPostLike() + "");
        holder.commentTv.setText(model.getCommentCount() + "");
        String content = model.getPostContent();
        if (content.equals("")) {
            holder.postContentTv.setVisibility(View.GONE);
        } else {
            holder.postContentTv.setVisibility(View.VISIBLE);
            holder.postContentTv.setText(content);
        }

        if (!model.getState().equals("unknown"))
        {
            holder.locationTv.setVisibility(View.VISIBLE);
            holder.locationTv.setText(model.getState() + ", " + model.getCountry());
            holder.locationTv.setOnClickListener(v -> {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("UserName", holder.nameTv.getText().toString());
                intent.putExtra("lat", model.getLat());
                intent.putExtra("lng", model.getLng());
                context.startActivity(intent);
            });
        }

        database.getReference()
                .child("Users")
                .child(model.getPostedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        assert user != null;
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.cartoon_penguin_dressed)
                                .into(holder.profileIv);
                        holder.nameTv.setText(user.getName());
//                        holder.aboutTv.setText(user.get);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child("Posts")
                .child(model.getPostID())
                .child("likes")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_active_svgrepo_com, 0, 0, 0);
                        } else {
                            holder.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_icon, 0, 0, 0);
                            holder.likeTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database.getReference()
                                            .child("Posts")
                                            .child(model.getPostID())
                                            .child("likes")
                                            .child(Objects.requireNonNull(auth.getUid()))
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

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(auth.getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostID(model.getPostID());
                                                                    notification.setPostedBy(model.getPostedBy());
                                                                    notification.setType("Like");

                                                                    database.getReference()
                                                                            .child("Notifications")
                                                                            .child(model.getPostedBy())
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

        holder.shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharingDialog(holder, model);
            }
        });
    }

    private void showSharingDialog(@NonNull viewHolder holder, Post model) {
        String[] options = {"Text", "Image", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose what to share ...")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            shareText(holder, model);
                        } else if (which == 1) {
                            shareImage(holder, model);
                        }
                    }
                })
                .show();
    }

    private void shareImage(viewHolder holder, Post post) {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
        intent.setType("image/jpeg");

        // add image
        if (!post.getPostImage().equals("")) {
            Glide.with(context).asBitmap().load(post.getPostImage()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, "Image Description", null);
                    Uri uri = Uri.parse(path);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(intent, "Share image via..."));
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        } else {
            Toast.makeText(context, "No image to share", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareText(viewHolder holder, Post post) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        // add text
        intent.putExtra(Intent.EXTRA_TEXT, holder.postContentTv.getText().toString().trim());

        context.startActivity(Intent.createChooser(intent, "Share text via..."));
    }

    @Override
    public int getItemCount() {
        return dashboardList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView profileIv, postImageIv, saveIv;
        TextView nameTv, aboutTv, likeTv, commentTv, shareTv, locationTv;
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
            locationTv = itemView.findViewById(R.id.location);
        }
    }
}
