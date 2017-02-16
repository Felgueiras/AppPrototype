package com.example.rafael.appprototype.Main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.CGA.CGAPublic;
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
    private final Activity context;

    public DrawerItemClickListener(Activity context, FragmentManager fragmentManager, DrawerLayout drawer) {
        this.fragmentManager = fragmentManager;
        this.drawer = drawer;
        this.context = context;

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            // Insert the fragment by replacing any existing fragment
            Log.d("Login", "Going to login");
            // check if there is any on-going session
            if (Constants.sessionID != null) {
                // delete from DB
                Session.getSessionByID(Constants.sessionID).delete();
                Constants.sessionID = null;
            }
            // go to login screen
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            context.finish();
            return true;
        }

        Fragment endFragment = null;

        if (id == R.id.evaluations_public) {
            endFragment = new CGAPublic();
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
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle(getResources().getString(R.string.session_discard));
            alertDialog.setMessage(context.getResources().getString(R.string.logout_choice));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // erase backstack?
                            SharedPreferences sharedPreferences = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean(Constants.logged_in, false).apply();

                            Intent intent = new Intent(context, PublicArea.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
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

        } else {
            // add Exit transition

            Fragment startFragment = context.getFragmentManager().findFragmentById(R.id.content_fragment);
            startFragment.setExitTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.fade));
            // add Enter transition
            endFragment.setEnterTransition(TransitionInflater.from(context).
                    inflateTransition(android.R.transition.fade));

            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, endFragment)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}