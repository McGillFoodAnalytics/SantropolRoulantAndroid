package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


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

        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        infoCard = (CardView)findViewById(R.id.infosessionCard);
        contactCard = (CardView)findViewById(R.id.contactCard);

        volunteerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, VolunteerOptions.class));
            }
        });

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, UserSchedule.class));
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(Home.this, VolunteerOptions.class));
            }
        });

        infoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();            }
        });

        contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();            }
        });

    }

    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
    }


    private void BackToMain(){
        finish();
        startActivity(new Intent(Home.this, Home.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }

}
