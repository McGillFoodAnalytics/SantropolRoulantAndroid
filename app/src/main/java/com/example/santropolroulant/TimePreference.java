package com.example.santropolroulant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class TimePreference extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView crdMorning, crdAfternoon;
    private TextView tvTitle3;
    private Button btnNext3;
    String timePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_type);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();

        crdMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitam";
            }
        });

        crdAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitpm";
            }
        });

        btnNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimePreference.this, EventsCalendar.class);
                intent.putExtra("event_type", timePref);
            }
        });

    }


    private void setupUIViews(){
        tvTitle3 = findViewById(R.id.tvTitle3);
        btnNext3 = findViewById(R.id.btnNext3);

        crdMorning = findViewById(R.id.crdMorning);
        crdAfternoon = findViewById(R.id.crdAfternoon);

    }






}
