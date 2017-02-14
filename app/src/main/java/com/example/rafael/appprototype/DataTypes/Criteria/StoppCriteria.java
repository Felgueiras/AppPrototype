package com.example.rafael.appprototype.DataTypes.Criteria;

import com.example.rafael.appprototype.Prescription.Stopp.Issue;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */

public class StoppCriteria {

    String category;
    ArrayList<PrescriptionStopp> prescriptions;

    public StoppCriteria(String category) {
        this.category = category;
        prescriptions = new ArrayList<>();

    }

    public void addPrescription(PrescriptionStopp pr) {
        prescriptions.add(pr);
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<PrescriptionStopp> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Add Stopp and Beers data.
     */
    public static ArrayList<StoppCriteria> getStoppData() {
        ArrayList<StoppCriteria> stoppGeneral = new ArrayList<>();
        /**
         * Stopp criteria.
         */
        // Urogenital
        StoppCriteria urogenitalSystem = new StoppCriteria("Urogenital System");
        PrescriptionStopp pr = new PrescriptionStopp("Bladder antimuscarinic drugs");
        Issue issue = new Issue("with dementia", "risk of increased confusion, agitation");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        pr.addIssue(new Issue("with chronic glaucoma", "risk of acute exacerbation of glaucoma"));
        // add PrescriptionStopp to StoppCriteria
        urogenitalSystem.addPrescription(pr);
        pr = new PrescriptionStopp("Alpha-blockers");
        issue = new Issue("in males with frequent incontinence i.e. one or more episodes\n" +
                "of incontinence daily",
                "risk of urinary frequency and worsening\n" +
                        "of incontinence");
        pr.addIssue(issue);
        urogenitalSystem.addPrescription(pr);
        stoppGeneral.add(urogenitalSystem);
        // Endocrine
        StoppCriteria endocrineSystem = new StoppCriteria("Endocrine System");
        pr = new PrescriptionStopp("Glibenclamide or chlorpropamide");
        issue = new Issue("with type 2 diabetes\n" +
                "mellitus", "risk of prolonged hypoglycaemia");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        // add PrescriptionStopp to StoppCriteria
        endocrineSystem.addPrescription(pr);
        pr = new PrescriptionStopp("Oestrogens");
        issue = new Issue("with a history of breast cancer or venous thromboembolism",
                "ncreased risk of recurrence");
        pr.addIssue(issue);
        endocrineSystem.addPrescription(pr);
        stoppGeneral.add(endocrineSystem);
        // Musculoskeletal
        StoppCriteria musculoskeletalSystem = new StoppCriteria("Musculoskeletal System");
        pr = new PrescriptionStopp("Non-steroidal anti-inflammatory drug (NSAID)");
        issue = new Issue("with history of peptic ulcer disease or gastrointestinal bleeding, unless\n" +
                "with concurrent histamine H2 receptor antagonist, PPI or misoprostol",
                "risk of peptic ulcer relapse");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        musculoskeletalSystem.addPrescription(pr);
        stoppGeneral.add(musculoskeletalSystem);
        // Gastrointestinal
        StoppCriteria gastrointestinalSystem = new StoppCriteria("Gastrointestinal System");
        pr = new PrescriptionStopp("Diphenoxylate, loperamide or codeine phosphate");
        issue = new Issue("for treatment of severe infective gastroenteritis i.e. bloody " +
                "diarrhoea, high fever or severe systemic toxicity",
                "risk of exacerbation or protraction of infection");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        gastrointestinalSystem.addPrescription(pr);
        stoppGeneral.add(gastrointestinalSystem);
        return stoppGeneral;
    }

    /**
     * Get the issues associated to a given drug (start stopp criteria).
     *
     * @param drugSearchingFor
     * @return
     */
    public ArrayList<Issue> getIssuesForGivenDrug(String drugSearchingFor, ArrayList<StoppCriteria> criterions) {
        for (StoppCriteria criterion : criterions) {
            ArrayList<PrescriptionStopp> prescriptions = criterion.getPrescriptions();
            for (PrescriptionStopp pr : prescriptions) {
                String drugName = pr.getDrugName();
                //system.out.println(drugName);
                if (drugName.equals(drugSearchingFor)) {
                    return pr.getIssues();
                }
            }
        }
        return null;
    }

    /**
     * Get the StoppCriteria for a given drug.
     *
     * @param drugSearchingFor
     * @return
     */
    public static PrescriptionStopp getStoppCriteriaPresciptionForDrug(String drugSearchingFor, ArrayList<StoppCriteria> criterions) {
        for (StoppCriteria criterion : criterions) {
            ArrayList<PrescriptionStopp> prescriptions = criterion.getPrescriptions();
            for (PrescriptionStopp pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    return pr;
                }
            }
        }
        return null;
    }

    /**
     * Get all the drugs from StoppGeneral.
     *
     * @return
     */
    public static ArrayList<String> getAllDrugsStopp(ArrayList<StoppCriteria> general) {
        ArrayList<String> drugs = new ArrayList<>();
        for (StoppCriteria cr : general) {
            ArrayList<PrescriptionStopp> prescriptions = cr.getPrescriptions();
            for (PrescriptionStopp pr : prescriptions) {
                drugs.add(pr.getDrugName());
            }
        }
        return drugs;
    }



}
