package com.example.rafael.appprototype;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rafael on 08-10-2016.
 */
public class DatabaseGSONOps {


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
        Date[] patientBirthDates = new Date[]{
                DatesHandler.stringToDate("1920-12-01"),
                DatesHandler.stringToDate("1920-12-01"),
                DatesHandler.stringToDate("1920-12-01"),
                DatesHandler.stringToDate("1920-12-01"),
                DatesHandler.stringToDate("1920-12-01")
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

        for (int patient = 0; patient < patientNames.length; patient++) {
            patientNames[patient] = "Patient " + patient;
            patientBirthDates[patient] = DatesHandler.stringToDate("1920-12-01");
            patientAddresses[patient] = "Aveiro";
            patientGenders[patient] = 'm';
        }
        for (int i = 0; i < patientNames.length; i++) {
            // create patients
            Patient patient = new Patient();
            patient.setName(patientNames[i]);

            patient.setBirthDate(patientBirthDates[i]);
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


    /**
     * Save
     *
     * @param context
     */
    public static void saveDataGson(Context context) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Patients
        ArrayList<Patient> patients = Patient.getAllPatients();
        String jsonArray = gson.toJson(patients);
        writeToFile(jsonArray, context, Constants.filePatients);

        // Sessions
        ArrayList<Session> sessions = Session.getAllSessions();
        jsonArray = gson.toJson(sessions);
        writeToFile(jsonArray, context, Constants.fileSessions);

        // Scales
        ArrayList<GeriatricScale> scales = GeriatricScale.getAllScales();
        jsonArray = gson.toJson(scales);
        writeToFile(jsonArray, context, Constants.fileScales);

        // Questions
        ArrayList<Question> questions = Question.getAllQuestions();
        jsonArray = gson.toJson(questions);
        writeToFile(jsonArray, context, Constants.fileQuestions);

        // Choices
        ArrayList<Choice> choices = Choice.getAllChoices();
        jsonArray = gson.toJson(choices);
        writeToFile(jsonArray, context, Constants.fileChoices);
    }

    public static void readDataGson(Context context) {
        eraseAll();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Choices
        String choicesArray = readFromFile(context, Constants.fileChoices);
        ArrayList<Choice> choices = gson.fromJson(choicesArray,
                new TypeToken<ArrayList<Choice>>() {
                }.getType());
        for (Choice choice : choices) {
            choice.getQuestion().save();
            Log.d("GSON", choice.toString());
            choice.save();
        }


        // Questions
        String questionsArray = readFromFile(context, Constants.fileQuestions);
        ArrayList<Question> questions = gson.fromJson(questionsArray,
                new TypeToken<ArrayList<Question>>() {
                }.getType());
        for (Question question : questions) {
            question.getScale().save();
            Log.d("GSON", question.toString());
            question.save();
        }

        // Scales
        String scalesArray = readFromFile(context, Constants.fileScales);
        ArrayList<GeriatricScale> scales = gson.fromJson(scalesArray,
                new TypeToken<ArrayList<GeriatricScale>>() {
                }.getType());
        for (GeriatricScale scale : scales) {
            Log.d("GSON", scale.toString());
            scale.getSession().save();
            for (Question question : questions) {
                question.getScale().save();
                Log.d("GSON", question.toString());
                question.save();
            }
            scale.save();
        }

        // Patients
        String patientsArray = readFromFile(context, Constants.filePatients);
        ArrayList<Patient> patients = gson.fromJson(patientsArray,
                new TypeToken<ArrayList<Patient>>() {
                }.getType());
        for (Patient p : patients) {
            Log.d("GSON", p.toString());
            p.save();
        }

        // Sessions
        String sessionsArray = readFromFile(context, Constants.fileSessions);
        ArrayList<Session> sessions = gson.fromJson(sessionsArray,
                new TypeToken<ArrayList<Session>>() {
                }.getType());
        for (Session session : sessions) {
            // save patient from that session
            session.getPatient().save();
            for (GeriatricScale scale : session.getScalesFromSession()) {
                scale.save();
            }

            Log.d("GSON", session.toString());
            session.save();
        }

    }

    private static void writeToFile(String data, Context context, String file) {

        FileOutputStream fOut = null;

        try {
            fOut = context.openFileOutput(file,
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void appendToFile(String data, Context context, String file) {

        FileOutputStream fOut = null;

        try {
            fOut = context.openFileOutput(file,
                    MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(Context context, String file) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}