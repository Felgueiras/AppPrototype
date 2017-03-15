package com.example.rafael.appprototype.Prescription.Stopp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.rafael.appprototype.DataTypes.Criteria.PrescriptionStopp;
import com.example.rafael.appprototype.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rafael on 03-11-2016.
 */
public class ExpandableListAdapterStop extends BaseExpandableListAdapter {

    private Activity _context;
    /**
     * Headers.
     */
    private List<String> _listDataHeader;
    /**
     * Children.
     */
    private HashMap<String, List<PrescriptionStopp>> _listDataChild;

    public ExpandableListAdapterStop(Activity context, List<String> listDataHeader,
                                     HashMap<String, List<PrescriptionStopp>> listChildData, FragmentManager childFragmentManager) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        FragmentManager fragmentManager = childFragmentManager;
    }

    @Override
    public PrescriptionStopp getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View childView, ViewGroup parent) {

        PrescriptionStopp prescription = getChild(groupPosition, childPosition);

        if (childView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childView = infalInflater.inflate(R.layout.criteria_stopp, null);
        }

        // drug names
        TextView drugName = (TextView) childView.findViewById(R.id.drug_name);
        drugName.setText(prescription.getDrugName());

        // issues
        RecyclerView drugIssues = (RecyclerView) childView.findViewById(R.id.issues);


        LinearLayoutManager layoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        drugIssues.setLayoutManager(layoutManager);

        StoppDrugIssuesAdapter adapter = new StoppDrugIssuesAdapter(_context, prescription.getIssues());
        drugIssues.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(_context,
                layoutManager.getOrientation());
        drugIssues.addItemDecoration(dividerItemDecoration);


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