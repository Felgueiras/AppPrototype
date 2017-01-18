package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.PatientEvolution.ViewPatientEvolutionFragment;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions.ViewPatientSessionsFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_main, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            actionTitle = bundle.getString("ACTION");
            transText = bundle.getString("TRANS_TEXT");
            view.findViewById(R.id.patientName).setTransitionName(transText);
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
        TextView patientName = (TextView) view.findViewById(R.id.patientName);
        TextView patientAge = (TextView) view.findViewById(R.id.patientAge);
        TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        ImageView patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);

        // set Patient infos
        patientName.setText(patient.getName());
        patientAge.setText(patient.getAge() + "");
        patientAddress.setText(patient.getAddress());
        patientPhoto.setImageResource(patient.getPicture());


        /**
         * Setup FABS
         */
        AddFloatingActionButton fabAddSession = (AddFloatingActionButton) view.findViewById(R.id.patient_createSession);
        fabAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putSerializable(NewEvaluation.PATIENT, patient);
                FragmentTransitions.replaceFragment(getActivity(),new NewEvaluation(), args, Constants.tag_create_new_session_for_patient);
                getActivity().setTitle(getResources().getString(R.string.tab_sessions));
            }
        });
        final FloatingActionButton fabFavorite = (FloatingActionButton) view.findViewById(R.id.patient_favorite);
        if (patient.isFavorite())
            fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_remove));
        else
            fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_add));
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set Patient as favorite
                patient.setFavorite(!patient.isFavorite());
                patient.save();
                if (patient.isFavorite()) {
                    Snackbar.make(getView(), R.string.patient_favorite_add, Snackbar.LENGTH_SHORT).show();
                    fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_remove));
                } else {
                    Snackbar.make(getView(), R.string.patient_favorite_remove, Snackbar.LENGTH_SHORT).show();
                    fabFavorite.setIconDrawable(getResources().getDrawable(R.drawable.ic_favorite_add));
                }
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
                ArrayList<Session> sessionsFromPatient = patient.getRecordsFromPatient();
                Fragment fragment;
                if (sessionsFromPatient.isEmpty()) {
                    fragment = new EmptyStateFragment();
                    Bundle args = new Bundle();
                    args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_for_patient));
                    fragment.setArguments(args);
                } else {
                    fragment = new ViewPatientSessionsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable(ViewPatientSessionsFragment.PATIENT, patient);
                    fragment.setArguments(args);
                }
                return fragment;
            } else if (position == 1) {
                Fragment fragment = new ViewPatientEvolutionFragment();
                Bundle args = new Bundle();
                args.putSerializable(ViewPatientEvolutionFragment.PATIENT, patient);
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
                    return "All sessions";
                case 1:
                    return "Evolution";
            }
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_questions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_back:
                getActivity().onBackPressed();
        }
        return true;

    }


}

