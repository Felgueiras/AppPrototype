package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSessionFragment;

import java.util.ArrayList;

public class ViewPatientSessionsAdapter extends RecyclerView.Adapter<ViewPatientSessionsAdapter.MyViewHolder> {

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

        public MyViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.recordDate);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Constructor of the ShowSingleEvaluation
     *
     * @param context
     * @param sessions
     * @param patient
     */
    public ViewPatientSessionsAdapter(Context context, ArrayList<Session> sessions, Patient patient) {
        this.context = context;
        this.sessions = sessions;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_record_info, parent, false);


        // add on click listener for the Session
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewSessionFragment.patientObject, patient);
                args.putSerializable(ReviewSessionFragment.SESSION,currentSession);
                ((MainActivity) context).replaceFragment(ReviewSessionFragment.class, args, Constants.tag_review_session);
            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Session session = sessions.get(position);
        holder.date.setText(session.getDate());
        currentSession =sessions.get(position);
    }





    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
