package com.felgueiras.apps.geriatric_helper.Main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.felgueiras.apps.geriatric_helper.CGAGuide.CGAGuideMain;
import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Choice;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.GeriatricScale;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Question;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Evaluations.EvaluationsHistoryMain;
import com.felgueiras.apps.geriatric_helper.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Help_Feedback.HelpMain;
import com.felgueiras.apps.geriatric_helper.Patients.PatientsMain;
import com.felgueiras.apps.geriatric_helper.Prescription.PrescriptionMain;
import com.felgueiras.apps.geriatric_helper.R;
import com.felgueiras.apps.geriatric_helper.Settings;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PrivateAreaMainFragment extends Fragment {


    private static Gson gson;

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


    private NavigationView navigationView;
    /**
     * Listener for the button clicks.
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment endFragment = null;

            switch (v.getId()) {
                case R.id.patients:
                    endFragment = new PatientsMain();
                    navigationView.getMenu().getItem(Constants.menu_positions_patients).setChecked(true);
                    break;
                case R.id.sessions:
                    endFragment = new EvaluationsHistoryMain();
                    navigationView.getMenu().getItem(Constants.menu_positions_sessions).setChecked(true);
                    break;
                case R.id.prescription:
                    endFragment = new PrescriptionMain();
                    navigationView.getMenu().getItem(Constants.menu_positions_prescription).setChecked(true);
                    break;
                case R.id.cga_guide:
                    endFragment = new CGAGuideMain();
                    navigationView.getMenu().getItem(Constants.menu_positions_cga_guide).setChecked(true);
                    break;
                case R.id.help:
                    endFragment = new HelpMain();
                    navigationView.getMenu().getItem(Constants.menu_positions_help).setChecked(true);
                    break;
                case R.id.settings:
                    Intent i = new Intent(getActivity(), Settings.class);
                    navigationView.getMenu().getItem(Constants.menu_positions_settings).setChecked(true);
                    getActivity().startActivity(i);
                    return;
            }
            ((PrivateAreaActivity) getActivity()).replaceFragmentSharedElements(endFragment, null,
                    Constants.tag_home_page_selected_page,
                    null);
        }
    };


    @InjectView(R.id.patients)
    Button patients;
    @InjectView(R.id.sessions)
    Button sessions;
    @InjectView(R.id.prescription)
    Button prescriptions;
    @InjectView(R.id.cga_guide)
    Button cgaGuide;
    @InjectView(R.id.settings)
    Button settings;
    @InjectView(R.id.help)
    Button help;
    @InjectView(R.id.app_icon)
    ImageView mImageView;


    public static void getScalesInJson(Context context) {


        ArrayList<GeriatricScaleNonDB> scales = Scales.getAllScales();
        for (int i = 0; i < scales.size(); i++) {
            String jsonArray = gson.toJson(scales.get(i));

            String fileName = scales.get(i).getShortName() + ".json";
            // upload file
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.
                    getReferenceFromUrl("gs://appprototype-bdd27.appspot.com")
                    .child("scales/" + fileName);

            UploadTask uploadTask = storageReference.putBytes(jsonArray.getBytes());
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navigationView = ((PrivateAreaActivity) getActivity()).getNavigationView();
        navigationView.getMenu().getItem(Constants.menu_positions_home_page).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.tab_personal_area));

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).setPrettyPrinting();
        gson = builder.create();


        // Inflate the layout for this fragment
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View view = inflater.inflate(R.layout.private_area_main_page, container, false);
        ButterKnife.inject(this, view);

        /**
         * Fetch Firebase data.
         */
        FirebaseHelper.fetchPatients();
        FirebaseHelper.fetchFavoritePatients();
        FirebaseHelper.fetchSessions();
        FirebaseHelper.fetchScales();
        FirebaseHelper.fetchQuestions();



        /**
         * Set the image drawables - this had to be done to avoid errors in lower API versions.
         */
        patients.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_people_white_24dp);
        sessions.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_sessions_white_24dp);
        prescriptions.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.pill_white);
        help.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_help_white_24dp);
        cgaGuide.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_search_white_24dp);
        settings.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_settings_white_24dp);


        patients.setOnClickListener(clickListener);
        sessions.setOnClickListener(clickListener);
        prescriptions.setOnClickListener(clickListener);
        cgaGuide.setOnClickListener(clickListener);
        settings.setOnClickListener(clickListener);
        help.setOnClickListener(clickListener);

        return view;
    }


}

