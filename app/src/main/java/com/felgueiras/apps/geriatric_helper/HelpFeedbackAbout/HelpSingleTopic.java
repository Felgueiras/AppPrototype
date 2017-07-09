package com.felgueiras.apps.geriatric_helper.HelpFeedbackAbout;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseStorageHelper;
import com.felgueiras.apps.geriatric_helper.R;


public class HelpSingleTopic extends Fragment {

    public static final String TOPIC = "TOPIC";
    public static final String TOPIC_TEXT = "TOPIC_TEXT";
    private VideoView vidView;
    private ProgressBar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_topic_detail, container, false);
        vidView = view.findViewById(R.id.myVideo);


        Bundle arguments = getArguments();
        String helpTopic = arguments.getString(TOPIC);
        String helpTopicText = arguments.getString(TOPIC_TEXT);

        // set the title
        getActivity().setTitle(helpTopicText);

        // fill views
        TextView helpText = view.findViewById(R.id.help_text);
        String text = "";
        switch (helpTopic) {
            case Constants.help_topic_cga:
                text = FirebaseRemoteConfig.getString("help_cga_description",
                        "");
                break;
            case Constants.help_topic_functionalities:
                text = FirebaseRemoteConfig.getString("help_functionalitites_description",
                        "");
                break;
            case Constants.help_topic_personal_area:
                text = FirebaseRemoteConfig.getString("help_personal_area",
                        "");
                break;
            case Constants.help_topic_patients:
                text = FirebaseRemoteConfig.getString("help_patients_description",
                        "");
                break;
            case Constants.help_topic_prescriptions:
                text = FirebaseRemoteConfig.getString("help_precription_description",
                        "");
                break;
            case Constants.help_topic_sessions:
                text = FirebaseRemoteConfig.getString("help_sessions_description",
                        "");
                break;

            case Constants.help_topic_cga_guide:
                text = FirebaseRemoteConfig.getString("help_cga_guide_description",
                        "");
                break;
            case Constants.help_topic_bibliography:
                text = FirebaseRemoteConfig.getString("help_topic_bibliography_description",
                        "");
                break;
            case Constants.help_topic_cga_definition:
                text = getActivity().getString(R.string.cga_definition);
                break;
            case Constants.help_topic_cga_objective:
                text = getActivity().getString(R.string.cga_objective);
                break;
            case Constants.help_topic_cga_when:
                text = getActivity().getString(R.string.cga_when);
                break;
            case Constants.help_topic_cga_who:
                text = getActivity().getString(R.string.cga_who);
                break;
            case Constants.help_topic_cga_how:
                text = getActivity().getString(R.string.cga_how);
                break;
            case Constants.help_topic_cga_instruments:
                text = getActivity().getString(R.string.cga_instruments);
                break;

        }
        helpText.setText(text);

        bar = view.findViewById(R.id.progressBar);

//        FirebaseStorageHelper.fetchVideoDisplay(helpTopic, vidView, bar, getActivity());


        return view;
    }
}
