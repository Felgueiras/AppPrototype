package com.example.rafael.appprototype.Patients.SinglePatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Patients.Progress.ProgressMainViewPager;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions.PatientNotesFragment;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions.PatientSessionsFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSinglePatientInfo extends Fragment {

    public static final String PATIENT = "PATIENT";
    /**
     * Patient to be displayed
     */
    private Patient patient;

    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_main, container, false);
        System.out.println("VIEW SINGLE PATIENT INFO");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String actionTitle = bundle.getString("ACTION");
            String transText = bundle.getString("TRANS_TEXT");
            //view.findViewById(R.id.label).setTransitionName(transText);
            //system.out.println("lol 2");
        }



        PatientSectionsPagerAdapter mSectionsPagerAdapter = new PatientSectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ////system.out.println(position);
                Constants.vpPatientsPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // get PATIENT
        patient = (Patient) bundle.getSerializable(PATIENT);
//        ((PrivateAreaActivity)getActivity()).changeTitle(PATIENT.getName());

        getActivity().setTitle(patient.getName());

        // access Views
        //TextView label = (TextView) view.findViewById(R.id.label);
        TextView patientBirthDate = (TextView) view.findViewById(R.id.patientAge);
        TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        ImageView patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);
        Button patientProgress = (Button) view.findViewById(R.id.patientEvolution);
//        Button erasePatient = (Button) view.findViewById(R.id.erasePatient);
        TextView processNumber = (TextView) view.findViewById(R.id.processNumber);

        // set Patient infos
        //label.setText(PATIENT.getName());
        patientBirthDate.setText(DatesHandler.dateToStringWithoutHour(patient.getBirthDate()) + " - " + patient.getAge() + " anos");
        patientAddress.setText("Morada: " + patient.getAddress());
        processNumber.setText("Processo nº " + patient.getProcessNumber());
        //patientPhoto.setImageResource(PATIENT.getPicture());
        switch (patient.getGender()) {
            case Constants.MALE:
                patientPhoto.setImageResource(R.drawable.male);
                break;
            case Constants.FEMALE:
                patientPhoto.setImageResource(R.drawable.female);
                break;
        }




        // view PATIENT evolution
        patientProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("EVOLUTION");
                Fragment fragment = new ProgressMainViewPager();
                Bundle args = new Bundle();
                args.putSerializable(ProgressMainViewPager.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(), new ProgressMainViewPager(), args, Constants.tag_patient_progress);
            }
        });

        /**
         * Erase current PATIENT.
         */
//        erasePatient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View view) {
//                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setTitle(getResources().getString(R.string.patient_erase));
//                alertDialog.setMessage(getResources().getString(R.string.patient_erase_question));
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // remove sessions from PATIENT
//                                ArrayList<Session> sessionsFromPatient = PATIENT.getSessionsFromPatient();
//                                for (Session session : sessionsFromPatient) {
//                                    session.delete();
//                                }
//                                PATIENT.delete();
//                                dialog.dismiss();
//
//                                DrawerLayout layout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
//                                Snackbar.make(layout, getResources().getString(R.string.patient_erase_snackbar), Snackbar.LENGTH_SHORT).show();
//
//                                BackStackHandler.clearBackStack();
//                                FragmentManager fragmentManager = getFragmentManager();
//                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                                fragmentManager.beginTransaction()
//                                        .remove(currentFragment)
//                                        .replace(R.id.current_fragment, new PatientsMain())
//                                        .commit();
//                            }
//                        });
//                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//            }
//        });

        return view;
    }

    public class PatientSectionsPagerAdapter extends FragmentPagerAdapter {

        public PatientSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // get list of Records from this PATIENT
                ArrayList<Session> sessionsFromPatient = patient.getSessionsFromPatient();
                Fragment fragment;
                if (sessionsFromPatient.isEmpty()) {
                    fragment = new PatientSessionsEmpty();
                    Bundle args = new Bundle();
                    args.putSerializable(PatientSessionsEmpty.PATIENT, patient);
                    args.putString(PatientSessionsEmpty.MESSAGE, getResources().getString(R.string.no_sessions_for_patient));
                    fragment.setArguments(args);
                } else {
                    fragment = new PatientSessionsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable(PatientSessionsFragment.PATIENT, patient);
                    fragment.setArguments(args);
                }
                return fragment;
            } else if (position == 1) {
                Fragment fragment = new PatientNotesFragment();
                Bundle args = new Bundle();
                args.putSerializable(PatientNotesFragment.PATIENT, patient);
                fragment.setArguments(args);
                return fragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.evaluations);
                case 1:
                    return getResources().getString(R.string.notes);
            }
            return null;
        }
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
                patient.save();

                if (patient.isFavorite()) {
                    Snackbar.make(getView(), R.string.patient_favorite_add, Snackbar.LENGTH_LONG).show();
                    item.setIcon(R.drawable.ic_star_white_24dp);

                } else {
                    Snackbar.make(getView(), R.string.patient_favorite_remove, Snackbar.LENGTH_LONG).show();
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
                                ArrayList<Session> sessionsFromPatient = patient.getSessionsFromPatient();
                                for (Session session : sessionsFromPatient) {
                                    session.delete();
                                }
                                patient.delete();
                                dialog.dismiss();

                                DrawerLayout layout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                Snackbar.make(layout, getResources().getString(R.string.patient_erase_snackbar), Snackbar.LENGTH_SHORT).show();

                                BackStackHandler.clearBackStack();
                                FragmentManager fragmentManager = getFragmentManager();
                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                fragmentManager.beginTransaction()
                                        .remove(currentFragment)
                                        .replace(R.id.current_fragment, new PatientsMain())
                                        .commit();
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

        }
        return true;

    }


}

