package com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


/**
 * Show all the Evaluations for a single initialTimestamp.
 */
public class SessionsSingleDay extends BaseAdapter {
    private final long initialTimestamp;
    Activity context;
    LayoutInflater inflater;

    public SessionsSingleDay(Activity context, long day) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.initialTimestamp = day;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        TextView dateTextView = (TextView) singleDayInfo.findViewById(R.id.dateText);

        dateTextView.setText(DatesHandler.dateToStringWithoutHour(new Date(initialTimestamp)));
        // get NewEvaluationPrivate for that initialTimestamp

        // fill the RecyclerView
        RecyclerView recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);

        // create Layout
        int numbercolumns = 1;
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            numbercolumns = 2;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // fetch sessions and then set the adapter
        fetchSessionsFromDay(initialTimestamp, recyclerView);
        return singleDayInfo;
    }


    private void fetchSessionsFromDay(long startDate, final RecyclerView recyclerView) {
        long endDate = startDate + 86400000;

        FirebaseHelper.firebaseTableSessions.orderByChild("date").startAt(startDate).endAt(endDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SessionFirebase> sessionsFromDate = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase session = postSnapshot.getValue(SessionFirebase.class);
                    session.setKey(postSnapshot.getKey());
                    Log.d("Session", session.getDate()+"");
                    sessionsFromDate.add(session);
                }
                Log.d("Firebase","Retrieved sessions from initialTimestamp patients");
                SessionCardEvaluationHistory adapter = new SessionCardEvaluationHistory(context, sessionsFromDate);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

    }

    /**
     * Erase a session from the PATIENT.
     *
//     * @param index Session index
     */
//    public void removeSession(int index) {
//        sessionsFromDate.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromDate.size());
//        adapter.notifyDataSetChanged();
//    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}