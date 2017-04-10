package com.felgueiras.apps.geriatric_helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Choice;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.GeriatricScale;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Question;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
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
import java.util.Random;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

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
                db.execSQL("DELETE FROM " + tableName);
            }
        }

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
            // save PATIENT from that session
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
