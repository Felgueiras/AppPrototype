package com.example.rafael.appprototype.Evaluations;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.ShowEvaluationsAllDays;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.ShowEvaluationsSingleDay;
import com.example.rafael.appprototype.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class EvaluationsHistoryGrid extends Fragment {

    private final ViewPager viewPager;
    private ListAdapter adapter;
    private GridView gridView;

    public EvaluationsHistoryGrid(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_history_grid, container, false);
        // fill the GridView
        gridView =  (GridView) view.findViewById(R.id.gridView);
        adapter = new ShowEvaluationsAllDays(getActivity(), this);
        gridView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(viewPager.getCurrentItem() == 1)
        {
            inflater.inflate(R.menu.menu_evaluations, menu);
            Log.d("Menu","Evaluations");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar:
                DialogFragment picker = new DatePickerFragment(gridView, this);
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


        private final GridView gridView;
        private final EvaluationsHistoryGrid fragment;

        public DatePickerFragment(GridView gridView, EvaluationsHistoryGrid evaluationsHistoryGrid) {
            this.gridView = gridView;
            this.fragment = evaluationsHistoryGrid;
        }

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
            Log.d("Date", sessionsFromDate.size() + "");
            /**
             * Filter by date.
             */
            ListAdapter adapter = new ShowEvaluationsSingleDay(getActivity(), fragment, c.getTime());
            gridView.setAdapter(adapter);

//                sessionsFromPatient.remove(index);
//                recyclerView.removeViewAt(index);
//                adapter.notifyItemRemoved(index);
//                adapter.notifyItemRangeChanged(index, sessionsFromPatient.size());
//                adapter.notifyDataSetChanged();
        }
    }
}