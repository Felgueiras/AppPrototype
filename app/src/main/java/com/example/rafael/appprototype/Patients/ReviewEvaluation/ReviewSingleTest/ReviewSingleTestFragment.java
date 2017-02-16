package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

/**
 * Display a list of Questions for a single Test.
 */
public class ReviewSingleTestFragment extends Fragment {


    public static String patient = "patient";
    public static String testDBobject = "testDBObject";
    Session session;
    /**
     * GeriatricTest which will be written to the DB.
     */
    private GeriatricTest test;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        test = (GeriatricTest) bundle.getSerializable(testDBobject);
        session = test.getSession();

        // set the title
        getActivity().setTitle(test.getShortName());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_display_single_test, container, false);
        // populate the ListView
        ListView testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        // create the adapter
        QuestionsListAdapter adapter = new QuestionsListAdapter(
                this.getActivity(),
                Scales.getTestByName(test.getScaleName()),
                test);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_questions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_back:
                /**
                 * Go back to display the list of tests
                 */
                test.save();
                getActivity().onBackPressed();
        }
        return true;

    }


}