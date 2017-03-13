package com.example.rafael.appprototype.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPrivate;
import com.example.rafael.appprototype.Help_Feedback.HelpTopics;
import com.example.rafael.appprototype.Patients.PatientProgress.ProgressDetail;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublic;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPublic;
import com.example.rafael.appprototype.Patients.PatientProgress.ProgressMainFragment;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewSingleScaleFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Evaluations.DisplayTest.ScaleFragment;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rafael on 09-10-2016.
 */
public class BackStackHandler implements FragmentManager.OnBackStackChangedListener {

    private static Activity context = null;
    static FragmentManager fragmentManager;
    private static SharedPreferences sharedPreferences;

    /**
     * Constructor for the BackStackHandler class
     *
     * @param fragmentManager
     * @param mainActivity
     */
    public BackStackHandler(FragmentManager fragmentManager, Activity mainActivity) {
        this.fragmentManager = fragmentManager;
        this.context = mainActivity;
        sharedPreferences = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
    }

    public static void handleBackButton(FragmentManager fragmentManager) {
        Fragment fragment = null;
        Bundle args = null;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // get fragment on top of stack
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
//            Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);


            Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);
            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();

            Log.d("Stack", "handleBackButton (tag):" + tag);
            /**
             * Only pop backstack if changing fragments/screens
             */
            if (!tag.equals(Constants.tag_create_session_no_patient) &&
                    !tag.equals(Constants.tag_create_session_with_patient) &&
                    !tag.equals(Constants.tag_cga_public)) {
                fragmentManager.popBackStack();
            }

