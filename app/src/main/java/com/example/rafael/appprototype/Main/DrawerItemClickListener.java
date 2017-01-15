package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
import com.example.rafael.appprototype.Help_Feedback.Help;
import com.example.rafael.appprototype.Help_Feedback.SendFeedback;
import com.example.rafael.appprototype.Login.LoginActivity;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Handle the selection of an item from the NaviagtionDrawer
 */
public class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {


    private final FragmentManager fragmentManager;
    private final DrawerLayout drawer;
    private final Context context;

    public DrawerItemClickListener(Context context, FragmentManager fragmentManager, DrawerLayout drawer) {
        this.fragmentManager = fragmentManager;
        this.drawer = drawer;
        this.context = context;

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment endFragment = null;
        if (id == R.id.evaluations) {
            endFragment = new EvaluationsMainFragment();
        } else if (id == R.id.prescription) {
            endFragment = new DrugPrescriptionMain();
        } else if (id == R.id.patients) {
            endFragment = new PatientsMain();
        } else if (id == R.id.sendFeedback) {
            endFragment = new SendFeedback();
        } else if (id == R.id.help) {
            endFragment = new Help();
        }

        if (id == R.id.logout) {
            // Insert the fragment by replacing any existing fragment
            Log.d("Logout", "Going to logout...");
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            sharedPreferences.edit().putString(Constants.userName, null).commit();
            // go to login screen
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
        } else {
            // add Exit transition
            /*
            Fragment startFragment = ((MainActivity) context).getFragmentManager().findFragmentById(R.id.content_fragment);
            startFragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.fade));
            // add Enter transition
            endFragment.setEnterTransition(TransitionInflater.from(context).
                    inflateTransition(android.R.transition.fade));
            */
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, endFragment)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}