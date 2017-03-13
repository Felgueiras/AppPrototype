package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.R;

/**
 * Display a list of Questions for a single Test.
 */
public class ScaleFragment extends Fragment {

    /**
     * String identifier for the Test.
     */
    public static final String testObject = "testNonDB";

    public static String patient = "patient";
    public static String testDBobject = "testDBObject";
    public static String CGA_AREA;
    Session session;


    /**
     * Selected Test.
     */
    private GeriatricScaleNonDB testNonDB;
    /**
     * GeriatricScale which will be written to the DB.
     */
    private GeriatricScale testDB;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        // get the list of tests
        Bundle bundle = getArguments();
        testNonDB = (GeriatricScaleNonDB) bundle.getSerializable(testObject);
        testDB = (GeriatricScale) bundle.getSerializable(testDBobject);
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
        ProgressBar progress = (ProgressBar) view.findViewById(R.id.scale_progress);
        // create the adapter
        QuestionsListAdapter adapter = new QuestionsListAdapter(this.getActivity(), testNonDB, testDB, progress);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }




}