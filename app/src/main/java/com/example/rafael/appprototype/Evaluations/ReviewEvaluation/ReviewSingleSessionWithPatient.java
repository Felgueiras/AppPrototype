package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewArea;
import com.example.rafael.appprototype.R;


public class ReviewSingleSessionWithPatient extends Fragment {

    public static String COMPARE_PREVIOUS;
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
        View view = inflater.inflate(R.layout.content_review_session, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (Session) args.getSerializable(SESSION);
        if (session.getPatient() != null) {
            getActivity().setTitle(session.getPatient().getName() + " - " + DatesHandler.dateToStringWithoutHour(session.getDate()));
        } else {
            getActivity().setTitle(DatesHandler.dateToStringWithoutHour(session.getDate()));
        }

        // check if we have to compare to the previous session
        //comparePreviousSession = args.getBoolean(COMPARE_PREVIOUS);
        boolean comparePreviousSession = true;

        EditText sessionNotes = (EditText) view.findViewById(R.id.session_notes);
        // if question is already answered
        if (session.getNotes() != null)
            if (!session.getNotes().equals("")) sessionNotes.setText(session.getNotes());


        sessionNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                session.setNotes(charSequence.toString());
                session.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /**
         * Show info about evaluations for every area.
         */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        ReviewArea adapter = new ReviewArea(getActivity(), session, comparePreviousSession);
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

}

