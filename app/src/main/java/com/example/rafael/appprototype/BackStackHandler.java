package com.example.rafael.appprototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPrivate;
import com.example.rafael.appprototype.Help_Feedback.HelpTopics;
import com.example.rafael.appprototype.Patients.PatientProgress.ProgressDetail;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublic;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPublic;
import com.example.rafael.appprototype.Patients.PatientProgress.ProgressMainFragment;
import com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleSession;
import com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest.ReviewSingleTestFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Evaluations.DisplayTest.ScaleFragment;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.Patients.ViewPatients.ViewPatientsFragment;

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
            Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);


            Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);
            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();

            Log.d("Stack", tag);
            /**
             * Only pop backstack if changing fragments/screens
             */
            if (!tag.equals(Constants.tag_create_session) &&
                    !tag.equals(Constants.tag_create_new_session_for_patient) &&
                    !tag.equals(Constants.tag_cga_public)) {
                fragmentManager.popBackStack();
            }

            /**
             * Viewing a scale.
             */
            if (tag.equals(Constants.tag_display_session_scale)) {

                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricScale test = (GeriatricScale) arguments.getSerializable(ScaleFragment.testDBobject);
                assert test != null;
                test.setAlreadyOpened(true);
                test.save();
                Session session = test.getSession();
                Patient patient = (Patient) arguments.getSerializable(ScaleFragment.patient);

                boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);

                if (alreadyLogged) {
                    args = new Bundle();
                    args.putSerializable(CGAAreaPrivate.sessionObject, session);
                    args.putSerializable(CGAAreaPrivate.PATIENT, patient);
                    args.putSerializable(CGAAreaPrivate.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));
                    fragment = new CGAAreaPrivate();
                } else {
                    args = new Bundle();
                    args.putSerializable(CGAAreaPublic.sessionObject, session);
                    args.putSerializable(CGAAreaPublic.PATIENT, patient);
                    args.putSerializable(CGAAreaPublic.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));

                    fragment = new CGAAreaPublic();
                }
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

            } else if (tag.equals(Constants.tag_view_patien_info_records)) {
                fragment = new PatientsMain();
            } else if (tag.equals(Constants.tag_view_sessions_history)) {
                fragment = new ViewPatientsFragment();
            } else if (tag.equals(Constants.tag_view_drug_info)) {
                fragment = new DrugPrescriptionMain();
            } else if (tag.equals(Constants.tag_create_session)) {
                Log.d("Stack", "pressed back in new session");
                ((CGAPrivate) currentFragment).discardFAB.performClick();
                return;
            } else if (tag.equals(Constants.tag_create_new_session_for_patient)) {
                Log.d("Stack", "pressed back in new session with patient");
                ((CGAPrivate) currentFragment).discardFAB.performClick();
                return;
            } else if (tag.equals(Constants.tag_cga_public)) {
                Log.d("Stack", "pressed back in new session (public)");
                ((CGAPublic) currentFragment).resetFAB.performClick();
                return;
            } else if (tag.equals(Constants.tag_help_topic)) {
                args = new Bundle();
                fragment = new HelpTopics();
            }
            /**
             * Sessions history / review
             */
            else if (tag.equals(Constants.tag_review_session)) {
                fragment = new PatientsMain();
            } else if (tag.equals(Constants.tag_review_session_from_patient_profile)) {

                // get the arguments
                Bundle arguments = fr.getArguments();
                Session session = (Session) arguments.getSerializable(ReviewSingleSession.SESSION);
                Patient patient = session.getPatient();
                args = new Bundle();
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                fragment = new ViewSinglePatientInfo();
                fragment.setArguments(args);
            } else if (tag.equals(Constants.tag_review_test)) {
                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricScale test = (GeriatricScale) arguments.getSerializable(ReviewSingleTestFragment.testDBobject);
                Session session = test.getSession();

                args = new Bundle();
                args.putSerializable(ReviewSingleSession.SESSION, session);
                fragment = new ReviewSingleSession();
            }

            fragment.setArguments(args);
            currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
            fragmentManager.beginTransaction()
                    .remove(currentFragment)
                    .replace(R.id.current_fragment, fragment)
                    .commit();
        } else {
            // empty stack
            System.out.println("Empty stack");
            context.finish();
        }
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            ToolbarHelper.hideBackButton(context);
        } else {
            ToolbarHelper.showBackButton(context);
            String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Log.d("BackStack", "BackStack changed, Top fragment tag is now " + tag);
        }
    }

    public static void discardSession(Patient patient) {
        System.out.println(patient);
        fragmentManager.popBackStack();
        Bundle args = new Bundle();
        Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);

        // session is discarded -> go back to the Patient's profile
        Constants.discard_session = false;
        // session is created -> go back to the Patient session view
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();

        args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
        Fragment fragment = new ViewSinglePatientInfo();
        fragment.setArguments(args);

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

        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

        Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);
        // current Fragment
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(index);
        String tagCurrent = backEntryCurrent.getName();
        Log.d("Stack", "Current tag:" + tagCurrent);

        if (tagCurrent.equals(Constants.tag_create_session)) {
            fragmentManager.popBackStack();
            fragment = new PatientsMain();

        } else if (tagCurrent.equals(Constants.tag_create_new_session_for_patient) ||
                tagCurrent.equals(Constants.tag_display_single_area_private)) {
            FragmentManager.BackStackEntry backEntryPrevious = fragmentManager.getBackStackEntryAt(index - 1);

            String tagPrevious = backEntryPrevious.getName();
            Log.d("Stack", "Previous tag:" + tagPrevious);
            if (tagPrevious.equals(Constants.tag_view_patien_info_records)) {


                fragmentManager.popBackStack();
                Bundle args = new Bundle();
                Bundle arguments = fr.getArguments();

                // session is created -> go back to the Patient session view
                sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
                String sessionID = sharedPreferences.getString(context.getString(R.string.saved_session_private), null);
                Session session = Session.getSessionByID(sessionID);
                sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();

                args.putBoolean(ReviewSingleSession.COMPARE_PREVIOUS, true);
                args.putSerializable(ReviewSingleSession.SESSION, session);
                fragment = new ReviewSingleSession();

                fragment.setArguments(args);
            }

        } else if (tagCurrent.equals(Constants.tag_create_patient)) {
            fragmentManager.popBackStack();
            fragment = new PatientsMain();

        }
        currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
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
