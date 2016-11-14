package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.app.Fragment;
import android.os.Bundle;
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


public class DisplaySingleTestFragment extends Fragment {

    /**
     * String identifier for the Test.
     */
    public static final String testObject = "test";
    public static String alreadyOpenedBefore = "alreadyOpened";
    /**
     * ID of the Session which this Test belongs
     */
    public static String sessionID = "session";
    public static String patient = "patient";
    Session session;

    /**
     * Yes if already opened before for this session, no otherwise
     */
    private boolean alreadyOpened;

    /**
     * Selected Test.
     */
    private GeriatricTestNonDB test;
    /**
     * GeriatricTest which will be written to the DB.
     */
    private GeriatricTest testDB;


    // newInstance constructor for creating fragment with arguments
    public static DisplaySingleTestFragment newInstance(int page, String title, GeriatricTestNonDB test) {
        DisplaySingleTestFragment fragmentFirst = new DisplaySingleTestFragment();
        Bundle args = new Bundle();
        args.putSerializable("test", test);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        // get the list of tests
        Bundle bundle = getArguments();
        test = (GeriatricTestNonDB) bundle.getSerializable(testObject);
        session = (Session) bundle.getSerializable(sessionID);
        alreadyOpened = bundle.getBoolean(alreadyOpenedBefore);

        // set the title
        getActivity().setTitle(test.getTestName());

        if (!alreadyOpened) {
            // create new Test and add to DB
            testDB = new GeriatricTest();
            String dummyID = session.getGuid() + "-" + test.getTestName();
            testDB.setGuid(dummyID);
            testDB.setTestName(test.getTestName());
            testDB.setType(test.getType());
            testDB.setSession(session);
            testDB.save();
        } else {
            testDB = GeriatricTest.getTestByID(session.getGuid() + "-" + test.getTestName());
        }

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_display_single_test, container, false);
        // populate the ListView
        /*
      ListView that will hold the Questions
     */
        ListView testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        // create the adapter
        /*
      Adapter to the ListView
     */
        ViewQuestionsListAdapter adapter = new ViewQuestionsListAdapter(this.getActivity(), test.getQuestions(), testDB, alreadyOpened);
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
                getActivity().onBackPressed();
        }
        return true;

    }


}