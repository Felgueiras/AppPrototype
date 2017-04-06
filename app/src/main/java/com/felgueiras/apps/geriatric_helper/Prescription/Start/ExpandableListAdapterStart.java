package com.felgueiras.apps.geriatric_helper.Prescription.Start;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.felgueiras.apps.geriatric_helper.DataTypes.Criteria.PrescriptionStart;
import com.felgueiras.apps.geriatric_helper.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 03-11-2016.
 */
public class ExpandableListAdapterStart extends BaseExpandableListAdapter {

    private Context _context;
    /**
     * Headers.
     */
    private List<String> _listDataHeader;
    /**
     * Children.
     */
    private HashMap<String, List<PrescriptionStart>> _listDataChild;

    public ExpandableListAdapterStart(Context context, List<String> listDataHeader,
                                      HashMap<String, List<PrescriptionStart>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public PrescriptionStart getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View childView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).toString();

        if (childView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = infalInflater.inflate(R.layout.criteria_start, null);
        }

        TextView txtListChild = (TextView) childView.findViewById(R.id.criteria_info);

        txtListChild.setText(childText);
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