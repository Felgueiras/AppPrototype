package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

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
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryGrid;
import com.example.rafael.appprototype.R;

import java.util.Date;
import java.util.List;


/**
 * Show all the Evaluations for a single day.
 */
public class ShowEvaluationsAllDays extends BaseAdapter {
    private final EvaluationsHistoryGrid fragment;
    Activity context;
    LayoutInflater inflater;
    private List<Session> sessionsFromDate;
    private RecyclerView recyclerView;
    private SessionCardEvaluationHistory adapter;

    public ShowEvaluationsAllDays(Activity context, EvaluationsHistoryGrid evaluationsHistoryGrid) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = evaluationsHistoryGrid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        TextView dateTextView = (TextView) singleDayInfo.findViewById(R.id.dateText);

        // get the date
        Date currentDate = Session.getDifferentSessionDates().get(position);
        dateTextView.setText(DatesHandler.dateToStringWithoutHour(currentDate));
        // get NewEvaluationPrivate for that date
        sessionsFromDate = Session.getSessionsFromDate(currentDate);

        // fill the RecyclerView
        recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);
        adapter = new SessionCardEvaluationHistory(context, sessionsFromDate, fragment);

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
     * Erase a session from the patient.
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
        return Session.getDifferentSessionDates().size();
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