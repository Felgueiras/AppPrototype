package com.example.rafael.appprototype.Evaluations.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPrivate;
import com.example.rafael.appprototype.Evaluations.SingleArea.CGAAreaPublic;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * Create the Card for each of the Tests available
 */
public class AreaCard extends RecyclerView.Adapter<AreaCard.CGACardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;

    private Activity context;


    /**
     * Create a View
     */
    public class CGACardHolder extends RecyclerView.ViewHolder implements Serializable {
        private final ImageButton infoButton;
        public TextView name, type, cgaCompletion;
        public View view;
        public EditText notes;

        public CGACardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.cga_area);
            //type = (TextView) view.findViewById(R.id.testType);
            infoButton = (ImageButton) view.findViewById(R.id.area_info);
            cgaCompletion = (TextView) view.findViewById(R.id.cga_completion);
            //addNotesButton = (EditText) view.findViewById(R.id.testNotes);
            this.view = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context       current Context
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     */
    public AreaCard(Activity context, Session session, boolean resuming, int patientGender) {
        this.context = context;
        this.session = session;
    }

    @Override
    public CGACardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.cga_area_card, parent, false);
        return new CGACardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final CGACardHolder holder, int position) {

        final String area = Constants.cga_areas[position];

        holder.name.setText(area);

        /**
         * Check if all scales were completed.
         */
        ArrayList<GeriatricScale> scalesFromArea = session.getScalesFromArea(area);
        Log.d("Area", "Size " + scalesFromArea.size());
        boolean allCompleted = true;
        for (GeriatricScale scale : scalesFromArea) {
            if (!scale.isCompleted()) {
                allCompleted = false;
                break;
            }
        }
        if (!allCompleted) {
            Log.d("Area", "Not all scales completed " + area);
        } else {
            Log.d("Area", "All scales completed " + area);
            holder.cgaCompletion.setText("Todas as escalas foram preenchidas");

        }


        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(area);
                // add info about this area
                String area_text = null;
                switch (area) {
                    case Constants.cga_mental:
                        area_text = context.getResources().getString(R.string.cga_mental);
                        break;
                    case Constants.cga_clinical:
                        area_text = Constants.clinical_evaluation_tips + "\n" + Constants.clinical_evaluation_what_to_do;
                        break;
                    case Constants.cga_functional:
                        area_text = context.getResources().getString(R.string.cga_functional);
                        break;
                    case Constants.cga_nutritional:
                        area_text = context.getResources().getString(R.string.cga_nutritional);
                        break;
                    case Constants.cga_social:
                        area_text = context.getResources().getString(R.string.cga_social);
                        break;
                }
                alertDialog.setMessage(area_text);
                alertDialog.show();
            }
        });

        /**
         * For when the CGA area is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedArea = (String) holder.name.getText();
                // Create new fragment and transaction
                SharedPreferences sharedPreferences = context.getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
                boolean alreadyLogged = sharedPreferences.getBoolean(Constants.logged_in, false);
                if (alreadyLogged) {
                    Fragment newFragment = new CGAAreaPrivate();
                    // add arguments
                    Bundle bundle = new Bundle();
                    Patient patient = session.getPatient();
                    if (patient != null)
                        bundle.putSerializable(CGAAreaPrivate.PATIENT, patient);

                    bundle.putSerializable(CGAAreaPrivate.sessionObject, session);
                    bundle.putString(CGAAreaPrivate.CGA_AREA, selectedArea);
                    newFragment.setArguments(bundle);
                    // setup the transaction
                    FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                    Fragment frag = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                    transaction.remove(frag);
                    transaction.replace(R.id.current_fragment, newFragment);
                    transaction.addToBackStack(Constants.tag_display_single_area_private).commit();
                } else {
                    Fragment newFragment = new CGAAreaPublic();
                    // add arguments
                    Bundle bundle = new Bundle();

                    Patient patient = session.getPatient();
                    if (patient != null)
                        bundle.putSerializable(CGAAreaPublic.PATIENT, patient);

                    bundle.putSerializable(CGAAreaPublic.sessionObject, session);
                    bundle.putString(CGAAreaPublic.CGA_AREA, selectedArea);
                    newFragment.setArguments(bundle);
                    // setup the transaction
                    FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                    Fragment frag = context.getFragmentManager().findFragmentById(R.id.current_fragment);
                    transaction.remove(frag);
                    transaction.replace(R.id.current_fragment, newFragment);
                    transaction.addToBackStack(Constants.tag_display_single_area_public).commit();
                }

            }
        });

        /**
         * Add a listener for when a note is added.
         */
        /*
        holder.addNotesButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentTest.setNotes(charSequence.toString());
                currentTest.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return Constants.cga_areas.length;

    }

}
