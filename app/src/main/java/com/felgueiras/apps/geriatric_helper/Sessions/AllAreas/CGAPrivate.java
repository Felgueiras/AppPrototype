package com.felgueiras.apps.geriatric_helper.Sessions.AllAreas;

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

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SessionHelper;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

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
        if (args != null) {
            patient = (PatientFirebase) args.getSerializable(PATIENT);
        }

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
            session = FirebaseDatabaseHelper.getSessionByID(sessionID);
            patient = PatientsManagement.getInstance().getPatientFromSession(session, getActivity());
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
                if (patient != null) {
                    getPatientPreviousSession();
                }

                // create a new session
                createNewSession();
                addScalesToSession();
                if (patient != null) {
                    Constants.SESSION_GENDER = patient.getGender();

                } else {
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


    /**
     * Get the patient's previous session, to check if there are notes to display.
     */
    private void getPatientPreviousSession() {

        FirebaseHelper.firebaseTableSessions.orderByChild("patientID").equalTo(patient.getGuid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SessionFirebase> patientSessions = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase sessions = postSnapshot.getValue(SessionFirebase.class);
                    sessions.setKey(postSnapshot.getKey());
                    patientSessions.add(sessions);
                }


                if (patientSessions.size() > 0) {
                    // sort by date descending
                    Collections.sort(patientSessions, new Comparator<SessionFirebase>() {
                        public int compare(SessionFirebase o1, SessionFirebase o2) {
                            return new Date(o2.getDate()).compareTo(new Date(o1.getDate()));
                        }
                    });
                    SessionFirebase previousSession = patientSessions.get(0);


                    boolean previousSessionContainsNotes = false;
                    // check if session has notes
                    if (previousSession.getNotes() != null) {
                        previousSessionContainsNotes = true;
                    }
                    // check if any scale from the session contains notes
                    ArrayList<GeriatricScaleFirebase> scalesPreviousSession = FirebaseDatabaseHelper.getScalesFromSession(previousSession);
                    for (GeriatricScaleFirebase scale : scalesPreviousSession) {
                        if (scale.getNotes() != null) {
                            previousSessionContainsNotes = true;
                        }
                    }
                    if (previousSessionContainsNotes) {
                        // display an alert with the notes from previous session
                        AlertDialog.Builder showPreviousSessionNotes = new AlertDialog.Builder(getActivity());
                        showPreviousSessionNotes.setTitle("Notas da última sessão");
                        String alertMessage = "";
                        alertMessage += previousSession.getNotes();

                        // get session's scales -> access their notes
                        for (GeriatricScaleFirebase scale : scalesPreviousSession) {
                            if (scale.getNotes() != null) {
                                alertMessage += "\n" + scale.getScaleName() + " - " + scale.getNotes();
                            }
                        }
                        showPreviousSessionNotes.setMessage(alertMessage);
                        // check notes for each scale
                        showPreviousSessionNotes.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                        showPreviousSessionNotes.create();
                        showPreviousSessionNotes.show();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
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
        ArrayList<GeriatricScaleNonDB> testsNonDB = Scales.scales;
        for (GeriatricScaleNonDB testNonDB : testsNonDB) {
            GeriatricScaleFirebase test = new GeriatricScaleFirebase();
            test.setGuid(session.getGuid() + "-" + testNonDB.getScaleName());
            test.setScaleName(testNonDB.getScaleName());
            test.setArea(testNonDB.getArea());
            test.setShortName(testNonDB.getShortName());
            test.setSessionID(session.getGuid());
            test.setSubCategory(testNonDB.getSubCategory());
            test.setDescription(testNonDB.getDescription());
            test.setMultipleChoice(testNonDB.isMultipleChoice());
            if (testNonDB.isSingleQuestion())
                test.setSingleQuestion(true);
            test.setAlreadyOpened(false);
            if (testNonDB.getScaleName().equals(Constants.test_name_clock_drawing))
                test.setContainsPhoto(true);
            if (testNonDB.getScaleName().equals(Constants.test_name_tinetti) || testNonDB.getScaleName().equals(Constants.test_name_marchaHolden))
                test.setContainsVideo(true);
            session.addScaleID(test.getGuid());
            FirebaseDatabaseHelper.createScale(test);
        }
    }

    /**
     * Generate a new SESSION_ID.
     */
    private void createNewSession() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String sessionID = date.toString();
        // save to dabatase
        session = new SessionFirebase();
        session.setGuid(sessionID);
        if (patient != null) {
            session.setPatientID(patient.getGuid());

            // add session to patient
            patient.addSession(session.getGuid(), getActivity());
        }


        // set date
        session.setDate(date.getTime());

        // save Session
        FirebaseDatabaseHelper.createSession(session);

        // save the ID
        SharedPreferencesHelper.setPrivateSession(getActivity(), sessionID);
    }


}

