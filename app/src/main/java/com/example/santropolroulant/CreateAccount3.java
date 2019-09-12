package com.example.santropolroulant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Address, City Postal Code,

//Phone, email, password, password
public class CreateAccount3 extends AppCompatActivity {

    private Button regButton;
    String first_name, last_name, birth_date, address, city, postal_code, email, password, confPassword, phone_number;
    private EditText userEmail, userPassword, userConfPassword, userPhoneNumber;
    private FirebaseAuth firebaseAuth;
    private View progressOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        setupUIViews(); // function way to set up UI elements
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            first_name = extras.getString("FIRST_NAME"); // retrieve the data using keyName
            last_name = extras.getString("LAST_NAME");
            birth_date = extras.getString("BIRTH_DATE");
            address = extras.getString("ADDRESS"); // retrieve the data using keyName
            city = extras.getString("CITY");
            postal_code = extras.getString("POSTAL_CODE");
        }


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){ // validate function as condition
                    //createAccount.setClickable(false);
                    //setVisible();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                // if successful then enter user data into firebase
                                Task[] tasks = sendUserData();
                                if (tasks[0].isSuccessful() && tasks[1].isSuccessful() && tasks[2].isSuccessful() && tasks[3].isSuccessful() &&tasks[4].isSuccessful() && tasks[5].isSuccessful() && tasks[6].isSuccessful()){
                                    Toast.makeText(CreateAccount3.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(CreateAccount3.this, "Oops a monkey quit!", Toast.LENGTH_SHORT).show();
                                }
                                //setInvisible();
                               // createAccount.setClickable(true);
                                startActivity(new Intent(CreateAccount3.this, MainActivity.class));

                            }else{
                                String s = task.getException().getMessage();
                                //setInvisible();
                                //createAccount.setClickable(true);
                                Toast.makeText(CreateAccount3.this, s, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }

    private void setupUIViews() {
        regButton = (Button) findViewById(R.id.register);

        userEmail = (EditText) findViewById(R.id.etUser_Email);
        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userPhoneNumber = (EditText) findViewById(R.id.etUser_Phone_Number);
        userPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userPassword = (EditText) findViewById(R.id.etUser_Password);
        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userConfPassword = (EditText) findViewById(R.id.etUser_Conf_Password);
        userConfPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    private Boolean validate(){
        Boolean result = false;

        email = userEmail.getText().toString().trim();
        phone_number = userPhoneNumber.getText().toString().trim();
        password = userPassword.getText().toString().trim();
        confPassword = userConfPassword.getText().toString().trim();
        if(email.isEmpty() || phone_number.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else{
            result = true;
        }
        return  result;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private Task[] sendUserData(){
        //Custom UserID (key) is first two letters of last name + phonenumber
        String first_two_letters = last_name.substring(0,2).toLowerCase();
        String key = first_two_letters+phone_number;

        //Record date of signup
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        String formattedDate = df.format(birth_date);

        Task[] tasks = new Task[7];
        // Write Statement
        // Call DatabaseReference
        // Specify the Children --> user --> UserID(key) --> ____ --> setValue
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        tasks[0] = myRef.child("user").child(key).child("first_name").setValue(first_name);
        tasks[1] = myRef.child("user").child(key).child("last_name").setValue(last_name);
        tasks[2] = myRef.child("user").child(key).child("birth_date").setValue(birth_date);
        tasks[3] = myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        tasks[4] = myRef.child("user").child(key).child("email").setValue(email);
        tasks[5] = myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        tasks[6] = myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users

        myRef.child("user").child(key).child("first_name").setValue(first_name);
        myRef.child("user").child(key).child("last_name").setValue(last_name);
        myRef.child("user").child(key).child("birth_date").setValue(birth_date);
        //myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        //myRef.child("user").child(key).child("email").setValue(email);
        //myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        //myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users


        Log.d("sendingUserData",first_name + " " + last_name);


        return tasks;
    }
/*
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);

    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        createAccount.setClickable(false);
    } */

}