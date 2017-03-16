package com.example.rafael.appprototype.Patients.Progress;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
     */
    public void buildTable(TableLayout table, ArrayList<GeriatricScale> scaleInstances,
                           GeriatricScaleNonDB scale, Context context) {
        // create axis information
        // x axis - date
        ArrayList<Date> xAxis = new ArrayList<>();
        // y axis - score
        ArrayList<Double> yAxis = new ArrayList<>();
        for (GeriatricScale t : scaleInstances) {
            Date date = t.getSession().getDate();
            xAxis.add(date);
            //system.out.println(date.toString());
            yAxis.add(t.getResult());
        }

        // create DataPoints
        DataPoint[] points = new DataPoint[xAxis.size()];
        for (int pos = 0; pos < xAxis.size(); pos++) {
            points[pos] = new DataPoint(xAxis.get(pos), yAxis.get(pos));
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
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
        for (Date sessionDate : xAxis) {
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
        // TODO check if values are male or female specific
        for (GradingNonDB grading : scale.getScoring().getValuesBoth()) {
            row = new TableRow(context);
            dummy = new TextView(context);
            dummy.setBackgroundResource(background);
            dummy.setPadding(paddingValue, paddingValue, paddingValue, paddingValue);
            String currentGrade = grading.getGrade();
            dummy.setText(currentGrade);
            dummy.setLayoutParams(new TableRow.LayoutParams(1));
            row.addView(dummy);
            // TODO start from here
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
