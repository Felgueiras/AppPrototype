package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.CGAGuide.CGAGuideMain;
import com.felgueiras.apps.geriatric_helper.CGAGuide.CGAGuideAreaFragment;
import com.felgueiras.apps.geriatric_helper.CGAGuide.CGAGuideScale;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas.CGAPublic;
import com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas.CGAPublicInfo;
import com.felgueiras.apps.geriatric_helper.Evaluations.DisplayTest.ScaleFragment;
import com.felgueiras.apps.geriatric_helper.Evaluations.EvaluationsHistoryMain;
import com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.felgueiras.apps.geriatric_helper.Evaluations.SingleArea.CGAAreaPrivate;
import com.felgueiras.apps.geriatric_helper.Evaluations.SingleArea.CGAAreaPublic;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.Help_Feedback.HelpMain;
import com.felgueiras.apps.geriatric_helper.Main.PrivateAreaMainFragment;
import com.felgueiras.apps.geriatric_helper.Prescription.PrescriptionMain;
import com.felgueiras.apps.geriatric_helper.Patients.Progress.ProgressFragment;
import com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewScaleFragment;
import com.felgueiras.apps.geriatric_helper.Patients.PatientsMain;
import com.felgueiras.apps.geriatric_helper.Patients.SinglePatient.PatientProfileFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rafael on 09-10-2016.
 */
public class BackStackHandler implements FragmentManager.OnBackStackChangedListener, Serializable {

    private static Activity context = null;
    static FragmentManager fragmentManager;
    private static SharedPreferences sharedPreferences;

    public static FragmentManager getFragmentManager() {
        return fragmentManager;
    }

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
        Fragment nextFragment = null;
        Bundle args = null;
        Fragment fr = fragmentManager.findFragmentById(R.id.current_fragment);

