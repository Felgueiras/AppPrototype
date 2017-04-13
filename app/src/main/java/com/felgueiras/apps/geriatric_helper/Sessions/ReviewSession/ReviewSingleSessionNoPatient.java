package com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPublicInfo;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Patients.PatientsMain;
import com.felgueiras.apps.geriatric_helper.PersonalAreaAccess.RegisterUser;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReviewSingleSessionNoPatient extends Fragment {

    /**
     * Session object
     */
    private SessionFirebase session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_navigation_review_session_no_patient, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (SessionFirebase) args.getSerializable(SESSION);

        getActivity().setTitle(getResources().getString(R.string.evaluation_results));

        /**
         * If first public evaluation, show alert dialog about saving sessions
         * and registering in the app.
         */
        Activity context = getActivity();
        boolean firstPublicEvaluation = SharedPreferencesHelper.checkFirstPublicEvaluation(getActivity());
        if (firstPublicEvaluation) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle(getResources().getString(R.string.session_discard));
            alertDialog.setMessage(context.getResources().getString(R.string.firstPublicEvaluation));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // go to register activity
                            dialog.dismiss();

                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack();

                            Intent intent = new Intent(getActivity(), RegisterUser.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.register_later),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        /**
         * Setup bottom navigation.
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);

        // disable areas that don't have any scale
        Menu menuNav = bottomNavigationView.getMenu();

        int defaultIndex = -1;

        // check if this session contains scales from this area
        ArrayList<GeriatricScaleFirebase> scalesFromSession = FirebaseHelper.getScalesFromSession(session);
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            // current area
            String currentArea = Constants.cga_areas[i];
            // disable area
            menuNav.getItem(i).setEnabled(false);
            for (GeriatricScaleFirebase scale : scalesFromSession) {
                if (scale.getArea().equals(currentArea)) {
                    menuNav.getItem(i).setEnabled(true);
                    if (defaultIndex == -1) {
                        defaultIndex = i;
                        menuNav.getItem(i).setChecked(true);
                    }
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

        Constants.bottomNavigationReviewSession = defaultIndex;
        transaction.replace(R.id.frame_layout_cga_area, fragments.get(defaultIndex));
        transaction.commit();


        final Map<Integer, Integer> fragmentMapping = new HashMap<>();
        fragmentMapping.put(R.id.cga_mental, 0);
        fragmentMapping.put(R.id.cga_functional, 1);
        fragmentMapping.put(R.id.cga_nutritional, 2);
        fragmentMapping.put(R.id.cga_social, 3);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;

                        Integer selectedIndex = fragmentMapping.get(item.getItemId());

                        if (Constants.bottomNavigationReviewSession != selectedIndex) {
                            Constants.bottomNavigationReviewSession = selectedIndex;
                            fragment = fragments.get(selectedIndex);
                        } else {
                            return true;
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






        // close session FAB
        FloatingActionButton closeFAB = (FloatingActionButton) view.findViewById(R.id.close_session);
        closeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                // check if logged in
                Fragment fragment;
                if (SharedPreferencesHelper.isLoggedIn(getActivity())) {
                    SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                    fragment = new PatientsMain();
                } else {
                    SharedPreferencesHelper.resetPublicSession(getActivity(), session.getGuid());
                    fragment = new CGAPublicInfo();
                }
                fragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .replace(R.id.current_fragment, fragment)
                        .commit();
            }
        });

        return view;
    }

}

