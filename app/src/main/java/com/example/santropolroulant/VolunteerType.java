package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class VolunteerType extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView crdKitchen;


    private CardView crdKam;
    private CardView crdKpm;
    private CardView crdDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_type);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();


        crdDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Similar to the other 'startActivity' except here we add info across activities
                Intent intent = new Intent(VolunteerOptions.this, CalanderSlots.class);
                // add the event type of the card clicked
                intent.putExtra("type", "deliv");
                intent.putExtra("type2", "deldr"); // adding additional event type for driving delivery (both needed)

                startActivity(intent);
            }
        });

        crdKpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerOptions.this, CalanderSlots.class);
                intent.putExtra("type", "kitpm");
                startActivity(intent);
            }
        });



        crdKam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerOptions.this, CalanderSlots.class);
                intent.putExtra("type", "kitam");
                startActivity(intent);
            }
        });

    }

    private void setupUIViews() {

        crdKitchen = (CardView) findViewById(R.id.crdKitchen);
        crdDelivery = (CardView) findViewById(R.id.crdDelivery);



    }


    // toolbar functionalities below
    // (imperfect)


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
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }
}
