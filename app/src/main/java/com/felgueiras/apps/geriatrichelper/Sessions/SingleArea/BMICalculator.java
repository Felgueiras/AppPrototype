package com.felgueiras.apps.geriatrichelper.Sessions.SingleArea;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by rafae on 07/07/2017.
 */

public class BMICalculator extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bmi_calculator, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getDialog().setTitle("Calculadora de IMC");


        final EditText height = view.findViewById(R.id.bmi_height);
        final EditText weight = view.findViewById(R.id.bmi_weight);
        final TextView result = view.findViewById(R.id.bmi_result);
        final Button calculateBMI = view.findViewById(R.id.bmi_button);

        // Show soft keyboard automatically and request focus to field
        height.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (!hasFocus) {
//                    calculateBMI.performClick();
//                }
//            }
//        });


        calculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Fields validation.
                 */
                String heightText = height.getText().toString();
                String weightText = weight.getText().toString();
                if (heightText.equals("") || weightText.equals("")) {
                    Snackbar.make(getView(), "Tem de fornecer dados sobre a altura e o peso do paciente", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                float heightVal = Float.parseFloat(heightText) * 0.01f;
                int weightVal = Integer.parseInt(weightText);
                // convert height from cm to m
                float bmi = weightVal / (heightVal * heightVal);


                // display result
                result.setText(String.valueOf(Math.round(bmi)));
            }
        });
    }
}
