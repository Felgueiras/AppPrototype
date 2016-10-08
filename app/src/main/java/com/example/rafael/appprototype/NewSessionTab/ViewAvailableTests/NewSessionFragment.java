package com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewSessionFragment extends Fragment {
    /**
     * Recycler view that will hold the cards of the different tests
     */
    private RecyclerView recyclerView;
    /**
     * List of all the tests available
     */
    private ArrayList<GeriatricTestNonDB> testsList;
    /**
     * Adapter to the RecyclerView
     */
    private GenerateTestsCardAdapter adapter;

    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";

    Patient patientForThisSession;
    /**
     * The ID for this Session
     */
    private String sessionID;
    /**
     * Session object
     */
    private Session session;

    public static String sessionObject = "session";

    boolean resuming = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_new_session_tab, container, false);
        Bundle args = getArguments();
        if (args != null) {
            /**
             * Assuming we already have the Patient
             */
            session = (Session) args.getSerializable(sessionObject);
            if (session != null) {
                /**
                 * This Session is being resumed
                 */
                Toast.makeText(getActivity(), "RESUMING", Toast.LENGTH_SHORT).show();
                resuming = true;
                Log.d("NewSession","Resuming");
            } else {
                // generate a new ID for this Session
                createNewSessionID();
            }
            patientForThisSession = (Patient) args.getSerializable(PATIENT);
            // create a new Fragment to hold info about the Patient
            Fragment fragment = new ViewPatientFragment();
            Bundle newArgs = new Bundle();
            if (patientForThisSession != null) {
                newArgs.putSerializable(ViewPatientFragment.PATIENT, patientForThisSession);
                fragment.setArguments(newArgs);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patientInfo, fragment)
                        .commit();
            } else {
                fragment = new SelectPatientFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patientInfo, fragment)
                        .commit();
            }

        } else {
            // generate a new ID for this Session
            createNewSessionID();
            /**
             * Assuming we don't have the Patient
             */
            Fragment fragment = new SelectPatientFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.patientInfo, fragment)
                    .commit();
        }

        /**
         * Setup the recycler view for the list of available tests
         */
        recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.testsRecyclerView);
        testsList = new ArrayList<>();
        testsList.add(StaticTestDefinition.escalaDeKatz());
        testsList.add(StaticTestDefinition.escalaDepressao());
        testsList.add(StaticTestDefinition.escalaLawtonBrody());
        testsList.add(StaticTestDefinition.marchaHolden());
        adapter = new GenerateTestsCardAdapter(getActivity(), testsList, session, resuming);

        // create Layout
        int numbercolumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // add on click listener to the button
        Button btn = (Button) myInflatedView.findViewById(R.id.finishSessionButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Session", "Finishing! Going to write to DB!");
                // check if there's at least one test to be commited
                // check if all the tests are finished
                // TODO get info about the tests and add to database
            }
        });

        return myInflatedView;
    }

    /**
     * Generate a new sessionIDString
     */
    private void createNewSessionID() {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        sessionID = time.toString();
        // save to dabatase
        session = new Session();
        session.setGuid(sessionID);
        session.save();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

