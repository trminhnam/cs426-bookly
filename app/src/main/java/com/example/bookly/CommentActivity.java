package com.example.bookly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookly.Adapter.CommentAdapter;
import com.example.bookly.Model.Comment;
import com.example.bookly.Model.Notification;
import com.example.bookly.Model.Post;
import com.example.bookly.Model.User;
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

public class CommentActivity extends AppCompatActivity {

    // extra from intent
    Intent intent;
    String postID;
    String postedBy;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // Comment view elements
    ImageView postImageIv;
    TextView postContentTv, postLikeTv;
    ImageView profileImageIv;
    TextView nameTv;
    ImageView commentPostButtonIv;
    TextView commentEt;
    TextView commentCountTv;
    Toolbar toolbar;

    // Comment recycler view
    RecyclerView commentRv;
    ArrayList<Comment> commentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // get extra from intent
        intent = getIntent();
        postID = intent.getStringExtra("postID");
        postedBy = intent.getStringExtra("postedBy");
        Toast.makeText(this, "Post ID: " + postID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Posted By: " + postedBy, Toast.LENGTH_SHORT).show();

        // init view
        postImageIv = findViewById(R.id.postImage);
        postContentTv = findViewById(R.id.description);
        postLikeTv = findViewById(R.id.like);
        profileImageIv = findViewById(R.id.profile_image);
        nameTv = findViewById(R.id.name);
        commentPostButtonIv = findViewById(R.id.commentPostBtn);
        commentEt = findViewById(R.id.commentEt);
        commentCountTv = findViewById(R.id.comment);
        toolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // init Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");

        // load post content from firebase
        database.getReference()
                .child("Posts")
                .child(postID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Post post = snapshot.getValue(Post.class);

                        if (post.getPostImage().equals("")) {
                            postImageIv.setVisibility(View.GONE);
                        } else {
                            postImageIv.setVisibility(View.VISIBLE);
                            Picasso.get()
                                    .load(post.getPostImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(postImageIv);
                        }

                        postContentTv.setText(post.getPostContent());
                        postLikeTv.setText(post.getPostLike() + "");
                        commentCountTv.setText(post.getCommentCount() + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // load user profile image and name from firebase
        database.getReference()
                .child("Users")
                .child(postedBy)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(profileImageIv);
                        nameTv.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // change reaction button image if user already liked the post
        database.getReference()
                .child("Posts")
                .child(postID)
                .child("likes")
                .child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            postLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_active_svgrepo_com, 0, 0, 0);
                        } else {
                            postLikeTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    database.getReference()
                                            .child("Posts")
                                            .child(postID)
                                            .child("likes")
                                            .child(auth.getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference()
                                                            .child("Posts")
                                                            .child(postID)
                                                            .child("postLike")
                                                            .setValue(Integer.parseInt(postLikeTv.getText().toString()) + 1) // increase likes
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    postLikeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_active_svgrepo_com, 0, 0, 0);

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(auth.getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostID(postID);
                                                                    notification.setPostedBy(postedBy);
                                                                    notification.setType("Like");

                                                                    database.getReference()
                                                                            .child("Notifications")
                                                                            .child(postedBy)
                                                                            .push()
                                                                            .setValue(notification);
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

        // add comment to firebase
        commentPostButtonIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Comment comment = new Comment();
                    comment.setCommentBody(commentEt.getText().toString());
                    comment.setCommentBy(auth.getCurrentUser().getUid());
                    comment.setCommentAt(new Date().getTime());

                    database.getReference()
                            .child("Posts")
                            .child(postID)
                            .child("Comments")
                            .push()
                            .setValue(comment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference()
                                            .child("Posts")
                                            .child(postID)
                                            .child("commentCount")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int commentCount = 0;
                                                    if (snapshot.exists()) {
                                                        commentCount = snapshot.getValue(Integer.class);
                                                    }

                                                    database.getReference()
                                                            .child("Posts")
                                                            .child(postID)
                                                            .child("commentCount")
                                                            .setValue(commentCount + 1)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    commentEt.setText("");
                                                                    Toast.makeText(CommentActivity.this, "Comment Successfully", Toast.LENGTH_SHORT).show();

                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(auth.getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostID(postID);
                                                                    notification.setPostedBy(postedBy);
                                                                    notification.setType("Comment");

                                                                    database.getReference()
                                                                            .child("Notifications")
                                                                            .child(postedBy)
                                                                            .push()
                                                                            .setValue(notification);
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(CommentActivity.this, "Comment Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CommentActivity.this, "Comment Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });


        // load comments from firebase
        commentRv = findViewById(R.id.commentRv);
        commentList = new ArrayList<>();
        CommentAdapter adapter = new CommentAdapter(this, commentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(layoutManager);
        commentRv.setAdapter(adapter);

        database.getReference()
                .child("Posts")
                .child(postID)
                .child("Comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            commentList.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}











































