package com.example.santropolroulant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.widget.TextView;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class VolunteerType extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView crdKitchen, crdDelivery;
    private TextView tvTitle;
    private Button btnNext;
    String volunteerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_type);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();

        // TO DO AFTER DELIVERY!!!!
        crdKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volunteerType = "kitchen";
                // Write pop up with information
            }
        });

        crdDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volunteerType = "delivery";
                // Write pop up with information
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent();

            if (volunteerType=="kitchen") {
                intent = new Intent(VolunteerType.this, TimePreference.class);
            } else if (volunteerType=="delivery") {
                intent = new Intent(VolunteerType.this, TransportType.class);
            }

            intent.putExtra("event_type", volunteerType);
            startActivity(intent);
        }

        });

    }

    private void setupUIViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle2);
        btnNext = (Button) findViewById(R.id.btnNext2);
        crdKitchen = (CardView) findViewById(R.id.crdKitchen);
        crdDelivery = (CardView) findViewById(R.id.crdDelivery);

    }


}
