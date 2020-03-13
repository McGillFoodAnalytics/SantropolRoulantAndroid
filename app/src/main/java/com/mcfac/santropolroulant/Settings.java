package com.mcfac.santropolroulant;

import android.app.Activity;
import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    private Button personalSettingsBtn;
    private Button languageBtn;
    private Button logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();      // Ensuring we are currently logged in
        if(user==null){
            Redirect.redirectToLogin(Settings.this, mAuth);
        }

        personalSettingsBtn = (Button) findViewById(R.id.btnMyProfile);
        languageBtn = (Button) findViewById(R.id.btnLang);
        logoutBtn = (Button) findViewById(R.id.btnLogout);

        personalSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click, run validate function*
                startActivity(new Intent(Settings.this, PersonalSettings.class));
            }
        });

        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On Click, run validate function*
                logout();
            }
        });


    }
    @Override
    public void onBackPressed() {
        //finishAffinity();
        startActivity(new Intent(Settings.this, Home.class));
    }

    private void logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Settings.this, MainActivity.class));
    }

    private void showChangeLanguageDialog() {

        final String[] listItems = {getString(R.string.english), getString(R.string.french)};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(Settings.this);
        mbuilder.setTitle("Choose Language...");
        mbuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i == 0) {
                    setLocale("en");
                    finishAffinity();
                    startActivity(new Intent(Settings.this, Settings.class));
                   // recreate();
                } else if (i == 1) {
                    setLocale("fr");
                    finishAffinity();
                    startActivity(new Intent(Settings.this, Settings.class));
                  //  recreate();
                }

                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mbuilder.create();
        mDialog.show();

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

    @Override
    public void finishAffinity() {
        super.finishAffinity();
        overridePendingTransition(0, 0);
    }

}
