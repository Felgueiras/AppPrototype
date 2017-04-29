package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientPrescriptions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.R;
import com.github.vipulasri.timelineview.TimelineView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolderPrescription extends RecyclerView.ViewHolder {

    View view;
    @InjectView(R.id.prescriptionDate)
    TextView date;
    @InjectView(R.id.prescriptionsForDate)
    RecyclerView prescriptionsForDate;
//    @InjectView(R.id.prescriptionNotes)
//    @InjectView(R.id.prescriptionName)
//    TextView name;
    @InjectView(R.id.time_marker)
    TimelineView mTimelineView;
//    EditText notes;
//    @InjectView(R.id.warning)
//    Button warning;

    public TimeLineViewHolderPrescription(View itemView, int viewType) {
        super(itemView);
        this.view = itemView;

        ButterKnife.inject(this, itemView);
        mTimelineView.initLine(viewType);
    }
}
