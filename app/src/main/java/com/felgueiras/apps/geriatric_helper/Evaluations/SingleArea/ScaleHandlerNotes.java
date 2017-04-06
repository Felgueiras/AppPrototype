package com.felgueiras.apps.geriatric_helper.Evaluations.SingleArea;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.EditText;

import com.felgueiras.apps.geriatric_helper.DataTypes.DB.GeriatricScale;
import com.felgueiras.apps.geriatric_helper.R;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class ScaleHandlerNotes implements View.OnClickListener {
    private Context context;
    private GeriatricScale finalCurrentTest;
    private ScaleCard.ScaleCardHolder holder;
    private ViewManager parentView;

    public ScaleHandlerNotes(Activity context, GeriatricScale finalCurrentTest, ScaleCard.ScaleCardHolder holder, ViewManager parentView) {
        this.context = context;
        this.finalCurrentTest = finalCurrentTest;
        this.holder = holder;
        this.parentView = parentView;
    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // get prompts.xml view
        View promptsView = LayoutInflater.from(context).inflate(R.layout.prompts, null);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        if (finalCurrentTest.hasNotes())
            userInput.setText(finalCurrentTest.getNotes());

        // set dialog message
        alertDialogBuilder
                .setTitle(R.string.scale_notes)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and save it as a note for the scale
                                finalCurrentTest.setNotes(userInput.getText().toString());
                                finalCurrentTest.save();
                                parentView.removeView(holder.addNotesButton);
                                holder.notes.setText(finalCurrentTest.getNotes());
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

