package com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.R;

public class AboutFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about, container, false);
        getActivity().setTitle(getResources().getString(R.string.about));
        return view;
    }

}

