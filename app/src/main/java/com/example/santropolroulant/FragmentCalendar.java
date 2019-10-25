package com.example.santropolroulant;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.savvi.rangedatepicker.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class FragmentCalendar extends Fragment {
    private FragmentCalendarListener listener;

    public interface FragmentCalendarListener{
        void onInputASent(String date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container,false);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        List<Date> dates = new ArrayList<Date>();

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 5);
        dt = c.getTime();
        dates.add(dt);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(2);




        final CalendarPickerView calendar = (CalendarPickerView) v.findViewById(R.id.calendar);
        calendar.init(today, nextYear.getTime()).withSelectedDate(today).withDeactivateDates(list);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date);

                Calendar calSelected = Calendar.getInstance();
                calSelected.setTime(date);

                String selectedDate = "" + calSelected.get(Calendar.DAY_OF_MONTH)
                        + " " + (calSelected.get(Calendar.MONTH) + 1)
                        + " " + calSelected.get(Calendar.YEAR);
                listener.onInputASent(selectedDate);
            }
            @Override
            public void onDateUnselected(Date date) {

            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCalendarListener) {
            listener = (FragmentCalendarListener) context;
        }
        else{
            throw new RuntimeException(context.toString()+" must implement FragmentCalendarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}