package com.felgueiras.apps.geriatric_helper.Sessions.SingleArea;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.FreeTextField;
import com.felgueiras.apps.geriatric_helper.DataTypes.DB.Session;
import com.felgueiras.apps.geriatric_helper.R;

import java.io.Serializable;


public class ClinicalEvaluation extends RecyclerView.Adapter<ClinicalEvaluation.TestCardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;


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
            field = view.findViewById(R.id.field);
            notes = view.findViewById(R.id.notes);
        }
    }

    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_clinical_evaluation_field, parent, false);
        return new TestCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
        String fieldName = Constants.clinical_evaluation_fields[position];
        holder.field.setText((position + 1) + " - " + fieldName);

        // create a new record in the DB
        String fieldID = session.getGuid() + "-" + fieldName;
        FreeTextField field = FreeTextField.getFieldByID(fieldID);
        final FreeTextField clinicalField;
        if (field != null) {
            clinicalField = field;
            holder.notes.setText(clinicalField.getNotes());

        } else {
            /**
             * Create new.
             */
            clinicalField = new FreeTextField(fieldName);
            clinicalField.setGuid(fieldID);
            clinicalField.setSession(session);
            clinicalField.save();
        }


        /**
         * Add a listener for when a note is added.
         */
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clinicalField.setNotes(charSequence.toString());
                clinicalField.save();
                System.out.println(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context       current Context
     * @param resuming      true if we are resuming a Session
     * @param patientGender
     * @param area
     */
    public ClinicalEvaluation(Activity context, Session session, boolean resuming, int patientGender, String area) {
        Activity context1 = context;
        this.session = session;
        int patientGender1 = patientGender;
        /*
      CGA area.
     */
        String area1 = area;
    }


    @Override
    public int getItemCount() {
        return Constants.clinical_evaluation_fields.length;
    }

}
