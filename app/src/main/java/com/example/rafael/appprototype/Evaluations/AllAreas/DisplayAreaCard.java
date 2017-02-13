package com.example.rafael.appprototype.Evaluations.AllAreas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Evaluations.SingleArea.Area;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class DisplayAreaCard extends RecyclerView.Adapter<DisplayAreaCard.CGACardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    private final int patientGender;

    private Activity context;


    /**
     * Create a View
     */
    public class CGACardHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView name, type, cgaCompletion;
        public View view;
        public EditText notes;

        public CGACardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.cga_area);
            //type = (TextView) view.findViewById(R.id.testType);
            cgaCompletion = (TextView) view.findViewById(R.id.cga_completion);
            //addNotesButton = (EditText) view.findViewById(R.id.testNotes);
            this.view = view;
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context       current Context
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     */
    public DisplayAreaCard(Activity context, Session session, boolean resuming, int patientGender) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
    }

    @Override
    public CGACardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.cga_card, parent, false);
        return new CGACardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final CGACardHolder holder, int position) {

        // access a given Test from the DB
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();


        holder.name.setText(Constants.cga_areas[position]);


        /*
        if (currentTest.hasNotes()) {
            holder.addNotesButton.setText(currentTest.getNotes());
        }
        */

        /**
         * For when the CGA area is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedArea = (String) holder.name.getText();
                // Create new fragment and transaction
                Fragment newFragment = new Area();
                // add arguments
                Bundle bundle = new Bundle();

                Patient patient = session.getPatient();
                System.out.println("Patient here is " + patient);
                if (patient != null)
                    bundle.putSerializable(Area.PATIENT, patient);

                bundle.putSerializable(Area.sessionObject, session);
                bundle.putString(Area.CGA_AREA, selectedArea);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_single_area).commit();
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
