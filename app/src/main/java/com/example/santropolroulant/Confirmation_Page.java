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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.santropolroulant.Adapters.EventAdapter;
import com.example.santropolroulant.Adapters.UserAdapter;
import com.example.santropolroulant.FirebaseClasses.Event;
import com.example.santropolroulant.FirebaseClasses.UserSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Confirmation_Page extends AppCompatActivity {

    private FirebaseAuth mAuth; // Authentication for UserID
    private RecyclerView recyclerView; // Recycler view to work with Custom Adapter
    private UserAdapter adapter; // Custom adapter 'EventAdapter'
    private List<UserSlot> userList; // List that will be filled with Event classes from Firebase Query
    private Button btnSignUp;
    private EditText txtNote;
    private Switch swtchNew;
    private Boolean isNew;
    private String selectedKey;
    private String selectedFirstName;
    private String selectedLastName;
    private String selectedUID;
    private String email;

    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation__page);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        swtchNew = (Switch)findViewById(R.id.swtchNew);
        txtNote = (EditText)findViewById(R.id.txtNote);
        recyclerView = findViewById(R.id.recyclerView2); //xml
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList); //
        recyclerView.setAdapter(adapter);

        swtchNew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isNew = true;
                }else{
                    isNew = false;
                }
            }
        });


        Intent intent = getIntent();
        final String typeT = intent.getStringExtra("type");
        final Integer dateT = intent.getIntExtra("date",-1);

        Log.d("hey:","Foop: " + String.valueOf(dateT) + typeT);

        queryFunction(typeT, dateT);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation_Page.this);
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


                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = firebaseDatabase.getReference(); // Get raw reference
                        // Creating key from EID and UID which IS the key for the attendee instance

                        // Writing attendee instance to the firebase db
                        myRef.child(EVENT_LOC).child(selectedKey).child("uid").setValue("blahblah");
                        myRef.child(EVENT_LOC).child(selectedKey).child("first_name").setValue(selectedFirstName);
                        myRef.child(EVENT_LOC).child(selectedKey).child("last_name").setValue(selectedLastName);

                        // Print Success message
                        Toast.makeText(Confirmation_Page.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }
        });

        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                UserSlot userSlot = userList.get(position);
                selectedKey = userSlot.getKey();
                selectedFirstName = userSlot.getFirstName();
                selectedLastName = userSlot.getLastName();

                userList.get(position).setFirstName("Selected Slot");
                adapter.notifyDataSetChanged();
                Toast.makeText(Confirmation_Page.this, userSlot.getFirstName(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void popUpClick(final String eid){
        AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation_Page.this);
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

                String clickedUid = mAuth.getUid(); // Get mAuth UID
                String note = txtNote.getText().toString().trim();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = firebaseDatabase.getReference(); // Get raw reference

                // Creating key from EID and UID which IS the key for the attendee instance
                String key = eid+clickedUid;

                // Writing attendee instance to the firebase db
                myRef.child("attendee").child(key).child("uid").setValue(clickedUid);
                myRef.child("attendee").child(key).child("note").setValue(note);
                myRef.child("attendee").child(key).child("new").setValue(isNew);
                myRef.child("attendee").child(key).child("eid").setValue(eid);

                // Print Success message
                Toast.makeText(Confirmation_Page.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    private void queryFunction(final String eventType, final Integer dateVal){
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("event_date")
                .equalTo(dateVal);

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();
                    Log.d("hey:","For loop key: " + key);

                    if (key.contains(String.valueOf(dateVal) + eventType)){
                        Log.d("hey:","For contained");
                        final String slot = userSnap.child("slot").getValue(String.class);
                        final String first_name = userSnap.child("first_name").getValue(String.class);
                        final String last_name = userSnap.child("last_name").getValue(String.class);

                        Log.d("hey:","Whats" +slot + " : " + first_name + " " + last_name);

                        userList.add(
                                new UserSlot(slot,first_name,last_name, key)
                        );
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addValueEventListener(countListener);

    }


}