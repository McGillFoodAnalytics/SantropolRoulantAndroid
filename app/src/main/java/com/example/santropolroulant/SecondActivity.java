package com.example.santropolroulant;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button VolunteerButton;


    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        VolunteerButton = (Button)findViewById(R.id.btnVolunteer);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        String userID = mAuth.getCurrentUser().getUid();

        Query queryAttendee = FirebaseDatabase.getInstance().getReference("attendee")
                .orderByChild("UID")
                .equalTo(userID);

        ValueEventListener attendeeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot attendeeSnapshot : dataSnapshot.getChildren()) {
                        String eid = attendeeSnapshot.child("EID").getValue(String.class);

                        DatabaseReference firstRef = FirebaseDatabase.getInstance().getReference().child("Event").child(eid);
                        ValueEventListener typeListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String dateVar = dataSnapshot.child("date").getValue(String.class);
                                final String typeVar = dataSnapshot.child("type").getValue(String.class);

                                if (dateVar != null) {
                                    DatabaseReference secondRef = FirebaseDatabase.getInstance().getReference().child("type").child(typeVar);
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                    Integer capVar = dataSnapshot.child("cap").getValue(Integer.class);
                                                    String slotVar = dataSnapshot.child("slot").getValue(String.class);
                                                    eventList.add(
                                                            new Event(
                                                                    dateVar,
                                                                    capVar,
                                                                    slotVar,
                                                                    typeVar
                                                            )
                                                    );
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            throw databaseError.toException();
                                        }
                                    };
                                    secondRef.addValueEventListener(eventListener);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        };
                        firstRef.addValueEventListener(typeListener);


                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        };
        queryAttendee.addValueEventListener(attendeeListener);

        VolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, VolunteerOptions.class));
            }
        });
    }


    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
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
        return super.onOptionsItemSelected(item);
    }
}
