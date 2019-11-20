package com.example.santropolroulant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.santropolroulant.Adapters.UserAdapter;
import com.example.santropolroulant.FirebaseClasses.UserSlot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class CalanderSlots extends AppCompatActivity {

    private RecyclerView recyclerView;      // Recycler View to work with Custom Adapter
    private UserAdapter adapter;            // Custom adapter 'UserAdapter'
    private List<UserSlot> userList;        // List that will be filled with Event classes from Firebase Query
    private Button btnSignUp;
    private String keyIntent;
    private final String EVENT_LOC = MainActivity.EVENT_LOC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_slots);

        btnSignUp = (Button)findViewById(R.id.btnRegSign);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalanderSlots.this, RegisterEvent.class);
                intent.putExtra("keyIntent", keyIntent); //keyIntent is saved below, it brings the eventKey to the next activity
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView3); //xml of recyclerview
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();

        adapter = new UserAdapter(this, userList); // The adapter is connected to the userList
        recyclerView.setAdapter(adapter); // recyclerView is connected to the adapter

        Intent intent = getIntent();                                  // Getting the info from last activity's intent
        final String gtype = intent.getStringExtra("type");     // eventType used inside later Query

        // Setting up custom calendar view taken from github
        // https://github.com/Mulham-Raee/Horizontal-Calendar

        // Starts this month
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        // on click listener for calendar dates
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                // getting the date form yymmdd from the clicked date
                String year = String.valueOf(date.get(Calendar.YEAR));
                String yearSub = year.substring(Math.max(year.length() - 2, 0));
                String month = String.valueOf(date.get(Calendar.MONTH)+1);
                Integer weekday = date.get(Calendar.DAY_OF_WEEK);

                // if the day of the month is single digit, add 0
                if (month.length() == 1){
                    month = "0"+ month;
                }

                // if month is single digit add 0
                String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
                if (day.length() == 1){
                    day = "0"+ day;
                }

                // Weekend condition vs weekday
                if (weekday.equals(1) || weekday.equals(7)){
                    // weekend query
                    // event type being passed through wkndConverter
                    // refer to queryFunction for more
                    queryFunction(wkndConverter(gtype), yearSub+month+day);

                    // here keyIntent is made (currently imperfect and needs working)
                    keyIntent = yearSub+month+day+wkndConverter(gtype)+"01";
                    Toast.makeText(CalanderSlots.this, "Weekend: " + keyIntent, Toast.LENGTH_SHORT).show(); // not needed
                } else {
                    // weekday query
                    queryFunction(gtype, yearSub+month+day);
                    keyIntent = yearSub+month+day+gtype+"01";
                    Toast.makeText(CalanderSlots.this, "Success: " + keyIntent, Toast.LENGTH_SHORT).show(); // not needed
                }

            }
        });

    }

    private void queryFunction(String eventType, final String dateVal) {

        // Firebase query in event table looking for keys between values
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByKey()
                .startAt(dateVal+eventType+"01") // 190830kitam01
                .endAt(dateVal+eventType+"99");  // 190830kitam99

        // This gets all slots of the particular event
        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear(); // clear the list when data is updated, then query/fill anew
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){

                    String key = userSnap.getKey(); // need to get key of query == eg. 190830kitam01
                    Log.d("hey:","Key: "+key);

                    final String slot = key.substring(Math.max(key.length() - 2, 0)); // last two characters of the key = slot
                    final String first_name = userSnap.child("first_name").getValue(String.class); // getting value from query
                    final String last_name = userSnap.child("last_name").getValue(String.class);
                    Log.d("hey:","Key: "+key);

                    // adding the queried info to the userlist (which was cleared)
                    userList.add(
                            new UserSlot(slot,first_name,last_name, "9")
                    );
                }
                adapter.notifyDataSetChanged(); // after for loop goes through all items in for loop, notify adapter (very important)

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // never used
            }
        };
        attendeeQuery.addValueEventListener(countListener); //calls the actual query as listener

    }

    private String wkndConverter(final String gtype){
        String wkndType = gtype;
        if (gtype.equals("kitam")){
            wkndType = "kitas";
        } else if (gtype.equals("kitpm")){
            wkndType = "kitps";
        } else if (gtype.equals("deliv")){
            wkndType = "delis";
        } else if (gtype.equals("deldr")){
            wkndType = "delds";
        }
        return wkndType;
    };



    private void BackToMain(){
        finish();
        startActivity(new Intent(CalanderSlots.this, Home.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.homeMenu:
                BackToMain();

        }

        return super.onOptionsItemSelected(item);
    }
}
