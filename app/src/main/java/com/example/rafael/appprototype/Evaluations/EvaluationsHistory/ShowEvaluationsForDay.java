package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

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
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;

import java.util.List;


/**
 * Show all the Evaluations for a single day.
 */
public class ShowEvaluationsForDay extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    public ShowEvaluationsForDay(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        TextView dateTextView = (TextView) singleDayInfo.findViewById(R.id.dateText);

        // get the date
        Session session = Session.getSessionDates().get(position);
        String currentDate = session.getDate();
        dateTextView.setText(currentDate);
        // get NewEvaluation for that date
        List<Session> sessionsFromDate = Session.getSessionsFromDate(currentDate);

        // fill the RecyclerView
        RecyclerView recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);
        ShowSingleEvaluation adapter = new ShowSingleEvaluation(context, sessionsFromDate);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return singleDayInfo;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public int getCount() {
        // get number of different dates where patients is not null
        int numDates = 0;
        for (int i = 0; i < Session.getSessionDates().size(); i++) {
            String currentDate = Session.getSessionDates().get(i).getDate();
            // get NewEvaluation for that date
            List<Session> sessionsFromDate = Session.getSessionsFromDate(currentDate);
            for (Session sess : sessionsFromDate) {
                //if(sess.getPatient()!=null)
                //{
                numDates++;
                break;
                //}
            }
        }

        return numDates;
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