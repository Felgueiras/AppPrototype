package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryGrid;
import com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleSession;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class SessionCardEvaluationHistory extends RecyclerView.Adapter<SessionCardEvaluationHistory.MyViewHolder> {

    private final EvaluationsHistoryGrid fragment;
    private Activity context;
    /**
     * Data to be displayed.
     */
    private List<Session> sessionsList;
    /**
     * View that holds the current evaluation.
     */
    private View evaluationView;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public ImageView photo, overflow;
        public RecyclerView testsList;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
//            photo = (ImageView) view.findViewById(R.id.patientPhoto);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
            testsList = (RecyclerView) view.findViewById(R.id.recyclerview);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param sessionsList
     * @param fragment
     */
    public SessionCardEvaluationHistory(Activity context, List<Session> sessionsList, EvaluationsHistoryGrid fragment) {
        this.context = context;
        this.sessionsList = sessionsList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        evaluationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session_history, parent, false);
        return new MyViewHolder(evaluationView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the current Session and tests from that Session
        final Session session = sessionsList.get(position);
        List<GeriatricScale> scalesFromSession = session.getScalesFromSession();
        Patient patient = session.getPatient();
        if (patient != null) {
            holder.patientName.setText(patient.getName());
            // loading album cover using Glide library
            //Glide.with(context).load(patient.getPicture()).into(holder.photo);
        }

        /**
         * Review a Session.
         */
        evaluationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewSingleSession.SESSION, session);
                FragmentTransitions.replaceFragment(context, new ReviewSingleSession(), args, Constants.tag_review_session);
            }
        });

        /**
         * Setup list.
         */
        holder.testsList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.testsList.setLayoutManager(layoutManager);

        SessionScalesAdapterRecycler adapter = new SessionScalesAdapterRecycler(context, scalesFromSession);
        holder.testsList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        holder.testsList.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
