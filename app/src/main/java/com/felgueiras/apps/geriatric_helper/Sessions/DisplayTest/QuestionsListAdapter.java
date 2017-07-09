package com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.Constants;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.ChoiceNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.GradingNonDB;
import com.felgueiras.apps.geriatric_helper.DataTypes.NonDB.QuestionNonDB;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.FirebaseDatabaseHelper;
import com.felgueiras.apps.geriatric_helper.HelpersHandlers.SharedPreferencesHelper;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.QuestionCategoriesViewPager.QuestionMultipleCategoriesViewPager;
import com.felgueiras.apps.geriatric_helper.Sessions.DisplayTest.SingleQuestion.MultipleChoiceHandler;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatric_helper.Firebase.RealtimeDatabase.QuestionFirebase;
import com.felgueiras.apps.geriatric_helper.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Create the layout of the Questions
 */
public class QuestionsListAdapter extends BaseAdapter implements Serializable {
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
    private final GeriatricScaleFirebase scale;
    private final GeriatricScaleNonDB testNonDB;
    private final ProgressBar progressBar;
    private final FragmentManager childFragmentManager;
    private final ListView listView;
    private final Button saveScaleButton;
    /**
     * HasSet that will hold the numbers of the Questions that were already answered.
     */
    private HashSet<Integer> positionsFilled = new HashSet<>();
    private ViewPager viewPagerAux;
    private TourGuide saveScaleTourGuide;

    public FragmentManager getChildFragmentManager() {
        return childFragmentManager;
    }

    int numquestions;
    private View questionView;


    /**
     * Display all Questions for a GeriatricScale
     *
     * @param context              current Context
     * @param test                 GeriatricScale that is being filled up
     * @param progress
     * @param childFragmentManager
     * @param listView
     * @param saveScaleButton
     */
    public QuestionsListAdapter(Activity context, GeriatricScaleNonDB testNonDb, GeriatricScaleFirebase test, ProgressBar progress, FragmentManager childFragmentManager, ListView listView, Button saveScaleButton) {
        this.context = context;
        this.questions = testNonDb.getQuestions();
        this.scale = test;
        this.testNonDB = testNonDb;
        this.progressBar = progress;
        this.childFragmentManager = childFragmentManager;
        this.listView = listView;
        this.saveScaleButton = saveScaleButton;


        numquestions = questions.size();
        if (progressBar != null) {
            progressBar.setMax(numquestions);
            int numAnswered = 0;
            // check how many questions were answered
            ArrayList<QuestionFirebase> questions = FirebaseDatabaseHelper.getQuestionsFromScale(scale);
            for (QuestionFirebase question : questions) {
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
                DrawerLayout layout = context.findViewById(R.id.drawer_layout);
                Snackbar.make(layout, R.string.all_questions_answered, Snackbar.LENGTH_SHORT).show();

                // show tour guide to save this scale
                if (SharedPreferencesHelper.showTour(context)) {

                    Animation enterAnimation = new AlphaAnimation(0f, 1f);
                    enterAnimation.setDuration(600);
                    enterAnimation.setFillAfter(true);

                    Animation exitAnimation = new AlphaAnimation(1f, 0f);
                    exitAnimation.setDuration(600);
                    exitAnimation.setFillAfter(true);

                    // create TourGuide on first area
                    saveScaleTourGuide = TourGuide.init(context).with(TourGuide.Technique.Click)
                            .setPointer(new Pointer())
                            .setToolTip(new ToolTip().setTitle("Guardar Escale").setDescription("Depois de preencher a escala, clique aqui para" +
                                    " a guardar e voltar à lista de escalas da área selecionada")
                                    .setGravity(Gravity.TOP | Gravity.CENTER))
                            .setOverlay(new Overlay().
                                    setEnterAnimation(enterAnimation)
                                    .setExitAnimation(exitAnimation))
                            .playOn(saveScaleButton);

                    // TODO handle click on tour guide
                    if (saveScaleTourGuide != null) {
                        saveScaleTourGuide.cleanUp();
                    }
                }
            }

            // write that to DB
            scale.setCompleted(true);

            FirebaseDatabaseHelper.updateScale(scale);
        }
    }

