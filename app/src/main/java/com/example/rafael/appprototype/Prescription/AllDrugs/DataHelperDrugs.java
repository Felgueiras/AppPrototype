package com.example.rafael.appprototype.Prescription.AllDrugs;

import android.content.Context;
import android.widget.Filter;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.HelpersHandlers.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelperDrugs {


    private static List<DrugSuggestion> drugSuggestions = createSuggestions();

    private static List<DrugSuggestion> createSuggestions() {
        ArrayList<DrugSuggestion> suggestions = new ArrayList<>();
        for (String drug : Constants.allDrugs) {
            suggestions.add(new DrugSuggestion(drug));
        }
        return suggestions;
    }


    public interface OnFindSuggestionsListener {
        void onResults(List<DrugSuggestion> results);
    }

    /**
     * Get the 3 last patients to be searched.
     *
     * @param context
     * @param count
     * @return
     */
    public static List<DrugSuggestion> getHistory(Context context, int count) {

        // TODo get those with history
        List<DrugSuggestion> suggestionList = new ArrayList<>();
        DrugSuggestion drugSuggestion;
        for (int i = 0; i < drugSuggestions.size(); i++) {
            drugSuggestion = drugSuggestions.get(i);
            if (drugSuggestion.getIsHistory()) {
                suggestionList.add(drugSuggestion);
            }
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    /**
     * reset the suggestion history.
     */
    public static void resetSuggestionsHistory() {
        for (DrugSuggestion personSuggestion : drugSuggestions) {
            personSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelperDrugs.resetSuggestionsHistory();
                List<DrugSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (DrugSuggestion suggestion : drugSuggestions) {
                        // remove accents
                        String first = StringHelper.removeAccents(suggestion.getBody().toUpperCase());
                        String second = StringHelper.removeAccents(constraint.toString().toUpperCase());
                        if (first.startsWith(second)) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<DrugSuggestion>() {
                    @Override
                    public int compare(DrugSuggestion lhs, DrugSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<DrugSuggestion>) results.values);
                }
            }
        }.filter(query);

    }

}