package com.felgueiras.apps.geriatric_helper.Patients.PatientPrescriptions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class PatientPrescriptionCreate extends Fragment {

    public static final String PATIENT = "patient";
    public static final String DRUG = "DRUG";
    RadioGroup radioGroup;
    private TextView name;
    private EditText notes;
    PatientFirebase patient;
    private String prescription;


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
        if (getArguments() != null && getArguments().containsKey(PATIENT))
            patient = (PatientFirebase) getArguments().getSerializable(PATIENT);

        if (getArguments() != null && getArguments().containsKey(DRUG))
            prescription = getArguments().getString(DRUG);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_prescription_to_patient, container, false);
        getActivity().setTitle(getResources().getString(R.string.add_prescription));


        // get views
        name = (TextView) view.findViewById(R.id.prescriptionName);
        notes = (EditText) view.findViewById(R.id.addressText);

        if (prescription != null) {
            name.setText(prescription);
        }

        Button addPrescription = (Button) view.findViewById(R.id.saveButton);
        addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validate name
                if (name.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.add_prescription_error_name, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // get current date
                Calendar c = Calendar.getInstance();
                Date prescriptionDate = c.getTime();

                // create new Prescription object
                PrescriptionFirebase prescription = new PrescriptionFirebase(name.getText().toString(),
                        notes.getText().toString(), prescriptionDate);
                prescription.setGuid("PRESCRIPTION" + new Random().nextInt());
                prescription.setName(name.getText().toString());
                prescription.setPatientID(patient.getGuid());
                patient.addPrescription(prescription.getGuid());

                // save Prescription
                FirebaseHelper.createPrescription(prescription);

                Snackbar.make(getView(), R.string.add_prescription_success, Snackbar.LENGTH_SHORT).show();

                BackStackHandler.getFragmentManager().popBackStack();
                BackStackHandler.getFragmentManager().popBackStack();
            }
        });


        return view;

    }


/**
 * Launch an AlertDialog that lets the user take a icon or select one from the device.
 */
    /*
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePatient.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    */


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                patientPhoto.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally

            {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /*
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap app_icon = (Bitmap) data.getExtras().get("data");
            patientPhoto.setImageBitmap(app_icon);
        }
        */

}
