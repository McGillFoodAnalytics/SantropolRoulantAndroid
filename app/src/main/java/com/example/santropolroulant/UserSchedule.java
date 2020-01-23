package com.example.santropolroulant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santropolroulant.Adapters.EventAdapter;
import com.example.santropolroulant.FirebaseClasses.Event;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.DKGRAY;


public class UserSchedule extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button VolunteerButton;

    ConstraintLayout relativeLayout;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private View progressOverlay;
    private View userSchedule;
    
    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;

    private int unicode = 0x1F494;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_schedule);

        progressOverlay = (View) findViewById(R.id.progress_overlay);
        progressOverlay.setVisibility(View.INVISIBLE);

        VolunteerButton = (Button)findViewById(R.id.btnVolunteer);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout = (ConstraintLayout) findViewById(R.id.user_schedule);

        userSchedule = (View) findViewById(R.id.user_schedule);
        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);


       /* adapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserSchedule.this);
                builder.setCancelable(true);
                builder.setTitle("Unregister?");
                builder.setMessage("Would you like to unregister from this volunteering event? :(");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserSchedule.this, "PumpFake Unregistered!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });*/

        recyclerView.setVisibility(View.INVISIBLE);
        //setVisible();
        getUserInfo();

        VolunteerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSchedule.this, VolunteerType.class));
                finish();
            }
        });

        enableSwipeToDeleteAndUndo();
    }

    private void querySchedule(String user_UID){
        Log.d("@ @ : snapshot here:", "hey : " + user_UID);
        Query querySchedule = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("uid")
                .equalTo(user_UID);

        final ValueEventListener scheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    setVisible();
                    for(DataSnapshot scheduleSnap : dataSnapshot.getChildren()){
                        Event curEvent = scheduleSnap.getValue(Event.class);
                        curEvent.setEvent_id(scheduleSnap.getKey());

                        Log.d("@ @ : snapshot here:", "hey : BRaaaa1" + curEvent.getEvent_id());
                        // Make manual entry to eventList
                        // Use default 'Capacity' capVar from the type table

                        Log.d("@ @ : snapshot here:", "hey : after" + curEvent.getEvent_date_txt()+ curEvent.getEvent_time_start() + curEvent.getEvent_time_end() +String.valueOf(curEvent.isFirst_shift()));
                        Boolean isDuplicate = false;
                        for(Event event: eventList)
                        {
                            if(curEvent.getEvent_id().equals(event.getEvent_id())){
                                isDuplicate = true;
                            }
                        }
                        if(!curEvent.getUid().equals("nan") && !isDuplicate) {
                            eventList.add(curEvent);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    setInvisible();
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        querySchedule.addValueEventListener(scheduleListener);
    }

    private void getUserInfo(){

        final String userID = mAuth.getCurrentUser().getUid();

        Query myRef = FirebaseDatabase.getInstance().getReference(USER_LOC)
                .orderByChild("key")
                .equalTo(userID);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                        String user_UID = userSnap.getKey();
                        Log.d("hey:","Key: "+user_UID);
                        querySchedule(user_UID);
                        break;
                    }
                }else{
                    Toast.makeText(UserSchedule.this, "Unable to Fetch Events", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(userListener);

    }

    private void BackToMain(){
        finish();
        startActivity(new Intent(UserSchedule.this, Home.class));
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

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(UserSchedule.this, Home.class));
        finish();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                // adapter.notifyDataSetChanged();
                AlertDialog.Builder builder = new AlertDialog.Builder(UserSchedule.this);

                builder.setCancelable(true);
                TextView title = new TextView(UserSchedule.this);
                int myColor = getResources().getColor(R.color.white);
                title.setText("Confirm? " + getEmojiByUnicode(unicode));
                title.setBackgroundColor(myColor);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(DKGRAY);
                title.setTextSize(20);
                builder.setCustomTitle(title);
                builder.setMessage("Are you should you want to unregister from this event?");


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int position = viewHolder.getAdapterPosition();
                        // final String item = adapter.getData().get(position);

                        //adapter.removeItem(position);
                        //eventList.remove(position);
                        deleteEvent(position);

                        //adapter.notifyDataSetChanged();

/*
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "You have been removed from the list.", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // adapter.restoreItem(item, position);
                                recyclerView.scrollToPosition(position);
                            }
                        });

                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();*/
                    }
                });

                AlertDialog dialog = builder.show();

                TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
                messageView.setGravity(Gravity.CENTER);

                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);

    }
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        userSchedule.setClickable(false);
    }

    private void deleteEvent(int position){
        //String key =
        String event_id = eventList.get(position).getEvent_id();
        eventList.remove(position);
        Task[] tasks = new Task[11];

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference myRef = firebaseDatabase.getReference();

        tasks[5] = myRef.child(EVENT_LOC).child(event_id).child("uid").setValue("nan");

        tasks[0] = myRef.child(EVENT_LOC).child(event_id).child("first_name").setValue("");

        tasks[1] = myRef.child(EVENT_LOC).child(event_id).child("last_name").setValue("");

        tasks[2] = myRef.child(EVENT_LOC).child(event_id).child("first_shift").setValue(false);

        tasks[3] = myRef.child(EVENT_LOC).child(event_id).child("key").setValue("nan");

        tasks[4] = myRef.child(EVENT_LOC).child(event_id).child("note").setValue("");



        adapter.notifyDataSetChanged();

        //return tasks;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}