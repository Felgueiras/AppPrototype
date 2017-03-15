package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class SessionScalesAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final List<GeriatricScale> sessionScales;
    private static LayoutInflater inflater = null;

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param tests ArrayList of Questions
     */
    public SessionScalesAdapter(Context context, List<GeriatricScale> tests) {
        this.sessionScales = tests;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // setup views
        View testView = inflater.inflate(R.layout.test_result, parent, false);
        TextView testName = (TextView) testView.findViewById(R.id.testName);
        TextView testResult = (TextView) testView.findViewById(R.id.testResult);

        // get values
        GeriatricScale test = sessionScales.get(position);
        String name = Scales.getShortName(test.getScaleName());
        GradingNonDB grading = Scales.getGradingForTestWithoutGenerating(
                test,
                Constants.SESSION_GENDER);
        // update views
        testName.setText(name);
        testResult.setText(grading.getGrade());

        return testView;
    }

    @Override
    public int getCount() {
        return sessionScales.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}