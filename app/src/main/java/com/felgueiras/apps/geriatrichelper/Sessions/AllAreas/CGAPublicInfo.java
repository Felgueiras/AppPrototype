package com.felgueiras.apps.geriatrichelper.Sessions.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout.HelpMainFragment;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.R;

import tourguide.tourguide.TourGuide;


/**
 * Display info about CGA and allow to create a new CGA session (public).
 */
public class CGAPublicInfo extends Fragment {


    private TourGuide createSessionGuide;

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
        View view = inflater.inflate(R.layout.cga_public_info, container, false);
        getActivity().setTitle(FirebaseRemoteConfig.getString("cga",
                getResources().getString(R.string.cga)));

        /**
         * Start a session.
         */
        Button startSession = view.findViewById(R.id.start_acg_evaluation);
        startSession.setText(FirebaseRemoteConfig.getString("create_session",
                getResources().getString(R.string.create_session)));

        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Session", "Clicked in CGAPublicInfo!");
                /**
                 * If first public evaluation, show alert dialog about saving sessions
                 * and registering in the app.
                 */
                Activity context = getActivity();
                boolean firstPublicEvaluation = SharedPreferencesHelper.checkFirstPublicEvaluation(getActivity());

                // TODO use in later versions
                /*if (firstPublicEvaluation) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    //alertDialog.setTitle(getResources().getString(R.string.session_discard));
                    alertDialog.setMessage(context.getResources().getString(R.string.firstPublicEvaluation));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // go to register activity
                                    dialog.dismiss();

                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStack();

                                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.register_later),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {*/

                SharedPreferencesHelper.unlockSessionCreation(getActivity());
                FragmentTransitions.replaceFragment(getActivity(), new CGAPublic(), null, Constants.tag_cga_public);
//                }

                // remove tutorial
                if (createSessionGuide != null)
                    createSessionGuide.cleanUp();


            }
        });


        /**
         * See more information.
         */
        Button moreInfo = view.findViewById(R.id.more_info);
        moreInfo.setText(FirebaseRemoteConfig.getString("more_info",
                getResources().getString(R.string.more_info)));
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransitions.replaceFragment(getActivity(), new HelpMainFragment(), null, Constants.more_info_clicked);
            }
        });
        

        // TODO
//        if (SharedPreferencesHelper.showTour(getActivity()))
//        {
//            // TourGuide
//
//            createSessionGuide = TourGuide.init(getActivity()).with(TourGuide.Technique.Click)
//                    .setPointer(new Pointer())
//                    .setToolTip(new ToolTip().setTitle("Criar Sessão AGG").setDescription("Clique aqui para iniciar uma nova sessão "))
//                    .setOverlay(new Overlay())
//                    .playOn(startSession);
//        }


        return view;
    }
}

