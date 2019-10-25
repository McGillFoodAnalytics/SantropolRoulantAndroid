package com.example.santropolroulant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class Calendar_Main extends AppCompatActivity implements FragmentCalendar.FragmentCalendarListener {
    private FragmentCalendar fragmentCalendar;
    private Button b;
    private String selected_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        bottomsheet_fragment fragment = bottomsheet_fragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
        fragmentCalendar = new FragmentCalendar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_calendar, fragmentCalendar)
                .commit();
        b = (Button) findViewById(R.id.done_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selected_date.isEmpty()){
                    Toast.makeText(Calendar_Main.this, selected_date, LENGTH_SHORT).show();
                    Intent intent = new Intent(Calendar_Main.this, Home.class);
                    intent.putExtra("message", selected_date);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onInputASent(String date) {
        selected_date = date;
    }
}