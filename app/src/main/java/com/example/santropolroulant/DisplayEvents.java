package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.santropolroulant.Adapters.EventAdapter;
import com.example.santropolroulant.FirebaseClasses.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayEvents extends AppCompatActivity{

    private FirebaseAuth mAuth; // Authentication for UserID
    private RecyclerView recyclerView; // Recycler view to work with Custom Adapter
    private EventAdapter adapter; // Custom adapter 'EventAdapter'
    private List<Event> eventList; // List that will be filled with Event classes from Firebase Query
    private Integer currentCap;

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

        // Access global variable set in 'Volunteer Options' button
        Intent intent = getIntent();
        final String gtype = intent.getStringExtra("type");

        Switch wkndSwitch = (Switch)findViewById(R.id.swtchWknd);

        // Building pop up dialog for when you click on the cardview
        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Event event = eventList.get(position);
                if (event.getNum_vols() < event.getCap()){
                    if (gtype.equals("mealDelivery") || gtype.equals("mealDelivery_Sat")) {
                        Intent intent = new Intent(DisplayEvents.this, Confirmation_Page_MealDelivery.class);
                        intent.putExtra("eid", eventList.get(position).getEid());
                        intent.putExtra("cap", eventList.get(position).getCap());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(DisplayEvents.this, Confirmation_Page.class);
                        intent.putExtra("eid", eventList.get(position).getEid());
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(DisplayEvents.this,"This Event is Full", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nestedListener(gtype);
        wkndSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    eventList.clear();
                    adapter.notifyDataSetChanged();
                    final String wkndType = gtype + "_Sat";
                    nestedListener(wkndType);

                }else{
                    adapter.notifyDataSetChanged();
                    nestedListener(gtype);
                }
            }
        });
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
                final Integer startTimeUnix = dataSnapshot.child("start_time").getValue(Integer.class);

                // Simple test for null
                if (slotVar != null) {
                    // Querying all events of type 'gtype' (KitchenAM, KitchenPM, Delivery ....)
                    Query query = FirebaseDatabase.getInstance().getReference("event")
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
                                    String is_new = snapshot.child("is_new").getValue(String.class);
                                    Integer number_of_volunteers = snapshot.child("number_of_volunteers").getValue(Integer.class);

                                    if (is_new.equals("New")){
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
                                                            eid,
                                                            number_of_volunteers
                                                    )
                                            );
                                        }else{
                                            Log.d("@ @ : snapshot here:", "hey : BRaaaa2");
                                            // Make manual entry to eventList
                                            // Use new 'Capacity' new_cap from specific modification to event instance
                                            eventList.add(
                                                    new Event(
                                                            dateVar,
                                                            new_cap,
                                                            slotVar,
                                                            gtype,
                                                            eid,
                                                            number_of_volunteers
                                                    )
                                            );
                                        }
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


    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(DisplayEvents.this, MainActivity.class));
    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(DisplayEvents.this, Home.class));
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
