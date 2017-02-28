package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewStub;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion.RightWrongQuestionHandler;
import com.example.rafael.appprototype.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 03-11-2016.
 */
public class ExpandableListAdapterCategories extends BaseExpandableListAdapter {

    private final GeriatricScaleNonDB testNonDB;
    private final GeriatricScale test;
    private final QuestionsListAdapter adapter;
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
                                           GeriatricScaleNonDB testNonDB,
                                           GeriatricScale test,
                                           QuestionsListAdapter viewQuestionsListAdapter) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.testNonDB = testNonDB;
        this.test = test;
        this.adapter = viewQuestionsListAdapter;
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
    public View getChildView(int categoryIndex,
                             int childPosition,
                             boolean isLastChild,
                             View questionView,
                             ViewGroup parent) {
        if (questionView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            questionView = infalInflater.inflate(R.layout.content_question_right_wrong, null);
        }

        QuestionCategory currentCategory = testNonDB.getQuestionsCategories().get(categoryIndex);

        ViewStub categoryInfo = ((ViewStub) questionView.findViewById(R.id.category_info_stub)); // get the reference of ViewStub
//        if (childPosition == 0) {
//            // display category info
//            if (categoryInfo != null) {
//                // only inflate once
//                View inflated = categoryInfo.inflate();
//                TextView tx = (TextView) inflated.findViewById(R.id.stub_text);
//                tx.setText(currentCategory.getDescription());
//            }
//        } else {
//            if(categoryInfo!=null)
//            {
//                ViewManager parentView = (ViewManager) categoryInfo.getParent();
//                parentView.removeView(categoryInfo);
//            }
//        }

        // question not in DB
        QuestionNonDB currentQuestionNonDB = testNonDB.getQuestionsCategories().get(categoryIndex).getQuestions().get(childPosition);
        // question in DB
        Question questionInDB;
        int questionIndex = QuestionCategory.getQuestionIndex(categoryIndex, childPosition, testNonDB);
        String dummyID = test.getGuid() + "-" + questionIndex;
        questionInDB = Question.getQuestionByID(dummyID);
        if (questionInDB == null) {
            questionInDB = new Question();
            // create question and add to DB
            questionInDB.setGuid(dummyID);
            questionInDB.setDescription(currentQuestionNonDB.getDescription());
            questionInDB.setTest(test);
            questionInDB.setYesOrNo(false);
            questionInDB.setRightWrong(true);
            questionInDB.save();
        }


        /**
         * Set View
         */
        TextView questionName = (TextView) questionView.findViewById(R.id.nameQuestion);
        questionName.setText((childPosition + 1) + " - " + currentQuestionNonDB.getDescription());
        // detect when choice changed
        RadioGroup radioGroup = (RadioGroup) questionView.findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RightWrongQuestionHandler(questionInDB, adapter, categoryIndex, childPosition, testNonDB));
        // if question is already answered
        if (questionInDB.isAnswered()) {
            ////system.out.println(questionInDB.toString());
            if (questionInDB.getSelectedRightWrong().equals("right")) {
                radioGroup.check(R.id.rightChoice);
            } else {
                radioGroup.check(R.id.wrongChoice);
            }
        }


        return questionView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
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