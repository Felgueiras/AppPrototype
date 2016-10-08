package com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleQuestion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.DB.Choice;
import com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleTest.DisplaySingleTestFragment;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Create the layout of the Questions
 */
public class SingleQuestionListAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<Choice> choices;
    private static LayoutInflater inflater = null;
    private final DisplaySingleTestFragment context;


    public SingleQuestionListAdapter(DisplaySingleTestFragment viewTests, ArrayList<Choice> choices) {
        context = viewTests;
        this.choices = choices;
        inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class Holder {
        TextView choiceText;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.content_choices_for_question, null);
        Choice currentChoice = choices.get(position);
        Log.d("CHOICE",position+"");
        holder.choiceText = (TextView) rowView.findViewById(R.id.choiceText);
        holder.choiceText.setText(currentChoice.getName());
        return rowView;
    }

}