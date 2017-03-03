package com.example.rafael.appprototype.Evaluations;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.SharedPreferencesHelper;

import java.util.ArrayList;

public class PatientCardPicker extends RecyclerView.Adapter<PatientCardPicker.MyViewHolder> implements Filterable {

    private final ArrayList<Patient> filteredList;
    private Activity context;
    /**
     * Data to be displayed.
     */
    private ArrayList<Patient> patients;
    private PatientsFilter patientsFilter;

    @Override
    public Filter getFilter() {
        if (patientsFilter == null)
            patientsFilter = new PatientsFilter(this, patients);
        return patientsFilter;
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age;
        public ImageView photo, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param patients
     */
    public PatientCardPicker(Activity context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
        this.filteredList = new ArrayList<>();
        filteredList.addAll(patients);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Patient patient = filteredList.get(position);

        holder.name.setText(patient.getName());
        // holder.type.setText(patient.getAge());


        // loading album cover using Glide library
        //Glide.with(context).load(patient.getPicture()).into(holder.photo);

        // add on click listener for the photo


        holder.name.setOnClickListener(new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {
                /**
                 * Pick a patient to be associated with a Session.
                 */

                Constants.selectPatient = false;
                Snackbar.make(v, context.getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                Log.d("Patient", "Selected patient");
                // add Patient to Session
                String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(context);

                // get session by ID
                Session session = Session.getSessionByID(sessionID);
                session.setPatient(patient);
                session.eraseScalesNotCompleted();
                session.save();

                // reset current private session
                SharedPreferencesHelper.resetPrivateSession(context, "");

                FragmentManager fragmentManager = context.getFragmentManager();
//                    fragmentManager.popBackStack();
                BackStackHandler.clearBackStack();
//                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                    fragmentManager.beginTransaction()
//                            .remove(currentFragment)
//                            .replace(R.id.current_fragment, new PatientsMain())
//                            .commit();
                /**
                 * Review session created for patient.
                 */
                Bundle args = new Bundle();
                args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                Fragment fragment = new ReviewSingleSessionWithPatient();

                fragment.setArguments(args);
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                fragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .replace(R.id.current_fragment, fragment)
                        .addToBackStack(Constants.tag_review_session)
                        .commit();
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
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(context, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Class that allows for the Patients to be filtered by area.
     */
    private class PatientsFilter extends Filter {
        private final PatientCardPicker adapter;
        private final ArrayList<Patient> originalList;
        private final ArrayList<Patient> filteredList;

        public PatientsFilter(PatientCardPicker adapter, ArrayList<Patient> patients) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>();
            originalList.addAll(patients);
            this.filteredList = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Patient patient : originalList) {
                    if (patient.getName().toString().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredList.clear();
            adapter.filteredList.addAll((ArrayList<Patient>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
