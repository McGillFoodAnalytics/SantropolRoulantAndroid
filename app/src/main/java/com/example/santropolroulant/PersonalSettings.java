package com.example.santropolroulant;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santropolroulant.DataValueTypes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.style.Theme_Holo_Light_Dialog_MinWidth;

public class PersonalSettings extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText prefInputFirstname, prefInputLastname, prefInputDOB,
            prefInputEmail, prefInputPhone;
    DatePickerDialog.OnDateSetListener date;
    List<User> users;
    User myUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        prefInputDOB.setText(sdf.format(myCalendar.getTime()));
    }
    //private PreferenceGroup[] settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);



        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        if(user == null){
            System.exit(-1);
        }

        prefInputFirstname = findViewById(R.id.prefinput_personal_firstname);
        prefInputLastname = findViewById(R.id.prefinput_personal_lastname);
        prefInputDOB = findViewById(R.id.prefinput_birthday_dob);
        prefInputEmail = findViewById(R.id.prefinput_contact_email);
        prefInputPhone= findViewById(R.id.prefinput_contact_phone);

        users = new ArrayList<User>();
        mDatabase = FirebaseDatabase.getInstance().getReference("userSample");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDS : dataSnapshot.getChildren()){
                    User userCurr = userDS.getValue(User.class);
                    users.add(userCurr);
                }

                Log.i("User Email", user.getEmail());

                for(User userTemp : users){
                    Log.i("Current Email", userTemp.getEmail());
                    if (userTemp.getEmail().equals(user.getEmail())){
                        myUser = userTemp;
                    }
                }

                if(myUser != null){
                    prefInputFirstname.setHint(myUser.getFirst_name());
                    prefInputLastname.setHint(myUser.getLast_name());
                    prefInputEmail.setHint(myUser.getEmail());
                    prefInputPhone.setHint(myUser.getPhone_number());
                } else {
                    Log.e("User Selection", "User not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //prefInputFirstname.setHint();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        prefInputDOB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        PersonalSettings.this,
                        Theme_Holo_Light_Dialog_MinWidth,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
