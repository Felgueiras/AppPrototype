package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest.ReviewSingleTestFragment;
import com.example.rafael.appprototype.Main.MainActivity;
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
            type = (TextView) view.findViewById(R.id.testType);
            testCompletion = (TextView) view.findViewById(R.id.testCompletion);
            testGrading = (TextView) view.findViewById(R.id.testGrading);
            testCard = view;
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
        int testResult = currentTest.getTestResult();

        // update the view
        holder.name.setText(currentTest.getShortName());
        holder.type.setText(currentTest.getType());
        holder.testCompletion.setText(testCompletionResult + "-" + testResult);

        // display Scoring to the user
        GradingNonDB match = StaticTestDefinition.getGradingForTest(testName, patient.getGender(), testResult);
        holder.testGrading.setText(match.getGrade());


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
                FragmentTransaction transaction = ((MainActivity) context).getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(Constants.tag_review_session).commit();
            }
        });


    }


    @Override
    public int getItemCount() {
        return session.getTestsFromSession().size();
    }

}
