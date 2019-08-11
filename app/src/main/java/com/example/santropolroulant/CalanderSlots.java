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

    private RecyclerView recyclerView; // Recycler view to work with Custom Adapter
    private UserAdapter adapter; // Custom adapter 'EventAdapter'
    private List<UserSlot> userList; // List that will be filled with Event classes from Firebase Query
    private Button btnSignUp;
    private List<UserSlot> driverList;
    private String keyIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_slots);

        btnSignUp = (Button)findViewById(R.id.btnRegSign);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalanderSlots.this, RegisterEvent.class);
                intent.putExtra("keyIntent", keyIntent);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView3); //xml
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        driverList = new ArrayList<>();

        adapter = new UserAdapter(this, userList); //
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        final String gtype = intent.getStringExtra("type");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String year = String.valueOf(date.get(Calendar.YEAR));
                String yearSub = year.substring(Math.max(year.length() - 2, 0));
                String month = String.valueOf(date.get(Calendar.MONTH)+1);
                Integer weekday = date.get(Calendar.DAY_OF_WEEK);
                if (month.length() == 1){
                    month = "0"+ month;
                }

                String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
                if (day.length() == 1){
                    day = "0"+ day;
                }

                if (weekday.equals(1) || weekday.equals(7)){
                    queryFunction(wkndConverter(gtype), yearSub+month+day);
                    keyIntent = yearSub+month+day+wkndConverter(gtype)+"01";
                    Toast.makeText(CalanderSlots.this, "Weekend: " + keyIntent, Toast.LENGTH_SHORT).show();
                } else {
                    queryFunction(gtype, yearSub+month+day);
                    keyIntent = yearSub+month+day+gtype+"01";
                    Toast.makeText(CalanderSlots.this, "Success: " + keyIntent, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void queryFunction(String eventType, final String dateVal){

        Query attendeeQuery = FirebaseDatabase.getInstance().getReference("event")
                .orderByKey()
                .startAt(dateVal+eventType+"01")
                .endAt(dateVal+eventType+"99");

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){

                    String key = userSnap.getKey();
                    Log.d("hey:","Key: "+key);

                    final String slot = key.substring(Math.max(key.length() - 2, 0));
                    final String first_name = userSnap.child("first_name").getValue(String.class);
                    final String last_name = userSnap.child("last_name").getValue(String.class);
                    Log.d("hey:","Key: "+key);

                    userList.add(
                            new UserSlot(slot,first_name,last_name, "9")
                    );
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addValueEventListener(countListener);

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
