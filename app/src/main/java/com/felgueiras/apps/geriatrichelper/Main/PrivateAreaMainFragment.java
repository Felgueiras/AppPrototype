package com.felgueiras.apps.geriatrichelper.Main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.felgueiras.apps.geriatrichelper.CGAGuide.CGAGuideMainFragment;
import com.felgueiras.apps.geriatrichelper.Constants;
import com.felgueiras.apps.geriatrichelper.HelpFeedbackAbout.HelpMainFragment;
import com.felgueiras.apps.geriatrichelper.Sessions.SessionsHistoryMainFragment;
import com.felgueiras.apps.geriatrichelper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatrichelper.Patients.PatientsMain;
import com.felgueiras.apps.geriatrichelper.Prescription.PrescriptionMainFragment;
import com.felgueiras.apps.geriatrichelper.R;
import com.felgueiras.apps.geriatrichelper.Settings.SettingsPrivate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PrivateAreaMainFragment extends Fragment {

    @BindView(R.id.patients) Button patients;
    @BindView(R.id.sessions) Button sessions;
    @BindView(R.id.prescription)   Button prescriptions;
    @BindView(R.id.cga_guide)    Button cgaGuide;
    @BindView(R.id.settings)    Button settings;
    @BindView(R.id.help)    Button help;
    @BindView(R.id.app_icon)    ImageView mImageView;



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
                    endFragment = new SessionsHistoryMainFragment();
                    navigationView.getMenu().getItem(Constants.menu_positions_sessions).setChecked(true);
                    break;
                case R.id.prescription:
                    endFragment = new PrescriptionMainFragment();
                    navigationView.getMenu().getItem(Constants.menu_positions_prescription).setChecked(true);
                    break;
                case R.id.cga_guide:
                    endFragment = new CGAGuideMainFragment();
                    navigationView.getMenu().getItem(Constants.menu_positions_cga_guide).setChecked(true);
                    break;
                case R.id.help:
                    endFragment = new HelpMainFragment();
                    navigationView.getMenu().getItem(Constants.menu_positions_help).setChecked(true);
                    break;
                case R.id.settings:
                    Intent i = new Intent(getActivity(), SettingsPrivate.class);
                    navigationView.getMenu().getItem(Constants.menu_positions_settings).setChecked(true);
                    getActivity().startActivity(i);
                    return;
            }
            ((PrivateAreaActivity) getActivity()).replaceFragmentSharedElements(endFragment, null,
                    Constants.tag_home_page_selected_page,
                    null);
        }
    };








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navigationView = ((PrivateAreaActivity) getActivity()).getNavigationView();
        navigationView.getMenu().getItem(Constants.menu_positions_home_page).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.tab_personal_area));


        // Inflate the layout for this fragment
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View view = inflater.inflate(R.layout.private_area_main_page, container, false);
        ButterKnife.bind(this,view);

        /**
         * Fetch Firebase data.
         */
        FirebaseHelper.initializeFirebase();

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

