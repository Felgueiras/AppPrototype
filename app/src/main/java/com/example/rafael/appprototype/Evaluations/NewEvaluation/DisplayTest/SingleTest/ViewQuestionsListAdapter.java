package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.MultipleChoiceHandler;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.YesNoQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Create the layout of the Questions
 */
public class ViewQuestionsListAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<QuestionNonDB> questions;
    private static LayoutInflater inflater = null;
    /**
     * Current Context
     */
    private final Context context;
    /**
     * Test
     */
    private final GeriatricTest test;
    /**
     * Yes if test already opened, no otherwise
     */
    private final boolean testAlreadyOpened;
    /**
     * HasSet that will hold the numbers of the Questions that were already answered.
     */
    private HashSet<Integer> positionsFilled = new HashSet<>();

    boolean allQuestionsAnswered = false;

    int numquestions;
    private View questionView;


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
        // TODO display all questions
        numquestions = 5;
        testAlreadyOpened = test.isAlreadyOpened();
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

    /**
     * Signal that a given Question was already answered
     *
     * @param position position of the answered Question
     */
    public void questionAnswered(int position) {
        positionsFilled.add(position);
        if (positionsFilled.size() == numquestions) {
            allQuestionsAnswered = true;
            //Snackbar.make(questionView, R.string.all_questions_answered, Snackbar.LENGTH_SHORT).show();
            // write that to DB
            test.setCompleted(true);
            test.save();
        }
    }

    public class Holder {
        TextView question;
        ListView choicesList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        questionView = null;
        QuestionNonDB currentQuestionNonDB = questions.get(position);
        if (!testAlreadyOpened) {
            // yes/no question
            if (currentQuestionNonDB.isYesOrNo()) {
                questionView = yesNoNotOpened(currentQuestionNonDB, position);
            }
            // multiple Choice
            else {
                questionView = multipleChoiceNotOpened(currentQuestionNonDB, position);
            }
        }
        // Test already opened
        else {
            // check if question is multiple choice or yes/no
            if (currentQuestionNonDB.isYesOrNo()) {
                questionView = yesNoAlreadyOpened(currentQuestionNonDB, position);
            } else {
                questionView = multipleChoiceAlreadyOpened(currentQuestionNonDB, position);
            }
        }
        return questionView;
    }

    /**
     * Multiple choice question that's being opened for the first time.
     *
     * @param currentQuestionNonDB
     * @param position
     * @return
     */
    private View multipleChoiceNotOpened(QuestionNonDB currentQuestionNonDB, int position) {
        View questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);

        // create question and add to DB
        Question question = new Question();
        String questionID = test.getGuid() + "-" + currentQuestionNonDB.getDescription();
        question.setDescription(currentQuestionNonDB.getDescription());
        question.setGuid(questionID);
        question.setTest(test);
        question.setYesOrNo(false);
        question.save();

        // create Choices and add to DB
        ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

        for (ChoiceNonDB currentChoice : choicesNonDB) {
            Choice choice = new Choice();
            String choiceID = test.getGuid() + "-" + currentQuestionNonDB.getDescription() + "-" + currentChoice.getDescription();
            // check if already in DB
            if (Choice.getChoiceByID(choiceID) == null) {
                choice.setGuid(choiceID);
                choice.setQuestion(question);
                if (currentChoice.getName() != null)
                    choice.setName(currentChoice.getName());
                choice.setDescription(currentChoice.getDescription());
                choice.setScore(currentChoice.getScore());
                choice.save();
            }

            // create RadioButton for that choice
            RadioButton newRadioButton = new RadioButton(context);
            if (Objects.equals(choice.getName(), "") || choice.getName() == null) {
                newRadioButton.setText(choice.getDescription());
            } else {
                newRadioButton.setText(choice.getName());

            }

            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(newRadioButton, 0, layoutParams);
        }
        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, position));


        Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((position + 1) + " - " + question.getDescription());

        // Setup the adapter
        if (question.getGuid() == null) {
            Log.e("Error", "Must have ID!");
        }
        return questionView;
    }


    /**
     * Multiple Choice Qustion, already opened before
     *
     * @param currentQuestionNonDB
     * @param position
     * @return
     */
    private View multipleChoiceAlreadyOpened(QuestionNonDB currentQuestionNonDB, int position) {
        View questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);
        // get Question from DB
        Question question = test.getQuestionsFromTest().get(position);

        // create Radio Group from the info in DB
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

        for (Choice choice : question.getChoicesForQuestion()) {

            // create RadioButton for that choice
            RadioButton newRadioButton = new RadioButton(context);
            if (Objects.equals(choice.getName(), "") || choice.getName() == null) {
                newRadioButton.setText(choice.getDescription());
            } else {
                newRadioButton.setText(choice.getName());

            }

            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(newRadioButton, 0, layoutParams);
        }
        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, position));

        Log.d("Multiple", "Already answered - " + question.isAnswered());
        if (question.isAnswered()) {
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
            // set the selected option
            int selectedChoice = question.getSelectedChoice();
            RadioButton selectedButton = (RadioButton) radioGroup.getChildAt(selectedChoice);
            selectedButton.setChecked(true);
        }

        // Test already opened, but question not answered
        else {
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
        }
        return questionView;
    }

    private View yesNoAlreadyOpened(QuestionNonDB currentQuestionNonDB, int position) {
        View questionView = inflater.inflate(R.layout.content_question_yes_no, null);
        Question questionInDB = test.getQuestionsFromTest().get(position);

        if (questionInDB.isAnswered()) {
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());

            RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new YesNoQuestionHandler(questionInDB, this, position));
            if (questionInDB.getSelectedYesNoChoice().equals("yes")) {
                radioGroup.check(R.id.yesChoice);
            } else {
                radioGroup.check(R.id.noChoice);
            }
        }
        /**
         * Test already opened, but question not answered
         */
        else {
            RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new YesNoQuestionHandler(questionInDB, this, position));
        }
        return questionView;
    }


    /**
     * Yes/No question, Test not opened before
     *
     * @param currentQuestionNonDB
     * @param position
     */
    private View yesNoNotOpened(QuestionNonDB currentQuestionNonDB, int position) {
        View questionView = inflater.inflate(R.layout.content_question_yes_no, null);
        // create question and add to DB
        Question question = new Question();
        String dummyID = test.getGuid() + "-" + currentQuestionNonDB.getDescription();
        question.setGuid(dummyID);
        question.setDescription(currentQuestionNonDB.getDescription());
        question.setTest(test);
        question.setYesOrNo(true);
        question.setYesValue(currentQuestionNonDB.getYesScore());
        question.setNoValue(currentQuestionNonDB.getNoScore());
        question.save();

        /**
         * Set View
         */
        Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
        // detect when choice changed
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new YesNoQuestionHandler(question, this, position));
        return questionView;
    }


}