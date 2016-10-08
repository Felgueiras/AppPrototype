package com.example.rafael.appprototype;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 08-10-2016.
 */
public class DatabaseOps {


    public static void eraseAll() {
        SQLiteDatabase db = ActiveAndroid.getDatabase();
        List<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tableName = cursor.getString(1);
            if (!tableName.equals("android_metadata") &&
                    !tableName.equals("sqlite_sequence")) {
                tables.add(tableName);
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (tables.size() > 0) {
            for (String tableName : tables) {
                db.execSQL("DELETE FROM " + tableName);
            }
        }

    }

    /**
     * Insert dummy data into DB
     */
    public static void insertDataToDB() {
        int numSessionsPerPacient = 3;
        String[] patientNames = new String[]{
                "João Almeida",
                "Maria da Luz",
                "José Francisco Pinto",
                "Leonor Conceição",
                "Vítor Semedo"
        };
        int[] patientAges = new int[]{
                65,
                79,
                80,
                72,
                67
        };
        char[] patientGenders = new char[]{
                'm',
                'f',
                'm',
                'f',
                'm'
        };
        String[] patientAddresses = new String[]{
                "Cacia",
                "Aveiro",
                "Sarrazola",
                "Vilar",
                "Porto"
        };
        for (int i = 0; i < patientNames.length; i++) {
            // create patients
            Patient patient = new Patient();
            patient.setName(patientNames[i]);
            patient.setAge(patientAges[i]);
            patient.setGuid("patient" + i);
            patient.setAddress(patientAddresses[i]);
            if (patientGenders[i] == 'm')
                patient.setPicture(R.drawable.male);
            else {
                patient.setPicture(R.drawable.female);
            }
            patient.save();

            /*
            // create Sessions for that patient
            for (int sess = 0; sess < numSessionsPerPacient; sess++) {
                Session session = new Session();
                session.setGuid("session" + sess + "-" + i);
                session.setDate(Session.dateToString(Session.createCustomDate(1999, 2, (sess + 1))));
                session.setPatient(patient);
                session.save();
            }
            */

        }
    }


}