            /**
             * Viewing a scale.
             */
            if (tag.equals(Constants.tag_display_session_scale)) {

                fragment = fr;
                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricScale test = (GeriatricScale) arguments.getSerializable(ScaleFragment.testDBobject);
                assert test != null;
                test.setAlreadyOpened(true);
                test.save();
//                Session session = test.getSession();
//                Patient patient = (Patient) arguments.getSerializable(ScaleFragment.patient);
//
//                if (SharedPreferencesHelper.isLoggedIn(context)) {
//                    args = new Bundle();
//                    args.putSerializable(CGAAreaPrivate.sessionObject, session);
//                    args.putSerializable(CGAAreaPrivate.PATIENT, patient);
//                    args.putSerializable(CGAAreaPrivate.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));
//                    fragment = new CGAAreaPrivate();
//                } else {
//                    args = new Bundle();
//                    args.putSerializable(CGAAreaPublic.sessionObject, session);
//                    args.putSerializable(CGAAreaPublic.PATIENT, patient);
//                    args.putSerializable(CGAAreaPublic.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));
//
//                    fragment = new CGAAreaPublic();
//                }
            }
            /**
             * Viewing single area (public).
             */
            else if (tag.equals(Constants.tag_display_single_area_public)) {
                args = new Bundle();
                fragment = new CGAPublic();
                fragment.setArguments(args);
            } else if (tag.equals(Constants.tag_display_single_area_private)) {

                // get the arguments
                Bundle arguments = fr.getArguments();

                args = new Bundle();

                fragment = new CGAPrivate();
                //Session session = test.getSession();
                Patient patient = (Patient) arguments.getSerializable(CGAAreaPrivate.PATIENT);
                //args.putSerializable(CGAPrivate.sessionObject, session);
                args.putSerializable(CGAPrivate.PATIENT, patient);
            }
            /**
             * Patient progress global -> patient profile
             */
            else if (tag.equals(Constants.tag_patient_progress)) {

                // get the arguments
                Bundle arguments = fr.getArguments();

                args = new Bundle();
                fragment = new ViewSinglePatientInfo();

                Patient patient = (Patient) arguments.getSerializable(ProgressMainFragment.PATIENT);
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);

            }
            /**
             * Patient progress detail -> Progress global.
             */
            else if (tag.equals(Constants.tag_progress_detail)) {
                args = new Bundle();
                fragment = new ProgressMainFragment();
                // get the arguments
                Bundle arguments = fr.getArguments();
                Patient patient = (Patient) arguments.getSerializable(ProgressDetail.PATIENT);
                args.putSerializable(ProgressMainFragment.PATIENT, patient);

            } else if (tag.equals(Constants.tag_view_patient_info_records)) {
                fragment = new PatientsMain();
            } else if (tag.equals(Constants.tag_view_drug_info)) {
                fragment = new DrugPrescriptionMain();
            } else if (tag.equals(Constants.tag_create_session_no_patient)) {
                Log.d("Stack", "pressed back in new session");
                ((CGAPrivate) fr).discardSession();
                return;
            } else if (tag.equals(Constants.tag_create_session_with_patient)) {
                Log.d("Stack", "pressed back in new session with patient");
                ((CGAPrivate) fr).discardSession();
                return;
            } else if (tag.equals(Constants.tag_cga_public)) {
                Log.d("Stack", "pressed back in new session (public)");
                ((CGAPublic) fr).discardSession();
                return;
            } else if (tag.equals(Constants.tag_review_session_public)) {
                Log.d("Stack", "Reviewing session (public area)");
                fragment = new CGAPublicInfo();
            } else if (tag.equals(Constants.tag_help_topic)) {
                args = new Bundle();
                fragment = new HelpTopics();
            }
            /**
             * Sessions history / review
             */
            else if (tag.equals(Constants.tag_review_session)) {
                fragment = new EvaluationsHistoryMain();
            } else if (tag.equals(Constants.tag_review_session_from_patient_profile)) {

                // get the arguments
                Bundle arguments = fr.getArguments();
                Session session = (Session) arguments.getSerializable(ReviewSingleSessionWithPatient.SESSION);
                Patient patient = session.getPatient();
                args = new Bundle();
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                fragment = new ViewSinglePatientInfo();
                fragment.setArguments(args);
            } else if (tag.equals(Constants.tag_review_test)) {
                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricScale test = (GeriatricScale) arguments.getSerializable(ReviewSingleScaleFragment.testDBobject);
                Session session = test.getSession();

                args = new Bundle();
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                fragment = new ReviewSingleSessionWithPatient();
                if (session.getPatient() == null) {
                    fragment = new ReviewSingleSessionNoPatient();
                }
            } else if (tag.equals(Constants.tag_create_patient)) {
                fragment = new PatientsMain();
            }

            if (fragment.getArguments() == null) {
                fragment.setArguments(args);
            }
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
            fragmentManager.beginTransaction()
                    .remove(currentFragment)
                    .replace(R.id.current_fragment, fragment)
                    .commit();
        } else {
            /**
             * Empty stack - ask if user really wishes to close the app
             */

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(context.getResources().getString(R.string.app_close));
            alertDialog.setMessage(context.getResources().getString(R.string.app_close_question));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            context.finish();
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

    @Override
    public void onBackStackChanged() {
        int stackSize = fragmentManager.getBackStackEntryCount();
        if (stackSize == 0) {
            ToolbarHelper.hideBackButton(context);
        } else {
            ToolbarHelper.showBackButton(context);

            // display back stack
            Log.d("Stack", "Size: " + stackSize);
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(i);
                String tag = backEntry.getName();
                Log.d("Stack", i + "-" + tag);
            }


        }
    }

    /**
     * Discard a session
     *
     * @param patient Session's patient or null if there is no Patient
     */
    public static void discardSession(Patient patient) {
        Log.d("Session", "Discarding session for patient " + patient);
        fragmentManager.popBackStack();
        Bundle args = new Bundle();
//        Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);

        Constants.discard_session = false;
        SharedPreferencesHelper.resetPrivateSession(context, "");

        Fragment fragment;
        if (patient != null) {
            /**
             * Session with patient.
             */
            fragment = new ViewSinglePatientInfo();
            args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
            fragment.setArguments(args);

        } else {
            /**
             * Session with no patient.
             */
            fragment = new PatientsMain();
        }

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
        fragmentManager.beginTransaction()
                .remove(currentFragment)
                .replace(R.id.current_fragment, fragment)
                .commit();
    }


    /**
     * Bo back to previous screen after action is performed.
     */

    public static void goToPreviousScreen() {
        Fragment fragment = null;

        // get fragment on top of stack
        FragmentManager fragmentManager = context.getFragmentManager();

        //String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        //Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

        Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);
        // current Fragment
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(index);
        String tagCurrent = backEntryCurrent.getName();
        Log.d("Stack", "Current tag:" + tagCurrent);

        if (tagCurrent.equals(Constants.tag_create_session_no_patient)) {
            fragmentManager.popBackStack();
            fragment = new PatientsMain();

        } else if (tagCurrent.equals(Constants.tag_create_session_with_patient) ||
                tagCurrent.equals(Constants.tag_display_single_area_private)) {
            FragmentManager.BackStackEntry backEntryPrevious = fragmentManager.getBackStackEntryAt(index - 1);

            String tagPrevious = backEntryPrevious.getName();
            Log.d("Stack", "Previous tag:" + tagPrevious);
            if (tagPrevious.equals(Constants.tag_view_patient_info_records)) {


                fragmentManager.popBackStack();
                Bundle args = new Bundle();
                Bundle arguments = fr.getArguments();

                // session is created -> go back to the Patient session view
                sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
                String sessionID = sharedPreferences.getString(context.getString(R.string.saved_session_private), null);
                Session session = Session.getSessionByID(sessionID);
                sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();

                args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                fragment = new ReviewSingleSessionWithPatient();

                fragment.setArguments(args);
            }

        } else if (tagCurrent.equals(Constants.tag_create_patient)) {
            fragmentManager.popBackStack();
            fragment = new PatientsMain();

        }
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
        fragmentManager.beginTransaction()
                .remove(currentFragment)
                .replace(R.id.current_fragment, fragment)
                .commit();
    }

    public static void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
