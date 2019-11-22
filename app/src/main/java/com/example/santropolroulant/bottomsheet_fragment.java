package com.example.santropolroulant;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.santropolroulant.Adapters.UserAdapter;
import com.example.santropolroulant.FirebaseClasses.UserSlot;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class bottomsheet_fragment extends Fragment {
    private ConstraintLayout mBottomSheet;
    private ImageView mLeftArrow;
    private AppCompatTextView dateText;
    private ImageView mRightArrow;
    private RecyclerView recyclerView;
    private UserAdapter adapter;        // Custom adapter 'EventAdapter'
    private List<UserSlot> userList;
    private EditText txtNote;
    private Boolean isNew;
    private Button signUp;
    private Switch swtchNew;
    private Integer datevalInfo;
    private String eventTypeInfo;
    boolean[] checkedItems = {false};
    private AppCompatTextView infoText;
    private Query attendeeQuery, eventQuery, userQuery;
    private DatabaseReference myRef;
    private ValueEventListener countListener, userListener, eventListener;
    private final String EVENT_LOC = MainActivity.EVENT_LOC;
    private final String USER_LOC = MainActivity.USER_LOC;
    private final String VACANT_UID = "nan";

    public bottomsheet_fragment() {
        // Required empty public constructor
    }

    public static bottomsheet_fragment newInstance() {
        return new bottomsheet_fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        dateText = view.findViewById(R.id.bottom_sheet_heading_txt);
        // find container view
        mBottomSheet = view.findViewById(R.id.bottom_sheet);
        userList = new ArrayList<>();
        adapter = new UserAdapter(getActivity() ,userList);
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true); // Fix size
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        mLeftArrow = view.findViewById(R.id.bottom_sheet_left_arrow);
        mRightArrow = view.findViewById(R.id.bottom_sheet_right_arrow);
        infoText = view.findViewById(R.id.bottom_sheet_heading_txt_info);
        signUp = view.findViewById(R.id.signUp);

        initializeBottomSheet();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpClick("");
            }
        });


        return view;
    }

    private void initializeBottomSheet() {

        // init the bottom sheet behavior

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        bottomSheetBehavior.setHideable(false);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int height = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight((int) ((float) height*0.40));
        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // change the state of the bottom sheet
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                //    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                //}
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (isAdded()) {
                    animateBottomSheetArrows(slideOffset);
                }
                //if (slideOffset>=0.0 && slideOffset<=0.5){
                //    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                //}
            }
        });
    }


    private void animateBottomSheetArrows(float slideOffset) {
        mLeftArrow.setRotation(slideOffset * -180);
        mRightArrow.setRotation(slideOffset * 180);
    }


    public void updateEditText(String newText,Integer dateval, String eventType) {
        eventTypeInfo = eventType;
        datevalInfo = dateval;
        queryFunction(eventType,dateval);
        dateText.setText(newText);
        String eventLongInfo;
        if (eventType.contains("kitam")) {
            if (newText.contains("saturday")) {
                eventLongInfo = "Kitchen 9:00am-12:00pm";
            }
            else{
                eventLongInfo = "Kitchen 9:30-12:30pm";
            }
        }
        else if (eventType.contains("kitpm")){
            if (newText.contains("saturday")) {
                eventLongInfo = "Kitchen 1:00-3:30pm";
            }
            else{
                eventLongInfo = "Kitchen 1:30-4:00pm";
            }
        }
        else if (eventType.contains("deldr")){
            if (newText.contains("saturday")) {
                eventLongInfo = "Meal Delivery 2:15-5:30pm";
            }
            else{
                eventLongInfo = "Meal Delivery 2:45-6:00pm";
            }
        }
        else if (eventType.contains("deliv")){
            if (newText.contains("saturday")) {
                eventLongInfo = "Meal Delivery 2:15-5:30pm";
            }
            else{
                eventLongInfo = "Meal Delivery 2:45-6:00pm";
            }
        }
        else{
            eventLongInfo = "";
        }
        infoText.setText(eventLongInfo);
    }
    private void popUpClick(final String key){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        final View customLayout = getLayoutInflater().inflate(R.layout.btn_share, null);
        txtNote = customLayout.findViewById(R.id.txtNote);
        String[] info = {"First Shift"};
        builder.setView(customLayout)
                .setPositiveButton("Sign up!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.AuthStateListener mAuth = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        String userUid = firebaseAuth.getCurrentUser().getUid();
                                        if (userUid != null) {
                                            Log.d("email", userUid);
                                            registerFunction(userUid);
                                        } else { //user is not logged in

                                        }

                                    }
                                };
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                firebaseAuth.addAuthStateListener(mAuth);
                                dialog.cancel();//
                            }
                        })
                .setMultiChoiceItems(info,checkedItems,new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
                            }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel(); // Cancel pop up

                            }
                        });



        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void queryFunction(final String eventType, final Integer dateVal){
        attendeeQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("event_date")
                .equalTo(dateVal);

        countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                Integer i=1;
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();
                    Log.d("hey:","For loop key: " + key);

                    if (key.contains(String.valueOf(dateVal) + eventType)){
                        Log.d("hey:","For contained");
                        //final String slot = userSnap.child("slot").getValue(String.class);
                        final String first_name = userSnap.child("first_name").getValue(String.class);
                        final String last_name = userSnap.child("last_name").getValue(String.class);

                        Log.d("hey:","Whats"  + " : " + first_name + " " + last_name);

                        if (!first_name.isEmpty() && !last_name.isEmpty()) {
                            userList.add(
                                    new UserSlot(i.toString(), first_name, last_name, key)
                            );
                            i = i + 1;
                        }
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addListenerForSingleValueEvent(countListener);

    }


    private void registerFunction(String userUid){
        //find user information
        userQuery = FirebaseDatabase.getInstance().getReference(USER_LOC).orderByChild("key")
                .equalTo(userUid);


        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userInfo : dataSnapshot.getChildren()) {
                    final String first_name_ = userInfo.child("first_name").getValue(String.class);
                    final String last_name_ = userInfo.child("last_name").getValue(String.class);
                    final String uid_ = userInfo.getKey();
                    registerFunction2(first_name_,last_name_,uid_);
                    break;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        };
        userQuery.addListenerForSingleValueEvent(userListener);
        // find event_name

    }
    private void registerFunction2(String first_name, String last_name, String uid){
        eventQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("event_date")
                .equalTo(datevalInfo);
        Log.d("fun", String.valueOf(datevalInfo));
        Log.d("fun", eventTypeInfo);
        Log.d("fun", first_name);
        Log.d("fun", last_name);
        Log.d("fun", uid);
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    String key = event.getKey();
                    String uid_ = event.child("uid").getValue(String.class);
                    Log.d("fun", key);
                    if (key.contains(String.valueOf(datevalInfo)) && key.contains(eventTypeInfo)) {
                        if (uid_.contentEquals(uid)){
                            Log.d("dupSignup", "tried duplicating");
                            Toast toast = Toast.makeText(getActivity(),
                                    "You are already signed up!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        }
                        else if(!uid_.equals(VACANT_UID)){
                            continue;
                        }
                        else {
                            String event_name_ = event.getKey();
                            Log.d("funTIMES", event_name_);
                            registerFunction3(key, first_name, last_name, uid);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        };
        eventQuery.addListenerForSingleValueEvent(eventListener);
    }

    private void registerFunction3(String event_name, String first_name, String last_name, String uid){
        String note = txtNote.getText().toString().trim();
        boolean isNew =  checkedItems[0];
        myRef = FirebaseDatabase.getInstance().getReference();
        if (event_name != null) {
            // Writing attendee instance to the firebase db
            myRef.child(EVENT_LOC).child(event_name).child("uid").setValue(uid);
            myRef.child(EVENT_LOC).child(event_name).child("note").setValue(note);
            myRef.child(EVENT_LOC).child(event_name).child("last_name").setValue(last_name);
            myRef.child(EVENT_LOC).child(event_name).child("first_name").setValue(first_name);
            if(isNew){
                myRef.child(EVENT_LOC).child(event_name).child("first_shift").setValue(true);
            } else {
                myRef.child(EVENT_LOC).child(event_name).child("first_shift").setValue(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove Listeners
        if(attendeeQuery != null){
            if (countListener != null){
                attendeeQuery.removeEventListener(countListener);
            }
        }

        if(userQuery != null){
            if(userListener != null){
                userQuery.removeEventListener(userListener);
            }
        }

        if(eventQuery != null){
            if(eventListener != null){
                eventQuery.removeEventListener(eventListener);
            }
        }
    }
}