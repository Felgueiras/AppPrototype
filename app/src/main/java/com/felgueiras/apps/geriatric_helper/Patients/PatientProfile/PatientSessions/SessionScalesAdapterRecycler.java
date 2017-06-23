package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientSessions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.List;

/**
 * Display a List of the resume for each GeriatricScale inside a Session.
 */
public class SessionScalesAdapterRecycler extends RecyclerView.Adapter<SessionScalesAdapterRecycler.MyViewHolder> {
    /**
     * Questions for a Test
     */
    private final List<GeriatricScaleFirebase> sessionScales;
    private final boolean displayResult;
    private View testView;
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView testName;
        private final TextView testResult;
        private final LinearLayout card;
        private final ImageView scaleAreaIcon;
        public TextView patientName;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            card = (LinearLayout)view.findViewById(R.id.testResultCard);
            testName = (TextView) view.findViewById(R.id.scaleName);
            testResult = (TextView) view.findViewById(R.id.testResult);
            scaleAreaIcon = (ImageView) view.findViewById(R.id.scaleAreaIcon);
        }
    }

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param tests ArrayList of Questions
     * @param displayResult
     */
    public SessionScalesAdapterRecycler(Context context, List<GeriatricScaleFirebase> tests, boolean displayResult) {
        this.sessionScales = tests;
        this.displayResult = displayResult;
    }


    @Override
    public SessionScalesAdapterRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result, parent, false);

        return new SessionScalesAdapterRecycler.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionScalesAdapterRecycler.MyViewHolder holder, int position) {

        // get values
        GeriatricScaleFirebase scale = sessionScales.get(position);
        String name = Scales.getShortName(scale.getScaleName());
        GradingNonDB grading = Scales.getGradingForTestWithoutGenerating(scale, Constants.SESSION_GENDER);
        // update views
        holder.testName.setText(name);
        if (grading != null && displayResult)
            holder.testResult.setText(grading.getGrade());
        else{
            holder.testResult.setVisibility(View.GONE);
        }

        // set the area icon
        final String area = scale.getArea();

        switch (area) {
            case Constants.cga_mental:
                holder.scaleAreaIcon.setImageResource(R.drawable.ic_mental);
                break;
            case Constants.cga_functional:
                holder.scaleAreaIcon.setImageResource(R.drawable.ic_functional);
                break;
            case Constants.cga_nutritional:
                holder.scaleAreaIcon.setImageResource(R.drawable.ic_nutritional_black);
                break;
            case Constants.cga_social:
                holder.scaleAreaIcon.setImageResource(R.drawable.ic_people_black_24dp);
                break;
        }

        /**
         * If a ClickListener was passed, add it
         */
        if (onClickListener != null) {
            holder.card.setOnClickListener(onClickListener);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sessionScales.size();
    }


}