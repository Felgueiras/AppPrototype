package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.HandleStack;
import com.example.rafael.appprototype.LockScreen.LockScreenFragment;
import com.example.rafael.appprototype.Login.LoginActivity;
import com.example.rafael.appprototype.Sessions.NewSessionTab.DisplayTest.SingleTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.Sessions.NewSessionTab.Sessions;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    /**
     * Hold the current fragment before going to lock screen
     */
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.activity_main_activity_recycler);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // insert data in DB (if first run)
        sharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstrun", true)) {
            sharedPreferences.edit().putBoolean("firstrun", false).commit();
            Log.d("FIRST RUN", "first run");
            DatabaseOps.insertDataToDB();
            // display login screen
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            // startActivity(i);

        }
        DatabaseOps.eraseAll();
        DatabaseOps.insertDataToDB();

        // set handler for the Fragment stack
        HandleStack handler = new HandleStack(getFragmentManager());
        getFragmentManager().addOnBackStackChangedListener(handler);

        // set default fragment

        Fragment fragment = null;
        /*
      Default Fragment to open when the app starts
     */

        String defaultFragment = Constants.fragment_show_patients;
        switch (defaultFragment) {
            case Constants.fragment_sessions:
                fragment = new Sessions();
                setTitle(getResources().getString(R.string.tab_sessions));
                break;
            case Constants.fragment_show_patients:
                fragment = new PatientsMain();
                setTitle(getResources().getString(R.string.tab_my_patients));
                break;
            case Constants.fragment_drug_prescription:
                fragment = new DrugPrescriptionMain();
                setTitle(getResources().getString(R.string.tab_drug_prescription));
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        /**
         * Set the NavigationDrawer items
         */
        String[] drawerPages = new String[]{
                getResources().getString(R.string.tab_sessions),
                getResources().getString(R.string.tab_drug_prescription),
                getResources().getString(R.string.tab_my_patients),
        };
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawerLayout != null;
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, drawerPages));
        // Set the list's click listener
        DrawerItemClickListener drawerListener = new DrawerItemClickListener(drawerPages, getFragmentManager(), this,
                drawerList, drawerLayout);
        drawerList.setOnItemClickListener(drawerListener);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Display a single Test for the doctor
     *
     * @param selectedTest Test that was selected from a Session
     * @param session      ID for the current Session
     * @param patient
     */
    public void displaySessionTest(GeriatricTestNonDB selectedTest, Session session, boolean alreadyOpened, Patient patient) {
        // Create new fragment and transaction
        Fragment newFragment = new DisplaySingleTestFragment();
        // add arguments
        Bundle bundle = new Bundle();
        bundle.putSerializable(DisplaySingleTestFragment.testObject, selectedTest);
        bundle.putSerializable(DisplaySingleTestFragment.sessionID, session);
        Log.d("Session", "Putting session: " + session.toString());
        bundle.putBoolean(DisplaySingleTestFragment.alreadyOpenedBefore, alreadyOpened);
        if (patient != null)
            bundle.putSerializable(DisplaySingleTestFragment.patient, patient);
        newFragment.setArguments(bundle);
        // setup the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(Constants.tag_display_session_test).commit();
    }

    /**
     * Replace one fragment by another
     *
     * @param fragmentClass
     * @param args
     */
    public void replaceFragment(Class fragmentClass, Bundle args, String addToBackStackTag) {
        Fragment newFragment = null;
        try {
            newFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (args != null) {
            newFragment.setArguments(args);
        }
        // Create new transaction and add to back stack
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        HandleStack.handleBackButton(fragmentManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        /*
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        */
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getLockStatus()) {
            // show lockscreen
            Log.d("Lock", "showing lock screen (onResume)");
            // store current fragment
            // showLockScreen();

        } else {
            // we are not locked.
            Log.d("Lock", "not locked");

        }

    }

    private void showLockScreen() {
        currentFragment = this.getFragmentManager().findFragmentById(R.id.content_frame);
        Log.d("Lock", "Stored current fragment");
        // create LockScreenFragment
        LockScreenFragment fragment = new LockScreenFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void setLockStatus(boolean lock) {
        getSharedPreferences("SOMETAG", 0).edit().putBoolean("LOCK", lock)
                .commit();
    }

    public boolean getLockStatus() {
        return getSharedPreferences("SOMETAG", 0).getBoolean("LOCK", true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Lock", "onPause -> going to lock");
        setLockStatus(true);
    }

    public void resumeAfterLock() {
        Log.d("Lock", "Resuming after lock!");
        // replace the Fragment before lock screen
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getLockStatus() == true) {
            // show lockscreen
            Log.d("Lock", "showing lock screen (onStart)");
            // set lock status to false
        } else {
            // we are not locked.
            Log.d("Lock", "not locked");
        }
    }

}