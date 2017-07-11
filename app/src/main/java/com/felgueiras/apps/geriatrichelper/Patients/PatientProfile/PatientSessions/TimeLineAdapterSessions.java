package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientSessions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.Orientation;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.TimeLineViewHolderSession;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapterSessions extends RecyclerView.Adapter<TimeLineViewHolderSession> {

    private final Activity context;
    private ArrayList<SessionFirebase> sessionsList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapterSessions(ArrayList<SessionFirebase> feedList, Orientation orientation, boolean withLinePadding, Activity activity) {
        sessionsList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        context = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolderSession onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        if (mOrientation == Orientation.HORIZONTAL) {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_horizontal_line_padding : R.layout.item_timeline_horizontal, parent, false);
        } else {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_line_padding_session : R.layout.item_timeline_session, parent, false);
        }

        return new TimeLineViewHolderSession(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolderSession holder, int position) {

        final SessionFirebase session = sessionsList.get(position);

        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));

        holder.mDate.setVisibility(View.VISIBLE);
        holder.mDate.setText(DatesHandler.dateToStringWithHour(new Date(session.getDate()), true));

        View.OnClickListener cardSelected = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Constants.bottomNavigationReviewSession = 3;
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                FragmentTransitions.replaceFragment(context,
                        new ReviewSingleSessionWithPatient(),
                        args,
                        Constants.tag_review_session_from_patient_profile);
            }
        };

        // setup scales list
        List<GeriatricScaleFirebase> sessionScales = FirebaseDatabaseHelper.getScalesFromSession(session);

        holder.sessionScales.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.sessionScales.setLayoutManager(layoutManager);

        SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, sessionScales, true);
        adapter.setOnClickListener(cardSelected);
        holder.sessionScales.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        holder.sessionScales.addItemDecoration(dividerItemDecoration);

        holder.view.setOnClickListener(cardSelected);
    }

    @Override
    public int getItemCount() {
        return (sessionsList != null ? sessionsList.size() : 0);
    }

}
