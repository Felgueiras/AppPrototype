package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient.PatientEvolution;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
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
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // test name
        public TextView testName;
        // view stub for the graph
        public ViewStub stub;
        public View itemView;

        public MyViewHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);
            stub = (ViewStub) view.findViewById(R.id.graph_stub);
            itemView = view;
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context
     * @param sessions
     * @param patient
     */
    public ViewPatientEvolutionAdapter(Context context, ArrayList<Session> sessions, Patient patient) {
        this.context = context;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_test_evolution, parent, false);

        /*
        // add on click listener for the Session
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ReviewSingleSession.SESSION, currentSession);
                ((MainActivity) context).replaceFragment(ReviewSingleSession.class, args, Constants.tag_review_session);
            }
        });
        */

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // current Test
        String testName = Constants.allTests[position];

        // get test from static definition
        GeriatricTestNonDB testInfo = StaticTestDefinition.getTestByName(testName);

        // get all the instances of that Test for this Patient
        ArrayList<GeriatricTest> testInstances = new ArrayList<>();
        // get instances for that test
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

            // only inflate once
            View inflated = null;
            holder.stub = (ViewStub) holder.itemView.findViewById(R.id.graph_stub);
            if (holder.stub != null) {
                // only inflate once
                inflated = holder.stub.inflate();

                // get GraphView and TextView from stub
                GraphView graph = (GraphView) inflated.findViewById(R.id.graph);

                TextView test = (TextView) inflated.findViewById(R.id.testName);
                // set test name in the gui
                test.setText(testName);

                // create axis information
                // x axis - date
                ArrayList<Date> xAxis = new ArrayList<>();
                // y axis - score
                ArrayList<Double> yAxis = new ArrayList<>();
                for (GeriatricTest t : testInstances) {
                    Date date = t.getSession().getDate();
                    xAxis.add(date);
                    System.out.println(date.toString());
                    yAxis.add(t.generateTestResult());
                }

                // create DataPoints
                DataPoint[] points = new DataPoint[xAxis.size()];
                for (int pos = 0; pos < xAxis.size(); pos++) {
                    points[pos] = new DataPoint(xAxis.get(pos), yAxis.get(pos));
                }
                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
                // set date label formatter
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context, format));
                graph.getGridLabelRenderer().setNumHorizontalLabels(xAxis.size()); // only 4 because of the space


                // set manual X bounds
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(xAxis.get(0).getTime());
                graph.getViewport().setMaxX(xAxis.get(xAxis.size() - 1).getTime());

                // set manual Y bounds
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(testInfo.getScoring().getMaxScore());
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
        } else {
            /**
             * When there are no evaluations for this test.
             */
        }

    }


    @Override
    public int getItemCount() {
        // display only the tests which we have data
        return Constants.allTests.length;
    }
}
