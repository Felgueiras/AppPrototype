package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.R;


public class EvaluationsHistoryGrid extends Fragment {

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sessions_history_grid, container, false);
        // fill the GridView
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ShowEvaluationsForDay(getActivity()));

        return view;
    }
}