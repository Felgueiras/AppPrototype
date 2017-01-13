package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.MultipleChoiceHandler;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.YesNoQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Create the layout of the Questions
 */
public class QuestionsListAdapter extends BaseAdapter {
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
    private final GeriatricTestNonDB testNonDB;
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
     * @param context current Context
     * @param test    GeriatricTest that is being filled up
     */
    public QuestionsListAdapter(Context context, GeriatricTestNonDB testNonDb, GeriatricTest test) {
        this.context = context;
        this.questions = testNonDb.getQuestions();
        this.test = test;
        this.testNonDB = testNonDb;


        numquestions = questions.size();
        numquestions = 2;
        testAlreadyOpened = test.isAlreadyOpened();
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (testNonDB.isSingleQuestion()) {
            // single question test
            numquestions = 1;
            return testNonDB.getScoring().getValuesBoth().size();
        }
        if (testNonDB.getQuestionsCategories().size() != 0) {
            // test with multiple categories
            return 1;
        }
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
            if(!test.isCompleted())
                Snackbar.make(questionView, R.string.all_questions_answered, Snackbar.LENGTH_SHORT).show();
            allQuestionsAnswered = true;

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
        if (!testAlreadyOpened) {
            // single question
            if (testNonDB.isSingleQuestion()) {
                questionView = singleChoiceNotOpened(position);
            } else if (testNonDB.isMultipleCategories()) {
                questionView = new QuestionMultipleCategories(inflater, testNonDB, context, test,
                        this).multipleCategoriesNotOpened();
            } else {
                QuestionNonDB currentQuestionNonDB = questions.get(position);
                // yes/no questions
                if (currentQuestionNonDB.isYesOrNo()) {
                    questionView = yesNoNotOpened(currentQuestionNonDB, position);
                }
                // multiple Choice
                else {
                    questionView = multipleChoiceNotOpened(currentQuestionNonDB, position);
                }
                // right/wrong
                if (currentQuestionNonDB.isRightWrong()) {
                }
            }

        }
        // Test already opened
        else {
            if (testNonDB.isSingleQuestion()) {
                questionView = singleChoiceAlreadyOpened(position);
            } else if (testNonDB.isMultipleCategories()) {
                questionView = new QuestionMultipleCategories(inflater, testNonDB, context, test,
                        this).multipleCategoriesAlreadyOpened();
            } else {
                QuestionNonDB currentQuestionNonDB = questions.get(position);
                // right/wrong
                if (currentQuestionNonDB.isRightWrong()) {

                }
                // check if question is multiple choice or yes/no
                if (currentQuestionNonDB.isYesOrNo()) {
                    questionView = yesNoAlreadyOpened(currentQuestionNonDB, position);

                }

                if (!currentQuestionNonDB.isRightWrong() && !currentQuestionNonDB.isYesOrNo()) {
                    questionView = multipleChoiceAlreadyOpened(currentQuestionNonDB, position);

                }
            }

        }
        return questionView;
    }


    /**
     * Single choice that is being opened for the first time.
     *
     * @param position
     * @return
     */
    private View singleChoiceNotOpened(final int position) {
        View questionView = inflater.inflate(R.layout.content_category_description, null);

        GradingNonDB currentGrading = testNonDB.getScoring().getValuesBoth().get(position);
        TextView category = (TextView) questionView.findViewById(R.id.categoryName);
        final String grade = currentGrading.getGrade();
        category.setText(grade);
        final TextView descTextView = (TextView) questionView.findViewById(R.id.categoryDescription);
        String description = currentGrading.getDescription();
        descTextView.setText(description);

        final float selected = 1f;
        final float unselected = 0.5f;

        // detect the selection
        descTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.setAlreadyOpened(true);
                test.setAnswer(grade);
                System.out.println(position + "-" + grade);
                test.setCompleted(true);
                test.save();
                // highlight
                descTextView.setAlpha(unselected);
            }
        });

        return questionView;
    }

    /**
     * March test already opened before.
     *
     * @param position
     * @return
     */
    private View singleChoiceAlreadyOpened(final int position) {
        View questionView = inflater.inflate(R.layout.content_category_description, null);

        GradingNonDB currentGrading = testNonDB.getScoring().getValuesBoth().get(position);
        TextView category = (TextView) questionView.findViewById(R.id.categoryName);
        final String grade = currentGrading.getGrade();
        category.setText(grade);
        TextView descTextView = (TextView) questionView.findViewById(R.id.categoryDescription);
        String description = currentGrading.getDescription();
        descTextView.setText(description);

        // highlight selected option


        // detect the selection
        descTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test.setAlreadyOpened(true);
                test.setAnswer(grade);
                test.save();
                // highlight
            }
        });

        return questionView;
    }

    /**
     * Multiple choice question that's being opened for the first time.
     *
     * @param currentQuestionNonDB
     * @param questionIndex
     * @return
     */
    private View multipleChoiceNotOpened(QuestionNonDB currentQuestionNonDB, int questionIndex) {
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

        for (int i = 0; i < choicesNonDB.size(); i++) {
            ChoiceNonDB currentChoice = choicesNonDB.get(i);
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
            addRadioButton(choice, radioGroup, i);

        }
        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, questionIndex));


        Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((questionIndex + 1) + " - " + question.getDescription());

        // Setup the adapter
        if (question.getGuid() == null) {
            Log.e("Error", "Must have ID!");
        }
        return questionView;
    }

    /**
     * Add a RadioButton to a RadioGroup.
     *
     * @param choice
     * @param radioGroup
     * @param i
     * @return
     */
    private RadioButton addRadioButton(Choice choice, RadioGroup radioGroup, int i) {
        RadioButton newRadioButton = new RadioButton(context);
        if (Objects.equals(choice.getName(), "") || choice.getName() == null) {
            newRadioButton.setText(choice.getDescription());
        } else {
            newRadioButton.setText(choice.getName() + " - " + choice.getDescription());
        }

        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.addView(newRadioButton, i, layoutParams);
        return newRadioButton;
    }


    /**
     * Multiple Choice Qustion, already opened before
     *
     * @param currentQuestionNonDB
     * @param position
     * @return
     */
    public View multipleChoiceAlreadyOpened(QuestionNonDB currentQuestionNonDB, int position) {
        System.out.println("Position " + position);
        View questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);
        // get Question from DB
        Question question = test.getQuestionsFromTest().get(position);

        // create Radio Group from the info in DB
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

        for (int i = 0; i < question.getChoicesForQuestion().size(); i++) {
            Choice choice = question.getChoicesForQuestion().get(i);

            // create RadioButton for that choice
            addRadioButton(choice, radioGroup, i);
        }
        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, position));

        if (question.isAnswered()) {
            System.out.println("answered");
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
            // set the selected option
            String selectedChoice = question.getSelectedChoice();
            System.out.println("sel choice is " + selectedChoice);
            ArrayList<Choice> choices = question.getChoicesForQuestion();
            int selectedIdx = -1;
            for (int i = 0; i < choices.size(); i++) {
                if (choices.get(i).getName().equals(selectedChoice)) {
                    selectedIdx = i;
                }
            }
            RadioButton selectedButton = (RadioButton) radioGroup.getChildAt(selectedIdx);
            selectedButton.setChecked(true);
        }

        // Test already opened, but question not answered
        else {
            System.out.println("not answered");
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