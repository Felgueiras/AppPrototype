package com.example.rafael.appprototype.Evaluations.SessionsHistoryTab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.R;

import java.util.List;

/**
 * Display a List of the resume for each GeriatricTest inside a Sesssion.
 */
public class ShowTestsForEvaluation extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final List<GeriatricTest> tests;
    private static LayoutInflater inflater = null;
    private View testView;

    /**
     * Display all Questions for a GeriatricTest
     *
     * @param tests ArrayList of Questions
     */
    public ShowTestsForEvaluation(Context context, List<GeriatricTest> tests) {
        this.tests = tests;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // setup views
        testView = inflater.inflate(R.layout.test_result, parent, false);
        TextView testName = (TextView) testView.findViewById(R.id.testName);
        TextView testResult = (TextView) testView.findViewById(R.id.testResult);

        // get values
        GeriatricTest geriatricTest = tests.get(position);
        String name = StaticTestDefinition.getShortName(geriatricTest.getTestName());
        int result = geriatricTest.getResult();

        // update views
        testName.setText(name);
        testResult.setText(result + "");

        return testView;
    }

    @Override
    public int getCount() {
        return tests.size();
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