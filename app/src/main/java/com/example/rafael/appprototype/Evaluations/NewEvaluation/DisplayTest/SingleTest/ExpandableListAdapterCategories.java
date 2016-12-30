package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.RightWrongQuestionHandler;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleQuestion.YesNoQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 03-11-2016.
 */
public class ExpandableListAdapterCategories extends BaseExpandableListAdapter {

    private final GeriatricTestNonDB testNonDB;
    private final GeriatricTest test;
    private final ViewQuestionsListAdapter adapter;
    private final boolean alreadyOpened;
    private Context _context;
    /**
     * Headers.
     */
    private List<String> _listDataHeader;
    /**
     * Children.
     */
    private HashMap<String, List<QuestionNonDB>> _listDataChild;

    public ExpandableListAdapterCategories(Context context, List<String> listDataHeader,
                                           HashMap<String, List<QuestionNonDB>> listChildData,
                                           GeriatricTestNonDB testNonDB,
                                           GeriatricTest test, ViewQuestionsListAdapter viewQuestionsListAdapter, boolean alreadyOpened) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.testNonDB = testNonDB;
        this.test = test;
        this.adapter = viewQuestionsListAdapter;
        this.alreadyOpened = alreadyOpened;
    }

    @Override
    public QuestionNonDB getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.content_question_right_wrong, null);
        }

        if (childPosition == 0) {
            // display category info
            ViewStub simpleViewStub = ((ViewStub) convertView.findViewById(R.id.simpleViewStub)); // get the reference of ViewStub
            if (simpleViewStub != null) {
                // only inflate once
                View inflated = simpleViewStub.inflate();
                TextView tx = (TextView) inflated.findViewById(R.id.stub_text);
                QuestionCategory currentCategory = testNonDB.getQuestionsCategories().get(groupPosition);
                tx.setText(currentCategory.getDescription());
            }
        }

        // question not in DB
        QuestionNonDB currentQuestionNonDB = testNonDB.getQuestionsCategories().get(groupPosition).getQuestions().get(childPosition);
        // question in DB
        Question questionInDB;
        String dummyID = test.getGuid() + "-" + currentQuestionNonDB.getDescription();


        if (!alreadyOpened) {
            questionInDB = new Question();
            // create question and add to DB
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setTest(test);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(true);
            questionInDB.save();
        } else {
            System.out.println("Already opened");
            // question already in DB, fetch it
            int questionIndex = QuestionCategory.getQuestionIndex(groupPosition, childPosition, testNonDB);
            questionInDB = Question.getQuestionByID(dummyID);
            System.out.println("Question index is " + questionIndex);
        }


        /**
         * Set View
         */
        TextView questionName = (TextView) convertView.findViewById(R.id.nameQuestion);
        questionName.setText((childPosition + 1) + " - " + currentQuestionNonDB.getDescription());
        // detect when choice changed
        RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RightWrongQuestionHandler(questionInDB, adapter, groupPosition, childPosition, testNonDB));
        // if question is already answered
        if (questionInDB.isAnswered()) {
            System.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                radioGroup.check(R.id.rightChoice);
            } else {
                radioGroup.check(R.id.noChoice);
            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}