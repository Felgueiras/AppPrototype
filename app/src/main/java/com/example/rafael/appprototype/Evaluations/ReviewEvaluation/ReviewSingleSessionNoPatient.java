package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewArea;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class ReviewSingleSessionNoPatient extends Fragment {

    /**
     * Session object
     */
    private Session session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";
    private boolean comparePreviousSession;

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
        View myInflatedView = inflater.inflate(R.layout.content_review_session_no_patient, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (Session) args.getSerializable(SESSION);

        getActivity().setTitle(DatesHandler.dateToStringWithoutHour(session.getDate()));


        // check if we have to compare to the previous session
        //comparePreviousSession = args.getBoolean(COMPARE_PREVIOUS);
        comparePreviousSession = true;

        /**
         * Show info about evaluations for every area.
         */
        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.area_scales_recycler_view);
        ReviewArea adapter = new ReviewArea(getActivity(), session, comparePreviousSession);
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // close session FAB
        FloatingActionButton closeFAB = (FloatingActionButton) myInflatedView.findViewById(R.id.close_session);
        closeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                // check if logged in
                Fragment fragment;
                if (SharedPreferencesHelper.isLoggedIn(getActivity())) {
                    SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                    fragment = new PatientsMain();
                } else {
                    SharedPreferencesHelper.resetPublicSession(getActivity(), session.getGuid());
                    fragment = new CGAPublicInfo();
                }
                fragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .replace(R.id.current_fragment, fragment)
                        .commit();
            }
        });

        return myInflatedView;
    }

}

