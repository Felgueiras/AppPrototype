package com.felgueiras.apps.geriatric_helper.Sessions.SingleArea;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SessionHelper;
import com.felgueiras.apps.geriatric_helper.R;


public class CGAAreaPrivate extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "PATIENT";
    public static String CGA_AREA = "area";

    PatientFirebase patientForThisSession;

    public static String SESSION = "session";

    boolean resuming = false;


    private SessionFirebase session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.menu_cga_private_patient, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area_private_bottom_buttons, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (PatientFirebase) args.getSerializable(PATIENT);

        String area = args.getString(CGA_AREA);
        session = (SessionFirebase) args.getSerializable(SESSION);
        getActivity().setTitle(area);

        RecyclerView recyclerView = view.findViewById(R.id.area_scales_recycler_view);
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
        if (!area.equals(Constants.cga_clinical)) {
            recyclerView.setAdapter(finalAdapter);
        }
//        else {
//            recyclerView.setAdapter(new ClinicalEvaluation(getActivity(),
//                    session,
//                    resuming,
//                    Constants.SESSION_GENDER,
//                    area));
//        }


        Button saveButton = view.findViewById(R.id.session_save);
        Button cancelButton = view.findViewById(R.id.session_cancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View layout =  getActivity().findViewById(R.id.newSessionLayout);
                SessionHelper.saveSession(getActivity(),session,patientForThisSession, getView(), layout,2);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewForSnackbar = getView();
                SessionHelper.cancelSession(getActivity(), session, viewForSnackbar, Constants.AREA);
            }
        });

        return view;
    }



}

