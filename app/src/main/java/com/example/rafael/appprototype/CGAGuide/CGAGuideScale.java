package com.example.rafael.appprototype.CGAGuide;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

import static com.example.rafael.appprototype.R.id.testQuestions;

public class CGAGuideScale extends Fragment {

    public static final String SCALE = "SCALE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cga_guide_scale, container, false);

        Bundle args = getArguments();

        GeriatricScaleNonDB scaleNonDB = (GeriatricScaleNonDB) args.getSerializable(SCALE);
        getActivity().setTitle(scaleNonDB.getScaleName());

        ListView scaleQuestions = (ListView) view.findViewById(testQuestions);
        
        // create fake session so doctor can fill out the questions
        QuestionsListAdapter adapter = new QuestionsListAdapter(this.getActivity(), scaleNonDB, null, null, getChildFragmentManager(), scaleQuestions);
        scaleQuestions.setAdapter(adapter);



        return view;
    }

}

