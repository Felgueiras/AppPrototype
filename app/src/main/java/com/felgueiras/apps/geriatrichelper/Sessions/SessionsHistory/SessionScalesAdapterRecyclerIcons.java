package com.felgueiras.apps.geriatrichelper.Sessions.SessionsHistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.DataTypes.Scales;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.R;

import java.util.List;

/**
 * Display a List of the resume for each GeriatricScale inside a Session.
 */
public class SessionScalesAdapterRecyclerIcons extends RecyclerView.Adapter<SessionScalesAdapterRecyclerIcons.MyViewHolder> {
    /**
     * Questions for a Test
     */
    private final List<GeriatricScaleFirebase> sessionScales;
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView testName;
        private final TextView testResult;
        private final LinearLayout card;
        public TextView patientName;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.testResultCard);
            testName = view.findViewById(R.id.scaleName);
            testResult = view.findViewById(R.id.testResult);
        }
    }

    /**
     * Display all Questions for a GeriatricScale
     *
     * @param tests ArrayList of Questions
     */
    public SessionScalesAdapterRecyclerIcons(Context context, List<GeriatricScaleFirebase> tests) {
        this.sessionScales = tests;
        Context context1 = context;
    }


    @Override
    public SessionScalesAdapterRecyclerIcons.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.scale_icon_name, parent, false);
        return new SessionScalesAdapterRecyclerIcons.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionScalesAdapterRecyclerIcons.MyViewHolder holder, int position) {

        // get values
        GeriatricScaleFirebase scale = sessionScales.get(position);
        String name = Scales.getShortName(scale.getScaleName());
        // update views
        holder.testName.setText(name);

        /*
          If a ClickListener was passed, add it
         */
        if (onClickListener != null) {
            holder.card.setOnClickListener(onClickListener);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sessionScales.size();
    }


}