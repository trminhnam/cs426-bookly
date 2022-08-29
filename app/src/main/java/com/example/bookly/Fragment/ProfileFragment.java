package com.example.bookly.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookly.R;
import com.example.bookly.Adapter.FollowersAdapter;
import com.example.bookly.Model.FollowModel;
import com.example.bookly.Model.User;
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

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    // firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // recycler view
    RecyclerView followerRv;
    ArrayList<FollowModel> followerList;

    // other view
    ImageView changeCoverPhotoIv, coverPhotoIv;
    TextView nameTv, majorTv;
    ImageView verifyAccountIv, profileImageIv;

    // user statistics
    TextView numFollowersTv;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(
                "https://bookly-19ee2-default-rtdb.asia-southeast1.firebasedatabase.app"
        );
        storage = FirebaseStorage.getInstance("gs://bookly-19ee2.appspot.com");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // view for profile image and verify account
        profileImageIv = view.findViewById(R.id.profile_image);
        verifyAccountIv = view.findViewById(R.id.ivVerifyAccountProfileFragment);
        verifyAccountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 22);
            }
        });

        // view for cover photo and change cover photo
        coverPhotoIv = view.findViewById(R.id.dlivProfileFragment);
        changeCoverPhotoIv = view.findViewById(R.id.ivchangeCoverPhoto);
        changeCoverPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        // view for name, major, user statistics
        nameTv = view.findViewById(R.id.tvNameProfileFragment);
        majorTv = view.findViewById(R.id.tvMajorProfileFragment);
        numFollowersTv = view.findViewById(R.id.tvNumFollowersProfileFragment);

        // verify account
        verifyAccountIv = view.findViewById(R.id.ivVerifyAccountProfileFragment);
        verifyAccountIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 22);
                Toast.makeText(getContext(), "Verify account", Toast.LENGTH_SHORT).show();
            }
        });


        // get profile information from firebase and firebase storage
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            User  user = snapshot.getValue(User.class);

                            // Get cover photo
                            Picasso.get()
                                    .load(user.getCoverPhoto())
                                    .placeholder(R.drawable.placeholder)
                                    .into(coverPhotoIv);

                            // get profile image
                            Picasso.get()
                                    .load(user.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(profileImageIv);

                            // get list of follower profile images
                            numFollowersTv.setText(String.valueOf(user.getFollowerCount()));
                            nameTv.setText(user.getName());
                            majorTv.setText(user.getAddress());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // recycler view for friend list
        followerRv = view.findViewById(R.id.rv_friend);

        followerList = new ArrayList<>();

        FollowersAdapter adapter = new FollowersAdapter(followerList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        followerRv.setLayoutManager(linearLayoutManager);
        followerRv.setAdapter(adapter);

        // get list of followers
        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .child("Followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                            followerList.add(followModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 11) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                coverPhotoIv.setImageURI(uri);
                final StorageReference storageReference = storage.getReference().child("coverPhoto").child(auth.getUid());

                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        String downloadUrl = uri.toString();
                                        database.getReference()
                                                .child("Users")
                                                .child(auth.getUid())
                                                .child("coverPhoto")
                                                .setValue(downloadUrl);
                                        Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        else if (requestCode == 22){
            if (data.getData() != null) {
                Uri uri = data.getData();
                profileImageIv.setImageURI(uri);
                final StorageReference storageReference = storage.getReference().child("profileImage").child(auth.getUid());

                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        String downloadUrl = uri.toString();
                                        database.getReference()
                                                .child("Users")
                                                .child(auth.getUid())
                                                .child("profileImage")
                                                .setValue(downloadUrl);
                                        Toast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}