package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DatabaseGSONOps;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
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
        System.out.println("Inside PrivateArea");
        Constants.area = Constants.area_private;
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(getApplication());
        setContentView(R.layout.navigation_drawer_private);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Log.d("Lock", "onCreate");
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        final String sessionID = sharedPreferences.getString(getResources().getString(R.string.saved_session_private), null);
        if (sessionID != null) {
            Log.d("Lock", "We have sessionID!");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
            alertDialog.setMessage("Deseja retomar a Sessão que tinha em curso?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.current_fragment, new CGAPrivate())
                                    .commit();
                        }
                    });
            final SharedPreferences finalSharedPreferences1 = sharedPreferences;
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // erase the sessionID
                            if (Session.getSessionByID(sessionID) != null)
                                Session.getSessionByID(sessionID).delete();
                            finalSharedPreferences1.edit().putString(getString(R.string.saved_session_private), null).apply();
                        }
                    });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    // erase the sessionID
                    if (Session.getSessionByID(sessionID) != null)
                        Session.getSessionByID(sessionID).delete();
                    finalSharedPreferences1.edit().putString(getString(R.string.saved_session_private), null).apply();
                }
            });
            alertDialog.show();
        }


        // insert data in DB (if first run)
        sharedPreferences = getSharedPreferences("com.mycompany.myAppName", 0);
        // user not logged in
        if (sharedPreferences.getBoolean(Constants.first_run, true)) {
            Log.d("FIRST RUN", "first run");
            sharedPreferences.edit().putBoolean(Constants.first_run, false).commit();
            DatabaseGSONOps.insertDataToDB();
            // display login screen
            // TODO log in
            /*
            Intent i = new Intent(CGAAreaPrivate.this, LoginActivity.class);
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


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        BackStackHandler.handleBackButton(fragmentManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resuming", "Resuming - PrivateArea");
        // check if we have an ongoing session
        Log.d("Resuming", "Resuming - PrivateArea");


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
            case R.id.save_gson:
                // save data as GSON
                DatabaseGSONOps.saveDataGson(this);
                return true;
            case R.id.read_gson:
                // save data as GSON
                DatabaseGSONOps.readDataGson(this);
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
        currentFragment = getFragmentManager().findFragmentById(R.id.current_fragment);
        transaction.remove(currentFragment);
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
    protected void onStop() {
        Log.d("Lock", "Private onStop -> going to lock");
        super.onStop();
    }
}