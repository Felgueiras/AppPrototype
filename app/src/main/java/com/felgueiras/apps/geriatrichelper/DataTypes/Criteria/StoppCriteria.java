package com.felgueiras.apps.geriatrichelper.DataTypes.Criteria;

import com.felgueiras.apps.geriatrichelper.Prescription.Stopp.Issue;

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
    public static ArrayList<StoppCriteria> getStoppCriteria() {
        ArrayList<StoppCriteria> stoppGeneral = new ArrayList<>();
        /*
          Stopp criteria.
         */
        // Urogenital
        StoppCriteria urogenitalSystem = new StoppCriteria("Urogenital System");
        PrescriptionStopp pr = new PrescriptionStopp("Bladder antimuscarinic drugs");
        Issue issue = new Issue("with dementia", "risk of increased confusion, agitation");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        pr.addIssue(new Issue("with chronic glaucoma", "risk of acute exacerbation of glaucoma"));
        pr.addIssue(new Issue("with chronic constipation", "risk of exacerbation of constipation"));
        pr.addIssue(new Issue("with chronic prostatism", "risk of urinary retention"));

        urogenitalSystem.addPrescription(pr);
        pr = new PrescriptionStopp("Alpha-blockers");
        issue = new Issue("in males with frequent incontinence i.e. one or more episodes\n" +
                "of incontinence daily",
                "risk of urinary frequency and worsening\n" +
                        "of incontinence");
        pr.addIssue(issue);
        pr.addIssue(new Issue("with long-term urinary catheter in situ i.e. more than 2 months",
                "drug not indicated"));
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
                "increased risk of recurrence");
        pr.addIssue(issue);
        issue = new Issue("without progestogen in patients with intact uterus",
                "risk of endometrial cancer");
        pr.addIssue(issue);
        endocrineSystem.addPrescription(pr);
        stoppGeneral.add(endocrineSystem);

        // Musculoskeletal
        StoppCriteria musculoskeletalSystem = new StoppCriteria("Musculoskeletal System");
        pr = new PrescriptionStopp("Non-steroidal anti-inflammatory drug (NSAID)");
        // 1
        issue = new Issue("with history of peptic ulcer disease or gastrointestinal bleeding, unless\n" +
                "with concurrent histamine H2 receptor antagonist, PPI or misoprostol",
                "risk of peptic ulcer relapse");
        pr.addIssue(issue);
        // 2
        issue = new Issue("with moderate-severe hypertension\n" +
                "(moderate: 160/100mmHg – 179/109mmHg; severe: ≥180/110mmHg)",
                "risk of exacerbation of hypertension");
        pr.addIssue(issue);
        // 3
        issue = new Issue("with heart failure",
                "(risk of exacerbation of heart failure");
        pr.addIssue(issue);
        // 4
        issue = new Issue("with warfarin",
                "(risk of gastrointestinal bleeding");
        pr.addIssue(issue);
        // 5
        issue = new Issue("with chronic renal failure - estimated GFR 20-50ml/min.",
                "risk of deterioration in renal function");
        pr.addIssue(issue);
        // 6
        issue = new Issue("Long-term use of NSAID (>3 months) for relief of mild joint pain in\n" +
                "osteoarthtitis",
                "(simple analgesics preferable and usually as effective for pain relief)");
        pr.addIssue(issue);
        // 7
        issue = new Issue("Long-term NSAID or colchicine for chronic treatment of gout where there is no contraindication to allopurinol",
                "(allopurinol first choice prophylactic drug in gout");
        pr.addIssue(issue);
        // 8
        issue = new Issue("Long-term corticosteroids (>3 months) as monotherapy for rheumatoid arthritis or osteoarthritis",
                "risk of major systemic corticosteroid side-effects");
        pr.addIssue(issue);
        musculoskeletalSystem.addPrescription(pr);
        stoppGeneral.add(musculoskeletalSystem);


        // Gastrointestinal system
        StoppCriteria gastrointestinalSystem = new StoppCriteria("Gastrointestinal System");
        // 1
        pr = new PrescriptionStopp("Diphenoxylate, loperamide or codeine phosphate");
        issue = new Issue("for treatment of severe infective gastroenteritis i.e. bloody " +
                "diarrhoea, high fever or severe systemic toxicity",
                "risk of exacerbation or protraction of infection");
        // add Issue to PrescriptionStopp
        pr.addIssue(issue);
        pr.addIssue(new Issue("for treatment of diarrhoea of unknown cause",
                "risk of delayed diagnosis, may exacerbate constipation with overflow diarrhoea, may precipitate toxic megacolon" +
                        " in inflammatory bowel disease, may delay recovery in unrecognised gastroenteritis"));
        gastrointestinalSystem.addPrescription(pr);
        // 2
        pr = new PrescriptionStopp("Prochlorperazine or metoclopramide");
        issue = new Issue("with Parkinsonism", "risk of exacerbating Parkinsonism");
        pr.addIssue(issue);
        gastrointestinalSystem.addPrescription(pr);
        // 3
        pr = new PrescriptionStopp("Proton pump inhibitor at treatment dose");
        issue = new Issue("for peptic ulcer disease at full therapeutic dosage for > 8 weeks",
                "earlier discontinuation or dose reduction for maintenance/prophylactic" +
                        " treatment of peptic ulcer disease, oesophagitis or GORD indicated");
        pr.addIssue(issue);
        gastrointestinalSystem.addPrescription(pr);
        // 4
        pr = new PrescriptionStopp("Anticholinergic antispasmodic drugs");
        issue = new Issue("with chronic constipation",
                "risk of exacerbation of constipation");
        pr.addIssue(issue);
        gastrointestinalSystem.addPrescription(pr);
        stoppGeneral.add(gastrointestinalSystem);

        // Respiratory System
        StoppCriteria respiratorySystem = new StoppCriteria("Respiratory System");
        // 1
        pr = new PrescriptionStopp("Theophylline");
        pr.addIssue(new Issue("as monotherapy for COPD",
                "safer, more effective alternative; risk of adverse effects due to narrow therapeutic index"));
        respiratorySystem.addPrescription(pr);
        // 2
        pr = new PrescriptionStopp("Systemic corticosteroids");
        issue = new Issue("instead of inhaled corticosteroids for maintenance therapy in moderate-severe COPD",
                "unnecessary exposure to long-term side-effects of systemic steroids");
        pr.addIssue(issue);
        respiratorySystem.addPrescription(pr);
        // 3
        pr = new PrescriptionStopp("Nebulised ipratropium");
        issue = new Issue("with glaucoma",
                "may exacerbate glaucoma");
        pr.addIssue(issue);
        respiratorySystem.addPrescription(pr);
        // 4
        pr = new PrescriptionStopp("First generation antihistamines");
        issue = new Issue("",
                "(sedative, may impair sensorium). Stop if PATIENT has fallen in past 3 months");
        pr.addIssue(issue);
        respiratorySystem.addPrescription(pr);
        stoppGeneral.add(respiratorySystem);

        // Respiratory System
        StoppCriteria centralNervousSystem = new StoppCriteria("Central Nervous System and Psychotropic Drugs");
        // 1
        pr = new PrescriptionStopp("Tricyclic antidepressants (TCAs)");
        pr.addIssue(new Issue("with dementia",
                "risk of worsening cognitive impairment"));
        pr.addIssue(new Issue("with glaucoma",
                "likely to exacerbate glaucoma"));
        pr.addIssue(new Issue("with cardiac conductive abnormalities",
                "pro-arrhythmic effects"));
        pr.addIssue(new Issue("with constipation",
                "likely to worsen constipation"));
        pr.addIssue(new Issue("with an opiate or calcium channel blocker",
                "risk of severe constipation"));
        pr.addIssue(new Issue("with prostatism or prior history of urinary retention",
                "risk of urinary retention"));
        centralNervousSystem.addPrescription(pr);
        // 2
        pr = new PrescriptionStopp("Benzodiazepines");
        pr.addIssue(new Issue("if long-term (i.e. > 1 month) and long-acting\n" +
                "e.g. chlordiazepoxide, fluazepam, nitrazepam, chlorazepate and benzodiazepines with long-acting metabolites e.g. diazepam",
                "risk of prolonged sedation, confusion, impaired balance, falls"));
        pr.addIssue(new Issue("if fallen in past 3 months",
                ""));
        centralNervousSystem.addPrescription(pr);
        // 3
        pr = new PrescriptionStopp("Neuroleptics");
        pr.addIssue(new Issue("long-term (i.e. > 1 month) as hypnotics", "risk of confusion, hypotension, extra-pyramidal side effects, falls"));
        pr.addIssue(new Issue("long-term ( > 1 month) in those with parkinsonism",
                "likely to worsen extra-pyramidal symptoms"));
        pr.addIssue(new Issue("if fallen in past 3 months",
                "may cause gait dyspraxia, Parkinsonism"));
        centralNervousSystem.addPrescription(pr);
        // 4
        pr = new PrescriptionStopp("Phenothiazines");
        pr.addIssue(new Issue("in patients with epilepsy",
                "may lower seizure threshold"));
        centralNervousSystem.addPrescription(pr);
        // 5
        pr = new PrescriptionStopp("Anticholinergics");
        pr.addIssue(new Issue("to treat extra-pyramidal side-effects of neuroleptic medications",
                "risk of anticholinergic toxicity"));
        centralNervousSystem.addPrescription(pr);
        // 6
        pr = new PrescriptionStopp("Selective serotonin re-uptake inhibitors (SSRI’s)");
        pr.addIssue(new Issue("with a history\n" +
                "of clinically significant hyponatraemia",
                "(<130mmol/l within the\n" +
                        "previous 2 months"));
        centralNervousSystem.addPrescription(pr);
        // 7
        pr = new PrescriptionStopp("First generation antihistamines");
        pr.addIssue(new Issue("if prolonged use (> 1 week)\n" +
                "i.e. diphenydramine, chlorpheniramine, cyclizine, promethazine",
                "risk of sedation and anti-cholinergic side effects"));
        centralNervousSystem.addPrescription(pr);
        // 8
        pr = new PrescriptionStopp("Opiates");
        pr.addIssue(new Issue("Use of long-term strong opiates as first line therapy for mildmoderate\n" +
                "pain",
                "(WHO analgesic ladder not observed"));
        pr.addIssue(new Issue("Regular opiates for more than 2 weeks in those with chronic\n" +
                "constipation without concurrent use of laxatives",
                "risk of severe constipation"));
        pr.addIssue(new Issue("long-term in those with dementia unless for palliative care or\n" +
                "management of chronic pain syndrome",
                "exacerbation of cognitive\n" +
                        "impairment"));
        centralNervousSystem.addPrescription(pr);
        stoppGeneral.add(centralNervousSystem);

        // Cardiovascular system
        StoppCriteria cardiovascularSystem = new StoppCriteria("Cardiovascular System");
        // 1
        pr = new PrescriptionStopp("Digoxin");
        issue = new Issue("a long-term dose > 125μg/day with impaired renal function —estimated GFR <50ml/min",
                "increased risk of toxicity");
        pr.addIssue(issue);
        cardiovascularSystem.addPrescription(pr);
        // 2
        pr = new PrescriptionStopp("Loop diuretic");
        issue = new Issue("for dependent ankle oedema only i.e. no clinical signs of heart failure",
                "no evidence of efficacy, compression hosiery usually more appropriate");
        pr.addIssue(issue);
        pr.addIssue(new Issue("as first-line monotherapy for hypertension",
                "safer, more effective alternatives available"));
        cardiovascularSystem.addPrescription(pr);
        // 3
        pr = new PrescriptionStopp("Thiazide diuretic");
        issue = new Issue("with a history of gout",
                "may exacerbate gout");
        pr.addIssue(issue);
        cardiovascularSystem.addPrescription(pr);
        // 4
        pr = new PrescriptionStopp("Beta-blocker");
        issue = new Issue("in combination with verapamil",
                "risk of symptomatic heart block");
        pr.addIssue(issue);
        cardiovascularSystem.addPrescription(pr);
        // 5
        pr = new PrescriptionStopp("Non-cardioselective beta-blocker");
        issue = new Issue("with Chronic Obstructive Pulmonary Disease (COPD)",
                "risk of bronchospasm");
        pr.addIssue(issue);
        cardiovascularSystem.addPrescription(pr);
        // 6
        pr = new PrescriptionStopp("Calcium channel blockers");
        pr.addIssue(new Issue("with chronic constipation",
                "may exacerbate constipation"));
        pr.addIssue(new Issue("Use of diltiazem or verapamil with NYHA Class III or IV heart failure",
                "may worsen heart failure"));
        pr.addIssue(new Issue("Vasodilator drugs known to cause hypotension in those with persistent " +
                "postural hypotension i.e. recurrent > 20mmHg drop in systolic blood pressure",
                "risk of syncope, falls). Stop if PATIENT has fallen in past 3 months"));
        cardiovascularSystem.addPrescription(pr);
        // 6
        pr = new PrescriptionStopp("Aspirin");
        pr.addIssue(new Issue("with a past history of peptic ulcer disease without histamine H2 receptor" +
                " antagonist or Proton Pump Inhibitor",
                "risk of bleeding"));
        pr.addIssue(new Issue("at dose > 150mg day",
                "increased bleeding risk, no evidence for\n" +
                        "increased efficacy"));
        pr.addIssue(new Issue("with no history of coronary, cerebral or peripheral" +
                " arterial symptoms or occlusive arterial event (",
                "not indicated"));
        pr.addIssue(new Issue("to treat dizziness not clearly attributable to cerebrovascular disease",
                "not indicited"));
        pr.addIssue(new Issue("with concurrent bleeding disorder",
                "high risk of bleeding"));
        // 7
        pr = new PrescriptionStopp("Warfarin");
        pr.addIssue(new Issue("for first, uncomplicated deep venous thrombosis for longer than 6 months duration",
                "no proven added benefit"));
        pr.addIssue(new Issue("for first uncomplicated pulmonary embolus for longer than 12 months duration",
                "no proven benefit"));
        pr.addIssue(new Issue("with concurrent bleeding disorder",
                "high risk of bleeding"));
        pr.addIssue(new Issue("Use of aspirin and warfarin in combination without gastroprotection",
                "except cimetidine because of interaction with warfarin, high risk of gastrointestinal bleeding"));
        cardiovascularSystem.addPrescription(pr);
        // 8
        pr = new PrescriptionStopp("Clopidogrel");
        pr.addIssue(new Issue("with concurrent bleeding disorder",
                "high risk of bleeding"));
        cardiovascularSystem.addPrescription(pr);
        // 9
        pr = new PrescriptionStopp("Dipyridamole");
        pr.addIssue(new Issue("as monotherapy for cardiovascular secondary prevention",
                "no evidence for efficacy"));
        pr.addIssue(new Issue("with concurrent bleeding disorder",
                "high risk of bleeding"));
        cardiovascularSystem.addPrescription(pr);


        stoppGeneral.add(cardiovascularSystem);
        return stoppGeneral;
    }

    /**
     * Get the issues associated to a given drug (start stopp criteria).
     *
     * @param drugSearchingFor name of the drug being searched for
     * @return Issues for a given drug
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
     * @param drugSearchingFor name of the drug being searched for
     * @return Stopp criteria for a drug
     */
    public static ArrayList<PrescriptionStopp> getStoppCriteriaPresciptionForDrug(String drugSearchingFor, ArrayList<StoppCriteria> criterions) {
        ArrayList<PrescriptionStopp> prescriptionStopps = new ArrayList<>();
        for (StoppCriteria criterion : criterions) {
            ArrayList<PrescriptionStopp> prescriptions = criterion.getPrescriptions();
            for (PrescriptionStopp pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    prescriptionStopps.add(pr);
                }
            }
        }
        return prescriptionStopps;
    }

    /**
     * Get all the drugs from StoppGeneral.
     *
     * @return all Stopp drugs
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
