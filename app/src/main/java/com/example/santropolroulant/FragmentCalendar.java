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

import com.savvi.rangedatepicker.CalendarPickerView;

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
        Integer dateVal = Integer.valueOf(Integer.toString(year).substring(1)+Integer.toString(month)+Integer.toString(day));
        listener.onInputASent(todayDate,dateVal);


        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.init(today, nextYear.getTime()).withSelectedDate(today);
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
}