package com.example.santropolroulant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santropolroulant.DataValueTypes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.R.style.Theme_Holo_Light_Dialog_MinWidth;

public class PersonalSettings extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText prefInputFirstname, prefInputLastname, prefInputDOB,
            prefInputEmail, prefInputPhone;
    Button saveButton;
    DatePickerDialog.OnDateSetListener date;
    List<User> users;
    User myUser;
    String firstname, lastname, dob, email, phone;

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

        //Find UI elements from layout
        prefInputFirstname = findViewById(R.id.prefinput_personal_firstname);
        prefInputLastname = findViewById(R.id.prefinput_personal_lastname);
        prefInputDOB = findViewById(R.id.prefinput_birthday_dob);
        prefInputEmail = findViewById(R.id.prefinput_contact_email);
        prefInputPhone= findViewById(R.id.prefinput_contact_phone);
        saveButton = findViewById(R.id.ps_save);

        /*
           * This
         */
        ValueEventListener saveChangesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDS : dataSnapshot.getChildren()) {
                    User userCurr = userDS.getValue(User.class);
                    users.add(userCurr);
                }

                Log.i("User Email", user.getEmail());

                for (User userTemp : users) {
                    Log.i("Current Email", userTemp.getEmail());
                    if (userTemp.getEmail().equals(user.getEmail())) {
                        myUser = userTemp;
                    }
                }

                if (myUser != null) {
                    setHintStrings(
                            myUser.getFirst_name(),
                            myUser.getLast_name(),
                            myUser.getEmail(),
                            myUser.getPhone_number());
                    setHints();

                } else {
                    Log.e("User Selection", "User not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        users = new ArrayList<User>();
        mDatabase = FirebaseDatabase.getInstance().getReference("userSample");


        mDatabase.addValueEventListener(saveChangesListener);


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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean editFirstname, editLastname, editEmail, editPhone, editSettings;
                String inFirstname, inLastname, inEmail, inPhone;

                editFirstname = editLastname = editEmail = editPhone = editSettings = false;
                inFirstname = prefInputFirstname.getText().toString();
                inLastname = prefInputLastname.getText().toString();
                inEmail = prefInputEmail.getText().toString();
                inPhone = prefInputPhone.getText().toString();

                String key = myUser.getKey();

                ArrayList<Task> tasks = new ArrayList<Task>();

                //Check if user put any info into fields
                if(!inFirstname.equals("")){ editFirstname = true; }

                if(!inLastname.equals("")){ editLastname = true; }

                if(!inEmail.equals("")){ editEmail = true; }

                if(!inPhone.equals("")){ editPhone = true; }

                //if so, upload new info
                if(editFirstname || editLastname || editEmail || editPhone){
                    if(editFirstname){
                        tasks.add(mDatabase.child(key).child("first_name").setValue(inFirstname));
                    }

                    if(editLastname){
                        tasks.add(mDatabase.child(key).child("first_name").setValue(inLastname));
                    }

                    if(editEmail){
                        tasks.add(mDatabase.child(key).child("email").setValue(inEmail));
                    }

                    if(editPhone){
                        tasks.add(mDatabase.child(key).child("phone").setValue(inPhone));
                    }

                    prefInputFirstname.getText().clear();
                    prefInputLastname.getText().clear();
                    prefInputEmail.getText().clear();
                    prefInputPhone.getText().clear();

                    Toast.makeText(PersonalSettings.this, "Changes have been saved!", Toast.LENGTH_SHORT).show();
                } else { //if not, prompt user for input
                    Toast.makeText(PersonalSettings.this, "Please Change Something!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void setHintStrings(String firstname, String lastname, String email, String phone){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
    }

    private void setHints(){
        prefInputFirstname.setHint(myUser.getFirst_name());
        prefInputLastname.setHint(myUser.getLast_name());
        prefInputEmail.setHint(myUser.getEmail());
        prefInputPhone.setHint(myUser.getPhone_number());
    }

}

