package com.example.rafael.appprototype.Evaluations;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

public class EvaluationsHistoryMain extends Fragment {

    private final ViewPager viewPager;
    private final int page;
    private FragmentManager fragmentManager;

    public EvaluationsHistoryMain(ViewPager viewPager, int position) {
        this.viewPager = viewPager;
        this.page = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.evaluations_history, container, false);

        if (Session.getAllSessions().isEmpty()) {
            fragmentManager = getFragmentManager();
            Fragment fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_history));
            fragment.setArguments(args);

            Fragment currentFragment = fragmentManager.findFragmentById(R.id.evaluation_history_frame_layout);
            fragmentManager.beginTransaction()
                    //.remove(currentFragment)
                    .replace(R.id.evaluation_history_frame_layout, fragment)
                    .commit();

        } else {
            fragmentManager = getFragmentManager();
            Fragment fragment = new EvaluationsHistoryGrid(viewPager, page);
            fragmentManager.beginTransaction()
                    .replace(R.id.evaluation_history_frame_layout, fragment)
                    .commit();
        }

        // create a new session without a patient
        AddFloatingActionButton fab = (AddFloatingActionButton) view.findViewById(R.id.new_evaluation_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create a new Session - switch to CreatePatient Fragment
                Bundle args = null;
                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session_no_patient);
            }
        });

        return view;
    }
}

