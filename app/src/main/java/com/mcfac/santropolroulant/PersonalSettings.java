package com.mcfac.santropolroulant;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.Query;
import com.mcfac.santropolroulant.FirebaseClasses.Event;
import com.mcfac.santropolroulant.FirebaseClasses.User;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcfac.santropolroulant.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.style.Theme_Holo_Light_Dialog_MinWidth;
import static android.graphics.Color.BLACK;

public class PersonalSettings extends AppCompatActivity {
    private ArrayList<InputField> inputFields;
    private Button saveButton;
    private User myUser;
    private String uid;
    private ValueEventListener saveChangesListener;
    private Query eventQuery;
    private ValueEventListener eventListener;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase, newMDatabase, userDatabase, eventRef;


    private final String USER_LOC = MainActivity.USER_LOC;
    private final String EVENT_LOC = MainActivity.EVENT_LOC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
        inputFields = new ArrayList<>();

        //Ensuring we are logged in to firebase
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        // Auto login for signed in user - Commented out below

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final String uid = pref.getString("uid", "notFound");

        //Pointing reference to the users in the database
       // mDatabase = FirebaseDatabase.getInstance().getReference(USER_LOC + "/" + uid);
        mDatabase = FirebaseDatabase.getInstance().getReference(USER_LOC).child(uid);
        eventRef = FirebaseDatabase.getInstance().getReference(EVENT_LOC);


        //Find UI elements from layout
        //each InputField takes the editText from the layout, the name of the field in the database, and the User method which gets that value from a User object

