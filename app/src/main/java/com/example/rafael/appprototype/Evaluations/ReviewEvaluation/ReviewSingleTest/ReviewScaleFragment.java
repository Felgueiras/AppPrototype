package com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;


public class ReviewScaleFragment extends Fragment {


    public static String PATIENT = "PATIENT";
    public static String SCALE = "SCALE";
    Session session;
    /**
     * GeriatricScale which will be written to the DB.
     */
    private GeriatricScale test;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        test = (GeriatricScale) bundle.getSerializable(SCALE);
        session = test.getSession();

        // set the title
        getActivity().setTitle(test.getShortName());
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_scale, container, false);
        // populate the ListView
        ListView testQuestions = (ListView) view.findViewById(R.id.testQuestions);
        // create the adapter
        QuestionsListAdapter adapter = new QuestionsListAdapter(
                this.getActivity(),
                Scales.getScaleByName(test.getScaleName()),
                test, null, getChildFragmentManager(), testQuestions);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


}