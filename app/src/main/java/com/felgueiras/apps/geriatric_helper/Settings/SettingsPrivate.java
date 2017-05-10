package com.felgueiras.apps.geriatric_helper.Settings;

/**
 * Created by felgueiras on 08/03/2017.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

public class SettingsPrivate extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        static SharedPreferences.OnSharedPreferenceChangeListener changeListener;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_private);

            changeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    boolean syncEnabled = SP.getBoolean(key, false);
                    if (syncEnabled) {
                        Log.d("Sync", "Syncing every patient to DB");

                        // get every patient from SharedPreferences
                        ArrayList<PatientFirebase> patients = SharedPreferencesHelper.getPatients(getActivity());
                        for (PatientFirebase patient : patients) {
                            PatientsManagement.getInstance().addPatient(patient, getActivity());
                        }
                    }
                }
            };

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(MyPreferenceFragment.changeListener);

    }


    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(MyPreferenceFragment.changeListener);


    }
}