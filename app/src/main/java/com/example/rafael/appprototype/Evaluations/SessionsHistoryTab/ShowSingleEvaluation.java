package com.example.rafael.appprototype.Evaluations.SessionsHistoryTab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.SinglePatient.ViewSinglePatientInfoAndSessions;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class ShowSingleEvaluation extends RecyclerView.Adapter<ShowSingleEvaluation.MyViewHolder> {

    private Context context;
    /**
     * Data to be displayed.
     */
    private List<Session> sessionsList;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName, age;
        public ImageView photo, overflow;
        public ListView testsList;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
            age = (TextView) view.findViewById(R.id.patientAge);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            testsList = (ListView) view.findViewById(R.id.session_tests_results);
        }
    }

    /**
     * Constructor of the ShowSingleEvaluation
     *
     * @param context
     * @param sessionsList
     */
    public ShowSingleEvaluation(Context context, List<Session> sessionsList) {
        this.context = context;
        this.sessionsList = sessionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_card, parent, false);
        return new MyViewHolder(itemView);
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

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                //args.putSerializable(ViewSinglePatientInfoAndSessions.PATIENT, patient);
                String addToBackStackTag = Constants.tag_view_patien_info_records;
                ((MainActivity) context).replaceFragment(ViewSinglePatientInfoAndSessions.class, args, addToBackStackTag);
            }
        });



        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        */
        // display the result for the tests
        ShowTestsForEvaluation adapter = new ShowTestsForEvaluation(context, testsFromSession);
        holder.testsList.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
