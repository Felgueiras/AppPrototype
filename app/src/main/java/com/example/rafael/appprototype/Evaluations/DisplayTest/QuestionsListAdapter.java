package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion.MultipleChoiceHandler;
import com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion.RightWrongQuestionHandler;
import com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion.YesNoQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashSet;

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
    private final Activity context;
    /**
     * Test
     */
    private final GeriatricScale scale;
    private final GeriatricScaleNonDB testNonDB;
    private final ProgressBar progressBar;
    /**
     * HasSet that will hold the numbers of the Questions that were already answered.
     */
    private HashSet<Integer> positionsFilled = new HashSet<>();


    int numquestions;
    private View questionView;


    /**
     * Display all Questions for a GeriatricScale
     *
     * @param context  current Context
     * @param test     GeriatricScale that is being filled up
     * @param progress
     */
    public QuestionsListAdapter(Activity context, GeriatricScaleNonDB testNonDb, GeriatricScale test, ProgressBar progress) {
        this.context = context;
        this.questions = testNonDb.getQuestions();
        this.scale = test;
        this.testNonDB = testNonDb;
        this.progressBar = progress;


        numquestions = questions.size();
        if (progressBar != null) {
            progressBar.setMax(numquestions);
            int numAnswered = 0;
            // check how many questions were answered
            ArrayList<Question> questions = test.getQuestionsFromScale();
            for (Question question : questions) {
                if (question.isAnswered()) {
                    numAnswered++;
                    positionsFilled.add(questions.indexOf(question));
                }
            }
            progressBar.setProgress(numAnswered);
        }

        /*
      Yes if scale already opened, no otherwise
     */
        boolean testAlreadyOpened = test.isAlreadyOpened();
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (testNonDB.isSingleQuestion()) {
            // single question scale
            numquestions = 1;
            if (progressBar != null) {
                progressBar.setMax(numquestions);
            }
            return numquestions;
        }
        if (testNonDB.isMultipleCategories()) {
            // scale with multiple categories
            numquestions = testNonDB.getNumberQuestions();
            if (progressBar != null) {
                progressBar.setMax(numquestions);
            }
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
        if (progressBar != null) {
            if (positionsFilled.size() > progressBar.getProgress())
                progressBar.setProgress(positionsFilled.size());
        }
        int nq = numquestions;
        if (testNonDB.getScoring() != null) {
            if (testNonDB.getScoring().isDifferentMenWomen()) {
                if (Constants.SESSION_GENDER == Constants.MALE) {
                    nq = 0;
                    for (QuestionNonDB questionNonDB : testNonDB.getQuestions()) {
                        if (!questionNonDB.isOnlyForWomen())
                            nq++;
                    }
                }
            }
        }


        if (positionsFilled.size() == nq) {
            if (!scale.isCompleted()) {
                DrawerLayout layout = (DrawerLayout) context.findViewById(R.id.drawer_layout);
                Snackbar.make(layout, R.string.all_questions_answered, Snackbar.LENGTH_SHORT).show();
            }

            // write that to DB
            scale.setCompleted(true);
            scale.save();
        }
    }

    public class Holder {
        TextView question;
        ListView choicesList;
    }

    @Override
    public View getView(final int questionIndex, View convertView, ViewGroup parent) {

        // multiple choice
        if (testNonDB.isMultipleChoice()) {
            QuestionNonDB currentQuestionNonDB = questions.get(questionIndex);
            questionView = multipleChoice(currentQuestionNonDB, questionIndex);
            if (currentQuestionNonDB.isOnlyForWomen() && Constants.SESSION_GENDER == Constants.MALE) {
                return inflater.inflate(R.layout.empty, null);
            }
            return questionView;
        }

        // multiple categories
        if (testNonDB.isMultipleCategories()) {
            questionView = new QuestionMultipleCategoriesIndividualCategory(inflater,
                    testNonDB,
                    (Activity) context,
                    scale,
                    this).getView();
            return questionView;
        }

        /**
         * Single question.
         */
        if (testNonDB.isSingleQuestion()) {
            questionView = singleChoice();
            return questionView;
        }

        QuestionNonDB currentQuestionNonDB = questions.get(questionIndex);
        /**
         * Numerical.
         */
        if (currentQuestionNonDB.isNumerical()) {
            questionView = numericalQuestion(currentQuestionNonDB, questionIndex);
            return questionView;
        }
        /**
         * Text input.
         */
        if (currentQuestionNonDB.isMultipleTextInput()) {
            questionView = multipleTextInput(currentQuestionNonDB, questionIndex);
            return questionView;
        }

        // right/wrong
        if (currentQuestionNonDB.isRightWrong()) {
            questionView = rightWrong(currentQuestionNonDB, questionIndex);
            return questionView;
        }

        if (currentQuestionNonDB.isYesOrNo()) {
            questionView = yesNo(currentQuestionNonDB, questionIndex);
        }

        return questionView;
    }

    /**
     * Multiple text input.
     *
     * @param currentQuestionNonDB
     * @param questionIndex
     * @return
     */
    private View multipleTextInput(QuestionNonDB currentQuestionNonDB, final int questionIndex) {
        // question in DB
        Question questionInDB;
        String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        questionInDB = Question.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new Question();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setScale(scale);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(false);
            questionInDB.setNumerical(false);
            questionInDB.setMultipleTextInput(true);
            questionInDB.save();
        }

        /**
         * Set View
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView = inflater.inflate(R.layout.content_question_multiple_text_input, null);
        TextView questionName = (TextView) questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        EditText answer = (EditText) questionView.findViewById(R.id.question_answer);

        // if question is already answered
        if (questionInDB.isAnswered()) {
            answer.setText(questionInDB.getTextAnswer());
        }

        // detect when answer changes changed
        final Question finalQuestionInDB = questionInDB;
        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalQuestionInDB.setTextAnswer(charSequence.toString());
                finalQuestionInDB.setAnswered(true);
                finalQuestionInDB.save();

                questionAnswered(questionIndex);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return questionView;
    }

    /**
     * Numerical question.
     *
     * @param questionNonDB
     * @param questionIndex
     * @return
     */
    private View numericalQuestion(final QuestionNonDB questionNonDB, final int questionIndex) {
        // question in DB
        Question questionInDB;
        String dummyID = scale.getGuid() + "-" + questionNonDB.getDescription();
        questionInDB = Question.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new Question();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(questionNonDB.getDescription());
            questionInDB.setScale(scale);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(false);
            questionInDB.setNumerical(true);
            questionInDB.save();
        }

        /**
         * Set View
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View questionView = inflater.inflate(R.layout.content_question_numerical, null);
        TextView questionName = (TextView) questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + questionNonDB.getDescription());

        EditText answer = (EditText) questionView.findViewById(R.id.question_answer);

        // if question is already answered
        if (questionInDB.isAnswered()) {
            answer.setText(questionInDB.getAnswerNumber() + "");
        }

        // detect when answer changes changed
        final Question finalQuestionInDB = questionInDB;
        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int answerNumber = 0;
                if (charSequence.length() != 0) {
                    answerNumber = Integer.parseInt(charSequence.toString());
                    finalQuestionInDB.setAnswerNumber(answerNumber);
                    finalQuestionInDB.setAnswered(true);
                    finalQuestionInDB.save();
                }


                questionAnswered(questionIndex);
                if (answerNumber > questionNonDB.getNumericalMax()) {
                    Snackbar.make(questionView, R.string.numerical_question_overflow, Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return questionView;
    }


    /**
     * Right or wrong question.
     *
     * @param currentQuestionNonDB
     * @param questionIndex
     * @return
     */
    private View rightWrong(QuestionNonDB currentQuestionNonDB, final int questionIndex) {
        // question in DB
        Question questionInDB;
        String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        questionInDB = Question.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new Question();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setScale(scale);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(true);
            questionInDB.save();
        }


        /**
         * Set View
         */
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView = infalInflater.inflate(R.layout.content_question_right_wrong_icons, null);
        TextView questionName = (TextView) questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        // right and wrong button
        final ImageButton right = (ImageButton) questionView.findViewById(R.id.rightChoice);
        final ImageButton wrong = (ImageButton) questionView.findViewById(R.id.wrongChoice);

        // if question is already answered
        if (questionInDB.isAnswered()) {
            ////system.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                right.setImageResource(R.drawable.ic_check_box_black_24dp);
            } else {
                wrong.setImageResource(R.drawable.close_box);
            }
        }

        final QuestionsListAdapter adapter = this;

        final Question finalQuestionInDB = questionInDB;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // detect when choice changed
                if (v.getId() == R.id.rightChoice) {
                    finalQuestionInDB.setSelectedRightWrong("right");
                    right.setImageResource(R.drawable.ic_check_box_black_24dp);
                    wrong.setImageResource(R.drawable.ic_close_black_24dp);
                } else if (v.getId() == R.id.wrongChoice) {
                    finalQuestionInDB.setSelectedRightWrong("wrong");
                    right.setImageResource(R.drawable.ic_check_black_24dp);
                    wrong.setImageResource(R.drawable.close_box);
                } else {
                    return;
                }
                finalQuestionInDB.setAnswered(true);
                finalQuestionInDB.save();
                /**
                 * Signal that que Question was answered
                 */
                adapter.questionAnswered(questionIndex);
            }
        };

        right.setOnClickListener(clickListener);
        wrong.setOnClickListener(clickListener);


        return questionView;
    }

    /**
     * Single choice scale.
     *
     * @return
     */
    private View singleChoice() {
        View questionView = inflater.inflate(R.layout.content_question_single_multiple_choice, null);

        // create Choices and add to DB
        final ArrayList<GradingNonDB> gradings = testNonDB.getScoring().getValuesBoth();
        testNonDB.getScoring().getValuesBoth();
        //ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

        for (int i = 0; i < gradings.size(); i++) {
            RadioButton newRadioButton = new RadioButton(context);
            GradingNonDB currentGrading = gradings.get(i);
            String grade = currentGrading.getGrade();
            String description = currentGrading.getDescription();
            newRadioButton.setText(description);
            newRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.font_size));

            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(newRadioButton, i, layoutParams);
            if (scale.isCompleted())
                if (scale.getAnswer().equals(grade))
                    newRadioButton.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioButtonIndex) {
                radioButtonIndex = (radioButtonIndex - 1) % gradings.size();
                questionAnswered(0);
                // get grade
                GradingNonDB grading = gradings.get(radioButtonIndex);
                scale.setAlreadyOpened(true);
                scale.setAnswer(grading.getGrade());
                scale.setCompleted(true);
                scale.save();
            }
        });

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
    private RadioButton addRadioButton(Choice choice, RadioGroup radioGroup, int i, Activity context) {
        RadioButton newRadioButton = new RadioButton(context);
        if (choice.getName().equals("") || choice.getName() == null) {
            newRadioButton.setText(choice.getDescription());
        } else {
            if (!choice.getName().equals(choice.getDescription()))
                newRadioButton.setText(choice.getName() + " - " + choice.getDescription());
            else
                newRadioButton.setText(choice.getDescription());
        }

        newRadioButton.setTextAppearance(context,R.style.tablet_text2);
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
    public View multipleChoice(QuestionNonDB currentQuestionNonDB, int position) {

        // show all or click and open AlertDialog to choose option
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        // create Layout
        String multipleChoiceType = SP.getString(context.getResources().getString(R.string.multipleChoiceType), "2");
        if (Constants.screenSize > Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            multipleChoiceType = SP.getString(context.getResources().getString(R.string.multipleChoiceType), "1");
        }
        Log.d("Multiple", multipleChoiceType);
        if (multipleChoiceType.equals("1")) {
            return multipleChoiceShowAllOptions(currentQuestionNonDB, position);
        } else if (multipleChoiceType.equals("2")) {
            return multipleChoiceSelectFromDialog(currentQuestionNonDB, position);
        }
        return null;

    }

    private View multipleChoiceShowAllOptions(QuestionNonDB currentQuestionNonDB, int position) {

        View questionView = inflater.inflate(R.layout.content_question_multiple_choice, null);

        RadioGroup radioGroup;
        // check if is already in DB
        String questionID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        Question question = Question.getQuestionByID(questionID);
        if (question == null) {
            // create question and add to DB
            question = new Question();
            question.setDescription(currentQuestionNonDB.getDescription());
            question.setGuid(questionID);
            question.setScale(scale);
            question.setYesOrNo(false);
            question.save();

            // create Choices and add to DB
            ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();
            radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

            for (int i = 0; i < choicesNonDB.size(); i++) {
                ChoiceNonDB currentChoice = choicesNonDB.get(i);
                Choice choice = new Choice();
                String choiceID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription() + "-" + currentChoice.getDescription();
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
                addRadioButton(choice, radioGroup, i, context);
            }

        } else {
            // get Question from DB
            question = scale.getQuestionsFromScale().get(position);
            // create Radio Group from the info in DB
            radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

            for (int i = 0; i < question.getChoicesForQuestion().size(); i++) {
                Choice choice = question.getChoicesForQuestion().get(i);

                // create RadioButton for that choice
                addRadioButton(choice, radioGroup, i, context);
            }
        }


        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, position));

        if (question.isAnswered()) {
            //system.out.println("answered");
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
            // set the selected option
            String selectedChoice = question.getSelectedChoice();
            //system.out.println("sel choice is " + selectedChoice);
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
            //system.out.println("not answered");
            Holder holder = new Holder();
            holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
        }
        return questionView;
    }


    /**
     * Multiple choice where option is selected from an AlertDialog.
     *
     * @param currentQuestionNonDB
     * @param questionIndex
     * @return
     */
    private View multipleChoiceSelectFromDialog(QuestionNonDB currentQuestionNonDB, final int questionIndex) {

        View questionView = inflater.inflate(R.layout.content_question_multiple_choice_alertdialog, null);

        String questionText = (questionIndex + 1) + " - " + currentQuestionNonDB.getDescription();

        //list of items
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(questionText);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.select_dialog_singlechoice);

        // check if is already in DB
        String questionID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        Question question = Question.getQuestionByID(questionID);
        if (question == null) {
            // create question and add to DB
            question = new Question();
            question.setDescription(currentQuestionNonDB.getDescription());
            question.setGuid(questionID);
            question.setScale(scale);
            question.setYesOrNo(false);
            question.save();

            // create Choices and add to DB
            ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();

            for (int i = 0; i < choicesNonDB.size(); i++) {
                ChoiceNonDB currentChoice = choicesNonDB.get(i);
                Choice choice = new Choice();
                String choiceID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription() + "-" + currentChoice.getDescription();
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

                if (choice.getName().equals("") || choice.getName() == null) {
                    arrayAdapter.add(choice.getDescription());
                } else {
                    if (!choice.getName().equals(choice.getDescription())) {
                        arrayAdapter.add(choice.getName() + " - " + choice.getDescription());
                    } else {
                        arrayAdapter.add(choice.getDescription());
                    }
                }
            }

        } else {
            /**
             * Question in DB,
             */
            // get Question from DB
            question = scale.getQuestionsFromScale().get(questionIndex);
            // create Radio Group from the info in DB

            for (int i = 0; i < question.getChoicesForQuestion().size(); i++) {
                Choice choice = question.getChoicesForQuestion().get(i);

                if (choice.getName().equals("") || choice.getName() == null) {
                    arrayAdapter.add(choice.getDescription());
                } else {
                    if (!choice.getName().equals(choice.getDescription())) {
                        arrayAdapter.add(choice.getName() + " - " + choice.getDescription());
                    } else {
                        arrayAdapter.add(choice.getDescription());
                    }
                }
            }
        }

        final Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText(questionText);

        // check if already answered
        if (question.isAnswered()) {
            holder.question.setBackgroundResource(R.color.question_answered);
        }

        final Question finalQuestion = question;
        final QuestionsListAdapter adapter = this;
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selectedAnswer) {
                        MultipleChoiceHandler multipleChoiceHandler = new MultipleChoiceHandler(finalQuestion, adapter, questionIndex);
                        multipleChoiceHandler.selectedFromAlertDialog(selectedAnswer);
                        holder.question.setBackgroundResource(R.color.question_answered);
                        dialog.dismiss();
                    }
                });


        final Question finalQuestion1 = question;
        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if already answered
                int selectedIdx = -1;
                if (finalQuestion1.isAnswered()) {

                    holder.question.setBackgroundResource(R.color.question_answered);
                    // set the selected option
                    String selectedChoice = finalQuestion1.getSelectedChoice();
                    //system.out.println("sel choice is " + selectedChoice);
                    ArrayList<Choice> choices = finalQuestion1.getChoicesForQuestion();
                    for (int i = 0; i < choices.size(); i++) {
                        if (choices.get(i).getName().equals(selectedChoice)) {
                            selectedIdx = i;
                        }
                    }
                }
                builderSingle.setSingleChoiceItems(arrayAdapter, selectedIdx,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedAnswer) {
                                MultipleChoiceHandler multipleChoiceHandler = new MultipleChoiceHandler(finalQuestion, adapter, questionIndex);
                                multipleChoiceHandler.selectedFromAlertDialog(selectedAnswer);
                                holder.question.setBackgroundResource(R.color.question_answered);
                                dialog.dismiss();
                            }
                        });
                builderSingle.show();
            }
        });

        return questionView;
    }


    private View yesNo(QuestionNonDB currentQuestionNonDB, int position) {
        // question in DB
        Question question;
        String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        question = Question.getQuestionByID(dummyID);
        if (question == null) {
            // create question and add to DB
            question = new Question();
            question.setGuid(dummyID);
            question.setDescription(currentQuestionNonDB.getDescription());
            question.setScale(scale);
            question.setYesOrNo(true);
            question.setRightWrong(false);
            question.setYesValue(currentQuestionNonDB.getYesScore());
            question.setNoValue(currentQuestionNonDB.getNoScore());
            question.save();
        }


        /**
         * Set View
         */
        View questionView = inflater.inflate(R.layout.content_question_yes_no, null);
        Holder holder = new Holder();
        holder.question = (TextView) questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
        // detect when choice changed
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);

        /**
         * Question already answered.
         */
        if (question.isAnswered()) {
            if (question.getSelectedYesNoChoice().equals("yes")) {
                radioGroup.check(R.id.yesChoice);
            } else {
                radioGroup.check(R.id.noChoice);
            }
        }

        radioGroup.setOnCheckedChangeListener(new YesNoQuestionHandler(question, this, position));
        return questionView;
    }


}