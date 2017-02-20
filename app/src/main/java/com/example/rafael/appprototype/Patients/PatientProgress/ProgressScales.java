package com.example.rafael.appprototype.Patients.PatientProgress;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.R;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

import static android.view.WindowManager.*;

public class ProgressScales extends RecyclerView.Adapter<ProgressScales.MyViewHolder> {

    /**
     * Patient which has these NewEvaluationPrivate.
     */
    private final String area;
    private final ArrayList<Session> patientSessions;
    private final Patient patient;
    private Activity context;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // test area
        public TextView testName;
        // view stub for the graph
        public View itemView;

        public MyViewHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);

            itemView = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context
     * @param sessions
     * @param area
     */
    public ProgressScales(Activity context, ArrayList<Session> sessions, String area, Patient patient) {
        this.context = context;
        this.area = area;
        this.patientSessions = sessions;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_progress_scale, parent, false);

        context.getWindow().setFlags(
                LayoutParams.FLAG_HARDWARE_ACCELERATED,
                LayoutParams.FLAG_HARDWARE_ACCELERATED);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the scales from all sessions for this area
        ArrayList<GeriatricScaleNonDB> testsForArea = Scales.getTestsForArea(area);

        // current test
        final GeriatricScaleNonDB scaleInfo = testsForArea.get(position);
        final String currentScale = testsForArea.get(position).getTestName();

        // get all the instances of that Test for this Patient
        ArrayList<GeriatricScale> scaleInstances = GeriatricScale.getScaleInstancesForPatient(patientSessions, currentScale);

        // set test area in the gui
        holder.testName.setText(currentScale);

        /**
         * Patient has been evaluated by this scale.
         */
        if (scaleInstances.size() > 0) {
            // inflate the ViewStub
            ViewStub graphViewStub = ((ViewStub) holder.itemView.findViewById(R.id.stub_graph_view)); // get the reference of ViewStub
            if (graphViewStub != null) {
                // only inflate once
                View inflated = graphViewStub.inflate();
                GraphView graph = (GraphView) inflated.findViewById(R.id.graph_view);

                GraphViewHelper.buildGraph(graph, scaleInstances, scaleInfo, context);

                /**
                 * View progress in detail.
                 */
                graph.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create new fragment and transaction
                        Fragment newFragment = new ProgressDetail();
                        // add arguments
                        Bundle bundle = new Bundle();
                        bundle.putString(ProgressDetail.SCALE, currentScale);
                        bundle.putSerializable(ProgressDetail.PATIENT, patient);
                        bundle.putSerializable(ProgressDetail.SCALE_INFO, scaleInfo);
                        newFragment.setArguments(bundle);
                        // setup the transaction
                        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                        transaction.replace(R.id.current_fragment, newFragment);
                        transaction.addToBackStack(Constants.tag_progress_detail).commit();
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        // get all scales from this area
        return Scales.getTestsForArea(area).size();
    }
}
