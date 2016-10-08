package com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class CreateTestCard extends RecyclerView.Adapter<CreateTestCard.MyViewHolder> {

    /**
     * ID for this Session
     */
    private final Session session;
    private Context context;
    /**
     * Data to be displayed.
     */
    private List<GeriatricTestNonDB> testsList;
    /**
     * Alpha value when test is unselected
     */
    private float unselected = 0.5f;
    /**
     * Alpha value when test is selected
     */
    private float selected = 1f;
    private String testName;


    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type, testCompletion;
        public View testCard;
        public boolean alreadyOpened = false;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            type = (TextView) view.findViewById(R.id.testType);
            testCompletion = (TextView) view.findViewById(R.id.testCompletion);
            testCard = view;
        }
    }

    /**
     * Constructor of the SinglePatientCard
     *
     * @param context   current Context
     * @param testsList List of the Tests for this Session
     * @param resuming  true if we are resuming a Session
     */
    public CreateTestCard(Context context, ArrayList<GeriatricTestNonDB> testsList, Session session, boolean resuming) {
        this.context = context;
        this.testsList = testsList;
        this.session = session;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);
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
                        session,
                        alreadyOpened);
            }
        });
        return new MyViewHolder(testCard);
    }

    /**
     * Get the result for the Test
     *
     * @param questionsFromTest
     * @return
     */
    private int getResult(ArrayList<Question> questionsFromTest) {
        int res = 0;

        for (Question question : questionsFromTest) {
            if (question.isYesOrNo()) {
                String selectedYesNoChoice = question.getSelectedYesNoChoice();
                if (selectedYesNoChoice.equals("yes")) {
                    res += question.getYesValue();

                } else {
                    res += question.getNoValue();
                }
            }
        }
        return res;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);
        String testCompletionResult = context.getResources().getString(R.string.test_result);

        GeriatricTestNonDB test = testsList.get(position);
        holder.name.setText(test.getTestName());
        holder.type.setText(test.getType());
        testName = (String) holder.name.getText();
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();


        holder.testCompletion.setText(testCompletionNotSelected);
        holder.testCard.setAlpha(unselected);
        for (int i = 0; i < testsFromSession.size(); i++) {
            GeriatricTest currentTestDB = testsFromSession.get(i);
            if (currentTestDB.getTestName().equals(testName)) {
                holder.alreadyOpened = true;
                holder.testCard.setAlpha(selected);
                holder.testCompletion.setText(testCompletionSelectedIncomplete);
                if (currentTestDB.isCompleted()) {
                    // go fetch the result
                    ArrayList<Question> questionsFromTest = currentTestDB.getQuestionsFromTest();
                    int testResult = getResult(questionsFromTest);
                    holder.testCompletion.setText(testCompletionResult + "-" + testResult);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

}
