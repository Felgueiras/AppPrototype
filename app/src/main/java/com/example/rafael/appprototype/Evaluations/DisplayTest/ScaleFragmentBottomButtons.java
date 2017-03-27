package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.SessionHelper;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.R;

/**
 * Display a list of Questions for a single Test.
 */
public class ScaleFragmentBottomButtons extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (!SharedPreferencesHelper.isLoggedIn(getActivity())) {
            /**
             * Public area.
             */
            view = inflater.inflate(R.layout.content_display_single_test_bottom_buttons_public, container, false);

            // finish
            Button finishSession = (Button) view.findViewById(R.id.session_finish);
            finishSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishSession();
                }
            });
        } else {
            /**
             * Private area.
             */
            view = inflater.inflate(R.layout.content_display_single_test_bottom_buttons_private, container, false);

            Button saveButton = (Button) view.findViewById(R.id.session_save);
            Button cancelButton = (Button) view.findViewById(R.id.session_cancel);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View viewForSnackbar = getActivity().findViewById(R.id.scale_progress);
                    SessionHelper.saveSession(getActivity(), session, session.getPatient(), viewForSnackbar, viewForSnackbar, 3);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View viewForSnackbar = getActivity().findViewById(R.id.scale_progress);
                    SessionHelper.cancelSession(getActivity(), session, viewForSnackbar, Constants.SCALE);
                }
            });
        }

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
                        Snackbar.make(getView(), "Sessão terminada", Snackbar.LENGTH_SHORT).show();

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