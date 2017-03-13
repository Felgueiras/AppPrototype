package com.example.rafael.appprototype.Patients.Recent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.Patients.ViewPatients.PatientsRecent;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class PatientCardEvaluationHistory extends RecyclerView.Adapter<PatientCardEvaluationHistory.MyViewHolder> {

    private final PatientsRecent fragment;
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
        public TextView patientName;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.patientName);
//            photo = (ImageView) view.findViewById(R.id.patientPhoto);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param sessionsList
     * @param fragment
     */
    public PatientCardEvaluationHistory(Activity context, List<Session> sessionsList, PatientsRecent fragment) {
        this.context = context;
        this.sessionsList = sessionsList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        patientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_patient_history, parent, false);
        return new MyViewHolder(patientView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the current Session and tests from that Session
        final Session session = sessionsList.get(position);
        List<GeriatricScale> scalesFromSession = session.getScalesFromSession();
        final Patient patient = session.getPatient();
        if (patient != null) {
            holder.patientName.setText(patient.getName());
            // loading album cover using Glide library
            //Glide.with(context).load(patientView.getPicture()).into(holder.photo);
        }

        /**
         * Review a Session.
         */
        this.patientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Pick a patientView to be associated with a Session.
                 */


                // TODO add shared elements for transitions
                Fragment endFragment = new ViewSinglePatientInfo();
                    /*
                    endFragment.setSharedElementReturnTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));


                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));
                    */


//                    patientTransitionName = holder.name.getTransitionName();
                Bundle args = new Bundle();
//                args.putString("ACTION", holder.name.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                ((PrivateArea) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patient_info_records,
                        holder.patientName);


            }
        });
    }


    @Override
    public int getItemCount() {
        return sessionsList.size();
    }
}
