package com.example.rafael.appprototype.Help_Feedback;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivate;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

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
        TextView helpTextView = (TextView) view.findViewById(R.id.help_text);

        //String helpText = context.getResources().getString(R.string.help_cga_description);
        final String helpTopic = Constants.help_topics[position];
        helpTextView.setText(helpTopic);
        helpTextView.getRootView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString(HelpSingleTopic.TOPIC, helpTopic);
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