    public void setViewPagerAux(ViewPager viewPagerAux) {
        this.viewPagerAux = viewPagerAux;
    }

    public ViewPager getViewPagerAux() {
        return viewPagerAux;
    }

    public class Holder {
        public TextView question;
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
            questionView = new QuestionMultipleCategoriesViewPager(inflater,
                    testNonDB,
                    context,
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
        QuestionFirebase questionInDB;
        String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        questionInDB = FirebaseDatabaseHelper.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new QuestionFirebase();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setScaleID(scale.getGuid());
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(false);
            questionInDB.setNumerical(false);
            questionInDB.setMultipleTextInput(true);
            FirebaseDatabaseHelper.createQuestion(questionInDB);
        }

        /**
         * Set View
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView = inflater.inflate(R.layout.content_question_multiple_text_input, null);
        TextView questionName = questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        EditText answer = questionView.findViewById(R.id.question_answer);

        // if question is already answered
        if (questionInDB.isAnswered()) {
            answer.setText(questionInDB.getTextAnswer());
        }

        // detect when answer changes changed
        final QuestionFirebase finalQuestionInDB = questionInDB;
        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalQuestionInDB.setTextAnswer(charSequence.toString());
                finalQuestionInDB.setAnswered(true);
                FirebaseDatabaseHelper.createQuestion(finalQuestionInDB);

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
        QuestionFirebase questionInDB;
        String dummyID = scale.getGuid() + "-" + questionNonDB.getDescription();
        questionInDB = FirebaseDatabaseHelper.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new QuestionFirebase();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(questionNonDB.getDescription());
            questionInDB.setScaleID(scale.getGuid());
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(false);
            questionInDB.setNumerical(true);
            FirebaseDatabaseHelper.createQuestion(questionInDB);
        }

        /**
         * Set View
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View questionView = inflater.inflate(R.layout.content_question_numerical, null);
        TextView questionName = questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + questionNonDB.getDescription());

        EditText answer = questionView.findViewById(R.id.question_answer);

        // if question is already answered
        if (questionInDB.isAnswered()) {
            answer.setText(questionInDB.getAnswerNumber() + "");
        }

        // detect when answer changes changed
        final QuestionFirebase finalQuestionInDB = questionInDB;
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
                    FirebaseDatabaseHelper.createQuestion(finalQuestionInDB);
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
        QuestionFirebase questionInDB;
        String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
        questionInDB = FirebaseDatabaseHelper.getQuestionByID(dummyID);
        if (questionInDB == null) {
            // create question and add to DB
            questionInDB = new QuestionFirebase();
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setScaleID(scale.getGuid());
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(true);
            FirebaseDatabaseHelper.createQuestion(questionInDB);

        }


        /**
         * Set View
         */
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View questionView = infalInflater.inflate(R.layout.content_question_right_wrong_icons, null);
        TextView questionName = questionView.findViewById(R.id.nameQuestion);
        questionName.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());

        // right and wrong button
        final ImageButton right = questionView.findViewById(R.id.rightChoice);
        final ImageButton wrong = questionView.findViewById(R.id.wrongChoice);

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

        final QuestionFirebase finalQuestionInDB = questionInDB;
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
                FirebaseDatabaseHelper.createQuestion(finalQuestionInDB);

                /**
                 * Signal that que Question was answered
                 */
                adapter.questionAnswered(questionIndex);

