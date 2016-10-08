package com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleQuestion.SingleQuestionGridAdapter;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleQuestion.YesNoQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Create the layout of the Questions
 */
public class ViewQuestionsListAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<QuestionNonDB> questions;
    private static LayoutInflater inflater = null;
    private final Context context;
    private final GeriatricTest test;
    /**
     * Adapter to dislay the choices for a single Question
     */
    private SingleQuestionGridAdapter adapter;
    /**
     * HasSet that will hold the numbers of the Questions that were already answered.
     */
    private HashSet<Integer> positionsFilled = new HashSet<>();

    boolean allQuestionsAnswered = false;

    int numquestions;


    /**
     * Display all Questions for a GeriatricTest
     *
     * @param context   current Context
     * @param questions ArrayList of Questions
     * @param test      GeriatricTest that is being filled up
     */
    public ViewQuestionsListAdapter(Context context, ArrayList<QuestionNonDB> questions, GeriatricTest test) {
        this.context = context;
        this.questions = questions;
        this.test = test;
        numquestions = questions.size();
        numquestions = 3;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return numquestions;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void questionAnswered(int position) {
        positionsFilled.add(position);
        Log.d("NewSession", "Questions answered - " + positionsFilled.size());
        if (positionsFilled.size() == numquestions) {
            Log.d("NewSession", "All questions answered!");
            allQuestionsAnswered = true;
            Toast.makeText(context, "All questions were answered", Toast.LENGTH_SHORT).show();
            // write that to DB
            test.setCompleted(true);
            test.save();
        }
    }

    public class Holder {
        TextView question;
        GridView choices;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        QuestionNonDB currentQuestion = questions.get(position);
        View questionView;
        // check if question is multiple choice or yes/no
        if (currentQuestion.isYesOrNo()) {
            /**
             * Create Question and add to DB
             */
            Question question = new Question();
            String dummyID = test.getGuid() + "-" + currentQuestion.getDescription();
            question.setGuid(dummyID);
            question.setTest(test);
            question.setYesOrNo(true);
            question.setYesValue(currentQuestion.getYesScore());
            question.setNoValue(currentQuestion.getNoScore());
            question.save();
            //Question retrievedQuestion = Question.getQuestionByID(dummyID);
            //Log.d("NewSession", "Retrieved Question ID is " + retrievedQuestion.getGuid());

            /**
             * Create View to hold the Question and its Choices
             */
            questionView = inflater.inflate(R.layout.content_question_yes_no, null);
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestion.getDescription());
            /**
             * Detect when Choice changed
             */
            RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new YesNoQuestionHandler(question, this, position));
        } else {
            questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestion.getDescription());
            holder.choices = (GridView) questionView.findViewById(R.id.questionChoices);
            adapter = new SingleQuestionGridAdapter(context, currentQuestion.getChoices());
            holder.choices.setAdapter(adapter);
        }

        return questionView;
    }


}