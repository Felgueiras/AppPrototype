package com.example.rafael.appprototype.ReviewSession;

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

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.CreateTestCard;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.SelectPatientFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewSinglePatientInfoAndSessions;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewSinglePatientOnlyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment that will display info for a Session that is being reviewed
 */
public class ReviewSessionFragment extends Fragment {

    /**
     * Patient for this Session
     */
    Patient patient;
    public static String patientObject = "patient";
    /**
     * Session object
     */
    private Session session;
    public static String sessionObject = "session";

    boolean resuming = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_review_session, container, false);
        Bundle args = getArguments();
        if (args != null) {
            session = (Session) args.getSerializable(sessionObject);
            patient = (Patient) args.getSerializable(patientObject);
            // create a new Fragment to hold info about the Patient
            Fragment fragment = new ViewSinglePatientOnlyInfo();
            Bundle newArgs = new Bundle();
            // set the patient for this session
            session.setPatient(patient);
            session.save();
            Log.d("ReviewSession", "Added patient to session");
            newArgs.putSerializable(ViewSinglePatientInfoAndSessions.PATIENT, patient);
            fragment.setArguments(newArgs);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.patientInfo, fragment)
                    .commit();


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
        /*
      Recycler view that will hold the cards of the different tests
     */
        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.testsRecyclerView);
        /*
      List of all the tests available
     */
        ArrayList<GeriatricTestNonDB> testsList = new ArrayList<>();
        //testsList.add(StaticTestDefinition.escalaDeKatz());
        testsList.add(StaticTestDefinition.escalaDepressao());
        //testsList.add(StaticTestDefinition.escalaLawtonBrody());
        //testsList.add(StaticTestDefinition.marchaHolden());
        /*
      Adapter to the RecyclerView
     */
        CreateTestCard adapter = new CreateTestCard(getActivity(), testsList, session, resuming, patient);

        // create Layout
        int numbercolumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return myInflatedView;
    }

    /**
     * Generate a new sessionIDString
     */
    private void createNewSessionID() {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        /*
      The ID for this Session
     */
        String sessionID = time.toString();
        // save to dabatase
        session = new Session();
        session.setGuid(sessionID);
        // set date
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        session.setDate(Session.dateToString(Session.createCustomDate(year, month, day, hour, minute)));
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


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

