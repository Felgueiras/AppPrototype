package com.example.rafael.appprototype.Evaluations.SingleArea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.Evaluations.PickPatientFragment;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class CGAAreaPrivate extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";
    public static String CGA_AREA = "area";

    Patient patientForThisSession;

    public static String SESSION = "session";

    boolean resuming = false;


    private FloatingActionButton discardFAB;
    private FloatingActionButton saveFAB;
    private Session session;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area_private, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        String area = args.getString(CGA_AREA);
        /*
      Session object
     */
        session = (Session) args.getSerializable(SESSION);
        getActivity().setTitle(area);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        ScaleCard adapter;
        RecyclerView.Adapter finalAdapter = null;

        // read patient for this session
        if (patientForThisSession != null) {
            adapter = new ScaleCard(getActivity(), session, resuming, patientForThisSession.getGender(), area);
        } else {
            // new evaluation created for no Patient
            adapter = new ScaleCard(getActivity(), session, resuming, Constants.SESSION_GENDER, area);
        }
        finalAdapter = adapter;

        // display the different scales to choose from this area
        int numbercolumns = 1;
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            numbercolumns = 2;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (area != Constants.cga_clinical) {
            recyclerView.setAdapter(finalAdapter);
        } else {
            recyclerView.setAdapter(new ClinicalEvaluation(getActivity(),
                    session,
                    resuming,
                    Constants.SESSION_GENDER,
                    area));
        }

        return view;
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

                Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
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
        SharedPreferencesHelper.lockSessionCreation(getActivity());

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_discard));
        alertDialog.setMessage(getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        Patient p = session.getPatient();
                        // how many sessions this patient have
                        FragmentManager fragmentManager = getFragmentManager();
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

}

