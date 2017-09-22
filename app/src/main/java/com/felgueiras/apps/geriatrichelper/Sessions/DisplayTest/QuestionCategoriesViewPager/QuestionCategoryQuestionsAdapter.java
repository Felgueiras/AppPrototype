package com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.QuestionCategoriesViewPager;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.GeriatricScaleNonDB;
import com.felgueiras.apps.geriatrichelper.DataTypes.NonDB.QuestionCategory;
import com.felgueiras.apps.geriatrichelper.Sessions.DisplayTest.QuestionsListAdapter;
import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.GeriatricScaleFirebase;
import com.felgueiras.apps.geriatrichelper.R;

/**
 * Created by felgueiras on 27/03/2017.
 */
public class QuestionCategoryQuestionsAdapter extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String SCALE_DB = "SCALE_DB";
    public static final String SCALE_NON_DB = "SCALE_NON_DB";
    public static final String ADAPTER = "ADAPTER";
    private GeriatricScaleNonDB scaleNonDB;
    private GeriatricScaleFirebase scaleDB;
    private int categoryIndex;
    private TextView categoryTextView;
    private TextView categoryNumber;
    private TextView instructions;
    private ImageButton previous;
    private ImageButton next;
    private RecyclerView questionsRecyclerView;
    QuestionsListAdapter adapter;
    private TextView notes;

    public static QuestionCategoryQuestionsAdapter newInstance(int categoryIndex,
                                                               GeriatricScaleNonDB scaleNonDB,
                                                               GeriatricScaleFirebase scaleDB,
                                                               QuestionsListAdapter adapter, ViewPager viewPager) {
        QuestionCategoryQuestionsAdapter f = new QuestionCategoryQuestionsAdapter();
        Bundle bdl = new Bundle(1);
        bdl.putInt(EXTRA_MESSAGE, categoryIndex);
        bdl.putSerializable(SCALE_DB, scaleDB);
        bdl.putSerializable(SCALE_NON_DB, scaleNonDB);
        bdl.putSerializable(ADAPTER, adapter);
        bdl.putSerializable(ADAPTER, adapter);
        bdl.putSerializable(ADAPTER, adapter);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        categoryIndex = getArguments().getInt(EXTRA_MESSAGE);
        scaleNonDB = (GeriatricScaleNonDB) getArguments().getSerializable(SCALE_NON_DB);
        scaleDB = (GeriatricScaleFirebase) getArguments().getSerializable(SCALE_DB);
        adapter = (QuestionsListAdapter) getArguments().getSerializable(ADAPTER);

        View questionView = inflater.inflate(R.layout.questions_multiple_categories_individual, null);


        /*
          Access views.
         */
        categoryTextView = questionView.findViewById(R.id.category);
        categoryNumber = questionView.findViewById(R.id.categoryNumber);
        instructions = questionView.findViewById(R.id.instructions);
        notes = questionView.findViewById(R.id.notes);
        // left and right arrows - switch category
        previous = questionView.findViewById(R.id.previousCategory);
        next = questionView.findViewById(R.id.nextCategory);
        // fill the RecyclerView
        questionsRecyclerView = questionView.findViewById(R.id.questions);


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getViewPagerAux().setCurrentItem(--categoryIndex, true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getViewPagerAux().setCurrentItem(++categoryIndex, true);
            }
        });

        fillView();

        return questionView;
    }

    private void fillView() {
        Log.d("Category", "refreshing");
//        recyclerAdapter.notifyDataSetChanged();
        QuestionCategory currentCategory = scaleNonDB.getQuestionsCategories().get(categoryIndex);
        categoryTextView.setText(currentCategory.getName());
        categoryNumber.setText((categoryIndex + 1) + "/" + scaleNonDB.getQuestionsCategories().size());
        String categoryInfo = currentCategory.getDescription();
        // display category info
        if (categoryInfo != null) {
            instructions.setText(categoryInfo);
        }
        if (currentCategory.getNotes() != null) {
            notes.setVisibility(View.VISIBLE);
            notes.setText(currentCategory.getNotes());
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        questionsRecyclerView.setLayoutManager(mLayoutManager);
        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final CategoryDisplayQuestions recyclerAdapter = new CategoryDisplayQuestions(getActivity(),
                scaleNonDB, categoryIndex, scaleDB, adapter, categoryTextView,
                questionsRecyclerView);
        questionsRecyclerView.setAdapter(recyclerAdapter);

        /*
          Hide previous/next icons
         */
        previous.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        if (categoryIndex == 0) {
            previous.setVisibility(View.INVISIBLE);
        }
        if (categoryIndex == scaleNonDB.getQuestionsCategories().size() - 1) {
            next.setVisibility(View.INVISIBLE);
        }
    }
}