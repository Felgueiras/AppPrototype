package com.felgueiras.apps.geriatrichelper.Patients.NewPatient;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;
import com.felgueiras.apps.geriatrichelper.Sessions.AllAreas.CGAPrivate;
import com.felgueiras.apps.geriatrichelper.Sessions.ReviewSession.ReviewSingleSessionWithPatient;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.BackStackHandler;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatrichelper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CreatePatientFragment extends Fragment {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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


        patientPhoto = view.findViewById(R.id.patientPhoto);


        patientName = view.findViewById(R.id.patientName);
        patientAddress = view.findViewById(R.id.addressText);


        radioGroup = view.findViewById(R.id.myRadioGroup);
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
        day = view.findViewById(R.id.birth_date_day);
        month = view.findViewById(R.id.birth_date_month);
        year = view.findViewById(R.id.birth_date_year);

        // hospital process number
        processNumber = view.findViewById(R.id.processNumber);

        Button savePatient = view.findViewById(R.id.saveButton);
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Create PATIENT.
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

                PatientFirebase patient = new PatientFirebase();
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

                String patientID = FirebaseHelper.firebaseTablePatients.push().getKey();

                // create Patient metadata
                patient.setGuid(patientID);

                PatientsManagement.getInstance().addPatient(patient, getActivity());

                PatientsManagement.getInstance().getPatients(getActivity()).add(patient);


                Snackbar.make(getView(), R.string.create_patient_success, Snackbar.LENGTH_SHORT).show();
                if (createType == CREATE_PATIENTS_LIST) {
                    BackStackHandler.getFragmentManager().popBackStack();
                } else if (createType == CREATE_BEFORE_SESSION) {
                    getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().popBackStack();
                    Bundle args = new Bundle();
                    /*
                      Go to new session with this PATIENT.
                     */
                    args = new Bundle();
                    args.putSerializable(CGAPrivate.PATIENT, patient);
                    FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(),
                            args,
                            Constants.tag_create_session_with_patient_from_session);
                } else if (createType == CREATE_AFTER_SESSION) {
                    getActivity().getFragmentManager().popBackStack();
                    getActivity().getFragmentManager().popBackStack();
                    Bundle args = new Bundle();
                    args.putSerializable(CGAPrivate.PATIENT, patient);
                    FragmentTransitions.replaceFragment(getActivity(), new CGAPrivate(),
                            args,
                            Constants.tag_create_session_with_patient_from_session);

//                    DrawerLayout layout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
//                    Snackbar.make(layout, context.getString(R.string.picked_patient_session_created), Snackbar.LENGTH_SHORT).show();
                    // add Patient to Session
                    String sessionID = SharedPreferencesHelper.isThereOngoingPrivateSession(getActivity());

                    // get session by ID
//                    Session session = Session.getSessionByID(sessionID);
//                    session.setPatient(patient);
//                    session.eraseScalesNotCompleted();
//                    session.save();

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
                    /*
                      Review session created for PATIENT.
                     */
                    args = new Bundle();
                    args.putBoolean(ReviewSingleSessionWithPatient.COMPARE_PREVIOUS, true);
//                    args.putSerializable(ReviewSingleSessionWithPatient.SESSION, session);
                    Fragment fragment = new ReviewSingleSessionWithPatient();

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
     * Prompt user to discard the patient.
     */
    public void discardPatient() {

        // check if there is any prescription, if not, go back
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Tem a certeza de que pretente rejeitar este paciente?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Rejeitar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BackStackHandler.getFragmentManager().popBackStack();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continuar a editar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
