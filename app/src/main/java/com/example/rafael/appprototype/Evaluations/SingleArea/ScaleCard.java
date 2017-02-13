package com.example.rafael.appprototype.Evaluations.SingleArea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Evaluations.DisplayTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class ScaleCard extends RecyclerView.Adapter<ScaleCard.TestCardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    private final int patientGender;
    /**
     * CGA area.
     */
    private final String area;

    private Activity context;
    private ArrayList<GeriatricTestNonDB> testsForArea;
    private ViewManager parentView;


    /**
     * Create a View
     */
    public class TestCardHolder extends RecyclerView.ViewHolder implements Serializable {
        private final TextView result_quantitative;
        private final TextView notes;
        public TextView name;
        public ImageButton description;
        public TextView result_qualitative;
        public View view;
        public ImageButton addNotesButton;

        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            description = (ImageButton) view.findViewById(R.id.scale_info);
            result_qualitative = (TextView) view.findViewById(R.id.result_qualitative);
            result_quantitative = (TextView) view.findViewById(R.id.result_quantitative);
            addNotesButton = (ImageButton) view.findViewById(R.id.addNotes);
            notes = (TextView) view.findViewById(R.id.testNotes);
            this.view = view;
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context       current Context
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     * @param area
     */
    public ScaleCard(Activity context, Session session, boolean resuming, int patientGender, String area) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
        this.area = area;
    }

    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
        return new TestCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
        // get constants
        parentView = (ViewManager) holder.result_qualitative.getParent();

        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);
        String testCompletionResult = context.getResources().getString(R.string.test_result);

        // access a given Test from the DB
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        // get by area
        GeriatricTest currentTest = null;
        for (GeriatricTest test : testsFromSession) {
            if (test.getShortName().equals(testsForArea.get(position).getShortName())) {
                currentTest = test;
                break;
            }
        }
        holder.name.setText(testsForArea.get(position).getShortName());

        final GeriatricTest finalCurrentTest = currentTest;

        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(finalCurrentTest.getTestName());
                alertDialog.setMessage(finalCurrentTest.getDescription());
                alertDialog.show();
            }
        });

        // fill the View
        //holder.cgaCompletion.setText(testCompletionNotSelected);
        /**
         Alpha value when test is unselected
         **/
        /*
        float unselected = 0.5f;
        holder.view.setAlpha(unselected);
        */
        // Test was already opened


        if (currentTest.isAlreadyOpened()) {
            float selected = 1f;
            holder.view.setAlpha(selected);
            // already complete
            if (currentTest.isCompleted()) {
                // display Scoring to the user
                System.out.println("Gender is " + patientGender);
                GradingNonDB match = Scales.getGradingForTest(
                        currentTest,
                        patientGender);
                // qualitative result
                holder.result_qualitative.setText(match.getGrade());
                // quantitative result
                String quantitative = "";
                quantitative += currentTest.getResult();
                GeriatricTestNonDB testNonDB = Scales.getTestByName(currentTest.getTestName());
                if (!testNonDB.getScoring().isDifferentMenWomen()) {
                    quantitative += " (" + testNonDB.getScoring().getMinScore();
                    quantitative += "-" + testNonDB.getScoring().getMaxScore() + ")";
                } else {
                    if (patientGender == Constants.MALE) {
                        quantitative += " (" + testNonDB.getScoring().getMinMen();
                        quantitative += "-" + testNonDB.getScoring().getMaxMen() + ")";
                    }
                }
                holder.result_quantitative.setText(quantitative);
            }
            // still incomplete
            else {
                holder.result_qualitative.setText(testCompletionSelectedIncomplete);
                parentView.removeView(holder.result_quantitative);
                parentView.removeView(holder.addNotesButton);
                parentView.removeView(holder.notes);
            }

        } else {
            parentView.removeView(holder.result_qualitative);
            parentView.removeView(holder.result_quantitative);
            parentView.removeView(holder.addNotesButton);
            parentView.removeView(holder.notes);
        }


        if (currentTest.hasNotes()) {
            parentView.removeView(holder.addNotesButton);
            holder.notes.setText(currentTest.getNotes());
        } else {
            parentView.removeView(holder.notes);
        }

        /**
         * Edit the notes by using an AlertDialog.
         */
        View.OnClickListener editNotesDialog = new View.OnClickListener() {
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
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and save it as a note for the scale
                                        finalCurrentTest.setNotes(userInput.getText().toString());
                                        finalCurrentTest.save();
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
        };

        holder.addNotesButton.setOnClickListener(editNotesDialog);
        holder.notes.setOnClickListener(editNotesDialog);

        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Test", "Going to open");
                String selectedTestName = finalCurrentTest.getTestName();

                // Create new fragment and transaction
                Fragment newFragment = new DisplaySingleTestFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(DisplaySingleTestFragment.testObject, Scales.getTestByName(selectedTestName));
                bundle.putSerializable(DisplaySingleTestFragment.testDBobject, finalCurrentTest);
                bundle.putSerializable(DisplaySingleTestFragment.CGA_AREA, area);
                bundle.putSerializable(DisplaySingleTestFragment.patient, session.getPatient());
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_session_scale).commit();
            }
        });

        /**
         * Add a listener for when a note is added.
         */
        /*
        holder.addNotesButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentTest.setNotes(charSequence.toString());
                currentTest.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        */
    }

    @Override
    public int getItemCount() {
        // get the number of tests that exist for this area
        testsForArea = Scales.getTestsForArea(area);
        return testsForArea.size();
    }

}
