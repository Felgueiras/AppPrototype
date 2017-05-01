package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolderDailyEvents extends RecyclerView.ViewHolder {

    View view;
    @InjectView(R.id.time_marker)
    public TimelineView mTimelineView;

    @InjectView(R.id.date)
    public TextView date;

    @InjectView(R.id.prescriptionsForDate)
    public RecyclerView prescriptionsForDate;

    @InjectView(R.id.sessionsForDate)
    public RecyclerView sessionsForDate;


    public TimeLineViewHolderDailyEvents(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.inject(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
