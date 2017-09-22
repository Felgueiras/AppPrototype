package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.Prescription.PrescriptionSingleDrugFragment;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

public class PatientPrescriptionsAdapter extends RecyclerView.Adapter<PatientPrescriptionsAdapter.PrescriptionCardViewHolder> {

    private final PatientPrescriptionsTimelineFragment fragment;
    private final boolean compactView;
    private Activity context;
    /**
     * Records from that PATIENT
     */
    private ArrayList<PrescriptionFirebase> prescriptions;

    /**
     * Create a View
     */
    public class PrescriptionCardViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView name;
        private final EditText notes;
        private final ImageView toggleInfo;
        private final ImageButton addNotes;
        public TextView date;
        public ImageView removePrescription;
        public Button warning;

        public PrescriptionCardViewHolder(View view) {
            super(view);
            this.view = view;
            notes = view.findViewById(R.id.prescriptionNotes);
            name = view.findViewById(R.id.prescriptionName);
            date = view.findViewById(R.id.prescriptionDate);
            removePrescription = view.findViewById(R.id.removePrescription);
            toggleInfo = view.findViewById(R.id.toggleInfo);
            addNotes = view.findViewById(R.id.addNotes);
            warning = view.findViewById(R.id.warning);

        }
    }


    public PatientPrescriptionsAdapter(Activity context,
                                       ArrayList<PrescriptionFirebase> prescriptions,
                                       PatientFirebase patient,
                                       PatientPrescriptionsTimelineFragment patientSessionsFragment,
                                       boolean compactView) {
        this.context = context;
        this.prescriptions = prescriptions;
        this.fragment = patientSessionsFragment;
        this.compactView = compactView;
    }


    @Override
    public PrescriptionCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_patient_prescription, parent, false);

        return new PrescriptionCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PrescriptionCardViewHolder holder, int position) {
        final PrescriptionFirebase prescription = prescriptions.get(position);


        // set views
        holder.name.setText(prescription.getName());
        holder.notes.setText(prescription.getNotes());

        // hide "detail" views
        holder.notes.setVisibility(View.GONE);
        holder.addNotes.setVisibility(View.GONE);
        holder.removePrescription.setVisibility(View.GONE);
        holder.warning.setVisibility(View.GONE);

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

        // toggle displaying prescription info
        holder.toggleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (holder.notes.getVisibility() == View.VISIBLE) {
                    // slide up
                    holder.notes.setAlpha(1.0f);
                    holder.addNotes.animate().translationY(-holder.addNotes.getHeight()).alpha(0.0f);
                    holder.warning.animate().translationY(-holder.warning.getHeight()).alpha(0.0f);
                    holder.removePrescription.animate().translationY(-holder.removePrescription.getHeight()).alpha(0.0f);

                    // Start the animation
                    holder.notes.animate()
                            .translationY(-holder.notes.getHeight())
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    // change icon
                                    ((ImageView) v).setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                    holder.notes.setVisibility(View.GONE);
                                    holder.addNotes.setVisibility(View.GONE);
                                    holder.warning.setVisibility(View.GONE);
                                    holder.removePrescription.setVisibility(View.GONE);

                                }
                            });
                } else {
                    // slide down
                    holder.notes.setVisibility(View.VISIBLE);

//                    TranslateAnimation slideDown = new TranslateAnimation(
//                            Animation.ABSOLUTE,200, Animation.ABSOLUTE,200,
//                            Animation.ABSOLUTE,200, Animation.ABSOLUTE,200);

                    // Start the animation
                    holder.notes.animate()
                            .translationY(0)
                            .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);


                        }
                    });

                    holder.addNotes.animate().translationY(0).alpha(1.0f);
                    holder.warning.animate().translationY(0).alpha(1.0f);
                    holder.removePrescription.animate().translationY(0).alpha(1.0f);
                    holder.notes.setVisibility(View.VISIBLE);
                    holder.addNotes.setVisibility(View.VISIBLE);
                    holder.warning.setVisibility(View.VISIBLE);
                    holder.removePrescription.setVisibility(View.VISIBLE);
                    PrescriptionFirebase.checkWarning(prescription.getName(), holder.warning);

                    ((ImageView) v).setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }

            }
        });


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
                                Snackbar.make(holder.view, "Prescrição eliminada.", Snackbar.LENGTH_SHORT).show();
                                FirebaseDatabaseHelper.deletePrescription(prescription, context);

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

        /*
          Setup warning button
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
