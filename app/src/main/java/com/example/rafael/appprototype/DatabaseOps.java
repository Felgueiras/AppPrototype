package com.example.rafael.appprototype;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.DataTypes.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 08-10-2016.
 */
public class DatabaseOps {


    /**
     * Erase all data from the DB.
     */
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
                ////system.out.println("Table - " + tableName);
                db.execSQL("DELETE FROM " + tableName);
            }
        }

    }

    /**
     * Insert dummy data into DB
     */
    public static void insertDataToDB() {
        int numSessionsPerPacient = 0;

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

        /*
        int numPatients = 5;
        String[] patientNames = new String[numPatients];
        int[] patientAges = new int[numPatients];
        String[] patientAddresses = new String[numPatients];
        double[] patientGenders = new double[numPatients];
        */

        for(int patient=0; patient < patientNames.length; patient++)
        {
            patientNames[patient] = "Patient " + patient;
            patientAges[patient] = 80;
            patientAddresses[patient] = "@home";
            patientGenders[patient] = 'm';

        }
        for (int i = 0; i < patientNames.length; i++) {
            // create patients
            Patient patient = new Patient();
            patient.setName(patientNames[i]);

            patient.setAge(patientAges[i]);
            patient.setGuid("patient" + i);
            patient.setAddress(patientAddresses[i]);
            patient.setGender(Constants.MALE);
            if (patientGenders[i] == 'm')
                patient.setPicture(R.drawable.male);
            else {
                patient.setPicture(R.drawable.female);
            }
            patient.setFavorite(false);
            patient.save();
        }
    }


}
