package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 05-10-2016.
 */
public class ViewSinglePatientOnlyInfo extends Fragment {

    public static final String PATIENT = "patient";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.patient_info_data, container, false);
        // get patient
        Bundle bundle = getArguments();
        /*
      Patient to be displayed
     */
        Patient patient = (Patient) bundle.getSerializable(PATIENT);
        getActivity().setTitle(patient.getName());

        // set Patient date
        TextView patientName = (TextView) view.findViewById(R.id.patientName);
        patientName.setText(patient.getName());

        // set Patient age
        TextView patientAge = (TextView) view.findViewById(R.id.patientAge);
        patientAge.setText(DatesHandler.dateToStringWithoutHour(patient.getBirthDate()) + "");

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

        return view;
    }
}

