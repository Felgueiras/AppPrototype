package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.PatientsManagement;
import com.felgueiras.apps.geriatric_helper.R;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by felgueiras on 10/04/2017.
 */

public class SessionPDF {

    static int leftIndentation = 50;


    /**
     * Fonts.
     */
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private static final String LOG_TAG = "PDF";
    private static Context context;
    private static PatientFirebase patient;
    private EditText mSubjectEditText, mBodyEditText;
    private Button mSaveButton;
    private static File myFile;

    static SessionFirebase session;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public SessionPDF(SessionFirebase session) {
        this.session = session;
    }

    /**
     * Check if storage permissions were granted.
     *
     * @param activity
     * @return
     */
    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        } else {
            return true;
        }
    }

    /**
     * Handle request permission results.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
//                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
//                        takePicture();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(CAMERA)) {
//                                showMessageOKCancel("You need to allow access to both the permissions",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{CAMERA},
//                                                            REQUEST_CAMERA);
//                                                }
//                                            }
//                                        });
//                                return;
//                            }
//                        }
//                    }
                    }
                }
                break;
        }
    }


    /**
     * Cret
     *
     * @param activity
     * @param includePatientInfo
     */
    public void createSessionPdf(Activity activity, boolean includePatientInfo) {
        context = activity;
        if (includePatientInfo)
            patient = PatientsManagement.getInstance().getPatientFromSession(session, context);


        if (!verifyStoragePermissions(activity)) {
            // currently there are no permissions
            return;
        }

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdf");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss", Locale.UK).format(date);

        myFile = new File(pdfFolder + timeStamp + ".pdf");

        OutputStream output = null;
        try {
            output = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * Create PDF.
         */

        //Step 1
        Document document = new Document();

        //Step 2
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            addMetaData(document);
            addTitlePageSession(document, includePatientInfo);
            addScalesInfo(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        promptForNextAction();
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("Relatório AGG");
//        document.addSubject("Using iText");
//        document.addKeywords("Java, PDF, iText");
//        document.addAuthor("Lars Vogel");
//        document.addCreator("Lars Vogel");
    }

    /**
     * Add title page with infos about patient.
     *
     * @param document
     * @param includePatientInfo
     * @throws DocumentException
     */
    private static void addTitlePageSession(Document document, boolean includePatientInfo)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Relatório médico de sessão de Avaliação Geriátrica Global", catFont));
        addEmptyLine(preface, 1);

        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(DatesHandler.dateToStringWithHour(new Date(), false),
                smallBold));
        addEmptyLine(preface, 3);

        // session's notes
        if (session.getNotes() != null) {
            preface.add(new Paragraph("Notas da sessão: " + session.getNotes(),
                    smallBold));
            addEmptyLine(preface, 1);
        }


        // infos about Patient
        if (includePatientInfo) {
            preface.add(new Paragraph(
                    "Nome: " + patient.getName(),
                    smallBold));
            preface.add(new Paragraph(
                    "Data de nascimento: " + patient.getBirthDate(),
                    smallBold));
            preface.add(new Paragraph(
                    "Morada: " + patient.getAddress(),
                    smallBold));
            preface.add(new Paragraph(
                    "Processo nº: " + patient.getProcessNumber(),
                    smallBold));
        }


        document.add(preface);
        // Start a new page
