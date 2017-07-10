package com.felgueiras.apps.geriatric_helper.Settings;

/**
 * Created by felgueiras on 08/03/2017.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Sessions.AllAreas.CGAPublic;

public class SettingsPublic extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        private SharedPreferences.OnSharedPreferenceChangeListener spChanged;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_public);

            Preference tourGuidePref = (Preference) findPreference(getResources().getString(R.string.tourGuide));
            tourGuidePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    // create a CGApublic activity with the help
                    SharedPreferencesHelper.enableTour(getActivity());
                    SharedPreferencesHelper.unlockSessionCreation(getActivity());
//                    FragmentTransitions.replaceFragment(Constants.publicArea, new CGAPublic(), null, Constants.tag_cga_public);
                    getActivity().finish();
                    return true;
                }
            });


            spChanged = new
                    SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                              String key) {
                            // update public area
                            Constants.publicArea.updateDrawer();



                            // check if "tour" key was changed
                            boolean tourGuideState = sharedPreferences.getBoolean(getResources().getString(R.string.tourGuide),
                                    false);
                            if (tourGuideState) {
                                // enable tour guide
                                SharedPreferencesHelper.enableTour(getActivity());
                            }


                        }
                    };
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(spChanged);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(spChanged);
            super.onPause();
        }
    }

}