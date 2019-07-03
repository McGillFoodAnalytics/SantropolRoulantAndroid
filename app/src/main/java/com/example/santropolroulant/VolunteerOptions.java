package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button btnKam;
    private Button btnKpm;
    private Button btnDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_options);
        mAuth = FirebaseAuth.getInstance();

        btnDelivery = (Button)findViewById(R.id.btnDelivery);
        btnKam = (Button)findViewById(R.id.btnKam);
        btnKpm = (Button)findViewById(R.id.btnKpm);

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VolunteerOptions.this, DisplayEvents.class));
                Global g = (Global)getApplication();
                g.setData("mealDelivery");
                String temp = g.getData();
                Toast.makeText(VolunteerOptions.this, temp, Toast.LENGTH_SHORT).show();
            }
        });

        btnKpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VolunteerOptions.this, DisplayEvents.class));
                Global g = (Global)getApplication();
                g.setData("KitchenPM");
                String temp = g.getData();
                Toast.makeText(VolunteerOptions.this, temp, Toast.LENGTH_SHORT).show();
            }
        });

        btnKam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VolunteerOptions.this, DisplayEvents.class));
                Global g = (Global)getApplication();
                g.setData("KitchenAM");
                String temp = g.getData();
                Toast.makeText(VolunteerOptions.this, temp, Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(VolunteerOptions.this, HomeActivity.class));
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
