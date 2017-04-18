package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientSessions;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory.SessionScalesAdapterRecycler;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionCardPatientProfile extends RecyclerView.Adapter<SessionCardPatientProfile.MyViewHolder> {

    private Activity context;
    /**
     * Records from that PATIENT
     */
    private ArrayList<SessionFirebase> sessions;

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


    public SessionCardPatientProfile(Activity context, ArrayList<SessionFirebase> sessions, PatientFirebase patient, PatientSessionsFragment patientSessionsFragment) {
        this.context = context;
        this.sessions = sessions;
        /*
      Patient which has these NewEvaluationPrivate.
     */
        PatientSessionsFragment fragment = patientSessionsFragment;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_patient, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SessionFirebase session = sessions.get(position);
        holder.date.setText(DatesHandler.dateToStringWithHour(new Date(session.getDate())));
        List<GeriatricScaleFirebase> sessionScales = FirebaseDatabaseHelper.getScalesFromSession(sessions.get(position));

        holder.testsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.testsList.setLayoutManager(layoutManager);

        SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, sessionScales);
        holder.testsList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        holder.testsList.addItemDecoration(dividerItemDecoration);
        final SessionFirebase currentSession = sessions.get(position);

        View.OnClickListener cardSelected = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Constants.bottomNavigationReviewSession = 0;
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, currentSession);
                FragmentTransitions.replaceFragment(context,
                        new ReviewSingleSessionWithPatient(),
                        args,
                        Constants.tag_review_session_from_patient_profile);
            }
        };
        // add on click listener for the Session
        holder.view.setOnClickListener(cardSelected);
        adapter.setOnClickListener(cardSelected);


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                    alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                alertDialog.setMessage("Deseja eliminar esta Sessão? " + currentSession.getDate());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(holder.view, "Sessão eliminada.", Snackbar.LENGTH_SHORT).show();
                                FirebaseDatabaseHelper.deleteSession(currentSession);

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(holder.view, "Ação cancelada", Snackbar.LENGTH_SHORT).show();

                            }
                        });
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
