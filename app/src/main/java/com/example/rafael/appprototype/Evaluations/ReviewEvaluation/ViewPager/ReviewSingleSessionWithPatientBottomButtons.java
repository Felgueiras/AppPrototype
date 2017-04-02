package com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ViewPager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Evaluations.EvaluationsAll;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewAreaCard;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ViewPager.ReviewAreaFragment;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Patients.Progress.ViewPager.ProgressAreaScalesFragment;
import com.example.rafael.appprototype.Patients.Progress.ViewPager.ProgressMainViewPager;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class ReviewSingleSessionWithPatientBottomButtons extends Fragment {

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

        transaction.replace(R.id.frame_layout_cga_area, ReviewAreaFragment.newInstance(Constants.cga_areas[defaultIndex], session));
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.cga_mental:
                                fragment = ReviewAreaFragment.newInstance(Constants.cga_mental, session);
                                break;
                            case R.id.cga_functional:
                                fragment = ReviewAreaFragment.newInstance(Constants.cga_functional, session);
                                break;
                            case R.id.cga_nutritional:
                                fragment = ReviewAreaFragment.newInstance(Constants.cga_nutritional, session);
                                break;
                            case R.id.cga_social:
                                fragment = ReviewAreaFragment.newInstance(Constants.cga_social, session);
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
                                    args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                                    fragment = new ViewSinglePatientInfo();
                                    fragment.setArguments(args);
                                }


                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                fragmentManager.beginTransaction()
                                        .remove(currentFragment)
                                        .replace(R.id.current_fragment, fragment)
                                        .commit();

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