        if (fragmentManager.getBackStackEntryCount() > 0) {


            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();

            Log.d("Stack", "handleBackButton (tag):" + tag);
            /**
             * Only pop backstack if changing fragments/screens.
             */
            if (!tag.equals(Constants.tag_create_session_no_patient) &&
                    !tag.equals(Constants.tag_create_session_with_patient) &&
                    !tag.equals(Constants.tag_cga_public)
                    && !tag.equals(Constants.tag_display_session_scale)) {
                fragmentManager.popBackStack();
            }
//            return;

            /**
             * Viewing a scale.
             */
            switch (tag) {
                case Constants.tag_display_session_scale: {

                    if (!((ScaleFragment) fr).checkComplete()) {
                        return;
                    }
                    fragmentManager.popBackStack();


                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    GeriatricScaleFirebase scale = (GeriatricScaleFirebase) arguments.getSerializable(ScaleFragment.SCALE);
                    assert scale != null;
                    scale.setAlreadyOpened(true);

                    FirebaseHelper.updateScale(scale);
                    SessionFirebase session = FirebaseHelper.getSessionFromScale(scale);
                    PatientFirebase patient = (PatientFirebase) arguments.getSerializable(ScaleFragment.patient);

                    if (SharedPreferencesHelper.isLoggedIn(context)) {
                        args = new Bundle();
                        args.putSerializable(CGAAreaPrivate.SESSION, session);
                        args.putSerializable(CGAAreaPrivate.PATIENT, patient);
                        args.putSerializable(CGAAreaPrivate.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));
                        nextFragment = new CGAAreaPrivate();
                    } else {
                        args = new Bundle();
                        args.putSerializable(CGAAreaPublic.sessionObject, session);
                        args.putSerializable(CGAAreaPublic.PATIENT, patient);
                        args.putSerializable(CGAAreaPublic.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));

                        nextFragment = new CGAAreaPublic();
                    }
                    break;
                }
                case Constants.tag_display_session_scale_shortcut: {
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    /**
                     * bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(scaleName));
                     bundle.putSerializable(ScaleFragment.SCALE, currentScaleDB);
                     bundle.putSerializable(ScaleFragment.CGA_AREA, currentScaleDB.getArea());
                     bundle.putSerializable(ScaleFragment.PATIENT, session.getDrugName());
                     */
                    GeriatricScaleFirebase scale = (GeriatricScaleFirebase) arguments.getSerializable(ScaleFragment.SCALE);
                    assert scale != null;
                    scale.setAlreadyOpened(true);
                    FirebaseHelper.updateScale(scale);

                    SessionFirebase session = FirebaseHelper.getSessionFromScale(scale);
                    PatientFirebase patient = (PatientFirebase) arguments.getSerializable(ScaleFragment.patient);

                    if (SharedPreferencesHelper.isLoggedIn(context)) {
                        args = new Bundle();
                        args.putSerializable(CGAPrivate.PATIENT, patient);
                        nextFragment = new CGAPrivate();
                    } else {
                        args = new Bundle();
//                    args.putSerializable(CGAAreaPublicBottomButtons.SCALE, session);
//                    args.putSerializable(CGAAreaPublicBottomButtons.PATIENT, PATIENT);
//                    args.putSerializable(CGAAreaPublicBottomButtons.CGA_AREA, arguments.getSerializable(ScaleFragment.CGA_AREA));

                        nextFragment = new CGAPublic();
                    }
                    break;
                }
                /**
                 *
                 * Viewing single area (public).
                 */
                case Constants.tag_display_single_area_public:
                    args = new Bundle();
                    nextFragment = new CGAPublic();
                    nextFragment.setArguments(args);
                    break;
                case Constants.tag_display_single_area_private: {

                    // get the arguments
                    Bundle arguments = fr.getArguments();

                    args = new Bundle();

                    nextFragment = new CGAPrivate();
                    //Session session = test.getSessionID();
                    PatientFirebase patient = (PatientFirebase) arguments.getSerializable(CGAAreaPrivate.PATIENT);
                    //args.putSerializable(CGAPrivateBottomButtons.SCALE, session);
                    args.putSerializable(CGAPrivate.PATIENT, patient);
                    break;
                }
                /**
                 * Patient progress global -> PATIENT profile
                 */
                case Constants.tag_patient_progress: {

                    // get the arguments
                    Bundle arguments = fr.getArguments();

                    args = new Bundle();
                    nextFragment = new PatientProfileFragment();

                    PatientFirebase patient = (PatientFirebase) arguments.getSerializable(ProgressFragment.PATIENT);
                    args.putSerializable(PatientProfileFragment.PATIENT, patient);

                    break;
                }
                /**
                 * Patient progress detail -> Progress global.
                 */
                case Constants.tag_review_scale_from_progress: {
                    args = new Bundle();

                    nextFragment = new ProgressFragment();
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    PatientFirebase patient = (PatientFirebase) arguments.getSerializable(ReviewScaleFragment.PATIENT);
                    args.putSerializable(ProgressFragment.PATIENT, patient);
                    break;
                }
                case Constants.tag_home_page_selected_page:
                    nextFragment = new PrivateAreaMainFragment();
                    break;
                case Constants.tag_view_patient_info_records:
                    nextFragment = new PatientsMain();
                    break;
                case Constants.tag_view_patient_info_records_from_sessions_list:
                    nextFragment = new EvaluationsHistoryMain();
                    break;
                case Constants.tag_view_drug_info:
                    nextFragment = new PrescriptionMain();
                    break;
                case Constants.tag_create_session_pick_patient_start:
                    // go back to sessions list
                    nextFragment = new EvaluationsHistoryMain();
                    break;
                case Constants.tag_create_session_no_patient:
                    Log.d("Stack", "pressed back in new session");
                    ((CGAPrivate) fr).discardSession();
                    return;
                case Constants.tag_create_session_with_patient:
                    Log.d("Stack", "pressed back in new session with PATIENT");
                    ((CGAPrivate) fr).discardSession();
                    return;
                case Constants.tag_cga_public:
                    Log.d("Stack", "pressed back in new session (public)");
                    ((CGAPublic) fr).finishSession();
                    return;
                case Constants.tag_review_session_public:
                    Log.d("Stack", "Reviewing session (public area)");
                    nextFragment = new CGAPublicInfo();
                    break;
                case Constants.tag_help_topic:
                    args = new Bundle();
                    nextFragment = new HelpMain();
                    break;
                /**
                 * Sessions history / review
                 */
                case Constants.tag_review_session_from_sessions_list:
                    SharedPreferencesHelper.resetPrivateSession(context, "");
                    nextFragment = new EvaluationsHistoryMain();
                    break;
                case Constants.tag_review_session_from_patient_profile: {

                    SharedPreferencesHelper.resetPrivateSession(context, "");

                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    SessionFirebase session = (SessionFirebase) arguments.getSerializable(ReviewSingleSessionWithPatient.SESSION);
                    PatientFirebase patient =  FirebaseHelper.getPatientFromSession(session);
                    args = new Bundle();
                    args.putSerializable(PatientProfileFragment.PATIENT, patient);
                    nextFragment = new PatientProfileFragment();
                    nextFragment.setArguments(args);
                    break;
                }
                case Constants.tag_review_test: {
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    GeriatricScaleFirebase test = (GeriatricScaleFirebase) arguments.getSerializable(ReviewScaleFragment.SCALE);
                    SessionFirebase session = FirebaseHelper.getSessionFromScale(test);

                    args = new Bundle();
                    args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                    nextFragment = new ReviewSingleSessionWithPatient();
                    if (FirebaseHelper.getPatientFromSession(session) == null) {
                        nextFragment = new ReviewSingleSessionNoPatient();
                    }
                    break;
                }
                case Constants.tag_create_patient:
                    nextFragment = new PatientsMain();
                    break;
                /**
                 * CGA Guide.
                 */
                case Constants.tag_guide_area:
                    nextFragment = new CGAGuideMain();
                    break;
                case Constants.more_info_clicked:
                    nextFragment = new CGAPublicInfo();
                    break;
                case Constants.tag_guide_scale:
                    Bundle arguments = fr.getArguments();
                    GeriatricScaleNonDB scale = (GeriatricScaleNonDB) arguments.getSerializable(CGAGuideScale.SCALE);
                    nextFragment = new CGAGuideAreaFragment();
                    // add arguments
                    Bundle bundle = new Bundle();
                    bundle.putString(CGAGuideAreaFragment.CGA_AREA, scale.getArea());
                    nextFragment.setArguments(bundle);
                    break;
            }

            if (nextFragment.isAdded()) {
                Log.d("Stack", "already added");
                return; //or return false/true, based on where you are calling from
            }

            if (nextFragment.getArguments() == null) {
                nextFragment.setArguments(args);
            }
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
            if (currentFragment != null) {
                System.out.println(currentFragment);
            }
