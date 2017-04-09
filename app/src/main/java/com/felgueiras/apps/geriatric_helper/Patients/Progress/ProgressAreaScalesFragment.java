package com.felgueiras.apps.geriatric_helper.Patients.Progress;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ProgressAreaScalesFragment extends Fragment {


    private static String PATIENT = "PATIENT";
    private static String AREA = "AREA";
    /**
     * Patient for this Session
     */
    private Patient patient;


    public static Fragment newInstance(String area, Patient patient) {
        ProgressAreaScalesFragment f = new ProgressAreaScalesFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable(PATIENT, patient);
        bdl.putString(AREA, area);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String area = getArguments().getString(AREA);
        Patient patient = (Patient) getArguments().getSerializable(PATIENT);

        View testCard = inflater.inflate(R.layout.content_progress_area, null);

//        TextView areatextView = (TextView) testCard.findViewById(R.id.area);
        RecyclerView scales = (RecyclerView) testCard.findViewById(R.id.area_scales);

//        areatextView.setText(area);

        /**
         * Show info about evaluations for every area.
         */
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String progressType = SP.getString(getActivity().getResources().getString(R.string.patientProgressType), "2");
        ArrayList<Session> patientSessions = patient.getSessionsFromPatient();
        if (progressType.equals("2")) {
            ProgressScalesForAreaGraph adapter = new ProgressScalesForAreaGraph(getActivity(), patientSessions, area, patient);
            int numbercolumns = 1;
            if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                numbercolumns = 2;
            }
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
            scales.setLayoutManager(mLayoutManager);
            scales.setItemAnimator(new DefaultItemAnimator());
            scales.setAdapter(adapter);
        } else if (progressType.equals("1")) {
//            ProgressScalesForAreaTable adapter = new ProgressScalesForAreaTable(getActivity(), patientSessions, area, patient);
//            // display the different scales to choose from this area
//            int numbercolumns = 1;
//            if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
//                numbercolumns = 2;
//            }
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
//            scales.setLayoutManager(mLayoutManager);
//            scales.setItemAnimator(new DefaultItemAnimator());
//            scales.setAdapter(adapter);
        }
        return testCard;
    }


}
