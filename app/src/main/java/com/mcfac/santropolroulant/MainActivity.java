package com.mcfac.santropolroulant;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;

    private FirebaseAuth firebaseAuth;
    private TextView timerView;

    public static final String EVENT_LOC = "event";
    public static final String USER_LOC = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main_page); //Designates which layout XML to be used for this page


        // Setting up UI
        loginButton = (Button) findViewById(R.id.Login_Button);
        signupButton = (Button) findViewById(R.id.Create_Account_Button);
        timerView = (TextView) findViewById(R.id.timerView);



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

        CountUpTimer timer = new CountUpTimer(500000) {
            public void onTick(int second) {
                if(second != -1)
                    timerView.setText(String.valueOf(second));
                else
                    timerView.setText(getEmojiByUnicode(0x221E));
            }
        };

        timer.start();


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

    //Restricting going back to unlock application
    @Override
    public void onBackPressed() {
        return;
    }

    //Abstract class for timer
    public abstract class CountUpTimer extends CountDownTimer {
        private static final long INTERVAL_MS = 500;
        private final long duration;

        protected CountUpTimer(long durationMs) {
            super(durationMs, INTERVAL_MS);
            this.duration = durationMs;
        }

        public abstract void onTick(int second);

        @Override
        public void onTick(long msUntilFinished) {
            int second = (int) ((duration - msUntilFinished) / 500);
            onTick(second);
        }

        @Override
        public void onFinish() {
            // onTick(duration / 500);
            onTick(-1);
        }
    }

    //Method to return emoji encoding based on unicode
    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}