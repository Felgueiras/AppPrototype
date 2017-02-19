package com.example.rafael.appprototype.Patients.PatientProgress;

import android.content.Context;
import android.graphics.Color;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by felgueiras on 18/02/2017.
 */

public class GraphViewHelper {

    /**
     * @param graph
     * @param scaleInstances
     * @param scaleInfo
     * @param context
     */
    public static void buildGraph(GraphView graph, ArrayList<GeriatricScale> scaleInstances,
                                  GeriatricScaleNonDB scaleInfo, Context context){
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        boolean hardwareAccelerated = graph.isHardwareAccelerated();
        System.out.println("hawrdare? " + hardwareAccelerated);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context, format));
        graph.getGridLabelRenderer().setNumHorizontalLabels(xAxis.size()); // only 4 because of the space


        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(xAxis.get(0).getTime());
        graph.getViewport().setMaxX(xAxis.get(xAxis.size() - 1).getTime());

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(scaleInfo.getScoring().getMaxScore());
        graph.addSeries(series);

        // styling
                /*
                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX() * 255 / 4,
                                (int) Math.abs(data.getY() * 255 / 6),
                                100);
                    }
                });
                */
        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
    }
}
