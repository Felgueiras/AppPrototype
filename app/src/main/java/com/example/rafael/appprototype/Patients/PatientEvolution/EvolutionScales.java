package com.example.rafael.appprototype.Patients.PatientEvolution;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.rafael.appprototype.R.id.testName;

public class EvolutionScales extends RecyclerView.Adapter<EvolutionScales.MyViewHolder> {

    /**
     * Patient which has these NewEvaluationPrivate.
     */
    private final Patient patient;
    private final String area;
    private final ArrayList<Session> patientSessions;
    private Context context;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final GraphView graph;
        // test area
        public TextView testName;
        // view stub for the graph
        public ViewStub stub;
        public View itemView;

        public MyViewHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);
            //stub = (ViewStub) view.findViewById(R.id.graph_stub);
            graph = (GraphView) view.findViewById(R.id.graph);

            itemView = view;
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context
     * @param sessions
     * @param area
     */
    public EvolutionScales(Context context, ArrayList<Session> sessions, String area, Patient patient) {
        this.context = context;
        this.patient = patient;
        this.area = area;
        this.patientSessions = sessions;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_test_evolution, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the scales from all sessions for this area
        ArrayList<GeriatricTestNonDB> testsForArea = Scales.getTestsForArea(area);

        // current test
        GeriatricTestNonDB testInfo = testsForArea.get(position);
        String testName = testsForArea.get(position).getTestName();

        // get all the instances of that Test for this Patient
        ArrayList<GeriatricTest> testInstances = new ArrayList<>();
        // get instances for that test
        for (Session currentSession : patientSessions) {
            List<GeriatricTest> testsFromSession = currentSession.getTestsFromSession();
            for (GeriatricTest currentTest : testsFromSession) {
                if (currentTest.getTestName().equals(testName)) {
                    testInstances.add(currentTest);
                }
            }
        }

        // set test area in the gui
        holder.testName.setText(testName);

        if (testInstances.size() > 0) {
            // create axis information
            // x axis - date
            ArrayList<Date> xAxis = new ArrayList<>();
            // y axis - score
            ArrayList<Double> yAxis = new ArrayList<>();
            for (GeriatricTest t : testInstances) {
                Date date = t.getSession().getDate();
                xAxis.add(date);
                //system.out.println(date.toString());
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

            holder.graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context, format));
            holder.graph.getGridLabelRenderer().setNumHorizontalLabels(xAxis.size()); // only 4 because of the space


            // set manual X bounds
            holder.graph.getViewport().setXAxisBoundsManual(true);
            holder.graph.getViewport().setMinX(xAxis.get(0).getTime());
            holder.graph.getViewport().setMaxX(xAxis.get(xAxis.size() - 1).getTime());

            // set manual Y bounds
            holder.graph.getViewport().setYAxisBoundsManual(true);
            holder.graph.getViewport().setMinY(0);
            holder.graph.getViewport().setMaxY(testInfo.getScoring().getMaxScore());
            holder.graph.addSeries(series);

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

        } else {
            /**
             * When there are no evaluations for this test.
             */
            System.out.println("It's zero!");
        }

    }


    @Override
    public int getItemCount() {
        // get all scales from this area
        return Scales.getTestsForArea(area).size();
    }
}
