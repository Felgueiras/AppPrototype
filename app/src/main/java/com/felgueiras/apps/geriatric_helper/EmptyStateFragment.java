package com.felgueiras.apps.geriatric_helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rafael on 21-11-2016.
 */
public class EmptyStateFragment extends Fragment {

    public static final String MESSAGE = "MESSAGE";


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_empty_state, container, false);

        Bundle bundle = getArguments();
        String messageToDisplay = bundle.getString(MESSAGE, "Empty state");
        TextView emptyStateTextView = (TextView) view.findViewById(R.id.emptyStateText);
        emptyStateTextView.setText(messageToDisplay);


        return view;
    }


}
