package com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseRemoteConfig;
import com.felgueiras.apps.geriatrichelper.R;


public class HelpMainFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set the title
        getActivity().setTitle(FirebaseRemoteConfig.getString("help",
                getResources().getString(R.string.help)));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.help_main, container, false);

        // get list view
        ListView helpTopicsList = view.findViewById(R.id.help_topics_list);

        HelpTopicsAdapter adapter = new HelpTopicsAdapter(getActivity());
        helpTopicsList.setAdapter(adapter);

        return view;
    }
}
