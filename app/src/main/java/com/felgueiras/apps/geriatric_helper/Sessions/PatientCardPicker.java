package com.felgueiras.apps.geriatric_helper.Sessions;

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

import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;

import java.util.ArrayList;

public class PatientCardPicker extends RecyclerView.Adapter<PatientCardPicker.MyViewHolder> implements Filterable {

    private final ArrayList<PatientFirebase> filteredList;
    private final boolean pickBeforeSession;
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
     *  @param context
     * @param patients
     * @param pickBeforeSession
     */
    public PatientCardPicker(Activity context, ArrayList<PatientFirebase> patients, boolean pickBeforeSession) {
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
        final PatientFirebase patient = filteredList.get(position);

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
                                    context.getFragmentManager().popBackStack();
                                    Bundle args = new Bundle();
                                    /**
                                     * Go to new session with this PATIENT.
                                     */
                                    args = new Bundle();
                                    args.putSerializable(CGAPrivate.PATIENT, patient);
                                    FragmentTransitions.replaceFragment(context, new CGAPrivate(),
                                            args,
                                            Constants.tag_create_session_with_patient_from_session);
//                                            Constants.tag_create_session_with_patient);
                                } else {
                                    DrawerLayout layout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
                                    Snackbar.make(layout, context.getString(R.string.picked_patient_session_created), Snackbar.LENGTH_SHORT).show();
                                    // add Patient to Session
                                    String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(context);

                                    // get session by ID
                                    SessionFirebase session = FirebaseHelper.getSessionByID(sessionID);
                                    session.setPatientID(patient.getGuid());
                                    FirebaseHelper.eraseScalesNotCompleted(session);

                                    FirebaseHelper.updateSession(session);

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
        private final ArrayList<PatientFirebase> originalList;
        private final ArrayList<PatientFirebase> filteredList;

        public PatientsFilter(PatientCardPicker adapter, ArrayList<PatientFirebase> patients) {
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
            adapter.filteredList.addAll((ArrayList<PatientFirebase>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
