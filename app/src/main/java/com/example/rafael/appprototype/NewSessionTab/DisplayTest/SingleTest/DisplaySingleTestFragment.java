package com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
     * ListView that will hold the Questions
     */
    private ListView testQuestions;
    /**
     * Adapter to the ListView
     */
    private ViewQuestionsListAdapter adapter;
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
        // get the list of tests
        Bundle bundle = getArguments();
        test = (GeriatricTestNonDB) bundle.getSerializable(testObject);
        alreadyOpened = bundle.getBoolean(alreadyOpenedBefore);
        session = (Session) bundle.getSerializable(sessionID);
        if (!alreadyOpened) {
            Log.d("NewSession","Not opened, adding to DB");
            // create new Test and add to DB
            testDB = new GeriatricTest();
            String dummyID = session.getGuid() + "-" + test.getTestName();
            testDB.setGuid(dummyID);
            testDB.setTestName(test.getTestName());
            testDB.setType(test.getType());
            testDB.setSession(session);
            testDB.save();
            // retrieve to check
            // GeriatricTest retrievedTest = GeriatricTest.getTestByID(dummyID);
            // Log.d("NewSession", "Test with SessionID " + retrievedTest.getGuid());
        }
        else
        {
            Log.d("NewSession","Already opened, not adding to DB");
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_display_single_test, container, false);
        // populate the ListView
        testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        // create the adapter
        adapter = new ViewQuestionsListAdapter(this.getActivity(),
                test.getQuestions(),
                testDB);
        testQuestions.setAdapter(adapter);
        return view;
    }


}