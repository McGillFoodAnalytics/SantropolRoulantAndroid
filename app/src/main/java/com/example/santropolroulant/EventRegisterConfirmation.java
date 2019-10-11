package com.example.santropolroulant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santropolroulant.DataValueTypes.Event;
import com.example.santropolroulant.DataValueTypes.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EventRegisterConfirmation extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String jsonEvent;
    private String jsonUser;
    private Event event;
    private User userObj;
    private Button registerEvent;
    private String eventID;
    private String userID;

    private final String USER_LOCATION = "userSample";

    private final String EVENT_LOCATION = "eventSample";
    private final String EVENT_FIRSTNAME = "first_name";
    private final String EVENT_LASTNAME = "last_name";
    private final String EVENT_UID = "uid";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
        registerEvent = (Button) findViewById(R.id.ps_save);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (extras != null) {
            extras.getString("EventID");
            jsonEvent = extras.getString("Event");
            jsonUser = extras.getString("User");

            event = new Gson().fromJson(jsonEvent, Event.class);
            userObj = new Gson().fromJson(jsonUser, User.class);
            userID = extras.getString("UID");

        } else {
            //TODO Redirect to main page, because we have nothing to send
        }

        registerEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Task> tasks = new ArrayList<Task>();
                tasks.add(mDatabase.child(EVENT_LOCATION).child(eventID).child(EVENT_FIRSTNAME).setValue(userObj.getFirst_name()));
                tasks.add(mDatabase.child(EVENT_LOCATION).child(eventID).child(EVENT_LASTNAME).setValue(userObj.getLast_name()));
                tasks.add(mDatabase.child(EVENT_LOCATION).child(eventID).child(EVENT_UID).setValue(userID));
                Toast.makeText(EventRegisterConfirmation.this, "Successfully Registered!",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
