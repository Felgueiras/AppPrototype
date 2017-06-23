package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolderPrescription extends RecyclerView.ViewHolder {

    View view;
    @BindView(R.id.prescriptionDate)
    public TextView date;
    @BindView(R.id.numberWarnings)
    public TextView numberWarnings;
    @BindView(R.id.prescriptionsForDate)
    public RecyclerView prescriptionsForDate;

    @BindView(R.id.time_marker)
    public TimelineView mTimelineView;
    @BindView(R.id.warning)
    public Button warning;

    public TimeLineViewHolderPrescription(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.bind(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
