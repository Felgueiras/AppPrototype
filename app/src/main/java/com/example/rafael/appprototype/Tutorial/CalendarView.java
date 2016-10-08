package com.example.rafael.appprototype.Tutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rafael.appprototype.R;

public class CalendarView extends AppCompatActivity {
    android.widget.CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_calendar);

        //initializes the calendarview
        initializeCalendar();
    }

    public void initializeCalendar() {
        calendar = (android.widget.CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to CalendarView.
        // here we set Monday as the first day of the CalendarView
        calendar.setFirstDayOfWeek(2);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }
}