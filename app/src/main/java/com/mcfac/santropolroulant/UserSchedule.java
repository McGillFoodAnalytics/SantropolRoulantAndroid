package com.mcfac.santropolroulant;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mcfac.santropolroulant.Adapters.EventAdapter;
import com.mcfac.santropolroulant.FirebaseClasses.Event;
import com.google.android.gms.tasks.Task;
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
import java.text.ParseException;


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

    //Database endpoints
    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;

    //Emoji unicode
    private int unicode = 0x1F494;
    private int unicodesad = 0x1F62D;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_schedule);

        //Loading display
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
        recyclerView.setVisibility(View.INVISIBLE);

       //Retrieving user's schedule
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

    //Query schedule of the specific using by looking for events with user's uid.
    private void querySchedule(String user_UID){
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

                        // Make manual entry to eventList
                        // Use default 'Capacity' capVar from the type table

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

    //Method which calls querySchedule with user's uid as its argument
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
                        querySchedule(user_UID);
                        break;
                    }
                }else{
                    //Toast.makeText(UserSchedule.this, R.string.unable_fetch_events, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(userListener);

    }

    //Redirect to home
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

    //When BACK BUTTON is pressed, the activity on the stack is restarted
    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(new Intent(UserSchedule.this, Home.class));
        finish();
    }

    //Swipe to delete function utilizing SwipeToDeleteCallback abstract class
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                try {
                    if (checkDate(position)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserSchedule.this);

                        builder.setCancelable(true);
                        TextView title = new TextView(UserSchedule.this);
                        int myColor = getResources().getColor(R.color.white);
                        title.setText(getString(R.string.confirm) + getEmojiByUnicode(unicode));
                        title.setBackgroundColor(myColor);
                        title.setPadding(10, 10, 10, 10);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(DKGRAY);
                        title.setTextSize(20);
                        builder.setCustomTitle(title);
                        builder.setMessage(getString(R.string.unregister));


                        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                adapter.notifyDataSetChanged();

                            }
                        });
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteEvent(position);


                            }
                        });

                        AlertDialog dialog = builder.show();

                        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                        messageView.setGravity(Gravity.CENTER);

                        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                        layoutParams.weight = 10;
                        btnPositive.setLayoutParams(layoutParams);
                        btnNegative.setLayoutParams(layoutParams);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserSchedule.this);
                        TextView title = new TextView(UserSchedule.this);
                        //int myColor = getResources().getColor(R.color.white);
                        title.setText(getEmojiByUnicode(unicodesad)+getEmojiByUnicode(unicodesad)+getEmojiByUnicode(unicodesad));
                        title.setPadding(10, 10, 10, 10);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(DKGRAY);
                        title.setTextSize(20);
                        builder.setCustomTitle(title);
                        builder.setMessage(R.string.event_cancel_denied);

                        AlertDialog dialog = builder.show();
                        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                        messageView.setGravity(Gravity.CENTER);
                        adapter.notifyDataSetChanged();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    //Method to set overlay spinner to invisible
    public void setInvisible() {
        progressOverlay.setVisibility(View.INVISIBLE);

    }

    //Method to set overlay spinner to visible
    public void setVisible() {
        progressOverlay.setVisibility(View.VISIBLE);
        userSchedule.setClickable(false);
    }

    //Method to delete the event from database and remove it from event adapter
    private void deleteEvent(int position){
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

    }

    //Method to verify if the event is more than 2 days away.
    public boolean checkDate(int position) throws ParseException{
        boolean check = false;

        SimpleDateFormat oldDF = new SimpleDateFormat("HH:mm, EEEE, MMMM dd, yyyy");
        SimpleDateFormat newDF = new SimpleDateFormat("HH:mm/dd/MM/yyyy");


        String today = newDF.format(Calendar.getInstance().getTime());            // gets current date
        String event = eventList.get(position).getEvent_date_txt();
        String event_time = eventList.get(position).getEvent_time_start();
        event = event_time+ ", "+ event;
        Date eventObj1 = oldDF.parse(event);
        event = newDF.format(eventObj1);
        Date eventDate = newDF.parse(event);
        Date todayDate = newDF.parse(today);

        long secs = ((eventDate.getTime() - todayDate.getTime()) / (1000));
        int minsApart = (int) secs / 60;

        if (Math.abs(minsApart) > 48*60)
            check = true;

        return check;
    }

    //Method to convert unicode to emoji
    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}