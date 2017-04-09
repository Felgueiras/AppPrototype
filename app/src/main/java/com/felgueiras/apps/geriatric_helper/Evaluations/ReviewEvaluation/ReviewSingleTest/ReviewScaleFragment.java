package com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation.ReviewSingleTest;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Evaluations.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;


public class ReviewScaleFragment extends Fragment {


    public static String PATIENT = "PATIENT";
    public static String SCALE = "SCALE";
    SessionFirebase session;
    /**
     * GeriatricScale which will be written to the DB.
     */
    private GeriatricScaleFirebase scale;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        scale = (GeriatricScaleFirebase) bundle.getSerializable(SCALE);
        session = FirebaseHelper.getSessionFromScale(scale);

        // set the title
        getActivity().setTitle(scale.getShortName());
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
                Scales.getScaleByName(scale.getScaleName()),
                scale, null, getChildFragmentManager(), testQuestions);
        testQuestions.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


}