package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DatabaseOps;
import com.example.rafael.appprototype.LockScreen.LockScreenFragment;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.NewSessionFragment;
import com.example.rafael.appprototype.SessionsHistoryTab.SessionsHistoryFragment;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ViewPatientsTab.ViewPatientsFragment;

public class MainActivity extends AppCompatActivity {


    private String[] drawerPages;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
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

        // insert data in DB (if first run)
        sharedPreferences = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstrun", true)) {
            sharedPreferences.edit().putBoolean("firstrun", false).commit();
            Log.d("FIRST RUN", "first run");
            DatabaseOps.insertDataToDB();
            // display login screen
            //Intent i = new Intent(MainActivity.this, LoginActivity.class);
            //startActivity(i);
        }
        DatabaseOps.eraseAll();
        DatabaseOps.insertDataToDB();

        /*
        getFragmentManager().
                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

                    @Override
                    public void onBackStackChanged() {
                        Log.d("BackStack", "BackStack changed");
                        if (getFragmentManager().getBackStackEntryCount() == 0)
                            return;
                        String fragmentName = getFragmentManager().getBackStackEntryAt(0).getName();
                        Fragment fr = getFragmentManager().findFragmentById(R.id.content_frame);
                        if (fragmentName.equals("displayTest")) {
                            Log.d("BackStack", "Fragment class is " + fr.getClass().getSimpleName());
                            Bundle arguments = fr.getArguments();
                            Session currentSession = (Session) arguments.getSerializable(DisplaySingleTestFragment.sessionID);
                            Log.d("BackStack", "SessionID is " + currentSession.getGuid());
                        }
                    }
                });
                */

        // set default fragment
        Fragment fragment = new SessionsHistoryFragment();
        setTitle(getResources().getString(R.string.patients_history));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        drawerPages = new String[]{getResources().getString(R.string.patients_history),
                //getResources().getString(R.string.consult_agenda),
                getResources().getString(R.string.create_new_session),
                getResources().getString(R.string.my_patients)};
        //getResources().getString(R.string.add_new_patient)};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, drawerPages));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * Display a single Test for the doctor
     *
     * @param selectedTest Test that was selected from a Session
     * @param session      ID for the current Session
     */
    public void displaySessionTest(GeriatricTestNonDB selectedTest, Session session, boolean alreadyOpened) {
        // Create new fragment and transaction
        Fragment newFragment = new DisplaySingleTestFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DisplaySingleTestFragment.testObject, selectedTest);
        bundle.putSerializable(DisplaySingleTestFragment.sessionID, session);
        bundle.putBoolean(DisplaySingleTestFragment.alreadyOpenedBefore, alreadyOpened);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("displayTest").commit();
    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Check which action to perform
            String selectedPage = drawerPages[position];
            Fragment fragment = null;
            if (selectedPage == getResources().getString(R.string.patients_history)) {
                fragment = new SessionsHistoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                setTitle(getResources().getString(R.string.tab_patients));
            } /*else if (selectedPage == getResources().getString(R.string.consult_agenda)) {
                fragment = new AgendaFragment();
                Bundle args = new Bundle();
                args.putInt(ArticleFragment.ARG_PLANET_NUMBER, position);
                fragment.setArguments(args);
                setTitle(getResources().getString(R.string.tab_agenda));
            }*/ else if (selectedPage == getResources().getString(R.string.create_new_session)) {
                fragment = new NewSessionFragment();
                setTitle(getResources().getString(R.string.tab_new_session));
            } else if (selectedPage == getResources().getString(R.string.my_patients)) {
                fragment = new ViewPatientsFragment();
                /*
                Bundle args = new Bundle();
                args.putInt(ArticleFragment.ARG_PLANET_NUMBER, position);
                fragment.setArguments(args);
                */
                setTitle(getResources().getString(R.string.tab_my_patients));
            }
            /*else if (selectedPage == getResources().getString(R.string.add_new_patient)) {
                fragment = new ArticleFragment();
                Bundle args = new Bundle();
                args.putInt(ArticleFragment.ARG_PLANET_NUMBER, position);
                fragment.setArguments(args);
                setTitle(getResources().getString(R.string.tab_add_new_patient));
            }
            */


            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();


            // Highlight the selected item, update the date, and close the drawer
            drawerList.setItemChecked(position, true);
            // setTitle(drawerPages[position]);
            drawerLayout.closeDrawer(drawerList);
        }
    }

    /**
     * Replace one fragment by another
     *
     * @param fragmentClass
     * @param args
     */
    public void replaceFragment(Class fragmentClass, Bundle args) {
        Fragment newFragment = null;
        try {
            newFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (args != null) {
            // add Bundle to the Fragment
            newFragment.setArguments(args);
        }
        // Create new fragment and transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("frag").commit();

        // add on backstack changed listener
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backCount = getFragmentManager().getBackStackEntryCount();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Log.d("Backstack", "onBackPressed");
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentName = getFragmentManager().getBackStackEntryAt(0).getName();
            Fragment fr = getFragmentManager().findFragmentById(R.id.content_frame);
            fragmentManager.popBackStack();
            if (fragmentName.equals("displayTest")) {
                // get the arguments
                Bundle arguments = fr.getArguments();
                Session session = (Session) arguments.getSerializable(DisplaySingleTestFragment.sessionID);
                Bundle args = new Bundle();
                args.putSerializable(NewSessionFragment.sessionObject, session);
                Fragment fragment = new NewSessionFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getLockStatus() == true) {
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
        Fragment f = this.getFragmentManager().findFragmentById(R.id.content_frame);
        currentFragment = f;
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

    /*
    @Override
    public void onStart() {
        super.onStart();
        if (getLockStatus() == true) {
            // show lockscreen
            Log.d("Lock","showing lock screen (onStart)");
            // set lock status to false
        } else {
            // we are not locked.
            Log.d("Lock","not locked");
        }
    }
    */


}