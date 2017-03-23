package com.example.rafael.appprototype.Patients.Recent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class PatientCardRecent extends RecyclerView.Adapter<PatientCardRecent.MyViewHolder> {

    private Activity context;
    /**
     * Data to be displayed.
     */
    private List<Session> sessionsList;
    /**
     * View that holds the current evaluation.
     */
    private View patientView;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout card;
        public TextView name, time;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            time = (TextView) view.findViewById(R.id.sessionTime);
            icon = (ImageView) view.findViewById(R.id.patientIcon);
            card = (LinearLayout) view.findViewById(R.id.patientCard);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param sessionsList
     * @param fragment
     */
    public PatientCardRecent(Activity context, List<Session> sessionsList, PatientsRecent fragment) {
        this.context = context;
        this.sessionsList = sessionsList;
        PatientsRecent fragment1 = fragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        patientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_recent, parent, false);
        return new MyViewHolder(patientView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the current Session and tests from that Session
        final Session session = sessionsList.get(position);
//        List<GeriatricScale> scalesFromSession = session.getScalesFromSession();
        final Patient patient = session.getPatient();
        if(patient!=null)
        {
            holder.name.setText(patient.getName());
            // loading album cover using Glide library
            //Glide.with(context).load(patientView.getPicture()).into(holder.photo);
            switch (patient.getGender()) {
                case Constants.MALE:
                    holder.icon.setImageResource(R.drawable.male);
                    break;
                case Constants.FEMALE:
                    holder.icon.setImageResource(R.drawable.female);
                    break;
            }

            // set session time
            holder.time.setText(DatesHandler.hour(session.getDate()));
        }


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment endFragment = new ViewSinglePatientInfo();
//                    patientTransitionName = holder.questionTextView.getTransitionName();
                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                ((PrivateArea) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patient_info_records,
                        holder.name);
            }
        };

        /**
         * Review a Session.
         */
        this.patientView.setOnClickListener(clickListener);
        holder.icon.setOnClickListener(clickListener);

    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
