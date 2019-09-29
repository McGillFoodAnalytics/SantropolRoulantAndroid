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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import static android.R.style.Theme_Holo_Light_Dialog_MinWidth;

public class PersonalSettings extends AppCompatActivity {
    ArrayList<InputField> inputFields;
    Button saveButton;
    DatePickerDialog.OnDateSetListener date;
    List<User> users;
    User myUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    //private PreferenceGroup[] settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);

        inputFields = new ArrayList<>();
        users = new ArrayList<User>();


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        if(user == null){
            System.exit(-1);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("userSample");

        //Find UI elements from layout
        try {
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_personal_firstname), "first_name", User.class.getDeclaredMethod("getFirst_name")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_personal_lastname), "last_name", User.class.getDeclaredMethod("getLast_name")));
            //inputFields.add(new DateField((EditText) findViewById(R.id.prefinput_birthday_dob), "dob", User.class.getDeclaredMethod("getDob")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_contact_email), "email", User.class.getDeclaredMethod("getEmail")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_contact_phone), "phone", User.class.getDeclaredMethod("getPhone_number")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_line), "address_street", User.class.getDeclaredMethod("getAddress_street")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_city), "address_city", User.class.getDeclaredMethod("getAddress_city")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_postal), "address_postal", User.class.getDeclaredMethod("getAddress_postal_code")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_username_username), "key", User.class.getDeclaredMethod("getKey")));
        }catch(NoSuchMethodException e){
            e.printStackTrace();
            System.exit(-1);
        }

        saveButton = findViewById(R.id.ps_save);

        /*
           * This is the event listener which will
         */

        ValueEventListener saveChangesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userDS : dataSnapshot.getChildren()) {
                    User userCurr = userDS.getValue(User.class);
                    users.add(userCurr);
                }

                for (User userTemp : users) {
                    if (userTemp.getEmail().equals(user.getEmail())) {
                        myUser = userTemp;
                    }
                }

                if (myUser != null) {
                    for(int i = 0; i < inputFields.size(); i++){
                        inputFields.get(i).setHint(myUser);
                    }
                } else {
                    Log.e("User Selection", "User not Found");
                    System.exit(-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        //Setting it to be called everytime the database is updated, (and of course once on creation)
        mDatabase.addValueEventListener(saveChangesListener);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = myUser.getKey();

                ArrayList<Task> tasks = new ArrayList<Task>();

                //Have any of the values been changed? Is the text not ""?
                boolean editSettings = false;

                for(int i = 0; i < inputFields.size(); i++){
                    if(!inputFields.get(i).getEditText().getText().toString().equals("")){
                        editSettings = true;
                    }
                }

                if( editSettings ) {
                    for(int i = 0; i < inputFields.size(); i++){
                        String dbEntry = inputFields.get(i).getDbReference();
                        String fieldText = inputFields.get(i).getEditText().getText().toString();
                        if(!fieldText.equals("")){
                            tasks.add(mDatabase.child(key).child(dbEntry).setValue(fieldText));
                        }
                        inputFields.get(i).clearText();
                    }
                    Toast.makeText(PersonalSettings.this, "Changes have been saved!", Toast.LENGTH_SHORT).show();
                } else { //if not, prompt user for input
                    Toast.makeText(PersonalSettings.this, "Please Change Something!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private class InputField{
        private EditText editText;
        private String dbReference;
        private Method getHint;


        public InputField(EditText editText, String dbReference, Method getHint){
            this.editText = editText;
            this.dbReference = dbReference;
            this.getHint = getHint;
        }

        public void clearText(){
            editText.getText().clear();
        }

        public void setHint(User myUser){
            try {
                editText.setHint((String)getHint.invoke(myUser));
            } catch (IllegalAccessException e){
                e.printStackTrace();
                System.exit(-1);
            } catch (InvocationTargetException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }

        public void setEditText(EditText editText) {
            this.editText = editText;
        }

        public void setDbReference(String dbReference) {
            this.dbReference = dbReference;
        }

        public EditText getEditText() {
            return editText;
        }

        public String getDbReference() {
            return dbReference;
        }
    }

    private class DateField extends InputField{
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;
        public DateField(EditText editText, String dbReference, Method getHint){
            super(editText, dbReference, getHint);
            editText.setOnClickListener(new View.OnClickListener(){
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

            date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };
        }
        private void updateLabel() {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            getEditText().setText(sdf.format(myCalendar.getTime()));
        }


    }
}

