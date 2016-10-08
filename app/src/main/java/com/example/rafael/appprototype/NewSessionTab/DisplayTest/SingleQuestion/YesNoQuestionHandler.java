package com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleQuestion;

import android.util.Log;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest.ViewQuestionsListAdapter;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class YesNoQuestionHandler implements RadioGroup.OnCheckedChangeListener {
    /**
     * Question
     */
    private final Question question;
    private final ViewQuestionsListAdapter adapter;
    private final int position;

    /**
     * Signal if the Question is answered or not
     */
    boolean answered = false;

    public YesNoQuestionHandler(Question question, ViewQuestionsListAdapter adapter, int position) {
        this.question = question;
        answered = false;
        this.adapter = adapter;
        this.position = position;
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * Store choice in DB
         */
        if (checkedId == R.id.yesChoice) {
            question.setSelectedYesNoChoice("yes");
            question.save();
        } else if (checkedId == R.id.noChoice) {
            question.setSelectedYesNoChoice("no");
            question.save();
        }
        String selectedYesNoChoice = question.getSelectedYesNoChoice();
        Log.d("NewSession", selectedYesNoChoice);
        /**
         * Set answered to true
         */
        answered = true;
        adapter.questionAnswered(position);
    }
}
