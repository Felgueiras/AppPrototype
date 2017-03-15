package com.example.rafael.appprototype.Prescription.Stopp;

import android.app.Activity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class StoppSingleDrugInfo extends RecyclerView.Adapter<StoppSingleDrugInfo.MyViewHolder>{

    private Activity context;
    /**
     * Data to be displayed.
     */
    private ArrayList<PrescriptionStopp> infos;



    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {


        private final RecyclerView drugIssues;

        public MyViewHolder(View view) {
            super(view);
            drugIssues= (RecyclerView) view.findViewById(R.id.issues);
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     * @param context
     * @param infos
     */
    public StoppSingleDrugInfo(Activity context, ArrayList<PrescriptionStopp> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_info_stopp, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PrescriptionStopp drugInfo = infos.get(position);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.drugIssues.setLayoutManager(layoutManager);

        StoppDrugIssuesAdapter adapter = new StoppDrugIssuesAdapter(context, drugInfo.getIssues());
        holder.drugIssues.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        holder.drugIssues.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public int getItemCount() {
        return infos.size();
    }


}
