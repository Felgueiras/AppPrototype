package com.example.rafael.appprototype.Main;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
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

        if (id == R.id.evaluations) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, new EvaluationsMainFragment())
                    .commit();

        } else if (id == R.id.prescription) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, new DrugPrescriptionMain())
                    .commit();

        } else if (id == R.id.patients) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, new PatientsMain())
                    .commit();

        }else if (id == R.id.logout) {
            // Insert the fragment by replacing any existing fragment
            Log.d("Logout","Going to logout...");
            SharedPreferences sharedPreferences = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
            sharedPreferences.edit().putString(Constants.userName, null).commit();
            // go to login screen
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}