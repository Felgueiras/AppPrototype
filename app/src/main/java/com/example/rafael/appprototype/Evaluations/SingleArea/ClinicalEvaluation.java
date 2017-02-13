package com.example.rafael.appprototype.Evaluations.SingleArea;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.ArrayList;


public class ClinicalEvaluation extends RecyclerView.Adapter<ClinicalEvaluation.TestCardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    private final int patientGender;
    /**
     * CGA area.
     */
    private final String area;

    private Activity context;
    private ArrayList<GeriatricTestNonDB> testsForArea;


    /**
     * Create a View
     */
    public class TestCardHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView field;
        public EditText notes;
        public View view;

        public TestCardHolder(View view) {
            super(view);
            this.view = view;
            field = (TextView) view.findViewById(R.id.field);
            notes = (EditText) view.findViewById(R.id.notes);
        }
    }

    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_clinical_evaluation_field, parent, false);
        return new TestCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
        holder.field.setText((position + 1) + " - " + Constants.clinical_evaluation_fields[position]);

        /**
         * Add a listener for when a note is added.
         */
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //currentTest.setNotes(charSequence.toString());
                //currentTest.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context       current Context
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     * @param area
     */
    public ClinicalEvaluation(Activity context, Session session, boolean resuming, int patientGender, String area) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
        this.area = area;
    }


    @Override
    public int getItemCount() {
        return Constants.clinical_evaluation_fields.length;
    }

}
