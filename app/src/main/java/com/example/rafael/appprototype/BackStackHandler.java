package com.example.rafael.appprototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSession;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewSingleTestFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.Patients.ViewPatients.ViewPatientsFragment;

/**
 * Created by rafael on 09-10-2016.
 */
public class BackStackHandler implements FragmentManager.OnBackStackChangedListener {

    private static Activity context = null;
    FragmentManager fragmentManager;

    /**
     * Constructor for the BackStackHandler class
     *
     * @param fragmentManager
     * @param mainActivity
     */
    public BackStackHandler(FragmentManager fragmentManager, Activity mainActivity) {
        this.fragmentManager = fragmentManager;
        this.context = mainActivity;
    }

    public static void handleBackButton(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // get fragment on top of stack
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);

            //String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
            Fragment fr = fragmentManager.findFragmentById(R.id.content_fragment);
            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Log.d("Stack", tag);
            if (tag.equals(Constants.tag_display_session_test)) {
                fragmentManager.popBackStack();

                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricTest test = (GeriatricTest) arguments.getSerializable(DisplaySingleTestFragment.testDBobject);
                Session session = test.getSession();
                Patient patient = (Patient) arguments.getSerializable(DisplaySingleTestFragment.patient);

                Bundle args = new Bundle();
                args.putSerializable(NewEvaluation.sessionObject, session);
                args.putSerializable(NewEvaluation.PATIENT, patient);
                Fragment fragment = new NewEvaluation();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_view_patien_info_records)) {
                fragmentManager.popBackStack();
                Fragment fragment = new PatientsMain();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_view_sessions_history)) {
                fragmentManager.popBackStack();

                Fragment fragment = new ViewPatientsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_view_drug_info)) {
                fragmentManager.popBackStack();

                Fragment fragment = new DrugPrescriptionMain();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_create_session)) {
                fragmentManager.popBackStack();
                Log.d("Stack", "pressed back in new session");
                ((NewEvaluation) currentFragment).discardFAB.performClick();
            } else if (tag.equals(Constants.tag_create_new_session_for_patient)) {
                Log.d("Stack", "pressed back in new session with patient");
                ((NewEvaluation) currentFragment).discardFAB.performClick();

            }
            /**
             * Sessions history / review
             */
            else if (tag.equals(Constants.tag_review_session)) {
                fragmentManager.popBackStack();
                Fragment fragment = new PatientsMain();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_review_session_from_patient_profile)) {
                // go back to the patient's profile
                fragmentManager.popBackStack();
                // get the arguments
                Bundle arguments = fr.getArguments();
                Session session = (Session) arguments.getSerializable(ReviewSingleSession.SESSION);
                Patient patient = session.getPatient();
                Bundle args = new Bundle();
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                Fragment fragment = new ViewSinglePatientInfo();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_review_test)) {
                fragmentManager.popBackStack();
                // get the arguments
                Bundle arguments = fr.getArguments();
                GeriatricTest test = (GeriatricTest) arguments.getSerializable(ReviewSingleTestFragment.testDBobject);
                Session session = test.getSession();

                Bundle args = new Bundle();
                args.putSerializable(ReviewSingleSession.SESSION, session);
                Fragment fragment = new ReviewSingleSession();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            }
        } else {
            // empty stack
            //system.out.println("Empty stack");
            context.finish();
        }
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return;
        }
        // context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Log.d("BackStack", "BackStack changed, Top fragment tag is now " + tag);
    }

    /**
     * Bo back to previous screen after action is performed.
     */
    public static void goToPreviousScreen() {
        // get fragment on top of stack
        FragmentManager fragmentManager = context.getFragmentManager();

        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        //Log.d("Stack","Current fragment:" + currentFragment.getArguments().size());

        //String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
        Fragment fr = fragmentManager.findFragmentById(R.id.content_fragment);
        // current Fragment
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(index);
        String tagCurrent = backEntryCurrent.getName();
        Log.d("Stack", "Current tag:" + tagCurrent);

        if (tagCurrent.equals(Constants.tag_create_session)) {
            fragmentManager.popBackStack();
            Fragment fragment = new PatientsMain();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, fragment)
                    .commit();
        } else if (tagCurrent.equals(Constants.tag_create_new_session_for_patient)) {
            FragmentManager.BackStackEntry backEntryPrevious = fragmentManager.getBackStackEntryAt(index - 1);

            String tagPrevious = backEntryPrevious.getName();
            Log.d("Stack", "Previous tag:" + tagPrevious);
            if (tagPrevious.equals(Constants.tag_view_patien_info_records)) {
                // session is created/canceled -> go back to the Patient view
                fragmentManager.popBackStack();
                Bundle arguments = fr.getArguments();
                Patient patient = (Patient) arguments.getSerializable(NewEvaluation.PATIENT);

                Bundle args = new Bundle();
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                Fragment fragment = new ViewSinglePatientInfo();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_fragment, fragment)
                        .commit();
            }

        } else if (tagCurrent.equals(Constants.tag_create_patient)) {
            fragmentManager.popBackStack();
            Fragment fragment = new PatientsMain();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, fragment)
                    .commit();
        }


    }
}
