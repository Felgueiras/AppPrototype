package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.HistoryCard.ShowTestsForSession;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSession;

import java.util.ArrayList;
import java.util.List;

public class CreatePatientSessionCard extends RecyclerView.Adapter<CreatePatientSessionCard.MyViewHolder> {

    /**
     * Patient which has these NewEvaluation.
     */
    private final Patient patient;
    private Context context;
    /**
     * Records from that patient
     */
    private ArrayList<Session> sessions;
    private Session currentSession;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public ImageView overflow;
        public ListView testsList;

        public MyViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.recordDate);
            testsList = (ListView) view.findViewById(R.id.session_tests_results);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public CreatePatientSessionCard(Context context, ArrayList<Session> sessions, Patient patient) {
        this.context = context;
        this.sessions = sessions;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_patient, parent, false);


        // add on click listener for the Session
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewSingleSession.SESSION,currentSession);
                ((MainActivity) context).replaceFragment(ReviewSingleSession.class, args, Constants.tag_review_session);
            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Session session = sessions.get(position);
        holder.date.setText(session.getDate());
        currentSession =sessions.get(position);
        List<GeriatricTest> testsFromSession = currentSession.getTestsFromSession();

        // display the result for the tests
        ShowTestsForSession adapter = new ShowTestsForSession(context, testsFromSession);
        holder.testsList.setAdapter(adapter);
    }





    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
