package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DatabaseGSONOps;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublic;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.DisplayTest.ScaleFragment;
import com.example.rafael.appprototype.Introduction.MyIntro;
import com.example.rafael.appprototype.LockScreen.LockScreenFragment;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.R;

public class PublicArea extends AppCompatActivity {

    /**
     * Hold the current fragment before going to lock screen
     */
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.navigation_drawer_public);

        Log.d("Lock", "onCreate");
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        final String sessionID = sharedPreferences.getString(getResources().getString(R.string.saved_session_public), null);
        if (sessionID != null) {
            Log.d("Lock", "We have sessionID!");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
            alertDialog.setMessage("Deseja retomar a Sessão que tinha em curso?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Constants.SESSION_ID = sessionID;
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.current_fragment, new CGAPublic())
                                    .commit();
                        }
                    });
            final SharedPreferences finalSharedPreferences1 = sharedPreferences;
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // erase the sessionID
                            Session.getSessionByID(sessionID).delete();
                            finalSharedPreferences1.edit().putString(getString(R.string.saved_session_public), null).apply();
                        }
                    });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    // erase the sessionID
                    Session.getSessionByID(sessionID).delete();
                    finalSharedPreferences1.edit().putString(getString(R.string.saved_session_public), null).apply();
                }
            });
            alertDialog.show();
        }

        System.out.println("1234");


        // get screen size
        Constants.screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
        if (alreadyLogged) {
            Intent intent = new Intent(PublicArea.this, PrivateArea.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

        //  Declare a new thread to do a preference check
        final SharedPreferences finalSharedPreferences = sharedPreferences;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = finalSharedPreferences.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(PublicArea.this, MyIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = finalSharedPreferences.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        Constants.area = Constants.area_public;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // insert data in DB (if first run)
        // user not logged in
        if (sharedPreferences.getBoolean(Constants.first_run, true)) {
            Log.d("FIRST RUN", "first run");
            sharedPreferences.edit().putBoolean(Constants.first_run, false).commit();
            DatabaseGSONOps.insertDataToDB();
        }

        // set handler for the Fragment stack
        BackStackHandler handler = new BackStackHandler(getFragmentManager(), this);
        getFragmentManager().addOnBackStackChangedListener(handler);

        // set sample fragment
        Fragment fragment = null;
        String defaultFragment = Constants.fragment_sessions;
        switch (defaultFragment) {
            case Constants.fragment_sessions:
                fragment = new CGAPublicInfo();
                setTitle(getResources().getString(R.string.cga));
                break;
            case Constants.fragment_drug_prescription:
                fragment = new DrugPrescriptionMain();
                setTitle(getResources().getString(R.string.tab_drug_prescription));
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment, fragment)
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


    /**
     * Display a single Test for the doctor
     *
     * @param selectedTest Test that was selected from a Session
     * @param testDB       holder for the Test Card view
     */
    public void displaySessionTest(GeriatricScaleNonDB selectedTest, GeriatricScale testDB) {
        // Create new fragment and transaction
        Fragment newFragment = new ScaleFragment();
        // add arguments
        Bundle bundle = new Bundle();
        bundle.putSerializable(ScaleFragment.testObject, selectedTest);
        bundle.putSerializable(ScaleFragment.testDBobject, testDB);
        Patient patient = testDB.getSession().getPatient();
        if (patient != null)
            bundle.putSerializable(ScaleFragment.patient, patient);
        newFragment.setArguments(bundle);
        // setup the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.current_fragment, newFragment);
        transaction.addToBackStack(Constants.tag_display_session_scale).commit();
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
        currentFragment = this.getFragmentManager().findFragmentById(R.id.current_fragment);
        Log.d("Lock", "Stored current fragment");
        // create LockScreenFragment
        LockScreenFragment fragment = new LockScreenFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment, fragment)
                .commit();
    }

    public void setLockStatus(boolean lock) {
        getSharedPreferences("SOMETAG", 0).edit().putBoolean("LOCK", lock)
                .commit();
    }

    public boolean getLockStatus() {
        return getSharedPreferences("SOMETAG", 0).getBoolean("LOCK", true);
    }


    public void resumeAfterLock() {
        Log.d("Lock", "Resuming after lock!");
        // replace the Fragment before lock screen
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment, currentFragment)
                .commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.erase_data, menu);


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.erase_data:
                // erase all data
                DatabaseGSONOps.eraseAll();
                DatabaseGSONOps.insertDataToDB();
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
     * Replace one fragment by another
     *
     * @param args
     */
    public void replaceFragment(Fragment endFragment, Bundle args, String addToBackStackTag) {

        // get current Fragment
        Fragment startFragment = getFragmentManager().findFragmentById(R.id.current_fragment);
        if (args != null) {
            endFragment.setArguments(args);
        }
        // add Exit transition

        startFragment.setExitTransition(TransitionInflater.from(
                this).inflateTransition(android.R.transition.fade));
        // add Enter transition
        endFragment.setEnterTransition(TransitionInflater.from(this).
                inflateTransition(android.R.transition.fade));

        // Create new transaction and add to back stack
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.current_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        transaction.commit();

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
        Fragment startFragment = getFragmentManager().findFragmentById(R.id.current_fragment);
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
        transaction.replace(R.id.current_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
        if (args.getString("TRANS_TEXT") != null) {
            transaction.addSharedElement(textView, args.getString("TRANS_TEXT"));
            //system.out.println("lol");
        }

        transaction.commit();

    }


    @Override
    protected void onPause() {
        Log.d("Lock", "Public onPause -> going to lock");
        if (Constants.SESSION_ID != null) {
            Log.d("Lock", "Public onPause -> SessionID" + Constants.SESSION_ID);
        }
        super.onStop();
    }


}