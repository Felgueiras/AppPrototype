package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

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
public class TimeLineViewHolderSession extends RecyclerView.ViewHolder {

    public View view;

    @InjectView(R.id.text_timeline_date)
    public TextView mDate;
    @InjectView(R.id.time_marker)
    public TimelineView mTimelineView;
    @InjectView(R.id.session_tests_results)
    public RecyclerView sessionScales;

    public TimeLineViewHolderSession(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.inject(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
