package com.felgueiras.apps.geriatric_helper.Patients.Progress;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Patient;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ProgressAreasAdapter extends RecyclerView.Adapter<ProgressAreasAdapter.TestCardHolder> {


    /**
     * Patient for this Session
     */
    private final Patient patient;
    private final ArrayList<Session> patientSessions;
    /**
     * Context.
     */
    private Activity context;


    public ProgressAreasAdapter(Activity context, ArrayList<Session> patientSessions, Patient patient) {
        this.context = context;
        this.patient = patient;
        this.patientSessions = patientSessions;
    }

    /**
     * CGACardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        public TextView area;
        public RecyclerView scales;

        /**
         * Create a CGACardHolder from a View
         *
         * @param view
         */
        public TestCardHolder(View view) {
            super(view);
//            area = (TextView) view.findViewById(R.id.area);
            scales = (RecyclerView) view.findViewById(R.id.area_scales);
        }
    }


    /**
     * Create a CGACardHolder from a View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the Test CardView
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_progress_area, parent, false);
        return new TestCardHolder(testCard);
    }

    /**
     * Display the data at a certain position
     *
     * @param holder   data to be displayed
     * @param position position to "put" the data
     */
    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
//        holder.area.setText(Constants.cga_areas[position]);

        /**
         * Show info about evaluations for every area.
         */
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String progressType = SP.getString(context.getResources().getString(R.string.patientProgressType), "1");
        if (progressType.equals("2")) {
            ProgressScalesForAreaGraph adapter = new ProgressScalesForAreaGraph(context, patientSessions, Constants.cga_areas[position], patient);
            int numbercolumns = 1;
            if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                numbercolumns = 2;
            }
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
            holder.scales.setLayoutManager(mLayoutManager);
            holder.scales.setItemAnimator(new DefaultItemAnimator());
            holder.scales.setAdapter(adapter);
        } else if (progressType.equals("1")) {
//            ProgressScalesForAreaTable adapter = new ProgressScalesForAreaTable(context, patientSessions, Constants.cga_areas[position], patient);
//            // display the different scales to choose from this area
//            int numbercolumns = 1;
//            if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
//                numbercolumns = 2;
//            }
//            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
//            holder.scales.setLayoutManager(mLayoutManager);
//            holder.scales.setItemAnimator(new DefaultItemAnimator());
//            holder.scales.setAdapter(adapter);
        }

    }


    @Override
    public int getItemCount() {
        return Constants.cga_areas.length;
    }

}
