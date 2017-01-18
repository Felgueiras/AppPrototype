package com.example.rafael.appprototype.Evaluations.EvaluationsHistory.HistoryCard;

import android.app.Activity;
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
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSession;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class ReviewSessionCards extends RecyclerView.Adapter<ReviewSessionCards.MyViewHolder> {

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
        public ListView testsList;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
            testsList = (ListView) view.findViewById(R.id.session_tests_results);
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context
     * @param sessionsList
     */
    public ReviewSessionCards(Activity context, List<Session> sessionsList) {
        this.context = context;
        this.sessionsList = sessionsList;
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
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        Patient patient = session.getPatient();
        if(patient!=null){
            holder.patientName.setText(patient.getName());
            //holder.age.setText(patient.getAge());
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
                FragmentTransitions.replaceFragment(context,new ReviewSingleSession(), args, Constants.tag_review_session);
            }
        });
        /**
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                //args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                String addToBackStackTag = Constants.tag_view_patien_info_records;
                FragmentTransitions.replaceFragment(getActivity(),ViewSinglePatientInfo.class, args, addToBackStackTag);
            }
        });
         */


        // display the result for the tests
        ShowTestsForSession adapter = new ShowTestsForSession(context, testsFromSession);
        holder.testsList.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
