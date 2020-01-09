package com.example.santropolroulant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VolunteerType extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CardView crdKitchen, crdDelivery;
    private ImageView imagedelivery, imagekitchen;
    private TextView tvTitle, text1delivery,text2delivery, text1kitchen, text2kitchen;
    private Button btnNext;
    String volunteerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_type);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();

        // TO DO AFTER DELIVERY!!!!
        crdKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volunteerType = "kitchen";
                crdKitchen.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdDelivery.setCardBackgroundColor(Color.parseColor("#ffffff"));
    //            crdDelivery.setBackgroundResource(R.drawable.radius_clicked);
                imagedelivery.setVisibility(View.INVISIBLE);
                text1delivery.setVisibility(View.INVISIBLE);
                text2delivery.setVisibility(View.INVISIBLE);
                imagekitchen.setVisibility(View.VISIBLE);
                text1kitchen.setVisibility(View.VISIBLE);
                text2kitchen.setVisibility(View.VISIBLE);
            }
        });

        crdDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volunteerType = "delivery";
                crdDelivery.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdKitchen.setCardBackgroundColor(Color.parseColor("#ffffff"));
       //         crdDelivery.setBackgroundResource(R.drawable.radius_clicked);
                imagedelivery.setVisibility(View.VISIBLE);
                text1delivery.setVisibility(View.VISIBLE);
                text2delivery.setVisibility(View.VISIBLE);
                imagekitchen.setVisibility(View.INVISIBLE);
                text1kitchen.setVisibility(View.INVISIBLE);
                text2kitchen.setVisibility(View.INVISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            if (volunteerType!=null) {
                if (volunteerType.equals("kitchen")) {
                    intent = new Intent(VolunteerType.this, TimePreference.class);
                } else if (volunteerType.equals("delivery")) {
                    intent = new Intent(VolunteerType.this, TransportType.class);
                }
                intent.putExtra("one", volunteerType);
                startActivity(intent);
            }
        }

        });

    }

    private void setupUIViews() {
        tvTitle = (TextView) findViewById(R.id.tvTitle2);
        btnNext = (Button) findViewById(R.id.btnNext2);
        crdKitchen = (CardView) findViewById(R.id.crdKitchen);
        crdDelivery = (CardView) findViewById(R.id.crdDelivery);
        imagedelivery = (ImageView) findViewById(R.id.meal_delivery_image);
        text1delivery = (TextView) findViewById(R.id.meal_delivery_text_1);
        text2delivery = (TextView) findViewById(R.id.meal_delivery_text_2);
        imagekitchen = (ImageView) findViewById(R.id.kitchen_image);
        text1kitchen = (TextView) findViewById(R.id.kitchen_text_1);
        text2kitchen = (TextView) findViewById(R.id.kitchen_text_2);
        imagedelivery.setVisibility(View.INVISIBLE);
        text1delivery.setVisibility(View.INVISIBLE);
        text2delivery.setVisibility(View.INVISIBLE);
        imagekitchen.setVisibility(View.INVISIBLE);
        text1kitchen.setVisibility(View.INVISIBLE);
        text2kitchen.setVisibility(View.INVISIBLE);
    }


}
