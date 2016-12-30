package com.example.rafael.appprototype.Evaluations.NewEvaluation;

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
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.ViewAvailableTests.DisplayTestCard;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Patients.ViewPatients.ViewPatientsFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create a new Evaluation or resume an ongoing one.
 */
public class NewEvaluation extends Fragment {

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

    public static FloatingActionButton discardFAB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_new_session_tab, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_sessions));
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        // read patient for this session
        if (patientForThisSession != null)
            Log.d("Patient", "we already have patient!!");
        /**
         * Resume an Evaluation.
         */
        if (Constants.sessionID != null) {
            // get session by ID
            session = Session.getSessionByID(Constants.sessionID);
            patientForThisSession = (Patient) args.getSerializable(PATIENT);
            Log.d("New Session", "We have patient: " + (patientForThisSession != null));
            // create a new Fragment to hold info about the Patient
            if (patientForThisSession != null) {
                // set the patient for this session
                session.setPatient(patientForThisSession);
                Log.d("Session", "Setting patient");
                session.save();
                if (Constants.pickingPatient) {
                    Constants.pickingPatient = false;
                    Constants.sessionID = null;
                    Log.d("Session", "Finishing!");
                    ((MainActivity) getActivity()).replaceFragment(EvaluationsHistoryMain.class, null, Constants.tag_view_sessions_history);
                }
            }
        }
        /**
         * Create a new one.
         */
        else {
            // create a new session
            createNewSession();
            addTestsToSession();
        }


        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.reviewsTestsRV);
        DisplayTestCard adapter = new DisplayTestCard(getActivity(), session, resuming, patientForThisSession);

        // create Layout
        int numbercolumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        final FloatingActionButton saveFAB = (FloatingActionButton) myInflatedView.findViewById(R.id.session_save);
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.newSessionLayout);

                // no test selected
                if (session.getTestsFromSession().size() == 0) {
                    Snackbar.make(layout, getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // check how many tests were completed
                int numTestsCompleted = 0;
                List<GeriatricTest> testsFromSession = session.getTestsFromSession();
                for (GeriatricTest test : testsFromSession) {
                    if (test.isCompleted())
                        numTestsCompleted++;
                }
                if (numTestsCompleted == 0) {
                    Snackbar.make(layout, getResources().getString(R.string.not_all_tests_complete), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // check if there is an added patient or not
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
                                    Constants.pickingPatient = true;
                                    Bundle args = new Bundle();
                                    args.putBoolean(ViewPatientsFragment.selectPatient, true);
                                    ((MainActivity) getActivity()).replaceFragment(ViewPatientsFragment.class, args,
                                            Constants.fragment_show_patients);
                                    dialog.dismiss();
                                    Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // reset this Session
                                    Constants.sessionID = null;
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, new EvaluationsMainFragment())
                                            .commit();
                                    Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                Constants.sessionID = null;
                List<GeriatricTest> finalTests = session.getTestsFromSession();
                for (GeriatricTest test : finalTests) {
                    if (!test.isCompleted()) {
                        test.setSession(null);
                        test.delete();
                    }
                }

                Log.d("Session", "Finishing!");
                ((MainActivity) getActivity()).replaceFragment(EvaluationsHistoryMain.class, null, Constants.tag_view_sessions_history);
            }
        });

        discardFAB = (FloatingActionButton) myInflatedView.findViewById(R.id.session_discard);
        discardFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.session_discard));
                alertDialog.setMessage(getResources().getString(R.string.session_discard_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // remove session
                                session.delete();
                                Constants.sessionID = null;
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, new EvaluationsMainFragment())
                                        .commit();
                                dialog.dismiss();
                                // Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
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
        });

        return myInflatedView;
    }

    /**
     * Add Tests to a Session
     */
    private void addTestsToSession() {
        /**
         *Nutritional.
         */
        GeriatricTest test = new GeriatricTest();
        GeriatricTestNonDB testNonDB = StaticTestDefinition.miniNutritionalAssessment();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();


        /**
         * Mini mental state.
         */
        test = new GeriatricTest();
        testNonDB = StaticTestDefinition.mentalStateFolstein();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();


        /**
         * Escala depressao.
         */
        test = new GeriatricTest();
        // set name and session
        testNonDB = StaticTestDefinition.escalaDepressaoYesavage();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();

        /**
         * Katz.
         */
        test = new GeriatricTest();
        // set name and session
        testNonDB = StaticTestDefinition.escalaDeKatz();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();

        /**
         * Lawton-Brody.
         */
        test = new GeriatricTest();
        testNonDB = StaticTestDefinition.escalaLawtonBrody();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();

        /**
         * Marcha Holden.
         */
        test = new GeriatricTest();
        testNonDB = StaticTestDefinition.marchaHolden();
        test.setTestName(testNonDB.getTestName());
        test.setShortName(testNonDB.getShortName());
        test.setSession(session);
        test.setAlreadyOpened(false);
        test.save();


    }

    /**
     * Generate a new sessionID.
     */
    private void createNewSession() {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        String sessionID = time.toString();
        // save to dabatase
        session = new Session();
        session.setGuid(sessionID);
        session.setPatient(patientForThisSession);

        // set date
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        session.setDate(Session.dateToString(Session.createCustomDate(year, month, day, hour, minute)));
        session.save();

        // save the ID
        Constants.sessionID = sessionID;
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

