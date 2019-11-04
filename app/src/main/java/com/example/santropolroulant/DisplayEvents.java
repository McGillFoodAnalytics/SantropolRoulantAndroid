package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DisplayEvents extends AppCompatActivity{

    private FirebaseAuth mAuth;         // Authentication for UserID
    private RecyclerView recyclerView;  // Recycler view to work with Custom Adapter
    private EventAdapter adapter;       // Custom adapter 'EventAdapter'
    private List<Event> eventList;      // List that will be filled with Event classes from Firebase Query
    private List<String> uniqueRefList;
    private Integer currentCap;
    private TextView title;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        mAuth = FirebaseAuth.getInstance();

        setUpUIViews();

        

        Intent intent = getIntent();                                 // Access global variable set in 'Volunteer Options' button
        final String gtype = intent.getStringExtra("type");

        //Switch wkndSwitch = (Switch)findViewById(R.id.swtchWknd);

        // Building pop up dialog for when you click on the cardview
        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Event event = eventList.get(position);

                if (gtype.equals("deldr") || gtype.equals("delds") || gtype.equals("deliv")|| gtype.equals("delis")) {
                    Intent intent = new Intent(DisplayEvents.this, Confirmation_Page_MealDelivery.class);
                    intent.putExtra("type", eventList.get(position).getEvent_type());
                    intent.putExtra("date", eventList.get(position).getDate());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DisplayEvents.this, Confirmation_Page.class);
                    intent.putExtra("type", eventList.get(position).getEvent_type());
                    intent.putExtra("date", eventList.get(position).getDate());
                    startActivity(intent);
                }
            }
        });

        nestedListener(gtype);
        nestedListener(wkndConverter(gtype));

        //wkndSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //@Override
            //public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //if (isChecked) {
                    //eventList.clear();
                    //adapter.notifyDataSetChanged();
                    //String wkndType = wkndConverter(gtype);
                    //nestedListener(wkndType);

                //}else{
                    //eventList.clear();
                    //adapter.notifyDataSetChanged();
                    //nestedListener(gtype);
                    //adapter.notifyDataSetChanged();
                //}
            //}
        //});
    }

    private void setUpUIViews(){

        title = findViewById(R.id.tvTitle);
        signUp = findViewById(R.id.btnSignUp);
        recyclerView = findViewById(R.id.recyclerView); // Sets linear layout to Recycler View

        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        uniqueRefList = new ArrayList<>();


    }


    // Function for nested listener
    private void nestedListener(final String gtype){
        Query queryEvents = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("event_type")
                .equalTo(gtype);

        ValueEventListener event_listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for(DataSnapshot scheduleSnap : dataSnapshot.getChildren()){
                        final String date_txt = scheduleSnap.child("event_date_txt").getValue(String.class);

                        if (!uniqueRefList.contains(date_txt)){
                            final Integer date = scheduleSnap.child("event_date").getValue(Integer.class);
                            final String start_time = scheduleSnap.child("event_time_start").getValue(String.class);
                            final String end_time = scheduleSnap.child("event_time_end").getValue(String.class);
                            final String is_current = scheduleSnap.child("is_current").getValue(String.class);
                            final Boolean first_shift = scheduleSnap.child("first_shift").getValue(Boolean.class);

                            final String event_type = scheduleSnap.child("event_type").getValue(String.class);
                            final String uid = scheduleSnap.child("uid").getValue(String.class);

                            final String note = scheduleSnap.child("note").getValue(String.class);
                            final String event_id = scheduleSnap.child("event_id").getValue(String.class);

                            eventList.add(
                                    new Event(
                                            date_txt,
                                            date,
                                            start_time,
                                            end_time,
                                            event_type,
                                            uid,
                                            note,
                                            is_current,
                                            first_shift,
                                            event_id
                                    )
                            );
                            Collections.sort(eventList, new Comparator<Event>() {
                                @Override
                                public int compare(Event e1, Event e2) {
                                    return e1.getDate().compareTo(e2.getDate());
                                }
                            });
                        };
                        uniqueRefList.add(date_txt);

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        queryEvents.addValueEventListener(event_listener);
    }

    private String wkndConverter(final String gtype){
        String wkndType = gtype;
        if (gtype.equals("kitam")){
            wkndType = "kitas";
        } else if (gtype.equals("kitpm")){
            wkndType = "kitps";
        } else if (gtype.equals("deliv")){
            wkndType = "delis";
        } else if (gtype.equals("deldr")){
            wkndType = "delds";
        }
        return wkndType;
    };



}
