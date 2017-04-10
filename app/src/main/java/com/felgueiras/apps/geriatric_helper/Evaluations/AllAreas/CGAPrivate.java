package com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SessionHelper;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Create a new private CGA.
 */
public class CGAPrivate extends Fragment {

    public static final String PATIENT = "PATIENT";

    private SessionFirebase session;

    PatientFirebase patient;

    boolean resuming = false;

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

        Bundle args = getArguments();
        if (args != null)
            patient = (PatientFirebase) args.getSerializable(PATIENT);

        // Inflate the layout for this fragment
        View view;
        if (patient != null) {
            view = inflater.inflate(R.layout.content_new_session_private_bottom_buttons, container, false);
            getActivity().setTitle("Nova AGG - " + patient.getName());
        } else {
            view = inflater.inflate(R.layout.content_new_session_private_no_patient_bottom_buttons, container, false);
            getActivity().setTitle("Nova AGG");
        }


        String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(getActivity());
        boolean canCreateSessions = SharedPreferencesHelper.isSessionCreationPermitted(getActivity());
        Log.d("Stack", "Session id is " + sessionID);
        Log.d("Stack", "Can create sessions? " + canCreateSessions);


        if (sessionID != null) {
            // get session by ID
            session = FirebaseHelper.getSessionByID(sessionID);
            patient = FirebaseHelper.getPatientFromSession(session);
            // create a new Fragment to hold info about the Patient
            if (patient != null) {
                // set the PATIENT for this session
                session.setPatientID(patient.getGuid());
                FirebaseHelper.firebaseTableSessions.child(session.getKey()).child("patientID").setValue(patient.getGuid());
            }
        }
        /**
         * Create a new one.
         */
        else {
            if (canCreateSessions) {
                // create a new session
                createNewSession();
                addScalesToSession();
                if (patient != null)
                    Constants.SESSION_GENDER = patient.getGender();
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.select_patient_gender);

                    //list of items
                    String[] items = new String[]{"M", "F"};
                    builder.setSingleChoiceItems(items, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // item selected logic
                                    if (which == 0)
                                        Constants.SESSION_GENDER = Constants.MALE;
                                    else
                                        Constants.SESSION_GENDER = Constants.FEMALE;
                                }
                            });

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                }
                            });

//                String negativeText = getString(android.R.string.cancel);
//                builder.setNegativeButton(negativeText,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // negative button logic
//                            }
//                        });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                }


                SharedPreferencesHelper.lockSessionCreation(getActivity());
            }
        }


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        AreaCard adapter;

        // new evaluation created for no Patient
        adapter = new AreaCard(getActivity(), session, resuming, Constants.SESSION_GENDER);

        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Button saveButton = (Button) view.findViewById(R.id.session_save);
        Button cancelButton = (Button) view.findViewById(R.id.session_cancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.newSessionLayout);
                SessionHelper.saveSession(getActivity(), session, patient, getView(), layout, 1);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardSession();
            }
        });


        return view;
    }


    public void discardSession() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_discard));
        alertDialog.setMessage(getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                        BackStackHandler.discardSession(session);
                        dialog.dismiss();
                        Snackbar.make(getView(), getResources().getString(R.string.session_discarded), Snackbar.LENGTH_SHORT).show();
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

    /**
     * Add Tests to a Session
     */
    private void addScalesToSession() {
        ArrayList<GeriatricScaleNonDB> testsNonDB = Scales.getAllScales();
        for (GeriatricScaleNonDB testNonDB : testsNonDB) {
            GeriatricScaleFirebase test = new GeriatricScaleFirebase();
            test.setGuid(session.getGuid() + "-" + testNonDB.getScaleName());
            test.setTestName(testNonDB.getScaleName());
            test.setArea(testNonDB.getArea());
            test.setShortName(testNonDB.getShortName());
            test.setSessionID(session.getGuid());
            test.setSubCategory(testNonDB.getSubCategory());
            test.setDescription(testNonDB.getDescription());
            if (testNonDB.isSingleQuestion())
                test.setSingleQuestion(true);
            test.setAlreadyOpened(false);
            if (testNonDB.getScaleName().equals(Constants.test_name_marchaHolden))
                test.setContainsPhoto(true);
            session.addScaleID(test.getGuid());
            FirebaseHelper.createScale(test);
        }
    }

    /**
     * Generate a new SESSION_ID.
     */
    private void createNewSession() {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        String sessionID = time.toString();
        // save to dabatase
        session = new SessionFirebase();
        session.setGuid(sessionID);
        if (patient != null) {
            session.setPatientID(patient.getGuid());

            // add session to patient
            patient.addSession(session.getGuid());
        }


        // set date
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        session.setDate(DatesHandler.createCustomDate(year, month, day, hour, minute));

        // save Session
        FirebaseHelper.createSession(session);

        // save the ID
        SharedPreferencesHelper.setPrivateSession(getActivity(), sessionID);
    }


}

