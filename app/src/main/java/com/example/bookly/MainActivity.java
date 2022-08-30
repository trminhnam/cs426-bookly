package com.example.bookly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
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
        transaction.replace(R.id.frame_layout_container, new HomeFragment());
        transaction.commit();


        // set the listener for the bottom navigation bar
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, new HomeFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_notification:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, new NotificationFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_search:
                        toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.frame_layout_container, new SearchFragment());
                        transaction.commit();
                        return true;
                    case R.id.navigation_profile:
                        toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.frame_layout_container, new ProfileFragment());
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });


        //floating button add post click
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                toolbar.setVisibility(View.GONE);
                transaction.replace(R.id.frame_layout_container, new AddPostFragment());
//                        navigation.setVisibility(View.GONE);
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.profile_setting:
                //Disable the remember me function
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();

                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}