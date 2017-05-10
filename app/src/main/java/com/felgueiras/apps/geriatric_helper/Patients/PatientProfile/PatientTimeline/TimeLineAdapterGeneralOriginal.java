package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsAdapter;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.TimeLineViewHolderPrescription;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientSessions.SessionScalesAdapterRecycler;
import com.github.vipulasri.timelineview.LineType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapterGeneralOriginal extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity context;
    private final boolean compactView;
    private ArrayList<Object> objectsList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    int TYPE_PRESCRIPTION = 0;
    int TYPE_SESSION = 1;

    public TimeLineAdapterGeneralOriginal(ArrayList<Object> feedList, Orientation orientation, boolean withLinePadding, Activity activity, boolean compactView) {
        objectsList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        context = activity;
        this.compactView = compactView;
    }

    @Override
    public int getItemViewType(int position) {
//        return TimelineView.getTimeLineViewType(position, getItemCount());


        /**
         * Return is XY
         * X - lineType
         * Y - sessions/prescription
         */
        int lineType, objectType;
        if (objectsList.size() == 1) {
            lineType = LineType.ONLYONE;
        } else if (position == 0) {
            lineType = LineType.BEGIN;
        } else if (position == getItemCount() - 1) {
            lineType = LineType.END;
        } else {
            lineType = LineType.NORMAL;
        }

        if (objectsList.get(position) instanceof ArrayList<?>) {
            objectType = TYPE_PRESCRIPTION;
        } else {
            objectType = TYPE_SESSION;
        }

        return 10 * lineType + objectType;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;
        int lineType = viewType / 10;
        int objectType = viewType % 10;


        if (objectType == TYPE_PRESCRIPTION) {
            view = mLayoutInflater.inflate(R.layout.item_timeline_prescription, parent, false);

            return new TimeLineViewHolderPrescription(view, lineType);
        } else if (objectType == TYPE_SESSION) {
            view = mLayoutInflater.inflate(R.layout.item_timeline_session, parent, false);

            return new TimeLineViewHolderSession(view, lineType);
        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (objectsList.get(position) instanceof ArrayList<?>) {

            final ArrayList<PrescriptionFirebase> prescriptionsForDate = (ArrayList<PrescriptionFirebase>) objectsList.get(position);
            TimeLineViewHolderPrescription holderPrescription = (TimeLineViewHolderPrescription) holder;
            configurePrescriptionViewHolder(prescriptionsForDate, holderPrescription);


        } else if (objectsList.get(position) instanceof SessionFirebase) {
            final SessionFirebase session = (SessionFirebase) objectsList.get(position);

            TimeLineViewHolderSession holderSession = (TimeLineViewHolderSession) holder;


            holderSession.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext,
                    R.drawable.pill_black,
                    R.color.colorAccent));

            configureSessionViewHolder(session, holderSession);


        }

    }

    /**
     * Configure a Session ViewHolder.
     *
     * @param session
     * @param holder
     */
    private void configureSessionViewHolder(final SessionFirebase session, TimeLineViewHolderSession holder) {
        holder.mDate.setVisibility(View.VISIBLE);
        holder.mDate.setText(DatesHandler.dateToStringWithoutHour(new Date(session.getDate())));

        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext,
                R.drawable.ic_sessions_white_24dp,
                R.color.colorAccent));


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

    /**
     * Configure a Prescription ViewHolder.
     *
     * @param prescriptionsForDate
     * @param holder
     */
    private void configurePrescriptionViewHolder(final ArrayList<PrescriptionFirebase> prescriptionsForDate,
                                                 TimeLineViewHolderPrescription holder) {
        Date currentDate = prescriptionsForDate.get(0).getDate();
        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext,
                R.drawable.pill_black,
                R.color.colorAccent));

        checkWarnings(holder, prescriptionsForDate);



        holder.date.setText(DatesHandler.dateToStringWithoutHour(currentDate));

        // get recycler view for prescriptions
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        holder.prescriptionsForDate.setLayoutManager(mLayoutManager);
        holder.prescriptionsForDate.setItemAnimator(new DefaultItemAnimator());

        PatientPrescriptionsAdapter mTimeLineAdapter = new PatientPrescriptionsAdapter(context, prescriptionsForDate,
                null, null, compactView);
        holder.prescriptionsForDate.setAdapter(mTimeLineAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                mLayoutManager.getOrientation());
        holder.prescriptionsForDate.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Check if any of the prescriptions for the current date contains associated warnings.
     *
     * @param holder
     * @param prescriptionsForDate
     */
    private void checkWarnings(TimeLineViewHolderPrescription holder, ArrayList<PrescriptionFirebase> prescriptionsForDate) {

        int numWarnings = 0;
        for (PrescriptionFirebase prescription : prescriptionsForDate) {
            if (PrescriptionFirebase.checkWarning(prescription.getName(), null)) {
                numWarnings++;
            }
        }
        if (numWarnings == 0) {
            holder.numberWarnings.setVisibility(View.GONE);
            holder.warning.setVisibility(View.GONE);
        } else {
            holder.numberWarnings.setText(numWarnings + " X ");
        }
    }

    @Override
    public int getItemCount() {
        return (objectsList != null ? objectsList.size() : 0);
    }

}
