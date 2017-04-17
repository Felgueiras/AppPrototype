package com.felgueiras.apps.geriatric_helper.Sessions.SessionsHistory;

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
        public TextView patientName;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            card = (LinearLayout)view.findViewById(R.id.testResultCard);
            testName = (TextView) view.findViewById(R.id.testName);
            testResult = (TextView) view.findViewById(R.id.testResult);
        }
    }

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param tests ArrayList of Questions
     */
    public SessionScalesAdapterRecycler(Context context, List<GeriatricScaleFirebase> tests) {
        this.sessionScales = tests;
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
        if (grading != null)
            holder.testResult.setText(grading.getGrade());

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