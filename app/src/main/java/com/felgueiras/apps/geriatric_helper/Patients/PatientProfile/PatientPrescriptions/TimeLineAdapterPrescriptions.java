package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatric_helper.Main.FragmentTransitions;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.Orientation;
import com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatric_helper.Prescription.PrescriptionSingleDrugFragment;
import com.felgueiras.apps.geriatric_helper.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapterPrescriptions extends RecyclerView.Adapter<TimeLineViewHolderPrescription> {

    private final Activity context;
    private final boolean compactView;
    private ArrayList<PrescriptionFirebase> prescriptionsList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Date> differentDates;

    public TimeLineAdapterPrescriptions(ArrayList<PrescriptionFirebase> feedList, Orientation orientation, boolean withLinePadding, Activity activity, boolean compactView) {
        prescriptionsList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        context = activity;
        this.compactView = compactView;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolderPrescription onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;


        view = mLayoutInflater.inflate(R.layout.item_timeline_prescription, parent, false);


        return new TimeLineViewHolderPrescription(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolderPrescription holder, int position) {

        // get the Date
        Date currentDate = differentDates.get(position);

        // get prescriptions from this date
        ArrayList<PrescriptionFirebase> prescriptionForDate = new ArrayList<>();
        for (PrescriptionFirebase prescription : prescriptionsList) {
            if (DatesHandler.getDateWithoutHour(prescription.getDate().getTime()).compareTo(currentDate) == 0) {
                prescriptionForDate.add(prescription);
            }
        }


        // get the current date
//        final PrescriptionFirebase prescription = prescriptionsList.get(position);

        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));

        holder.date.setVisibility(View.VISIBLE);
        holder.date.setText(DatesHandler.dateToStringWithoutHour(currentDate));

        // get recycler view for prescriptions
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        holder.prescriptionsForDate.setLayoutManager(mLayoutManager);
        holder.prescriptionsForDate.setItemAnimator(new DefaultItemAnimator());

        PatientPrescriptionsCard mTimeLineAdapter = new PatientPrescriptionsCard(context, prescriptionForDate,
                null, null, compactView);
        holder.prescriptionsForDate.setAdapter(mTimeLineAdapter);


    }

    @Override
    public int getItemCount() {
        // get different prescription dates
        HashSet<Date> days = new HashSet<>();
        for (PrescriptionFirebase prescriptionFirebase : prescriptionsList) {
            Date dateWithoutHour = DatesHandler.getDateWithoutHour(prescriptionFirebase.getDate().getTime());
            days.add(dateWithoutHour);
        }

        differentDates = new ArrayList<>();
        differentDates.addAll(days);
        // order by date (descending)
        Collections.sort(differentDates, new Comparator<Date>() {
            @Override
            public int compare(Date first, Date second) {
                return second.compareTo(first);
            }
        });

        return days.size();
    }

}
