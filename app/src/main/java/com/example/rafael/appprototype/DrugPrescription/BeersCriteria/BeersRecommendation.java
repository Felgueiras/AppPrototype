package com.example.rafael.appprototype.DrugPrescription.BeersCriteria;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class BeersRecommendation {

    String organSystem;
    ArrayList<TherapeuticCategoryEntry> entries;

    public BeersRecommendation(String organSystem) {
        this.organSystem = organSystem;
        entries = new ArrayList<>();
    }

    public void addEntry(TherapeuticCategoryEntry entry) {
        entries.add(entry);
    }

    public String getOrganSystem() {
        return organSystem;
    }

    public ArrayList<TherapeuticCategoryEntry> getEntries() {
        return entries;
    }
}
