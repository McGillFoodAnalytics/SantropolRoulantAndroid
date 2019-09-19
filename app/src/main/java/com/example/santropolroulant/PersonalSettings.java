package com.example.santropolroulant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalSettings extends AppCompatActivity {

    //private PreferenceGroup[] settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
    }

    /*private enum Type {
        TELEPHONE,
        EMAIL,

    }

    private class Preference<E>{


    }

    private class PreferenceGroup{
        public String title;
        public Preference[] preferences;
        PreferenceGroup(String title, Preference[] preferences){
            this.title = title;
            this.preferences = preferences;
        }
    }*/
}
