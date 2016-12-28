package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.ViewPatientSessions;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistory.ShowTestsForEvaluation;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewEvaluationMain;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewPatientEvolutionAdapter extends RecyclerView.Adapter<ViewPatientEvolutionAdapter.MyViewHolder> {

    /**
     * Patient which has these NewEvaluation.
     */
    private final Patient patient;
    private Context context;
    /**
     * Records from that patient
     */
    private ArrayList<Session> sessions;
    private Session currentSession;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public ImageView overflow;
        public ListView testsList;

        public MyViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.recordDate);
            testsList = (ListView) view.findViewById(R.id.session_tests_results);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Constructor of the ShowSingleEvaluation
     *
     * @param context
     * @param sessions
     * @param patient
     */
    public ViewPatientEvolutionAdapter(Context context, ArrayList<Session> sessions, Patient patient) {
        this.context = context;
        this.sessions = sessions;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_test_evolution, parent, false);


        // add on click listener for the Session
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewEvaluationMain.SESSION, currentSession);
                ((MainActivity) context).replaceFragment(ReviewEvaluationMain.class, args, Constants.tag_review_session);
            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        // display the result for the tests
        //ShowTestsForEvaluation adapter = new ShowTestsForEvaluation(context, testsFromSession);
        //holder.testsList.setAdapter(adapter);

        // TODO patient progress
        // current Test
        String testName = Constants.allTests[position];
        // get all the instances of that Test for this Patient
        ArrayList<GeriatricTest> testInstances = new ArrayList<>();

        ArrayList<Session> sessionsFromPatient = patient.getRecordsFromPatient();
        for (Session currentSession : sessionsFromPatient) {
            List<GeriatricTest> testsFromSession = currentSession.getTestsFromSession();
            for (GeriatricTest currentTest : testsFromSession) {
                if (currentTest.getTestName().equals(testName)) {
                    testInstances.add(currentTest);
                }
            }

        }

        if (testInstances.size() > 0) {
            System.out.println("Test name: " + testName + "\n\tNum instances: " + testInstances.size());
            // create axis information
            // x axis - date
            ArrayList<Date> xAxis = new ArrayList<>();
            // y axis - score
            ArrayList<Integer> yAxis = new ArrayList<>();
            for (GeriatricTest t : testInstances) {
                Date date = t.getSession().getDateAsString();
                xAxis.add(date);
                yAxis.add(t.getResult());
            }
            // present graph with temporal evolution
            System.out.println("Presenting graph");
            // get GraphView from layout
            GraphView graph = (GraphView) holder.itemView.findViewById(R.id.graph);
            // create DataPoints
            DataPoint[] points = new DataPoint[xAxis.size()];
            for (int pos = 0; pos < xAxis.size(); pos++) {
                points[pos] = new DataPoint(pos, yAxis.get(pos));
            }
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
            // set manual X bounds
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);

            // set manual Y bounds
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(5);
            graph.addSeries(series);

            // styling
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                }
            });

            series.setSpacing(50);

            // draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.RED);
        }

    }


    @Override
    public int getItemCount() {
        return Constants.allTests.length;
    }
}
