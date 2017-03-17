package com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class RightWrongQuestionHandler implements View.OnClickListener {
    /**
     * Question
     */
    private final Question question;
    private final QuestionsListAdapter adapter;
    private final GeriatricScaleNonDB testNonDB;
    private final ImageButton right;
    private final ImageButton wrong;
    private int index = 0;

    public RightWrongQuestionHandler(Question question, QuestionsListAdapter adapter, GeriatricScaleNonDB testNonDB,
                                     int index, ImageButton right, ImageButton wrong) {
        this.question = question;
        this.adapter = adapter;
        this.testNonDB = testNonDB;
        this.index = index;
        this.right = right;
        this.wrong = wrong;
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
        question.save();
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(index);
    }
}
