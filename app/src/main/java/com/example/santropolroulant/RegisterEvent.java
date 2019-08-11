package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterEvent extends AppCompatActivity {


    private Button btnConfirmSignup;
    private FirebaseAuth firebaseAuth;
    private EditText txtNote;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);

        txtNote = findViewById(R.id.txtNote);

        btnConfirmSignup = findViewById(R.id.btnConfirmSignup);
        btnConfirmSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note = txtNote.getText().toString().trim();
                getUserInfo();
            }
        });

    }

    public void getUserInfo(){
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        String userKey = user.getUid();
        Query myRef = FirebaseDatabase.getInstance().getReference("user")
                .orderByChild("key")
                .equalTo(userKey);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                        String user_uid = userSnap.getKey();
                        String user_first_name = userSnap.child("first_name").getValue(String.class);
                        String user_last_name = userSnap.child("last_name").getValue(String.class);
                        writeSignUp(user_uid, user_first_name, user_last_name);
                        Log.d("hey:","Key: "+user_first_name+user_last_name+user_uid);
                    }
                }else{
                    Toast.makeText(RegisterEvent.this, "Unable to Register", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(userListener);
    }


    private void writeSignUp(String uid, String firstName, String lastName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventRef = firebaseDatabase.getReference("event");
        Intent intent = getIntent();
        final String keyIntent = intent.getStringExtra("keyIntent");
        eventRef.child(keyIntent).child("first_name").setValue(firstName);
        eventRef.child(keyIntent).child("last_name").setValue(lastName);
        eventRef.child(keyIntent).child("uid").setValue(uid);
        eventRef.child(keyIntent).child("note").setValue(note);
    }


    private void BackToMain(){
        finish();
        startActivity(new Intent(RegisterEvent.this, Home.class));
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
}
