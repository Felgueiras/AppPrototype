package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display a List of the resume for each GeriatricScale inside a Session.
 */
public class SessionScalesAdapterRecycler extends RecyclerView.Adapter<SessionScalesAdapterRecycler.MyViewHolder> {
    /**
     * Questions for a Test
     */
    private final List<GeriatricScale> sessionScales;
    private final Context context;
    private View testView;


    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView testName;
        private final TextView testResult;
        public TextView patientName;
        public ImageView  overflow;

        public MyViewHolder(View view) {
            super(view);
            testName = (TextView) view.findViewById(R.id.testName);
            testResult = (TextView) view.findViewById(R.id.testResult);
        }
    }

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param tests ArrayList of Questions
     */
    public SessionScalesAdapterRecycler(Context context, List<GeriatricScale> tests) {
        this.sessionScales = tests;
        this.context = context;
    }



    @Override
    public SessionScalesAdapterRecycler.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result, parent, false);

        return new SessionScalesAdapterRecycler.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionScalesAdapterRecycler.MyViewHolder holder, int position) {

        // get values
        GeriatricScale test = sessionScales.get(position);
        String name = Scales.getShortName(test.getScaleName());
        GradingNonDB grading = Scales.getGradingForTestWithoutGenerating(
                test,
                Constants.SESSION_GENDER);
        // update views
        holder.testName.setText(name);
        if(grading!=null)
            holder.testResult.setText(grading.getGrade());
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