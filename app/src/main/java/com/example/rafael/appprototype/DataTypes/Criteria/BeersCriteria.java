package com.example.rafael.appprototype.DataTypes.Criteria;

import com.example.rafael.appprototype.DrugPrescription.Beers.TherapeuticCategoryEntry;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class BeersCriteria {

    String organSystem;
    ArrayList<TherapeuticCategoryEntry> entries;

    public BeersCriteria(String organSystem) {
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

    /**
     * Get all the drugs from Beers criterias.
     *
     * @return list of drugs from beers criteria
     */
    public static ArrayList<String> getAllDrugsBeers(ArrayList<BeersCriteria> beersData) {

        ArrayList<String> drugs = new ArrayList<>();
        for (BeersCriteria rec : beersData) {
            for (TherapeuticCategoryEntry entry : rec.getEntries()) {
                drugs.addAll(entry.getDrugs());
            }
        }
        return drugs;
    }

    /**
     * Add Beers data.
     */
    public static ArrayList<BeersCriteria> getBeersData() {
        ArrayList<BeersCriteria> beersData = new ArrayList<>();
        // Anticholinergics (excludes TCAs)
        BeersCriteria rec = new BeersCriteria("Anticholinergics (excludes TCAs)");
        TherapeuticCategoryEntry entry = new TherapeuticCategoryEntry("First-generation" +
                " antihistamines (as single agent or as part of combination products)");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Avoid", "Highly anticholinergic; clearance reduced with advanced age, and\n" +
                "tolerance develops when used as hypnotic; increased risk of confu-\n" +
                "sion, dry mouth, constipation, and other anticholinergic effects/\n" +
                "toxicity.");
        recommendationInfo.setQualityOfEvidence("High (Hydroxyzine and Promethazine), Moderate (All others)");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        ArrayList<String> drugs = new ArrayList<>();
        drugs.add("Brompheniramine");
        drugs.add("Carbinoxamine");
        drugs.add("Chlorpheniramine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Cardiovascular
        rec = new BeersCriteria("Cardiovascular");
        entry = new TherapeuticCategoryEntry("Alpha 1 blockers");
        recommendationInfo = new RecommendationInfo("Avoid use as an antihypertensive",
                "High risk of orthostatic hypotension; not recommended as routine\n" +
                        "treatment for hypertension; alternative agents have superior risk/\n" +
                        "benefit profile");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Doxazosin");
        drugs.add("Prazosin");
        drugs.add("Terazosin");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Endocrine
        rec = new BeersCriteria("Endocrine");
        entry = new TherapeuticCategoryEntry("Androgens");
        recommendationInfo = new RecommendationInfo("Avoid unless indicated for moderate to severe hypogonadism.",
                "Potential for cardiac problems and contraindicated in men with\n" +
                        "prostate cancer");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Methyltestosterone*");
        drugs.add("Testosterone");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);
        return beersData;

    }

    /**
     * Return the Beers criteria beersGeneral for a certain drug patientName.
     *
     * @param selectedDrug
     * @return
     */
    public static RecommendationInfo getBeersCriteriaInfoAboutDrug(String selectedDrug, ArrayList<BeersCriteria> beersData) {
        for (BeersCriteria rec : beersData) {
            for (TherapeuticCategoryEntry entry : rec.getEntries()) {
                ArrayList<String> drugs = entry.getDrugs();
                if (drugs.contains(selectedDrug)) {
                    return entry.getInfo();
                }
            }
        }
        return null;
    }
}
