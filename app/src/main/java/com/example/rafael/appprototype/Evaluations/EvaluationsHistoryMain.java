package com.example.rafael.appprototype.Evaluations;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.EmptyStateFragment;
import com.example.rafael.appprototype.R;

public class EvaluationsHistoryMain extends Fragment {

    private final ViewPager viewPager;
    private FragmentManager fragmentManager;

    public EvaluationsHistoryMain(ViewPager viewPager) {
        this.viewPager = viewPager;
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
            Fragment fragment = new EvaluationsHistoryGrid(viewPager);
            fragmentManager.beginTransaction()
                    .replace(R.id.evaluation_history_frame_layout, fragment)
                    .commit();
        }

//        // FAB
//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_evaluation_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // create a new Session - switch to CreatePatient Fragment
//                Bundle args = null;
//                FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), args, Constants.tag_create_session);
//
//
//            }
//        });

        return view;
    }
}

