package com.mcfac.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private EditText userName, passWord;
    private Button loginButton;
    private TextView loginHeader, usernameInfo, forgotPassword;
    String userEmail, userPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    String key;
    private View progressOverlay;
    private View loginView;
    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Designates which layout XML to be used for this page
        setupUIViews();                          // Sets up UI using function

        loginView = (View) findViewById(R.id.activity_login);
        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.INVISIBLE);


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
                setVisible();
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
        //forgotPassword = (Button) findViewById(R.id.btnForgotPassword);

     //   loginHeader = (TextView)findViewById(R.id.tvLogInHeader);
        usernameInfo = (TextView)findViewById(R.id.tvUsernameInfo);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    // *Validate function
    //TODO: Parse username, as ref.child(username) will crash if there is '.', which may be put by someone if they put there email
    private void validate(final String username, String usersPassword){

        Pattern p = Pattern.compile("[^0-9A-zÀ-ú]");
        if(p.matcher(username).find()){
            Toast.makeText(Login.this, R.string.username_alphanumeric, Toast.LENGTH_SHORT).show();
            return;
        }
        ref = FirebaseDatabase.getInstance().getReference(USER_LOC);
        ref.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot!=null){


                    Log.d("inDataChange", "true");
                    String userEmail =  snapshot.child("email").getValue(String.class);
                    performLogin(userEmail,userPassword, username);
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
    private void performLogin(String emailId, String password, final String username){

        if(emailId==null){
            Toast.makeText(Login.this, R.string.login_failed_username, Toast.LENGTH_SHORT).show();
            setInvisible();
            return;
        }

        //Firebase Authentication instance + built in function to sign in with Email and Password
        firebaseAuth.signInWithEmailAndPassword(emailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("uid", username);
                    editor.commit();
                    setInvisible();
                    // Go to Home activity
                    startActivity(new Intent(Login.this, Home.class));
                    finishAffinity();
                }else{
                    Toast.makeText(Login.this, R.string.login_fail_password, Toast.LENGTH_SHORT).show();
                    setInvisible();
                }
            }
        });
    }

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);

    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        loginView.setClickable(false);
    }
}