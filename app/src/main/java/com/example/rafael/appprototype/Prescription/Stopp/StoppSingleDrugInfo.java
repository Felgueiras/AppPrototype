package com.example.rafael.appprototype.Prescription.Stopp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
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


        private final ListView drugIssues;

        public MyViewHolder(View view) {
            super(view);
            drugIssues= (ListView) view.findViewById(R.id.issues);
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


        StoppDrugIssues adapter = new StoppDrugIssues(context, drugInfo.getIssues());
        holder.drugIssues.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return infos.size();
    }


}
