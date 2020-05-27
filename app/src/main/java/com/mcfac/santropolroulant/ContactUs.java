package com.mcfac.santropolroulant;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.mcfac.santropolroulant.R;

//This class is for the Contact Us activity
public class ContactUs extends FragmentActivity implements OnMapReadyCallback {

    private TextView title, phone, email, address;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setupUIViews();


    }


    private void setupUIViews() {

        title = findViewById(R.id.tvTitle2);
        phone = findViewById(R.id.tvPhoneNumber);

        //Listener to call Santropol Roulant
        phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission( ContactUs.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions( ContactUs.this, new String[] {android.Manifest.permission.CALL_PHONE},1);
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+15142849335"));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

        email = findViewById(R.id.tvEmail);

        //Listener to send an email to Santropol Roulant
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "info@santropolroulant.org"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hello!");
                    intent.putExtra(Intent.EXTRA_TEXT, "(Insert text here)");
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    Log.e("Sending an email", "Email failed");
                }
            }
        });

        address = findViewById(R.id.tvAddress);

        //Listener to call Santropol Roulant
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse("tel:" + phone.getText().toString()));
                startActivity(phoneIntent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" +email.getText().toString()));
                startActivity(emailIntent);
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Map to view location of Santropol Roulant
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng roulantHQ = new LatLng(45.516815, -73.575003);
        mMap.addMarker(new MarkerOptions().position(roulantHQ).title("Santropol Roulant HQ"));
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