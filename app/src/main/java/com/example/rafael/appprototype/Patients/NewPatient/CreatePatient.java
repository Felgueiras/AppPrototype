package com.example.rafael.appprototype.Patients.NewPatient;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CreatePatient extends Fragment {

    RadioGroup radioGroup;
    private Bitmap bitmap;
    ImageView patientPhoto;
    private EditText year, month, day;
    private EditText patientName;
    private String patientGender = null;
    private Button cancelFAB;
    private Button saveFAB;
    private static TextView dateView;
    private EditText patientAddress;
    private static Date selectedDate = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_patient_save:
                /**
                 * Create patient.
                 */

                if (patientName.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_name, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                // birth date validation
                String dayText = day.getText().toString();
                String monthText = month.getText().toString();
                String yearText = year.getText().toString();
                if (dayText.equals("") || monthText.equals("") || yearText.equals("")) {
                    Snackbar.make(getView(), R.string.create_patient_error_no_birthDate, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(yearText),
                        Integer.parseInt(monthText)-1,
                        Integer.parseInt(dayText));
                selectedDate = c.getTime();
                if (patientAddress.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_address, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                if (patientGender == null) {
                    Snackbar.make(getView(), R.string.create_patient_error_gender, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                // TODO set photo
                Patient patient = new Patient();
                patient.setName(patientName.getText().toString());

                patient.setBirthDate(selectedDate);
                patient.setGuid("patient" + new Random().nextInt());
                patient.setAddress(patientAddress.getText().toString());
                if (patientGender.equals("male"))
                    patient.setPicture(R.drawable.male);
                else {
                    patient.setPicture(R.drawable.female);
                }
                patient.setFavorite(false);
                patient.save();

                Snackbar.make(getView(), R.string.create_patient_success, Snackbar.LENGTH_SHORT).show();
                BackStackHandler.goToPreviousScreen();
                break;
//            switch (cancel):
//            //                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
////                //alertDialog.setTitle(getResources().getString(R.string.session_discard));
////                alertDialog.setMessage(getResources().getString(R.string.new_patient_discard));
////                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int which) {
////                                BackStackHandler.goToPreviousScreen();
////                            }
////                        });
////                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int which) {
////                                dialog.dismiss();
////                            }
////                        });
////                alertDialog.show();
//            break;


        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_create_patient, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_patient, container, false);
        getActivity().setTitle(getResources().getString(R.string.new_patient));


        patientPhoto = (ImageView) view.findViewById(R.id.patientPhoto);
        patientPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // selectImage();
            }
        });

        patientName = (EditText) view.findViewById(R.id.patientName);
        patientAddress = (EditText) view.findViewById(R.id.addressText);


        radioGroup = (RadioGroup) view.findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.maleButton) {
                    patientGender = "male";
                } else if (checkedId == R.id.femaleButton) {
                    patientGender = "female";
                }
            }
        });

        // birth date
        day = (EditText) view.findViewById(R.id.birth_date_day);
        month = (EditText) view.findViewById(R.id.birth_date_month);
        year = (EditText) view.findViewById(R.id.birth_date_year);


        return view;

    }


/**
 * Launch an AlertDialog that lets the user take a photo or select one from the device.
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
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            patientPhoto.setImageBitmap(photo);
        }
        */

}