        try {
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_personal_firstname), "first_name", User.class.getDeclaredMethod("getFirst_name")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_personal_lastname), "last_name", User.class.getDeclaredMethod("getLast_name")));
            inputFields.add(new DateField((EditText) findViewById(R.id.prefinput_birthday_dob), "dob", User.class.getDeclaredMethod("getDob")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_contact_email), "email", User.class.getDeclaredMethod("getEmail")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_contact_phone), "phone_number", User.class.getDeclaredMethod("getPhone_number")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_line), "address_street", User.class.getDeclaredMethod("getAddress_street")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_city), "address_city", User.class.getDeclaredMethod("getAddress_city")));
            inputFields.add(new InputField((EditText) findViewById(R.id.prefinput_address_postal), "address_postal", User.class.getDeclaredMethod("getAddress_postal_code")));
            inputFields.add(new NonEditableInputField((EditText) findViewById(R.id.prefinput_username_username), "key", User.class.getDeclaredMethod("getUid")));
        }catch(NoSuchMethodException e){
            e.printStackTrace();
            System.exit(-1);
        }

        saveButton = findViewById(R.id.ps_save);

        //This is the event listener which will get all the UseR from the database.
        saveChangesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(!dataSnapshot.exists()){
                    Log.e("User Selection", "User not Found");
                    Redirect.redirectToLogin(PersonalSettings.this, firebaseAuth);
                } else {
                    myUser = dataSnapshot.getValue(User.class);
                    myUser.setUid(dataSnapshot.getKey());
                    for(int i = 0; i < inputFields.size(); i++){
                        inputFields.get(i).setHint(myUser);
                    }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("User Selection", "User not Found");
            }

        };

        //Setting it to be called everytime the database is updated, (and of course once on creation)
        saveChangesListener = mDatabase.addValueEventListener(saveChangesListener);

        //Occurs when save button is selected
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key = myUser.getKey();

                //A task is a call to set a value in firebase
                ArrayList<Task> tasks = new ArrayList<Task>();

                //Have any of the values been changed? Is the text not ""?
                boolean editSettings = false;
                boolean usernameChange = false;
                boolean emailChange = false;
                String last_name = "";
                String phone_num = "";
                String newemail = "";

                //If atleast one of the EditTexts have been changed, we want to tell firebase we
                //have new info.
                for(int i = 0; i < inputFields.size(); i++){
                    editSettings = editSettings || inputFields.get(i).hasChanged();

                    if(inputFields.get(i).hasChanged()){
                        usernameChange = usernameChange || inputFields.get(i).getDbReference().contains("last_name") || inputFields.get(i).getDbReference().contains("phone_number");
                        emailChange = emailChange || inputFields.get(i).getDbReference().contains("email");
                        if (inputFields.get(i).getDbReference().contains("email")){
                            newemail = inputFields.get(i).getText();
                        }
                    }

                    if(inputFields.get(i).getDbReference().contains("last_name")){
                        last_name = myUser.getLast_name();
                    }

                    if(inputFields.get(i).getDbReference().contains("phone_number")){
                        phone_num = myUser.getPhone_number();
                    }
                }

                if( editSettings ) {
                    for(int i = 0; i < inputFields.size(); i++){
                        String dbEntry = inputFields.get(i).getDbReference();
                        String fieldText = inputFields.get(i).getText();
                        // If this field has been changed
                        if(inputFields.get(i).hasChanged()){
                            tasks.add(mDatabase.child(dbEntry).setValue(fieldText));
                        }
                    }

                    //If username needs to change
                    if(usernameChange){

                        for(int i = 0; i < inputFields.size(); i++){
                            if(inputFields.get(i).hasChanged() && inputFields.get(i).getDbReference().contains("last_name")) {
                                last_name = inputFields.get(i).getText();
                            }
                            if(inputFields.get(i).hasChanged() && inputFields.get(i).getDbReference().contains("phone_number")) {
                                phone_num = inputFields.get(i).getText();
                            }
                            inputFields.get(i).clearText();
                        }
                        updateUsername(phone_num, last_name);
                    }
                    if(emailChange){
                        updateEmail(newemail);
                    }

                    Toast.makeText(PersonalSettings.this, R.string.changes_saved, Toast.LENGTH_SHORT).show();
                } else { //if not, prompt user for input
                    Toast.makeText(PersonalSettings.this, R.string.change_smth, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null) {
            if(saveChangesListener != null){
                mDatabase.removeEventListener(saveChangesListener);
            }
        }
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

        public String getText(){
            return editText.getText().toString();
        }

        //Clear the text in the UI
        public void clearText(){
            editText.getText().clear();
        }

        //Applies the User method we obtained on creation on the given User parameter (myUser)
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

        public Method getGetHint() {
            return getHint;
        }

        public boolean hasChanged(){
            return !editText.getText().toString().equals("");
        }
    }

    //Method dealing with updates to their phone number or last name which then changes the username
    private void updateUsername(String phone, String lastName){
        String first_two_letters = lastName.substring(0,2).toLowerCase();
        String newKey = first_two_letters + phone;

        SharedPreferences.Editor editor = getSharedPreferences( "MyPref", 0).edit();
        editor.putString("uid", newKey);
        editor.apply();

        //Creating new key
        userDatabase = FirebaseDatabase.getInstance().getReference(USER_LOC);
        userDatabase.child(newKey).child("last_name").setValue("test");//arbitrary setup of key

        newMDatabase = FirebaseDatabase.getInstance().getReference(USER_LOC).child(newKey);
        String oldUsername = mDatabase.getKey();
        copyRecord(mDatabase, newMDatabase);


        mDatabase.removeEventListener(saveChangesListener);
        saveChangesListener = newMDatabase.addValueEventListener(saveChangesListener);

        mDatabase.removeValue();
        mDatabase = newMDatabase;
        updateEvents(oldUsername, newKey);

    }

    //This method deals with email changes, requiring re-authentication as each email has its own encrypted key.
    private void updateEmail(String newemail){
        // Get auth credentials from the user for re-authentication
        //prompt user for password
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Please re-enter your password");
        alert.setTitle("Password");

        alert.setView(edittext);

        alert.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String password = edittext.getText().toString();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(firebaseAuth.getCurrentUser().getEmail(), password); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                firebaseAuth.getCurrentUser().reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                Log.d("OK", "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\
                                firebaseAuth.getCurrentUser().updateEmail(newemail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("OK", "User email address updated.");
                                                    dialog.cancel();
                                                }
                                                else{
                                                }
                                            }
                                        });
                                }
                                else{
                                }
                            }
                        });
            }
        });

        alert.setNegativeButton("Cancel Changes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.cancel();
            }
        });

        alert.show();



    }

    //This method is used when a username is changed.  Since when a username changes, a new user must be made
    //in the User database and thus must copy the records from the old username
    private void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d("copy", "Success!");
                        } else {
                            Log.d("copy", "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    private void updateEvents(String oldUsername, String newUsername){ //String firstName, String lastName, String oldUid, String newUid
        eventQuery = FirebaseDatabase.getInstance().getReference(MainActivity.EVENT_LOC)
                .orderByChild("uid")
                .equalTo(oldUsername);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot scheduleSnap : dataSnapshot.getChildren()) {
                    Event curEvent = scheduleSnap.getValue(Event.class);
                    Log.d("currentEvent", curEvent.getUid());
                    updateEventUid(scheduleSnap.getKey(), newUsername);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        };
        eventQuery.addListenerForSingleValueEvent(eventListener);

    }
    private void updateEventUid(String key, String newUsername){
        Log.d("updateEventUid", key);
        eventRef.child(key).child("uid").setValue(newUsername);
    }

    private class NonEditableInputField extends InputField{
        public NonEditableInputField(EditText editText, String dbReference, Method getHint) {
            super(editText, dbReference, getHint);
            editText.setEnabled(false);
        }

        @Override
        public boolean hasChanged() {
            return false;
        }
    }
    private class DateField extends InputField{
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;
        private Date originalDate;
        private String displayFormat = "dd-MM-yyyy";
        private String databaseFormat = "yyyyMMdd";

        public DateField(EditText editText, String dbReference, Method getHint){
            super(editText, dbReference, getHint);

            //This sets the appearance of the DatePicker
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

            //updates the date saved in myCalendar, then calls update label, which changes the date
            //that appears in text field
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

        @Override
        public void setHint(User myUser) {
            SimpleDateFormat formatter = new SimpleDateFormat(databaseFormat, Locale.ENGLISH);
            try {
                originalDate = formatter.parse((String) getGetHint().invoke(myUser));
                myCalendar.setTime(originalDate);
                updateLabel();
            } catch (IllegalAccessException e){
                e.printStackTrace();
                System.exit(-1);
            } catch (InvocationTargetException e){
                e.printStackTrace();
                System.exit(-1);
            } catch (ParseException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }

        private void updateLabel() {//In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(displayFormat, Locale.US);

            getEditText().setText(sdf.format(myCalendar.getTime()));
        }

        @Override
        public String getText() {
            SimpleDateFormat dbFormatter = new SimpleDateFormat(databaseFormat, Locale.US);
            return dbFormatter.format(myCalendar.getTime());
        }

        @Override
        public boolean hasChanged() {
            return !myCalendar.getTime().equals(originalDate);
        }
    }
}