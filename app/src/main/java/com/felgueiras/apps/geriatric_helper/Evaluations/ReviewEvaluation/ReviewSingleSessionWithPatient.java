package com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Evaluations.EvaluationsHistoryMain;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Patients.SinglePatient.PatientProfileFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


public class ReviewSingleSessionWithPatient extends Fragment {

    public static String COMPARE_PREVIOUS;
    /**
     * Session object
     */
    private Session session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";
    private TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        SharedPreferencesHelper.unlockSessionCreation(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_navigation_review_areas, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (Session) args.getSerializable(SESSION);

        if (session.getPatient() != null) {
            getActivity().setTitle(session.getPatient().getName() + " - " + DatesHandler.dateToStringWithoutHour(session.getDate()));
        } else {
            getActivity().setTitle(DatesHandler.dateToStringWithoutHour(session.getDate()));
        }


        /**
         * Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);

        // disable areas that don't have any scale
        Menu menuNav = bottomNavigationView.getMenu();

        int defaultIndex = -1;

        for (int i = 0; i < Constants.cga_areas.length; i++) {
            String currentArea = Constants.cga_areas[i];
            if (Scales.getTestsForArea(session.getScalesFromSession(), currentArea).size() == 0) {
                // disable
                menuNav.getItem(i).setEnabled(false);
            } else {
                if (defaultIndex == -1) {
                    defaultIndex = i;
                    menuNav.getItem(i).setChecked(true);

                }
            }
        }
        /**
         * Default fragment.
         */
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_cga_area);
        if (currentFragment != null)
            transaction.remove(currentFragment);

        bottomNavigationView.getMenu().getItem(defaultIndex).setChecked(true);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_mental, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_functional, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_nutritional, session));
        fragments.add(ReviewAreaFragment.newInstance(Constants.cga_social, session));

        transaction.replace(R.id.frame_layout_cga_area, fragments.get(defaultIndex));
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;

                        switch (item.getItemId()) {
                            case R.id.cga_mental:
                                Constants.bottomNavigationReviewSession = 0;
                                fragment = fragments.get(0);
                                break;
                            case R.id.cga_functional:
                                Constants.bottomNavigationReviewSession = 1;
                                fragment = fragments.get(1);
                                break;
                            case R.id.cga_nutritional:
                                Constants.bottomNavigationReviewSession = 2;
                                fragment = fragments.get(2);
                                break;
                            case R.id.cga_social:
                                Constants.bottomNavigationReviewSession = 3;
                                fragment = fragments.get(3);
                                break;
                        }

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layout_cga_area);
                        if (currentFragment != null)
                            transaction.remove(currentFragment);
                        transaction.replace(R.id.frame_layout_cga_area, fragment);
                        transaction.commit();


                        return true;
                    }
                });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_patient_session, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.session_erase));
                alertDialog.setMessage(getResources().getString(R.string.session_erase_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // erase session
                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                int index = fragmentManager.getBackStackEntryCount() - 1;
                                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                                String tag = backEntry.getName();

                                fragmentManager.popBackStack();
                                Patient patient = session.getPatient();
                                dialog.dismiss();

                                DrawerLayout layout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                Snackbar.make(layout, getResources().getString(R.string.session_erase_snackbar), Snackbar.LENGTH_SHORT).show();

                                Fragment fragment = null;
                                if (tag.equals(Constants.tag_review_session_from_sessions_list)) {
                                    // go back to sessions list
                                    fragment = new EvaluationsHistoryMain();
                                } else if (tag.equals(Constants.tag_review_session_from_patient_profile)) {
                                    // go back to PATIENT profile
                                    Bundle args = new Bundle();
                                    args.putSerializable(PatientProfileFragment.PATIENT, patient);
                                    fragment = new PatientProfileFragment();
                                    fragment.setArguments(args);
                                }


//                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                                fragmentManager.beginTransaction()
//                                        .remove(currentFragment)
//                                        .replace(R.id.current_fragment, fragment)
//                                        .commit();

                                session.delete();
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

