package com.felgueiras.apps.geriatrichelper.Firebase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * Created by felgueiras on 15/04/2017.
 */

public class FirebaseRemoteConfig {
    /**
     * Setup remote config (Firebase).
     *
     * @param context
     */
    public static void setupRemoteConfig(Context context) {
        Log.d("Firebase", "Setting up remote config");
        FirebaseHelper.mFirebaseRemoteConfig = com.google.firebase.remoteconfig.FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        FirebaseHelper.mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        // set defaults - can be setup using a map
//        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        // fetch remote config
        // cache expiration in seconds
        long cacheExpiration = 3600;

//expire the cache immediately for development mode.
        if (FirebaseHelper.mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // fetch
        FirebaseHelper.mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            FirebaseHelper.mFirebaseRemoteConfig.activateFetched();

                            // check whether promo is on
                            String appName = FirebaseHelper.mFirebaseRemoteConfig.getString("app_name");
                            Log.d("RemoteConfig", appName);

                        } else {
                            //task failed
                        }
                    }
                });
    }

    /**
     * Get a String for a "key" using Firebase Remote Config instance.
     *
     * @param key
     * @param stringInResources
     * @return
     */
    public static String getString(String key, String stringInResources) {
        String ret = "";
        String stringFromFirebase = FirebaseHelper.mFirebaseRemoteConfig.getString(key);
        if (stringFromFirebase.equals("")) {
            // if not defined in RemoteConfig, load from languages file
            ret = stringInResources;
        } else {
            ret = stringFromFirebase;
        }
        return ret.replace("\\n", System.getProperty("line.separator"));


    }
}
