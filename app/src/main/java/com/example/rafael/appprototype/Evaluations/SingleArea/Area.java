package com.example.rafael.appprototype.Evaluations.SingleArea;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class Area extends Fragment {

    /**
     * Patient for this Session
     */
    public static String PATIENT = "patient";
    public static String CGA_AREA = "area";

    Patient patientForThisSession;
    /**
     * Session object
     */
    private Session session;

    public static String sessionObject = "session";

    boolean resuming = false;

    public static FloatingActionButton resetFAB;
    private static FloatingActionButton saveFAB;
    private String area;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_single_area, container, false);
        // check the Constants
        Bundle args = getArguments();
        patientForThisSession = (Patient) args.getSerializable(PATIENT);

        area = args.getString(CGA_AREA);
        session = (Session) args.getSerializable(sessionObject);
        getActivity().setTitle(area);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.area_scales_recycler_view);
        ScaleCard adapter;
        RecyclerView.Adapter finalAdapter = null;

        // read patient for this session
        if (patientForThisSession != null) {
            adapter = new ScaleCard(getActivity(), session, resuming, patientForThisSession.getGender(), area);
        } else {
            // new evaluation created for no Patient
            adapter = new ScaleCard(getActivity(), session, resuming, Constants.SESSION_GENDER, area);
        }
        finalAdapter = adapter;

        // display the different scales to choose from this area
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numbercolumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (area != Constants.cga_clinical) {
            recyclerView.setAdapter(finalAdapter);
        } else {
            recyclerView.setAdapter(new ClinicalEvaluation(getActivity(),
                    session,
                    resuming,
                    Constants.SESSION_GENDER,
                    area));
        }


        // reset the session
        resetFAB = (FloatingActionButton) view.findViewById(R.id.session_reset);
        resetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(getResources().getString(R.string.session_reset));
                alertDialog.setMessage(getResources().getString(R.string.session_reset_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // remove session
                                Area.this.session.delete();
                                Constants.sessionID = null;
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_fragment, new Area())
                                        .commit();
                                dialog.dismiss();

                                // Snackbar.make(getView(), getResources().getString(R.string.session_created), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        return view;
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_area, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(area);
                // add info about this area
                String area_text = null;
                switch (area) {
                    case Constants.cga_afetivo:
                        area_text = getContext().getResources().getString(R.string.cga_afective);
                        break;
                    case Constants.cga_clinical:
                        area_text = Constants.clinical_evaluation_tips +"\n"+Constants.clinical_evaluation_what_to_do;
                        break;
                    case Constants.cga_cognitivo:
                        area_text = getContext().getResources().getString(R.string.cga_cognitive);
                        break;
                    case Constants.cga_functional:
                        area_text = getContext().getResources().getString(R.string.cga_functional);
                        break;
                    case Constants.cga_nutritional:
                        area_text = getContext().getResources().getString(R.string.cga_nutritional);
                        break;
                    case Constants.cga_social:
                        area_text = getContext().getResources().getString(R.string.cga_social);
                        break;
                }
                alertDialog.setMessage(area_text);
                alertDialog.show();
        }
        return true;

    }

}
