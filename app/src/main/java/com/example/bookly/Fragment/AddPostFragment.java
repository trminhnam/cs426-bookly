package com.example.bookly.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bookly.API.SentimentAnalysis;
import com.example.bookly.Model.Post;
import com.example.bookly.Model.SentimentModel;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

public class AddPostFragment extends Fragment {

    EditText postContentEt;
    AppCompatButton postBtn;
    ImageView uploadImageIv, postImageIv;
    Uri uri;

    ImageView profileImageIv;
    TextView nameTv, aboutTv;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // progress dialog
    ProgressDialog progressDialog;

    private static final int UPLOAD_IMAGE_INTENT_CODE = 10;

    public AddPostFragment() {
        // Required empty public constructor
    }


    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app");
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");

        // initialize progress dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        postContentEt = view.findViewById(R.id.postContentEt);
        postBtn = view.findViewById(R.id.postBtn);
        uploadImageIv = view.findViewById(R.id.uploadIv);
        postImageIv = view.findViewById(R.id.postImageIv);
        profileImageIv = view.findViewById(R.id.profile_image);
        nameTv = view.findViewById(R.id.nameTv);
        aboutTv = view.findViewById(R.id.aboutTv);

        // get current user profile image, name and about information from firebase
        database.getReference().child("Users")
                .child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            assert user != null;
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(profileImageIv);
                            nameTv.setText(user.getName());
                            aboutTv.setText(user.getAddress());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // enable and disable post button when user enter text in post content edit text
        postContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = postContentEt.getText().toString();
                if (!content.isEmpty()){
                    enablePostButton();
                } else {
                    disablePostButton();
                }

                // do not allow duplicated new lines
                if (charSequence.length() > 3 && charSequence.charAt(charSequence.length()-1) == '\n' && charSequence.charAt(charSequence.length()-2) == '\n' && charSequence.charAt(charSequence.length()-3) == '\n'){
                    postContentEt.setText(charSequence.subSequence(0, charSequence.length()-2));
                    postContentEt.setSelection(charSequence.length()-2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // load a new image from gallery when user click on upload image image view
        uploadImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, UPLOAD_IMAGE_INTENT_CODE);
            }
        });

        // post content and add data to firebase when user click on post button
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageReference storageReference = storage.getReference().child("Posts")
                        .child(auth.getCurrentUser().getUid()).child(new Date().getTime()+"");

                progressDialog.setTitle("Uploading post. Please wait...");
                progressDialog.show();

                if (uri != null){
                    storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Post post = new Post();
                                            String content = postContentEt.getText().toString().trim();
                                            content = normalizerText(content);

                                            post.setPostImage(uri.toString());
                                            post.setPostedBy(auth.getCurrentUser().getUid());
                                            post.setPostContent(content);
                                            post.setPostedAt(new Date().getTime());
                                            // analysis text
                                            SentimentModel analysisText = new SentimentAnalysis().predict(content);
                                            Toast.makeText(getContext(), analysisText.getLabel(), Toast.LENGTH_SHORT).show();

                                            if (Objects.equals(analysisText.getLabel(), "NEGATIVE")){
                                                // TODO:
                                            }

                                            database.getReference().child("Posts")
                                                    .push().setValue(post)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            disablePostButton();
                                                            postContentEt.setText("");
                                                            postImageIv.setVisibility(View.GONE);
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getContext(), "Post added", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getContext(), "Failed to add post", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Failed to get post image link", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed to upload post image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    // add new post without image
                    Post post = new Post();
                    String content = postContentEt.getText().toString().trim();
                    content = normalizerText(content);

                    post.setPostImage("");
                    post.setPostedBy(auth.getCurrentUser().getUid());
                    post.setPostContent(content);
                    post.setPostedAt(new Date().getTime());
                    // analysis text
                    SentimentModel analysisText = new SentimentAnalysis().predict(content);
                    Toast.makeText(getContext(), analysisText.getLabel(), Toast.LENGTH_SHORT).show();

                    if (Objects.equals(analysisText.getLabel(), "NEGATIVE")){
                        // TODO:
                    }

                    database.getReference().child("Posts")
                            .push().setValue(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    disablePostButton();
                                    postContentEt.setText("");
                                    postImageIv.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Post added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed to add post", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)
            return;
        if (data.getData() != null){
            uri = data.getData();
            postImageIv.setImageURI(uri);
            postImageIv.setVisibility(View.VISIBLE);

            enablePostButton();
        }
    }

    private String normalizerText(String text){
        return text.trim();
    }

    private void enablePostButton() {
        postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_btn_bg));
        postBtn.setTextColor(getContext().getResources().getColor(R.color.white));
        postBtn.setEnabled(true);
    }

    private void disablePostButton() {
        postBtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.post_active_btn));
        postBtn.setTextColor(getContext().getResources().getColor(R.color.gray));
        postBtn.setEnabled(false);
    }
}