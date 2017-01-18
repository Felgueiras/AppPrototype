package com.example.rafael.appprototype.Evaluations.NewEvaluation.ViewAvailableTests;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class DisplayTestCard extends RecyclerView.Adapter<DisplayTestCard.TestCardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    private final int patientGender;

    private Activity context;


    /**
     * Create a View
     */
    public class TestCardHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView name, type, testCompletion;
        public View view;
        public EditText notes;

        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            //type = (TextView) view.findViewById(R.id.testType);
            testCompletion = (TextView) view.findViewById(R.id.testCompletion);
            notes = (EditText) view.findViewById(R.id.testNotes);
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
    public DisplayTestCard(Activity context, Session session, boolean resuming, int patientGender) {
        this.context = context;
        this.session = session;
        this.patientGender = patientGender;
    }

    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
        return new TestCardHolder(testCard);
    }


    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
        // get constants
        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);
        String testCompletionResult = context.getResources().getString(R.string.test_result);

        // access a given Test from the DB
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        final GeriatricTest currentTest = testsFromSession.get(position);
        holder.name.setText(currentTest.getShortName());
        //holder.type.setText(currentTest.getType());

        // fill the View
        //holder.testCompletion.setText(testCompletionNotSelected);
        /**
         Alpha value when test is unselected
         **/
        float unselected = 0.5f;
        holder.view.setAlpha(unselected);
        // Test was already opened
        if (currentTest.isAlreadyOpened()) {
            float selected = 1f;
            holder.view.setAlpha(selected);
            // already complete
            if (currentTest.isCompleted()) {
                // display Scoring to the user
                GradingNonDB match = StaticTestDefinition.getGradingForTest(
                        currentTest,
                        patientGender);
                holder.testCompletion.setText(match.getGrade());
            }
            // still incomplete
            else {
                holder.testCompletion.setText(testCompletionSelectedIncomplete);
            }

        }

        if (currentTest.hasNotes()) {
            holder.notes.setText(currentTest.getNotes());
        }

        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "Going to open");
                String selectedTestName = currentTest.getTestName();


                // Create new fragment and transaction
                Fragment newFragment = new DisplaySingleTestFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(DisplaySingleTestFragment.testObject, StaticTestDefinition.getTestByName(selectedTestName));
                bundle.putSerializable(DisplaySingleTestFragment.testDBobject, currentTest);
                Patient patient = currentTest.getSession().getPatient();
                if (patient != null)
                    bundle.putSerializable(DisplaySingleTestFragment.patient, patient);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_display_session_test).commit();


            }
        });

        /**
         * Add a listener for when a note is added.
         */
        holder.notes.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    public int getItemCount() {
        return session.getTestsFromSession().size();
    }

}
