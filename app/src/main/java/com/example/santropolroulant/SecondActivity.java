package com.example.santropolroulant;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button VolunteerButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        VolunteerButton = (Button)findViewById(R.id.btnVolunteer);
        mAuth = FirebaseAuth.getInstance();

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
