package com.example.santropolroulant;

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
    private CardView crdDriver, crdBiker, crdWalker, crdPubTransport;
    private Button btnNext2;
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
                transportType = "deldr";
            }
        });

        crdBiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "deliv";
            }
        });

        crdWalker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "delis";
            }
        });

        crdPubTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transportType = "delds";
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransportType.this, EventsCalendar.class);
                intent.putExtra("event_type", transportType);
            }
        });



    }


    private void setupUIViews() {

        tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        btnNext2 = (Button) findViewById(R.id.btnNext2);

        crdDriver = (CardView) findViewById(R.id.crdDriver);
        crdBiker = (CardView) findViewById(R.id.crdBiker);
        crdWalker = (CardView) findViewById(R.id.crdWalker);
        crdPubTransport = (CardView) findViewById(R.id.crdPubTransport);


    }




}
