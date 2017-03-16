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
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Evaluations.PickPatientFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Create a new private CGA.
 */
public class CGAPrivate extends Fragment {

    public static final String PATIENT = "patient";
    public static String GENDER = "gender";


    private Session session;


    Patient patient;

    boolean resuming = false;

    public static FloatingActionButton discardFAB;
    private static FloatingActionButton saveFAB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_cga_private_patient, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.session_save:
                /**
                 * Create session.
                 */
                RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.newSessionLayout);

                // no test selected
                if (session.getScalesFromSession().size() == 0) {
                    Snackbar.make(layout, getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(layout, getResources().getString(R.string.complete_one_scale_atleast), Snackbar.LENGTH_SHORT).show();
                    break;
                }

                // check if there is an added patient or not
                // no patient selected
                if (patient == null) {
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
//                                    fragmentManager.popBackStack();
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
//                                    args.putSerializable(ReviewSingleSessionNoPatient.SESSION, sessionCopy);
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
                if (patient.isFirstSession()) {
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

                Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                BackStackHandler.goToPreviousScreen();
                break;
            case R.id.session_cancel:
                discardSession();
                break;

        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("CGAPrivate", "onCreateView");
        Bundle args = getArguments();
        if (args != null)
            patient = (Patient) args.getSerializable(PATIENT);

        // Inflate the layout for this fragment
        View view;
        if (patient != null) {
            view = inflater.inflate(R.layout.content_new_session_private, container, false);
            getActivity().setTitle(patient.getName() + " - " + getResources().getString(R.string.cga));
        } else {
            view = inflater.inflate(R.layout.content_new_session_private_no_patient, container, false);
            getActivity().setTitle(getResources().getString(R.string.cga));
        }


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(getActivity());


        if (sessionID != null) {
            // get session by ID
            session = Session.getSessionByID(sessionID);
            patient = session.getPatient();
            // create a new Fragment to hold info about the Patient
            if (patient != null) {
                // set the patient for this session
                session.setPatient(patient);
                session.save();
            }
        }
        /**
         * Create a new one.
         */
        else {
            // create a new session
            createNewSession();
            addTestsToSession();
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
                        BackStackHandler.discardSession(session.getPatient());
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
    private void addTestsToSession() {
        ArrayList<GeriatricScaleNonDB> testsNonDB = Scales.getAllScales();
        for (GeriatricScaleNonDB testNonDB : testsNonDB) {
            GeriatricScale test = new GeriatricScale();
            test.setGuid(session.getGuid() + "-" + testNonDB.getScaleName());
            test.setTestName(testNonDB.getScaleName());
            test.setArea(testNonDB.getArea());
            test.setShortName(testNonDB.getShortName());
            test.setSession(session);
            test.setDescription(testNonDB.getDescription());
            if (testNonDB.isSingleQuestion())
                test.setSingleQuestion(true);
            test.setAlreadyOpened(false);
            test.save();
        }
    }

    /**
     * Generate a new SESSION_ID.
     */
    private void createNewSession() {
        Log.d("CGAprivate", "Creating new session");
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        String sessionID = time.toString();
        // save to dabatase
        session = new Session();
        session.setGuid(sessionID);
        session.setPatient(patient);


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
        SharedPreferencesHelper.setPrivateSession(getActivity(), sessionID);
    }


}

