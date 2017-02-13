package com.example.rafael.appprototype.Patients.ReviewEvaluation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest.ReviewArea;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;


public class ReviewSingleSession extends Fragment {

    /**
     * Patient for this Session
     */
    Patient patient;
    /**
     * Session object
     */
    private Session session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.content_review_session, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (Session) args.getSerializable(SESSION);
        getActivity().setTitle(session.getDate()+"");

        patient = session.getPatient();
        // create a new Fragment to hold info about the Patient
        Fragment fragment = new ViewSinglePatientOnlyInfo();
        Bundle newArgs = new Bundle();
        newArgs.putSerializable(ViewSinglePatientOnlyInfo.PATIENT, patient);
        fragment.setArguments(newArgs);
        FragmentManager fragmentManager = getFragmentManager();
        /*
        fragmentManager.beginTransaction()
                .replace(R.id.patientInfo, fragment)
                .commit();
                */

        /**
         * Show info about evaluations for every area.
         */
        RecyclerView recyclerView = (RecyclerView) myInflatedView.findViewById(R.id.area_scales_recycler_view);
        ReviewArea adapter = new ReviewArea(getActivity(), session);
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return myInflatedView;
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

