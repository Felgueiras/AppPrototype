package com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;

/**
 * Create the layout of the Questions
 */
public class MultipleChoiceHandler implements RadioGroup.OnCheckedChangeListener {

    private static LayoutInflater inflater = null;
    private final Question question;
    private final QuestionsListAdapter adapter;
    private final ListView listView;
    private int position;


    public MultipleChoiceHandler(Question question, QuestionsListAdapter adapter, int position, ListView listView) {
        this.question = question;
        this.adapter = adapter;
        this.position = position;
        this.listView = listView;
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
                    String selected = question.getChoicesForQuestion().get(i).getName();
                    System.out.println("Selected " + selected);
                    question.setSelectedChoice(question.getChoicesForQuestion().get(i).getName());
                }
            }
        }
        question.setAnswered(true);
        question.save();
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(position);

        listView.smoothScrollToPosition(position+1);

    }


    public void selectedFromAlertDialog(int selectedAnswer) {

        // save the text of the option
        String selected = question.getChoicesForQuestion().get(selectedAnswer).getName();
        System.out.println("Selected " + selected);
        question.setSelectedChoice(question.getChoicesForQuestion().get(selectedAnswer).getName());

        question.setAnswered(true);
        question.save();
        /**
         * Signal that que Question was answered
         */
        adapter.questionAnswered(position);
    }
}