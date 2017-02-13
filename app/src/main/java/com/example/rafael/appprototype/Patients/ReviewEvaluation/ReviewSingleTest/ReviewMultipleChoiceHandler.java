package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Question;
import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.Evaluations.DisplayTest.QuestionsListAdapter;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Create the layout of the Questions
 */
public class ReviewMultipleChoiceHandler extends BaseAdapter implements AdapterView.OnItemClickListener {
    /**
     * Questions for a Test
     */
    private final ArrayList<ChoiceNonDB> choices;
    private static LayoutInflater inflater = null;
    private final Question question;
    private final QuestionsListAdapter adapter;
    private int questionNumber;


    public ReviewMultipleChoiceHandler(Context context, ArrayList<ChoiceNonDB> choices, Question question, QuestionsListAdapter adapter, int questionNumber) {
        this.choices = choices;
        this.question = question;
        this.adapter = adapter;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.questionNumber = questionNumber;
    }

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // save selected Choice to DB
        question.setSelectedChoice(choices.get(position).getName());
        question.setAnswered(true);
        question.save();

        adapter.questionAnswered(questionNumber);

        // Color for selected and not selected
        int selected = Color.parseColor("#dddddd");
        int notSelected = Color.parseColor("#ffffff");

        // highlight the chosen item from the ListView

        for (int index = 0; index < parent.getChildCount(); ++index) {
            View nextChild = parent.getChildAt(index);
            nextChild.setBackgroundColor(notSelected);
        }
        view.setBackgroundColor(selected);
    }

    public class Holder {
        TextView choiceText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the View
        View rowView = inflater.inflate(R.layout.content_choices_for_question, null);
        // get the current Choice
        ChoiceNonDB currentChoice = choices.get(position);
        // create a Hilder
        Holder holder = new Holder();
        holder.choiceText = (TextView) rowView.findViewById(R.id.choiceText);
        // fill view with info
        String name = currentChoice.getName();
        String description = currentChoice.getDescription();
        if (name != null)
            holder.choiceText.setText(name + " - " + description);
        else
            holder.choiceText.setText(description);
        return rowView;
    }

}