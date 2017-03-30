package com.example.rafael.appprototype.Main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rafael.appprototype.CGAGuide.CGAGuideMain;
import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.CustomViews.MainPageCustomButton;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Help_Feedback.HelpMain;
import com.example.rafael.appprototype.Patients.PatientsMain;
import com.example.rafael.appprototype.Prescription.DrugPrescriptionMain;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.Settings;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Display the list of Patients to view them or select one of them.
 */
public class PrivateAreaMainFragment extends Fragment {


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
                    endFragment = new DrugPrescriptionMain();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        navigationView = ((PrivateAreaActivity) getActivity()).getNavigationView();
        navigationView.getMenu().getItem(Constants.menu_positions_home_page).setChecked(true);
        getActivity().setTitle(getResources().getString(R.string.tab_personal_area));


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.private_area_main_page, container, false);
        ButterKnife.inject(this, view);

//        // Initializes the side spinner from code.
//        SideSpinner fruitsSpinner;
//        fruitsSpinner = (SideSpinner) view.findViewById(R.id.sidespinner_fruits);
//
//        CharSequence fruitList[] = {"Apple",
//                "Orange",
//                "Pear",
//                "Grapes"};
//        fruitsSpinner.setValues(fruitList);
//        fruitsSpinner.setSelectedIndex(1);


//
        patients.setOnClickListener(clickListener);
        sessions.setOnClickListener(clickListener);
        prescriptions.setOnClickListener(clickListener);
        cgaGuide.setOnClickListener(clickListener);
        settings.setOnClickListener(clickListener);
        help.setOnClickListener(clickListener);

        return view;
    }


}

