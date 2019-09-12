package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import java.util.Calendar;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.FrameLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";

    private EditText userFirstName,userLastName, userPhoneNumber, userPassword, userEmail;
    private Button next_1, regButton, testBtn;
    private TextView userLogin, userBirthDate;
    private FirebaseAuth firebaseAuth;
    private View progressOverlay;
    private TextView username;
    private View createAccount;
    String first_name, last_name, phone_number , email, password, birth_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.INVISIBLE);

        createAccount = (View) findViewById(R.id.create_account);
        setupUIViews(); // function way to set up UI elements

        firebaseAuth = FirebaseAuth.getInstance();



       /* regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){ // validate function as condition
                    createAccount.setClickable(false);
                    setVisible();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                // if successful then enter user data into firebase
                                Task[] tasks = sendUserData();
                                if (tasks[0].isSuccessful() && tasks[1].isSuccessful() && tasks[2].isSuccessful() && tasks[3].isSuccessful() &&tasks[4].isSuccessful() && tasks[5].isSuccessful() && tasks[6].isSuccessful()){
                                    Toast.makeText(CreateAccount.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(CreateAccount.this, "Oops a monkey quit!", Toast.LENGTH_SHORT).show();
                                }
                                setInvisible();
                                createAccount.setClickable(true);
                                startActivity(new Intent(CreateAccount.this, MainActivity.class));

                            }else{
                                String s = task.getException().getMessage();
                                setInvisible();
                                createAccount.setClickable(true);
                                Toast.makeText(CreateAccount.this, s, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });*/

        next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                intent.putExtra("FIRST_NAME", first_name);
                intent.putExtra("LAST_NAME", last_name);
                intent.putExtra("BIRTH_DATE", birth_date);
                startActivity(intent);
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, MainActivity.class));
            }
        });

    }
    /*private void setupUIViewsSuccess(){
        loginRedirectButton = (Button)findViewById(R.id.go_to_login);
        String first_two_letters = last_name.substring(0,2).toLowerCase();
        String key = first_two_letters+phone_number;
        username = (TextView) findViewById(R.id.username_view);
        username.setText(key); //set text for text view
    }*/
    private void setupUIViews() {

        next_1 = (Button) findViewById(R.id.btn_next_about_you);

        userFirstName = (EditText) findViewById(R.id.etUserF_Name);
        userFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userLastName = (EditText) findViewById(R.id.etUserL_Name);
        userLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        /*userPassword = (EditText) findViewById(R.id.etUserPassword);
        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userPhoneNumber = (EditText) findViewById(R.id.etUserPhone);
        userPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        userEmail = (EditText) findViewById(R.id.etUserEmail);
        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        */
        /*userBirthDate = (EditText)findViewById(R.id.etUserBirthDate);
        userBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });*/
        //regButton = (Button) findViewById(R.id.btnRegister);

        userLogin = (TextView) findViewById(R.id.tvUserLogin);



        userBirthDate = (TextView) findViewById(R.id.etUserBirthDate);
        userBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateAccount.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);


                String date = month + "/" + day + "/" + year;
                userBirthDate.setText(date);
                birth_date = "" + year + month + day;
            }
        };


    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    // Boolean function to check if all info is entered
    private Boolean validate(){
        Boolean result = false;

        first_name = userFirstName.getText().toString().trim();
        last_name = userLastName.getText().toString().trim();
        birth_date = userBirthDate.getText().toString().trim();
        phone_number = userPhoneNumber.getText().toString().trim();
        password = userPassword.getText().toString().trim();
        email = userEmail.getText().toString().trim();

        if(first_name.isEmpty() || last_name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();

        }else{
            result = true;
        }
        return  result;
    }


    private Task[] sendUserData(){
        //Custom UserID (key) is first two letters of last name + phonenumber
        String first_two_letters = last_name.substring(0,2).toLowerCase();
        String key = first_two_letters+phone_number;

        //Record date of signup
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        String formattedDate = df.format(c);
        Task[] tasks = new Task[7];
        // Write Statement
        // Call DatabaseReference
        // Specify the Children --> user --> UserID(key) --> ____ --> setValue
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        tasks[0] = myRef.child("user").child(key).child("first_name").setValue(first_name);
        tasks[1] = myRef.child("user").child(key).child("last_name").setValue(last_name);
        tasks[2] = myRef.child("user").child(key).child("birth_date").setValue(birth_date);
        tasks[3] = myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        tasks[4] = myRef.child("user").child(key).child("email").setValue(email);
        tasks[5] = myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        tasks[6] = myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users

        myRef.child("user").child(key).child("first_name").setValue(first_name);
        myRef.child("user").child(key).child("last_name").setValue(last_name);
        myRef.child("user").child(key).child("birth_date").setValue(birth_date);
       //myRef.child("user").child(key).child("phone_number").setValue(phone_number);
        //myRef.child("user").child(key).child("email").setValue(email);
        //myRef.child("user").child(key).child("signup_date").setValue(formattedDate);
        //myRef.child("user").child(key).child("key").setValue(firebaseAuth.getUid()); //this is the firebase's UID for the users


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
