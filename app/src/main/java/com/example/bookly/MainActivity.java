package com.example.bookly;

import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.bookly.Fragment.AddPostFragment;
import com.example.bookly.Fragment.HomeFragment;
import com.example.bookly.Fragment.NotificationFragment;
import com.example.bookly.Fragment.ProfileFragment;
import com.example.bookly.Fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private final static String HomeFragment_TAG = "HomeFragment";
    private final static String AddPostFragment_TAG = "AddPostFragment";
    private final static String NotificationFragment_TAG = "NotificationFragment";
    private final static String ProfileFragment_TAG = "ProfileFragment";
    private final static String SearchFragment_TAG = "SearchFragment";

    // save all the fragments in this array
    private static Fragment fragment;
    private static BottomNavigationView navigation;
    Toolbar toolbar;
    FloatingActionButton postBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My profile");
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        navigation = findViewById(R.id.navigation);
        postBtn = findViewById(R.id.addPostBtn);

        // firebase
        auth = FirebaseAuth.getInstance();

        // set default fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = getFragment(HomeFragment_TAG);
        transaction.replace(R.id.frame_layout_container, fragment, HomeFragment_TAG);
        transaction.commit();


        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, getFragment(HomeFragment_TAG), HomeFragment_TAG);
                        transaction.commit();
                        return true;
                    case R.id.navigation_notification:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, getFragment(NotificationFragment_TAG), NotificationFragment_TAG);
                        transaction.commit();
                        return true;
                    case R.id.navigation_search:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, getFragment(SearchFragment_TAG), SearchFragment_TAG);
                        transaction.commit();
                        return true;
                    case R.id.navigation_profile:
                        toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.frame_layout_container, getFragment(ProfileFragment_TAG), ProfileFragment_TAG);
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });


        //floating button add post click
        postBtn.setOnClickListener(view -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            toolbar.setVisibility(View.GONE);
            transaction1.replace(R.id.frame_layout_container, getFragment(AddPostFragment_TAG), AddPostFragment_TAG);
//                        navigation.setVisibility(View.GONE);
            transaction1.commit();
        });
    }

    public Fragment getFragment(String fragmentName) {
        switch (fragmentName) {
            case NotificationFragment_TAG:
                return NotificationFragment.getInstance();
            case SearchFragment_TAG:
                return SearchFragment.getInstance();
            case ProfileFragment_TAG:
                return ProfileFragment.getInstance();
            case AddPostFragment_TAG:
                return AddPostFragment.getInstance();
            default: // Home Fragment
                return HomeFragment.getInstance();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.profile_setting) {//Disable the remember me function
            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "false");
            editor.apply();

            new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.ic_logout_black)
                    .setTitle("Logging out").setMessage("Are you sure you want to logging out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("No", null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (getSupportFragmentManager().findFragmentByTag(HomeFragment_TAG) == null)
            {
                toolbar.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout_container, getFragment(HomeFragment_TAG), HomeFragment_TAG)
                        .commit();
            } else {
                super.onBackPressed();
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}