package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.DatesHandler;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.Orientation;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.utils.VectorDrawableUtils;
import com.felgueiras.apps.geriatrichelper.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class PrescriptionsDailyAdapter extends RecyclerView.Adapter<TimeLineViewHolderPrescription> {

    private final Activity context;
    private final boolean compactView;
    private final PatientPrescriptionsTimelineFragment fragment;
    private ArrayList<PrescriptionFirebase> prescriptionsList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Date> differentDates;

    public PrescriptionsDailyAdapter(ArrayList<PrescriptionFirebase> feedList, Orientation orientation, boolean withLinePadding, Activity activity, boolean compactView, PatientPrescriptionsTimelineFragment fragment) {
        prescriptionsList = feedList;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        context = activity;
        this.compactView = compactView;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolderPrescription onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.item_timeline_prescription, parent, false);
        return new TimeLineViewHolderPrescription(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolderPrescription holder, int position) {

        // get the Date
        Date currentDate = differentDates.get(position);

        // get prescriptions from this date
        ArrayList<PrescriptionFirebase> prescriptionsForDate = new ArrayList<>();
        for (PrescriptionFirebase prescription : prescriptionsList) {
            if (DatesHandler.getDateWithoutHour(prescription.getDate().getTime()).compareTo(currentDate) == 0) {
                prescriptionsForDate.add(prescription);
            }
        }

        // check number of warnings
        checkWarnings(holder, prescriptionsForDate);


        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));

        holder.date.setVisibility(View.VISIBLE);
        holder.date.setText(DatesHandler.dateToStringWithoutHour(currentDate));

        // get recycler view for prescriptions
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        holder.prescriptionsForDate.setLayoutManager(mLayoutManager);
        holder.prescriptionsForDate.setItemAnimator(new DefaultItemAnimator());

        PatientPrescriptionsAdapter mTimeLineAdapter = new PatientPrescriptionsAdapter(context, prescriptionsForDate,
                null, fragment, compactView);
        holder.prescriptionsForDate.setAdapter(mTimeLineAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                mLayoutManager.getOrientation());
        holder.prescriptionsForDate.addItemDecoration(dividerItemDecoration);


    }

    /**
     * Check if any of the prescriptions for the current date contains associated warnings.
     *
     * @param holder
     * @param prescriptionsForDate
     */
    private void checkWarnings(TimeLineViewHolderPrescription holder, ArrayList<PrescriptionFirebase> prescriptionsForDate) {

        int numWarnings = 0;
        for (PrescriptionFirebase prescription : prescriptionsForDate) {
            if (PrescriptionFirebase.checkWarning(prescription.getName(), null)) {
                numWarnings++;
            }
        }
        if (numWarnings == 0) {
            holder.numberWarnings.setVisibility(View.GONE);
            holder.warning.setVisibility(View.GONE);
        } else {
            holder.numberWarnings.setText(numWarnings + " X ");
        }
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
