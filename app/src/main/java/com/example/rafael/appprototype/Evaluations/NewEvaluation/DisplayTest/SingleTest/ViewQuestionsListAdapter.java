package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.content.Context;
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

import com.example.rafael.appprototype.Constants;
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
    public ViewQuestionsListAdapter(Context context, GeriatricTestNonDB testNonDb, GeriatricTest test) {
        this.context = context;
        this.questions = testNonDb.getQuestions();
        this.test = test;
        this.testNonDB = testNonDb;

        numquestions = questions.size();
        testAlreadyOpened = test.isAlreadyOpened();
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (testNonDB.getTestName().equals(Constants.test_name_marchaHolden)) {
            return testNonDB.getScoring().getValuesBoth().size();
        }
        if (testNonDB.getQuestionsCategories().size() != 0) {
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
        if (!testAlreadyOpened) {
            if (testNonDB.getTestName().equals(Constants.test_name_marchaHolden)) {
                questionView = marchaHoldenNotOpened(position);
            } else {
                QuestionNonDB currentQuestionNonDB = questions.get(position);
                // yes/no question
                if (currentQuestionNonDB.isYesOrNo()) {
                    questionView = yesNoNotOpened(currentQuestionNonDB, position);
                }
                // multiple Choice
                else {
                    questionView = multipleChoiceNotOpened(currentQuestionNonDB, position);
                }
                // right/wrong
                if (currentQuestionNonDB.isRightWrong()) {
                    questionView = rightWrongNotOpened();
                }
            }

        }
        // Test already opened
        else {
            if (testNonDB.getTestName().equals(Constants.test_name_marchaHolden)) {
                questionView = marchaHoldenAlreadyOpened(position);
            } else {
                QuestionNonDB currentQuestionNonDB = questions.get(position);
                // right/wrong
                if (currentQuestionNonDB.isRightWrong()) {
                    questionView = rightWrongAlreadyOpened();
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
     * Holden test that is being opened for the first time.
     *
     * @param position
     * @return
     */
    private View marchaHoldenNotOpened(final int position) {
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
                System.out.println("Selected");
                test.setAlreadyOpened(true);
                test.setMarchaOption(position);
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
    private View marchaHoldenAlreadyOpened(final int position) {
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
                test.setMarchaOption(position);
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
    public View multipleChoiceAlreadyOpened(QuestionNonDB currentQuestionNonDB, int position) {
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


    public View rightWrongNotOpened() {
        View questionView = inflater.inflate(R.layout.activity_categories_list, null);

        ExpandableListAdapterCategories listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<QuestionNonDB>> listDataChild;

        // get the listview
        expListView = (ExpandableListView) questionView.findViewById(R.id.lvExp);


        // prepare data
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < testNonDB.getQuestionsCategories().size(); i++) {
            QuestionCategory cat = testNonDB.getQuestionsCategories().get(i);
            // header
            listDataHeader.add(cat.getCategory());
            // child
            List<QuestionNonDB> child = new ArrayList<>();
            for (QuestionNonDB question : cat.getQuestions()) {
                child.add(question);
            }
            listDataChild.put(listDataHeader.get(i), child); // Header, Child data
        }

        listAdapter = new ExpandableListAdapterCategories(context, listDataHeader, listDataChild,
                testNonDB, test, this, false);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return questionView;
    }

    public View rightWrongAlreadyOpened() {
        View questionView = inflater.inflate(R.layout.activity_categories_list, null);

        ExpandableListAdapterCategories listAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        HashMap<String, List<QuestionNonDB>> listDataChild;

        // get the listview
        expListView = (ExpandableListView) questionView.findViewById(R.id.lvExp);


        // prepare data
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        for (int i = 0; i < testNonDB.getQuestionsCategories().size(); i++) {
            QuestionCategory cat = testNonDB.getQuestionsCategories().get(i);
            // header
            listDataHeader.add(cat.getCategory());
            // child
            List<QuestionNonDB> child = new ArrayList<>();
            for (QuestionNonDB question : cat.getQuestions()) {
                child.add(question);
            }
            listDataChild.put(listDataHeader.get(i), child);
        }

        listAdapter = new ExpandableListAdapterCategories(context, listDataHeader, listDataChild,
                testNonDB, test, this, true);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return questionView;
    }


}