                listView.smoothScrollToPosition(questionIndex + 1);

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
        RadioGroup radioGroup = questionView.findViewById(R.id.radioGroup);

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
            if (scale != null) {
                if (scale.isCompleted())
                    if (scale.getAnswer().equals(grade))
                        newRadioButton.setChecked(true);
            }

        }

        if (scale != null) {
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


                    FirebaseDatabaseHelper.updateScale(scale);
                }
            });
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
    private RadioButton addRadioButton(ChoiceNonDB choice, RadioGroup radioGroup, int i, Activity context) {
        RadioButton newRadioButton = new RadioButton(context);
        if (choice.getName().equals("") || choice.getName() == null) {
            newRadioButton.setText(choice.getDescription());
        } else {
            if (!choice.getName().equals(choice.getDescription()))
                newRadioButton.setText(choice.getName() + " - " + choice.getDescription());
            else
                newRadioButton.setText(choice.getDescription());
        }

        newRadioButton.setTextAppearance(context, R.style.tablet_text2);
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
        QuestionFirebase question = FirebaseDatabaseHelper.getQuestionByID(questionID);
        if (question == null) {
            // create question and add to DB
            question = new QuestionFirebase();
            question.setDescription(currentQuestionNonDB.getDescription());
            question.setGuid(questionID);
            question.setScaleID(scale.getGuid());
            question.setYesOrNo(false);
            FirebaseDatabaseHelper.createQuestion(question);


            // create Choices and add to DB
            ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();
            radioGroup = questionView.findViewById(R.id.radioGroup);

            for (int i = 0; i < choicesNonDB.size(); i++) {
                ChoiceNonDB currentChoice = choicesNonDB.get(i);
//                ChoiceFirebase choice = new ChoiceFirebase();
//                String choiceID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription() + "-" + currentChoice.getDescription();
//                // check if already in DB
//                if (FirebaseDatabaseHelper.getChoiceByID(choiceID) == null) {
//                    choice.setGuid(choiceID);
//                    choice.setQuestionID(question.getGuid());
//                    if (currentChoice.getName() != null)
//                        choice.setName(currentChoice.getName());
//                    choice.setDescription(currentChoice.getDescription());
//                    choice.setScore(currentChoice.getScore());
//
//                    FirebaseDatabaseHelper.createChoice(choice);
//                }

                // create RadioButton for that choice
                addRadioButton(currentChoice, radioGroup, i, context);
            }

        } else {
            // get Question from DB
            question = FirebaseDatabaseHelper.getQuestionsFromScale(scale).get(position);
            // create Radio Group from the info in DB
            radioGroup = questionView.findViewById(R.id.radioGroup);

            for (int i = 0; i < FirebaseDatabaseHelper.getChoicesForQuestion(currentQuestionNonDB).size(); i++) {
                ChoiceNonDB choice = FirebaseDatabaseHelper.getChoicesForQuestion(currentQuestionNonDB).get(i);

                // create RadioButton for that choice
                addRadioButton(choice, radioGroup, i, context);
            }
        }


        radioGroup.setOnCheckedChangeListener(new MultipleChoiceHandler(question, this, position, listView, currentQuestionNonDB));

        if (question.isAnswered()) {
            //system.out.println("answered");
            Holder holder = new Holder();
            holder.question = questionView.findViewById(R.id.nameQuestion);
            holder.question.setText((position + 1) + " - " + currentQuestionNonDB.getDescription());
            // set the selected option
            String selectedChoice = question.getSelectedChoice();
            //system.out.println("sel choice is " + selectedChoice);
            ArrayList<ChoiceNonDB> choices = FirebaseDatabaseHelper.getChoicesForQuestion(currentQuestionNonDB);
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
            holder.question = questionView.findViewById(R.id.nameQuestion);
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
    private View multipleChoiceSelectFromDialog(final QuestionNonDB currentQuestionNonDB, final int questionIndex) {

        View questionView = inflater.inflate(R.layout.content_question_multiple_choice_alertdialog, null);

        String questionText = (questionIndex + 1) + " - " + currentQuestionNonDB.getDescription();

        //list of items
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(questionText);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_singlechoice) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position % 2 == 0) { // we're on an even row
                    v.setBackgroundColor(context.getResources().getColor(R.color.multiple_choice_grey));
//                } else {
//                    v.setBackgroundColor(context.getResources().getColor(R.color.Aqua));
//                }

                }
                return v;
            }

        };


        // check if is already in DB

        QuestionFirebase question = null;
        if (scale != null) {
            String questionID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
            question = FirebaseDatabaseHelper.getQuestionByID(questionID);
            if (question == null) {
                // create question and add to DB
                question = new QuestionFirebase();
                question.setDescription(currentQuestionNonDB.getDescription());
                question.setGuid(questionID);
                question.setScaleID(scale.getGuid());
                question.setYesOrNo(false);

                FirebaseDatabaseHelper.createQuestion(question);

                // create Choices and add to DB
                ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();

                for (int i = 0; i < choicesNonDB.size(); i++) {
                    ChoiceNonDB currentChoice = choicesNonDB.get(i);

                    if (currentChoice.getName().equals("") || currentChoice.getName() == null) {
                        arrayAdapter.add(currentChoice.getDescription());
                    } else {
                        if (!currentChoice.getName().equals(currentChoice.getDescription())) {
                            // TODO put choice name in bold
                            arrayAdapter.add(currentChoice.getName() + " - " + currentChoice.getDescription());
                        } else {
                            arrayAdapter.add(currentChoice.getDescription());
                        }
                    }
                }


            } else {
                /**
                 * Question in DB,
                 */
                // get Question from DB
                question = FirebaseDatabaseHelper.getQuestionsFromScale(scale).get(questionIndex);
                // create Radio Group from the info in DB

                ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();

                for (int i = 0; i < choicesNonDB.size(); i++) {
                    ChoiceNonDB currentChoice = choicesNonDB.get(i);

                    if (currentChoice.getName().equals("") || currentChoice.getName() == null) {
                        arrayAdapter.add(currentChoice.getDescription());
                    } else {
                        if (!currentChoice.getName().equals(currentChoice.getDescription())) {
                            arrayAdapter.add(currentChoice.getName() + " - " + currentChoice.getDescription());
                        } else {
                            arrayAdapter.add(currentChoice.getDescription());
                        }
                    }
                }
            }
        } else {
            ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();

            for (int i = 0; i < choicesNonDB.size(); i++) {
                ChoiceNonDB currentChoice = choicesNonDB.get(i);


                if (currentChoice.getName().equals("") || currentChoice.getName() == null) {
                    arrayAdapter.add(currentChoice.getDescription());
                } else {
                    if (!currentChoice.getName().equals(currentChoice.getDescription())) {
                        arrayAdapter.add(currentChoice.getName() + " - " + currentChoice.getDescription());
                    } else {
                        arrayAdapter.add(currentChoice.getDescription());
                    }
                }
            }
        }

        final Holder holder = new Holder();
        holder.question = questionView.findViewById(R.id.nameQuestion);
        holder.question.setText(questionText);

        // check if already answered
        if (question != null && question.isAnswered()) {
            holder.question.setBackgroundResource(R.color.question_answered);
        }

        final QuestionFirebase finalQuestionFirebase = question;
        final QuestionsListAdapter adapter = this;
        if (question != null) {
            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int selectedAnswer) {
                            MultipleChoiceHandler multipleChoiceHandler = new MultipleChoiceHandler(finalQuestionFirebase, adapter, questionIndex, listView, currentQuestionNonDB);
                            multipleChoiceHandler.selectedFromAlertDialog(selectedAnswer);
                            holder.question.setBackgroundResource(R.color.question_answered);
                            dialog.dismiss();
                        }
                    });

        } else {
            builderSingle.setAdapter(arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int selectedAnswer) {
                            MultipleChoiceHandler multipleChoiceHandler = new MultipleChoiceHandler(finalQuestionFirebase, adapter, questionIndex, listView, currentQuestionNonDB);
                            dialog.dismiss();
                        }
                    });
        }


        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if already answered
                int selectedIdx = -1;
                if (finalQuestionFirebase != null && finalQuestionFirebase.isAnswered()) {

                    holder.question.setBackgroundResource(R.color.question_answered);
                    // set the selected option
                    String selectedChoice = finalQuestionFirebase.getSelectedChoice();
                    //system.out.println("sel choice is " + selectedChoice);
                    ArrayList<ChoiceNonDB> choicesNonDB = currentQuestionNonDB.getChoices();

                    for (int i = 0; i < choicesNonDB.size(); i++) {
                        if (choicesNonDB.get(i).getName().equals(selectedChoice)) {
                            selectedIdx = i;
                        }
                    }
                }
                // TODO add shading
                builderSingle.setSingleChoiceItems(arrayAdapter, selectedIdx,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedAnswer) {
                                if (finalQuestionFirebase != null) {
                                    MultipleChoiceHandler multipleChoiceHandler = new MultipleChoiceHandler(
                                            finalQuestionFirebase,
                                            adapter,
                                            questionIndex,
                                            listView,
                                            currentQuestionNonDB);
                                    multipleChoiceHandler.selectedFromAlertDialog(selectedAnswer);
                                    holder.question.setBackgroundResource(R.color.question_answered);

                                }
                                dialog.dismiss();
                            }
                        });
                builderSingle.show();
            }
        });


        return questionView;
    }


    private View yesNo(QuestionNonDB currentQuestionNonDB, final int questionIndex) {
        // question in DB
        QuestionFirebase question = null;
        if (scale != null) {
            String dummyID = scale.getGuid() + "-" + currentQuestionNonDB.getDescription();
            question = FirebaseDatabaseHelper.getQuestionByID(dummyID);
            if (question == null) {
                // create question and add to DB
                question = new QuestionFirebase();
                question.setGuid(dummyID);
                question.setDescription(currentQuestionNonDB.getDescription());
                question.setScaleID(scale.getGuid());
                question.setYesOrNo(true);
                question.setRightWrong(false);
                question.setYesValue(currentQuestionNonDB.getYesScore());
                question.setNoValue(currentQuestionNonDB.getNoScore());

                FirebaseDatabaseHelper.createQuestion(question);
            }
        }


        /**
         * Set View
         */
        View questionView = inflater.inflate(R.layout.content_question_yes_no, null);
        Holder holder = new Holder();
        holder.question = questionView.findViewById(R.id.nameQuestion);
        holder.question.setText((questionIndex + 1) + " - " + currentQuestionNonDB.getDescription());
        // detect when choice changed
//        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);


        // yes and no buttons
        final Button yes = questionView.findViewById(R.id.rightChoice);
        final Button no = questionView.findViewById(R.id.wrongChoice);

        /**
         * Question already answered.
         */
        if (question != null && question.isAnswered()) {
//            questionView.setBackgroundResource(R.color.question_answered);
            if (question.getSelectedYesNoChoice().equals("yes")) {
                yes.setBackgroundColor(context.getResources().getColor(R.color.yes_light));
            } else {
                no.setBackgroundColor(context.getResources().getColor(R.color.no_light));
            }
        }

        final QuestionFirebase finalQuestion = question;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // detect when choice changed
                if (finalQuestion != null) {
                    if (v.getId() == R.id.rightChoice) {
                        finalQuestion.setSelectedYesNoChoice("yes");
                        yes.setBackgroundResource(R.color.yes_light);
                        no.setBackgroundResource(android.R.drawable.btn_default);
                    } else if (v.getId() == R.id.wrongChoice) {
                        finalQuestion.setSelectedYesNoChoice("no");
                        yes.setBackgroundResource(android.R.drawable.btn_default);
                        no.setBackgroundResource(R.color.no_light);
                    } else {
                        return;
                    }
                    finalQuestion.setAnswered(true);
                    FirebaseDatabaseHelper.updateQuestion(finalQuestion);

                    /**
                     * Signal that que Question was answered
                     */
                    questionAnswered(questionIndex);
                    /**
                     * Scroll to next position.
                     */
                    listView.smoothScrollToPosition(questionIndex + 1);
                }

            }
        };

        yes.setOnClickListener(clickListener);
        no.setOnClickListener(clickListener);

        return questionView;
    }


}