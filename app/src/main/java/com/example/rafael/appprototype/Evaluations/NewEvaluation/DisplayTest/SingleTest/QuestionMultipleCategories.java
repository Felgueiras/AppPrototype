package com.example.rafael.appprototype.Evaluations.NewEvaluation.DisplayTest.SingleTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricTest;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricTestNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionNonDB;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 08-01-2017.
 */
public class QuestionMultipleCategories {


    private final LayoutInflater inflater;
    private final GeriatricTestNonDB testNonDB;
    private final Context context;
    GeriatricTest test;
    private final QuestionsListAdapter adapter;

    public QuestionMultipleCategories(LayoutInflater inflater, GeriatricTestNonDB testNonDB, Context context, GeriatricTest test, QuestionsListAdapter adapter) {
        this.inflater = inflater;
        this.testNonDB = testNonDB;
        this.context = context;
        this.test = test;
        this.adapter = adapter;
    }

    /**
     * Multiple question categories.
     *
     * @return
     */
    public View multipleCategoriesNotOpened() {
        View questionView = inflater.inflate(R.layout.questions_multiple_categories, null);

        // categories list
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
            // get QuestionCategory and add it to the headers
            QuestionCategory cat = testNonDB.getQuestionsCategories().get(i);
            listDataHeader.add(cat.getCategory());
            // child
            List<QuestionNonDB> questions = new ArrayList<>();
            for (QuestionNonDB question : cat.getQuestions()) {
                questions.add(question);
            }
            listDataChild.put(listDataHeader.get(i), questions); // Header, Child data
        }
        // set the adapter for displaying
        listAdapter = new ExpandableListAdapterCategories(context, listDataHeader, listDataChild,
                testNonDB, test, adapter, false);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return questionView;
    }

    public View multipleCategoriesAlreadyOpened() {
        View questionView = inflater.inflate(R.layout.questions_multiple_categories, null);

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
                testNonDB, test, adapter, true);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return questionView;
    }
}
