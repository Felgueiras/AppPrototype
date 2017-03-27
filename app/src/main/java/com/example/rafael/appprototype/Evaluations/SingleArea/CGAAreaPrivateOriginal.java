package com.example.rafael.appprototype.Evaluations.SingleArea;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.HelpersHandlers.SessionHelper;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.R;


public class CGAAreaPrivateOriginal extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";
    public static String CGA_AREA = "area";

    Patient patientForThisSession;

    public static String SESSION = "session";

    boolean resuming = false;


    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_cga_private_patient, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area_private, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        String area = args.getString(CGA_AREA);
        /*
      Session object
     */
        session = (Session) args.getSerializable(SESSION);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.session_save:
                RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.newSessionLayout);
                SessionHelper.saveSession(getActivity(),session,session.getPatient(), getView(), layout,2);
                break;
            case R.id.session_cancel:
                cancelSession();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public void cancelSession() {
        Log.d("Stack", "Cancel");
        SharedPreferencesHelper.lockSessionCreation(getActivity());

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getString(R.string.session_discard));
        alertDialog.setMessage(getResources().getString(R.string.session_discard_question));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // remove session
                        Patient p = session.getPatient();
                        // how many sessions this patient have
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.popBackStack();
                        SharedPreferencesHelper.resetPrivateSession(getActivity(), session.getGuid());
                        BackStackHandler.discardSession(p);
                        dialog.dismiss();
                        Snackbar.make(getView(), getResources().getString(R.string.session_discarded), Snackbar.LENGTH_SHORT).show();
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

