package com.example.rafael.appprototype.NewSessionTab.ViewAvailableTests;

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
import com.example.rafael.appprototype.DataTypes.StaticTestDefinition;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Create the Card for each of the Tests available
 */
public class GenerateTestsCardAdapter extends RecyclerView.Adapter<GenerateTestsCardAdapter.MyViewHolder> {

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
    boolean alreadyOpened = false;


    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.testName);
            type = (TextView) view.findViewById(R.id.testType);
        }
    }

    /**
     * Constructor of the ViewSinglePatientCardAdapter
     *
     * @param context   current Context
     * @param testsList List of the Tests for this Session
     * @param resuming  true if we are resuming a Session
     */
    public GenerateTestsCardAdapter(Context context, ArrayList<GeriatricTestNonDB> testsList, Session session, boolean resuming) {
        this.context = context;
        this.testsList = testsList;
        this.session = session;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card, parent, false);

        String testCompletionNotSelected = context.getResources().getString(R.string.test_not_selected);
        String testCompletionSelectedIncomplete = context.getResources().getString(R.string.test_incomplete);
        String testCompletionResult = context.getResources().getString(R.string.test_result);
        /**
         * Check if we have info about this Test for this Session inside DB
         */
        List<GeriatricTest> testsFromSession = session.getTestsFromSession();
        TextView testCompletion = (TextView) testCard.findViewById(R.id.testCompletion);
        testCompletion.setText(testCompletionNotSelected);
        testCard.setAlpha(unselected);
        for (int i = 0; i < testsFromSession.size(); i++) {
            GeriatricTest currentTestDB = testsFromSession.get(i);
            if (currentTestDB.getTestName().equals(testName)) {
                alreadyOpened = true;
                testCard.setAlpha(selected);
                testCompletion.setText(testCompletionSelectedIncomplete);
                if (currentTestDB.isCompleted()) {
                    Log.d("NewSession", "Completed");
                    // go fetch the result
                    int result = 0;
                    ArrayList<Question> questionsFromTest = currentTestDB.getQuestionsFromTest();
                    Log.d("NewSession", "Size is " + questionsFromTest.size());
                    int testResult = getResultAllquestions(questionsFromTest);
                    testCompletion.setText(testCompletionResult+"-"+testResult);
                }
            }
        }


        testCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NewSession", "Selected a test!");
                TextView textView = (TextView) testCard.findViewById(R.id.testName);
                String selectedTestName = (String) textView.getText();

                ((MainActivity) context).displaySessionTest(StaticTestDefinition.getTestByName(selectedTestName),
                        alreadyOpened,
                        session);
            }
        });
        return new MyViewHolder(testCard);
    }

    private int getResultAllquestions(ArrayList<Question> questionsFromTest) {
        int res = 0;

        for (Question question : questionsFromTest) {
            if (question.isYesOrNo()) {
                String selectedYesNoChoice = question.getSelectedYesNoChoice();
                if (selectedYesNoChoice.equals("yes")) {
                    Log.d("NewSession", "Selected yes");
                    res += question.getYesValue();

                } else {
                    Log.d("NewSession", "Selected no");
                    res += question.getNoValue();
                }
            }
        }
        return res;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        GeriatricTestNonDB test = testsList.get(position);
        holder.name.setText(test.getTestName());
        holder.type.setText(test.getType());
        testName = (String) holder.name.getText();
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

}
