package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import com.example.santropolroulant.DataValueTypes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Home extends AppCompatActivity {

    private CardView volunteerCard;
    private CardView scheduleCard;
    private CardView profileCard;
    private CardView contactCard;
    private String name = "default";
    private TextView tvGreeting, tvHello;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Gets name of current user to add to greeting
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();      // Ensuring we are currently logged in

        //TODO: Reinstate name display
        /*
        if (user==null){
            String userName = user.toString();
            Log.d("LOGGED IN", userName);
            System.exit(-1);
        }

        try {
            name = (String) User.class.getDeclaredMethod("getFirst_name").invoke(user);
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }*/

        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        contactCard = (CardView)findViewById(R.id.contactCard);

        setUpUIViews();

        // Bunch of CardView listeners
        volunteerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, VolunteerType.class));
            }
        });

        scheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, UserSchedule.class));
            }
        });

        // To be developed...
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, PersonalSettings.class));
                //Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(Home.this, VolunteerOptions.class));
            }
        });

        contactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ContactUs.class));
            }
            // Toast.makeText(Home.this, "Yet To Come", Toast.LENGTH_SHORT).show();            }
        });



    }

    private void setUpUIViews() {

        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        tvHello = (TextView) findViewById(R.id.tvHello);
        tvHello.setText(name + "!");

        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        contactCard = (CardView)findViewById(R.id.contactCard);
    }



    @Override
    public void onBackPressed() {
        return;
    }
    // to be implemented somewhere on this page
    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
    }
}
