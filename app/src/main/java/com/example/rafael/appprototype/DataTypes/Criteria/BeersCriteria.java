package com.example.rafael.appprototype.DataTypes.Criteria;

import com.example.rafael.appprototype.Prescription.Beers.TherapeuticCategoryEntry;

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
        // entry 1
        TherapeuticCategoryEntry entry = new TherapeuticCategoryEntry("First-generation" +
                " antihistamines (as single agent or as part of combination products)");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Avoid", "Highly anticholinergic; clearance reduced with advanced age, and\n" +
                "tolerance develops when used as hypnotic; increased risk of confu-\n" +
                "sion, dry mouth, constipation, and other anticholinergic effects/\n" +
                "toxicity.Use of diphenhydramine in special situations such as acute treatment of severe allergic reaction may be appropriate.");
        recommendationInfo.setQualityOfEvidence("High (Hydroxyzine and Promethazine), Moderate (All others)");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        ArrayList<String> drugs = new ArrayList<>();
        drugs.add("Brompheniramine");
        drugs.add("Carbinoxamine");
        drugs.add("Chlorpheniramine");
        drugs.add("Clemastine");
        drugs.add("Cyproheptadine");
        drugs.add("Dexbrompheniramine");
        drugs.add("Dexchlorpheniramine");
        drugs.add("Diphenhydramine (oral)");
        drugs.add("Doxylamine");
        drugs.add("Hydroxyzine");
        drugs.add("Promethazine");
        drugs.add("Triprolidine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 2
        entry = new TherapeuticCategoryEntry("Antiparkinson agents");
        recommendationInfo = new RecommendationInfo("Avoid", "" +
                "Not recommended for prevention of extrapyramidal symptoms with antipsychotics;" +
                " more effective agents available for treatment of Parkinson disease.");
        recommendationInfo.setQualityOfEvidence("Moderate;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Benztropine (oral)");
        drugs.add("Trihexyphenidyl");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Antispasmodics");
        recommendationInfo = new RecommendationInfo("Avoid except in short-term palliative care to decrease oral secretions.",
                "Highly anticholinergic, uncertain effectiveness.");
        recommendationInfo.setQualityOfEvidence("Moderate;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Belladonna alkaloids");
        drugs.add("Clidinium-chlordiazepoxide");
        drugs.add("Dicyclomine");
        drugs.add("Hyoscyamine");
        drugs.add("Propantheline");
        drugs.add("Scopolamine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Antithrombotics
        rec = new BeersCriteria("Antithrombotics");
        // entry 1
        entry = new TherapeuticCategoryEntry("Dipyridamole, oral short-acting* (does not apply to the extended-release combination with aspirin)");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "May cause orthostatic hypotension; more effective" +
                        " alternatives available; IV form acceptable for use in cardiac stress testing.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Dipyridamole, oral short-acting* (does not apply to the extended-release combination with aspirin)");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 2
        entry = new TherapeuticCategoryEntry("Ticlopidine*");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Safer, effective alternatives available.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Ticlopidine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Antithrombotics
        rec = new BeersCriteria("Anti-infective");
        // entry 1
        entry = new TherapeuticCategoryEntry("Nitrofurantoin");
        recommendationInfo = new RecommendationInfo("Avoid for long-term suppression; avoid in patients with CrCl <60 mL/min..",
                "Potential for pulmonary toxicity; safer alternatives available; lack" +
                        " of efficacy in patients with CrCl <60 mL/min due to inadequate drug concentration in the urine.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Nitrofurantoin");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Cardiovascular
        rec = new BeersCriteria("Cardiovascular");
        // entry 1
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
        // entry 2
        entry = new TherapeuticCategoryEntry("Alpha agonists");
        recommendationInfo = new RecommendationInfo("Avoid clonidine as a first-line antihypertensive. Avoid others as listed.",
                "High risk of adverse CNS effects; may cause bradycardia" +
                        " and orthostatic hypotension; not recommended as routine treatment for hypertension.");
        recommendationInfo.setQualityOfEvidence("Low;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Clonidine");
        drugs.add("Guanabenz*");
        drugs.add("Guanfacine*");
        drugs.add("Methyldopa*");
        drugs.add("Reserpine (>0.1 mg/day)*");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Antiarrhythmic drugs (Class Ia, Ic, III)");
        recommendationInfo = new RecommendationInfo("Avoid antiarrhythmic drugs as first-line treatment of atrial fibrillation.",
                "Data suggest that rate control yields better balance of benefits and harms than rhythm control for most older adults.\n" +
                        "Amiodarone is associated with multiple toxicities, including thyroid disease, pulmonary disorders, and QT interval prolongation");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Amiodarone");
        drugs.add("Dofetilide*");
        drugs.add("Dronedarone*");
        drugs.add("Flecainide*");
        drugs.add("Ibutilide");
        drugs.add("Procainamide");
        drugs.add("Propafenone");
        drugs.add("Quinidine");
        drugs.add("Sotalol");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Disopyramide*");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Disopyramide is a potent negative inotrope and therefore may" +
                        " induce heart failure in older adults; strongly anticholinergic; other antiarrhythmic drugs preferred.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Disopyramide");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Dronedarone*");
        recommendationInfo = new RecommendationInfo("Avoid in patients with permanent atrial fibrillation or heart failure.",
                "Worse outcomes have been reported in patients taking dronedarone who have permanent atrial fibrillation or " +
                        "heart failure. In general, rate control is preferred over rhythm control for atrial fibrillation.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Dronedarone");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 6
        entry = new TherapeuticCategoryEntry("Digoxin >0.125 mg/day");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "In heart failure, higher dosages associated with no additional benefit and may increase risk of toxicity; decreased renal clearance may increase risk of toxicity.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Digoxin >0.125 mg/day");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Nifedipine, immediate release*");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Potential for hypotension; risk of precipitating myocardial ischemia.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Nifedipine, immediate release*");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 8
        entry = new TherapeuticCategoryEntry("Spironolactone >25 mg/day");
        recommendationInfo = new RecommendationInfo("Avoid in patients with heart failure or with a CrCl <30 mL/min.",
                "In heart failure, the risk of hyperkalemia is higher in older adults if taking >25 mg/day.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Spironolactone >25 mg/day");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Endocrine
        rec = new BeersCriteria("Central Nervous System");
        // entry 1
        entry = new TherapeuticCategoryEntry("Tertiary TCAs, alone or in combination:");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Highly anticholinergic, sedating, and cause orthostatic hypotension;\n" +
                        "the safety profile of low-dose doxepin (≤6 mg/day) is comparable\n" +
                        "to that of placebo.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Amitriptyline");
        drugs.add("Chlordiazepoxideamitriptyline");
        drugs.add("Clomipramine");
        drugs.add("Doxepin >6 mg/day");
        drugs.add("Imipramine");
        drugs.add("Perphenazine-amitriptyline");
        drugs.add("Trimipramine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 2
        entry = new TherapeuticCategoryEntry("Antipsychotics, first- (conventional) and second-\n" +
                "(atypical) generation (see online for full list)");
        recommendationInfo = new RecommendationInfo("Avoid use for behavioral problems of dementia unless\n" +
                "non-pharmacologic options have failed and patient is\n" +
                "threat to self or others",
                "Increased risk of cerebrovascular accident (stroke) and mortality in\n" +
                        "persons with dementia.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Antipsychotics, first- (conventional) and second-\n" +
                "(atypical) generation (see online for full list)");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Thioridazine\n" +
                "Mesoridazine");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Highly anticholinergic and greater risk of QT-interval prolongation");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Thioridazine\n" +
                "Mesoridazine");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Benzodiazepines");
        recommendationInfo = new RecommendationInfo("Avoid benzodiazepines (any type) for treatment of insomnia,\n" +
                "agitation, or delirium.",
                "Older adults have increased sensitivity to benzodiazepines and\n" +
                        "decreased metabolism of long-acting agents. In general, all benzodiazepines\n" +
                        "increase risk of cognitive impairment, delirium, falls,\n" +
                        "fractures, and motor vehicle accidents in older adults.\n" +
                        "May be appropriate for seizure disorders, rapid eye movement\n" +
                        "sleep disorders, benzodiazepine withdrawal, ethanol withdrawal,\n" +
                        "severe generalized anxiety disorder, periprocedural anesthesia,\n" +
                        "end-of-life care.");
        recommendationInfo.setQualityOfEvidence("High;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Alprazolam");
        drugs.add("Estazolam");
        drugs.add("Lorazepam");
        drugs.add("Oxazepam");
        drugs.add("Temazepam");
        drugs.add("Chlorazepate");
        drugs.add("Chlordiazepoxide");
        drugs.add("Chlordiazepoxide-amitriptyline");
        drugs.add("Clidinium-chlordiazepoxide");
        drugs.add("Clonazepam");
        drugs.add("Diazepam");
        drugs.add("Flurazepam");
        drugs.add("Quazepam");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Chloral hydrate");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Tolerance occurs within 10 days and risk outweighs the benefits in\n" +
                        "light of overdose with doses only 3 times the recommended dose.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Chloral hydrate");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 6
        entry = new TherapeuticCategoryEntry("Meprobamate");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "High rate of physical dependence; very sedating.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Meprobamate");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Nonbenzodiazepine\n" +
                "hypnotics");
        recommendationInfo = new RecommendationInfo("Avoid chronic use (>90 days)",
                "Benzodiazepine-receptor agonists that have adverse events similar\n" +
                        "to those of benzodiazepines in older adults (e.g., delirium, falls,\n" +
                        "fractures); minimal improvement in sleep latency and duration.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Eszopiclone");
        drugs.add("Zolpidem");
        drugs.add("Zaleplon");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 8
        entry = new TherapeuticCategoryEntry("Ergot mesylates*\n" +
                "Isoxsuprine*");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Lack of efficacy.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Ergot mesylates*\n" +
                "Isoxsuprine*");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);

        // Endocrine
        rec = new BeersCriteria("Endocrine");
        // entry 1
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
        // entry 2
        entry = new TherapeuticCategoryEntry("Desiccated thyroid");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Concerns about cardiac effects; safer alternatives available.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Desiccated thyroid");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Estrogens with or without progestins");
        recommendationInfo = new RecommendationInfo("Avoid oral and topical patch. Topical vaginal cream: Acceptable\n" +
                "to use low-dose intravaginal estrogen for the\n" +
                "management of dyspareunia, lower urinary tract infections,\n" +
                "and other vaginal symptoms.",
                "Evidence of carcinogenic potential (breast and endometrium); lack\n" +
                        "of cardioprotective effect and cognitive protection in older women.\n" +
                        "Evidence that vaginal estrogens for treatment of vaginal dryness is\n" +
                        "safe and effective in women with breast cancer, especially at dosages\n" +
                        "of estradiol <25 mcg twice weekly.");
        recommendationInfo.setQualityOfEvidence("High (Oral and Patch), Moderate (Topical)");
        recommendationInfo.setStrengthOfRecommendation("Strong (Oral and\n" +
                "Patch), Weak (Topical)");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Estrogens with or without progestins");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Growth hormone");
        recommendationInfo = new RecommendationInfo("Avoid, except as hormone replacement following pituitary\n" +
                "gland removal.",
                "Effect on body composition is small and associated with edema,\n" +
                        "arthralgia, carpal tunnel syndrome, gynecomastia, impaired fasting\n" +
                        "glucose.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Growth hormone");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Insulin, sliding scale");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Higher risk of hypoglycemia without improvement in hyperglycemia\n" +
                        "management regardless of care setting.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Insulin, sliding scale");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 6
        entry = new TherapeuticCategoryEntry("Megestrol");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Minimal effect on weight; increases risk of thrombotic events and\n" +
                        "possibly death in older adults.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Megestrol");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Sulfonylureas, long-duration");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Chlorpropamide: prolonged half-life in older adults; can cause\n" +
                        "prolonged hypoglycemia; causes SIADH\n" +
                        "Glyburide: higher risk of severe prolonged hypoglycemia in older\n" +
                        "adults.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Chlorpropamide");
        drugs.add("Glyburide");
        entry.setDrugs(drugs);
        rec.addEntry(entry);
        beersData.add(rec);
        // TODO add Gastrointestinal

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
