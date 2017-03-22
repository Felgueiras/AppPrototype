package com.example.rafael.appprototype.Evaluations.AllAreas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Help_Feedback.HelpTopics;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;


/**
 * Display info about CGA and allow to create a new CGA session (public).
 */
public class CGAPublicInfo extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cga_public_info, container, false);
        getActivity().setTitle(getResources().getString(R.string.cga));

        Log.d("Session", "Inside CGAPublicInfo");
        SharedPreferencesHelper.unlockSessionCreation(getActivity());



        /**
         * Start a session.
         */
        Button startSession = (Button) view.findViewById(R.id.start_acg_evaluation);
        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Session", "Clicked!");
                FragmentTransitions.replaceFragment(getActivity(), new CGAPublic(), null, Constants.tag_cga_public);
            }
        });


        /**
         * See more information.
         */
        Button moreInfo = (Button) view.findViewById(R.id.more_info);
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HelpTopics endFragment = new HelpTopics();
                BackStackHandler.clearBackStack();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.current_fragment, endFragment)
                        .commit();
            }
        });
        return view;
    }
}

