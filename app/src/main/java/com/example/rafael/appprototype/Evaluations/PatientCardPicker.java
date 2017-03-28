package com.example.rafael.appprototype.Evaluations;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivateBottomButtons;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;

import java.util.ArrayList;

public class PatientCardPicker extends RecyclerView.Adapter<PatientCardPicker.MyViewHolder> implements Filterable {

    private final ArrayList<Patient> filteredList;
    private final boolean pickBeforeSession;
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
        private RelativeLayout card;
        public TextView name;
        public ImageView icon, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            icon = (ImageView) view.findViewById(R.id.patientIcon);
            card = (RelativeLayout) view.findViewById(R.id.patientCard);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param patients
     * @param pickBeforeSession
     */
    public PatientCardPicker(Activity context, ArrayList<Patient> patients, boolean pickBeforeSession) {
        this.context = context;
        this.patients = patients;
        this.filteredList = new ArrayList<>();
        filteredList.addAll(patients);
        this.pickBeforeSession = pickBeforeSession;
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
        // holder.type.setText(PATIENT.getAge());


        // loading album cover using Glide library
        //Glide.with(context).load(PATIENT.getPicture()).into(holder.icon);

        // add on click listener for the icon

        View.OnClickListener clickListener = new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {
                // prompt if really want to save it
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                alertDialog.setMessage("Deseja mesmo associar o paciente " + patient.getName() + " à sessão?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (pickBeforeSession) {
                                    /**
                                     * Go to PATIENT's profile
                                     */
                                    context.getFragmentManager().popBackStack();
                                    Bundle args = new Bundle();
//                                    args.putSerializable(ViewSinglePatientInfo.PATIENT, PATIENT);
//                                    FragmentTransitions.replaceFragment(context, new ViewSinglePatientInfo(),
//                                            args,
//                                            Constants.tag_view_patient_info_records);
                                    /**
                                     * Go to new session with this PATIENT.
                                     */
                                    args = new Bundle();
                                    args.putSerializable(CGAPrivateBottomButtons.PATIENT, patient);
                                    FragmentTransitions.replaceFragment(context, new CGAPrivateBottomButtons(),
                                            args,
                                            Constants.tag_create_session_with_patient_from_session);
                                } else {
                                    DrawerLayout layout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
                                    Snackbar.make(layout, context.getString(R.string.picked_patient_session_created), Snackbar.LENGTH_SHORT).show();
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
                                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                    fragmentManager.beginTransaction()
                                            .remove(currentFragment)
                                            .commit();
//                    fragmentManager.popBackStack();
                                    BackStackHandler.clearBackStack();
//                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                    fragmentManager.beginTransaction()
//                            .remove(currentFragment)
//                            .replace(R.id.current_fragment, new PatientsMain())
//                            .commit();
                                    /**
                                     * Review session created for PATIENT.
                                     */
                                    Bundle args = new Bundle();
                                    args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
                                    args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                                    Fragment fragment = new ReviewSingleSessionWithPatient();

                                    // TODO go to new session with this PATIENT
                                    fragment.setArguments(args);
                                    currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                    fragmentManager.beginTransaction()
                                            .remove(currentFragment)
                                            .replace(R.id.current_fragment, fragment)
                                            .addToBackStack(Constants.tag_review_session_from_sessions_list)
                                            .commit();
                                }
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        };

        holder.card.setOnClickListener(clickListener);
        holder.name.setOnClickListener(clickListener);
        holder.icon.setOnClickListener(clickListener);


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
                    if (patient.getName().toLowerCase().trim().contains(filterPattern)) {
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
