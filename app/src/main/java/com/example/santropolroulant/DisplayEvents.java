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

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        mAuth = FirebaseAuth.getInstance();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayEvents.this);

                builder.setCancelable(true);
                builder.setTitle("Sign Up");
                builder.setMessage("Would you like to sign up to volunteer?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Event clickedEvent = eventList.get(position);
                        String clickedEid = clickedEvent.getEid();
                        Toast.makeText(DisplayEvents.this, clickedEid, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });


//        dbEvents = FirebaseDatabase.getInstance().getReference("Event");
//        dbEvents.addValueEventListener(valueEventListener);

        Global g = (Global)getApplication();
        String gtype = g.getData();
        DatabaseReference firstRef = FirebaseDatabase.getInstance().getReference().child("type").child(gtype);
        ValueEventListener typeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String slotVar = dataSnapshot.child("slot").getValue(String.class);
                final Integer capVar = dataSnapshot.child("cap").getValue(Integer.class);

                if (slotVar != null) {
                    Global g = (Global)getApplication();
                    final String gtype = g.getData();
                    Query query = FirebaseDatabase.getInstance().getReference("Event")
                            .orderByChild("type")
                            .equalTo(gtype);

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            eventList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String eid = snapshot.getKey();
                                    String dateVar = snapshot.child("date").getValue(String.class);
                                    eventList.add(
                                            new Event(
                                                    dateVar,
                                                    capVar,
                                                    slotVar,
                                                    gtype,
                                                    eid
                                            )
                                    );
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    };
                    query.addValueEventListener(eventListener);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        };
        firstRef.addValueEventListener(typeListener);


    }


    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(DisplayEvents.this, MainActivity.class));
    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(DisplayEvents.this, MainActivity.class));
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
