package com.example.santropolroulant;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Address, City Postal Code,

//Phone, email, password, password
public class CreateAccount3 extends AppCompatActivity {

    private Button regButton;
    String first_name, last_name, birth_date, address_street, address_number, city, postal_code, email, password, confPassword, phone_number;
    private EditText userEmail, userPassword, userConfPassword, userPhoneNumber;
    private FirebaseAuth firebaseAuth;
    private View progressOverlay;
    private View createAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_3);
        createAccount = (View) findViewById(R.id.create_account_3);


        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.INVISIBLE);

        setupUIViews(); // function way to set up UI elements
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            first_name = extras.getString("FIRST_NAME"); // retrieve the data using keyName
            last_name = extras.getString("LAST_NAME");
            birth_date = extras.getString("BIRTH_DATE");
            address_street = extras.getString("ADDRESS_STREET"); // retrieve the data using keyName
            address_number = extras.getString("ADDRESS_NUMBER");
            city = extras.getString("CITY");
            postal_code = extras.getString("POSTAL_CODE");
        }


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("start", "fuck");
                if(validate()){ // validate function as condition
                    createAccount.setClickable(false);
                    setVisible();
                    Log.d("wtv", "onDateSet: mm/dd/yyy: ");
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           Log.d("wtv", "FUCK");

                            if(task.isSuccessful()){
                                // if successful then enter user data into firebase
                                Task[] tasks = sendUserData();

                                //if (tasks[0].isSuccessful() && tasks[1].isSuccessful() && tasks[2].isSuccessful() && tasks[3].isSuccessful() &&tasks[4].isSuccessful() && tasks[5].isSuccessful() && tasks[6].isSuccessful() && tasks[7].isSuccessful() && tasks[8].isSuccessful()){
                                    Toast.makeText(CreateAccount3.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    setInvisible();
                                    createAccount.setClickable(true);

                                    startActivity(new Intent(CreateAccount3.this, Login.class));
                                /*}
                                else{
                                    Toast.makeText(CreateAccount3.this, "Oops a monkey quit!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateAccount3.this, CreateAccount.class));
                                    setInvisible();
                                    createAccount.setClickable(true);
                                    startActivity(new Intent(CreateAccount3.this, CreateAccount.class));
                                }*/

                            }else{
                                String s = task.getException().getMessage();
                                Toast.makeText(CreateAccount3.this, s, Toast.LENGTH_SHORT).show();
                                setInvisible();
                                createAccount.setClickable(true);
                                startActivity(new Intent(CreateAccount3.this, MainActivity.class));

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

    private Boolean validate(){
        Boolean result = false;

        email = userEmail.getText().toString().trim();
        phone_number = userPhoneNumber.getText().toString().trim();
        password = userPassword.getText().toString().trim();
        confPassword = userConfPassword.getText().toString().trim();
        if(email.isEmpty() || phone_number.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else{
            result = true;
        }
        return  result;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
   /*
    private Task[] sendUserData2(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();

        Task[] tasks = new Task[1];
        tasks[0] = myRef.child("user").child(key).child("first_name").setValue(first_name);
    }
    */

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

        Task[] tasks = new Task[11];
        // Write Statement
        // Call DatabaseReference
        // Specify the Children --> user --> UserID(key) --> ____ --> setValue
        Log.d("sendingUserData",first_name + "before3 " + last_name);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Log.d("sendingUserData",first_name + "before2 " + last_name);

        DatabaseReference myRef = firebaseDatabase.getReference();
        Log.d("myRed", myRef.toString() + " " + myRef.getKey());
        Log.d("sendingUserData",first_name + "before " + last_name);
        myRef.child("user").setValue(key);
        myRef.child("user").child(key).setValue("first_name");
        myRef.child("user").child(key).setValue("last_name");
        myRef.child("user").child(key).setValue("dob");
        myRef.child("user").child(key).setValue("phone_number");
        myRef.child("user").child(key).setValue("email");
        myRef.child("user").child(key).setValue("signup_date");
        myRef.child("user").child(key).setValue("address_city");
        myRef.child("user").child(key).setValue("address_number");
        myRef.child("user").child(key).setValue("address_street");
        myRef.child("user").child(key).setValue("address_postal_code");
        myRef.child("user").child(key).setValue("key");

        tasks[0] = myRef.child("user").child(key).child("first_name").setValue(first_name);
        Log.d("sendingUserData0", String.valueOf(tasks[0].getResult()));

        tasks[1] = myRef.child("user").child(key).child("last_name").setValue(last_name);
        Log.d("sendingUserData1", String.valueOf(tasks[1].isSuccessful()));

        tasks[2] = myRef.child("user").child(key).child("dob").setValue(birth_date);
        Log.d("sendingUserData2", String.valueOf(tasks[2].isSuccessful()));

        tasks[3] = myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        Log.d("sendingUserData3", String.valueOf(tasks[3].isSuccessful()));

        tasks[4] = myRef.child("user").child(key).child("email").setValue(email);
        Log.d("sendingUserData4", String.valueOf(tasks[4].isSuccessful()));

        tasks[5] = myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        Log.d("sendingUserData5", String.valueOf(tasks[5].isSuccessful()));

        tasks[6] = myRef.child("user").child(key).child("address_city").setValue(city);
        Log.d("sendingUserData6", String.valueOf(tasks[6].isSuccessful()));

        tasks[7] = myRef.child("user").child(key).child("address_number").setValue(address_number_int);
        Log.d("sendingUserData6", String.valueOf(tasks[6].isSuccessful()));

        tasks[8] = myRef.child("user").child(key).child("address_street").setValue(address_street);
        Log.d("sendingUserData6", String.valueOf(tasks[6].isSuccessful()));

        tasks[9] = myRef.child("user").child(key).child("address_postal_code").setValue(postal_code);
        Log.d("sendingUserData7", String.valueOf(tasks[7].isSuccessful()));

        tasks[10] = myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users
        Log.d("sendingUserData8", String.valueOf(tasks[8].isSuccessful()));

        Log.d("sendingUserData",first_name + " " + last_name);


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