package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;




public class Home extends AppCompatActivity {

    private CardView volunteerCard;
    private CardView scheduleCard;
    private CardView profileCard;
    private CardView contactCard;
    private String name = "default";
    private TextView tvGreeting, tvHello;
    private FirebaseAuth mAuth;
    private ValueEventListener nameListener;
    private DatabaseReference mDatabase;
    private final String USER_LOC = MainActivity.USER_LOC;
    private final String USER_FIRSTNAME_LOC = "first_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Gets name of current user to add to greeting
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();      // Ensuring we are currently logged in
        if(user==null){
            Redirect.redirectToLogin(Home.this, mAuth);
        }


        //TODO: Reinstate name display

        //Get uid from sharedPreferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final String uid = pref.getString("uid", "notFound");

        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        contactCard = (CardView)findViewById(R.id.contactCard);

        //Set database and listener to get name
        mDatabase = FirebaseDatabase.getInstance().getReference(USER_LOC + "/" + uid + "/" + USER_FIRSTNAME_LOC);

        nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    Log.e("User Selection", "User not Found");
                    Redirect.redirectToLogin(Home.this, mAuth);
                } else {
                   name = dataSnapshot.getValue(String.class);
                   setUpUIViews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(nameListener);



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
    //@Override
    //public void onBackPressed() {
    //    return;
   //}

    private void setUpUIViews() {

        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        tvHello = (TextView) findViewById(R.id.tvHello);
        tvHello.setText("Hello, " +name + "!");


        volunteerCard = (CardView)findViewById(R.id.volunteerCard);
        scheduleCard = (CardView)findViewById(R.id.scheduleCard);
        profileCard = (CardView)findViewById(R.id.profileCard);
        contactCard = (CardView)findViewById(R.id.contactCard);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDatabase != null){
            if(nameListener != null){
                mDatabase.removeEventListener(nameListener);
            }
        }
    }

    // to be implemented somewhere on this page
    private void Logout(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
    }

    public int dpToPx(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.getResources().getDisplayMetrics());
    }
    public int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.getResources().getDisplayMetrics());
    }
}
