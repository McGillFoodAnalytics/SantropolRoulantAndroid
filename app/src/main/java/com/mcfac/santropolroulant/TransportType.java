package com.mcfac.santropolroulant;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.mcfac.santropolroulant.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TransportType extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvTitle2, text1driver, text2driver, text1nondriver, text2nondriver;
    private CardView crdDriver, crdNonDriver;
    private Button btnNext2;
    private ImageView imagedelivery, imagekitchen;
    String transportType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_type);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();

        crdDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "deld";
                crdDriver.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdNonDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));

                imagedelivery.setVisibility(View.VISIBLE);
                text1driver.setVisibility(View.VISIBLE);
                text2driver.setVisibility(View.VISIBLE);
                imagekitchen.setVisibility(View.INVISIBLE);
                text1nondriver.setVisibility(View.INVISIBLE);
                text2nondriver.setVisibility(View.INVISIBLE);
            }
        });

        crdNonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "deli";
                crdNonDriver.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));


                imagedelivery.setVisibility(View.INVISIBLE);
                text1driver.setVisibility(View.INVISIBLE);
                text2driver.setVisibility(View.INVISIBLE);
                imagekitchen.setVisibility(View.VISIBLE);
                text1nondriver.setVisibility(View.VISIBLE);
                text2nondriver.setVisibility(View.VISIBLE);
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transportType!=null) {
                    Intent intent = new Intent(TransportType.this, Calendar_Main.class);
                    intent.putExtra("two", transportType);
                    startActivity(intent);
                }
            }
        });



    }


    private void setupUIViews(){
        tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        btnNext2 = (Button) findViewById(R.id.btnNext2);
        crdDriver = (CardView) findViewById(R.id.crdDriver);
        crdNonDriver = (CardView) findViewById(R.id.crdNonDriver);

        imagedelivery = (ImageView) findViewById(R.id.meal_delivery_image);
        text1driver = (TextView) findViewById(R.id.driver_text_1);
        text2driver = (TextView) findViewById(R.id.driver_text_2);
        imagekitchen = (ImageView) findViewById(R.id.kitchen_image);
        text1nondriver = (TextView) findViewById(R.id.non_driver_text_1);
        text2nondriver = (TextView) findViewById(R.id.non_driver_text_2);
        imagedelivery.setVisibility(View.INVISIBLE);
        text1driver.setVisibility(View.INVISIBLE);
        text2driver.setVisibility(View.INVISIBLE);
        imagekitchen.setVisibility(View.INVISIBLE);
        text1nondriver.setVisibility(View.INVISIBLE);
        text2nondriver.setVisibility(View.INVISIBLE);
        crdDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));
        crdNonDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));

    }


   /* @Override
    public void onBackPressed() {
        TransportType.this.finish();
    }*/





}
