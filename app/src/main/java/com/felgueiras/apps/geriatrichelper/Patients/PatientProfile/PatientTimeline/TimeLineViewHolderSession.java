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
public class TimeLineViewHolderSession extends RecyclerView.ViewHolder {

    public View view;

    @BindView(R.id.text_timeline_date)
    public TextView mDate;
    @BindView(R.id.time_marker)
    public TimelineView mTimelineView;
    @BindView(R.id.session_tests_results)
    public RecyclerView sessionScales;

    public TimeLineViewHolderSession(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
