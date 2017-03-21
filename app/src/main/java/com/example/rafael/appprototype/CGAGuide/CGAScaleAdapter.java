package com.example.rafael.appprototype.CGAGuide;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Evaluations.SingleArea.ScaleHandlerInfo;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Create the Card for each of the Tests available
 */
public class CGAScaleAdapter extends RecyclerView.Adapter<CGAScaleAdapter.ScaleCardHolder> {

    /**
     * CGA area.
     */
    private final String area;

    private Activity context;
    private ArrayList<GeriatricScaleNonDB> testsForArea;

    /**
     * Create a View
     */
    public static class ScaleCardHolder extends RecyclerView.ViewHolder implements Serializable {

        private final TableLayout scaleScoring;
        private TextView scaleInfo;
        public TextView name;
        public View view;

        public ScaleCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            scaleInfo = (TextView) view.findViewById(R.id.scale_info);
            scaleScoring = (TableLayout) view.findViewById(R.id.scale_scoring_table);
            this.view = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context current Context
     * @param area
     */
    public CGAScaleAdapter(Activity context, String area) {
        this.context = context;
        this.area = area;
    }

    @Override
    public ScaleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.cga_guide_scale_card, parent, false);
        return new ScaleCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final ScaleCardHolder holder, int position) {


        String scaleName = testsForArea.get(position).getScaleName();
        GeriatricScaleNonDB currentScale = Scales.getScaleByName(scaleName);
        holder.name.setText(testsForArea.get(position).getShortName());

        holder.scaleInfo.setText(currentScale.getDescription());

        // scale scoring
        new ScaleHandlerInfo(context).fillTableScaleScoring(
                currentScale,
                holder.scaleScoring
        );


        /**
         * For when the Test is selected.
         */
//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (finalCurrentTest.getScaleName().equals(Constants.test_name_mini_nutritional_assessment_global)) {
//                    // check if triagem is already answered
//                    Log.d("Nutritional", "Global pressed");
//
//                    GeriatricScale triagem = session.getScaleByName(Constants.test_name_mini_nutritional_assessment_triagem);
//                    if (!triagem.isCompleted()) {
//                        Snackbar.make(holder.view, "Precisa primeiro de completar a triagem", Snackbar.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//                String selectedTestName = finalCurrentTest.getScaleName();
//
//                // Create new fragment and transaction
//                Fragment newFragment = new ScaleFragment();
//                // add arguments
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(selectedTestName));
//                bundle.putSerializable(ScaleFragment.SCALE, finalCurrentTest);
//                bundle.putSerializable(ScaleFragment.CGA_AREA, area);
//                bundle.putSerializable(ScaleFragment.patient, session.getPatient());
//                newFragment.setArguments(bundle);
//                // setup the transaction
//                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
//                transaction.replace(R.id.current_fragment, newFragment);
//                transaction.addToBackStack(Constants.tag_display_session_scale).commit();
//            }
//        });

    }


    @Override
    public int getItemCount() {
        // get the number of tests that exist for this area
        testsForArea = Scales.getTestsForArea(area);
        return testsForArea.size();
    }

}
