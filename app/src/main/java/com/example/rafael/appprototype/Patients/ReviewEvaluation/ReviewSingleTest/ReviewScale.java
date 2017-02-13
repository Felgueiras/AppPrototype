package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

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
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Main.PrivateArea;
import com.example.rafael.appprototype.R;

import java.util.List;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewScale extends RecyclerView.Adapter<ReviewScale.TestCardHolder> {

    /**
     * Session for the Tests.
     */
    private final Session session;
    /**
     * Patient for this Session
     */
    private final Patient patient;
    private final String area;
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
     * @param area
     */
    public ReviewScale(Context context, Session session, String area) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
        this.area = area;
    }


    /**
     * CGACardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        private final TextView result_qualitative;
        public TextView name, type, testGrading;
        public EditText notes;
        /**
         * Test card view.
         */
        public View testCard;
        public TextView result_quantitative;

        /**
         * Create a CGACardHolder from a View
         *
         * @param view
         */
        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            //type = (TextView) view.findViewById(R.id.testType);
            result_qualitative = (TextView) view.findViewById(R.id.result_qualitative);
            result_quantitative = (TextView) view.findViewById(R.id.result_quantitative);
            //testGrading = (TextView) view.findViewById(R.id.testGrading);
            testCard = view;
            notes = (EditText) view.findViewById(R.id.testNotes);

        }
    }


    /**
     * Create a CGACardHolder from a View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the Test CardView
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.scale_card, parent, false);
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


        // access a given Test from the DB
        holder.name.setText(currentTest.getShortName());


        // display Scoring to the user
        GradingNonDB match = Scales.getGradingForTest(
                currentTest,
                patient.getGender());
        holder.result_qualitative.setText(match.getGrade());
        // quantitative result
        String quantitative = "";
        quantitative += currentTest.getResult();
        GeriatricTestNonDB testNonDB = Scales.getTestByName(currentTest.getTestName());
        if (!testNonDB.getScoring().isDifferentMenWomen()) {
            quantitative += " (" + testNonDB.getScoring().getMinScore();
            quantitative += "-" + testNonDB.getScoring().getMaxScore() + ")";
        } else {
            if (patient.getGender() == Constants.MALE) {
                quantitative += " (" + testNonDB.getScoring().getMinMen();
                quantitative += "-" + testNonDB.getScoring().getMaxMen() + ")";
            }
        }
        holder.result_quantitative.setText(quantitative);

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

        List<GeriatricTest> allScales = session.getTestsFromSession();
        // get scales for this area
        return Scales.getTestsForArea(allScales, area).size();
    }

}
