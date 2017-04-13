package com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.SingleQuestion;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatric_helper.Firebase.FirebaseHelper;
import com.felgueiras.apps.geriatric_helper.Firebase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class RightWrongQuestionHandler implements View.OnClickListener {
    /**
     * Question
     */
    private final QuestionFirebase question;
    private final QuestionsListAdapter adapter;
    private final GeriatricScaleNonDB testNonDB;
    private final ImageButton right;
    private final ImageButton wrong;
    private final TextView categoryText;
    private final QuestionCategory currentCategory;
    private int index = 0;

    public RightWrongQuestionHandler(QuestionFirebase question, QuestionsListAdapter adapter, GeriatricScaleNonDB testNonDB,
                                     int index, ImageButton right, ImageButton wrong, TextView categoryTextView, QuestionCategory currentCategory) {
        this.question = question;
        this.adapter = adapter;
        this.testNonDB = testNonDB;
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
            right.setImageResource(R.drawable.ic_check_box_black_24dp);
            wrong.setImageResource(R.drawable.ic_close_black_24dp);
        } else if (v.getId() == R.id.wrongChoice) {
            question.setSelectedRightWrong("wrong");
            right.setImageResource(R.drawable.ic_check_black_24dp);
            wrong.setImageResource(R.drawable.close_box);
        }
        question.setAnswered(true);

        FirebaseHelper.updateQuestion(question);
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(index);

        if (categoryText != null) {
            // check if all questions from category were answered
            boolean allAnswered = true;
            for(QuestionNonDB question : currentCategory.getQuestions()){

            }
        }
    }
}
