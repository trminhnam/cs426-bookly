package com.example.bookly;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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



//    private Fragment getCurrentFragment(int index) {
//        switch (index) {
//            case 0: // HomeFragment
//                fragment = HomeFragment.getInstance();
//                break;
//            case 1: // Notification
//                fragment = NotificationFragment.getInstance();
//                break;
//            case 2: // game
//                fragment = AddPostFragment.getInstance();
//                break;
//            case 3: // shopping cart
//                fragment = SearchFragment.getInstance();
//                break;
//            case 4: // map
//                fragment = ProfileFragment.getInstance();
//                break;
//        }
//        return fragment;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        transaction.commit();
                        return true;
                    case R.id.navigation_notification:
                        transaction.replace(R.id.frame_layout_container, new NotificationFragment());
                        Toast.makeText(MainActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        transaction.commit();
                        return true;
                    case R.id.navigation_addpost:
                        transaction.replace(R.id.frame_layout_container, new AddPostFragment());
                        Toast.makeText(MainActivity.this, "addpost", Toast.LENGTH_SHORT).show();
                        transaction.commit();
                        return true;
                    case R.id.navigation_search:
                        transaction.replace(R.id.frame_layout_container, new SearchFragment());
                        Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
                        transaction.commit();
                        return true;
                    case R.id.navigation_profile:
                        transaction.replace(R.id.frame_layout_container, new ProfileFragment());
                        Toast.makeText(MainActivity.this, "user", Toast.LENGTH_SHORT).show();
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });
    }
//
//    private void switchFragment(int index) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, getCurrentFragment(index));
//        transaction.commit();
//    }
}