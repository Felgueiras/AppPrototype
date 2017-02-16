package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DatesHandler;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.HistoryCard.ReviewSessionCards;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;

import java.util.Date;
import java.util.List;


/**
 * Show all the Evaluations for a single day.
 */
public class ShowEvaluationsForDay extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;

    public ShowEvaluationsForDay(Activity context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        TextView dateTextView = (TextView) singleDayInfo.findViewById(R.id.dateText);

        // get the date
        Date currentDate = Session.getDifferentSessionDates().get(position);
        dateTextView.setText(DatesHandler.dateToStringWithoutHour(currentDate));
        // get NewEvaluationPrivate for that date
        List<Session> sessionsFromDate = Session.getSessionsFromDate(currentDate);

        // fill the RecyclerView
        RecyclerView recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);
        ReviewSessionCards adapter = new ReviewSessionCards(context, sessionsFromDate);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return singleDayInfo;
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