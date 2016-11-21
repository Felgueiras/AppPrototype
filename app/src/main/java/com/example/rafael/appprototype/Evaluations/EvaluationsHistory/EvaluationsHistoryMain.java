package com.example.rafael.appprototype.Evaluations.EvaluationsHistory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.R;

public class EvaluationsHistoryMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_grid, container, false);


        if (Session.getAllSessions().isEmpty()) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_history));
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.evaluationsHistory, fragment)
                    .commit();

            GridView grid = (GridView) myInflatedView.findViewById(R.id.gridView);
            grid.setVisibility(View.GONE);

        } else {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new EvaluationsHistoryGrid();
            fragmentManager.beginTransaction()
                    .replace(R.id.evaluationsHistory, fragment)
                    .commit();
        }

        return myInflatedView;
    }
}

