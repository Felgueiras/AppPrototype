package com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.SingleQuestion;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.QuestionFirebase;

/**
 * Create the layout of the Questions
 */
public class MultipleChoiceHandler implements RadioGroup.OnCheckedChangeListener {

    private static LayoutInflater inflater = null;
    private final QuestionFirebase question;
    private final QuestionsListAdapter adapter;
    private final ListView listView;
    private final QuestionNonDB questionNonDB;
    private int position;


    public MultipleChoiceHandler(QuestionFirebase question, QuestionsListAdapter adapter, int position, ListView listView, QuestionNonDB currentQuestionNonDB) {
        this.question = question;
        this.adapter = adapter;
        this.position = position;
        this.listView = listView;
        this.questionNonDB = currentQuestionNonDB;
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        // check which button was selected
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                if (((RadioButton) o).isChecked()) {
                    // save the text of the option
                    String selected = FirebaseDatabaseHelper.getChoicesForQuestion(questionNonDB).get(i).getName();
                    System.out.println("Selected " + selected);
                    question.setSelectedChoice(FirebaseDatabaseHelper.getChoicesForQuestion(questionNonDB).get(i).getName());
                }
            }
        }
        question.setAnswered(true);

        FirebaseDatabaseHelper.updateQuestion(question);
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(position);

        listView.smoothScrollToPosition(position+1);

    }


    public void selectedFromAlertDialog(int selectedAnswer) {

        // save the text of the option
//        String selected = FirebaseHelper.getChoicesForQuestion(question).get(selectedAnswer).getName();
        question.setSelectedChoice(questionNonDB.getChoices().get(selectedAnswer).getName());

        question.setAnswered(true);
        FirebaseDatabaseHelper.updateQuestion(question);
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(position);
    }
}