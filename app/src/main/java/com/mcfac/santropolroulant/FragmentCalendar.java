package com.mcfac.santropolroulant;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mcfac.santropolroulant.R;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.savvi.rangedatepicker.SubTitle;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//This class deals with the registration calendar fragment
public class FragmentCalendar extends Fragment {
    private FragmentCalendarListener listener;
    ArrayList<Date> emptyDates = new ArrayList<Date>();
    String event_type;
    ArrayList<SubTitle> subTitles = new ArrayList<>();

    private final String EVENT_LOC = "event";


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
        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 2);
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

        ArrayList<Date> calendarList =  getDatesBetween(today, nextMonth.getTime());

        Integer dateVal = Integer.valueOf(Integer.toString(year).substring(1)+ String.format("%02d", month)+ String.format("%02d",day));
        listener.onInputASent(todayDate,dateVal);
        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.init(today, nextMonth.getTime());


        Query attendeeQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("event_date");

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emptyDates.clear();
                subTitles.clear();
                Integer i=0;

                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();

                    if (key.contains(event_type)){
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

                        if (userSnap.child("is_important_event").getValue(boolean.class)) {
                            java.util.Date subtitleday = cal.getTime();
                            subTitles.add(new SubTitle(subtitleday, getString(R.string.priority)));
                        }
                        if(!emptyDates.contains(emptyDay) && !emptyDay.before(today)) {
                            for(Date potentialRemoval : calendarList){
                                String potentialRemovalDate = "" + potentialRemoval.getYear() + "/" + potentialRemoval.getMonth() + "/" + potentialRemoval.getDate();
                                String emptyDayDate = "" + emptyDay.getYear() + "/" + emptyDay.getMonth() + "/" + emptyDay.getDate();
                                if(potentialRemovalDate.equals(emptyDayDate)){
                                    calendarList.remove(potentialRemoval);
                                    break;
                                }
                            }
                            //calendarList.remove(emptyDay);
                            Log.d("addition:",emptyDay.toString());
                            Log.d("is 15 gone", calendarList.get(1).toString());
                            i++;
                        }

                    }
                }


                final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
                //calendar.highlightDates(emptyDates);
                calendar.init(today, nextMonth.getTime()).withHighlightedDates(calendarList).withSubTitles(subTitles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        attendeeQuery.addValueEventListener(countListener);


        ArrayList<Date> highlight1 = new ArrayList<>();
        highlight1.add(0, nextDay.getTime());
        ArrayList<Date> highlight2 = new ArrayList<>();
        highlight2.add(0, nextDay2.getTime());


        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int month = cal.get(Calendar.MONTH)+1;
                int year  = cal.get(Calendar.YEAR);
                int day   = cal.get(Calendar.DAY_OF_MONTH);
                Integer dateVal = Integer.valueOf(Integer.toString(year).substring(1)+ String.format("%02d", month)+ String.format("%02d",day));
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
        Query attendeeQuery = FirebaseDatabase.getInstance().getReference(EVENT_LOC)
                .orderByChild("event_date");

        ValueEventListener countListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emptyDates.clear();
                Integer i=0;
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    String key = userSnap.getKey();


                    if (key.contains(eventType)){
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

        View v = inflater.inflate(R.layout.fragment_calendar, container,false);
        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.highlightDates(emptyDates);


    }

    //This method returns an ArrayList of dates between two dates
    public static ArrayList<Date> getDatesBetween(Date startDate, Date endDate) {
        ArrayList<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            result.setHours(0);
            result.setMinutes(0);
            result.setSeconds(0);
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        Log.d("dates", datesInRange.toString());
        return datesInRange;
    }

}