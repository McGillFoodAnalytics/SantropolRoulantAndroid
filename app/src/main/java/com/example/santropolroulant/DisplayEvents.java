package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayEvents extends AppCompatActivity{

    private FirebaseAuth mAuth; // Authentication for UserID
    private RecyclerView recyclerView; // Recycler view to work with Custom Adapter
    private EventAdapter adapter; // Custom adapter 'EventAdapter'
    private List<Event> eventList; // List that will be filled with Event classes from Firebase Query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        mAuth = FirebaseAuth.getInstance();


        recyclerView = findViewById(R.id.recyclerView); //xml
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList); //
        recyclerView.setAdapter(adapter);

        // Building pop up dialog for when you click on the cardview
        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                popUpClick(position); // call private void function below
            }
        });

        // Access global variable set in 'Volunteer Options' button
        Global g = (Global)getApplication();
        final String gtype = g.getData();

        nestedListener(gtype);
    }


    // Function for nested listener
    private void nestedListener(final String gtype){
        // Query the 'type' table for the type button clicked previously
        DatabaseReference firstRef = FirebaseDatabase.getInstance().getReference().child("type").child(gtype);
        // Create new nested value listener
        ValueEventListener typeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Initial snapshot holds slot and cap
                // Set to final as they will be entered into array at the end
                final String slotVar = dataSnapshot.child("slot").getValue(String.class);
                final Integer capVar = dataSnapshot.child("cap").getValue(Integer.class);

                // Simple test for null
                if (slotVar != null) {
                    // Querying all events of type 'gtype' (KitchenAM, KitchenPM, Delivery ....)
                    Query query = FirebaseDatabase.getInstance().getReference("Event")
                            .orderByChild("type")
                            .equalTo(gtype);

                    // New event listener for new nested query
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            eventList.clear(); // Clean up array
                            if (dataSnapshot.exists()) {
                                // for each event snapshot from the query
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String eid = snapshot.getKey(); // Get event ID
                                    String dateVar = snapshot.child("date").getValue(String.class); // Get Date child
                                    Integer new_cap = snapshot.child("new_cap").getValue(Integer.class);
                                    if (new_cap == null){
                                        Log.d("@ @ : snapshot here:", "hey : BRaaaa1");
                                        // Make manual entry to eventList
                                        // Use default 'Capacity' capVar from the type table
                                        eventList.add(
                                                new Event(
                                                        dateVar,
                                                        capVar,
                                                        slotVar,
                                                        gtype,
                                                        eid
                                                )
                                        );
                                    }else {
                                        Log.d("@ @ : snapshot here:", "hey : BRaaaa2");
                                        // Make manual entry to eventList
                                        // Use new 'Capacity' new_cap from specific modification to event instance
                                        eventList.add(
                                                new Event(
                                                        dateVar,
                                                        new_cap,
                                                        slotVar,
                                                        gtype,
                                                        eid
                                                )
                                        );
                                    }
                                }
                                // Very helpful tool to ensure updated adapter on changes.
                                adapter.notifyDataSetChanged();
                            }
                        }
                        // Ignore 'onCancelled'
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    };
                    query.addValueEventListener(eventListener); // most inner listner = runs last
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        };
        firstRef.addValueEventListener(typeListener); // outer listener = runs first
    }




    // Function for popup dialogue
    private void popUpClick(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayEvents.this);

        builder.setCancelable(true);
        builder.setTitle("Sign Up"); // Title
        builder.setMessage("Would you like to sign up to volunteer?"); // Message of pop up

        // Negative Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel(); // Cancel pop up
            }
        });
        // Positive Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Event clickedEvent = eventList.get(position); // Event class pulled by position from the event array
                String clickedEid = clickedEvent.getEid(); // Extract EID
                String clickedUid = mAuth.getUid(); // Get mAuth UID
                String note = "tester";

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference(); // Get raw reference

                // Creating key from EID and UID which IS the key for the attendee instance
                String key = clickedEid+clickedUid;
                // Writing attendee instance to the firebase db
                myRef.child("attendee").child(key).child("EID").setValue(clickedEid);
                myRef.child("attendee").child(key).child("UID").setValue(clickedUid);
                myRef.child("attendee").child(key).child("Note").setValue(note);
                // Print Success message
                Toast.makeText(DisplayEvents.this, "Successfully Signed Up!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }



    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(DisplayEvents.this, MainActivity.class));
    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(DisplayEvents.this, HomeActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:
                Logout();

        }
        switch(item.getItemId()){
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }


}
