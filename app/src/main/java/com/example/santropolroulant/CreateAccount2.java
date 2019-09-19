package com.example.santropolroulant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


//Address, City, Postal Code,

//Phone, email, password, password
public class CreateAccount2 extends AppCompatActivity {

    private Button next_2;
    String first_name, last_name, birth_date, address, city, postal_code;
    private EditText userAddress, userCity, userPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_2);
        setupUIViews(); // function way to set up UI elements

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            first_name = extras.getString("FIRST_NAME"); // retrieve the data using keyName
            last_name = extras.getString("LAST_NAME");
            birth_date = extras.getString("BIRTH_DATE");
        }

        next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    Intent intent = new Intent(CreateAccount2.this, CreateAccount3.class);
                    intent.putExtra("FIRST_NAME", first_name);
                    intent.putExtra("LAST_NAME", last_name);
                    intent.putExtra("BIRTH_DATE", birth_date);
                    intent.putExtra("ADDRESS", address);
                    intent.putExtra("CITY", city);
                    intent.putExtra("POSTAL_CODE", postal_code);
                    startActivity(intent);
                }
            }
        });

    }

    private void setupUIViews() {
        next_2 = (Button) findViewById(R.id.etNext_2);

        userAddress = (EditText) findViewById(R.id.etUser_Address);
        userAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userCity = (EditText) findViewById(R.id.etUser_City);
        userCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        userPostalCode = (EditText) findViewById(R.id.etUser_Postal_Code);
        userPostalCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        city = userCity.getText().toString().trim();
        address = userAddress.getText().toString().trim();
        address.replaceAll("\\s+","");
        postal_code = userPostalCode.getText().toString().trim();

        if(city.isEmpty() || address.isEmpty() || postal_code.isEmpty()){
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

}
