package com.example.santropolroulant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;


import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;


public class ContactUs extends FragmentActivity implements OnMapReadyCallback {

    private TextView title, phone, email, address;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setupUIViews();
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us); // Designates which layout XML to be used for this page
        setupUIViews();
    }*/


    private void setupUIViews() {

        title = findViewById(R.id.tvTitle2);
        phone = findViewById(R.id.tvPhoneNumber);
        email = findViewById(R.id.tvEmail);
        address = findViewById(R.id.tvAddress);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng roulantHQ = new LatLng(45.516815, -73.575003);
        mMap.addMarker(new MarkerOptions().position(roulantHQ).title("Santropol Roulant HQ"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(roulantHQ));
        moveToCurrentLocation(roulantHQ);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }
}
