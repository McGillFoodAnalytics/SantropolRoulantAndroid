package com.example.santropolroulant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santropolroulant.Adapters.EventAdapter;
import com.example.santropolroulant.FirebaseClasses.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserSchedule extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button VolunteerButton;


    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        VolunteerButton = (Button)findViewById(R.id.btnVolunteer);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserSchedule.this);

                builder.setCancelable(true);
                builder.setTitle("Unregister?");
                builder.setMessage("Would you like to unregister from this volunteering event? :(");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserSchedule.this, "PumpFake Unregistered!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        getUserInfo();

        VolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSchedule.this, VolunteerOptions.class));
            }
        });
    }

    private void querySchedule(String user_UID){
        Log.d("@ @ : snapshot here:", "hey : " + user_UID);

        Query querySchedule = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("uid")
                .equalTo(user_UID);

        final ValueEventListener scheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot scheduleSnap : dataSnapshot.getChildren()){
                        String key = scheduleSnap.getKey();
                        final Integer date = scheduleSnap.child("event_date").getValue(Integer.class);
                        final String date_txt = scheduleSnap.child("event_date_txt").getValue(String.class);
                        final String start_time = scheduleSnap.child("event_time_start").getValue(String.class);
                        final String end_time = scheduleSnap.child("event_time_end").getValue(String.class);
                        final String is_current = scheduleSnap.child("is_current").getValue(String.class);
                        final Boolean first_shift = scheduleSnap.child("first_shift").getValue(Boolean.class);
                        final String event_type = scheduleSnap.child("event_type").getValue(String.class);
                        final String uid = scheduleSnap.child("uid").getValue(String.class);
                        final String note = scheduleSnap.child("note").getValue(String.class);

                        Log.d("@ @ : snapshot here:", "hey : BRaaaa1" + key);
                            // Make manual entry to eventList
                            // Use default 'Capacity' capVar from the type table

                        Log.d("@ @ : snapshot here:", "hey : after" + date_txt+ start_time + end_time +String.valueOf(first_shift));

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
                                        false
                                )
                        );
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        querySchedule.addValueEventListener(scheduleListener);
    }

    private void getUserInfo(){

        final String userID = mAuth.getCurrentUser().getUid();

        Query myRef = FirebaseDatabase.getInstance().getReference("user")
                .orderByChild("key")
                .equalTo(userID);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                        String user_UID = userSnap.getKey();
                        Log.d("hey:","Key: "+user_UID);
                        querySchedule(user_UID);
                    }
                }else{
                    Toast.makeText(UserSchedule.this, "Unable to Fetch Events", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(userListener);
    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(UserSchedule.this, Home.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(UserSchedule.this, UserSchedule.class));
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
}
