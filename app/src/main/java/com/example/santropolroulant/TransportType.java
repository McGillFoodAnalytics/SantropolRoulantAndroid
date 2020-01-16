package com.example.santropolroulant;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TransportType extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvTitle2;
    private CardView crdDriver, crdNonDriver;
    private Button btnNext2;
    String transportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_type);
        mAuth = FirebaseAuth.getInstance();
        tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        btnNext2 = (Button) findViewById(R.id.btnNext2);

        crdDriver = (CardView) findViewById(R.id.crdDriver);
        crdNonDriver = (CardView) findViewById(R.id.crdNonDriver);
        crdDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));
        crdNonDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));

        crdDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "deldr";
                crdDriver.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdNonDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));
          //      crdDriver.setBackgroundResource(R.drawable.radius_clicked);
            }
        });

        crdNonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "deliv";
                crdNonDriver.setCardBackgroundColor(Color.parseColor("#D07FCE"));
                crdDriver.setCardBackgroundColor(Color.parseColor("#DCAADC"));
         //       crdNonDriver.setBackgroundResource(R.drawable.radius_clicked);
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





}
