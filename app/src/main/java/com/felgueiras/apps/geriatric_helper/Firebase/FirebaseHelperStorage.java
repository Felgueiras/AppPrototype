package com.felgueiras.apps.geriatric_helper.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by felgueiras on 11/04/2017.
 */

public class FirebaseHelperStorage {

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

}
