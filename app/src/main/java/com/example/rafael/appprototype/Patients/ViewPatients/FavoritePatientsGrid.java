package com.example.rafael.appprototype.Patients.ViewPatients;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 03-10-2016.
 */
public class FavoritePatientsGrid extends BaseAdapter {
    /**
     * All the Patients
     */
    private ArrayList<Patient> patients;
    Activity context;

    public FavoritePatientsGrid(Activity context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Patient patient = patients.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // each view is a Fragment layout that holds a Fragment with a Recycler View inside
        View card = inflater.inflate(R.layout.patient_card_grid, null);

        ImageView icon = (ImageView) card.findViewById(R.id.patientIcon);
        final TextView name = (TextView) card.findViewById(R.id.patientName);

        name.setText(patient.getName());
        // holder.type.setText(patient.getAge());


        // loading album cover using Glide library
        //Glide.with(context).load(patient.getPicture()).into(holder.icon);

        // add on click listener for the icon
        switch (patient.getGender())
        {
            case Constants.MALE:
                icon.setImageResource(R.drawable.male);
                break;
            case Constants.FEMALE:
                icon.setImageResource(R.drawable.female);
                break;
        }


        View.OnClickListener clickListener =  new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {
                /**
                 * Pick a patient to be associated with a Session.
                 */


                // TODO add shared elements for transitions
                Fragment endFragment = new ViewSinglePatientInfo();
                    /*
                    endFragment.setSharedElementReturnTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));


                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));
                    */


//                    patientTransitionName = holder.questionTextView.getTransitionName();
                Bundle args = new Bundle();
//                args.putString("ACTION", holder.questionTextView.getText().toString());
//                args.putString("TRANS_TEXT", patientTransitionName);
                args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                ((PrivateArea) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patient_info_records,
                        name);

            }
        };

        name.setOnClickListener(clickListener);
        icon.setOnClickListener(clickListener);



        return card;
    }


    @Override
    public int getCount() {
        return patients.size();
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