package com.example.rafael.appprototype.Evaluations;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;

public class EvaluationsHistoryMain extends Fragment {


    public EvaluationsHistoryMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.evaluations_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_evaluation_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new Session - switch to CreatePatient Fragment
                Bundle args = null;
                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session_no_patient);
            }
        });


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment;
        /**
         * Setup fragment for FrameLayout.
         */
        if (Session.getAllSessions().isEmpty()) {
            fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_history));
            fragment.setArguments(args);
        } else {
            fragment = new EvaluationsHistoryGrid();
        }
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.evaluation_history_frame_layout);
        if (currentFragment != null)
            transaction.remove(currentFragment);
        transaction.replace(R.id.evaluation_history_frame_layout, fragment);
        transaction.commit();
    }

}

