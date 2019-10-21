
package com.example.santropolroulant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;


public class TimePreference extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;
    private FirebaseAuth mAuth;
    private CardView crdMorning, crdAfternoon;
    private TextView tvTitle3;
    private Button btnNext3;
    String timePref;

    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_preference);
        mAuth = FirebaseAuth.getInstance();
        setupUIViews();

        crdMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitam";
                crdMorning.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#D3D163DA"));
            }
        });

        crdAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePref = "kitpm";
                crdAfternoon.setCardBackgroundColor(Color.parseColor("#B128B8"));
                crdMorning.setCardBackgroundColor(Color.parseColor("#D3D163DA"));
            }
        });

        btnNext3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (timePref != null) {
                    datePickerDialog = DatePickerDialog.newInstance(TimePreference.this, year, month, day);
                    datePickerDialog.setThemeDark(true);
                    datePickerDialog.showYearPickerFirst(false);
                    datePickerDialog.setOkText("Done");
                    c.add(Calendar.DAY_OF_YEAR, 0);
                    datePickerDialog.setMaxDate(c);
                    c.add(Calendar.DAY_OF_YEAR, 21);
                    datePickerDialog.setMaxDate(c);

                    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialogInterface) {

                            Toast.makeText(TimePreference.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");

                    Intent intent = new Intent(TimePreference.this, EventsCalendar.class);
                    intent.putExtra("event_type", timePref);

                }
            }
        });

    }


    private void setupUIViews() {
        tvTitle3 = findViewById(R.id.tvTitle3);
        btnNext3 = findViewById(R.id.btnNext3);

        crdMorning = findViewById(R.id.crdMorning);
        crdAfternoon = findViewById(R.id.crdAfternoon);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date = "Date: " + Day + "/" + (Month + 1) + "/" + Year;

        Toast.makeText(TimePreference.this, date, Toast.LENGTH_LONG).show();

    }

}





