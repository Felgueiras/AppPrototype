package com.felgueiras.apps.geriatrichelper.Patients.AllPatients.FloatingSearch;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;

import com.felgueiras.apps.geriatrichelper.Firebase.RealtimeDatabase.PatientFirebase;
import com.felgueiras.apps.geriatrichelper.HelpersHandlers.StringHelper;
import com.felgueiras.apps.geriatrichelper.PatientsManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {


    private static List<PersonSuggestion> sPatientSuggestions;

    private static List<PersonSuggestion> createSuggestions(Context context) {
        ArrayList<PersonSuggestion> suggestions = new ArrayList<>();
        for (PatientFirebase patient : PatientsManagement.getInstance().getPatients(context)) {
            suggestions.add(new PersonSuggestion(patient));
        }
        return suggestions;
    }


    public interface OnFindSuggestionsListener {
        void onResults(List<PersonSuggestion> results);
    }

    /**
     * Get the 3 last patients to be searched.
     *
     * @param context
     * @param count
     * @return
     */
    public static List<PersonSuggestion> getHistory(Context context, int count) {
        sPatientSuggestions = createSuggestions(context);
        List<PersonSuggestion> suggestionList = new ArrayList<>();
        PersonSuggestion personSuggestion;
        for (int i = 0; i < sPatientSuggestions.size(); i++) {
            personSuggestion = sPatientSuggestions.get(i);
//            personSuggestion.setIsHistory(true);
//            suggestionList.add(personSuggestion);
            if (personSuggestion.getIsHistory()) {
                suggestionList.add(personSuggestion);
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
        for (PersonSuggestion personSuggestion : sPatientSuggestions) {
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

                DataHelper.resetSuggestionsHistory();
                List<PersonSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (PersonSuggestion suggestion : sPatientSuggestions) {
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
                Collections.sort(suggestionList, new Comparator<PersonSuggestion>() {
                    @Override
                    public int compare(PersonSuggestion lhs, PersonSuggestion rhs) {
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
                    listener.onResults((List<PersonSuggestion>) results.values);
                }
            }
        }.filter(query);

    }

}