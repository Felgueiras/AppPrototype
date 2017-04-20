package com.felgueiras.apps.geriatric_helper.CGAGuide;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Sessions.SingleArea.ScaleInfoHelper;
import com.felgueiras.apps.geriatric_helper.R;

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
    private ArrayList<GeriatricScaleNonDB> scalesForArea;

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
            name = (TextView) view.findViewById(R.id.scaleName);
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


        final String scaleName = scalesForArea.get(position).getScaleName();
        final GeriatricScaleNonDB currentScale = Scales.getScaleByName(scaleName);
        holder.name.setText(scalesForArea.get(position).getShortName());

        holder.scaleInfo.setText(currentScale.getDescription());

        // scale scoring
        new ScaleInfoHelper(context,currentScale).fillTableScaleScoring(
                currentScale,
                holder.scaleScoring
        );


        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // view the CGA guide for the scale

                // Create new fragment and transaction
                Fragment newFragment = new CGAGuideScale();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(CGAGuideScale.SCALE, currentScale);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.current_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_guide_scale).commit();
            }
        });

    }


    @Override
    public int getItemCount() {
        // get the number of tests that exist for this area
        scalesForArea = Scales.getScalesForArea(area);
        return scalesForArea.size();
    }

}
