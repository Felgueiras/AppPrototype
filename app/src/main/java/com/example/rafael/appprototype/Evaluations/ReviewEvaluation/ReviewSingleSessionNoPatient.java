package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPublicInfo;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewAreaCard;
import com.example.rafael.appprototype.PersonalAreaAccess.RegisterUser;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;


public class ReviewSingleSessionNoPatient extends Fragment {

    /**
     * Session object
     */
    private Session session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";

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

        getActivity().setTitle("Resultados da Sessão");

        /**
         * If first public evaluation, show alert dialog about saving sessions
         * and registering in the app.
         */
        Activity context = getActivity();
        boolean firstPublicEvaluation = SharedPreferencesHelper.checkFirstPublicEvaluation(getActivity());
        if (firstPublicEvaluation) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle(getResources().getString(R.string.session_discard));
            alertDialog.setMessage(context.getResources().getString(R.string.firstPublicEvaluation));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // go to register activity
                            dialog.dismiss();

                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.popBackStack();

                            Intent intent = new Intent(getActivity(), RegisterUser.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.register_later),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        // check if we have to compare to the previous session
        //comparePreviousSession = args.getBoolean(COMPARE_PREVIOUS);
        boolean comparePreviousSession = true;

        /**
         * Show info about evaluations for every area.
         */
        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.area_scales_recycler_view);
        ReviewAreaCard adapter = new ReviewAreaCard(getActivity(), session, comparePreviousSession);
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

