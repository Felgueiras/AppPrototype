package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsFragment;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory.SessionScalesAdapterRecycler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientSessionCard extends RecyclerView.Adapter<TimeLineViewHolderSession> {

    private Activity context;

    private ArrayList<SessionFirebase> sessions;


    public PatientSessionCard(Activity context,
                              ArrayList<SessionFirebase> sessions,
                              PatientFirebase patient,
                              PatientPrescriptionsFragment patientSessionsFragment) {
        this.context = context;
        this.sessions = sessions;
//        this.fragment = patientSessionsFragment;
    }


    @Override
    public TimeLineViewHolderSession onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_session, parent, false);

        return new TimeLineViewHolderSession(itemView, 0);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolderSession holder, int position) {
        final SessionFirebase session = sessions.get(position);

        // hide unnecessary views
        holder.mTimelineView.setVisibility(View.GONE);
        holder.mDate.setVisibility(View.GONE);

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

        SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, sessionScales, false);
        adapter.setOnClickListener(cardSelected);
        holder.sessionScales.setAdapter(adapter);

        holder.view.setOnClickListener(cardSelected);

    }


    @Override
    public int getItemCount() {
        Log.d("Daily", "Sessions " + sessions.size());
        return sessions.size();
    }
}
