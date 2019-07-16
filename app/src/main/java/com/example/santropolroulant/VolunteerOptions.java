package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class VolunteerOptions extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView crdKam;
    private CardView crdKpm;
    private CardView crdDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_options);
        mAuth = FirebaseAuth.getInstance();

        crdDelivery = (CardView) findViewById(R.id.crdDelivery);
        crdKam = (CardView)findViewById(R.id.crdKam);
        crdKpm = (CardView) findViewById(R.id.crdKpm);

        crdDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerOptions.this, DisplayEvents.class);
                intent.putExtra("type", "mealDelivery");
                startActivity(intent);

            }
        });

        crdKpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerOptions.this, DisplayEvents.class);
                intent.putExtra("type", "KitchenPM");
                startActivity(intent);
            }
        });



        crdKam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerOptions.this, DisplayEvents.class);
                intent.putExtra("type", "KitchenAM");
                startActivity(intent);
            }
        });

    }

    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(VolunteerOptions.this, MainActivity.class));
    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(VolunteerOptions.this, Home.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:
                Logout();

        }
        switch(item.getItemId()){
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }
}
