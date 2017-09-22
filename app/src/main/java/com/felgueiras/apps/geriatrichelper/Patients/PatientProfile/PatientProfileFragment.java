package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientNotes.PatientNotesFragment;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsEmpty;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsTimelineFragment;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientSessions.PatientSessionsEmpty;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientSessions.PatientSessionsTimelineFragment;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.PatientTimelineFragmentOriginal;
import com.felgueiras.apps.geriatrichelper.Patients.Progress.ProgressFragment;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class PatientProfileFragment extends Fragment {

    public static final String PATIENT = "PATIENT";
    /**
     * Patient to be displayed
     */
    private PatientFirebase patient;

    private Menu menu;

    Fragment defaultFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Profile","OnCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Profile","OnResume");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Profile","OnStop");

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.patient_profile, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String actionTitle = bundle.getString("ACTION");
            String transText = bundle.getString("TRANS_TEXT");
            //view.findViewById(R.id.label).setTransitionName(transText);
            //system.out.println("lol 2");
        }


        // get PATIENT
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);

        getActivity().setTitle(patient.getName());

        // access Views
        final View patientInfo = view.findViewById(R.id.patientInfo);
        final View separator = view.findViewById(R.id.separator);
        TextView patientBirthDate = view.findViewById(R.id.patientAge);
        TextView patientAddress = view.findViewById(R.id.patientAddress);
        TextView processNumber = view.findViewById(R.id.processNumber);
        ImageButton hidePatientInfo = view.findViewById(R.id.hidePatientInfo);

        // set Patient infos
        patientBirthDate.setText(DatesHandler.dateToStringWithoutHour(patient.getBirthDate()) + " - " +
                patient.getAge() + " anos");
        patientAddress.setText("Morada: " + patient.getAddress());
        processNumber.setText("Processo nº " + patient.getProcessNumber());
        hidePatientInfo.bringToFront();
        if(!Constants.patientInfoShow)
        {
            hidePatientInfo.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            patientInfo.setVisibility(View.GONE);
            separator.animate().translationY(0);
            hidePatientInfo.animate().translationY(0);
        }


        hidePatientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (patientInfo.getVisibility() == View.VISIBLE) {
                    // slide up
                    patientInfo.setAlpha(1.0f);
                    v.animate().translationY(-patientInfo.getHeight());
                    separator.animate().translationY(-patientInfo.getHeight()).alpha(1.0f);
                    Constants.patientInfoShow = false;

                    // Start the animation
                    patientInfo.animate()
                            .translationY(-patientInfo.getHeight())
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    // change icon
                                    ((ImageButton) v).setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                    patientInfo.setVisibility(View.GONE);
                                    separator.animate().translationY(0);
                                    v.animate().translationY(0);

                                }
                            });
                } else {
                    // slide down
                    patientInfo.setVisibility(View.VISIBLE);
                    Constants.patientInfoShow = true;


                    // Start the animation
                    patientInfo.animate()
                            .translationY(0)
                            .alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            patientInfo.setVisibility(View.VISIBLE);
                            ((ImageButton) v).setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                        }
                    });
                }

            }
        });


        //patientPhoto.setImageResource(PATIENT.getPicture());
