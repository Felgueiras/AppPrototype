package com.felgueiras.apps.geriatrichelper.Main;

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
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.MyApplication;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPublic;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPublicInfo;
import com.felgueiras.apps.geriatrichelper.Introduction.GeriatricHelperIntro;
import com.felgueiras.apps.geriatrichelper.Prescription.PrescriptionMainFragment;
import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublicAreaActivity extends AppCompatActivity {


    /**
     * Current context.
     */
    private PublicAreaActivity context;
    /**
     * SharedPreferences.
     */
    private SharedPreferences sharedPreferences;

    BackStackHandler handler;
    // ButterKnife
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // save context
        context = this;

        // support vector drawables on lower API
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_public);

        ButterKnife.bind(this);


        //Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // user already logged in
        if (auth.getCurrentUser() != null) {

            // load initial list of patients
            FirebaseHelper.initializeFirebase();

            PatientsManagement.loadInitialPatients(this);
            startActivity(new Intent(this, PrivateAreaActivity.class));
            finish();
        }


        /*
          Views/layout.
         */
        // setup toolbar
        setSupportActionBar(toolbar);


        // display home
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Constants.toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Constants.toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new DrawerItemClickListener(this, getFragmentManager(), drawer));
        navigationView.getMenu().getItem(0).setChecked(true);

        // show modules in navigation drawer
        ModulesManagement.manageModulesPublicArea(context, navigationView);

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

        // initialize ActiveAndroid (DB)
        ActiveAndroid.initialize(getApplication());


        // access SharedPreferences
        Log.d("Preferences", getString(R.string.sharedPreferencesTag));
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        // is there an ongoing public session?
        isTherePublicSession();

        // get screen size
        Constants.screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;


        boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
        if (alreadyLogged) {
            Intent intent = new Intent(PublicAreaActivity.this, PrivateAreaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }

        // not logged in

        //  Declare a new thread to do a preference check
        final SharedPreferences finalSharedPreferences = sharedPreferences;
        Thread checkFirstStart = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = finalSharedPreferences.getBoolean("firstStart", true);

                /**
                 * Disable launchinng app intro when testing
                 * Disable tour guide
                 */
                if (MyApplication.isRunningTest()) {
                    isFirstStart = false;
                    SharedPreferencesHelper.disableTour(getApplicationContext());
                }

                //  If the activity has never started before...
                if (isFirstStart) {
                    //  Launch app intro
                    Intent i = new Intent(PublicAreaActivity.this, GeriatricHelperIntro.class);
                    startActivity(i);
                    //  Make a new preferences editor
                    SharedPreferences.Editor e = finalSharedPreferences.edit();
                    //  Edit preference to make it false because we don'checkFirstStart want this to run again
                    e.putBoolean("firstStart", false);
                    e.apply();
                    // insert dummy data in the app
//                    DatabaseOps.insertDummyData();
                }
            }
        });
        // Start the thread
        checkFirstStart.start();

        // set public area of the app
        Constants.area = Constants.area_public;


        // set sample fragment
        Fragment fragment = null;
        String defaultFragment = Constants.fragment_sessions;
        switch (defaultFragment) {
            case Constants.fragment_sessions:
                fragment = new CGAPublicInfo();
                setTitle(getResources().getString(R.string.cga));
                break;
            case Constants.fragment_drug_prescription:
                fragment = new PrescriptionMainFragment();
                setTitle(getResources().getString(R.string.tab_drug_prescription));
                break;
        }
        if (savedInstanceState == null) {
            // set handler for the Fragment stack
            handler = new BackStackHandler(getFragmentManager(), this);
            getFragmentManager().addOnBackStackChangedListener(handler);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.current_fragment, fragment, "initial_tag")
                    .commit();

        } else {
            Log.d("Orientation", "Saved instance state not null");
            fragment = getFragmentManager().findFragmentByTag("initial_tag");
//            handler =
            handler = (BackStackHandler) savedInstanceState.getSerializable("backStackHandler");
            Log.d("Key", BackStackHandler.getFragmentManager().getBackStackEntryCount() + "");
            handler.onBackStackChanged();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", "12345");
        // save fragment manager
        outState.putSerializable("backStackHandler", handler);
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        BackStackHandler.handleBackButton(fragmentManager);
    }


//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.erase_data, menu);
//
//
//        return true;
//    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        //respond to menu item selection
//        switch (item.getItemId()) {
//            case R.id.erase_data:
//                // erase all data
//                DatabaseOps.eraseAll();
//                DatabaseOps.insertDummyData();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }


    @Override
    public void onStart() {
        super.onStart();
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

//        startFragment.setExitTransition(TransitionInflater.from(
//                this).inflateTransition(android.R.transition.fade));
//        // add Enter transition
//        endFragment.setEnterTransition(TransitionInflater.from(this).
//                inflateTransition(android.R.transition.fade));

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
        /*
        startFragment.setExitTransition(TransitionInflater.from(
                this).inflateTransition(android.R.transition.fade));
        // add Enter transition
        endFragment.setEnterTransition(TransitionInflater.from(this).
                inflateTransition(android.R.transition.fade));
        */
        // Create new transaction and add to back stack
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(R.id.current_fragment, endFragment);
        if (!addToBackStackTag.equals(""))
            transaction.addToBackStack(addToBackStackTag);
//        if (args.getString("TRANS_TEXT") != null) {
//            transaction.addSharedElement(textView, args.getString("TRANS_TEXT"));
//            //system.out.println("lol");
//        }

        transaction.commit();

    }


    /**
     * Check if the app was closed when in the middle of a session.
     */
    public void isTherePublicSession() {
        Log.d("Session", "checking if there is public session");
        final String sessionID = SharedPreferencesHelper.isThereOngoingPublicSession(this);

        if (sessionID != null) {
            /**
             * Avoid showing dialog about continuing last session when testing
             */
            if (MyApplication.isRunningTest()) {
                SharedPreferencesHelper.resetPublicSession(context);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Foi Encontrada uma Sessão a decorrer");
                alertDialog.setMessage("Deseja retomar a Sessão que tinha em curso?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Constants.SESSION_ID = sessionID;

                                // get public scales from prefs
                                Constants.publicScales = SharedPreferencesHelper.getPublicScales(context);
                                Constants.publicQuestions = SharedPreferencesHelper.getPublicQuestions(context);
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
                                SharedPreferencesHelper.resetPublicSession(context);
                                // reset public scales and questions from prefs
                                SharedPreferencesHelper.resetPublicScalesQuestions(context);
                            }
                        });
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        // erase the sessionID
                        SharedPreferencesHelper.resetPublicSession(context);
                        // reset public scales and questions from prefs
                        SharedPreferencesHelper.resetPublicScalesQuestions(context);
                    }
                });
                alertDialog.show();
            }


        }

    }

    public void updateDrawer() {
        Log.d("PublicArea", "Updating drawer...");
        ModulesManagement.manageModulesPublicArea(context, navigationView);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PublicActiv", "On stop");

        // persist public scales and questions
        SharedPreferencesHelper.persistPublicScalesQuestions(this);

        // get them
        ArrayList<GeriatricScaleFirebase> publicScales = SharedPreferencesHelper.getPublicScales(this);
        Log.d("PublicActiv", publicScales.size() + "");

        // get them
        ArrayList<QuestionFirebase> publicQuestions = SharedPreferencesHelper.getPublicQuestions(this);
        Log.d("PublicActiv", publicQuestions.size() + "");
    }

}