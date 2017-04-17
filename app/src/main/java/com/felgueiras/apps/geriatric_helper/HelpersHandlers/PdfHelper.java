package com.felgueiras.apps.geriatric_helper.HelpersHandlers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by felgueiras on 10/04/2017.
 */

public class PdfHelper {

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

    public static boolean verifyStoragePermissions(Activity activity) {
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


    public static void createSamplePdf(Activity activity) {
        context = activity;

        verifyStoragePermissions(activity);

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

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
            addTitlePage(document);
            addContent(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        /**
         *
         */

//        promptForNextAction();
        viewPdf();


    }


    public static void createSessionPdf(Activity activity, SessionFirebase sess) {
        context = activity;
        session = sess;
        patient = FirebaseDatabaseHelper.getPatientFromSession(session);


        if (!verifyStoragePermissions(activity)) {
            // currently there are no permissions
            return;
        }

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

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
            addTitlePageSession(document);
            addContentSession(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        /**
         *
         */

//        promptForNextAction();
        viewPdf();


    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addTitlePageSession(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Relatório médico de sessão de Avaliação Geriátrica Global", catFont));
        addEmptyLine(preface, 1);

        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                new Date() + "",
                smallBold));
        addEmptyLine(preface, 3);

        // infos about Patient
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

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void addContentSession(Document document) throws DocumentException {
        Anchor anchor = new Anchor("Relatório", catFont);
        anchor.setName("Relatório");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        // represent data from each CGA area
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            String area = Constants.cga_areas[i];
            ArrayList<GeriatricScaleFirebase> scalesForArea = FirebaseDatabaseHelper.getScalesForArea(FirebaseDatabaseHelper.getScalesFromSession(session), area);
            // area
            Paragraph subPara = new Paragraph(area, subFont);
            Section subCatPart = catPart.addSection(subPara);
            // display tests/scales from that area
            for (int j = 0; j < Scales.getScalesForArea(area).size(); j++) {
                GeriatricScaleNonDB scale = Scales.getScalesForArea(area).get(j);
//                subCatPart.add();
                Section scaleInfo = subCatPart.addSection(new Paragraph(scale.getScaleName()));

                for (GeriatricScaleFirebase scaleFirebase : scalesForArea) {
                    GradingNonDB match;
                    if (scaleFirebase.getScaleName().equals(scale.getScaleName())) {
                        // present info - result (qualitative/quantitative)
                        match = Scales.getGradingForScale(
                                scaleFirebase,
                                patient.getGender());

                        if (match != null) {
                            Paragraph qualitativeResult = new Paragraph("Resultado qualitativo: " + match.getGrade());
                            qualitativeResult.setIndentationLeft(leftIndentation);
                            scaleInfo.add(qualitativeResult);
                        }

                        Paragraph quantitativeResult = new Paragraph("Resultado quantitativo: " + scaleFirebase.getResult() + "");
                        // paragraph.setAlignment(Element.ALIGN_LEFT);
                        quantitativeResult.setIndentationLeft(leftIndentation);
                        scaleInfo.add(quantitativeResult);
                    }
                }
            }

        }


//        // add a list
//        createList(subCatPart);
//        Paragraph paragraph = new Paragraph();
//        addEmptyLine(paragraph, 5);
//        subCatPart.add(paragraph);
//
//        // add a table
//        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

//        subPara = new Paragraph("Subcategory", subFont);
//        subCatPart = catPart.addSection(subPara);
//        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
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
    private void emailNote() {
        // TODO email PDF
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT, mSubjectEditText.getText().toString());
        email.putExtra(Intent.EXTRA_TEXT, mBodyEditText.getText().toString());
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        context.startActivity(email);
    }

    private void promptForNextAction() {
        final String[] options = {context.getString(R.string.label_email),
                context.getString(R.string.label_preview),
                context.getString(R.string.label_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Note Saved, What Next?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(context.getString(R.string.label_email))) {
                    emailNote();
                } else if (options[which].equals(context.getString(R.string.label_preview))) {
                    viewPdf();
                } else if (options[which].equals(context.getString(R.string.label_cancel))) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }
}
