package com.example.rafael.appprototype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.Patients.ViewPatientsTab.ViewPatientsFragment;

/**
 * Created by rafael on 09-10-2016.
 */
public class HandleStack implements FragmentManager.OnBackStackChangedListener {

    FragmentManager fragmentManager;

    /**
     * Constructor for the HandleStack class
     *
     * @param fragmentManager
     */
    public HandleStack(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    public static void handleBackButton(FragmentManager fragmentManager) {
        Log.d("Backstack", "onBackPressed");
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
            Fragment fr = fragmentManager.findFragmentById(R.id.content_frame);
            int index = fragmentManager.getBackStackEntryCount() - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Log.d("Backstack", "Frag patientName is " + fragmentName + ", tag is " + tag);
            fragmentManager.popBackStack();
            if (tag.equals(Constants.tag_display_session_test)) {
                // get the arguments
                Bundle arguments = fr.getArguments();
                Session session = (Session) arguments.getSerializable(DisplaySingleTestFragment.sessionID);
                Log.d("Session","HandleStack -> " + session.toString());
                Patient patient = (Patient) arguments.getSerializable(DisplaySingleTestFragment.patient);

                Bundle args = new Bundle();
                args.putSerializable(NewEvaluation.sessionObject, session);
                args.putSerializable(NewEvaluation.PATIENT, patient);
                Fragment fragment = new NewEvaluation();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            } else if (tag.equals(Constants.tag_view_patien_info_records)) {
                Fragment fragment = new PatientsMain();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }else if (tag.equals(Constants.tag_view_sessions_history)) {
                Fragment fragment = new ViewPatientsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        if (fragmentManager.getBackStackEntryCount() == 0)
            return;
        String fragmentName = fragmentManager.getBackStackEntryAt(0).getName();
        int index = fragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Log.d("BackStack", "BackStack changed, Top fragment tag is now " + tag);
    }
}
