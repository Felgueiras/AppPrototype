package com.felgueiras.apps.geriatric_helper.Sessions.SingleArea;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class SessionNoteshandler{
    private Context context;
    private SessionFirebase session;

    public SessionNoteshandler(Activity context, SessionFirebase session) {
        this.context = context;
        this.session = session;
    }


    public void editNotes() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // get prompts.xml view
        View promptsView = LayoutInflater.from(context).inflate(R.layout.prompts, null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        if (session.getNotes()!=null)
            userInput.setText(session.getNotes());

        // set dialog message
        alertDialogBuilder
                .setTitle(R.string.scale_notes)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and save it as a note for the scale
                                session.setNotes(userInput.getText().toString());
                                FirebaseDatabaseHelper.updateSession(session);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

