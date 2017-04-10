package com.felgueiras.apps.geriatric_helper.Evaluations.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Evaluations.DisplayTest.ScaleFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.ArrayList;

/**
 * Display a List of the resume for each GeriatricScale inside a Sesssion.
 */
public class AreaScalesIconsAdapter extends RecyclerView.Adapter<AreaScalesIconsAdapter.ScaleIconHolder> {
    /**
     * Questions for a Test
     */
    private final ArrayList<GeriatricScaleFirebase> scales;
    private static LayoutInflater inflater = null;
    private final SessionFirebase session;
    private final Activity context;

    /**
     * Display all Questions for a GeriatricScale
     * @param scales  ArrayList of Questions
     * @param session
     */
    public AreaScalesIconsAdapter(Activity context, ArrayList<GeriatricScaleFirebase> scales, SessionFirebase session) {
        this.scales = scales;
        this.session = session;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ScaleIconHolder extends RecyclerView.ViewHolder {
        private final Button scaleIcon;

        public ScaleIconHolder(View view) {
            super(view);
            scaleIcon = (Button) view.findViewById(R.id.scale_icon);
        }
    }


    @Override
    public ScaleIconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.area_scale_icon, parent, false);
        return new ScaleIconHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScaleIconHolder holder, int position) {
        final GeriatricScaleFirebase currentScaleDB = scales.get(position);
        final String scaleName = currentScaleDB.getScaleName();
        GeriatricScaleNonDB currentScaleNonDB = Scales.getScaleByName(scaleName);

        // update views
        holder.scaleIcon.setText(currentScaleNonDB.getIconName());
        // check completion
        if (currentScaleDB.isCompleted()) {
            holder.scaleIcon.setBackgroundResource(R.drawable.scale_icon_completed);
        } else {
            holder.scaleIcon.setBackgroundResource(R.drawable.scale_icon_incomplete);
        }
        holder.scaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaleName.equals(Constants.test_name_mini_nutritional_assessment_global)) {
                    // check if triagem is already answered
                    Log.d("Nutritional", "Global pressed");

                    // TODo
//                    GeriatricScale triagem = session.getScaleByName(Constants.test_name_mini_nutritional_assessment_triagem);
//                    if (!triagem.isCompleted()) {
//                        Snackbar.make(holder.scaleIcon, "Precisa primeiro de completar a triagem", Snackbar.LENGTH_SHORT).show();
//                        return;
//                    }
                }

                // Create new fragment and transaction
                Fragment newFragment = new ScaleFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(scaleName));
                bundle.putSerializable(ScaleFragment.SCALE, currentScaleDB);
                bundle.putSerializable(ScaleFragment.CGA_AREA, currentScaleDB.getArea());
                bundle.putSerializable(ScaleFragment.patient, FirebaseHelper.getPatientFromSession(session));
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.current_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_session_scale_shortcut).commit();
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return scales.size();
    }


}