//        switch (patient.getGender()) {
//            case Constants.MALE:
//                patientPhoto.setImageResource(R.drawable.male);
//                break;
//            case Constants.FEMALE:
//                patientPhoto.setImageResource(R.drawable.female);
//                break;
//        }


        /*
          Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        /*
          Default fragment.
         */
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_bottom_navigation);
        if (currentFragment != null)
            transaction.remove(currentFragment);

        // put fragments in array
        final ArrayList<Fragment> fragments = new ArrayList<>();

        final ArrayList<SessionFirebase> sessionsFromPatient = FirebaseDatabaseHelper.getSessionsFromPatient(patient);


        for (int i = 0; i < 4; i++) {
            Fragment frag = null;
            Bundle args = null;
            switch (i) {
                case 0:
                    if (sessionsFromPatient.isEmpty()) {
                        frag = new PatientSessionsEmpty();
                        args = new Bundle();
                        args.putSerializable(PatientSessionsEmpty.PATIENT, patient);
                        args.putString(PatientSessionsEmpty.MESSAGE, getResources().getString(R.string.no_sessions_for_patient));
                        frag.setArguments(args);
                    } else {
                        frag = new PatientSessionsTimelineFragment();
                        args = new Bundle();
                        args.putSerializable(PatientSessionsTimelineFragment.PATIENT, patient);
                        frag.setArguments(args);
                    }
                    break;
                case 2:
                    frag = new PatientNotesFragment();
                    args = new Bundle();
                    args.putSerializable(PatientNotesFragment.PATIENT, patient);
                    frag.setArguments(args);
                    break;
                case 1:
                    if (patient.getPrescriptionsIDS().size() == 0 || patient.getPrescriptionsIDS() == null) {
                        frag = new PatientPrescriptionsEmpty();
                        args = new Bundle();
                        args.putSerializable(PatientPrescriptionsEmpty.PATIENT, patient);
                        args.putString(PatientPrescriptionsEmpty.MESSAGE, getResources().
                                getString(R.string.no_prescriptions_for_patient));
                        frag.setArguments(args);
                    } else {
                        frag = new PatientPrescriptionsTimelineFragment();
                        args = new Bundle();
                        args.putSerializable(PatientPrescriptionsTimelineFragment.PATIENT, patient);
                        frag.setArguments(args);
                    }
                    break;
                case 3:
                    frag = new PatientTimelineFragmentOriginal();
                    args = new Bundle();
                    args.putSerializable(PatientTimelineFragmentOriginal.PATIENT, patient);
                    frag.setArguments(args);

                    break;

            }
            fragments.add(frag);
        }


        defaultFragment = fragments.get(Constants.patientProfileBottomNavigation);


        transaction.replace(R.id.frame_layout_bottom_navigation, defaultFragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // same item pressed
                        Fragment fragment = null;


                        switch (item.getItemId()) {
                            case R.id.patient_sessions:
                                if (Constants.patientProfileBottomNavigation == 0)
                                    return true;
                                Constants.patientProfileBottomNavigation = 0;
                                break;
                            case R.id.patient_prescriptions:
                                if (Constants.patientProfileBottomNavigation == 1)
                                    return true;
                                Constants.patientProfileBottomNavigation = 1;
                                break;
                            case R.id.patient_notes:
                                if (Constants.patientProfileBottomNavigation == 2)
                                    return true;
                                Constants.patientProfileBottomNavigation = 2;
                                break;
                            case R.id.patientTimeline:
                                if (Constants.patientProfileBottomNavigation == 3)
                                    return true;
                                Constants.patientProfileBottomNavigation = 3;
                                break;


                        }
                        fragment = fragments.get(Constants.patientProfileBottomNavigation);


                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_bottom_navigation);
                        if (currentFragment != null)
                            transaction.remove(currentFragment);
                        transaction.replace(R.id.frame_layout_bottom_navigation, fragment);
                        transaction.commit();

                        return true;
                    }
                });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_patient_profile, menu);
        this.menu = menu;
        checkFavorite();
    }

    private void checkFavorite() {
        MenuItem favoriteItem = menu.findItem(R.id.favorite);
        if (patient.isFavorite())
            favoriteItem.setIcon(R.drawable.ic_star_white_24dp);
        else
            favoriteItem.setIcon(R.drawable.ic_star_border_black_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                patient.setFavorite(!patient.isFavorite());
                PatientsManagement.getInstance().updatePatient(patient, getActivity());

                if (patient.isFavorite()) {
                    Snackbar.make(view, R.string.patient_favorite_add, Snackbar.LENGTH_LONG).show();
                    item.setIcon(R.drawable.ic_star_white_24dp);

                } else {
                    Snackbar.make(view.findViewById(R.id.bottom_navigation), R.string.patient_favorite_remove, Snackbar.LENGTH_LONG).show();
                    item.setIcon(R.drawable.ic_star_border_black_24dp);
                }
                break;
            case R.id.delete:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.patient_erase));
                alertDialog.setMessage(getResources().getString(R.string.patient_erase_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // remove sessions from PATIENT
                                PatientsManagement.getInstance().deletePatient(patient, getActivity());
                                dialog.dismiss();

                                DrawerLayout layout = getActivity().findViewById(R.id.drawer_layout);
                                Snackbar.make(layout, getResources().getString(R.string.patient_erase_snackbar), Snackbar.LENGTH_SHORT).show();

                                BackStackHandler.getFragmentManager().popBackStack();
//                                FragmentManager fragmentManager = BackStackHandler.getFragmentManager();
//                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                                fragmentManager.beginTransaction()
//                                        .remove(currentFragment)
//                                        .replace(R.id.current_fragment, new PatientsMain())
//                                        .commit();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case R.id.progress:
                // reset the page
                Bundle args = new Bundle();
                args.putSerializable(ProgressFragment.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(), new ProgressFragment(), args, Constants.tag_patient_progress);

                break;

        }
        return true;
    }


}

