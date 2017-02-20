package com.example.rafael.appprototype.Evaluations;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.ShowEvaluationsForDay;
import com.example.rafael.appprototype.Patients.NewPatient.CreatePatient;
import com.example.rafael.appprototype.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class EvaluationsHistoryGrid extends Fragment {

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        setHasOptionsMenu(false);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_history_grid, container, false);
        // fill the GridView
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ShowEvaluationsForDay(getActivity(), this));

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_evaluations, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar:
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
        }
        return true;

    }

//    /**
//     * Erase a session from the patient.
//     *
//     * @param index Session index
//     */
//    public void removeSession(int index) {
//        sessionsFromPatient.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
//        adapter.notifyDataSetChanged();
//    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());

            // get Sessions from that date
            List<Session> sessionsFromDate = Session.getSessionsFromDate(c.getTime());
            System.out.println("Sessions from date: " + sessionsFromDate.size());
        }
    }
}