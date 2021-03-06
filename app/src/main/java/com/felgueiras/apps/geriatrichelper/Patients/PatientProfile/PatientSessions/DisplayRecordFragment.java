package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientSessions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.felgueiras.apps.geriatrichelper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatrichelper.R;


public class DisplayRecordFragment extends Fragment {
    private Session session;

    // newInstance constructor for creating fragment with arguments
    public static DisplayRecordFragment newInstance(int page, String title, Session session) {
        DisplayRecordFragment fragmentFirst = new DisplayRecordFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putSerializable("session", session);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = (Session) getArguments().getSerializable("session");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        // populate the ListView
        ListView lv = view.findViewById(R.id.recordInfo);
        // create the adapter
        ScalesListAdapter adapter = new ScalesListAdapter(this, session.getScalesFromSession());
        lv.setAdapter(adapter);
        return view;
    }
}