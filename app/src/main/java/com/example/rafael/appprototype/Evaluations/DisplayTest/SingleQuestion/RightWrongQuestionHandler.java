package com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion;

import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 06-10-2016.
 */
public class RightWrongQuestionHandler implements RadioGroup.OnCheckedChangeListener {
    /**
     * Question
     */
    private final Question question;
    private final QuestionsListAdapter adapter;
    private final int category;
    private final int questionInCategory;
    private final GeriatricTestNonDB testNonDB;
    private int index = 0;


    public RightWrongQuestionHandler(Question question, QuestionsListAdapter adapter, int category, int questionInCategory,
                                     GeriatricTestNonDB testNonDB) {
        this.question = question;
        this.adapter = adapter;
        this.category = category;
        this.questionInCategory = questionInCategory;
        this.testNonDB = testNonDB;
        // calculate the global index for the question
        index = QuestionCategory.getQuestionIndex(category, questionInCategory, testNonDB);
    }

    public RightWrongQuestionHandler(Question question, QuestionsListAdapter adapter, GeriatricTestNonDB testNonDB,
                                     int index) {
        this.question = question;
        this.adapter = adapter;
        this.testNonDB = testNonDB;
        this.index = index;
        category = 0;
        questionInCategory = 0;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * Store choice in DB
         */
        if (checkedId == R.id.rightChoice) {
            question.setSelectedRightWrong("right");
        } else if (checkedId == R.id.wrongChoice) {
            question.setSelectedRightWrong("wrong");
        }
        question.setAnswered(true);
        question.save();
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(index);
    }
}
