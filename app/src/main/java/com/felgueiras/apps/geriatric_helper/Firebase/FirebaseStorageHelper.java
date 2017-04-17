package com.felgueiras.apps.geriatric_helper.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by felgueiras on 11/04/2017.
 */

public class FirebaseStorageHelper {

    static Map<String, String> resourcesMap = new HashMap<>();


    public static void downloadLanguageResources() {

        // get system language
//        final String displayLanguage = Locale.getDefault().getLanguage().toUpperCase();
        final String displayLanguage = "pt";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String fileName = "strings-" + displayLanguage + ".xml";

        StorageReference storageRef = storage.getReferenceFromUrl(FirebaseHelper.firebaseURL).child("strings/" + fileName);

        try {
            final File localFile = File.createTempFile("strings", "xml");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    XmlPullParserFactory pullParserFactory = null;
                    XmlPullParser parser;
                    try {
                        pullParserFactory = XmlPullParserFactory.newInstance();

                        parser = pullParserFactory.newPullParser();

                        InputStream in_s = new FileInputStream(localFile);
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        parser.setInput(in_s, null);

                        parseXML(parser);
                    } catch (XmlPullParserException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof com.google.firebase.storage.StorageException) {
                        Log.d("Download", "No language resource");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
//        }
    }

    private static void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
//                    resources = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
//                    Log.d("XML","starter tag -> " + parser.getAttributeCount());
                    if (name.equals("string")) {
//                        Log.d("XML", "starter tag -> " + parser.getAttributeValue(0));
//                        Log.d("XML", "text --> " + parser.nextText());
                        resourcesMap.put(parser.getAttributeValue(0), parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
//                    if (name.equalsIgnoreCase("string") && currentString != null) {
//                        resources.add(currentString);
//                    }
            }
            eventType = parser.next();
        }

        printResources(resourcesMap);
    }

    private static void printResources(Map<String, String> resources) {
        Log.d("XML", "Number of strings: " + resources.keySet().size());
    }

    public static String getString(String name) {
        if (resourcesMap.keySet().contains(name)) {
            return resourcesMap.get(name);
        } else {
            return "";
        }
    }

    /**
     * Download all scales.
     *
     * @param context
     */
    public static void downloadScales(final Context context) {

        // get system language
//        final String scaleLanguage = Locale.getDefault().getLanguage().toUpperCase();

        // download scales from that language
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        final Gson gson = builder.create();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // clear the scales
        Scales.scales.clear();
        SharedPreferencesHelper.resetScales(context);
        FirebaseHelper.scalesCurrent = 0;

        for (int i = 0; i < FirebaseHelper.scalesNames.length; i++) {
            final String scaleName = FirebaseHelper.scalesNames[i];
            final String scaleLanguage = FirebaseHelper.scalesLanguages[i];
            String fileName = scaleName + "-" + scaleLanguage + ".json";

            StorageReference storageRef = storage.getReferenceFromUrl(FirebaseHelper.firebaseURL).child("scales/" + fileName);

            try {
                final File localFile = File.createTempFile("scale", "json");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            GeriatricScaleNonDB scaleNonDB = gson.fromJson(new FileReader(localFile), GeriatricScaleNonDB.class);
                            // save to shared preferences
                            SharedPreferencesHelper.addScale(scaleNonDB, context);
                            Scales.scales.add(scaleNonDB);
                            FirebaseHelper.scalesCurrent++;
                            if (FirebaseHelper.scalesCurrent == FirebaseHelper.scalesTotal)
                                FirebaseHelper.canLeaveLaunchScreen = true;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (exception instanceof com.google.firebase.storage.StorageException) {
                            // scale was not found for that language
                            Log.d("Download", "Scale " + scaleName + " does not exist for " + scaleLanguage + " language");
                        }
                        FirebaseHelper.scalesCurrent++;
                        if (FirebaseHelper.scalesCurrent == FirebaseHelper.scalesTotal)
                            FirebaseHelper.canLeaveLaunchScreen = true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    /**
     * Download medical criteria.
     *
     * @param context
     */
    public static void downloadCriteria(final Context context) {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        final Gson gson = builder.create();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // clear the criteria
        FirebaseHelper.startCriteria.clear();
        FirebaseHelper.stoppCriteria.clear();
        SharedPreferencesHelper.resetCriteria(context);

        // download start criteria
        String fileName = "start.json";
        StorageReference storageRef = storage.getReferenceFromUrl(FirebaseHelper.firebaseURL).child("criteria/" + fileName);

        try {
            final File localFile = File.createTempFile("criteria", "json");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        StartCriteria[] criteria = gson.fromJson(new FileReader(localFile), StartCriteria[].class);
                        // save to shared preferences
                        for (StartCriteria cr : criteria) {
                            SharedPreferencesHelper.addStartCriteria(cr, context);
                            FirebaseHelper.startCriteria.add(cr);
                        }
                        Log.d("Criteria", "Downloaded start criteria");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof com.google.firebase.storage.StorageException) {
                        // scale was not found for that language
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

        // download stopp criteria
        fileName = "stopp.json";
        storageRef = storage.getReferenceFromUrl(FirebaseHelper.firebaseURL).child("criteria/" + fileName);

        try {
            final File localFile = File.createTempFile("criteria", "json");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        StoppCriteria[] criteria = gson.fromJson(new FileReader(localFile), StoppCriteria[].class);
                        // save to shared preferences
                        for (StoppCriteria cr : criteria) {
                            SharedPreferencesHelper.addStoppCriteria(cr, context);
                            FirebaseHelper.stoppCriteria.add(cr);
                        }
                        Log.d("Criteria", "Downloaded stopp criteria");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof com.google.firebase.storage.StorageException) {
                        // scale was not found for that language
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * JSON handling.
     */
    public static void uploadScales() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();


        for (int i = 0; i < FirebaseHelper.scalesNames.length; i++) {
            // scale name
            String scaleName = FirebaseHelper.scalesNames[i];
            String scaleLanguage = FirebaseHelper.scalesLanguages[i];
            String jsonArray = gson.toJson(Scales.getScaleByName(scaleName));

            String fileName = scaleName + "-" + scaleLanguage + ".json";
            // upload file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.
                    getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("scales/" + fileName);

            UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        }


    }



    public static void uploadCriteria() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        Gson gson = builder.create();

        // upload Beers criteria
        String jsonArray = gson.toJson(StartCriteria.getStartCriteria());

        String fileName = "start.json";
        // upload file
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.
                getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                .child("criteria/" + fileName);

        UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Criteria", "Uploaded start criteria");
            }
        });


//        // upload Start criteria
//        String jsonArray = gson.toJson(StartCriteria.getStartCriteria());
//
//        String fileName = "start.json";
//        // upload file
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage.
//                getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
//                .child("criteria/" + fileName);
//
//        UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d("Criteria", "Uploaded start criteria");
//            }
//        });
//
//        // upload Stopp criteria
//        jsonArray = gson.toJson(StoppCriteria.getStoppCriteria());
//
//        fileName = "stopp.json";
//        // upload file
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.
//                getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
//                .child("criteria/" + fileName);
//
//        uploadTask = storageReference.putBytes(jsonArray.getBytes());
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Log.d("Criteria", "Uploaded stopp criteria");
//            }
//        });


    }

    public static ArrayList<StoppCriteria> getStoppCriteria() {
        return FirebaseHelper.stoppCriteria;
    }

    public static ArrayList<StartCriteria> getStartCriteria() {
        return FirebaseHelper.startCriteria;
    }
}
