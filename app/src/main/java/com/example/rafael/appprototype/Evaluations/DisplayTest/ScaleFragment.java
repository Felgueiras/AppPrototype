package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.PickPatientFragment;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Display a list of Questions for a single Test.
 */
public class ScaleFragment extends Fragment {

    /**
     * String identifier for the Test.
     */
    public static final String testObject = "scaleNonDB";

    public static String patient = "patient";
    public static String SCALE = "testDBObject";
    public static String CGA_AREA;
    Session session;


    /**
     * Selected Test.
     */
    private GeriatricScaleNonDB scaleNonDB;
    /**
     * GeriatricScale which will be written to the DB.
     */
    private GeriatricScale scaleDB;
    private boolean proceed;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        // get the list of tests
        Bundle bundle = getArguments();
        scaleNonDB = (GeriatricScaleNonDB) bundle.getSerializable(testObject);
        scaleDB = (GeriatricScale) bundle.getSerializable(SCALE);
        session = scaleDB.getSession();

        // set the title
        getActivity().setTitle(scaleNonDB.getShortName());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_display_single_test, container, false);
        // populate the ListView
        ListView testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        ProgressBar progress = (ProgressBar) view.findViewById(R.id.scale_progress);
        // create the adapter
        QuestionsListAdapter adapter = new QuestionsListAdapter(this.getActivity(), scaleNonDB, scaleDB, progress);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferencesTag), Context.MODE_PRIVATE);
        boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
        if (alreadyLogged) {
            inflater.inflate(R.menu.menu_cga_private_patient, menu);
        } else {
            inflater.inflate(R.menu.menu_cga_public, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /**
             * Public.
             */
            case R.id.session_finish:
                finishSession();
                break;
            /**
             * Private.
             */
            case R.id.session_save:
                /**
                 * Create session.
                 */
                View viewForSnackbar = getActivity().findViewById(R.id.scale_progress);

                // no test selected
                if (session.getScalesFromSession().size() == 0) {
                    Snackbar.make(viewForSnackbar, getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
                    break;
                }

                // check how many tests were completed
                int numTestsCompleted = 0;
                List<GeriatricScale> testsFromSession = session.getScalesFromSession();
                for (GeriatricScale test : testsFromSession) {
                    if (test.isCompleted())
                        numTestsCompleted++;
                }
                if (numTestsCompleted == 0) {
                    Snackbar.make(viewForSnackbar, getResources().getString(R.string.complete_one_scale_atleast), Snackbar.LENGTH_SHORT).show();
                    break;
                }

                // check if there is an added patient or not
                // no patient selected
                if (session.getPatient() == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    //alertDialog.setTitle("Criar paciente");
                    alertDialog.setMessage("Deseja adicionar paciente a esta sessão?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * Open the fragment to pick an already existing Patient.
                                     */
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStack();
                                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                    fragmentManager.beginTransaction()
                                            .remove(currentFragment)
                                            .replace(R.id.current_fragment, new PickPatientFragment())
                                            .commit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // reset this Session
                                    FragmentManager fragmentManager = getFragmentManager();
//                                    fragmentManager.popBackStack();
                                    BackStackHandler.clearBackStack();
                                    session.eraseScalesNotCompleted();
                                    Session sessionCopy = session;
                                    SharedPreferencesHelper.resetPrivateSession(getActivity(), "");
                                    SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                    fragmentManager.beginTransaction()
                                            .remove(currentFragment)
                                            .replace(R.id.current_fragment, new PatientsMain())
                                            .commit();

//                                    /**
//                                     * Review session created for patient.
//                                     */
//                                    Bundle args = new Bundle();
//                                    args.putSerializable(ReviewSingleSessionNoPatient.SCALE, sessionCopy);
//                                    Fragment fragment = new ReviewSingleSessionNoPatient();
//                                    fragment.setArguments(args);
//                                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                                    fragmentManager.beginTransaction()
//                                            .remove(currentFragment)
//                                            .replace(R.id.current_fragment, fragment)
//                                            .addToBackStack(Constants.tag_review_session)
//                                            .commit();

                                    dialog.dismiss();
//                                    Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.show();
                    break;
                }

                /**
                 * If first session, all areas must be evaluated.
                 */
                if (session.getPatient().isFirstSession()) {
                    // check all areas are evaluated -> at least one test completed
                    boolean allAreasEvaluated = true;
                    for (String currentArea : Constants.cga_areas) {
                        ArrayList<GeriatricScale> scalesFromArea = session.getScalesFromArea(currentArea);
                        boolean oneScaleEvaluated = false;
                        for (GeriatricScale currentScale : scalesFromArea) {
                            if (currentScale.isCompleted()) {
                                oneScaleEvaluated = true;
                                break;
                            }
                        }
                        if (!oneScaleEvaluated) {
                            allAreasEvaluated = false;
                            break;
                        }
                    }
//                    if (!allAreasEvaluated) {
//                        Snackbar.make(layout, getResources().getString(R.string.first_session_evaluate_all_areas), Snackbar.LENGTH_SHORT).show();
//                        return;
//                    }
                }

                /**
                 * Erase scales that weren't completed.
                 */
                session.eraseScalesNotCompleted();

                // display results in JSON
                DatabaseOps.displayData(getActivity());

                SharedPreferencesHelper.lockSessionCreation(getActivity());


                Snackbar.make(viewForSnackbar, getResources().getString(R.string.session_created), Snackbar.LENGTH_LONG).show();
                BackStackHandler.goToPreviousScreen();
                break;
            case R.id.session_cancel:
                cancelSession();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void cancelSession() {
        Log.d("Stack","Cancel");
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_discard));
        alertDialog.setMessage(getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        SharedPreferencesHelper.lockSessionCreation(getActivity());

                        Patient p = session.getPatient();
                        // how many sessions this patient have
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                        fragmentManager.popBackStack();
                        SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                        BackStackHandler.discardSession(p);
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


    private void finishSession() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_reset));
        alertDialog.setMessage(getResources().getString(R.string.session_reset_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesHelper.lockSessionCreation(getActivity());

                        // remove session
                        session.eraseScalesNotCompleted();
                        Snackbar.make(getView(),"Sessão terminada",Snackbar.LENGTH_SHORT).show();

                        if (session.getScalesFromSession().size() == 0) {
                            SharedPreferencesHelper.resetPublicSession(getActivity(), session.getGuid());

                            BackStackHandler.clearBackStack();
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                            Fragment fragment = new CGAPublicInfo();
                            fragmentManager.beginTransaction()
                                    .remove(currentFragment)
                                    .replace(R.id.current_fragment, fragment)
                                    .commit();
                        } else {
                            Session sessionCopy = session;
                            SharedPreferencesHelper.lockSessionCreation(getActivity());
                            SharedPreferencesHelper.resetPublicSession(getActivity(), null);

                            BackStackHandler.clearBackStack();

                            FragmentManager fragmentManager = getFragmentManager();
//                            fragmentManager.popBackStack();
                            Bundle args = new Bundle();
                            args.putSerializable(ReviewSingleSessionNoPatient.SESSION, sessionCopy);
                            Fragment fragment = new ReviewSingleSessionNoPatient();
                            fragment.setArguments(args);
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction()
                                    .remove(currentFragment)
                                    .replace(R.id.current_fragment, fragment)
//                                            .addToBackStack(Constants.tag_review_session_public)
                                    .commit();
                        }

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


    /**
     * Check if scale is complete when trying to go back.
     */
    public boolean checkComplete() {
        if (proceed)
            return true;
        if (!scaleDB.isCompleted()) {
            /**
             * Scale is incomplete - inform user.
             */
            // wait for option to be selected
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(R.string.select_patient_gender);
            builder.setMessage(R.string.scale_incomplete);

            String positiveText = getString(android.R.string.yes);
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                            dialog.dismiss();
                            proceed = true;
                            getActivity().onBackPressed();
                        }
                    });

            String negativeText = getString(android.R.string.no);
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                            proceed = false;
                            dialog.dismiss();
                        }
                    });

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
        } else {
            proceed = true;
        }
        return proceed;
    }
}