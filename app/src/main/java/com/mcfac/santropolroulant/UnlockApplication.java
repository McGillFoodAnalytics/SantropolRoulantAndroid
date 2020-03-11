package com.mcfac.santropolroulant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcfac.santropolroulant.R;

import java.util.Locale;


public class UnlockApplication extends AppCompatActivity{
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private TextView accessCodeView;
    private SharedPreferences setKeys;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    private ImageView logo;
    private TextView welcome, description1, description2, loginRedirect;
    private EditText codeInput;
    private Button enterCodeButton;
    private LinearLayout infoSessionBox;

    String accessCode;
    String inputCode;
    Boolean isUnlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        setupUIViews();
        loadLocale();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        //if(user == null){
        //    System.exit(-1);
        //}

        /*TO SET BACK TO LOCKED*/
        /*SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("unlocked", false);
        editor.apply();*/


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        isUnlocked = prefs.getBoolean("unlocked", false);//"No name defined" is the default value.

        if(isUnlocked == true){
            startActivity(new Intent(UnlockApplication.this, MainActivity.class));
            finish();
        }


        //If it still locked then get the access code from database
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("registration_code").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String access_code = snapshot.getValue().toString();

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("code", access_code);
                editor.apply();
                // Log.d("abc123", accessCodeData);


                //setKeys= getSharedPreferences("com.example.santropolroulant", Context.MODE_PRIVATE);
                //editKeys = setKeys.edit();
                //editKeys.putString( "AccessCodeData", accessCodeData);
                //editKeys.apply();

                //dataStorage.setAccessCode(accessCodeData);
                //accessCodeView.setText(accessCodeData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        enterCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("unlocked", true);
                    editor.apply();
                    startActivity(new Intent(UnlockApplication.this, MainActivity.class));
                    finish();
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(UnlockApplication.this, Login.class));
            }

        });

       //accessCode = dataStorage.getAccessCode();


    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);

        Locale.setDefault(locale);

        Configuration config = new Configuration();

        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences( "Settings", MODE_PRIVATE).edit();

        editor.putString("My_Lang", lang);

        editor.apply();

    }

    public void loadLocale() {

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);

        String language = prefs.getString("My_Lang", "");

        setLocale(language);
    }

    private void setupUIViews() {
        //TODO:Determine what R.id.accessCode is, and why isn't it working anymore
        //accessCodeView = (TextView) findViewById(R.id.accessCode);
        logo = (ImageView) findViewById(R.id.unlock_logo);
        welcome = (TextView) findViewById(R.id.unlock_welcome);
        description1 = (TextView) findViewById(R.id.unlock_description1a);
        description2 = (TextView) findViewById(R.id.unlock_description2a);

        codeInput = (EditText) findViewById(R.id.code_input);
        codeInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        enterCodeButton = (Button) findViewById(R.id.enter_code_button);
        loginRedirect = (TextView) findViewById(R.id.unlock_login);


    }


    private class DataStorage {
        private String access;

        public DataStorage(){

        }
        public DataStorage(String code){
            this.access = code;
        }

        public void setAccessCode(String code) {
            access = code;
            Log.d("TheCode", access);
        }

        public String getAccessCode() {
            return access;
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private Boolean validate(){
        Boolean result = false;

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        accessCode = prefs.getString("code", "nocode");
        Log.d("pleasebeban", accessCode);

        inputCode = codeInput.getText().toString().trim();

        if(inputCode.isEmpty()){
            Toast.makeText(this, R.string.enter_a_code, Toast.LENGTH_SHORT).show();
        }else if (!(accessCode.equals(inputCode)) && (!inputCode.isEmpty())){
            Toast.makeText(this, R.string.invalid_code, Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return  result;
    }
}
