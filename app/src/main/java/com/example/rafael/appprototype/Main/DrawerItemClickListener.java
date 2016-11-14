package com.example.rafael.appprototype.Main;

import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

/**
 * Handle the selection of an item from the NaviagtionDrawer
 */
public class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {


    private final FragmentManager fragmentManager;
    private final DrawerLayout drawer;

    public DrawerItemClickListener(FragmentManager fragmentManager, DrawerLayout drawer) {
        this.fragmentManager = fragmentManager;
        this.drawer = drawer;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.evaluations) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new EvaluationsMainFragment())
                    .commit();

        } else if (id == R.id.prescription) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new DrugPrescriptionMain())
                    .commit();

        } else if (id == R.id.patients) {
            // Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new PatientsMain())
                    .commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}