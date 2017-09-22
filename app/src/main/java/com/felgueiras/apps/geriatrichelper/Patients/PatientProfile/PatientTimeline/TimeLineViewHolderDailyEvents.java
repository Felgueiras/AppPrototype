package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolderDailyEvents extends RecyclerView.ViewHolder {

    View view;
    @BindView(R.id.time_marker)
    public TimelineView mTimelineView;

    @BindView(R.id.date)
    public TextView date;

    @BindView(R.id.prescriptionsForDate)
    public RecyclerView prescriptionsForDate;

    @BindView(R.id.sessionsForDate)
    public RecyclerView sessionsForDate;


    public TimeLineViewHolderDailyEvents(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
