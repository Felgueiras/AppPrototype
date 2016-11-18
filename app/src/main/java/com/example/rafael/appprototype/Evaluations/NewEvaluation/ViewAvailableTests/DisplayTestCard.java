package com.example.rafael.appprototype.Evaluations.NewEvaluation.ViewAvailableTests;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class DisplayTestCard extends RecyclerView.Adapter<DisplayTestCard.TestCardHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    /**
     * Patient for this Session
     */
    private final Patient patient;
    private Context context;
    /**
     * Data to be displayed.
     */
    private List<GeriatricTestNonDB> testsList;
    private String testName;


    /**
     * Create a View
     */
    public class TestCardHolder extends RecyclerView.ViewHolder implements Serializable {
        public TextView name, type, testCompletion;
        public View view;

        public TestCardHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            type = (TextView) view.findViewById(R.id.testType);
            testCompletion = (TextView) view.findViewById(R.id.testCompletion);
            this.view = view;
        }
    }

    /**
     * Constructor of the ShowSingleEvaluation
     *
     * @param context               current Context
     * @param testsList             List of the Tests for this Session
     * @param resuming              true if we are resuming a Session
     * @param patientForThisSession
     */
    public DisplayTestCard(Context context, ArrayList<GeriatricTestNonDB> testsList, Session session, boolean resuming, Patient patientForThisSession) {
        this.context = context;
        this.testsList = testsList;
        this.session = session;
        this.patient = patientForThisSession;
    }

    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
        return new TestCardHolder(testCard);
    }

    /**
     * Get the result for the Test
     *
     * @param questionsFromTest Questions for a Test
     * @return final result for the Test
     */
    private int getTestResult(ArrayList<Question> questionsFromTest) {
        int res = 0;

        for (Question question : questionsFromTest) {
            /**
             * Yes/no Question
             */
            if (question.isYesOrNo()) {
                String selectedYesNoChoice = question.getSelectedYesNoChoice();
                if (selectedYesNoChoice.equals("yes")) {
                    res += question.getYesValue();

                } else {
                    res += question.getNoValue();
                }
            }
            /**
             * Multiple Choice Question
             */
            else {
                // get the selected Choice
                Choice selectedChoice = question.getChoicesForQuestion().get(question.getSelectedChoice());
                res += selectedChoice.getScore();
            }
        }
        return res;
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
        holder.name.setText(currentTest.getTestName());
        holder.type.setText(currentTest.getType());

        // fill the View
        holder.testCompletion.setText(testCompletionNotSelected);
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
                // go fetch the result
                ArrayList<Question> questionsFromTest = currentTest.getQuestionsFromTest();
                int testResult = getTestResult(questionsFromTest);
                // save the result
                currentTest.setResult(testResult);
                currentTest.save();
                holder.testCompletion.setText(testCompletionResult + "-" + testResult);
            }
            // still incomplete
            else {
                holder.testCompletion.setText(testCompletionSelectedIncomplete);
            }

        }

        /**
         * For when the Test is selected.
         */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "Going to open");
                String selectedTestName = currentTest.getTestName();
                ((MainActivity) context).displaySessionTest(StaticTestDefinition.getTestByName(selectedTestName),
                        currentTest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

}
