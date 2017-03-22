package com.example.rafael.appprototype.Patients.ViewPatients;

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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.HelpersHandlers.StringHelper;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;

import java.util.ArrayList;

public class PatientCard extends RecyclerView.Adapter<PatientCard.MyViewHolder> implements Filterable, SectionTitleProvider {

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

    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        Log.d("Fast", "Called");
        return "P";
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout card;
        public TextView name, age;
        public ImageView icon, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
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
    public PatientCard(Activity context, ArrayList<Patient> patients) {
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
        //Glide.with(context).load(patient.getPicture()).into(holder.icon);

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
                 * Pick a patient to be associated with a Session.
                 */


                // TODO add shared elements for transitions
                Fragment endFragment = new ViewSinglePatientInfo();


                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                ((PrivateArea) context).replaceFragmentSharedElements(endFragment, args,
                        Constants.tag_view_patient_info_records,
                        holder.name);
            }
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.icon.setOnClickListener(clickListener);


//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow, patient);
//            }
//        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Patient patient) {
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
        private final Patient patient;

        public MyMenuItemClickListener(View view, Patient position) {
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
//                                        ((PatientSessionsFragment) fragment).removePatientFromFavorites(patient);
//                                    else if (fragment instanceof EvaluationsAll)
//                                        ((EvaluationsAll) fragment).removePatientFromFavorites(patient);
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
//                    args.putSerializable(ViewSinglePatientInfo.PATIENT, session.getPatient());
//                    ((PrivateArea) context).replaceFragmentSharedElements(endFragment,
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
        private final PatientCard adapter;
        private final ArrayList<Patient> originalList;
        private final ArrayList<Patient> filteredList;

        public PatientsFilter(PatientCard adapter, ArrayList<Patient> patients) {
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
            adapter.filteredList.addAll((ArrayList<Patient>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
