package com.example.rafael.appprototype.Evaluations;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.util.Calendar;
import java.util.List;

public class EvaluationsMainFragment extends Fragment {
    private int year;
    private int month;
    private int day;

    // TODO search by date, calendar view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);


        // Inflate the layout for this fragment
        View v = null;
        if (Constants.sessionID == null) {
            /*
            v = inflater.inflate(R.layout.content_evaluations_history, container, false);
            getActivity().setTitle(getResources().getString(R.string.tab_sessions));

            Button firstDate = (Button) v.findViewById(R.id.firstDate);

            // fill the GridView
            GridView gridView = (GridView) v.findViewById(R.id.gridView);
            gridView.setAdapter(new ShowEvaluationsForDay(getActivity()));

            firstDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(getActivity(), myDateListener, year, month, day).show();
                }
            });
            */
            /**
             * Calendar view.
             */
            v = inflater.inflate(R.layout.content_evaluations_history_calendar, container, false);
            CalendarView calendar = (CalendarView) v.findViewById(R.id.evaluationsCalendar);
            calendar.setFirstDayOfWeek(2);
            calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                    // check if we have evaluations from this date
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day--);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    List<Session> sessionsFromDate = Session.getSessionsFromDate(cal.getTime());

                    // display sessions from that day
                }
            });


            // FAB
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_evaluation);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create a new Session - switch to CreatePatient Fragment
                    Bundle args = new Bundle();
                    ((MainActivity) getActivity()).replaceFragment(new NewEvaluation(), args, Constants.create_session);
                }
            });
        } else {
            /**
             * Resume the ongoing session.
             */
            Bundle args = new Bundle();
            ((MainActivity) getActivity()).replaceFragment(new NewEvaluation(), args, Constants.create_session);
        }

        return v;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        //dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        System.out.println(year + "");
    }
}

