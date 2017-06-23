package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;


public class PatientTimelineFragmentGroupByDay extends Fragment {

    public static final String PATIENT = "PATIENT";
    private PatientFirebase patient;


    private RecyclerView mRecyclerView;
    private TimeLineAdapterGeneralGroupByDay mTimeLineAdapter;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private ArrayList<SessionFirebase> patientSessions;
    private ArrayList<PrescriptionFirebase> patientsPrescriptions;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        patient = (PatientFirebase) bundle.getSerializable(PATIENT);

        mOrientation = Orientation.VERTICAL;
        mWithLinePadding = false;
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_general, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

//        /**
//         * Setup FABS
//         */
//        FloatingActionButton fabAddSession = (FloatingActionButton) view.findViewById(R.id.patient_createSession);
//        fabAddSession.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle args = new Bundle();
//                args.putSerializable(CGAPrivate.PATIENT, patient);
//                // pass the previous session
//                SharedPreferencesHelper.unlockSessionCreation(getActivity());
//                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session_with_patient);
//                getActivity().setTitle(getResources().getString(R.string.cga));
//            }
//        });

        // create timeline
        initView();
        return view;
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
    }

    /**
     * Create timeline.
     */
    private void initView() {
        retrievePatientSessions();
    }


    /**
     * Retrieve the patient's sessions.
     */
    private void retrievePatientSessions() {

        FirebaseHelper.firebaseTableSessions.orderByChild("patientID").equalTo(patient.getGuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patientSessions = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SessionFirebase sessions = postSnapshot.getValue(SessionFirebase.class);
                    sessions.setKey(postSnapshot.getKey());
                    patientSessions.add(sessions);
                }

                // get prescriptions
                FirebaseHelper.firebaseTablePrescriptions.orderByChild("patientID")
                        .equalTo(patient.getGuid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                patientsPrescriptions = new ArrayList<>();
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    PrescriptionFirebase prescription = postSnapshot.getValue(PrescriptionFirebase.class);
                                    prescription.setKey(postSnapshot.getKey());
                                    patientsPrescriptions.add(prescription);
                                }

                                ArrayList<DailyEvents> dailyEvents = getDailyEvents();


//                                dailyEvents.addAll(patientsPrescriptions);


//                                 sort all by date
//                                Collections.sort(dailyEvents, new Comparator<Object>() {
//                                    public int compare(Object o1, Object o2) {
//                                        Date d1, d2;
//                                        if (o1 instanceof ArrayList<?>) {
//                                            d1 = ((ArrayList<PrescriptionFirebase>) o1).get(0).getDate();
//                                        } else {
//                                            d1 = new Date(((SessionFirebase) o1).getDate());
//                                        }
//                                        if (o2 instanceof ArrayList<?>) {
//                                            d2 = ((ArrayList<PrescriptionFirebase>) o2).get(0).getDate();
//                                        } else {
//                                            d2 = new Date(((SessionFirebase) o2).getDate());
//                                        }
//                                        return d2.compareTo(d1);
//                                    }
//                                });


                                mTimeLineAdapter = new TimeLineAdapterGeneralGroupByDay(dailyEvents,
                                        mOrientation, mWithLinePadding, getActivity(), false);
                                mRecyclerView.setAdapter(mTimeLineAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }


    /**
     * Get Sessions and Prescriptions for a single day.
     */
    public ArrayList<DailyEvents> getDailyEvents() {

        ArrayList<DailyEvents> dailyEvents = new ArrayList<>();


        // get events by date
        HashSet<Date> daysSet = new HashSet<>();
        for (PrescriptionFirebase prescriptionFirebase : patientsPrescriptions) {
            Date dateWithoutHour = DatesHandler.getDateWithoutHour(prescriptionFirebase.getDate().getTime());
            daysSet.add(dateWithoutHour);
        }
        for (SessionFirebase session : patientSessions) {
            Date dateWithoutHour = DatesHandler.getDateWithoutHour(session.getDate());
            daysSet.add(dateWithoutHour);
        }

        ArrayList<Date> differentDates = new ArrayList<>();
        differentDates.addAll(daysSet);
        // order by date (descending)
        Collections.sort(differentDates, new Comparator<Date>() {
            @Override
            public int compare(Date first, Date second) {
                return second.compareTo(first);
            }
        });

        // get events for current date
        for (Date currentDate : differentDates) {
            // get prescriptions from this date
            ArrayList<PrescriptionFirebase> prescriptionsForDate = new ArrayList<>();
            for (PrescriptionFirebase prescription : patientsPrescriptions) {
                if (DatesHandler.getDateWithoutHour(prescription.getDate().getTime()).compareTo(currentDate) == 0) {
                    prescriptionsForDate.add(prescription);
                }
            }
            // get sessions from this date
            ArrayList<SessionFirebase> sessionsForDate = new ArrayList<>();
            for (SessionFirebase session : patientSessions) {
                if (DatesHandler.getDateWithoutHour(session.getDate()).compareTo(currentDate) == 0) {
                    sessionsForDate.add(session);
                }
            }
            // add to array
            dailyEvents.add(new DailyEvents(currentDate, prescriptionsForDate, sessionsForDate));

        }

        return dailyEvents;
    }
}