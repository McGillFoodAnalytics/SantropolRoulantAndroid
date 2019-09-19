package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    private EditText userName, passWord;
    private Button loginButton, forgotPassword;
    private TextView loginHeader, usernameInfo;
    String userEmail, userPassword;
    private FirebaseAuth firebaseAuth;


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
                validate(userName.getText().toString().trim(), passWord.getText().toString().trim());
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
    private void validate(String userEmail, String userPassword){
        // Firebase Authentication instance + built in function to sign in with Email and Password
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Go to Home activity
                    startActivity(new Intent(Login.this, Home.class));
                }else{
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}