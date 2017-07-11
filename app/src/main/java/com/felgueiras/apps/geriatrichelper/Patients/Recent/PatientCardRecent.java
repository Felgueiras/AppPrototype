package com.felgueiras.apps.geriatrichelper.Patients.Recent;

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

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatrichelper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientProfileFragment;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.Date;
import java.util.List;

/**
 * Display the resume of an Evaluation.
 */
public class PatientCardRecent extends RecyclerView.Adapter<PatientCardRecent.MyViewHolder> {

    private Activity context;
    /**
     * Data to be displayed.
     */
    private List<SessionFirebase> sessionsList;
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
            name = view.findViewById(R.id.patientName);
            time = view.findViewById(R.id.sessionTime);
            icon = view.findViewById(R.id.patientIcon);
            card = view.findViewById(R.id.patientCard);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *  @param context
     * @param sessionsList
     * @param fragment
     */
    public PatientCardRecent(Activity context, List<SessionFirebase> sessionsList, PatientsRecent fragment) {
        this.context = context;
        this.sessionsList = sessionsList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        patientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_recent, parent, false);
        return new MyViewHolder(patientView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the current Session and tests from that Session
        final SessionFirebase session = sessionsList.get(position);
//        List<GeriatricScale> scalesFromSession = session.getScalesFromSession();
        final PatientFirebase patient = PatientsManagement.getInstance().getPatientFromSession(session, context);
        if(patient!=null)
        {
            holder.name.setText(patient.getName());
            // loading album cover using Glide library
            //Glide.with(context).load(patientView.getPicture()).into(holder.icon);
            switch (patient.getGender()) {
                case Constants.MALE:
                    holder.icon.setImageResource(R.drawable.male);
                    break;
                case Constants.FEMALE:
                    holder.icon.setImageResource(R.drawable.female);
                    break;
            }

            // set session time
            holder.time.setText(DatesHandler.hour(new Date(session.getDate())));
        }


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment endFragment = new PatientProfileFragment();
//                    patientTransitionName = holder.questionTextView.getTransitionName();
                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(PatientProfileFragment.PATIENT, patient);
                ((PrivateAreaActivity) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patient_info_records,
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
