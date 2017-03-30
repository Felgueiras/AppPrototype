package com.example.rafael.appprototype.Help_Feedback;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.R;


public class HelpSingleTopic extends Fragment {

    public static final String TOPIC = "TOPIC";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_topic_detail, container, false);

        Bundle arguments = getArguments();
        String helpTopic = arguments.getString(TOPIC);

        // set the title
        getActivity().setTitle(helpTopic);

        // fill views
        TextView helpText = (TextView) view.findViewById(R.id.help_text);
        String text = "";
        switch (helpTopic) {
            case Constants.help_topic_cga:
                text = getResources().getString(R.string.help_cga_description);
                break;
            case Constants.help_topic_functionalities:
                text = getResources().getString(R.string.help_features_description);
                break;
            case Constants.help_topic_personal_area:
                text = getResources().getString(R.string.help_personal_area);
                break;
            case Constants.help_topic_patients:
                text = getResources().getString(R.string.help_patients_description);
                break;
            case Constants.help_topic_prescriptions:
                text = getResources().getString(R.string.help_precription_description);
            case Constants.help_topic_sessions:
                text = getResources().getString(R.string.help_sessions_description);
            case Constants.help_topic_cga_guide:
                text = getResources().getString(R.string.help_cga_guide_description);
                break;
        }
        helpText.setText(text);

        return view;
    }
}
