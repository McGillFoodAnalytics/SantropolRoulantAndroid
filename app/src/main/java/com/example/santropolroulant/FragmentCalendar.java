package com.example.santropolroulant;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.santropolroulant.FirebaseClasses.UserSlot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class FragmentCalendar extends Fragment {
    private FragmentCalendarListener listener;
    ArrayList<Date> emptyDates = new ArrayList<Date>();
    String event_type;

    public FragmentCalendar(String event_type){
        this.event_type = event_type;
    }
    public interface FragmentCalendarListener{
        void onInputASent(String date, Integer dateVal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container,false);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        List<Date> dates = new ArrayList<Date>();

        String todayDate = DateFormat.getDateInstance(DateFormat.FULL).format(today);
        Calendar todaycal = Calendar.getInstance();
        todaycal.setTime(today);
        int month = todaycal.get(Calendar.MONTH)+1;
        int year  = todaycal.get(Calendar.YEAR);
        int day   = todaycal.get(Calendar.DAY_OF_MONTH);

        Calendar nextDay = Calendar.getInstance();
        nextDay.add(Calendar.DATE, 2);

        Calendar nextDay2 = Calendar.getInstance();
        nextDay2.add(Calendar.DATE, 5);

        Integer dateVal = Integer.valueOf(Integer.toString(year).substring(1)+Integer.toString(month)+Integer.toString(day));
        listener.onInputASent(todayDate,dateVal);
        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.init(today, nextYear.getTime()).withSelectedDate(today).withHighlightedDate(nextDay2.getTime());

        //queryFunction(event_type, inflater, container);
        //**********************
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("event_date");

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emptyDates.clear();
                Integer i=0;
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();
                    Log.d("hey:","For loop key: " + key);

                    if (key.contains(event_type)){
                        Log.d("hey:","For contained");
                        //final String slot = userSnap.child("slot").getValue(String.class);
                        final Long event_date_long = userSnap.child("event_date").getValue(Long.class);

                        String event_date = "" + event_date_long;
                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        int year = 2000 + Integer.parseInt(event_date.substring(0,2));
                        int month = Integer.parseInt(event_date.substring(2,4));
                        int day = Integer.parseInt(event_date.substring(4,6));
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month - 1);
                        cal.set(Calendar.DATE, day);

                        java.util.Date emptyDay = cal.getTime();

                        if(!emptyDates.contains(emptyDay) && !emptyDay.before(today)) {
                            emptyDates.add(i, emptyDay);
                            Log.d("addition:",emptyDay.toString());
                            i++;
                        }

                        if(i == 1) { break; }
                    }
                }
                // updateCalendar(emptyDates, inflater, container);
               /* Date today = new Date();
                Calendar nextDay = Calendar.getInstance();
                nextDay.add(Calendar.DATE, 5);
                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);
*/
                Log.d("updateCalendar", emptyDates.get(0).toString());
                final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
                //calendar.highlightDates(emptyDates);
                calendar.init(today, nextYear.getTime()).withSelectedDate(today).withHighlightedDates(emptyDates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addValueEventListener(countListener);


        //**************************

        ArrayList<Date> highlight1 = new ArrayList<>();
        highlight1.add(0, nextDay.getTime());
        ArrayList<Date> highlight2 = new ArrayList<>();
        highlight2.add(0, nextDay2.getTime());


        //final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        //calendar.init(today, nextYear.getTime()).withSelectedDate(today).withHighlightedDates(highlight1);
        //calendar.init(today, nextYear.getTime()).withSelectedDate(today).withHighlightedDates(highlight2);
        //calendar.highlightDates(highlight2);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH)+1;
                int year  = cal.get(Calendar.YEAR);
                int day   = cal.get(Calendar.DAY_OF_MONTH);
                Integer dateVal = Integer.valueOf(Integer.toString(year).substring(1)+Integer.toString(month)+Integer.toString(day));
                listener.onInputASent(selectedDate,dateVal);
            }
            @Override
            public void onDateUnselected(Date date) {

            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCalendarListener) {
            listener = (FragmentCalendarListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void queryFunction(final String eventType, LayoutInflater inflater, ViewGroup container){
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference("event")
                .orderByChild("event_date");

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emptyDates.clear();
                Integer i=0;
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();
                    Log.d("hey:","For loop key: " + key);

                    if (key.contains(eventType)){
                        Log.d("hey:","For contained");
                        //final String slot = userSnap.child("slot").getValue(String.class);
                        final Long event_date_long = userSnap.child("event_date").getValue(Long.class);

                        String event_date = "" + event_date_long;
                        Calendar cal = Calendar.getInstance();
                        cal.clear();
                        int year = 2000 + Integer.parseInt(event_date.substring(0,2));
                        int month = Integer.parseInt(event_date.substring(2,4));
                        int day = Integer.parseInt(event_date.substring(4,6));
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DATE, day);

                        java.util.Date emptyDay = cal.getTime();

                        if(!emptyDates.contains(emptyDay)) {
                            emptyDates.add(i, emptyDay);
                            Log.d("addition:",emptyDay.toString());
                            i++;
                        }

                        if(i == 3) { break; }
                    }
                }
               // updateCalendar(emptyDates, inflater, container);
               /* Date today = new Date();
                Calendar nextDay = Calendar.getInstance();
                nextDay.add(Calendar.DATE, 5);
                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);
*/
                Log.d("updateCalendar", emptyDates.get(0).toString());
                View v = inflater.inflate(R.layout.fragment_calendar, container,false);
                final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
                calendar.highlightDates(emptyDates);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addValueEventListener(countListener);

    }

    public void updateCalendar(ArrayList<Date> emptyDates, LayoutInflater inflater, ViewGroup container){

        Date today = new Date();
        Calendar nextDay = Calendar.getInstance();
        nextDay.add(Calendar.DATE, 5);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        emptyDates.clear();
        emptyDates.add(0, nextDay.getTime());

        Log.d("updateCalendar", emptyDates.get(0).toString());
        View v = inflater.inflate(R.layout.fragment_calendar, container,false);
        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.highlightDates(emptyDates);


    }

}