package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.Evaluations.EvaluationsHistoryMain;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ViewPager.ReviewAreaFragment;
import com.example.rafael.appprototype.HelpersHandlers.DatesHandler;
import com.example.rafael.appprototype.Patients.SinglePatient.ViewSinglePatientInfo;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;


public class ReviewSingleSessionWithPatientPager extends Fragment {

    public static String COMPARE_PREVIOUS;
    /**
     * Session object
     */
    private Session session;
    /**
     * String that identifies the Session to be passed as argument.
     */
    public static String SESSION = "session";
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_patient_session, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.session_erase));
                alertDialog.setMessage(getResources().getString(R.string.session_erase_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // erase session
                                FragmentManager fragmentManager = getActivity().getFragmentManager();
                                int index = fragmentManager.getBackStackEntryCount() - 1;
                                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                                String tag = backEntry.getName();

                                fragmentManager.popBackStack();
                                Patient patient = session.getPatient();
                                dialog.dismiss();

                                DrawerLayout layout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                Snackbar.make(layout, getResources().getString(R.string.session_erase_snackbar), Snackbar.LENGTH_SHORT).show();

                                Fragment fragment = null;
                                if (tag.equals(Constants.tag_review_session_from_sessions_list)) {
                                    // go back to sessions list
                                    fragment = new EvaluationsHistoryMain();
                                } else if (tag.equals(Constants.tag_review_session_from_patient_profile)) {
                                    // go back to PATIENT profile
                                    Bundle args = new Bundle();
                                    args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                                    fragment = new ViewSinglePatientInfo();
                                    fragment.setArguments(args);
                                }


                                Fragment currentFragment = fragmentManager.findFragmentById(R.id.current_fragment);
                                fragmentManager.beginTransaction()
                                        .remove(currentFragment)
                                        .replace(R.id.current_fragment, fragment)
                                        .commit();

                                session.delete();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;

        }
        return true;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        SharedPreferencesHelper.unlockSessionCreation(getActivity());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.view_pager, container, false);
        Bundle args = getArguments();
        // get Session and Patient
        session = (Session) args.getSerializable(SESSION);

        if (session.getPatient() != null) {
            getActivity().setTitle(session.getPatient().getName() + " - " + DatesHandler.dateToStringWithoutHour(session.getDate()));
        } else {
            getActivity().setTitle(DatesHandler.dateToStringWithoutHour(session.getDate()));
        }

        // check if we have to compare to the previous session
        //comparePreviousSession = args.getBoolean(COMPARE_PREVIOUS);
        boolean comparePreviousSession = true;

//        EditText sessionNotes = (EditText) view.findViewById(R.id.session_notes);
//        // if question is already answered
//        if (session.getNotes() != null)
//            if (!session.getNotes().equals("")) sessionNotes.setText(session.getNotes());


//        sessionNotes.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                session.setNotes(charSequence.toString());
//                session.save();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


//        /**
//         * Show info about evaluations for every area.
//         */
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
//        ReviewAreaCard adapter = new ReviewAreaCard(getActivity(), session, comparePreviousSession);
//        int numbercolumns = 1;
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);

        /**
         * Setup view pager.
         */
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.container);
        MyPageAdapter pageAdapter = new MyPageAdapter(getChildFragmentManager());
        setupViewPager(viewPager, pageAdapter);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(Constants.progressPage);

        /**
         * Setup tabs.
         */
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        return view;
    }

    public void setupViewPager(ViewPager viewPager, MyPageAdapter adapter) {
        for (int i = 0; i < Constants.cga_areas.length; i++) {
            // sessions for this area
            adapter.addFragment(ReviewAreaFragment.newInstance(Constants.cga_areas[i],
                    session), getActivity().getResources().getString(R.string.drugs_beers));
        }
        viewPager.setAdapter(adapter);
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private ArrayList<String> mFragmentTitleList = new ArrayList<>();

        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Constants.cga_areas[position];
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

}

