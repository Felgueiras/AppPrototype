package com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.SingleQuestion;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class RightWrongQuestionHandler implements View.OnClickListener {
    /**
     * Question
     */
    private final QuestionFirebase question;
    private final QuestionsListAdapter adapter;
    private final ImageButton right;
    private final ImageButton wrong;
    private final TextView categoryText;
    private final QuestionCategory currentCategory;
    private int index = 0;

    /**
     * Handle the answer of a right/wrong question.
     *
     * @param question
     * @param adapter
     * @param testNonDB
     * @param index
     * @param right
     * @param wrong
     * @param categoryTextView
     * @param currentCategory
     */
    public RightWrongQuestionHandler(QuestionFirebase question, QuestionsListAdapter adapter, GeriatricScaleNonDB testNonDB,
                                     int index, ImageButton right, ImageButton wrong, TextView categoryTextView, QuestionCategory currentCategory) {
        this.question = question;
        this.adapter = adapter;
        this.index = index;
        this.right = right;
        this.wrong = wrong;
        this.categoryText = categoryTextView;
        this.currentCategory = currentCategory;
    }


    @Override
    public void onClick(View v) {
        // detect when choice changed
        if (v.getId() == R.id.rightChoice) {
            question.setSelectedRightWrong("right");
            right.setImageResource(R.drawable.ic_right_selected);
            wrong.setImageResource(R.drawable.ic_wrong_unselected);
        } else if (v.getId() == R.id.wrongChoice) {
            question.setSelectedRightWrong("wrong");
            right.setImageResource(R.drawable.ic_right_unselected);
            wrong.setImageResource(R.drawable.ic_wrong_selected);
        }
        question.setAnswered(true);

        FirebaseDatabaseHelper.updateQuestion(question);
        /*
          Signal that que Question was answered
         */
        adapter.questionAnswered(index);

        if (categoryText != null) {
            // check if all questions from category were answered
            boolean allAnswered = true;
            for (QuestionNonDB question : currentCategory.getQuestions()) {

            }
        }
    }
}
