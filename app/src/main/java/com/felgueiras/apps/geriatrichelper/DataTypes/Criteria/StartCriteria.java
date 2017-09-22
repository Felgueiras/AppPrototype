package com.felgueiras.apps.geriatrichelper.DataTypes.Criteria;

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
    public static ArrayList<StartCriteria> getStartCriteria() {
        ArrayList<StartCriteria> startGeneral = new ArrayList<>();
        // Encodrine
        StartCriteria criteria = new StartCriteria("Endocrine System");
        // 1
        PrescriptionStart prescriptionStart = new PrescriptionStart("Metformin", "Metformin with type 2 diabetes +/- metabolic syndrome" +
                "(in the absence of renal impairment—estimated GFR <50ml/ min).");
        criteria.addPrescription(prescriptionStart);
        // 2
        prescriptionStart = new PrescriptionStart("ACE inhibitor or Angiotensin Receptor Blocker",
                "in diabetes with nephropathy i.e. overt urinalysis" +
                        " proteinuria or micoralbuminuria (>30mg/24 hours)" +
                        " +/- serum biochemical renal impairment—estimated GFR <50ml/min.");
        criteria.addPrescription(prescriptionStart);
        // 3
        prescriptionStart = new PrescriptionStart("Antiplatelet therapy",
                "in diabetes mellitus if one or more co-existing major cardiovascular " +
                        "risk factor present (hypertension, hypercholesterolaemia, smoking history).");
        criteria.addPrescription(prescriptionStart);
        // 4
        prescriptionStart = new PrescriptionStart("Statin",
                "Statin therapy in diabetes mellitus if one or more co-existing major cardiovascular risk factor present");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);


        // Musculoskeletal
        criteria = new StartCriteria("Musculoskeletal System");
        prescriptionStart = new PrescriptionStart("Disease-modifying anti-rheumatic drug (DMARD)",
                "with active moderate-severe rheumatoid disease lasting > 12 weeks");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Bisphosphonates",
                "in patients taking maintenance oral corticosteroid therapy.");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Calcium and Vitamin D",
                "supplement in patients with known osteoporosis (radiological" +
                        " evidence or previous fragility fracture or acquired dorsal kyphosis).");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);

        // Cardiovascular System
        criteria = new StartCriteria("Cardiovascular System");
        prescriptionStart = new PrescriptionStart("Warfarin",
                "in the presence of chronic atrial fibrillation");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Aspirin",
                "in the presence of chronic atrial fibrillation, where warfarin is contraindicated, but not aspirin");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Aspirin or clopidogrel",
                "with a documented history of atherosclerotic coronary, cerebral" +
                        " or peripheral vascular disease in patients with sinus rhythm.");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Antihypertensive",
                "Antihypertensive therapy where systolic blood pressure consistently >160 mmHg.");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Statin",
                "Statin therapy with a documented history of coronary, cerebral or peripheral vascular disease, where the PATIENT’s functional status remains" +
                        " independent for activities of daily living and life expectancy is > 5 years");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Angiotensin Converting Enzyme (ACE) inhibitor",
                "with chronic heart failure");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("ACE inhibitor",
                "following acute myocardial infarction");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Beta-blocker",
                "with chronic stable angina");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);

        // Central Nervous System and Psychotropic Drugs
        criteria = new StartCriteria("Central Nervous System and Psychotropic Drugs");
        prescriptionStart = new PrescriptionStart("L-DOPA",
                "in idiopathic Parkinson’s disease with definite functional impairment and resultant disability.");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Antidepressant drug",
                "in the presence of moderate-severe depressive symptoms lasting at least three months");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);

        // Respiratory System
        criteria = new StartCriteria("Respiratory System");
        prescriptionStart = new PrescriptionStart("Regular inhaled beta 2 agonist or anticholinergic (antimuscarinic)",
                "agent for mild to moderate asthma or COPD");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);


        //Gastrointestinal
        criteria = new StartCriteria("Gastrointestinal System");
        prescriptionStart = new PrescriptionStart("Proton Pump Inhibitor",
                "with severe gastro-oesophageal acid reflux disease or peptic stricture requiring dilatation.");
        criteria.addPrescription(prescriptionStart);
        prescriptionStart = new PrescriptionStart("Fibre supplement",
                "for chronic, symptomatic diverticular disease with constipation.");
        criteria.addPrescription(prescriptionStart);
        startGeneral.add(criteria);
        return startGeneral;
    }

    /**
     * Get all the drugs from StoppGeneral.
     *
     * @param general
     * @return
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
    public static ArrayList<PrescriptionStart> getStartCriteriaPresciptionForDrug(String drugSearchingFor, ArrayList<StartCriteria> criterions) {
        ArrayList<PrescriptionStart> prescriptionStarts = new ArrayList<>();

        for (StartCriteria criterion : criterions) {
            ArrayList<PrescriptionStart> prescriptions = criterion.getPrescriptions();
            for (PrescriptionStart pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    prescriptionStarts.add(pr);
                }
            }
        }
        return prescriptionStarts;
    }
}