//            fragmentManager.beginTransaction()
//                    .remove(currentFragment)
//                    .replace(R.id.current_fragment, nextFragment)
//                    .commit();
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
     * @param session
     */
    public static void discardSession(SessionFirebase session) {
        // check if initial tag is create session no PATIENT
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(index);
        String tagCurrent = backEntryCurrent.getName();

        Log.d("Stack", "Discarding session tag:" + tagCurrent);

        fragmentManager.popBackStack();
        Bundle args = new Bundle();

        Constants.discard_session = false;
        SharedPreferencesHelper.resetPrivateSession(context, "");

        // erase from firebase
        FirebaseHelper.deleteSession(session);


        Fragment fragment = null;
        switch (tagCurrent) {
//            case Constants.tag_create_session_with_patient:
//            case Constants.tag_display_single_area_private:
//            case Constants.tag_display_session_scale:
//                // created session with PATIENT
//                fragment = new PatientProfileFragment();
//                args.putSerializable(PatientProfileFragment.PATIENT, patient);
//                fragment.setArguments(args);
//                break;
            case Constants.tag_create_session_no_patient:
            case Constants.tag_create_session_with_patient_from_session:
                // created a Session with no PATIENT -> go back to sessions screen
                fragment = new EvaluationsHistoryMain();
                break;
            case Constants.tag_create_session_from_favorites:
                fragment = new PatientsMain();

                break;
        }

//        Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//        fragmentManager.beginTransaction()
//                .remove(currentFragment)
//                .replace(R.id.current_fragment, fragment)
//                .commit();
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

        } else if (tagCurrent.equals(Constants.tag_create_session_with_patient)) {
            FragmentManager.BackStackEntry backEntryPrevious = fragmentManager.getBackStackEntryAt(index - 1);

            String tagPrevious = backEntryPrevious.getName();
            Log.d("Stack", "Previous tag:" + tagPrevious);
            if (tagPrevious.equals(Constants.tag_view_patient_info_records)) {
                /**
                 * Session saved when viewing a scale - go to PATIENT's profile.
                 */
                // lock session creation
                SharedPreferencesHelper.lockSessionCreation(context);
                fragmentManager.popBackStack();

                Bundle args = new Bundle();
                SessionFirebase session = FirebaseHelper.getSessionByID(SharedPreferencesHelper.isThereOngoingPrivateSession(context));
                SharedPreferencesHelper.resetPrivateSession(context, "");

                args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                fragment = new ReviewSingleSessionWithPatient();

                fragment.setArguments(args);

                // Create new transaction and add to back stack
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                transaction.remove(currentFragment);
                transaction.replace(R.id.current_fragment, fragment);
                transaction.addToBackStack(Constants.tag_review_session_from_patient_profile);
                transaction.commit();
                return;

            } else if (tagPrevious.equals(Constants.tag_view_patient_info_records_from_sessions_list)) {

                fragmentManager.popBackStack();
                Bundle args = new Bundle();
                Bundle arguments = fr.getArguments();

                // session is created -> go back to the Patient session view
                sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
                String sessionID = sharedPreferences.getString(context.getString(R.string.saved_session_private), null);
                SessionFirebase session = FirebaseHelper.getSessionByID(sessionID);
                sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();

                args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
                args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                fragment = new ReviewSingleSessionWithPatient();

                fragment.setArguments(args);
            }


        } else if (tagCurrent.equals(Constants.tag_create_session_with_patient_from_session)) {
            // TODO review session
            fragmentManager.popBackStack();
            fragment = new EvaluationsHistoryMain();
        } else if (tagCurrent.equals(Constants.tag_create_patient)) {
            fragmentManager.popBackStack();
            fragment = new PatientsMain();
        } else if (tagCurrent.equals(Constants.tag_display_single_area_private)) {
            /**
             * Session saved when viewing an area - go to PATIENT's profile.
             */
            // lock session creation
            SharedPreferencesHelper.lockSessionCreation(context);
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            Bundle args = new Bundle();
            Bundle arguments = fr.getArguments();
            SessionFirebase currentSession = (SessionFirebase) arguments.getSerializable(CGAAreaPrivate.SESSION);

            SharedPreferencesHelper.resetPrivateSession(context, "");

            args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
            args.putSerializable(ReviewSingleSessionWithPatient.SESSION, currentSession);
            fragment = new ReviewSingleSessionWithPatient();

            fragment.setArguments(args);

            // Create new transaction and add to back stack
            FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
            Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
            transaction.remove(currentFragment);
            transaction.replace(R.id.current_fragment, fragment);
            transaction.addToBackStack(Constants.tag_review_session_from_patient_profile);
            transaction.commit();
            return;
        } else if (tagCurrent.equals(Constants.tag_display_session_scale)) {
            /**
             * Session saved when viewing a scale - go to PATIENT's profile.
             */
            // lock session creation
            SharedPreferencesHelper.lockSessionCreation(context);
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();


            Bundle args = new Bundle();
            Bundle arguments = fr.getArguments();
            GeriatricScaleFirebase scale = (GeriatricScaleFirebase) arguments.getSerializable(ScaleFragment.SCALE);
            boolean containsFavorite = checkBackStackContainsTag(Constants.tag_create_session_from_favorites);
            if (containsFavorite) {
                PatientFirebase patient = FirebaseHelper.getPatientFromSession(FirebaseHelper.getSessionFromScale(scale));

                args.putSerializable(PatientProfileFragment.PATIENT, patient);
                fragment = new PatientProfileFragment();
                fragment.setArguments(args);

                // Create new transaction and add to back stack
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                transaction.remove(currentFragment);
                transaction.replace(R.id.current_fragment, fragment);
                transaction.addToBackStack(Constants.tag_view_patient_info_records);
                transaction.commit();
                return;

            }


            SharedPreferencesHelper.resetPrivateSession(context, "");

            args = new Bundle();
            args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
            args.putSerializable(ReviewSingleSessionWithPatient.SESSION, FirebaseHelper.getSessionFromScale(scale));
            fragment = new ReviewSingleSessionWithPatient();

            fragment.setArguments(args);

            // Create new transaction and add to back stack
            FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
            Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
            transaction.remove(currentFragment);
            transaction.replace(R.id.current_fragment, fragment);
            transaction.addToBackStack(Constants.tag_review_session_from_patient_profile);
            transaction.commit();
            return;
        }

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
        fragmentManager.beginTransaction()
                .remove(currentFragment)
                .replace(R.id.current_fragment, fragment)
                .commit();
    }

    private static boolean checkBackStackContainsTag(String tag) {
        FragmentManager fragmentManager = context.getFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(i);
            String tagCurrent = backEntryCurrent.getName();
            if (tagCurrent.equals(tag))
                return true;
        }
        return false;
    }

    public static void clearBackStack() {
        FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(0);
        fragmentManager.popBackStack(entry.getId(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.executePendingTransactions();

//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
//            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        }

        // remove all fragments in the
//        fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.current_fragment)).commit();
    }
}
