package com.example.rafael.appprototype.Tutorial;

import android.app.DatePickerDialog;
import android.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.R;

import java.util.Calendar;
import java.util.Random;

public class CreatePatient extends Fragment {

    RadioGroup radioGroup;
    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Bitmap bitmap;
    ImageView patientPhoto;
    private int year, month, day;
    private EditText patientName;
    private String patientGender = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.create_patient, container, false);

        patientPhoto = (ImageView) v.findViewById(R.id.patientPhoto);
        patientPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // selectImage();
            }
        });

        patientName = (EditText) v.findViewById(R.id.patientName);

        radioGroup = (RadioGroup) v.findViewById(R.id.myRadioGroup);
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

        Button setDate = (Button) v.findViewById(R.id.setDate);
        assert setDate != null;
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        return v;

    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(year, monthOfYear + 1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        // dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_patient, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                Bundle args = new Bundle();
                ((MainActivity) getActivity()).replaceFragment(PatientsMain.class, args, Constants.tag_create_new_session_for_patient);
                break;
            case R.id.action_save:
                if (patientName.getText().length() == 0) {
                    Snackbar.make(getView(), R.string.create_patient_error_name, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                if (patientGender == null) {
                    Snackbar.make(getView(), R.string.create_patient_error_gender, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                // TODO set date of birth + address + photo
                Patient patient = new Patient();
                patient.setName(patientName.getText().toString());
                patient.setAge(90);
                patient.setGuid("patient" + new Random().nextInt());
                patient.setAddress("address");
                if (patientGender.equals("male"))
                    patient.setPicture(R.drawable.male);
                else {
                    patient.setPicture(R.drawable.female);
                }
                patient.setFavorite(false);
                patient.save();

                args = new Bundle();
                ((MainActivity) getActivity()).replaceFragment(PatientsMain.class, args, Constants.tag_create_new_session_for_patient);
                Snackbar.make(getView(), R.string.create_patient_success, Snackbar.LENGTH_SHORT).show();
                break;
        }
        return true;

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
