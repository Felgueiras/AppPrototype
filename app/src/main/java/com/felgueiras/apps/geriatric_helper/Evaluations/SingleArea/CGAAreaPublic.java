package com.felgueiras.apps.geriatric_helper.Evaluations.SingleArea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas.CGAPublicInfo;
import com.felgueiras.apps.geriatric_helper.Evaluations.ReviewEvaluation.ReviewSingleSessionNoPatient;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.R;


public class CGAAreaPublic extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "PATIENT";
    public static String CGA_AREA = "area";

    Patient patientForThisSession;

    public static String sessionObject = "session";

    boolean resuming = false;
    private Session session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area_public_bottom_buttons, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        String area = args.getString(CGA_AREA);
        session = (Session) args.getSerializable(sessionObject);
        getActivity().setTitle(area);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        ScaleCard adapter;
        RecyclerView.Adapter finalAdapter = null;

        // read PATIENT for this session
        if (patientForThisSession != null) {
            adapter = new ScaleCard(getActivity(), session, resuming, patientForThisSession.getGender(), area);
        } else {
            // new evaluation created for no Patient
            adapter = new ScaleCard(getActivity(), session, resuming, Constants.SESSION_GENDER, area);
        }
        finalAdapter = adapter;

        // display the different scales to choose from this area
        int numbercolumns = 1;
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            numbercolumns = 2;
        }
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (area != Constants.cga_clinical) {
            recyclerView.setAdapter(finalAdapter);
        } else {
            recyclerView.setAdapter(new ClinicalEvaluation(getActivity(),
                    session,
                    resuming,
                    Constants.SESSION_GENDER,
                    area));
        }

        Button finishSession = (Button) view.findViewById(R.id.session_finish);
        finishSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSession();
            }
        });

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.menu_cga_public, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.session_finish:
                finishSession();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void finishSession() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_reset));
        alertDialog.setMessage(getResources().getString(R.string.session_reset_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        session.eraseScalesNotCompleted();
                        Snackbar.make(getView(), "Sessão terminada", Snackbar.LENGTH_SHORT).show();

                        SharedPreferencesHelper.lockSessionCreation(getActivity());


                        if (session.getScalesFromSession().size() == 0) {
                            SharedPreferencesHelper.resetPublicSession(getActivity(), session.getGuid());

                            BackStackHandler.clearBackStack();
                            FragmentManager fragmentManager = getFragmentManager();
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                            Fragment fragment = new CGAPublicInfo();
                            fragmentManager.beginTransaction()
                                    .remove(currentFragment)
                                    .replace(R.id.current_fragment, fragment)
                                    .commit();
                        } else {
                            Session sessionCopy = session;
                            SharedPreferencesHelper.resetPublicSession(getActivity(), null);

                            BackStackHandler.clearBackStack();

                            FragmentManager fragmentManager = BackStackHandler.getFragmentManager();
//                            fragmentManager.popBackStack();
                            Bundle args = new Bundle();
                            args.putSerializable(ReviewSingleSessionNoPatient.SESSION, sessionCopy);
                            Fragment fragment = new ReviewSingleSessionNoPatient();
                            fragment.setArguments(args);
                            Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                            fragmentManager.popBackStack();
                            fragmentManager.beginTransaction()
                                    .remove(currentFragment)
                                    .replace(R.id.current_fragment, fragment)
//                                            .addToBackStack(Constants.tag_review_session_public)
                                    .commit();
                        }

                        dialog.dismiss();

                        // Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


}

