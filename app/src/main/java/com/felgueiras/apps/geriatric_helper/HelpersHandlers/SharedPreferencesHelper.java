package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.R;

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
        Log.d("Stack","locking");

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, false).apply();
    }

    public static void unlockSessionCreation(Activity context) {
        Log.d("Stack","unlocking");
        // set the user as being logged in
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.create_session_permitted, true).apply();
    }

    public static boolean isSessionCreationPermitted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.create_session_permitted, true);
    }


}
