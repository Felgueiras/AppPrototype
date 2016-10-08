package com.example.rafael.appprototype.NewSessionTab.DisplayTest.SingleQuestion;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.NonDB.ChoiceNonDB;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

/**
 * Created by rafael on 03-10-2016.
 */
public class SingleQuestionGridAdapter extends BaseAdapter {
    /**
     * Questions for a Test
     */
    private final ArrayList<ChoiceNonDB> choices;
    private static LayoutInflater inflater = null;
    private final Context context;

    public SingleQuestionGridAdapter(Context context, ArrayList<ChoiceNonDB> choices) {
        this.context = context;
        this.choices = choices;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // each view is a Fragment layout that holds a Fragment with a Recycler View inside
        View rowView = inflater.inflate(R.layout.content_choices_for_question, null);
        ChoiceNonDB currentChoice = choices.get(position);
        TextView choiceText = (TextView) rowView.findViewById(R.id.choiceText);
        choiceText.setText(currentChoice.getDescription());
        return rowView;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}