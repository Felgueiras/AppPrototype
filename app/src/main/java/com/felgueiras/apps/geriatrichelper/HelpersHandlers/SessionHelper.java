package com.felgueiras.apps.geriatrichelper.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Sessions.PickPatientFragment;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.Patients.PatientsMain;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.List;

/**
 * Created by felgueiras on 24/03/2017.
 */

public class SessionHelper {

    public static void saveSession(final Activity context, final SessionFirebase session, PatientFirebase patient, final View view, View layout, int i) {
        /*
          Create session.
         */

        // no test selected
        if (FirebaseDatabaseHelper.getScalesFromSession(session).size() == 0) {
            Log.d("Session", "size");
            Snackbar.make(layout, context.getResources().getString(R.string.you_must_select_test), Snackbar.LENGTH_SHORT).show();
            return;
        }


        // check how many tests were completed
        int numTestsCompleted = 0;
        List<GeriatricScaleFirebase> testsFromSession = FirebaseDatabaseHelper.getScalesFromSession(session);
        for (GeriatricScaleFirebase test : testsFromSession) {
            if (test.isCompleted())
                numTestsCompleted++;
        }
        if (numTestsCompleted == 0) {
            Snackbar.make(layout, context.getResources().getString(R.string.complete_one_scale_atleast), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // check if there is an added PATIENT or not
        // no PATIENT selected
        if (patient == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("Criar paciente");
            alertDialog.setMessage("Deseja adicionar paciente a esta sessão?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*
                              Open the fragment to pick an already existing Patient.
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
                            FirebaseDatabaseHelper.eraseScalesNotCompleted(session);

                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                            fragmentManager.beginTransaction()
                                    .remove(currentFragment)
                                    .replace(R.id.current_fragment, new PatientsMain())
                                    .commit();

//                                    /**
//                                     * Review session created for PATIENT.
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

        /*
          If first session, all areas must be evaluated.
         */
//        if (patient.isFirstSession()) {
//            // check all areas are evaluated -> at least one test completed
//            boolean allAreasEvaluated = true;
//            for (String currentArea : Constants.cga_areas) {
//                ArrayList<GeriatricScale> scalesFromArea = session.getScalesFromArea(currentArea);
//                boolean oneScaleEvaluated = false;
//                for (GeriatricScale currentScale : scalesFromArea) {
//                    if (currentScale.isCompleted()) {
//                        oneScaleEvaluated = true;
//                        break;
//                    }
//                }
//                if (!oneScaleEvaluated) {
//                    allAreasEvaluated = false;
//                    break;
//                }
//            }
////            if (!allAreasEvaluated) {
////                Snackbar.make(layout, context.getResources().getString(R.string.first_session_evaluate_all_areas), Snackbar.LENGTH_SHORT).show();
////                return;
////            }
//
//        }
        promptDialogReallyWishToSaveSession(session, context, view);

    }

    private static void promptDialogReallyWishToSaveSession(final SessionFirebase session, final Context context, final View view) {
        // prompt if really want to save it
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//            alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
        alertDialog.setMessage("Deseja mesmo guardar a Sessão que tinha em curso?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                          Erase scales that weren't completed.
                         */
                        FirebaseDatabaseHelper.eraseScalesNotCompleted(session);

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


    /**
     * Cancel a session.
     *
     * @param context
     * @param session
     * @param view
     * @param place
     */
    public static void cancelSession(final Activity context, final SessionFirebase session, final View view, final String place) {
        Log.d("Stack", "Cancel");
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.session_discard));
        alertDialog.setMessage(context.getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        SharedPreferencesHelper.lockSessionCreation(context);

//                        PatientFirebase p = FirebaseHelper.getPatientFromSession(session);
                        // how many sessions this PATIENT have
                        FragmentManager fragmentManager = context.getFragmentManager();
                        if (place.equals(Constants.SCALE)) {
                            fragmentManager.popBackStack();
                            fragmentManager.popBackStack();
                        } else if (place.equals(Constants.AREA)) {
                            fragmentManager.popBackStack();
                        }
                        SharedPreferencesHelper.resetPrivateSession(context, "");
                        BackStackHandler.discardSession(session);

                        FirebaseHelper.getSessions().remove(session);
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
