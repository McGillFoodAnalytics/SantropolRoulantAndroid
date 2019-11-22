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

    private EditText userFirstName,userLastName;
    private Button next_1;
    private TextView userLogin, userBirthDate;

    String first_name, last_name, birth_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        setupUIViews(); // function way to set up UI elements


        next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    Intent intent = new Intent(CreateAccount.this, CreateAccount2.class);
                    intent.putExtra("FIRST_NAME", first_name);
                    intent.putExtra("LAST_NAME", last_name);
                    intent.putExtra("BIRTH_DATE", birth_date);
                    startActivity(intent);
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccount.this, MainActivity.class));
            }
        });

    }

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
        String[] birthDateRaw = userBirthDate.getText().toString().trim().split("/");

        int monthRaw = Integer.parseInt(birthDateRaw[0]);
        int dayRaw = Integer.parseInt(birthDateRaw[1]);
        birth_date = birthDateRaw[2].trim() + String.format("%02d", monthRaw) + String.format("%02d", dayRaw);

        if(first_name.isEmpty() || last_name.isEmpty() || birth_date.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return result;
        }else{
            result = true;
        }

        return  result;
    }



}
