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

import com.example.santropolroulant.Adapters.UserAdapter;
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


public class Confirmation_Page_MealDelivery extends AppCompatActivity {

    private FirebaseAuth mAuth;         // Authentication for UserID
    private RecyclerView recyclerView;  // Recycler view to work with Custom Adapter
    private UserAdapter adapter;        // Custom adapter 'EventAdapter'
    private List<UserSlot> userList;    // List that will be filled with Event classes from Firebase Query
    private Button btnSignUp;
    private EditText txtNote;
    private Switch swtchNew;
    private Switch swtchDriver;
    private Boolean isNew;
    private Boolean isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation__page__meal_delivery);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp = (Button)findViewById(R.id.btnSignUp_meal);
        swtchNew = (Switch)findViewById(R.id.swtchNew_meal);
        swtchDriver = (Switch)findViewById(R.id.swtchDriver);
        txtNote = (EditText)findViewById(R.id.txtNote_meal);
        recyclerView = findViewById(R.id.recyclerView2_meal); //xml
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

        swtchDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isDriver = true;
                }else{
                    isDriver = false;
                }
            }
        });


        Intent intent = getIntent();
        final String eid = intent.getStringExtra("eid");

        queryFunction(eid);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpClick(eid);
            }
        });


    }

    private void popUpClick(final String eid){
        AlertDialog.Builder builder = new AlertDialog.Builder(Confirmation_Page_MealDelivery.this);
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
                myRef.child("attendee").child(key).child("driver").setValue(isDriver);
                myRef.child("attendee").child(key).child("eid").setValue(eid);

                // Print Success message
                Toast.makeText(Confirmation_Page_MealDelivery.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    private void queryFunction(final String eid){
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference("attendee")
                .orderByChild("eid")
                .equalTo(eid);
        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String uid = userSnap.child("uid").getValue(String.class);

                    Log.d("ConfirmPAGE", "Outer Ting");
                    DatabaseReference innerRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

                    ValueEventListener userListener= new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String firstname = dataSnapshot.child("first_name").getValue(String.class);
                            String lastname = dataSnapshot.child("last_name").getValue(String.class);

                            Log.d("ConfirmPAGE", "Inner Ting" + email + firstname + lastname);
                            userList.add(
                                    new UserSlot(
                                            email,
                                            firstname,
                                            lastname,
                                            "2"
                                    )
                            );
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    innerRef.addValueEventListener(userListener);

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
