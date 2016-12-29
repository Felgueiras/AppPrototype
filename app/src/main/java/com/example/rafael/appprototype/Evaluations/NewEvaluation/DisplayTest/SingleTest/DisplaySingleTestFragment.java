package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.R;

/**
 * Display a list of Questions for a single Test.
 */
public class DisplaySingleTestFragment extends Fragment {

    /**
     * String identifier for the Test.
     */
    public static final String testObject = "testNonDB";

    public static String patient = "patient";
    public static String testDBobject = "testDBObject";
    Session session;

    /**
     * Selected Test.
     */
    private GeriatricTestNonDB testNonDB;
    /**
     * GeriatricTest which will be written to the DB.
     */
    private GeriatricTest testDB;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        // get the list of tests
        Bundle bundle = getArguments();
        testNonDB = (GeriatricTestNonDB) bundle.getSerializable(testObject);
        testDB = (GeriatricTest) bundle.getSerializable(testDBobject);
        session = testDB.getSession();

        // set the title
        getActivity().setTitle(testNonDB.getShortName());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_display_single_test, container, false);
        // populate the ListView
        ListView testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        // create the adapter
        ViewQuestionsListAdapter adapter = new ViewQuestionsListAdapter(this.getActivity(), testNonDB, testDB);
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
                testDB.setAlreadyOpened(true);
                testDB.save();
                getActivity().onBackPressed();
        }
        return true;

    }


}