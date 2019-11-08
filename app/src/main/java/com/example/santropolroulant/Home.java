package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity {

    private CardView volunteerCard;
    private CardView scheduleCard;
    private CardView profileCard;
    private CardView infoCard;
    private CardView contactCard;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        contactCard = (CardView)findViewById(R.id.contactCard);

        // Bunch of CardView listeners
        volunteerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, VolunteerType.class));
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String username = pref.getString("uid", "notFound");

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, UserSchedule.class));
            }
        });

        // To be developed...
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, PersonalSettings.class));
                //Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(Home.this, VolunteerOptions.class));
            }
        });

        contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ContactUs.class));
            }
            // Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();            }
        });




    }
    @Override
    public void onBackPressed() {

        return;
    }
    // to be implemented somewhere on this page
    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
    }
}
