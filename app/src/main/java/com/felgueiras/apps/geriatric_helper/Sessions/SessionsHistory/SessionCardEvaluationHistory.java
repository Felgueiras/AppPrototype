package com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SessionCardHelper;

import java.util.Date;
import java.util.List;


public class SessionCardEvaluationHistory extends RecyclerView.Adapter<SessionCardEvaluationHistory.MyViewHolder> {

    private Activity context;
    /**
     * Data to be displayed.
     */
    private List<SessionFirebase> sessionsList;

    /**
     * Create a View
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public TextView patientName;
        public ImageView overflow;
        public RecyclerView testsForDayRecycler;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            testsForDayRecycler = (RecyclerView) view.findViewById(R.id.recyclerview);
            card = (CardView) view.findViewById(R.id.sessionHistoryCard);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param sessionsList
     */
    public SessionCardEvaluationHistory(Activity context, List<SessionFirebase> sessionsList) {
        this.context = context;
        this.sessionsList = sessionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
      View that holds the current evaluation.
     */
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String infoType = SP.getString(context.getResources().getString(R.string.sessionResumeInformation), "3");
        View evaluationView = null;
        if (infoType.equals("2") || infoType.equals("1")) {
            evaluationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_history_patient_scale, parent, false);
        } else {
            evaluationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_history_patient_scale_result, parent, false);
        }

        return new MyViewHolder(evaluationView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the current Session and tests from that Session
        final SessionFirebase session = sessionsList.get(position);
        List<GeriatricScaleFirebase> scalesFromSession = FirebaseHelper.getScalesFromSession(session);
        PatientFirebase patient = FirebaseHelper.getPatientFromSession(session);
        if (patient != null) {
            holder.patientName.setText(patient.getName() + " - " + DatesHandler.hour(new Date(session.getDate())));
            // loading album cover using Glide library
            //Glide.with(context).load(PATIENT.getPicture()).into(holder.icon);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Review", "Clicked");
                Bundle args = new Bundle();
                Constants.bottomNavigationReviewSession = 0;
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                FragmentTransitions.replaceFragment(context, new ReviewSingleSessionWithPatient(), args, Constants.tag_review_session_from_sessions_list);
            }
        };

        /**
         * Review a Session.
         */
        holder.card.setOnClickListener(clickListener);

        /**
         * Setup list.
         */
        holder.testsForDayRecycler.setHasFixedSize(true);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String infoType = SP.getString(context.getResources().getString(R.string.sessionResumeInformation), "3");
        if (infoType.equals("2")) {
            /**
             * Patient questionTextView + scale.
             */
            holder.testsForDayRecycler.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.testsForDayRecycler.setLayoutManager(layoutManager);

            SessionScalesAdapterRecyclerIcons adapter = new SessionScalesAdapterRecyclerIcons(context, scalesFromSession);
            adapter.setOnClickListener(clickListener);
            holder.testsForDayRecycler.setAdapter(adapter);

        } else if (infoType.equals("3")) {
            /**
             * Patient questionTextView + scale + result.
             */
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            holder.testsForDayRecycler.setLayoutManager(layoutManager);

            SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, scalesFromSession);
            adapter.setOnClickListener(clickListener);
            holder.testsForDayRecycler.setAdapter(adapter);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                    layoutManager.getOrientation());
            holder.testsForDayRecycler.addItemDecoration(dividerItemDecoration);

        }

        holder.overflow.setOnClickListener(
                new SessionCardHelper(holder.overflow,
                        position,
                        context,
                        session));

    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
