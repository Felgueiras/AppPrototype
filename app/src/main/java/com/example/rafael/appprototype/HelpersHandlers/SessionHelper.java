package com.example.rafael.appprototype.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.Evaluations.PickPatientFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felgueiras on 24/03/2017.
 */

public class SessionHelper {

    public static void saveSession(final Activity context, final Session session, Patient patient, final View view, View layout, int i) {
        Log.d("Session", "Saving " + i);
        /**
         * Create session.
         */

        // no test selected
        if (session.getScalesFromSession().size() == 0) {
            Log.d("Session", "size");
            Snackbar.make(layout, context.getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
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
            Snackbar.make(layout, context.getResources().getString(R.string.complete_one_scale_atleast), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // check if there is an added patient or not
        // no patient selected
        if (patient == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("Criar paciente");
            alertDialog.setMessage("Deseja adicionar paciente a esta sessão?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /**
                             * Open the fragment to pick an already existing Patient.
                             */
                            FragmentManager fragmentManager = context.getFragmentManager();
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
                            FragmentManager fragmentManager = context.getFragmentManager();
//                                    fragmentManager.popBackStack();
                            BackStackHandler.clearBackStack();
                            session.eraseScalesNotCompleted();

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
//                                            .addToBackStack(Constants.tag_review_session_from_sessions_list)
//                                            .commit();

                            dialog.dismiss();
//                                    Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                        }
                    });
            alertDialog.show();
            return;
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
            // TODO first session for a patient
//                    if (!allAreasEvaluated) {
//                        Snackbar.make(layout, getResources().getString(R.string.first_session_evaluate_all_areas), Snackbar.LENGTH_SHORT).show();
//                        return;
//                    }

        }
        Log.d("Session", "Showing dialog");
        // prompt if really want to save it
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
        alertDialog.setMessage("Deseja mesmo guardar a Sessão que tinha em curso?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * Erase scales that weren't completed.
                         */
                        session.eraseScalesNotCompleted();

                        // display results in JSON
                        DatabaseOps.displayData(context);

                        Snackbar.make(view, context.getResources().getString(R.string.session_created), Snackbar.LENGTH_LONG).show();
                        BackStackHandler.goToPreviousScreen();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


    public static void cancelSession(final Activity context, final Session session, final View view, final String place) {
        Log.d("Stack", "Cancel");
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.session_discard));
        alertDialog.setMessage(context.getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        SharedPreferencesHelper.lockSessionCreation(context);

                        Patient p = session.getPatient();
                        // how many sessions this patient have
                        FragmentManager fragmentManager = context.getFragmentManager();
                        if (place.equals(Constants.SCALE)) {
                            fragmentManager.popBackStack();
                            fragmentManager.popBackStack();
                        } else if (place.equals(Constants.AREA)) {
                            fragmentManager.popBackStack();
                        }
                        SharedPreferencesHelper.resetPrivateSession(context, session.getGuid());
                        BackStackHandler.discardSession(p);
                        dialog.dismiss();
                        Snackbar.make(view, context.getResources().getString(R.string.session_discarded), Snackbar.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
