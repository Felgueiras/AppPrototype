package com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Help_Feedback.HelpMain;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;


/**
 * Display info about CGA and allow to create a new CGA session (public).
 */
public class CGAPublicInfo extends Fragment {


    private View view;

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
        view = inflater.inflate(R.layout.cga_public_info, container, false);
        getActivity().setTitle(getResources().getString(R.string.cga));



        /**
         * Start a session.
         */
        Button startSession = (Button) view.findViewById(R.id.start_acg_evaluation);
        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Session", "Clicked in CGAPublicInfo!");
                SharedPreferencesHelper.unlockSessionCreation(getActivity());
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

                FragmentTransitions.replaceFragment(getActivity(), new HelpMain(), null, Constants.more_info_clicked);
            }
        });
        return view;
    }
}

