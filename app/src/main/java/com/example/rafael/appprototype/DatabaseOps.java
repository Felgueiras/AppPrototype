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
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
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


    /**
     * Insert dummy data into DB
     */
    public static void insertDummyData() {

        Log.d("Dummy", "Inserting dummy data");

        int totalPatients = 30;
        String[] patientsMale =
                {"Manuel Vieira",
                        "Ivo Silva",
                        "Júlio Arrábida",
                        "Manuel João Costa",
                        "Vitor Manuel da Assunção",
                        "José Pinto",
                        "Teotónio Francisco",
                        "Fernando Silva",
                        "Augusto Cunha",
                        "Óscar Felgueiras",
                        "Vítor de Sá",
                        "Leandro Ricardo",
                        "Mário Alexandre Costa",
                        "Aníbal Salgueiro",
                        "Bernardo Silva"};
        String[] patientsFemale =
                {"Maria da Graça",
                        "Maria Silva",
                        "Júlia Adalberto",
                        "Beatriz Fernandes Madureira",
                        "Emília Felgueiras Pinto",
                        "Maria Adélia Cirne",
                        "Ana Inês Gonçalves",
                        "Clara Ferreira",
                        "Ana Maria Pureza",
                        "Leonor Fernandes",
                        "Lurdes Onofre Seixas",
                        "Teresa Silves",
                        "Paula Faria",
                        "Leonor da Graça Gomes",
                        "Beatriz Flores"};

        String[] patientNames = new String[totalPatients];
        int[] patientAges = new int[totalPatients];
        String[] patientAddresses = new String[totalPatients];
        double[] patientGenders = new double[totalPatients];
        Date[] patientBirthDates = new Date[totalPatients];


        String[] addresses = {
                "Aveiro",
                "Eixo",
                "Esgueira",
                "Ílhavo",
                "Estarreja",
                "Cacia",
                "Azurva",
                "Salreu"
        };

        Random random = new Random();

        /**
         * Male patients.
         */
        for (String aPatientsMale : patientsMale) {
            // create patients
            Patient patient = new Patient();
            patient.setName(aPatientsMale);
            patient.setProcessNumber(random.nextInt(1000)+"");
            patient.setBirthDate(DatesHandler.stringToDate("01-12-1920"));
            patient.setGuid("PATIENT-" + aPatientsMale);
            patient.setAddress(addresses[random.nextInt(addresses.length)]);
            patient.setPicture(R.drawable.male);
            patient.setGender(Constants.MALE);
            patient.setFavorite(false);
            patient.save();
        }
        /**
         * Female patients.
         */
        for (String aPatientsFemale : patientsFemale) {
            // create patients
            Patient patient = new Patient();
            patient.setName(aPatientsFemale);
            patient.setProcessNumber(random.nextInt(1000)+"");
            patient.setBirthDate(DatesHandler.stringToDate("01-12-1920"));
            patient.setGuid("PATIENT-" + aPatientsFemale);
            patient.setAddress(addresses[random.nextInt(addresses.length)]);
            patient.setPicture(R.drawable.female);
            patient.setGender(Constants.FEMALE);
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

    public static void displayData(Context context) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        // Patients
        ArrayList<Patient> patients = Patient.getAllPatients();
        String jsonArray = gson.toJson(patients);
        System.out.println(jsonArray);

        // Sessions
        ArrayList<Session> sessions = Session.getAllSessions();
        jsonArray = gson.toJson(sessions);
        System.out.println(jsonArray);


        // Scales
        ArrayList<GeriatricScale> scales = GeriatricScale.getAllScales();
        jsonArray = gson.toJson(scales);
        System.out.println(jsonArray);


        // Questions
        ArrayList<Question> questions = Question.getAllQuestions();
        jsonArray = gson.toJson(questions);
        System.out.println(jsonArray);


        // Choices
        ArrayList<Choice> choices = Choice.getAllChoices();
        jsonArray = gson.toJson(choices);
        System.out.println(jsonArray);
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
