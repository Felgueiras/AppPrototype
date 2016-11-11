package com.example.rafael.appprototype.NewSessionTab;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.CreateTestCard;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.SelectPatientFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.SessionsHistoryTab.SessionsHistoryFragment;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewSinglePatientInfoAndSessions;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewSinglePatientOnlyInfo;
import com.example.rafael.appprototype.ViewPatientsTab.ViewPatientsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewSessionFragment extends Fragment {

    public static final String SAVE_SESSION = "save";
    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";

    Patient patientForThisSession;
    /**
     * Session object
     */
    private Session session;

    public static String sessionObject = "session";

    boolean resuming = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_new_session_tab, container, false);
        Bundle args = getArguments();
        if (args != null) {
            session = (Session) args.getSerializable(sessionObject);
            Log.d("Session", "NewSessionFragment" + session);
            if (session != null) {
                resuming = true;
                Log.d("NewSession", "Resuming");
            } else {
                // generate a new ID for this Session
                Log.d("Session", "Generating new Session ID");
                // only create a new ID when we have a patient
                createNewSessionID();
            }

            patientForThisSession = (Patient) args.getSerializable(PATIENT);
            Log.d("New Session", "We have patient: " + (patientForThisSession != null));
            // create a new Fragment to hold info about the Patient
            Fragment fragment = new ViewSinglePatientOnlyInfo();
            Bundle newArgs = new Bundle();
            if (patientForThisSession != null) {
                // set the patient for this session
                session.setPatient(patientForThisSession);
                Log.d("Session", "Setting patient");
                session.save();
                /*
                newArgs.putSerializable(ViewSinglePatientInfoAndSessions.PATIENT, patientForThisSession);
                fragment.setArguments(newArgs);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patientInfo, fragment)
                        .commit();
                        */
                // save this session and start a new one
                boolean save = args.getBoolean(SAVE_SESSION);
                if (save) {
                    Log.d("Session", "Saving!");
                }
            } else {
                /*
                newArgs.putSerializable(SelectPatientFragment.PATIENT, patientForThisSession);
                fragment.setArguments(newArgs);
                fragment = new SelectPatientFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.patientInfo, fragment)
                        .commit();
                        */
            }

        } else {
            // generate a new ID for this Session
            createNewSessionID();
            /**
             * Assuming we don't have the Patient
             */
            /*
            Fragment fragment = new SelectPatientFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.patientInfo, fragment)
                    .commit();
                    */
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
        // testsList.add(StaticTestDefinition.escalaDeKatz());
        testsList.add(StaticTestDefinition.escalaDepressao());
        //testsList.add(StaticTestDefinition.escalaLawtonBrody());
        //testsList.add(StaticTestDefinition.marchaHolden());
        /*
      Adapter to the RecyclerView
     */
        CreateTestCard adapter = new CreateTestCard(getActivity(), testsList, session, resuming, patientForThisSession);

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
                // check if there is an added patient or not
                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.newSessionLayout);
                // no patient selected
                if (patientForThisSession == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    //alertDialog.setTitle("Criar paciente");
                    alertDialog.setMessage("Deseja adicionar paciente a esta sessão?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * Open the fragment to pick an already existing Patient.
                                     */
                                    Bundle args = new Bundle();
                                    args.putBoolean(ViewPatientsFragment.selectPatient, true);
                                    ((MainActivity) getActivity()).replaceFragment(ViewPatientsFragment.class, args,
                                            Constants.fragment_show_patients);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // reset this Session
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, new NewSessionFragment())
                                            .commit();
                                }
                            });
                    alertDialog.show();
                    //Snackbar.make(linearLayout, getResources().getString(R.string.you_must_add_patient), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // no test selected
                if (session.getTestsFromSession().size() == 0) {
                    Snackbar.make(linearLayout, getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // check if there is any incomplete test
                List<GeriatricTest> testsFromSession = session.getTestsFromSession();
                for (GeriatricTest test : testsFromSession) {
                    // incomplete test
                    if (!test.isCompleted()) {
                        Snackbar.make(linearLayout, getResources().getString(R.string.not_all_tests_complete), Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.d("Session", "Finishing!");
                ((MainActivity) getActivity()).replaceFragment(SessionsHistoryFragment.class, null, Constants.tag_view_sessions_history);
            }
        });

        return myInflatedView;
    }

    /**
     * Generate a new sessionID.
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

