package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.Beers.BeersCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions.Old.PatientPrescriptionsFragment;
import com.felgueiras.apps.geriatric_helper.Prescription.PrescriptionSingleDrugFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

public class PatientPrescriptionsCard extends RecyclerView.Adapter<PatientPrescriptionsCard.MyViewHolder> {

    private final PatientPrescriptionsFragment fragment;
    private final boolean compactView;
    private Activity context;
    /**
     * Records from that PATIENT
     */
    private ArrayList<PrescriptionFirebase> prescriptions;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView name;
        private final EditText notes;
        public TextView date;
        public ImageView removePrescription;
        public Button warning;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            notes = (EditText) view.findViewById(R.id.prescriptionNotes);
            name = (TextView) view.findViewById(R.id.prescriptionName);
            date = (TextView) view.findViewById(R.id.prescriptionDate);
            removePrescription = (ImageView) view.findViewById(R.id.removePrescription);
            warning = (Button) view.findViewById(R.id.warning);
        }
    }


    public PatientPrescriptionsCard(Activity context,
                                    ArrayList<PrescriptionFirebase> prescriptions,
                                    PatientFirebase patient,
                                    PatientPrescriptionsFragment patientSessionsFragment,
                                    boolean compactView) {
        this.context = context;
        this.prescriptions = prescriptions;
        this.fragment = patientSessionsFragment;
        this.compactView = compactView;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_patient_prescription, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PrescriptionFirebase prescription = prescriptions.get(position);


        // set views
        holder.name.setText(prescription.getName());
        if (compactView) {
            holder.notes.setVisibility(View.GONE);
            holder.warning.setVisibility(View.GONE);
        } else {

            holder.notes.setText(prescription.getNotes());
//            holder.date.setText("Data de prescrição: " + DatesHandler.dateToStringWithHour(prescription.getDate(), true));

            // allow editing notes
            holder.notes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    prescription.setNotes(charSequence.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    FirebaseDatabaseHelper.updatePrescription(prescription);
                }
            });

            PrescriptionFirebase.checkWarning(prescription.getName(), holder.warning);


        }


//        holder.removePrescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // remove prescription from patient's prescriptions
//                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
////                    alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
//                alertDialog.setMessage("Deseja eliminar esta Prescrição?");
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Snackbar.make(holder.view, "Prescrião eliminada.", Snackbar.LENGTH_SHORT).show();
//                                FirebaseDatabaseHelper.deletePrescription(prescription, context);
//
//                                // refresh the adapter
//                                fragment.removePrescription(prescription);
//                            }
//                        });
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Snackbar.make(holder.view, "Ação cancelada", Snackbar.LENGTH_SHORT).show();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });

        /**
         * Setup warning button
         */
        holder.warning.setBackgroundResource(R.drawable.ic_warning_24dp);


        holder.warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // view "profile" for that drug
                Fragment endFragment = new PrescriptionSingleDrugFragment();
                Bundle args = new Bundle();
                args.putString(PrescriptionSingleDrugFragment.DRUG, prescription.getName());
                FragmentTransitions.replaceFragment(context, endFragment, args, Constants.tag_view_drug_info);
            }
        });

    }


    @Override
    public int getItemCount() {
        return prescriptions.size();
    }
}
