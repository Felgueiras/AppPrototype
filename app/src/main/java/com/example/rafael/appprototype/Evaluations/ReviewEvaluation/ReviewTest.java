package com.example.rafael.appprototype.Evaluations.ReviewEvaluation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewTest extends RecyclerView.Adapter<ReviewTest.TestCardHolder> {

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
    public ReviewTest(Context context, Session session) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
    }


    /**
     * TestCardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        public TextView name, type, testCompletion;
        /**
         * Test card view.
         */
        public View testCard;
        public boolean alreadyOpened = false;

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
        // for when the test is selected
        // TODO review when card is selected
        testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyOpened = false;
                TextView textView = (TextView) testCard.findViewById(R.id.testName);
                String selectedTestName = (String) textView.getText();
                // check if already selected
                for (int i = 0; i < session.getTestsFromSession().size(); i++) {
                    GeriatricTest currentTestDB = session.getTestsFromSession().get(i);
                    if (currentTestDB.getTestName().equals(testName)) {
                        alreadyOpened = true;
                    }
                }
                ((MainActivity) context).displaySessionTest(StaticTestDefinition.getTestByName(selectedTestName),
                        null);
            }
        });
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
        // access string constants
        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);
        String testCompletionResult = context.getResources().getString(R.string.test_result);


        // get current test
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        GeriatricTest currentTest = testsFromSession.get(position);
        testName = (String) holder.name.getText();

        // update the view
        holder.name.setText(currentTest.getTestName());
        holder.type.setText(currentTest.getType());
        holder.testCompletion.setText(testCompletionNotSelected);
        /**
         Alpha value when test is unselected
         **/
        float unselected = 0.5f;
        holder.testCard.setAlpha(unselected);
        holder.alreadyOpened = true;
        /**
         Alpha value when test is selected
         **/
        float selected = 1f;
        holder.testCard.setAlpha(selected);
        holder.testCompletion.setText(testCompletionSelectedIncomplete);
        if (currentTest.isCompleted()) {
            // go fetch the result
            ArrayList<Question> questionsFromTest = currentTest.getQuestionsFromTest();
            int testResult = getTestResult(questionsFromTest);
            // save the result
            currentTest.setResult(testResult);
            currentTest.save();
            holder.testCompletion.setText(testCompletionResult + "-" + testResult);
        }


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
    public int getItemCount() {
        return session.getTestsFromSession().size();
    }

}
