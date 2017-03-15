package com.example.rafael.appprototype.Main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.LockScreen.LockScreenActivity;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.HelpersHandlers.ToolbarHelper;

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

        final Activity context = this;

        /**
         * Views/layout.
         */
        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // display home
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Constants.toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Constants.toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new DrawerItemClickListener(this, getFragmentManager(), drawer));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Toolbar", view.toString());
                if (!Constants.upButton) {
                    drawer.openDrawer(Gravity.LEFT);
                } else {
                    Log.d("Toolbar", "Up button showing");
                    onBackPressed();
                }
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        final String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(context);
        if (sessionID != null) {
            Log.d("Session", "Ongoing private session");
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
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Não",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // erase the sessionID
                            SharedPreferencesHelper.resetPrivateSession(context, sessionID);
                        }
                    });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    // erase the sessionID
                    SharedPreferencesHelper.resetPrivateSession(context, sessionID);
                }
            });
            alertDialog.show();
        }


        // set username
        View headerLayout = navigationView.getHeaderView(0);
        TextView userName = (TextView) headerLayout.findViewById(R.id.userName);
        userName.setText(sharedPreferences.getString(getString(R.string.username), null));


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
            case Constants.fragment_sessions:
                fragment = new EvaluationsHistoryMain();
                setTitle(getResources().getString(R.string.tab_sessions));
                break;

        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment, fragment)
                .commit();


    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        BackStackHandler.handleBackButton(fragmentManager);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SharedPreferencesHelper.getLockStatus(this)) {
            // show lockscreen
            Log.d("Lock", "onResume - showing lock screen");
            // store current fragment
//            showLockScreen();
        } else {
            Log.d("Lock", "onResume - not locked");
        }

    }

    private void showLockScreen() {
        currentFragment = this.getFragmentManager().findFragmentById(R.id.current_fragment);
        // create LockScreenActivity
//        LockScreenActivity fragment = new LockScreenActivity();
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.current_fragment, fragment)
//                .commit();

        Intent intent = new Intent(PrivateArea.this, LockScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.erase_data, menu);
//
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        //respond to menu item selection
//        switch (item.getItemId()) {
//            case R.id.erase_data:
//                // erase all data
//                DatabaseOps.eraseAll();
//                DatabaseOps.insertDummyData();
//                return true;
////            case R.id.save_gson:
////                // save data as GSON
////                DatabaseOps.saveDataGson(this);
////                return true;
////            case R.id.read_gson:
////                // save data as GSON
////                DatabaseOps.readDataGson(this);
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }


    @Override
    public void onStart() {
        super.onStart();
        if (SharedPreferencesHelper.getLockStatus(this)) {
            // show lockscreen
//            Log.d("Lock", "showing lock screen (onStart)");
            // set lock status to false
        } else {
            // we are not locked.
//            Log.d("Lock", "not locked");
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


        ToolbarHelper.showBackButton(this);


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
//        if (args.getString("TRANS_TEXT") != null) {
//            transaction.addSharedElement(textView, args.getString("TRANS_TEXT"));
//            //system.out.println("lol");
//        }

        transaction.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lock", "onStop -> going to lock");
        SharedPreferencesHelper.setLockStatus(this, true);
    }

    public void changeTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}