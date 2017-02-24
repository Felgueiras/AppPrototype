package com.example.rafael.appprototype.Patients.SinglePatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.example.rafael.appprototype.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DatesHandler;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Patients.PatientProgress.ProgressMainFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions.PatientNotesFragment;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewPatientSessions.PatientSessionsFragment;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSinglePatientInfo extends Fragment {

    public static final String PATIENT = "patient";
    /**
     * Patient to be displayed
     */
    private Patient patient;

    private PatientSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String actionTitle;
    private String transText;
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
            actionTitle = bundle.getString("ACTION");
            transText = bundle.getString("TRANS_TEXT");
            //view.findViewById(R.id.patientName).setTransitionName(transText);
            //system.out.println("lol 2");
        }

        mSectionsPagerAdapter = new PatientSectionsPagerAdapter(getChildFragmentManager());


        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) view.findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(Constants.vpPatientsPage);

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

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // get patient
        patient = (Patient) bundle.getSerializable(PATIENT);
        getActivity().setTitle(patient.getName());

        // access Views
        //TextView patientName = (TextView) view.findViewById(R.id.patientName);
        TextView patientBirthDate = (TextView) view.findViewById(R.id.patientAge);
        TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        ImageView patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);
        Button patientProgress = (Button) view.findViewById(R.id.patientEvolution);
        Button erasePatient = (Button) view.findViewById(R.id.erasePatient);

        // set Patient infos
        //patientName.setText(patient.getName());
        patientBirthDate.setText(DatesHandler.dateToStringWithoutHour(patient.getBirthDate()) + "");
        patientAddress.setText(patient.getAddress());
        //patientPhoto.setImageResource(patient.getPicture());


        /**
         * Setup FABS
         */
        AddFloatingActionButton fabAddSession = (AddFloatingActionButton) view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(CGAPrivate.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_new_session_for_patient);
                getActivity().setTitle(getResources().getString(R.string.cga));
            }
        });

        // view patient evolution
        patientProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("EVOLUTION");
                Fragment fragment = new ProgressMainFragment();
                Bundle args = new Bundle();
                args.putSerializable(ProgressMainFragment.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(), new ProgressMainFragment(), args, Constants.tag_patient_progress);
            }
        });

        /**
         * Erase current patient.
         */
        erasePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.patient_erase));
                alertDialog.setMessage(getResources().getString(R.string.patient_erase_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // remove sessions from patient
                                ArrayList<Session> sessionsFromPatient = patient.getSessionsFromPatient();
                                for (Session session : sessionsFromPatient) {
                                    session.delete();
                                }
                                patient.delete();
                                dialog.dismiss();

                                BackStackHandler.clearBackStack();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.current_fragment, new PatientsMain())
                                        .commit();
                                Snackbar.make(getView(), getResources().getString(R.string.patient_erase_snackbar), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        return view;
    }

    public class PatientSectionsPagerAdapter extends FragmentPagerAdapter {

        public PatientSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // get list of Records from this patient
                ArrayList<Session> sessionsFromPatient = patient.getSessionsFromPatient();
                Fragment fragment;
                if (sessionsFromPatient.isEmpty()) {
                    fragment = new EmptyStateFragment();
                    Bundle args = new Bundle();
                    args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_for_patient));
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
            favoriteItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
        else
            favoriteItem.setIcon(R.drawable.ic_favorite_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                patient.setFavorite(!patient.isFavorite());
                patient.save();

                if (patient.isFavorite()) {
                    Snackbar.make(getView(), R.string.patient_favorite_add, Snackbar.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);

                } else {
                    Snackbar.make(getView(), R.string.patient_favorite_remove, Snackbar.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                }

        }
        return true;

    }


}

