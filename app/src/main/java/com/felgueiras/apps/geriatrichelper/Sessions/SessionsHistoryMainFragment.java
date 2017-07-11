package com.felgueiras.apps.geriatrichelper.Sessions;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.EmptyStateFragment;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.R;

public class SessionsHistoryMainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.evaluations_history, container, false);
        getActivity().setTitle(getResources().getString(R.string.evaluations));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FloatingActionButton fab = view.findViewById(R.id.new_evaluation_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new Session - switch to CreatePatientFragment Fragment
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.new_session_private);

                //list of items
                String[] items = new String[]{"Sem paciente", "Com paciente"};
                builder.setSingleChoiceItems(items, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // item selected logic
                                SharedPreferencesHelper.unlockSessionCreation(getActivity());
                                if (which == 0) {
                                    dialog.dismiss();
                                    FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(), null,
                                            Constants.tag_create_session_no_patient);
                                } else {
                                    // pick PATIENT
//                                    fragmentManager.popBackStack();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(PickPatientFragment.PICK_BEFORE_SESSION, true);
                                    FragmentTransitions.replaceFragment(getActivity(), new PickPatientFragment(),
                                            bundle,
                                            Constants.tag_create_session_pick_patient_start);
                                    dialog.dismiss();
                                }
                            }
                        });

                String negativeText = getString(android.R.string.cancel);
                builder.setNegativeButton(negativeText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // negative button logic
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment;
        /**
         * Setup fragment for FrameLayout.
         */
        if (FirebaseHelper.getSessions().isEmpty()) {
            fragment = new EmptyStateFragment();
            Bundle args = new Bundle();
            args.putString(EmptyStateFragment.MESSAGE, getResources().getString(R.string.no_sessions_history));
            fragment.setArguments(args);
        } else {
            fragment = new EvaluationsAllFragment();
        }
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.evaluation_history_frame_layout);
        if (currentFragment != null)
            transaction.remove(currentFragment);
        transaction.replace(R.id.evaluation_history_frame_layout, fragment);
        transaction.commit();
    }

}

