package com.felgueiras.apps.geriatric_helper;

/**
 * Created by felgueiras on 08/03/2017.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

//            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//            String strUserName = SP.getString(getResources().getString(R.string.areaCardShowScalesIcon), "NA");

        }
    }

}