//        document.newPage();pd
    }


    /**
     * Add scales section to the PDF.
     *
     * @param document
     * @throws DocumentException
     */
    private void addScalesInfo(Document document) throws DocumentException {
        Anchor anchor = new Anchor("Relatório", catFont);
        anchor.setName("Relatório");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);


        // represent data from each CGA area
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            // check if there are scales from this area

            String area = Constants.cga_areas[i];
            ArrayList<GeriatricScaleFirebase> scalesForArea = FirebaseDatabaseHelper.getScalesForArea(FirebaseDatabaseHelper.getScalesFromSession(session), area);
            if (scalesForArea.size() == 0) {
                continue;
            }

            // area
            Paragraph subPara = new Paragraph(area, subFont);
            Section subCatPart = catPart.addSection(subPara);
            // display tests/scales from that area
            for (int j = 0; j < Scales.getScalesForArea(area).size(); j++) {
                // search if there is an occurrence for this scale
                GeriatricScaleNonDB scale = Scales.getScalesForArea(area).get(j);

                for (GeriatricScaleFirebase scaleFirebase : scalesForArea) {
                    GradingNonDB match;
                    if (scaleFirebase.getScaleName().equals(scale.getScaleName())) {
                        Section scaleInfo = subCatPart.addSection(new Paragraph(scale.getScaleName()));
                        // present info - result (qualitative/quantitative)
                        if (patient != null) {
                            match = Scales.getGradingForScale(
                                    scaleFirebase,
                                    patient.getGender());
                        } else {
                            match = Scales.getGradingForScale(
                                    scaleFirebase,
                                    Constants.SESSION_GENDER);
                        }

                        // add notes
                        if (scaleFirebase.getNotes() != null) {
                            Paragraph qualitativeResult = new Paragraph("Notas: " + scaleFirebase.getNotes());
                            qualitativeResult.setIndentationLeft(leftIndentation);
                            scaleInfo.add(qualitativeResult);
                        }

                        if (match != null) {
                            Paragraph qualitativeResult = new Paragraph("Resultado qualitativo: " + match.getGrade());
                            qualitativeResult.setIndentationLeft(leftIndentation);
                            scaleInfo.add(qualitativeResult);
                        }

                        Paragraph quantitativeResult = new Paragraph("Resultado quantitativo: " + scaleFirebase.getResult() + "");
                        // paragraph.setAlignment(Element.ALIGN_LEFT);
                        quantitativeResult.setIndentationLeft(leftIndentation);
                        scaleInfo.add(quantitativeResult);

                        if (scaleFirebase.photos()) {
                            String photoPath = scaleFirebase.getPhotoPath();
                            if (photoPath != null) {
                                // add photoDownloaded
                                try {

                                    // load photoDownloaded from firebase
                                    FirebaseStorageHelper.fetchImageFirebasePDF(scaleFirebase, scaleInfo);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }
            }

        }

        // automatically go to new page
        document.add(catPart);
    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);
        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    /**
     * View pdf.
     */
    private static void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri ur = FileProvider.getUriForFile(context,
                "com.example.android.fileprovider",
                myFile);
//        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setDataAndType(ur, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    /**
     * Email pdf as a note.
     */
    private static void emailPDF() {
        Intent email = new Intent(Intent.ACTION_SEND);
//        email.putExtra(Intent.EXTRA_SUBJECT, mSubjectEditText.getText().toString());
//        email.putExtra(Intent.EXTRA_TEXT, mBodyEditText.getText().toString());
        Uri uri = FileProvider.getUriForFile(context,
                "com.example.android.fileprovider",
                myFile);
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        context.startActivity(email);
    }

    private static void printPDF() {
        Intent email = new Intent(Intent.ACTION_SEND);
//        email.putExtra(Intent.EXTRA_SUBJECT, mSubjectEditText.getText().toString());
//        email.putExtra(Intent.EXTRA_TEXT, mBodyEditText.getText().toString());
        Uri uri = FileProvider.getUriForFile(context,
                "com.example.android.fileprovider",
                myFile);
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        context.startActivity(email);
    }

    /**
     * Prompt the user what to do (view, print or email pdf)
     */
    private static void promptForNextAction() {
        final String[] options = {
                context.getString(R.string.label_preview),
                context.getString(R.string.label_email),
//                context.getString(R.string.label_print),
                context.getString(R.string.label_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.sessionPDF);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(context.getString(R.string.label_email))) {
                    emailPDF();
                } else if (options[which].equals(context.getString(R.string.label_preview))) {
                    viewPdf();
                } else if (options[which].equals(context.getString(R.string.label_print))) {
                    printPDF();
                } else if (options[which].equals(context.getString(R.string.label_cancel))) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }


//    // The definition of our task class
//    private class FirebaseLoadPhoto extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            FirebaseStorageHelper.fetchImageFirebasePDF();
////            FirebaseStorageHelper.downloadLanguageResources();
//            while (true) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (FirebaseHelper.canLeaveLaunchScreen)
//                    break;
//            }
//            return "All Done!";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            bar.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            bar.setVisibility(View.GONE);
//
//            // go to public area
//            Intent intent = new Intent(getBaseContext(), PublicAreaActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//
//


}
