package com.example.bookly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.Comment;
import com.example.bookly.Model.User;
import com.example.bookly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {

    Context context;
    ArrayList<Comment> commentList;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    public CommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;

        // init Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_comment, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.aboutTv.setText(comment.getCommentBody());
        holder.timeTv.setText(getDate(comment.getCommentAt()));

        database.getReference()
                .child("Users")
                .child(comment.getCommentBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        Picasso.get()
                                .load(user.getProfileImage())
                                .placeholder(R.drawable.cartoon_penguin_dressed)
                                .into(holder.profileIv);

                        holder.userNameTv.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView userNameTv, aboutTv, timeTv;
        ImageView profileIv;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTv = itemView.findViewById(R.id.nameCommentItemTv);
            aboutTv = itemView.findViewById(R.id.aboutCommentItemTv);
            timeTv = itemView.findViewById(R.id.timeCommentItemTv);

            profileIv = itemView.findViewById(R.id.profileImageCommentItemIv);
        }
    }

    // convert timestamp to datetime format
    private String getDate(long timestamp) {
        return android.text.format.DateFormat.format("dd/MM/yyyy hh:mm", timestamp).toString();
    }
}
