package com.example.santropolroulant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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

public class Calendar_Main extends FragmentActivity {
    private FragmentCalendar fragmentCalendar;
    FragmentTransaction transaction;
    bottomsheet_fragment bottomsheetfragment;
    private Button b;
    private String selected_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragmentCalendar = new FragmentCalendar();
        bottomsheetfragment = new bottomsheet_fragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, bottomsheetfragment)
                .commit();
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

}