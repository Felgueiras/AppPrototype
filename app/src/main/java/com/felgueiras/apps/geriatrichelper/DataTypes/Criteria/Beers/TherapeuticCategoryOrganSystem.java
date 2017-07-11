package com.felgueiras.apps.geriatrichelper.DataTypes.Criteria.Beers;

import com.felgueiras.apps.geriatrichelper.Prescription.Beers.TherapeuticCategoryEntry;

import java.util.ArrayList;

/**
 * Created by felgueiras on 10/03/2017.
 */

public class TherapeuticCategoryOrganSystem {

    private final String therapeuticCategoryOrganSystem;
    private final ArrayList<TherapeuticCategoryEntry> entries;

    public TherapeuticCategoryOrganSystem(String therapeuticCategoryOrganSystem) {
        this.therapeuticCategoryOrganSystem = therapeuticCategoryOrganSystem;
        entries = new ArrayList<>();
    }

    public void addEntry(TherapeuticCategoryEntry entry) {
        entries.add(entry);
    }

    public String getTherapeuticCategoryOrganSystem() {
        return therapeuticCategoryOrganSystem;
    }

    public ArrayList<TherapeuticCategoryEntry> getEntries() {
        return entries;
    }
}
