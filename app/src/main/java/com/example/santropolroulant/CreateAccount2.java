package com.example.santropolroulant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


//Address, City Postal Code,

//Phone, email, password, password
public class CreateAccount2 extends AppCompatActivity {

    private Button testBtn;
    String first_name, last_name, birth_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        setupUIViews(); // function way to set up UI elements

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            first_name = extras.getString("FIRST_NAME"); // retrieve the data using keyName
            last_name = extras.getString("LAST_NAME");
            birth_date = extras.getString("BIRTH_DATE");
        }

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount2.this, CreateAccount3.class ));
            }
        });

    }

    private void setupUIViews() {
        testBtn = (Button) findViewById(R.id.go_to_login);
    }



}
