package com.example.rafael.appprototype.Evaluations.DisplayTest;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.GeriatricScale;
import com.example.rafael.appprototype.DataTypes.NonDB.GeriatricScaleNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.QuestionCategory;
import com.example.rafael.appprototype.Evaluations.DisplayTest.SingleQuestion.CategoryDisplayQuestions;
import com.example.rafael.appprototype.R;

/**
 * Created by rafael on 08-01-2017.
 */
public class QuestionMultipleCategoriesIndividualCategory {


    private final LayoutInflater inflater;
    private final GeriatricScaleNonDB scaleNonDB;
    private final Activity context;
    GeriatricScale scaleDB;
    private final QuestionsListAdapter adapter;
    private int categoryIndex;
    private CategoryDisplayQuestions recyclerAdapter;
    private RecyclerView questionsRecyclerView;
    private TextView categoryTextView;
    private TextView instructions;

    public QuestionMultipleCategoriesIndividualCategory(LayoutInflater inflater, GeriatricScaleNonDB testNonDB, Activity context, GeriatricScale test, QuestionsListAdapter adapter) {
        this.inflater = inflater;
        this.scaleNonDB = testNonDB;
        this.context = context;
        this.scaleDB = test;
        this.adapter = adapter;
    }

    /**
     * Multiple question categories.
     *
     * @return
     */
    public View getView() {
        View questionView = inflater.inflate(R.layout.questions_multiple_categories_individual, null);

        /**
         * Access views.
         */
        categoryTextView = (TextView) questionView.findViewById(R.id.category);
        instructions = (TextView) questionView.findViewById(R.id.instructions);
        // left and right arrows - switch category
        ImageButton previous = (ImageButton) questionView.findViewById(R.id.previousCategory);
        ImageButton next = (ImageButton) questionView.findViewById(R.id.nextCategory);
        // fill the RecyclerView
        questionsRecyclerView = (RecyclerView) questionView.findViewById(R.id.questions);


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryIndex > 0) {
                    categoryIndex--;
                    refreshRecycler();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryIndex < scaleNonDB.getQuestionsCategories().size()-1) {
                    categoryIndex++;
                    refreshRecycler();
                }
            }
        });


        refreshRecycler();

        return questionView;
    }

    private void refreshRecycler() {
//        recyclerAdapter.notifyDataSetChanged();
        QuestionCategory currentCategory = scaleNonDB.getQuestionsCategories().get(categoryIndex);
        categoryTextView.setText(currentCategory.getName());
        String categoryInfo = currentCategory.getDescription();
        // display category info
        if (categoryInfo != null) {
            instructions.setText(categoryInfo);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        questionsRecyclerView.setLayoutManager(mLayoutManager);
        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final CategoryDisplayQuestions recyclerAdapter = new CategoryDisplayQuestions(context,
                scaleNonDB, categoryIndex, scaleDB, adapter);
        questionsRecyclerView.setAdapter(recyclerAdapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        questionsRecyclerView.addItemDecoration(dividerItemDecoration);
    }
}
