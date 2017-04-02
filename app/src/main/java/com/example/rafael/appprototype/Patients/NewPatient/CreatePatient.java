package com.example.rafael.appprototype.Patients.NewPatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.AllAreas.CGAPrivateBottomButtons;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ViewPager.ReviewSingleSessionWithPatientBottomButtons;
import com.example.rafael.appprototype.HelpersHandlers.BackStackHandler;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.HelpersHandlers.SharedPreferencesHelper;
import com.example.rafael.appprototype.Main.FragmentTransitions;
import com.example.rafael.appprototype.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CreatePatient extends Fragment {

    public static final String CREATE_PATIENT_TYPE = "CREATE_PATIENT_TYPE";
    // create patient before session starts
    public static final int CREATE_BEFORE_SESSION = 0;
    // create patient after session ends
    public static final int CREATE_AFTER_SESSION = 1;
    // create patient in patients list
    public static final int CREATE_PATIENTS_LIST = 2;

    RadioGroup radioGroup;
    private Bitmap bitmap;
    ImageView patientPhoto;
    private EditText year, month, day;
    private EditText patientName;
    private String patientGender = null;
    private EditText patientAddress;
    private EditText processNumber;
    private int createType;


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
                 * Create PATIENT.
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
                if (Integer.parseInt(dayText) > 31 || Integer.parseInt(monthText) > 12) {
                    Snackbar.make(getView(), R.string.create_patient_error_invalid_birthDate, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(yearText),
                        Integer.parseInt(monthText) - 1,
                        Integer.parseInt(dayText));
                Date selectedDate = c.getTime();

                if (patientGender == null) {
                    Snackbar.make(getView(), R.string.create_patient_error_gender, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                if (patientAddress.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_address, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                Patient patient = new Patient();
                patient.setName(patientName.getText().toString());

                patient.setBirthDate(selectedDate);
                patient.setGuid("PATIENT" + new Random().nextInt());
                patient.setAddress(patientAddress.getText().toString());
                if (patientGender.equals("male")) {
                    patient.setPicture(R.drawable.male);
                    patient.setGender(Constants.MALE);
                } else {
                    patient.setPicture(R.drawable.female);
                    patient.setGender(Constants.FEMALE);
                }
                patient.setProcessNumber(processNumber.getText().toString());
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
//        inflater.inflate(R.menu.menu_create_patient, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createType = CREATE_PATIENTS_LIST;
        if (getArguments() != null && getArguments().containsKey(CREATE_PATIENT_TYPE))
            createType = getArguments().getInt(CREATE_PATIENT_TYPE);


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

        // hospital process number
        processNumber = (EditText) view.findViewById(R.id.processNumber);

        Button savePatient = (Button) view.findViewById(R.id.savePatient);
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Create PATIENT.
                 */

                if (patientName.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_name, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // birth date validation
                String dayText = day.getText().toString();
                String monthText = month.getText().toString();
                String yearText = year.getText().toString();
                if (dayText.equals("") || monthText.equals("") || yearText.equals("")) {
                    Snackbar.make(getView(), R.string.create_patient_error_no_birthDate, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(dayText) > 31 || Integer.parseInt(monthText) > 12) {
                    Snackbar.make(getView(), R.string.create_patient_error_invalid_birthDate, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(yearText),
                        Integer.parseInt(monthText) - 1,
                        Integer.parseInt(dayText));
                Date selectedDate = c.getTime();

                if (patientGender == null) {
                    Snackbar.make(getView(), R.string.create_patient_error_gender, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (patientAddress.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_address, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (processNumber.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_process_number, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Patient patient = new Patient();
                patient.setName(patientName.getText().toString());

                patient.setBirthDate(selectedDate);
                patient.setGuid("PATIENT" + new Random().nextInt());
                patient.setAddress(patientAddress.getText().toString());
                if (patientGender.equals("male")) {
                    patient.setPicture(R.drawable.male);
                    patient.setGender(Constants.MALE);
                } else {
                    patient.setPicture(R.drawable.female);
                    patient.setGender(Constants.FEMALE);
                }
                patient.setProcessNumber(processNumber.getText().toString());
                patient.setFavorite(false);
                patient.save();

                Snackbar.make(getView(), R.string.create_patient_success, Snackbar.LENGTH_SHORT).show();
                if (createType == CREATE_PATIENTS_LIST) {
                    BackStackHandler.goToPreviousScreen();
                } else if (createType == CREATE_BEFORE_SESSION) {
                    getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().popBackStack();
                    Bundle args = new Bundle();
                    /**
                     * Go to new session with this PATIENT.
                     */
                    args = new Bundle();
                    args.putSerializable(CGAPrivateBottomButtons.PATIENT, patient);
                    FragmentTransitions.replaceFragment(getActivity(), new CGAPrivateBottomButtons(),
                            args,
                            Constants.tag_create_session_with_patient_from_session);
                } else if (createType == CREATE_AFTER_SESSION) {
                    getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().popBackStack();
                    Bundle args = new Bundle();
                    args.putSerializable(CGAPrivateBottomButtons.PATIENT, patient);
                    FragmentTransitions.replaceFragment(getActivity(), new CGAPrivateBottomButtons(),
                            args,
                            Constants.tag_create_session_with_patient_from_session);

//                    DrawerLayout layout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
//                    Snackbar.make(layout, context.getString(R.string.picked_patient_session_created), Snackbar.LENGTH_SHORT).show();
                    // add Patient to Session
                    String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(getActivity());

                    // get session by ID
                    Session session = Session.getSessionByID(sessionID);
                    session.setPatient(patient);
                    session.eraseScalesNotCompleted();
                    session.save();

                    // reset current private session
                    SharedPreferencesHelper.resetPrivateSession(getActivity(), "");

                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                    fragmentManager.beginTransaction()
                            .remove(currentFragment)
                            .commit();
//                    fragmentManager.popBackStack();
                    BackStackHandler.clearBackStack();
//                    Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
//                    fragmentManager.beginTransaction()
//                            .remove(currentFragment)
//                            .replace(R.id.current_fragment, new PatientsMain())
//                            .commit();
                    /**
                     * Review session created for PATIENT.
                     */
                    args = new Bundle();
                    args.putBoolean(ReviewSingleSessionWithPatientBottomButtons.COMPARE_PREVIOUS, true);
                    args.putSerializable(ReviewSingleSessionWithPatientBottomButtons.SESSION, session);
                    Fragment fragment = new ReviewSingleSessionWithPatientBottomButtons();

                    fragment.setArguments(args);
                    currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                    fragmentManager.beginTransaction()
                            .remove(currentFragment)
                            .replace(R.id.current_fragment, fragment)
                            .addToBackStack(Constants.tag_review_session_from_sessions_list)
                            .commit();
                }
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
