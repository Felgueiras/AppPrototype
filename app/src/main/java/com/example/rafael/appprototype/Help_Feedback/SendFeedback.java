package com.example.rafael.appprototype.Help_Feedback;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.R;


public class SendFeedback extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.send_feedback, container, false);

        // set the title
        getActivity().setTitle(getResources().getString(R.string.send_feedback));
        return v;
    }
}
