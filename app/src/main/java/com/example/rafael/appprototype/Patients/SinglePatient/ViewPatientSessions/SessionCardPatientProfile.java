package com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DatesHandler;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.SessionScalesAdapterRecycler;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.SessionCardHelper;

import java.util.ArrayList;
import java.util.List;

public class SessionCardPatientProfile extends RecyclerView.Adapter<SessionCardPatientProfile.MyViewHolder> {

    /**
     * Patient which has these NewEvaluationPrivate.
     */
    private final Patient patient;
    private final PatientSessionsFragment fragment;
    private Activity context;
    /**
     * Records from that patient
     */
    private ArrayList<Session> sessions;
    private Session session;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        public TextView date;
        public ImageView overflow;
        public RecyclerView testsList;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;

            date = (TextView) view.findViewById(R.id.recordDate);
            testsList = (RecyclerView) view.findViewById(R.id.session_tests_results);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public SessionCardPatientProfile(Activity context, ArrayList<Session> sessions, Patient patient, PatientSessionsFragment patientSessionsFragment) {
        this.context = context;
        this.sessions = sessions;
        this.patient = patient;
        this.fragment = patientSessionsFragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_patient, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        session = sessions.get(position);
        holder.date.setText(DatesHandler.dateToStringWithoutHour(session.getDate()));
        List<GeriatricScale> sessionScales = sessions.get(position).getScalesFromSession();

        holder.testsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.testsList.setLayoutManager(layoutManager);

        SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, sessionScales);
        holder.testsList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        holder.testsList.addItemDecoration(dividerItemDecoration);

        // add on click listener for the Session
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, sessions.get(position));
                FragmentTransitions.replaceFragment(context, new ReviewSingleSessionWithPatient(), args, Constants.tag_review_session_from_patient_profile);
            }
        });

        holder.overflow.setOnClickListener(new SessionCardHelper(holder.overflow, position, context, session, fragment));
    }


    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
