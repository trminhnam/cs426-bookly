package com.example.bookly;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    // save all the fragments in this array
    private static Fragment fragment;
    private static BottomNavigationView navigation;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.navigation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_container, new HomeFragment());
        transaction.commit();


        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        transaction.replace(R.id.frame_layout_container, new HomeFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_notification:
                        transaction.replace(R.id.frame_layout_container, new NotificationFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_addpost:
                        transaction.replace(R.id.frame_layout_container, new AddPostFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_search:
                        transaction.replace(R.id.frame_layout_container, new SearchFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_profile:
                        transaction.replace(R.id.frame_layout_container, new ProfileFragment());
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });
    }
}