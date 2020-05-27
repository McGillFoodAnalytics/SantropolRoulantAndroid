
package com.mcfac.santropolroulant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

//This class deals with the user's selection between morning or afternoon shifts
public class TimePreference extends AppCompatActivity  {
    private CardView crdMorning, crdAfternoon;
    private TextView tvTitle3;
    private Button btnNext3;
    String timePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_preference);

        tvTitle3 = findViewById(R.id.tvTitle3);
        btnNext3 = findViewById(R.id.btnNext3);
        crdMorning = findViewById(R.id.crdMorning);
        crdAfternoon = findViewById(R.id.crdAfternoon);
        crdAfternoon.setCardBackgroundColor(Color.parseColor("#DCAADC"));
        crdMorning.setCardBackgroundColor(Color.parseColor("#DCAADC"));

        crdMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kita";
                crdMorning.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#DCAADC"));
            }
        });

        crdAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitp";
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdMorning.setCardBackgroundColor(Color.parseColor("#DCAADC"));
            }
        });

        btnNext3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                    Intent intent = new Intent(TimePreference.this, Calendar_Main.class);
                    intent.putExtra("two", timePref);
                    startActivity(intent);


            }
        });

    }



}





