package com.felgueiras.apps.geriatric_helper.Patients.Progress;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Sessions.ReviewSession.ReviewSingleTest.ReviewScaleFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class GraphViewHelper {

    /**
     * @param graph
     * @param scaleInstances
     * @param scaleInfo
     * @param context
     * @param patient
     */
    public static void buildProgressGraph(GraphView graph, final ArrayList<GeriatricScaleFirebase> scaleInstances,
                                          final GeriatricScaleNonDB scaleInfo, final Activity context, final PatientFirebase patient) {

        // set date label formatter
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);


        // custom label formatter to show currency "EUR"


        // create axis information
        // x axis - date
        final ArrayList<Date> xAxisDate = new ArrayList<>();
//        ArrayList<String> xAxisString = new ArrayList<>();
        ArrayList<Integer> xAxisDouble = new ArrayList<>();
        int current = 0;
        // y axis - score
        ArrayList<Double> yAxis = new ArrayList<>();
        for (GeriatricScaleFirebase scale : scaleInstances) {
            Date date = new Date(FirebaseHelper.getSessionFromScale(scale).getDate());
            xAxisDate.add(date);
//            xAxisString.add(date.toString());
            xAxisDouble.add(current++);
            yAxis.add(scale.getResult());
        }


        // create DataPoints
        DataPoint[] points = new DataPoint[xAxisDouble.size()];
        for (int pos = 0; pos < xAxisDouble.size(); pos++) {
            points[pos] = new DataPoint(xAxisDouble.get(pos), yAxis.get(pos));
        }


        // points series
        final BarGraphSeries<DataPoint> pointsSeries = new BarGraphSeries<>(points);

        // set manual X bounds
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(xAxis2.get(0) - 1);
//        graph.getViewport().setMaxX(xAxis2.get(xAxis2.size() - 1) + 1);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(scaleInfo.getScoring().getMaxScore());


        pointsSeries.setSpacing(50);
        pointsSeries.setDrawValuesOnTop(true);
        pointsSeries.setValuesOnTopColor(Color.BLACK);


        pointsSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return findColorForScore(scaleInstances.get(0).getScaleName(), data.getY(), patient);
            }
        });

        pointsSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                // check the x -> index
                int index = (int) dataPoint.getX();
                Date currentDate = xAxisDate.get(index);
                GeriatricScaleFirebase selectedScale = scaleInstances.get(index);


                // view the results for the selected session
                // Create new fragment and transaction
                Fragment newFragment = new ReviewScaleFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReviewScaleFragment.SCALE, selectedScale);
                bundle.putSerializable(ReviewScaleFragment.PATIENT, patient);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.current_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_review_scale_from_progress).commit();
//
            }
        });


        graph.addSeries(pointsSeries);


//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context,
//                SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL)));
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context,
//                format));
        graph.getGridLabelRenderer().setNumHorizontalLabels(xAxisDouble.size());

//// set date label formatter
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(xAxis.size()); // only 4 because of the space

// set manual x bounds to have nice steps
//        graph.getViewport().setMinX(xAxis.get(0).getTime());
        graph.getViewport().setMinX(xAxisDouble.get(0));
//        graph.getViewport().setMaxX(xAxis.get(xAxis.size() - 1).getTime());
        graph.getViewport().setMaxX(xAxisDouble.get(xAxisDouble.size() - 1));
        graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    public static int findColorForScore(String scaleName, double score, PatientFirebase patient) {

        GeriatricScaleNonDB scaleNonDB = Scales.getScaleByName(scaleName);

        int scoreCategoryIndex = scaleNonDB.getScoring().getGradingIndex(score, patient.getGender());
        ArrayList<GradingNonDB> toConsider;
        if (scaleNonDB.getScoring().isDifferentMenWomen()) {
            if (patient.getGender() == Constants.MALE) {
                toConsider = scaleNonDB.getScoring().getValuesMen();
            } else {
                toConsider = scaleNonDB.getScoring().getValuesWomen();

            }
        } else {
            toConsider = scaleNonDB.getScoring().getValuesBoth();
        }
        int maxScore = toConsider.size() - 1;

        // assing red and green values
//        double green = scoreCategoryIndex * 1.0 / (maxScore);
        double green = (maxScore - scoreCategoryIndex) * 1.0 / maxScore;
        green *= 255;
        return Color.rgb(0, (int) green, 0);
    }

    public static int findColorForGrade(String scaleName, GradingNonDB grade, PatientFirebase patient, ArrayList<GradingNonDB> toConsider) {

        GeriatricScaleNonDB scaleNonDB = Scales.getScaleByName(scaleName);

        int scoreCategoryIndex = scaleNonDB.getScoring().getGradingIndex(grade.getMin(), patient.getGender());
        int maxScore = toConsider.size() - 1;

        // assing red and green values
//        double green = scoreCategoryIndex * 1.0 / (maxScore);
        double green = (maxScore - scoreCategoryIndex) * 1.0 / maxScore;
        green *= 255;
        return Color.rgb(0, (int) green, 0);
    }
}
