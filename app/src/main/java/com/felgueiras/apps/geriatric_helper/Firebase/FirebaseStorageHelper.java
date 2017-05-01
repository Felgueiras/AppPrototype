package com.felgueiras.apps.geriatric_helper.Firebase;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StartCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.StoppCriteria;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by felgueiras on 11/04/2017.
 */

public class FirebaseStorageHelper {

    private static FirebaseStorageHelper singleton = new FirebaseStorageHelper();
    private final FirebaseStorage storage;
    private final StorageReference patientsStorageReference;
    static String patientsFile = "patients.json";
    final Gson gson;


    public static FirebaseStorageHelper getInstance() {
        return singleton;
    }

    private FirebaseStorageHelper() {
        storage = FirebaseStorage.getInstance();
        patientsStorageReference = storage.getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                .child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + patientsFile);
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        gson = builder.create();
    }

    static Map<String, String> resourcesMap = new HashMap<>();


    public void downloadLanguageResources() {

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

    private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    private void printResources(Map<String, String> resources) {
        Log.d("XML", "Number of strings: " + resources.keySet().size());
    }

    public String getString(String name) {
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
     * @param firebaseTablePublic
     */
    public void downloadScales(final Context context, DatabaseReference firebaseTablePublic) {
        Log.d("Scales", "Downloading scales");

        // download scales from that language
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        final Gson gson = builder.create();

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        // clear the scales
        Scales.scales.clear();
        SharedPreferencesHelper.resetScales(context);
        FirebaseHelper.scalesCurrent = 0;

        // default system language
        final String displayLanguage = Locale.getDefault().getLanguage().toLowerCase();


        // fetch every existing scale
        firebaseTablePublic.child("scales").orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseHelper.scales.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ScaleMetadata scale = postSnapshot.getValue(ScaleMetadata.class);
                    scale.setKey(postSnapshot.getKey());
//                    Log.d("Scales", scale.getLanguages().size() + "");

                    final String scaleName = scale.getName();
                    String scaleLanguage = null;

                    // check if we have the scale version for this language
                    if (scale.getLanguages().contains(displayLanguage)) {
                        Log.d("Scales", "Language match " + scale.getName());
                        scaleLanguage = displayLanguage.toUpperCase();

                    } else {
                        Log.d("Scales", "Language mismatch " + scale.getName());
                        if (scale.getLanguages().size() == 1) {
                            // only one available language
                            scaleLanguage = scale.getLanguages().get(0).toUpperCase();
                        } else {
                            // multiple languages
                            if (scale.getLanguages().contains("en")) {
                                // english is available
                                scaleLanguage = "en";
                            } else {
                                // return first one
                                scaleLanguage = scale.getLanguages().get(0);
                            }

                        }
                    }
                    String fileName = scaleName + "-" + scaleLanguage + ".json";

                    StorageReference storageRef = storage.getReferenceFromUrl(FirebaseHelper.firebaseURL).child("scales/" + fileName);

                    try {
                        final File localFile = File.createTempFile("scale", "json");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    Log.d("Scales", "Downloaded " + scaleName);
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
                Log.d("Fetch", "Patients");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });

        // get system language
//        final String scaleLanguage = Locale.getDefault().getLanguage().toUpperCase();


    }


    /**
     * Download medical criteria.
     *
     * @param context
     */
    public void downloadCriteria(final Context context) {

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
    public void uploadScales() {

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


    public void uploadCriteria() {

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
//        StorageReference patientsStorageReference = storage.
//                getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
//                .child("criteria/" + fileName);
//
//        UploadTask uploadTask = patientsStorageReference.putBytes(jsonArray.getBytes());
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
//        patientsStorageReference = storage.
//                getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
//                .child("criteria/" + fileName);
//
//        uploadTask = patientsStorageReference.putBytes(jsonArray.getBytes());
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

    public ArrayList<StoppCriteria> getStoppCriteria() {
        return FirebaseHelper.stoppCriteria;
    }

    public ArrayList<StartCriteria> getStartCriteria() {
        return FirebaseHelper.startCriteria;
    }

    /**
     * Save patients on Firebase back-end as a ciphered JSON file.
     *
     * @param context
     */
    public void sendPatientsBackEnd(Context context) {
        // convert to JSON array

        String jsonArray = gson.toJson(PatientsManagement.getInstance().getPatients(context));

        // write JSON to file
        File patientsJSONFile = createPatientsJSONFile(context);

        try {
            PrintWriter out = new PrintWriter(patientsJSONFile);
            out.println(jsonArray);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // cipher file contents
        CipherDecipherFiles cip = CipherDecipherFiles.getInstance();
        cip.cipherFileContents(patientsJSONFile);

        // upload to Firebase
        InputStream stream = null;
        try {
            stream = new FileInputStream(patientsJSONFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        UploadTask uploadTask = patientsStorageReference.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Firebase", "Patients updated successfully");
            }
        });
    }


    private File createPatientsJSONFile(Context context) {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "patients";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".json",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file;
    }

    /**
     * Get patients from Firebase.
     *
     * @param context
     */
    public void getPatients(final Context context) {

        try {
            final File localFile = File.createTempFile("patients", "json");
            patientsStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Patients", "Downloaded patients");
                    // decipher file
                    CipherDecipherFiles cip = CipherDecipherFiles.getInstance();
                    cip.decipherFile(localFile);

                    // check contents of file
                    String content = null;
                    try {
                        content = new Scanner(localFile).useDelimiter("\\Z").next();
                        System.out.println(content);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // read file
                    ArrayList<PatientFirebase> patients = null;
                    try {
                        patients = new ArrayList<>(Arrays.asList(gson.fromJson(new FileReader(localFile), PatientFirebase[].class)));
                        Log.d("Patients", patients.size() + "");

                        // save to shared preferences
                        SharedPreferencesHelper.addInitialPatients(patients, context);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("Patients", "Patients file not found in Firebase");

                    SharedPreferencesHelper.addInitialPatients(new ArrayList<PatientFirebase>(), context);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}
