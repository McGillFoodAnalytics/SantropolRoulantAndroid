package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText userName, passWord;
    private Button loginButton, forgotPassword;
    private TextView loginHeader, usernameInfo;
    String userEmail, userPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Designates which layout XML to be used for this page
        setupUIViews();                          // Sets up UI using function



        // Getting current app user from Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //  FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        //if(user != null){
        //finish();
        //startActivity(new Intent(MainActivity.this, Home.class));
        //}


        // Click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click, run validate function
                String username = userName.getText().toString().trim();
                userPassword = passWord.getText().toString().trim();
                validate(username, userPassword);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, PasswordActivity.class));         // On click, go to PasswordActivity
            }
        });
    }

    private void setupUIViews() {
        userName = (EditText)findViewById(R.id.etUsername);
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        passWord = (EditText)findViewById(R.id.etPassword);
        passWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        loginButton = (Button)findViewById(R.id.btnLogin);
        forgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        loginHeader = (TextView)findViewById(R.id.tvLogInHeader);
        usernameInfo = (TextView)findViewById(R.id.tvUsernameInfo);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    // *Validate function
    //TODO: Parse username, as ref.child(username) will crash if there is '.', which may be put by someone if they put there email.
    private void validate(String username, String usersPassword){
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot!=null){
                    Log.d("inDataChange", "true");
                    String userEmail =  snapshot.child("email").getValue(String.class);
                    performLogin(userEmail,userPassword);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               Log.d( "inDataChange","false");

            }
        });

    }

/*
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("code", access_code);
                editor.apply();
            }*/
    private void performLogin(String emailId, String password){

        if(emailId==null){
            Toast.makeText(Login.this, "Login Failed: Incorrect Username", Toast.LENGTH_SHORT).show();
            return;
        }

        //Firebase Authentication instance + built in function to sign in with Email and Password
        firebaseAuth.signInWithEmailAndPassword(emailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Go to Home activity
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                }else{
                    Toast.makeText(Login.this, "Login Failed: Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}