package com.example.rafael.appprototype.Evaluations;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluationPrivate;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;

import java.util.Calendar;

public class PublicEvaluations extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_evaluations_public, container, false);
        getActivity().setTitle(getResources().getString(R.string.tab_sessions));



        // FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_evaluation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog chooseGender;

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {getString(R.string.male), getString(R.string.female)};

                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.select_patient_gender));
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Bundle args = new Bundle();

                        switch (item) {
                            case 0:
                                args.putInt(NewEvaluationPrivate.GENDER, Constants.MALE);
                                break;
                            case 1:
                                args.putInt(NewEvaluationPrivate.GENDER, Constants.FEMALE);
                                break;

                        }
                        dialog.dismiss();
                        // create a new Session - switch to CreatePatient Fragment
                        FragmentTransitions.replaceFragment(getActivity(), new NewEvaluationPrivate(), args, Constants.tag_create_session);
                    }
                });
                chooseGender = builder.create();
                chooseGender.show();
            }
        });
        

        return view;
    }
}

