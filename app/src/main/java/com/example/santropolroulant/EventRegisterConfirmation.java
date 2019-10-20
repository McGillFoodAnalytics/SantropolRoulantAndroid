package com.example.santropolroulant;

import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class EventRegisterConfirmation extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mEventDatabase, mUserDataBase;
    private User userObj;
    private Button registerEvent;
    private String eventID;

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

        //Get Saved uid from shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final String uid = pref.getString("uid", "notFound");

        //set reference to point to specific user
        mUserDataBase = FirebaseDatabase.getInstance().getReference(
                USER_LOCATION
                + "/" + uid
        );

        //Get user info from reference in to a User class
        ValueEventListener getUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot == null){
                    Log.e("User Selection", "User not Found");
                    System.exit(-1);
                } else {
                    userObj = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mUserDataBase.addListenerForSingleValueEvent(getUserListener);

        //get eventID from previous Activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            eventID = extras.getString("EventID");

        } else {
            //TODO Redirect to main page, because we have nothing to send
            Log.e("Error", "no EventID was passed");
        }

        //Set Reference to point to specific Event
        mEventDatabase = FirebaseDatabase.getInstance().getReference(
                EVENT_LOCATION
                        +"/" + eventID
        );

        registerEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Task> tasks = new ArrayList<Task>();
                tasks.add(mEventDatabase.child(EVENT_FIRSTNAME).setValue(userObj.getFirst_name()));
                tasks.add(mEventDatabase.child(EVENT_LASTNAME).setValue(userObj.getLast_name()));
                tasks.add(mEventDatabase.child(EVENT_UID).setValue(uid));
                Toast.makeText(EventRegisterConfirmation.this, "Successfully Registered!",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
