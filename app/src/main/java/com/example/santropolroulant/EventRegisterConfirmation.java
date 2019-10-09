package com.example.santropolroulant;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santropolroulant.DataValueTypes.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

public class EventRegisterConfirmation extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String jsonEvent;
    private Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);


        firebaseAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            jsonEvent = extras.getString("Event");
            event = new Gson().fromJson(jsonEvent, Event.class);

        } else {
            //TODO Redirect to main page, because we have nothing to send
        }

    }


}
