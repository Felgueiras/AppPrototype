package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.DrugPrescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.Evaluations.EvaluationsMainFragment;
import com.example.rafael.appprototype.LockScreen.LockScreenFragment;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

public class PrivateArea extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    /**
     * Hold the current fragment before going to lock screen
     */
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Constants.area = Constants.area_private;
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.navigation_drawer_private);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        // insert data in DB (if first run)
        sharedPreferences = getSharedPreferences("com.mycompany.myAppName", 0);
        // user not logged in
        if (sharedPreferences.getBoolean(Constants.first_run, true)) {
            Log.d("FIRST RUN", "first run");
            sharedPreferences.edit().putBoolean(Constants.first_run, false).commit();
            DatabaseOps.insertDataToDB();
            // display login screen
            // TODO log in
            /*
            Intent i = new Intent(PrivateArea.this, LoginActivity.class);
            startActivity(i);
            */

        }
        // user already logged in

        else {
            // TODO set the doctor photo after having logged in
            View headerLayout = navigationView.getHeaderView(0);
            TextView userName = (TextView) headerLayout.findViewById(R.id.userName);
            userName.setText(sharedPreferences.getString(Constants.userName, null));
            ImageView userImage = (ImageView) headerLayout.findViewById(R.id.userPhoto);
            //userImage.setImageResource(R.drawable.male);
            //TextView userSubtext = (TextView) headerLayout.findViewById(R.id.userSubText);
            //userSubtext.setText("[Some text here]");
        }

        System.out.println("PRIVATE AREA");

        // set handler for the Fragment stack
        BackStackHandler handler = new BackStackHandler(getFragmentManager(), this);
        getFragmentManager().addOnBackStackChangedListener(handler);

        // set sample fragment
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
                .replace(R.id.content_fragment, fragment)
                .commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Log", "Clicked");
            }
        });
        drawer.setDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(new DrawerItemClickListener(this, getFragmentManager(), drawer));

    }





    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        BackStackHandler.handleBackButton(fragmentManager);
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
        currentFragment = this.getFragmentManager().findFragmentById(R.id.content_fragment);
        Log.d("Lock", "Stored current fragment");
        // create LockScreenFragment
        LockScreenFragment fragment = new LockScreenFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_fragment, fragment)
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
                .replace(R.id.content_fragment, currentFragment)
                .commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sample, menu);


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                // erase all data
                DatabaseOps.eraseAll();
                DatabaseOps.insertDataToDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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


    /**
     * Replace a fragment with another one using shared elements.
     *
     * @param endFragment
     * @param args
     * @param addToBackStackTag
     * @param textView
     */
    public void replaceFragmentSharedElements(Fragment endFragment, Bundle args, String addToBackStackTag, TextView textView) {
        if (args != null) {
            endFragment.setArguments(args);
        }
        // get current Fragment
        Fragment startFragment = getFragmentManager().findFragmentById(R.id.content_fragment);
        if (args != null) {
            endFragment.setArguments(args);
        }
        // add Exit transition
        // TODO transitions are messing up with GraphView
        /*
        startFragment.setExitTransition(TransitionInflater.from(
                this).inflateTransition(android.R.transition.fade));
        // add Enter transition
        endFragment.setEnterTransition(TransitionInflater.from(this).
                inflateTransition(android.R.transition.fade));
        */
        // Create new transaction and add to back stack
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        if (args.getString("TRANS_TEXT") != null) {
            transaction.addSharedElement(textView, args.getString("TRANS_TEXT"));
            //system.out.println("lol");
        }

        transaction.commit();

    }
}