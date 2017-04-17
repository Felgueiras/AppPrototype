package com.felgueiras.apps.geriatric_helper.Patients.Progress;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewStub;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

import static android.view.WindowManager.*;

public class ProgressScalesForAreaGraph extends RecyclerView.Adapter<ProgressScalesForAreaGraph.MyViewHolder> {

    /**
     * Patient which has these NewEvaluationPrivate.
     */
    private final String area;
    private final ArrayList<SessionFirebase> patientSessions;
    private final PatientFirebase patient;
    private Activity context;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView scaleNotEvaluated;
        // test area
        public TextView testName;
        // view stub for the graph
        public View itemView;

        public MyViewHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);
            scaleNotEvaluated = (TextView) view.findViewById(R.id.scale_not_evaluated);
            itemView = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *  @param context
     * @param sessions
     * @param area
     * @param patient
     */
    public ProgressScalesForAreaGraph(Activity context, ArrayList<SessionFirebase> sessions, String area, PatientFirebase patient) {
        this.context = context;
        this.area = area;
        this.patientSessions = sessions;
        this.patient = patient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_progress_scale_graph, parent, false);

        context.getWindow().setFlags(
                LayoutParams.FLAG_HARDWARE_ACCELERATED,
                LayoutParams.FLAG_HARDWARE_ACCELERATED);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // get the scales from all sessions for this area
        ArrayList<GeriatricScaleNonDB> testsForArea = Scales.getScalesForArea(area);

        // current test
        final GeriatricScaleNonDB scaleInfo = testsForArea.get(position);
        final String currentScale = testsForArea.get(position).getScaleName();

        // get all the instances of that Test for this Patient
        ArrayList<GeriatricScaleFirebase> scaleInstances =    FirebaseDatabaseHelper.getScaleInstancesForPatient(patientSessions, currentScale);

        // set test area in the gui
        holder.testName.setText(currentScale);

        /**
         * Patient has been evaluated by this scale.
         */
        if (scaleInstances.size() > 0) {
            // inflate the ViewStub
            ViewStub graphViewStub = ((ViewStub) holder.itemView.findViewById(R.id.area_info_stub)); // get the reference of ViewStub
            if (graphViewStub != null) {
                // only inflate once
                View inflated = graphViewStub.inflate();
                GraphView graph = (GraphView) inflated.findViewById(R.id.graph_view);

                // create labels for graph
                RecyclerView labels = (RecyclerView) inflated.findViewById(R.id.scoringLabels);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
                labels.setLayoutManager(mLayoutManager);
                labels.setItemAnimator(new DefaultItemAnimator());
                ScaleLegendAdapter adapter = new ScaleLegendAdapter(patient,Scales.getScaleByName(scaleInstances.get(0).getScaleName()));
                labels.setAdapter(adapter);

                // create graphview
                GraphViewHelper.buildProgressGraph(graph, scaleInstances, scaleInfo, context, patient);

                /**
                 * View progress in detail.
                 */
//                graph.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // Create new fragment and transaction
//                        Fragment newFragment = new ProgressDetail();
//                        // add arguments
//                        Bundle bundle = new Bundle();
//                        bundle.putString(ProgressDetail.SCALE, currentScale);
//                        bundle.putSerializable(ProgressDetail.PATIENT, patient);
//                        bundle.putSerializable(ProgressDetail.SCALE_INFO, scaleInfo);
//                        newFragment.setArguments(bundle);
//                        // setup the transaction
//                        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.current_fragment, newFragment);
//                        transaction.addToBackStack(Constants.tag_progress_detail).commit();
//                    }
//                });
            }
            // delete other text
            ViewManager parentView = (ViewManager) holder.testName.getParent();
            parentView.removeView(holder.scaleNotEvaluated);
        }
    }


    @Override
    public int getItemCount() {
        // get all scales from this area
        return Scales.getScalesForArea(area).size();
    }
}
