package com.example.santropolroulant;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.santropolroulant.Adapters.UserAdapter;
import com.example.santropolroulant.FirebaseClasses.UserSlot;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import android.view.WindowManager;
import android.content.Context;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

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


    public void updateEditText(CharSequence newText,Integer dateval, String eventType) {
        eventTypeInfo = eventType;
        datevalInfo = dateval;
        queryFunction(eventType,dateval);
        dateText.setText(newText);
    }
    private void popUpClick(final String key){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        final View customLayout = getLayoutInflater().inflate(R.layout.btn_share, null);
        txtNote = customLayout.findViewById(R.id.txtNote);
        String[] info = {"First Shift"};
        boolean[] checkedItems = {false};
        builder.setView(customLayout)
                .setPositiveButton("Sign up!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.AuthStateListener mAuth = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        String email = firebaseAuth.getCurrentUser().getEmail();
                                        if (email != null) {
                                            Log.d("email", email);
                                                registerFunction(email);
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
                                // user checked an item
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
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("event_date")
                .equalTo(dateVal);

        ValueEventListener countListener = new ValueEventListener() {
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
        attendeeQuery.addValueEventListener(countListener);

    }


    private void registerFunction(String email){
        //find user information

        Query userQuery = FirebaseDatabase.getInstance().getReference("user");


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userInfo : dataSnapshot.getChildren()) {
                    final String confirm_email = userInfo.child("email").getValue(String.class);
                    if (confirm_email.contains(email))
                    {
                        final String first_name_ = userInfo.child("first_name").getValue(String.class);
                        final String last_name_ = userInfo.child("last_name").getValue(String.class);
                        final String uid_ = userInfo.getKey();
                        Log.d("funTIMES", uid_);
                        registerFunction2(first_name_,last_name_,uid_);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        };
        userQuery.addValueEventListener(userListener);
        // find event_name

    }
    private void registerFunction2(String first_name, String last_name, String uid){
        Query eventQuery = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("event_date")
                .equalTo(datevalInfo);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    String key = event.getKey();
                    String uid_ = event.child("uid").getValue(String.class);
                    if (key.contains(String.valueOf(datevalInfo)) && key.contains(eventTypeInfo)) {
                        if (uid_.contentEquals(uid)){
                            break;
                        }
                        else {
                            String event_name_ = event.getKey();
                            Log.d("funTIMES", event_name_);
                            registerFunction3(event_name_, first_name, last_name, uid);
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
        eventQuery.addValueEventListener(eventListener);
    }

    private void registerFunction3(String event_name, String first_name , String last_name, String uid){
        String note = txtNote.getText().toString().trim();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        if (event_name != null) {
            // Writing attendee instance to the firebase db
            myRef.child("event").child(event_name).child("uid").setValue(uid);
            myRef.child("event").child(event_name).child("note").setValue(note);
            myRef.child("event").child(event_name).child("last_name").setValue(last_name);
            myRef.child("event").child(event_name).child("first_name").setValue(first_name);
        }
    }
}