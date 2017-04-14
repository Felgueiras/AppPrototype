package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by felgueiras on 21/02/2017.
 */

public class SharedPreferencesHelper {


    public static String isThereOngoingPublicSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.saved_session_public), null);
    }

    public static String isThereOngoingPrivateSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.saved_session_private), null);
    }

    public static void resetPublicSession(Activity context, String sessionID) {
        if (sessionID != null) {
            Session session = Session.getSessionByID(sessionID);
            if (session != null)
                session.delete();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_public), null).apply();
    }


    /**
     * Persiste scales version.
     *
     * @param context
     * @param scalesVersion
     */
    public static void setScalesVersion(Context context, int scalesVersion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putInt(context.getString(R.string.scalesVersion), scalesVersion).apply();
    }


    /**
     * Get the current scales version.
     *
     * @param context
     * @return
     */
    public static int getScalesVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.scalesVersion), -1);
    }

    public static void setPrivateSession(Activity context, String sessionID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), sessionID).apply();
    }

    public static void resetPrivateSession(Activity context, String sessionID) {
        Session session = Session.getSessionByID(sessionID);
        if (session != null)
            session.delete();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_private), null).apply();
    }

    public static boolean getLockStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean("LOCK", false);
    }

    public static void setLockStatus(Context context, boolean lock) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("LOCK", lock).apply();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.logged_in, false);
    }


    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.username), "");
    }

    /**
     * Get user email.
     *
     * @param context
     * @return
     */
    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.email), "");
    }

    public static String getUserPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.password), "");
    }

    /**
     * Check if it is the first public evaluation.
     *
     * @param context
     * @return
     */
    public static boolean checkFirstPublicEvaluation(Activity context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
        if (!alreadyLogged) {
            boolean firstPublicEvaluation = sharedPreferences.getBoolean("first_public_evaluation", true);
            if (firstPublicEvaluation) {
                SharedPreferences.Editor e = sharedPreferences.edit();
                //  Edit preference to make it false because we don'checkFirstStart want this to run again
                e.putBoolean("first_public_evaluation", false);
                e.apply();
                return true;
            }
        }
        return false;
    }

    public static void lockSessionCreation(Activity context) {
        // set the user as being logged in
        Log.d("Stack", "locking");

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, false).apply();
    }

    public static void unlockSessionCreation(Activity context) {
        Log.d("Stack", "unlocking");
        // set the user as being logged in
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, true).apply();
    }

    public static boolean isSessionCreationPermitted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.create_session_permitted, true);
    }


    /**
     * Persist a scale into shared prferences.
     *
     * @param scaleNonDB
     * @param context
     */
    public static void addScale(GeriatricScaleNonDB scaleNonDB, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);

        /**
         * //Retrieve the values
         Set<String> set = myScores.getStringSet("key", null);


         */

        // save scale as JSON String
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        String json = gson.toJson(scaleNonDB);

        //Set the values
        Set<String> set = new HashSet<String>();
        set.addAll(getScalesSet(context));
        // add new set
        set.add(json);

        sharedPreferences.edit().putStringSet("scales", set).apply();
        Log.d("Scales", "Size: " + set.size());
    }

    /**
     * Get scales.
     *
     * @param context
     */
    public static Set<String> getScalesSet(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getStringSet("scales", new HashSet<String>());
    }

    public static void resetScales(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putStringSet("scales", new HashSet<String>()).apply();
    }

    public static ArrayList<GeriatricScaleNonDB> getScalesArrayList(Context context) {
        ArrayList<GeriatricScaleNonDB> scales = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        Set<String> scalesSet = sharedPreferences.getStringSet("scales", new HashSet<String>());

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();
        for (String currentScaleString : new ArrayList<String>(scalesSet)) {
            GeriatricScaleNonDB scaleNonDB = gson.fromJson(currentScaleString, GeriatricScaleNonDB.class);
            scales.add(scaleNonDB);
        }
        return scales;
    }
}
