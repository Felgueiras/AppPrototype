package com.example.rafael.appprototype.HelpersHandlers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.rafael.appprototype.CGAGuide.CGAGuideMain;
import com.example.rafael.appprototype.CGAGuide.CGAGuideAreaFragment;
import com.example.rafael.appprototype.CGAGuide.CGAGuideScale;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublic;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.DisplayTest.ScaleFragmentBottomButtons;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPrivateBottomButtons;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPublicBottomButtons;
import com.example.rafael.appprototype.Help_Feedback.HelpMain;
import com.example.rafael.appprototype.Main.PrivateAreaMainFragment;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Patients.Progress.ProgressMain;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleSessionWithPatient;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewScaleFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rafael on 09-10-2016.
 */
public class BackStackHandler implements FragmentManager.OnBackStackChangedListener, Serializable {

    private static Activity context = null;
    static FragmentManager fragmentManager;
    private static SharedPreferences sharedPreferences;

    public FragmentManager getFragmentManager() {
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
        Fragment fragment = null;
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
                    !tag.equals(Constants.tag_cga_public) &&
                    !tag.equals(Constants.tag_display_session_scale)) {
                fragmentManager.popBackStack();
            }

            /**
             * Viewing a scale.
             */
            switch (tag) {
                case Constants.tag_display_session_scale: {

                    if (!((ScaleFragmentBottomButtons) fr).checkComplete()) {
                        return;
                    }
                    fragmentManager.popBackStack();


                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    GeriatricScale scale = (GeriatricScale) arguments.getSerializable(ScaleFragmentBottomButtons.SCALE);
                    assert scale != null;
                    scale.setAlreadyOpened(true);
                    scale.save();
                    Session session = scale.getSession();
                    Patient patient = (Patient) arguments.getSerializable(ScaleFragmentBottomButtons.patient);

                    if (SharedPreferencesHelper.isLoggedIn(context)) {
                        args = new Bundle();
                        args.putSerializable(CGAAreaPrivateBottomButtons.SESSION, session);
                        args.putSerializable(CGAAreaPrivateBottomButtons.PATIENT, patient);
                        args.putSerializable(CGAAreaPrivateBottomButtons.CGA_AREA, arguments.getSerializable(ScaleFragmentBottomButtons.CGA_AREA));
                        fragment = new CGAAreaPrivateBottomButtons();
                    } else {
                        args = new Bundle();
                        args.putSerializable(CGAAreaPublicBottomButtons.sessionObject, session);
                        args.putSerializable(CGAAreaPublicBottomButtons.PATIENT, patient);
                        args.putSerializable(CGAAreaPublicBottomButtons.CGA_AREA, arguments.getSerializable(ScaleFragmentBottomButtons.CGA_AREA));

                        fragment = new CGAAreaPublicBottomButtons();
                    }
                    break;
                }
                case Constants.tag_display_session_scale_shortcut: {
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    /**
                     * bundle.putSerializable(ScaleFragmentBottomButtons.testObject, Scales.getScaleByName(scaleName));
                     bundle.putSerializable(ScaleFragmentBottomButtons.SCALE, currentScaleDB);
                     bundle.putSerializable(ScaleFragmentBottomButtons.CGA_AREA, currentScaleDB.getArea());
                     bundle.putSerializable(ScaleFragmentBottomButtons.PATIENT, session.getDrugName());
                     */
                    GeriatricScale scale = (GeriatricScale) arguments.getSerializable(ScaleFragmentBottomButtons.SCALE);
                    assert scale != null;
                    scale.setAlreadyOpened(true);
                    scale.save();
                    Session session = scale.getSession();
                    Patient patient = (Patient) arguments.getSerializable(ScaleFragmentBottomButtons.patient);

                    if (SharedPreferencesHelper.isLoggedIn(context)) {
                        args = new Bundle();
                        args.putSerializable(CGAPrivate.PATIENT, patient);
                        fragment = new CGAPrivate();
                    } else {
                        args = new Bundle();
//                    args.putSerializable(CGAAreaPublicBottomButtons.SCALE, session);
//                    args.putSerializable(CGAAreaPublicBottomButtons.PATIENT, PATIENT);
//                    args.putSerializable(CGAAreaPublicBottomButtons.CGA_AREA, arguments.getSerializable(ScaleFragmentBottomButtons.CGA_AREA));

                        fragment = new CGAPublic();
                    }
                    break;
                }
                /**
                 *
                 * Viewing single area (public).
                 */
                case Constants.tag_display_single_area_public:
                    args = new Bundle();
                    fragment = new CGAPublic();
                    fragment.setArguments(args);
                    break;
                case Constants.tag_display_single_area_private: {

                    // get the arguments
                    Bundle arguments = fr.getArguments();

                    args = new Bundle();

                    fragment = new CGAPrivate();
                    //Session session = test.getSession();
                    Patient patient = (Patient) arguments.getSerializable(CGAAreaPrivateBottomButtons.PATIENT);
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
                    fragment = new ViewSinglePatientInfo();

                    Patient patient = (Patient) arguments.getSerializable(ProgressMain.PATIENT);
                    args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);

                    break;
                }
                /**
                 * Patient progress detail -> Progress global.
                 */
                case Constants.tag_review_scale_from_progress: {
                    args = new Bundle();
                    fragment = new ProgressMain();
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    Patient patient = (Patient) arguments.getSerializable(ReviewScaleFragment.PATIENT);
                    args.putSerializable(ProgressMain.PATIENT, patient);
                    break;
                }
                case Constants.tag_home_page_selected_page:
                    fragment = new PrivateAreaMainFragment();
                    break;
                case Constants.tag_view_patient_info_records:
                    fragment = new PatientsMain();
                    break;
                case Constants.tag_view_patient_info_records_from_sessions_list:
                    fragment = new EvaluationsHistoryMain();
                    break;
                case Constants.tag_view_drug_info:
                    fragment = new DrugPrescriptionMain();
                    break;
                case Constants.tag_create_session_pick_patient_start:
                    // go back to sessions list
                    fragment = new EvaluationsHistoryMain();
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
                    fragment = new CGAPublicInfo();
                    break;
                case Constants.tag_help_topic:
                    args = new Bundle();
                    fragment = new HelpMain();
                    break;
                /**
                 * Sessions history / review
                 */
                case Constants.tag_review_session_from_sessions_list:
                    SharedPreferencesHelper.resetPrivateSession(context, "");
                    fragment = new EvaluationsHistoryMain();
                    break;
                case Constants.tag_review_session_from_patient_profile: {

                    SharedPreferencesHelper.resetPrivateSession(context, "");

                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    Session session = (Session) arguments.getSerializable(ReviewSingleSessionWithPatient.SESSION);
                    Patient patient = session.getPatient();
                    args = new Bundle();
                    args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                    fragment = new ViewSinglePatientInfo();
                    fragment.setArguments(args);
                    break;
                }
                case Constants.tag_review_test: {
                    // get the arguments
                    Bundle arguments = fr.getArguments();
                    GeriatricScale test = (GeriatricScale) arguments.getSerializable(ReviewScaleFragment.SCALE);
                    Session session = test.getSession();

                    args = new Bundle();
                    args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                    fragment = new ReviewSingleSessionWithPatient();
                    if (session.getPatient() == null) {
                        fragment = new ReviewSingleSessionNoPatient();
                    }
                    break;
                }
                case Constants.tag_create_patient:
                    fragment = new PatientsMain();
                    break;
                /**
                 * CGA Guide.
                 */
                case Constants.tag_guide_area:
                    fragment = new CGAGuideMain();
                    break;
                case Constants.more_info_clicked:
                    fragment = new CGAPublicInfo();
                    break;
                case Constants.tag_guide_scale:
                    Bundle arguments = fr.getArguments();
                    GeriatricScaleNonDB scale = (GeriatricScaleNonDB) arguments.getSerializable(CGAGuideScale.SCALE);
                     fragment = new CGAGuideAreaFragment();
                    // add arguments
                    Bundle bundle = new Bundle();
                    bundle.putString(CGAGuideAreaFragment.CGA_AREA, scale.getArea());
                    fragment.setArguments(bundle);
                    break;
            }

            if (fragment.isAdded()) {
                Log.d("Stack", "already added");
                return; //or return false/true, based on where you are calling from
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
     * @param patient Session's PATIENT or null if there is no Patient
     */
    public static void discardSession(Patient patient) {
        Log.d("Session", "Discarding session for PATIENT " + patient);
        // check if initial tag is create session no PATIENT
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntryCurrent = fragmentManager.getBackStackEntryAt(index);
        String tagCurrent = backEntryCurrent.getName();

        Log.d("Stack", "Discarding session tag:" + tagCurrent);

        fragmentManager.popBackStack();
        Bundle args = new Bundle();

        Constants.discard_session = false;
        SharedPreferencesHelper.resetPrivateSession(context, "");

        Fragment fragment = null;
        switch (tagCurrent) {
            case Constants.tag_create_session_with_patient:
            case Constants.tag_display_single_area_private:
            case Constants.tag_display_session_scale:
                // created session with PATIENT
                fragment = new ViewSinglePatientInfo();
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                fragment.setArguments(args);
                break;
            case Constants.tag_create_session_no_patient:
            case Constants.tag_create_session_with_patient_from_session:
                // created a Session with no PATIENT -> go back to sessions screen
                fragment = new EvaluationsHistoryMain();
                break;
            case Constants.tag_create_session_from_favorites:
                fragment = new PatientsMain();

                break;
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
                Session session = Session.getSessionByID(SharedPreferencesHelper.isThereOngoingPrivateSession(context));
                SharedPreferencesHelper.resetPrivateSession(context, "");

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
                Session session = Session.getSessionByID(sessionID);
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
            Session currentSession = (Session) arguments.getSerializable(CGAAreaPrivateBottomButtons.SESSION);

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
            GeriatricScale scale = (GeriatricScale) arguments.getSerializable(ScaleFragmentBottomButtons.SCALE);
            Patient patient = scale.getSession().getPatient();
            boolean containsFavorite = checkBackStackContainsTag(Constants.tag_create_session_from_favorites);
            if (containsFavorite) {


                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                fragment = new ViewSinglePatientInfo();
                fragment.setArguments(args);

                // Create new transaction and add to back stack
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                Fragment currentFragment = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                transaction.remove(currentFragment);
                transaction.replace(R.id.current_fragment, fragment);
                transaction.addToBackStack(Constants.tag_view_patient_info_records);
                transaction.commit();


            }


            SharedPreferencesHelper.resetPrivateSession(context, "");

            args = new Bundle();
            args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
            args.putSerializable(ReviewSingleSessionWithPatient.SESSION, scale.getSession());
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
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
