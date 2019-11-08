
package com.example.santropolroulant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


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

        crdMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitam";
                crdMorning.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#D3D163DA"));
            }
        });

        crdAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitpm";
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdMorning.setCardBackgroundColor(Color.parseColor("#D3D163DA"));
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





