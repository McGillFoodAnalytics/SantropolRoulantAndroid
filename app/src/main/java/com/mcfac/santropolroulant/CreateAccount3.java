package com.mcfac.santropolroulant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcfac.santropolroulant.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//This class is for the final activity which asks for user information
public class CreateAccount3 extends AppCompatActivity {

    private Button regButton;
    String first_name, last_name, birth_date, address_street, address_number, city, postal_code, email, password, confPassword, phone_number;
    private EditText userEmail, userPassword, userConfPassword, userPhoneNumber;
    private FirebaseAuth firebaseAuth;
    private View progressOverlay;
    private View createAccount;

    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        createAccount = (View) findViewById(R.id.create_account_3);


        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.INVISIBLE);

        setupUIViews(); // function way to set up UI elements
        firebaseAuth = FirebaseAuth.getInstance();

        //Retrieving info from CreateAccount and CreateAccount2
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            first_name = extras.getString("FIRST_NAME");
            last_name = extras.getString("LAST_NAME");
            birth_date = extras.getString("BIRTH_DATE");
            address_street = extras.getString("ADDRESS_STREET");
            address_number = extras.getString("ADDRESS_NUMBER");
            city = extras.getString("CITY");
            postal_code = extras.getString("POSTAL_CODE");
        }

        //Listener for registration button which create the user in firebase with their email and password
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){ // validate function as condition
                    createAccount.setClickable(false);
                    setVisible();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        //Sending user to CreateAccountFinish where they will see their username
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                // if successful then entersendUser user data into firebase
                                Task[] tasks = sendUserData();

                                //if (tasks[0].isSuccessful() && tasks[1].isSuccessful() && tasks[2].isSuccessful() && tasks[3].isSuccessful() &&tasks[4].isSuccessful() && tasks[5].isSuccessful() && tasks[6].isSuccessful() && tasks[7].isSuccessful() && tasks[8].isSuccessful()){
                                Toast.makeText(CreateAccount3.this, R.string.reg_success, Toast.LENGTH_SHORT).show();
                                setInvisible();
                                createAccount.setClickable(true);

                                Intent intent = new Intent(CreateAccount3.this, CreateAccountFinish.class);
                                intent.putExtra("LAST_NAME", last_name);
                                intent.putExtra("PHONE_NUMBER", phone_number);
                                startActivity(intent);

                            }else{
                                String s = task.getException().getMessage();
                                Toast.makeText(CreateAccount3.this, s, Toast.LENGTH_SHORT).show();

                                setInvisible();
                                createAccount.setClickable(true);

                            }

                        }
                    });
                }
            }
        });


    }

    private void setupUIViews() {
        regButton = (Button) findViewById(R.id.register);

        userEmail = (EditText) findViewById(R.id.etUser_Email);
        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userPhoneNumber = (EditText) findViewById(R.id.etUser_Phone_Number);
        userPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userPassword = (EditText) findViewById(R.id.etUser_Password);
        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userConfPassword = (EditText) findViewById(R.id.etUser_Conf_Password);
        userConfPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    //Method returning Boolean to validate input fields
    private Boolean validate(){
        Boolean result = false;

        email = userEmail.getText().toString().trim();
        phone_number = userPhoneNumber.getText().toString().trim().replaceAll("[^0-9]", "");
        password = userPassword.getText().toString().trim();
        confPassword = userConfPassword.getText().toString().trim();
        if(email.isEmpty() || phone_number.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
            Toast.makeText(this, R.string.enter_details, Toast.LENGTH_SHORT).show();

        }else if (!password.equals(confPassword)){
            Toast.makeText(this, R.string.passwords_match, Toast.LENGTH_SHORT).show();

        }
        else{
            result = true;
        }
        return  result;
    }

    //Method to hide keyboard for better UX
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //Method which sends all the user's info to the User database
    private Task[] sendUserData(){

        //Custom UserID (key) is first two letters of last name + phonenumber
        String first_two_letters = last_name.substring(0,2).toLowerCase();
        String key = first_two_letters+phone_number;
        Log.d("sendingUserData",first_name + "start " + last_name);

        //Record date of signup
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        String formattedDate = df.format(c);

        int address_number_int = Integer.parseInt(address_number);

        Task[] tasks = new Task[12];
        // Write Statement
        // Call DatabaseReference
        // Specify the Children --> user --> UserID(key) --> ____ --> setValue

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference myRef = firebaseDatabase.getReference();

        //Setting values to the user's endpoint in User database
        tasks[0] = myRef.child(USER_LOC).child(key).child("first_name").setValue(first_name);

        tasks[1] = myRef.child(USER_LOC).child(key).child("last_name").setValue(last_name);

        tasks[2] = myRef.child(USER_LOC).child(key).child("dob").setValue(birth_date);

        tasks[3] = myRef.child(USER_LOC).child(key).child("phone_number").setValue(phone_number);

        tasks[4] = myRef.child(USER_LOC).child(key).child("email").setValue(email);

        tasks[5] = myRef.child(USER_LOC).child(key).child("signup_date").setValue(formattedDate);

        tasks[6] = myRef.child(USER_LOC).child(key).child("address_city").setValue(city);

        tasks[7] = myRef.child(USER_LOC).child(key).child("address_number").setValue(address_number_int);

        tasks[8] = myRef.child(USER_LOC).child(key).child("address_street").setValue(address_street);

        tasks[9] = myRef.child(USER_LOC).child(key).child("address_postal_code").setValue(postal_code);

        tasks[10] = myRef.child(USER_LOC).child(key).child("no_show").setValue(0);

        tasks[11] = myRef.child(USER_LOC).child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users

        return tasks;
    }

    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);

    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        createAccount.setClickable(false);
    }


}