package com.example.rafael.appprototype.Prescription.Beers.ExpandableListViewAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
import com.example.rafael.appprototype.Prescription.Beers.TherapeuticCategoryEntry;
import com.example.rafael.appprototype.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 03-11-2016.
 */
public class BeersCriteriaOrganSystem extends BaseExpandableListAdapter {

    private Context _context;
    /**
     * Headers.
     */
    private List<String> _listDataHeader;
    /**
     * Children.
     */
    private HashMap<String, List<TherapeuticCategoryEntry>> _listDataChild;

    public BeersCriteriaOrganSystem(Context context, List<String> listDataHeader,
                                    HashMap<String, List<TherapeuticCategoryEntry>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public TherapeuticCategoryEntry getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View childView, ViewGroup parent) {

        final TherapeuticCategoryEntry therapeuticCategory = getChild(groupPosition, childPosition);

        if (childView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = infalInflater.inflate(R.layout.criteria_beers, null);
        }


        // therapeutic category
        TextView category = (TextView) childView.findViewById(R.id.therapeutic_category);
        // drugs list
        TextView drugs = (TextView) childView.findViewById(R.id.drugs);
        // Reccomendation
        TextView recommendation = (TextView) childView.findViewById(R.id.recommendation);
        // Rationale
        TextView rationale = (TextView) childView.findViewById(R.id.rationale);
        // QE
        TextView qualityOfEvidence = (TextView) childView.findViewById(R.id.qualityOfEvidence);
        // SR
        TextView strengthOfRecommendation = (TextView) childView.findViewById(R.id.strengthOfRecommendation);

        // set the views
        category.setText(therapeuticCategory.getCategoryName());
        drugs.setText(therapeuticCategory.getDrugsAsList());
        RecommendationInfo drugInfo = therapeuticCategory.getInfo();
        recommendation.setText(_context.getString(R.string.beers_recommendation) + " " + drugInfo.getRecommendation());
        rationale.setText(_context.getString(R.string.beers_rationale) + " " + drugInfo.getRationale());
        qualityOfEvidence.setText(_context.getString(R.string.beers_quality_evidence) + " " + drugInfo.getQualityOfEvidence());
        strengthOfRecommendation.setText(_context.getString(R.string.beers_strength_recommendation) + " " + drugInfo.getStrengthOfRecommendation());


        return childView;
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