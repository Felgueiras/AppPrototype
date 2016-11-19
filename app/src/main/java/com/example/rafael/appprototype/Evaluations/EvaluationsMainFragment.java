package com.example.rafael.appprototype.Evaluations;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.Evaluations.SessionsHistoryTab.ShowEvaluationsForDay;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

public class EvaluationsMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = null;
        if (Constants.sessionID == null) {
            v = inflater.inflate(R.layout.content_grid_view, container, false);
            getActivity().setTitle(getResources().getString(R.string.tab_sessions));

            // fill the GridView
            GridView gridView = (GridView) v.findViewById(R.id.gridView);
            gridView.setAdapter(new ShowEvaluationsForDay(getActivity()));

            // FAB
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_evaluation);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // create a new Session - switch to CreatePatient Fragment
                    Bundle args = new Bundle();
                    ((MainActivity) getActivity()).replaceFragment(NewEvaluation.class, args, Constants.create_session);
                }
            });
        } else {
            /**
             * Resume the ongoing session.
             */
            Bundle args = new Bundle();
            ((MainActivity) getActivity()).replaceFragment(NewEvaluation.class, args, Constants.create_patient);
        }

        return v;
    }
}

