package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsCard;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.TimeLineViewHolderDailyEvents;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.TimeLineViewHolderPrescription;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory.SessionScalesAdapterRecycler;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapterGeneral extends RecyclerView.Adapter<TimeLineViewHolderDailyEvents> {

    private final Activity context;
    private final boolean compactView;
    private ArrayList<DailyEvents> dailyEvents;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    int TYPE_PRESCRIPTION = 0;
    int TYPE_SESSION = 1;

    public TimeLineAdapterGeneral(ArrayList<DailyEvents> feedList, Orientation orientation, boolean withLinePadding, Activity activity, boolean compactView) {
        dailyEvents = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        context = activity;
        this.compactView = compactView;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());

    }


    @Override
    public TimeLineViewHolderDailyEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;


        view = mLayoutInflater.inflate(R.layout.item_timeline_daily_events, parent, false);

        return new TimeLineViewHolderDailyEvents(view, viewType);


    }

    @Override
    public void onBindViewHolder(TimeLineViewHolderDailyEvents holder, int position) {

        // get the daily event
        DailyEvents dailyEvent = this.dailyEvents.get(position);

        configureDailyEventsViewHolder(dailyEvent, holder);
    }

    /**
     * Configure a Prescription ViewHolder.
     *
     * @param dailyEvent
     * @param holder
     */
    private void configureDailyEventsViewHolder(DailyEvents dailyEvent, TimeLineViewHolderDailyEvents holder) {
        Date currentDate = dailyEvent.getDate();
        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext,
                R.drawable.marker,
                R.color.colorAccent));


        holder.date.setText(DatesHandler.dateToStringWithoutHour(currentDate));

        // get recycler view for prescriptions
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        holder.prescriptionsForDate.setLayoutManager(mLayoutManager);
        holder.prescriptionsForDate.setItemAnimator(new DefaultItemAnimator());

        PatientPrescriptionsCard prescriptionsAdapter = new PatientPrescriptionsCard(context, dailyEvent.getPrescriptions(),
                null, null, compactView);
        holder.prescriptionsForDate.setAdapter(prescriptionsAdapter);

        // get recycler view for sessions
        RecyclerView.LayoutManager sessionsLayoutManager = new GridLayoutManager(context, 1);
        holder.sessionsForDate.setLayoutManager(sessionsLayoutManager);
        holder.sessionsForDate.setItemAnimator(new DefaultItemAnimator());

        PatientSessionCard sessionsAdapter = new PatientSessionCard(context, dailyEvent.getSessions(),
                null, null);
        holder.sessionsForDate.setAdapter(sessionsAdapter);
    }

    @Override
    public int getItemCount() {
        return (dailyEvents != null ? dailyEvents.size() : 0);
    }

}
