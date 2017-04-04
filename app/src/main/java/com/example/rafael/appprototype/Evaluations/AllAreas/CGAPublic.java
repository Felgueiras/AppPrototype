package com.example.rafael.appprototype.Evaluations.AllAreas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Create a new public CGA.
 */
public class CGAPublic extends Fragment {

    private Session session;

    boolean resuming = false;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.menu_cga_public, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.session_finish:
                finishSession();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_new_session_public_bottom_buttons, container, false);
        getActivity().setTitle(getResources().getString(R.string.cga_public));

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.sharedPreferencesTag), MODE_PRIVATE);


        Log.d("Session", "Inside CGAPublicBottomButtons");


        /**
         * Resume an Evaluation.
         */
        final String sessionID = SharedPreferencesHelper.isThereOngoingPublicSession(getActivity());
        boolean canCreateSessions = SharedPreferencesHelper.isSessionCreationPermitted(getActivity());

        if (sessionID != null) {
            // get session by ID
            session = Session.getSessionByID(sessionID);
        }
        /**
         * Create a new one.
         */
        else {
            if (canCreateSessions) {
                // create a new session
                createNewSession();
                addScalesToSession();

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
                                dialog.dismiss();
                            }
                        });


                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }
        }


        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.area_scales_recycler_view);
        AreaCard adapter = new AreaCard(getActivity(), session, resuming, Constants.SESSION_GENDER);

        Button finishSession = (Button) myInflatedView.findViewById(R.id.session_finish);
        finishSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSession();
            }
        });


        // create Layout
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /*
        saveFAB = (FloatingActionButton) myInflatedView.findViewById(R.id.session_save);
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.newSessionLayout);

                // no test selected
                if (session.getScalesFromSession().size() == 0) {
                    Snackbar.make(layout, getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // check how many tests were completed
                int numTestsCompleted = 0;
                List<GeriatricScale> testsFromSession = session.getScalesFromSession();
                for (GeriatricScale test : testsFromSession) {
                    if (test.isCompleted())
                        numTestsCompleted++;
                }
                if (numTestsCompleted == 0) {
                    Snackbar.make(layout, getResources().getString(R.string.not_all_tests_complete), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // check if there is an added PATIENT or not
                // no PATIENT selected
                if (PATIENT == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    //alertDialog.setTitle("Criar paciente");
                    alertDialog.setMessage("Deseja adicionar paciente a esta sessão?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    /**
                                     * Open the fragment to pick an already existing Patient.
                                     */
        /*
                                    Constants.pickingPatient = true;
                                    Bundle args = new Bundle();
                                    args.putBoolean(PatientsList.selectPatient, true);
                                    FragmentTransitions.replaceFragment(getActivity(), new PatientsList(), args,
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
                                    Constants.SESSION_ID = null;
                                    // delete Session from DB
                                    session.delete();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_fragment, new PatientsMain())
                                            .commit();
                                    Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    alertDialog.show();
                    return;
                }
                Constants.SESSION_ID = null;
                List<GeriatricScale> finalTests = session.getScalesFromSession();
                for (GeriatricScale test : finalTests) {
                    if (!test.isCompleted()) {
                        test.setSession(null);
                        test.delete();
                    }
                }

                Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                BackStackHandler.goToPreviousScreen();
            }
        });
        */

        return myInflatedView;
    }

    /**
     * Add Tests to a Session
     */
    private void addScalesToSession() {
        ArrayList<GeriatricScaleNonDB> testsNonDB = Scales.getAllScales();
        for (GeriatricScaleNonDB testNonDB : testsNonDB) {
            GeriatricScale scale = new GeriatricScale();
            scale.setGuid(session.getGuid() + "-" + testNonDB.getScaleName());
            scale.setTestName(testNonDB.getScaleName());
            scale.setShortName(testNonDB.getShortName());
            scale.setArea(testNonDB.getArea());
            scale.setSession(session);
            scale.setDescription(testNonDB.getDescription());
            if (testNonDB.isSingleQuestion())
                scale.setSingleQuestion(true);
            scale.setAlreadyOpened(false);
            scale.save();
        }
    }

    /**
     * Generate a new Session.
     */
    private void createNewSession() {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        String sessionID = time.toString();
        // save to dabatase
        session = new Session();
        session.setGuid(sessionID);

        // set date
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        session.setDate(DatesHandler.createCustomDate(year, month, day, hour, minute));
        //system.out.println("Session date is " + session.getDate());
        session.save();

        // save the ID
        sharedPreferences.edit().putString(getString(R.string.saved_session_public), sessionID).apply();
    }

    public void finishSession() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_reset));
        alertDialog.setMessage(getResources().getString(R.string.session_reset_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
                            SharedPreferencesHelper.resetPublicSession(getActivity(), null);

//                                    BackStackHandler.clearBackStack();
                            Bundle args = new Bundle();
                            args.putSerializable(ReviewSingleSessionNoPatient.SESSION, sessionCopy);
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment fragment = new ReviewSingleSessionNoPatient();
                            fragment.setArguments(args);
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                            fragmentManager.popBackStack();
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
}

