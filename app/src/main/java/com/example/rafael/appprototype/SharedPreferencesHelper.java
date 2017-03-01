package com.example.rafael.appprototype;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.rafael.appprototype.DataTypes.DB.Session;

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
        Session session = Session.getSessionByID(sessionID);
        if (session != null)
            session.delete();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreferencesTag), MODE_PRIVATE);
        sharedPreferences.edit().putString(context.getString(R.string.saved_session_public), null).apply();
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
}
