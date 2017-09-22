package com.felgueiras.apps.geriatrichelper.Patients.Progress;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;
import java.util.List;


public class ScaleLegendAdapter extends RecyclerView.Adapter<ScaleLegendAdapter.MyViewHolder> {

    private PatientFirebase patient;
    private GeriatricScaleNonDB scale;
    private Activity context;
    ArrayList<GradingNonDB> toConsider = new ArrayList<>();
    /**
     * Data to be displayed.
     */
    private List<SessionFirebase> sessionsList;

    public ScaleLegendAdapter(PatientFirebase patient, GeriatricScaleNonDB scaleByName) {
        this.patient = patient;
        this.scale = scaleByName;
    }

    /**
     * Create a View
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final Button labelColor;
        public TextView label;

        public MyViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.label);
            labelColor = view.findViewById(R.id.labelColor);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        // current grade

        GradingNonDB currentGrading = toConsider.get(position);

        holder.label.setText(currentGrading.getGrade());
        holder.labelColor.setBackgroundColor(GraphViewHelper.findColorForGrade(scale.getScaleName(), currentGrading, patient, toConsider));

    }


    @Override
    public int getItemCount() {
        if (scale.getScoring().isDifferentMenWomen()) {
            if (patient.getGender() == Constants.MALE) {
                toConsider = scale.getScoring().getValuesMen();
            } else {
                toConsider = scale.getScoring().getValuesWomen();

            }
        } else {
            toConsider = scale.getScoring().getValuesBoth();

        }
        return toConsider.size();
    }
}
