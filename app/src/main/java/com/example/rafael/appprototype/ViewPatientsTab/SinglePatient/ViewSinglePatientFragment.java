package com.example.rafael.appprototype.ViewPatientsTab.SinglePatient;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests.NewSessionFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSinglePatientFragment extends Fragment {

    public static final String PATIENT = "patient";
    /**
     * Patient to be displayed
     */
    private Patient patient;
    /**
     * RecyclerView to display the Patient's Records
     */
    private RecyclerView recyclerView;
    /**
     * Adapter to the RecyclerView
     */
    private ViewPatientRecordsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_patient_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newSession:
                Bundle args = new Bundle();
                args.putSerializable(NewSessionFragment.PATIENT, patient);
                ((MainActivity) getActivity()).replaceFragment(NewSessionFragment.class, args);
                break;
        }
        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_main, container, false);

        // get patient
        Bundle bundle = getArguments();
        patient = (Patient) bundle.getSerializable(PATIENT);
        getActivity().setTitle(patient.getName());

        // set Patient date
        TextView patientName = (TextView) view.findViewById(R.id.patientName);
        patientName.setText(patient.getName());

        // set Patient age
        TextView patientAge = (TextView) view.findViewById(R.id.patientAge);
        patientAge.setText(patient.getAge() + "");

        // set Patient address
        TextView patientAddress = (TextView) view.findViewById(R.id.patientAddress);
        patientAddress.setText(patient.getAddress());

        // set Patient photo
        ImageView patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);
        patientPhoto.setImageResource(patient.getPicture());
        /*
        if (patient.getGender() == Constants.MALE) {
            patientPhoto.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.male));
        } else {
            patientPhoto.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.female));
        }*/


        recyclerView = (RecyclerView) view.findViewById(R.id.patientRecords);

        // get list of Records from this patient
        ArrayList<Session> recordsFromPatient = patient.getRecordsFromPatient();
        adapter = new ViewPatientRecordsAdapter(getActivity(), recordsFromPatient);

        // create Layout
        int numbercolumns = 3;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

