package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button loginButton;
    private CardView userRegistration;
    private FirebaseAuth firebaseAuth;
    private CardView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //Designates which layout XML to be used for this page

        // Setting up UI
        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        loginButton = (Button)findViewById(R.id.btnLogin);
        // The following are CardViews rather than buttons for design purpose. Same functionality
        //userRegistration = (CardView) findViewById(R.id.crdRegister);
        forgotPassword = (CardView) findViewById(R.id.crdForgotPassword);

        // Getting current app user from Firebase
        //firebaseAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        //if(user != null){
            //finish();
            //startActivity(new Intent(MainActivity.this, Home.class));
        //}

        // Click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click, run validate function*
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });
/*
        // userRegistration click listener
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go from this activity "MainActivity" to "CreateAccount" activity
                startActivity(new Intent(Login.this, CreateAccount.class));
            }
        });
*/
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go from this activity "MainActivity" to "PasswordActivity" activity
                startActivity(new Intent(Login.this, PasswordActivity.class));
            }
        });
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
