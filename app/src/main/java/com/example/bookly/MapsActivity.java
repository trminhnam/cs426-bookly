package com.example.bookly;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double lat = 0.0, lng = 0.0;
    String UserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // get extra from intent
        Bundle extras = getIntent().getExtras();
        lat = extras.getDouble("lat", 0.0);
        lng = extras.getDouble("lng", 0.0);
        UserName = extras.getString("UserName", "unknown");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(UserName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        LatLngBounds bounds = new LatLngBounds(new LatLng(lat, lng), new LatLng(lat, lng));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(UserName));
//        CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(bounds, 2000,2000,2);
//        mMap.animateCamera(camera);
    }
}