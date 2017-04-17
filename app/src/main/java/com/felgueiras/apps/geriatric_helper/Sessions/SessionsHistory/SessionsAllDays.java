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
import android.widget.Button;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Sessions.EvaluationsAllFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


/**
 * Show all the Evaluations for a single day.
 */
public class SessionsAllDays extends BaseAdapter {
    Activity context;
    LayoutInflater inflater;


    public SessionsAllDays(Activity context, EvaluationsAllFragment evaluationsHistoryGrid) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        EvaluationsAllFragment fragment = evaluationsHistoryGrid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleDayInfo = inflater.inflate(R.layout.content_sessions_history, null);
        Button dateTextView = (Button) singleDayInfo.findViewById(R.id.dateText);

        // get the date
        Date currentDate = FirebaseDatabaseHelper.getDifferentSessionDates().get(position);
        Log.d("Sessions", "Current date: " + currentDate);
        dateTextView.setText(DatesHandler.dateToStringWithoutHour(currentDate));
//        sessionsFromDate = FirebaseHelper.getSessionsFromDate(currentDate);

        // fill the RecyclerView
        RecyclerView recyclerView = (RecyclerView) singleDayInfo.findViewById(R.id.recycler_view_sessions_day);

//        adapter = new SessionCardEvaluationHistory(context, sessionsFromDate);
        // create Layout
        int numbercolumns = 1;
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            numbercolumns = 2;
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchSessionsFromDay(currentDate, recyclerView);
//        recyclerView.setAdapter(adapter);


        return singleDayInfo;
    }


    private void fetchSessionsFromDay(final Date date, final RecyclerView recyclerView) {
        long startDate = date.getTime();
        long endDate = startDate + 86400000;
        Log.d("Firebase", startDate + "-" + endDate);

        FirebaseHelper.firebaseTableSessions.orderByChild("date").startAt(startDate).endAt(endDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SessionFirebase> sessionsFromDate = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase session = postSnapshot.getValue(SessionFirebase.class);
                    session.setKey(postSnapshot.getKey());
                    Log.d("Session", session.getDate() + "");
                    sessionsFromDate.add(session);
                }
                Log.d("Sessions", "Sessions for date: " + date +
                        "\nSize: " + sessionsFromDate.size());
                RecyclerView.Adapter<SessionCardEvaluationHistory.MyViewHolder> adapter;
                adapter = new SessionCardEvaluationHistory(context, sessionsFromDate);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

    }


    @Override
    public int getCount() {
        return FirebaseDatabaseHelper.getDifferentSessionDates().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * Erase a session from the PATIENT.
     *
     * @param index Session index
     */
    public void removeSession(int index) {
//        sessionsFromDate.remove(index);
//        recyclerView.removeViewAt(index);
//        adapter.notifyItemRemoved(index);
//        adapter.notifyItemRangeChanged(index, sessionsFromDate.size());
//        adapter.notifyDataSetChanged();
    }
}