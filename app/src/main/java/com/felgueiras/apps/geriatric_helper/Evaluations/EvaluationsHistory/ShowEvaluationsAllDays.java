package com.felgueiras.apps.geriatric_helper.Evaluations.EvaluationsHistory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Evaluations.EvaluationsAll;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.Date;
import java.util.List;


/**
 * Show all the Evaluations for a single day.
 */
public class ShowEvaluationsAllDays extends BaseAdapter {
    private final EvaluationsAll fragment;
    Activity context;
    LayoutInflater inflater;
    private List<SessionFirebase> sessionsFromDate;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<SessionCardEvaluationHistory.MyViewHolder> adapter;

    public ShowEvaluationsAllDays(Activity context, EvaluationsAll evaluationsHistoryGrid) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = evaluationsHistoryGrid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        Button dateTextView = (Button) singleDayInfo.findViewById(R.id.dateText);

        // get the date
        Date currentDate = FirebaseHelper.getDifferentSessionDates().get(position);
        dateTextView.setText(DatesHandler.dateToStringWithoutHour(currentDate));
        sessionsFromDate = FirebaseHelper.getSessionsFromDate(currentDate);

        // fill the RecyclerView
        recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);

        adapter = new SessionCardEvaluationHistory(context, sessionsFromDate);
        // create Layout
        int numbercolumns = 1;
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            numbercolumns = 2;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        return singleDayInfo;
    }

    /**
     * Erase a session from the PATIENT.
     *
     * @param index Session index
     */
    public void removeSession(int index) {
        sessionsFromDate.remove(index);
        recyclerView.removeViewAt(index);
        adapter.notifyItemRemoved(index);
        adapter.notifyItemRangeChanged(index, sessionsFromDate.size());
        adapter.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return FirebaseHelper.getDifferentSessionDates().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}