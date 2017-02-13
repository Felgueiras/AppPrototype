package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class ReviewYesNoQuestionHandler implements RadioGroup.OnCheckedChangeListener {
    /**
     * Question
     */
    private final Question question;


    public ReviewYesNoQuestionHandler(Question question) {
        this.question = question;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * Store choice in DB
         */
        if (checkedId == R.id.yesChoice) {
            question.setSelectedYesNoChoice("yes");
        } else if (checkedId == R.id.noChoice) {
            question.setSelectedYesNoChoice("no");
        }
        question.setAnswered(true);
        question.save();

    }
}
