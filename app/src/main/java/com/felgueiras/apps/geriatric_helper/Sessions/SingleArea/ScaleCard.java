package com.felgueiras.apps.geriatric_helper.Sessions.SingleArea;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.Scales;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.ScaleFragment;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.SessionFirebase;
import com.felgueiras.apps.geriatric_helper.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Create the Card for each of the Tests available
 */
public class ScaleCard extends RecyclerView.Adapter<ScaleCard.ScaleCardHolder> {

    /**
     * ID for this Session
     */
    private final SessionFirebase session;
    private final int patientGender;
    /**
     * CGA area.
     */
    private final String area;

    private Activity context;
    private ArrayList<GeriatricScaleNonDB> testsForArea;


    /**
     * Create a View
     */
    public static class ScaleCardHolder extends RecyclerView.ViewHolder implements Serializable {
        public final TextView result_quantitative;
        public final TextView notes;
        public final TextView subCategory;
        public TextView name;
        public ImageButton description;
        public TextView result_qualitative;
        public View view;
        public ImageButton addNotesButton;
        public TextView patientProgress;

        public ScaleCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            subCategory = (TextView) view.findViewById(R.id.testSubCategory);
            description = (ImageButton) view.findViewById(R.id.scale_info);
            result_qualitative = (TextView) view.findViewById(R.id.result_qualitative);
            result_quantitative = (TextView) view.findViewById(R.id.result_quantitative);
            addNotesButton = (ImageButton) view.findViewById(R.id.addNotes);
            notes = (TextView) view.findViewById(R.id.testNotes);
            patientProgress = (TextView) view.findViewById(R.id.patient_progress);
            this.view = view;
        }
    }

    /**
     * Constructor of the SessionCardEvaluationHistory
     *
     * @param context       current Context
     * @param session
     * @param reviewing     true if we are reviewing a Session
     * @param patientGender
     * @param area
     */
    public ScaleCard(Activity context, SessionFirebase session, boolean reviewing, int patientGender, String area) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
        this.area = area;
    }

    @Override
    public ScaleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.scale_card, parent, false);
        return new ScaleCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final ScaleCardHolder holder, int position) {
        // get constants
        ViewManager parentView = (ViewManager) holder.result_qualitative.getParent();
        // get scale nonDB
        GeriatricScaleNonDB scaleNonDB = testsForArea.get(position);

        GeriatricScaleFirebase currentScale = FirebaseDatabaseHelper.getScaleFromSession(session, scaleNonDB.getScaleName());

//        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);


        holder.name.setText(scaleNonDB.getShortName());

        // display subcategory for functional area scales
        if (area.equals(Constants.cga_functional)) {
            holder.subCategory.setVisibility(View.VISIBLE);
            holder.subCategory.setText(scaleNonDB.getSubCategory());
        }

        final GeriatricScaleFirebase finalCurrentTest = currentScale;

        holder.description.setOnClickListener(new ScaleInfoHelper(context, Scales.getScaleByName(currentScale.getScaleName())));

        // Test was already opened
        if (currentScale != null && currentScale.isAlreadyOpened()) {
            float selected = 1f;
            holder.view.setAlpha(selected);
            // already complete
            if (currentScale.isCompleted()) {
                // display Scoring to the user
                GradingNonDB match = Scales.getGradingForScale(
                        currentScale,
                        patientGender);
                // qualitative result
                if (match != null)
                    holder.result_qualitative.setText(match.getGrade());
                // quantitative result
                String quantitative = "";
                quantitative += currentScale.getResult();
                GeriatricScaleNonDB testNonDB = Scales.getScaleByName(currentScale.getScaleName());
                if (testNonDB.getScoring() != null) {
                    if (!testNonDB.getScoring().isDifferentMenWomen()) {
                        quantitative += " (" + testNonDB.getScoring().getMinScore();
                        quantitative += "-" + testNonDB.getScoring().getMaxScore() + ")";
                    } else {
                        if (patientGender == Constants.MALE) {
                            quantitative += " (" + testNonDB.getScoring().getMinMen();
                            quantitative += "-" + testNonDB.getScoring().getMaxMen() + ")";
                        }
                    }
                } else {
                    quantitative = "";
                }
                holder.result_quantitative.setText(quantitative);
            }
            // still incomplete
            else {
                holder.result_qualitative.setText(testCompletionSelectedIncomplete);
//                parentView.removeView(holder.result_quantitative);
//                parentView.removeView(holder.addNotesButton);
//                parentView.removeView(holder.notes);
            }

        } else {
//            parentView.removeView(holder.result_qualitative);
//            parentView.removeView(holder.result_quantitative);
//            parentView.removeView(holder.addNotesButton);
//            parentView.removeView(holder.notes);
        }


        if (currentScale != null && currentScale.hasNotes()) {
            parentView.removeView(holder.addNotesButton);
            holder.notes.setText(currentScale.getNotes());
        } else {
            parentView.removeView(holder.notes);
        }


        holder.addNotesButton.setOnClickListener(new ScaleHandlerNotes(context, finalCurrentTest, holder, parentView));
        holder.notes.setOnClickListener(new ScaleHandlerNotes(context, finalCurrentTest, holder, parentView));

        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalCurrentTest.getScaleName().equals(Constants.test_name_mini_nutritional_assessment_global)) {
                    // check if triagem is already answered

                    GeriatricScaleFirebase triagem = FirebaseDatabaseHelper.getScaleFromSession(session,
                            Constants.test_name_mini_nutritional_assessment_triagem);

                }

                String selectedTestName = finalCurrentTest.getScaleName();

                // Create new fragment and transaction
                Fragment newFragment = new ScaleFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ScaleFragment.testObject, Scales.getScaleByName(selectedTestName));
                bundle.putSerializable(ScaleFragment.SCALE, finalCurrentTest);
                bundle.putSerializable(ScaleFragment.CGA_AREA, area);
                bundle.putSerializable(ScaleFragment.patient, FirebaseDatabaseHelper.getPatientFromSession(session));
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.current_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_session_scale).commit();
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
                currentScale.setNotes(charSequence.toString());
                currentScale.save();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        */

    }


    @Override
    public int getItemCount() {
        Log.d("Scales",Scales.scales.size()+"");
        // get the number of tests that exist for this area
        testsForArea = Scales.getScalesForArea(area);
        return testsForArea.size();
    }

}
