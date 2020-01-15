package com.example.santropolroulant;

import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.santropolroulant.R.id.btnCreateAccountConfFinish;
import static com.example.santropolroulant.R.id.tvCreateAccountConfUsername;

public class CreateAccountFinish extends AppCompatActivity {

    private TextView username;
    private DatabaseReference mDatabase;
    private Button finish_button;
    private final String USER_LOC = MainActivity.USER_LOC;
    private final String USER_USERNAME_LOC = "first_name";
    private ValueEventListener nameListener;
    private String user_name = "default";
    private String last_name, phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_confirmation);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Log.d("usaname", "notnull");
            last_name = extras.getString("LAST_NAME");
            phone_number = extras.getString("PHONE_NUMBER");
        }

        setupUIViews();

        user_name = last_name.substring(0,2) + phone_number;
        Log.d("usaname", user_name);

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountFinish.this, Login.class);
                startActivity(intent);
            }
        });

    }

    private void setupUIViews() {

        username = (TextView) findViewById(tvCreateAccountConfUsername);
        username.setText(user_name);

        finish_button = (Button) findViewById(btnCreateAccountConfFinish);


    }
}
