package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientTimeline.Cards;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PrescriptionFirebase;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions.PatientPrescriptionsAdapter;
import com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions.TimeLineViewHolderPrescription;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.ArrayList;

public class PatientPrescriptionCardTimeline extends RecyclerView.Adapter<TimeLineViewHolderPrescription> {

    private Activity context;

    private ArrayList<PrescriptionFirebase> prescriptions;


    public PatientPrescriptionCardTimeline(Activity context,
                                           ArrayList<PrescriptionFirebase> prescriptions,
                                           PatientFirebase patient) {
        this.context = context;
        this.prescriptions = prescriptions;
    }


    @Override
    public TimeLineViewHolderPrescription onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_prescription, parent, false);
        return new TimeLineViewHolderPrescription(itemView, 0);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolderPrescription holder, int position) {

        // hide unnecessary views
        holder.mTimelineView.setVisibility(View.GONE);
        holder.date.setVisibility(View.GONE);


        holder.prescriptionsForDate.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.prescriptionsForDate.setLayoutManager(layoutManager);

        PatientPrescriptionsAdapter adapter = new PatientPrescriptionsAdapter(context, prescriptions,
                null, null, true);
        holder.prescriptionsForDate.setAdapter(adapter);


    }


    @Override
    public int getItemCount() {
        return 1;
    }
}
