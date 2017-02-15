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
import android.widget.TableLayout;
import android.widget.TableRow;
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
    private int paddingValue;
    private int background;


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
                /*
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(finalCurrentTest.getTestName());
                alertDialog.setMessage(finalCurrentTest.getDescription());
                alertDialog.show();
                */

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.scale_info, null);
                dialogBuilder.setView(dialogView);

                TextView scaleDescription = (TextView) dialogView.findViewById(R.id.scale_description);
                scaleDescription.setText(finalCurrentTest.getDescription());

                // create table with classification for this scale
                TableLayout table = (TableLayout) dialogView.findViewById(R.id.scale_outcomes);
                GeriatricTestNonDB test = Scales.getTestByName(finalCurrentTest.getTestName());
                if (!test.getScoring().isDifferentMenWomen()) {
                    addTableHeader(table, false);

                    // add content
                    ArrayList<GradingNonDB> gradings = test.getScoring().getValuesBoth();
                    for (GradingNonDB grading : gradings) {
                        TableRow row = new TableRow(context);
                        TextView grade = new TextView(context);
                        grade.setBackgroundResource(background);
                        grade.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                        TextView score = new TextView(context);
                        score.setBackgroundResource(background);
                        score.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                        //TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        grade.setText(grading.getGrade());
                        grade.setLayoutParams(new TableRow.LayoutParams(1));
                        if (grading.getMin() != grading.getMax() && grading.getMax() > grading.getMin())
                            score.setText(grading.getMin() + "-" + grading.getMax());
                        else
                            score.setText(grading.getMin() + "");
                        score.setLayoutParams(new TableRow.LayoutParams(2));
                        row.addView(grade);
                        row.addView(score);
                        table.addView(row);
                    }
                } else {
                    addTableHeader(table, true);

                    // show values for men and women
                    ArrayList<GradingNonDB> gradings = test.getScoring().getValuesBoth();
                    for (int i = 0; i < test.getScoring().getValuesMen().size(); i++) {
                        GradingNonDB gradingMen = test.getScoring().getValuesMen().get(i);
                        GradingNonDB gradingWomen = test.getScoring().getValuesWomen().get(i);
                        TableRow row = new TableRow(context);
                        TextView grade = new TextView(context);
                        grade.setBackgroundResource(background);
                        grade.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                        TextView scoreMen = new TextView(context);
                        scoreMen.setBackgroundResource(background);
                        scoreMen.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                        TextView scoreWomen = new TextView(context);
                        scoreWomen.setBackgroundResource(background);
                        scoreWomen.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                        //TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        grade.setText(gradingMen.getGrade());
                        grade.setLayoutParams(new TableRow.LayoutParams(1));
                        // men
                        if (gradingMen.getMin() != gradingMen.getMax() && gradingMen.getMax() > gradingMen.getMin())
                            scoreMen.setText(gradingMen.getMin() + "-" + gradingMen.getMax());
                        else
                            scoreMen.setText(gradingMen.getMin() + "");
                        scoreMen.setLayoutParams(new TableRow.LayoutParams(2));
                        // women
                        if (gradingWomen.getMin() != gradingWomen.getMax() && gradingWomen.getMax() > gradingWomen.getMin())
                            scoreWomen.setText(gradingWomen.getMin() + "-" + gradingWomen.getMax());
                        else
                            scoreWomen.setText(gradingWomen.getMin() + "");
                        scoreWomen.setLayoutParams(new TableRow.LayoutParams(3));
                        row.addView(grade);
                        row.addView(scoreMen);
                        row.addView(scoreWomen);
                        table.addView(row);
                    }
                }


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.setTitle(finalCurrentTest.getTestName());
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

    /**
     * Add a header to the table.
     *
     * @param table
     * @param differentMenWomen
     */
    private void addTableHeader(TableLayout table, boolean differentMenWomen) {
        paddingValue = 5;
        background = R.drawable.cell_shape;
        if (!differentMenWomen) {
            // add header
            TableRow header = new TableRow(context);
            TextView result = new TextView(context);
            result.setBackgroundResource(background);
            result.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            TextView points = new TextView(context);
            points.setBackgroundResource(background);
            points.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            result.setText("Resultado");
            points.setText("Pontuação");
            result.setLayoutParams(new TableRow.LayoutParams(1));
            points.setLayoutParams(new TableRow.LayoutParams(2));
            header.addView(result);
            header.addView(points);
            table.addView(header);
        } else {
            TableRow header = new TableRow(context);
            TextView result = new TextView(context);
            result.setBackgroundResource(background);
            result.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            TextView men = new TextView(context);
            men.setBackgroundResource(background);
            men.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            TextView women = new TextView(context);
            women.setBackgroundResource(background);
            women.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            result.setText("Resultado");
            men.setText("Homem");
            women.setText("Mulher");
            result.setLayoutParams(new TableRow.LayoutParams(1));
            men.setLayoutParams(new TableRow.LayoutParams(2));
            women.setLayoutParams(new TableRow.LayoutParams(3));
            header.addView(result);
            header.addView(men);
            header.addView(women);
            table.addView(header);
        }

    }

    @Override
    public int getItemCount() {
        // get the number of tests that exist for this area
        testsForArea = Scales.getTestsForArea(area);
        return testsForArea.size();
    }

}
