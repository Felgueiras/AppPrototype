package com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.GeriatricScale;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Evaluations.DisplayTest.ScaleFragment;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * Display a List of the resume for each GeriatricScale inside a Sesssion.
 */
public class AreaScalesAlreadyCompleted extends RecyclerView.Adapter<AreaScalesAlreadyCompleted.ScaleIconHolder> {
    /**
     * Questions for a Test
     */
    private final ArrayList<GeriatricScale> scales;
    private static LayoutInflater inflater = null;
    private final Session session;
    private final Activity context;
    private ArrayList<GeriatricScale> completedScales;

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param scales  ArrayList of Questions
     * @param session
     */
    public AreaScalesAlreadyCompleted(Activity context, ArrayList<GeriatricScale> scales, Session session) {
        this.scales = scales;
        this.session = session;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ScaleIconHolder extends RecyclerView.ViewHolder {
        private final TextView testName;

        public ScaleIconHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);
        }
    }


    @Override
    public ScaleIconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.scale_icon_name, parent, false);
        return new ScaleIconHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScaleIconHolder holder, int position) {
        final GeriatricScale currentScaleDB = completedScales.get(position);
        final String scaleName = currentScaleDB.getScaleName();
        GeriatricScaleNonDB currentScaleNonDB = Scales.getScaleByName(scaleName);

        // update views
        holder.testName.setText(currentScaleNonDB.getShortName());

        holder.testName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaleName.equals(Constants.test_name_mini_nutritional_assessment_global)) {
                    // check if triagem is already answered
                    Log.d("Nutritional", "Global pressed");

                    GeriatricScale triagem = session.getScaleByName(Constants.test_name_mini_nutritional_assessment_triagem);
                    if (!triagem.isCompleted()) {
                        Snackbar.make(holder.testName, "Precisa primeiro de completar a triagem", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Create new fragment and transaction
                Fragment newFragment = new ScaleFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(scaleName));
                bundle.putSerializable(ScaleFragment.SCALE, currentScaleDB);
                bundle.putSerializable(ScaleFragment.CGA_AREA, currentScaleDB.getArea());
                bundle.putSerializable(ScaleFragment.patient, session.getPatient());
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.current_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_session_scale_shortcut).commit();
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        // only completed scales
        completedScales = new ArrayList<>();
        for (GeriatricScale scale : scales) {
            if (scale.isCompleted()) {
                completedScales.add(scale);
            }
        }
        return completedScales.size();
    }


}