package com.example.rafael.appprototype.DataTypes.Criteria;

import java.util.ArrayList;

/**
 * Information about a StartCriteria for a certain category.
 */
public class StartCriteria {

    String category;
    ArrayList<PrescriptionStart> prescriptions;

    public StartCriteria(String category) {
        this.category = category;
        this.prescriptions = new ArrayList<>();
    }

    public void addPrescription(PrescriptionStart prescriptionStart) {
        prescriptions.add(prescriptionStart);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<PrescriptionStart> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Create startGeneral criteria.
     */
    public static ArrayList<StartCriteria> getStartData() {
        ArrayList<StartCriteria> startGeneral = new ArrayList<>();
        // Encodrine
        StartCriteria criterion = new StartCriteria("Endocrine System");
        PrescriptionStart prescriptionStart = new PrescriptionStart("Metformin", "Metformin with type 2 diabetes +/- metabolic syndrome" +
                "(in the absence of renal impairment—estimated GFR <50ml/ min).");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
        // Musculoskeletal
        criterion = new StartCriteria("Musculoskeletal System");
        prescriptionStart = new PrescriptionStart("Disease-modifying anti-rheumatic drug (DMARD)",
                "with active moderate-severe rheumatoid disease lasting > 12 weeks");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
        //Gastrointestinal
        criterion = new StartCriteria("Gastrointestinal System");
        prescriptionStart = new PrescriptionStart("Proton Pump Inhibitor",
                "with severe gastro-oesophageal acid reflux disease or peptic stricture requiring dilatation.");
        criterion.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Fibre supplement",
                "for chronic, symptomatic diverticular disease with constipation.");
        criterion.addPrescription(prescriptionStart);
        startGeneral.add(criterion);
        return startGeneral;
    }

    /**
     * Get all the drugs from StoppGeneral.
     *
     * @return
     * @param general
     */
    public static ArrayList<String> getAllDrugsStart(ArrayList<StartCriteria> general) {
        ArrayList<String> drugs = new ArrayList<>();
        for (StartCriteria cr : general) {
            ArrayList<PrescriptionStart> prescriptions = cr.getPrescriptions();
            for (PrescriptionStart pr : prescriptions) {
                drugs.add(pr.getDrugName());
            }
        }
        return drugs;
    }

    /**
     * Get the StartCriteria for a given drug.
     *
     * @param drugSearchingFor
     * @return
     */
    public static PrescriptionStart getStartCriteriaPresciptionForDrug(String drugSearchingFor, ArrayList<StartCriteria> criterions) {
        for (StartCriteria criterion : criterions) {
            ArrayList<PrescriptionStart> prescriptions = criterion.getPrescriptions();
            for (PrescriptionStart pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    return pr;
                }
            }
        }
        return null;
    }
}
