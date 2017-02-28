package com.example.rafael.appprototype.Evaluations.SingleArea;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
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

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class CGAAreaPublic extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";
    public static String CGA_AREA = "area";

    Patient patientForThisSession;
    /**
     * Session object
     */
    private Session session;

    public static String sessionObject = "session";

    boolean resuming = false;

    public static FloatingActionButton resetFAB;
    private String area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area_public, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        area = args.getString(CGA_AREA);
        session = (Session) args.getSerializable(sessionObject);
        getActivity().setTitle(area);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        ScaleCard adapter;
        RecyclerView.Adapter finalAdapter = null;

        // read patient for this session
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

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_area, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_info:
//                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setTitle(area);
//                // add info about this area
//                String area_text = null;
//                switch (area) {
//                    case Constants.cga_afective:
//                        area_text = getActivity().getResources().getString(R.string.cga_afective);
//                        break;
//                    case Constants.cga_clinical:
//                        area_text = Constants.clinical_evaluation_tips + "\n" + Constants.clinical_evaluation_what_to_do;
//                        break;
//                    case Constants.cga_cognitive:
//                        area_text = getActivity().getResources().getString(R.string.cga_cognitive);
//                        break;
//                    case Constants.cga_functional:
//                        area_text = getActivity().getResources().getString(R.string.cga_functional);
//                        break;
//                    case Constants.cga_nutritional:
//                        area_text = getActivity().getResources().getString(R.string.cga_nutritional);
//                        break;
//                    case Constants.cga_social:
//                        area_text = getActivity().getResources().getString(R.string.cga_social);
//                        break;
//                }
//                alertDialog.setMessage(area_text);
//                alertDialog.show();
//        }
//        return true;
//
//    }

}

