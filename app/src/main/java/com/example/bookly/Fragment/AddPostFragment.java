package com.example.bookly.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bookly.API.SentimentAnalysis;
import com.example.bookly.Customview.PostEditText;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddPostFragment extends Fragment {
    private static AddPostFragment instance;

    PostEditText postContentEt;
    AppCompatButton postBtn;
    ImageView uploadImageIv, postImageIv;
    Uri uri;

    ImageView profileImageIv;
    TextView nameTv, aboutTv, locationTv;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // progress dialog
    ProgressDialog progressDialog;

    private static final int UPLOAD_IMAGE_INTENT_CODE = 10;

    // gps location
    List<Address> addresses;
    DetectLocation detectLocation = null;
    private LocationManager locationManager;
    private double cur_lat = 0.0, cur_lng = 0.0;
    private String address="unknown", city="unknown", state="unknown", country="unknown";
    // gps permission arrays
    private String[] locationPermissions;
    // gps permission constants
    private static final int LOCATION_REQUEST_CODE = 100;

    public static AddPostFragment getInstance() {
        if (instance == null) {
            instance = new AddPostFragment();
        }
        return instance;
    }

    public AddPostFragment() {
        // Required empty public constructor
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

        // initialize permission arrays
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
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
        locationTv = view.findViewById(R.id.locationTv);

        // add button on edit text
        postContentEt.setBottomRightIcon(R.drawable.ic_baseline_add_location_24);

        // set on click listener button add gps
        postContentEt.setIconClickListener(new PostEditText.IconClickListener() {
            @Override
            public void onClick() {
                if (detectLocation==null) {
                    detectLocation = new DetectLocation();
                    detectLocation.detectLocation();
                    locationTv.setVisibility(View.VISIBLE);
                    postContentEt.setBottomRightIcon(R.drawable.ic_baseline_location_off_24);
                }
                else
                    if (detectLocation.isFinishing()){
                        postContentEt.setBottomRightIcon(R.drawable.ic_baseline_location_off_24);
                        locationTv.setVisibility(View.VISIBLE);
                        detectLocation = new DetectLocation();
                        detectLocation.detectLocation();
                    } else {
                        postContentEt.setBottomRightIcon(R.drawable.ic_baseline_add_location_24);
                        locationTv.setVisibility(View.GONE);
                        detectLocation.stop();
                        cur_lat = 0.0; cur_lng = 0.0;
                        address="unknown"; city="unknown"; state="unknown"; country="unknown";
                    }
            }
        });

        // get current user profile image, name and about information from firebase
        database.getReference().child("Users")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
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
                String content = Objects.requireNonNull(postContentEt.getText()).toString();
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
        postBtn.setOnClickListener(v -> {

            // destroy detect location object
            if (detectLocation!=null) {
                detectLocation.stop();
            }

            // hide keyboard
            hideKeyboard(requireActivity());

            String content = Objects.requireNonNull(postContentEt.getText()).toString().trim();
            content = normalizerText(content);

            // analysis text
            SentimentModel analysisText = new SentimentAnalysis().predict(content);
            // Toast.makeText(getContext(), analysisText.getLabel(), Toast.LENGTH_SHORT).show();

            if (Objects.equals(analysisText.getLabel(), "NEGATIVE")){
                String finalContent = content;
                new AlertDialog.Builder(requireActivity()).setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
                        .setTitle("Your content is negative")
                        .setMessage("Do you want to continue posting?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            uploadPost(finalContent);
                        })
                        .setNegativeButton("No", (dialog, which)-> {
                            dialog.dismiss();
                        })
                        .setNeutralButton("Cancel", (dialog, which)-> {
                            // clean post content edit text
                            postContentEt.setText("");
                            postImageIv.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            // back to home fragment
                            requireActivity().onBackPressed();
                        })
                        .setOnDismissListener(DialogInterface::dismiss)
                        .show();
            }else{
                uploadPost(content);
            }
        });
        return view;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void uploadPost(String content){
        progressDialog.setTitle("Uploading post. Please wait...");
        progressDialog.show();
        final StorageReference storageReference = storage.getReference().child("Posts")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child(new Date().getTime()+"");
        if (uri != null){
            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Post post = new Post();

                        // set post data
                        post.setPostImage(uri.toString());
                        post.setPostedBy(auth.getCurrentUser().getUid());
                        post.setPostContent(content);
                        post.setPostedAt(new Date().getTime());

                        // set post location
                        post.setLocation(cur_lat, cur_lng);
                        post.setAddress(address);
                        post.setCity(city);
                        post.setState(state);
                        post.setCountry(country);

                        database.getReference().child("Posts")
                                .push().setValue(post)
                                .addOnSuccessListener(unused -> {
                                    disablePostButton();
                                    postContentEt.setText("");
                                    postImageIv.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Post added", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Failed to add post", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed to get post image link", Toast.LENGTH_SHORT).show();
                    })).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed to upload post image", Toast.LENGTH_SHORT).show();
                    });
        }
        else {
            // add new post without image
            Post post = new Post();
            post.setPostImage("");
            post.setPostedBy(auth.getCurrentUser().getUid());
            post.setPostContent(content);
            post.setPostedAt(new Date().getTime());

            // set post location
            post.setLocation(cur_lat, cur_lng);
            post.setAddress(address);
            post.setCity(city);
            post.setState(state);
            post.setCountry(country);

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
        postBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.post_btn_bg));
        postBtn.setTextColor(requireContext().getResources().getColor(R.color.white));
        postBtn.setEnabled(true);
    }

    private void disablePostButton() {
        postBtn.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.post_active_btn));
        postBtn.setTextColor(requireContext().getResources().getColor(R.color.gray));
        postBtn.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void findAddress(double lat, double lng) {
        Geocoder geocoder;
        geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
//            address = addresses.get(0).getAddressLine(0); // dose not work for vietnam
//            city = addresses.get(0).getLocality();    // dose not work for vietnam
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();

            locationTv.setText(state + ", " + country);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (detectLocation != null)
            detectLocation.stop();
    }

    // class for getting location of user
    class DetectLocation extends AppCompatActivity implements LocationListener {

        private boolean isRunning = true;

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            locationManager.removeUpdates(this);
        }

        public void stop() {
            isRunning = false;
            finish();
            locationManager.removeUpdates(this);
        }

        public void start() {
            isRunning = true;
        }

        public DetectLocation() {
            start();
            if (checkLocationPermission()) {
                // already allowed
                detectLocation();
            } else {
                requestLocationPermission();
            }
        }

        // Check location permission
        private boolean checkLocationPermission() {
            return ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        detectLocation();
                    } else {
                        Toast.makeText(getContext(), "Location permission is required ...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
            Toast.makeText(getContext(), "Please turn on location...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            cur_lat = location.getLatitude();
            cur_lng = location.getLongitude();

            Log.d("REGISTER_LOCATION", "Latitude = " + cur_lat);
            Log.d("REGISTER_LOCATION", "Longitude = " + cur_lng);

            if (isRunning)
                findAddress(cur_lat, cur_lng);
        }

        private void detectLocation() {
//            Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();

            locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestLocationPermission();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
        }

        private void requestLocationPermission() {
            ActivityCompat.requestPermissions((Activity) requireContext(), locationPermissions, LOCATION_REQUEST_CODE);
        }
    }
}