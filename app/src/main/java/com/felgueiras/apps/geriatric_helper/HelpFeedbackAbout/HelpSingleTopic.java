package com.felgueiras.apps.geriatric_helper.HelpFeedbackAbout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatric_helper.R;


public class HelpSingleTopic extends Fragment {

    public static final String TOPIC = "TOPIC";
    public static final String TOPIC_TEXT = "TOPIC_TEXT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_topic_detail, container, false);

        Bundle arguments = getArguments();
        String helpTopic = arguments.getString(TOPIC);
        String helpTopicText = arguments.getString(TOPIC_TEXT);

        // set the title
        getActivity().setTitle(helpTopicText);

        // fill views
        TextView helpText = (TextView) view.findViewById(R.id.help_text);
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
            case Constants.help_topic_sessions:
                text = FirebaseRemoteConfig.getString("help_sessions_description",
                        "");
            case Constants.help_topic_cga_guide:
                text = FirebaseRemoteConfig.getString("help_cga_guide_description",
                        "");
                break;
        }
        helpText.setText(text);

        return view;
    }
}
