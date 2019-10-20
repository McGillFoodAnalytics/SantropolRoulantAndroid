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

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page); //Designates which layout XML to be used for this page

        // Setting up UI
        loginButton = (Button)findViewById(R.id.Login_Button);
        signupButton = (Button)findViewById(R.id.Create_Account_Button);

        // Auto login for signed in user - Commented out below
        /*firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this, Home.class));
        }//Have to implement logout before doing this*/

        // Click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click, run validate function*
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        //userRegistration click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go from this activity "MainActivity" to "CreateAccount" activity
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });

    }

}
