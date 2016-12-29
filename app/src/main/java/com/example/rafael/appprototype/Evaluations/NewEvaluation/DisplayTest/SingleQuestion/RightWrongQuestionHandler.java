package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion;

import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest.ViewQuestionsListAdapter;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class RightWrongQuestionHandler implements RadioGroup.OnCheckedChangeListener {
    /**
     * Question
     */
    private final Question question;
    private final ViewQuestionsListAdapter adapter;
    private final int position;


    public RightWrongQuestionHandler(Question question, ViewQuestionsListAdapter adapter, int position) {
        this.question = question;
        this.adapter = adapter;
        this.position = position;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * Store choice in DB
         */
        if (checkedId == R.id.rightChoice) {
            question.setSelectedYesNoChoice("right");
        } else if (checkedId == R.id.wrongChoice) {
            question.setSelectedYesNoChoice("wrong");
        }
        question.setAnswered(true);
        question.save();
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(position);
    }
}
