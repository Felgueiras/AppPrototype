package com.example.rafael.appprototype.Evaluations.ReviewEvaluation.ReviewSingleTest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Create the layout of the Questions
 */
public class ReviewQuestionsListAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<Question> questions;
    private static LayoutInflater inflater = null;
    /**
     * Current Context
     */
    private final Context context;
    /**
     * Test
     */
    private final GeriatricTest test;

    int numquestions;
    private View questionView;


    /**
     * Display all Questions for a GeriatricTest
     *
     * @param context current Context
     * @param test    GeriatricTest that is being filled up
     */
    public ReviewQuestionsListAdapter(Context context, GeriatricTest test) {
        this.context = context;
        this.questions = test.getQuestionsFromTest();
        this.test = test;
        numquestions = questions.size();
        // TODO display all questions
        numquestions = 5;
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


    public class Holder {
        TextView question;
        ListView choicesList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        questionView = null;
        Question currentQuestion = questions.get(position);


        // check if question is multiple choice or yes/no
        if (currentQuestion.isYesOrNo()) {
            questionView = yesNoQuestion(currentQuestion, position);
        } else {
            //questionView = multipleChoiceAlreadyOpened(currentQuestion, position);
        }

        return questionView;
    }


    /**
     * Multiple Choice Qustion, already opened before
     *
     * @param currentQuestion
     * @param position
     * @return
     */
    private View multipleChoiceAlreadyOpened(Question currentQuestion, int position) {
        View questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);
        Question questionInDB = test.getQuestionsFromTest().get(position);

        if (questionInDB.isAnswered()) {
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestion.getDescription());
            holder.choicesList = (ListView) questionView.findViewById(R.id.questionChoices);

            /**
             ReviewMultipleChoiceHandler multipleChoiceHandler = new ReviewMultipleChoiceHandler(context, currentQuestion.getChoices(), questionInDB, this, position);
             holder.choicesList.setAdapter(multipleChoiceHandler);
             holder.choicesList.setOnItemClickListener(multipleChoiceHandler);
             **/
            // highlight the already selected item from ListView
        }

        // Test already opened, but question not answered
        else {
            Log.d("question", "not answered");
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestion.getDescription());
            holder.choicesList = (ListView) questionView.findViewById(R.id.questionChoices);

            /**
             ReviewMultipleChoiceHandler multipleChoiceHandler = new ReviewMultipleChoiceHandler(context, currentQuestion.getChoices(), questionInDB, this, position);
             holder.choicesList.setAdapter(multipleChoiceHandler);
             holder.choicesList.setOnItemClickListener(multipleChoiceHandler);
             **/

        }
        return questionView;
    }

    private View yesNoQuestion(Question currentQuestion, int position) {
        View questionView = inflater.inflate(R.layout.content_question_yes_no, null);
        Question question = test.getQuestionsFromTest().get(position);

        Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((position + 1) + " - " + currentQuestion.getDescription());

        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new ReviewYesNoQuestionHandler(question));
        if (question.getSelectedYesNoChoice().equals("yes")) {
            radioGroup.check(R.id.yesChoice);
        } else {
            radioGroup.check(R.id.noChoice);
        }


        return questionView;
    }


}