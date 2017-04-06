package com.felgueiras.apps.geriatric_helper.Patients.Progress.Table;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.GeriatricScale;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class ProgressTableHelper {

    private int background = R.drawable.cell_shape;
    private int paddingValue = 5;

    /**
     * @param table
     * @param scaleInstances
     * @param scale
     * @param context
     * @param patient
     */
    public void buildTable(TableLayout table,
                           ArrayList<GeriatricScale> scaleInstances,
                           GeriatricScaleNonDB scale,
                           Context context, Patient patient) {
        // order scale instances by date
        Collections.sort(scaleInstances, new Comparator<GeriatricScale>() {
            @Override
            public int compare(GeriatricScale first, GeriatricScale second) {
                Date firstDate = first.getSession().getDate();
                Date secondDate = second.getSession().getDate();
                if (firstDate.after(secondDate)) {
                    return 1;
                } else if (firstDate.before(secondDate)) {
                    return -1;
                } else
                    return 0;
            }
        });

        // create axis information
        // x axis - date
        ArrayList<Date> dates = new ArrayList<>();
        // y axis - score
        for (GeriatricScale t : scaleInstances) {
            Date date = t.getSession().getDate();
            dates.add(date);
        }

        // set date label formatter
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        /**
         * Build top row - dummy + days.
         */
        TableRow row = new TableRow(context);
        TextView dummy = new TextView(context);
        dummy.setBackgroundResource(background);
        dummy.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
        dummy.setText("Header");
        dummy.setLayoutParams(new TableRow.LayoutParams(1));
        row.addView(dummy);
        for (Date sessionDate : dates) {
            TextView dateTextView = new TextView(context);
            dateTextView.setBackgroundResource(background);
            dateTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            dateTextView.setText(DatesHandler.dateToStringDayMonthYear(sessionDate));
            dateTextView.setLayoutParams(new TableRow.LayoutParams(1));
            row.addView(dateTextView);
        }
        table.addView(row);

        /**
         * Other rows - for each score, days that had that result
         */
        ArrayList<GradingNonDB> valuesToConsider;
        if (scale.getScoring().isDifferentMenWomen()) {
            if (patient.getGender() == Constants.MALE)
                valuesToConsider = scale.getScoring().getValuesMen();
            else
                valuesToConsider = scale.getScoring().getValuesWomen();
        } else {
            valuesToConsider = scale.getScoring().getValuesBoth();
        }


        for (GradingNonDB grading : valuesToConsider) {
            row = new TableRow(context);
            dummy = new TextView(context);
            dummy.setBackgroundResource(background);
            dummy.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            String currentGrade = grading.getGrade();
            dummy.setText(currentGrade);
            dummy.setLayoutParams(new TableRow.LayoutParams(1));
            row.addView(dummy);
            for (GeriatricScale scaleInstance : scaleInstances) {
                TextView dateTextView = new TextView(context);
                dateTextView.setBackgroundResource(background);
                dateTextView.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
                // check if for that day had the score in question
                GradingNonDB match = Scales.getGradingForScale(
                        scaleInstance,
                        scaleInstance.getSession().getPatient().getGender());
                if (match.getGrade().equals(currentGrade)) {
                    dateTextView.setText("X");
                } else {
                    dateTextView.setText("");
                }
                dateTextView.setGravity(Gravity.CENTER);
                dateTextView.setLayoutParams(new TableRow.LayoutParams(1));
                row.addView(dateTextView);
            }
            table.addView(row);
        }

    }
}

