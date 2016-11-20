package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.ViewAvailableTests.DisplayTestCard;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewSingleTestFragment;
import com.example.rafael.appprototype.HandleStack;
import com.example.rafael.appprototype.LockScreen.LockScreenFragment;
import com.example.rafael.appprototype.Login.LoginActivity;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest.DisplaySingleTestFragment;
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
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        // insert data in DB (if first run)
        sharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        // user not logged in
        if (sharedPreferences.getString(Constants.userName, null) == null  ) {
            Log.d("FIRST RUN", "first run");
            DatabaseOps.insertDataToDB();
            // display login screen
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        // user already logged in

        else
        {
            // TODO set the doctor photo after having logged in
            View headerLayout = navigationView.getHeaderView(0);
            TextView userName = (TextView) headerLayout.findViewById(R.id.userName);
            userName.setText(sharedPreferences.getString(Constants.userName, null));
            ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userPhoto);
            //userImage.setImageResource(R.drawable.male);
            //TextView userSubtext = (TextView) headerLayout.findViewById(R.id.userSubText);
            //userSubtext.setText("[Some text here]");
        }




        DatabaseOps.eraseAll();
        DatabaseOps.insertDataToDB();

        // set handler for the Fragment stack
        HandleStack handler = new HandleStack(getFragmentManager(), this);
        getFragmentManager().addOnBackStackChangedListener(handler);

        // set default fragment
        Fragment fragment = null;
        String defaultFragment = Constants.fragment_show_patients;
        switch (defaultFragment) {
            case Constants.fragment_sessions:
                fragment = new EvaluationsMainFragment();
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Log","Clicked");
            }
        });
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(new DrawerItemClickListener(this, getFragmentManager(), drawer));

    }


    /**
     * Display a single Test for the doctor
     * @param selectedTest Test that was selected from a Session
     * @param testDB holder for the Test Card view
     */
    public void displaySessionTest(GeriatricTestNonDB selectedTest, GeriatricTest testDB) {
        // Create new fragment and transaction
        Fragment newFragment = new DisplaySingleTestFragment();
        // add arguments
        Bundle bundle = new Bundle();
        bundle.putSerializable(DisplaySingleTestFragment.testObject, selectedTest);
        bundle.putSerializable(DisplaySingleTestFragment.testDBobject, testDB);
        Patient patient = testDB.getSession().getPatient();
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
        Log.d("Back","...");
        FragmentManager fragmentManager = getFragmentManager();
        HandleStack.handleBackButton(fragmentManager);
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