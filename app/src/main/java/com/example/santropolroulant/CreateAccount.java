package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateAccount extends AppCompatActivity {

    private EditText userFirstName,userLastName, userPhoneNumber, userPassword, userEmail;
    private Button regButton;
    private Button loginRedirectButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private TextView username;
    String first_name, last_name, phone_number , email, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setupUIViews(); // function way to set up UI elements

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){ // validate function as condition
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                // if successful then enter user data into firebase
                                sendUserData();
                                //Toast.makeText(CreateAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.activity_username);
                                setupUIViewsSuccess();
                                //startActivity(new Intent(CreateAccount.this, MainActivity.class));

                            }else{
                                Toast.makeText(CreateAccount.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        loginRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, MainActivity.class));
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, MainActivity.class));
            }
        });
    }
    private void setupUIViewsSuccess(){
        loginRedirectButton = (Button)findViewById(R.id.go_to_login);

        String first_two_letters = last_name.substring(0,2).toLowerCase();
        String key = first_two_letters+phone_number;
        username = (TextView) findViewById(R.id.username_view);
        username.setText(key); //set text for text view
    }
    private void setupUIViews(){
        userFirstName = (EditText)findViewById(R.id.etUserF_Name);
        userFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userLastName = (EditText)findViewById(R.id.etUserL_Name);
        userLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userPhoneNumber = (EditText)findViewById(R.id.etUserPhone);
        userPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    // Boolean function to check if all info is entered
    private Boolean validate(){
        Boolean result = false;

        first_name = userFirstName.getText().toString().trim();
        last_name = userLastName.getText().toString().trim();
        phone_number = userPhoneNumber.getText().toString().trim();
        password = userPassword.getText().toString().trim();
        email = userEmail.getText().toString().trim();

        if(first_name.isEmpty() || last_name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else{
            result = true;
        }
        return  result;
    }


    private void sendUserData(){
        //Custom UserID (key) is first two letters of last name + phonenumber
        String first_twoletters = last_name.substring(0,2).toLowerCase();
        String key = first_twoletters+phone_number;

        //Record date of signup
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        String formattedDate = df.format(c);

        // Write Statement
        // Call DatabaseReference
        // Specify the Children --> user --> UserID(key) --> ____ --> setValue
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        myRef.child("user").child(key).child("first_name").setValue(first_name);
        myRef.child("user").child(key).child("last_name").setValue(last_name);
        myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        myRef.child("user").child(key).child("email").setValue(email);
        myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users


        Log.d("sendingUserData",first_name + " " + last_name);
    }
}
