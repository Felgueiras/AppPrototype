package com.felgueiras.apps.geriatric_helper.Patients.AllPatients;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.StringHelper;
import com.felgueiras.apps.geriatric_helper.Main.PrivateAreaActivity;
import com.felgueiras.apps.geriatric_helper.Patients.SinglePatient.PatientProfileFragment;
import com.felgueiras.apps.geriatric_helper.R;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;

public class PatientCardPatientsList extends RecyclerView.Adapter<PatientCardPatientsList.MyViewHolder> implements Filterable, SectionTitleProvider {

    private final ArrayList<PatientFirebase> filteredList;
    private Activity context;
    /**
     * Data to be displayed.
     */
    private ArrayList<PatientFirebase> patients;
    private PatientsFilter patientsFilter;

    @Override
    public Filter getFilter() {
        if (patientsFilter == null)
            patientsFilter = new PatientsFilter(this, patients);
        return patientsFilter;
    }

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        // get the initial of the PATIENT
        return patients.get(position).getName().charAt(0) + "";
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout card;
        public TextView name, initial;
        public ImageView icon, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            initial = (TextView) view.findViewById(R.id.initialLetter);
            icon = (ImageView) view.findViewById(R.id.patientIcon);
            card = (RelativeLayout) view.findViewById(R.id.patientCard);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param patients
     */
    public PatientCardPatientsList(Activity context, ArrayList<PatientFirebase> patients) {
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
        final PatientFirebase patient = filteredList.get(position);

        holder.name.setText(patient.getName());

        // check if is first PATIENT with this initial
        char patientInitial = patient.getName().charAt(0);
        boolean showInitial = false;
        if (position == 0) {
            showInitial = true;
        } else {
            char previousPatientInitial = filteredList.get(position - 1).getName().charAt(0);
            if (patientInitial != previousPatientInitial)
                showInitial = true;
        }
        if (showInitial) {
            holder.initial.setVisibility(View.VISIBLE);
            holder.initial.setText(patientInitial + "");
        } else {
            holder.initial.setVisibility(View.INVISIBLE);
        }


        // loading album cover using Glide library
        //Glide.with(context).load(PATIENT.getPicture()).into(holder.icon);

        // add on click listener for the icon

        switch (patient.getGender()) {
            case Constants.MALE:
                holder.icon.setImageResource(R.drawable.male);
                break;
            case Constants.FEMALE:
                holder.icon.setImageResource(R.drawable.female);
                break;
        }


        View.OnClickListener clickListener = new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {
                /**
                 * Pick a PATIENT to be associated with a Session.
                 */


                Fragment endFragment = new PatientProfileFragment();


                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(PatientProfileFragment.PATIENT, patient);
                ((PrivateAreaActivity) context).replaceFragmentSharedElements(endFragment, args,
                        Constants.tag_view_patient_info_records,
                        holder.name);
            }
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.icon.setOnClickListener(clickListener);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LongClick", "Open");
                return false;
            }
        });


//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow, PATIENT);
//            }
//        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, PatientFirebase patient) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        /**
         * Inflate menu depending on the fragment.
         */

        inflater.inflate(R.menu.menu_patient_card, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(view, patient));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final View view;
        private final PatientFirebase patient;

        public MyMenuItemClickListener(View view, PatientFirebase position) {
            this.view = view;
            this.patient = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.evaluate_patient:
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//                    alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                    alertDialog.setMessage("Deseja eliminar esta Sessão?");
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Snackbar.make(view, "Sessão eliminada.", Snackbar.LENGTH_SHORT).show();
//                                    session.delete();
//                                    // refresh the adapter
//                                    if (fragment instanceof PatientSessionsFragment)
//                                        ((PatientSessionsFragment) fragment).removePatientFromFavorites(PATIENT);
//                                    else if (fragment instanceof EvaluationsAllFragment)
//                                        ((EvaluationsAllFragment) fragment).removePatientFromFavorites(PATIENT);
//                                }
//                            });
//                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Snackbar.make(view, "Ação cancelada", Snackbar.LENGTH_SHORT).show();
//
//                                }
//                            });
                    alertDialog.show();
                    return true;
//                case R.id.session_go_patient_profile:
//                    Fragment endFragment = new ViewSinglePatientInfo();
//
//                    Bundle args = new Bundle();
//                    args.putSerializable(ViewSinglePatientInfo.PATIENT, session.getDrugName());
//                    ((PrivateAreaActivity) context).replaceFragmentSharedElements(endFragment,
//                            args,
//                            Constants.tag_view_patient_info_records_from_sessions_list,
//                            null);
//                    break;
                default:
            }
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Class that allows for the Patients to be filtered by area.
     */
    private class PatientsFilter extends Filter {
        private final PatientCardPatientsList adapter;
        private final ArrayList<PatientFirebase> originalList;
        private final ArrayList<PatientFirebase> filteredList;

        public PatientsFilter(PatientCardPatientsList adapter, ArrayList<PatientFirebase> patients) {
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

                for (final PatientFirebase patient : originalList) {
                    String patientNameNoAccents = StringHelper.removeAccents(patient.getName());
                    if (patientNameNoAccents.toLowerCase().trim().contains(StringHelper.removeAccents(filterPattern))) {
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
            adapter.filteredList.addAll((ArrayList<PatientFirebase>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
