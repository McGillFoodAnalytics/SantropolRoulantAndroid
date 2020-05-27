package com.mcfac.santropolroulant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcfac.santropolroulant.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;


//This class deals with Event Registration.  It implements FragmentCalendar and updates bottomsheet_fragment
public class Calendar_Main extends AppCompatActivity implements FragmentCalendar.FragmentCalendarListener {

    private FragmentCalendar fragmentCalendar;
    FragmentTransaction transaction;
    bottomsheet_fragment bottomsheetfragment;
    private Button b;
    private String selected_date;
    String eventType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventType = extras.getString("two");
        }

        fragmentCalendar = new FragmentCalendar(eventType);
        bottomsheetfragment = new bottomsheet_fragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, bottomsheetfragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_calendar, fragmentCalendar)
                .commit();
    }

    //Redirect Home upon pressing back button on device
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Calendar_Main.this, Home.class));
        finish();

    }

    //Method to update bottomsheetfragment upon a selection to the calendar in FragmentCalendar
    @Override
    public void onInputASent(String input, Integer dateval) {
        bottomsheetfragment.updateEditText(input,dateval,eventType);
    }


}