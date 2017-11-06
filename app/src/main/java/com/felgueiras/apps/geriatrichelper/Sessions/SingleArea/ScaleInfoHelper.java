package com.felgueiras.apps.geriatrichelper.Sessions.SingleArea;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.Scales;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class ScaleInfoHelper implements View.OnClickListener {


    private Activity context;
    private GeriatricScaleNonDB currentScale;
    private int background = R.drawable.cell_shape;
    private int paddingValue = 5;


    public ScaleInfoHelper(Activity context, GeriatricScaleNonDB currentScale) {
        this.context = context;
        this.currentScale = currentScale;
    }

    public ScaleInfoHelper(Activity context) {
        this.context = context;
    }

    /**
     * Display scale's info.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.scale_info, null);
        dialogBuilder.setView(dialogView);

        TextView scaleDescription = dialogView.findViewById(R.id.scale_description);
        scaleDescription.setText(currentScale.getDescription());

        // create table
        TableLayout table = dialogView.findViewById(R.id.scale_outcomes);
        GeriatricScaleNonDB test = Scales.getScaleByName(currentScale.getScaleName());

//        if (currentScale.getScaleName().equals(Constants.test_name_mini_mental_state)) {
        // Mini mental scale displays education levels and not different scorings
        fillTableScale(test, table, currentScale.getScoring().getName());

//        } else {
//            // create table with classification for this scale
//            fillTableScale(test, table, false);
//        }

        AlertDialog alertDialog = dialogBuilder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).create();
        alertDialog.setTitle(currentScale.getScaleName());
        alertDialog.show();
    }


    /**
     * Create table for scorings.
     *
     * @param test
     * @param table
     * @param scoringName
     */
    public void fillTableScale(GeriatricScaleNonDB test, TableLayout table, String scoringName) {
        if (test.getScoring() != null) {
            if (!test.getScoring().isDifferentMenWomen()) {
                addTableHeader(table, false, scoringName);

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
                    score.setGravity(Gravity.CENTER);
                    row.addView(score);
                    table.addView(row);
                }
            } else {
                addTableHeader(table, true, scoringName);

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
                    scoreMen.setGravity(Gravity.CENTER);

                    row.addView(scoreMen);
                    scoreWomen.setGravity(Gravity.CENTER);

                    row.addView(scoreWomen);
                    table.addView(row);
                }
            }
        }
    }

    /**
     * Add a header to the table.
     *
     * @param table
     * @param differentMenWomen
     * @param scoringName
     */
    public void addTableHeader(TableLayout table, boolean differentMenWomen, String scoringName) {

        if (!differentMenWomen) {
            // add header
            TableRow header = new TableRow(context);
            TextView result = new TextView(context);
            result.setBackgroundResource(background);
            result.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            TextView points = new TextView(context);
            points.setBackgroundResource(background);
            points.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            if (scoringName != null && !scoringName.equals("")) {
                result.setText(scoringName);
            } else {
                // default scoring name
                result.setText("Resultado");
            }
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
            if (scoringName != null && !scoringName.equals("")) {
                result.setText(scoringName);
            } else {
                // default scoring name
                result.setText("Resultado");
            }
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
}

