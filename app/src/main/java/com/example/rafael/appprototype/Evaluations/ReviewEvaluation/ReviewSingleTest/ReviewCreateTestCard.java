package com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.R;

import java.util.List;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewCreateTestCard extends RecyclerView.Adapter<ReviewCreateTestCard.TestCardHolder> {

    /**
     * Session for the Tests.
     */
    private final Session session;
    /**
     * Patient for this Session
     */
    private final Patient patient;
    /**
     * Context.
     */
    private Context context;
    /**
     * Name of Test being displayed.
     */
    private String testName;


    /**
     * Default constructor for the RV adapter.
     *
     * @param context
     * @param session
     */
    public ReviewCreateTestCard(Context context, Session session) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
    }


    /**
     * TestCardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        public TextView name, type, testCompletion, testGrading;
        public EditText notes;
        /**
         * Test card view.
         */
        public View testCard;

        /**
         * Create a TestCardHolder from a View
         *
         * @param view
         */
        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            //type = (TextView) view.findViewById(R.id.testType);
            testCompletion = (TextView) view.findViewById(R.id.testCompletion);
            //testGrading = (TextView) view.findViewById(R.id.testGrading);
            testCard = view;
            notes = (EditText) view.findViewById(R.id.testNotes);

        }
    }


    /**
     * Create a TestCardHolder from a View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the Test CardView
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
        return new TestCardHolder(testCard);
    }

    /**
     * Display the data at a certain position
     *
     * @param holder   data to be displayed
     * @param position position to "put" the data
     */
    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {

        String testCompletionResult = context.getResources().getString(R.string.test_result);
        // get current test
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        final GeriatricTest currentTest = testsFromSession.get(position);
        testName = currentTest.getTestName();
        double testResult = currentTest.generateTestResult();

        // get constants
        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);

        // access a given Test from the DB
        holder.name.setText(currentTest.getShortName());
        //holder.type.setText(currentTest.getType());

        // fill the View
        //holder.testCompletion.setText(testCompletionNotSelected);
        /**
         Alpha value when test is unselected
         **/
        float unselected = 0.5f;
        // Test was already opened
        if (currentTest.isAlreadyOpened()) {
            float selected = 1f;
            // already complete
            if (currentTest.isCompleted()) {
                // display Scoring to the user
                GradingNonDB match = StaticTestDefinition.getGradingForTest(
                        currentTest,
                        patient.getGender());
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

        // for when the test is selected
        holder.testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment and transaction
                Fragment newFragment = new ReviewSingleTestFragment();
                // add arguments
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReviewSingleTestFragment.testDBobject, currentTest);
                newFragment.setArguments(bundle);
                // setup the transaction
                FragmentTransaction transaction = ((PrivateArea) context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_fragment, newFragment);
                transaction.addToBackStack(Constants.tag_review_test).commit();
            }
        });


    }


    @Override
    public int getItemCount() {
        return session.getTestsFromSession().size();
    }

}
