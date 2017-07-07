package com.felgueiras.apps.geriatric_helper.HelpFeedbackAbout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.R;

/**
 * Created by felgueiras on 18/02/2017.
 */
public class HelpTopicsAdapter extends BaseAdapter {
    private final Activity context;
    private final LayoutInflater inflater;

    public HelpTopicsAdapter(Activity context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // setup views
        View view = inflater.inflate(R.layout.help_topic, parent, false);
        TextView helpTextView = view.findViewById(R.id.help_text);

        //String helpText = context.getResources().getString(R.string.help_cga_description);
        final String helpTopic = Constants.help_topics[position];
        // get text from RemoteConfig
        String helpTopicString = null;
        switch (position) {
            case 0:
                helpTopicString = FirebaseRemoteConfig.getString("help_topic_cga", "");
                break;
            case 1:
                helpTopicString = FirebaseRemoteConfig.getString("help_topic_functionalities", "");
                break;
            case 2:
                helpTopicString = FirebaseRemoteConfig.getString("help_topic_personal_area", "");
                break;
            case 3:
                helpTopicString = FirebaseRemoteConfig.getString("help_topic_patients", "");
                break;
            case 4:
                helpTopicString = FirebaseRemoteConfig.getString("help_topic_bibliography", "");
                break;
//            case 5:
//                text = FirebaseRemoteConfig.getString("help_topic_prescriptions", "");
//                break;
//            case 6:
//                text = FirebaseRemoteConfig.getString("help_topic_cga_guide", "");
//                break;
//            case 7:
//                text = FirebaseRemoteConfig.getString("help_topic_bibliography", "");
//                break;
        }

        helpTopicString = helpTopic;

        helpTextView.setText(helpTopicString);
        final String finalText = helpTopicString;
        helpTextView.getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString(HelpSingleTopic.TOPIC, helpTopic);
                args.putString(HelpSingleTopic.TOPIC_TEXT, finalText);
                FragmentTransitions.replaceFragment(context, new HelpSingleTopic(), args, Constants.tag_help_topic);
            }
        });


        return view;
    }

    @Override
    public int getCount() {
        return Constants.help_topics.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
