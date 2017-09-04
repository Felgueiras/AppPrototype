package com.felgueiras.apps.geriatrichelper.Patients.PatientProfile.PatientPrescriptions.AddPrescriptions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by felgueiras on 20/04/2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private final LayoutInflater mInflater;
    private final ArrayList<String> drugs;

    public AutoCompleteAdapter(Context context, ArrayList<String> drugsNames) {
        super(context, -1);
        mInflater = LayoutInflater.from(context);
        this.drugs = drugsNames;
    }

    /**
     * Get item to be displayed on the search suggestions.
     *
     * @param position postiion
     * @param convertView View
     * @param parent parent View
     * @return View
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView tv;
        if (convertView != null) {
            tv = (TextView) convertView;
        } else {
            tv = (TextView) mInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        tv.setText(getItem(position));

        return tv;
    }


    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                ArrayList<String> filteredList = new ArrayList<>();
                if (constraint != null) {
                    if (constraint.length() == 0) {
                        filteredList.addAll(drugs);

                    } else {
//                        addMoreButton.setVisibility(View.VISIBLE);

                        final String filterPattern = constraint.toString().toLowerCase().trim();
                        Log.d("Filter", filterPattern);

                        for (final String currentDrug : drugs) {
                            if (currentDrug.toLowerCase().trim().contains(filterPattern)) {
                                filteredList.add(currentDrug);
                                Log.d("FilterRes", currentDrug);
                            }
                        }
                    }
                }


                final FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(final CharSequence contraint, final FilterResults results) {
                clear();

                if (results != null && results.count > 0) {
                    for (String currentDrug : (ArrayList<String>) results.values) {
                        add(currentDrug);
                    }
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }


            @Override
            public CharSequence convertResultToString(final Object resultValue) {
                return resultValue == null ? "" : (String) resultValue;
            }
        };
        return myFilter;
    }
}
