package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.Beers.BeersCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

public class PatientPrescriptionsCard extends RecyclerView.Adapter<PatientPrescriptionsCard.MyViewHolder> {

    private final PatientPrescriptionsFragment fragment;
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
        private final TextView notes, name;
        public TextView date;
        public ImageView removePrescription;
        public Button warning;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            notes = (TextView) view.findViewById(R.id.prescriptionNotes);
            name = (TextView) view.findViewById(R.id.prescriptionName);
            date = (TextView) view.findViewById(R.id.prescriptionDate);
            removePrescription = (ImageView) view.findViewById(R.id.removePrescription);
            warning = (Button) view.findViewById(R.id.warning);
        }
    }


    public PatientPrescriptionsCard(Activity context, ArrayList<PrescriptionFirebase> prescriptions, PatientFirebase patient, PatientPrescriptionsFragment patientSessionsFragment) {
        this.context = context;
        this.prescriptions = prescriptions;
        this.fragment = patientSessionsFragment;
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
        holder.notes.setText(prescription.getNotes());
        holder.date.setText("Data de prescrição: " + DatesHandler.dateToStringWithHour(prescription.getDate()));
        holder.removePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove prescription from patient's prescriptions
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                    alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                alertDialog.setMessage("Deseja eliminar esta Prescrição?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar.make(holder.view, "Prescrião eliminada.", Snackbar.LENGTH_SHORT).show();
                                FirebaseDatabaseHelper.deletePrescription(prescription);

                                // refresh the adapter
                                fragment.removePrescription(prescription);
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

        /**
         * Setup warning button
         */
        holder.warning.setBackgroundResource(R.drawable.ic_warning_24dp);

        ArrayList<String> stoppCriteriaDrugs = StoppCriteria.getAllDrugsStopp(StoppCriteria.getStoppCriteria());
        ArrayList<String> beersCriteriaDrugs = BeersCriteria.getBeersDrugsAllString();

        // display warning
        if (stoppCriteriaDrugs.contains(prescription.getName())
                ||
                beersCriteriaDrugs.contains(prescription.getName())) {
            holder.warning.setVisibility(View.VISIBLE);
        } else {
            holder.warning.setVisibility(View.GONE);
        }

        holder.warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.prescription_warning, null);
                dialogBuilder.setView(dialogView);

                // display prescription warning
                TextView warningText = (TextView) dialogView.findViewById(R.id.warningText);
                warningText.setText("Este composto ativo deve ser evitado");

                AlertDialog alertDialog = dialogBuilder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create();
                alertDialog.setTitle("Aviso");
                alertDialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return prescriptions.size();
    }
}
