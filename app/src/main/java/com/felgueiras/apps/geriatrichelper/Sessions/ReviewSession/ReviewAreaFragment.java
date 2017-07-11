package com.felgueiras.apps.geriatrichelper.Sessions.ReviewSession;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.DataTypes.Scales;
import com.felgueiras.apps.geriatrichelper.Sessions.ReviewSession.ReviewSingleTest.ReviewScaleCard;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatrichelper.R;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewAreaFragment extends Fragment {


    private static String SESSION = "SESSION";
    private static String AREA = "AREA";
    /**
     * Patient for this Session
     */
    private PatientFirebase patient;


    public static Fragment newInstance(String area, SessionFirebase session) {
        ReviewAreaFragment f = new ReviewAreaFragment();
        Bundle bdl = new Bundle();
        bdl.putSerializable(SESSION, session);
        bdl.putString(AREA, area);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String area = getArguments().getString(AREA);
        SessionFirebase session = (SessionFirebase) getArguments().getSerializable(SESSION);

        View areaCard = inflater.inflate(R.layout.content_review_area, null);

        RecyclerView scales = areaCard.findViewById(R.id.area_scales);


//        String area = Constants.cga_areas[position];

        // check if the session had any scale from this area
        if (Scales.getScalesForArea(area).size() == 0) {
//            ViewManager parentView = (ViewManager) holder.area.getParent();
//            if (parentView != null) {
//                parentView.removeView(holder.area);
//                parentView.removeView(holder.scales);
//            }
        } else {
            /**
             * Show info about evaluations for every area.
             */
            ReviewScaleCard adapter = new ReviewScaleCard(getActivity(), session, area, true);
            int numbercolumns = 1;
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
            scales.setLayoutManager(mLayoutManager);
            scales.setItemAnimator(new DefaultItemAnimator());
            scales.setAdapter(adapter);
        }
        return areaCard;
    }


}
