package com.example.rafael.appprototype.DataTypes.Criteria.Beers;

import com.example.rafael.appprototype.DataTypes.Criteria.RecommendationInfo;
import com.example.rafael.appprototype.Prescription.Beers.TherapeuticCategoryEntry;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */
public class BeersCriteria {

    String organSystem;

    ArrayList<TherapeuticCategoryOrganSystem> therapeuticCategoryOrganSystems;

    ArrayList<TherapeuticCategoryEntry> entries;
    private static ArrayList<String> beersDrugsNames;

    public static ArrayList<String> getBeersDrugsNamesString() {
        ArrayList<String> drugs = new ArrayList<>();
        for (BeersDrug drug : getBeersDrugs()) {
            drugs.add(drug.getDrug());
        }
        return drugs;
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
    public static ArrayList<String> getBeersDrugsByOrganSystemString() {

        ArrayList<String> drugs = new ArrayList<>();
        for (TherapeuticCategoryOrganSystem rec : getBeersTherapeuticCategoryOrganSystem()) {
            for (TherapeuticCategoryEntry entry : rec.getEntries()) {
                drugs.addAll(entry.getDrugs());
            }
        }
        return drugs;
    }

    public static ArrayList<String> getBeersDrugsAllString() {
        ArrayList<String> drugs = new ArrayList<>();
        drugs.addAll(getBeersDrugsByDiseaseString());
        drugs.addAll(getBeersDrugsByOrganSystemString());
        drugs.addAll(getBeersDrugsNamesString());
        return drugs;
    }

    public static ArrayList<String> getBeersDrugsByDiseaseString() {

        ArrayList<String> drugs = new ArrayList<>();
        for (OrganSystemWithDiseasesSyndromes disease : getBeersDiseaseSyndrome()) {
            for (DiseaseSyndrome entry : disease.getDiseaseSyndromes()) {
                ArrayList<DrugCategory> drugCategories = entry.getDrugCategories();
                // if single category, add category questionTextView
                for (DrugCategory category : drugCategories) {
                    if (category.getDrugs().size() == 0) {
                        drugs.add(category.getCategory());
                    } else {
                        drugs.addAll(category.getDrugs());
                    }
                }
            }
        }
        return drugs;
    }


    public static ArrayList<TherapeuticCategoryOrganSystem> getBeersTherapeuticCategoryOrganSystem() {
        ArrayList<TherapeuticCategoryOrganSystem> beersData = new ArrayList<>();
        // Anticholinergics (excludes TCAs)
        TherapeuticCategoryOrganSystem category = new TherapeuticCategoryOrganSystem("Anticholinergics (excludes TCAs)");
        // entry 1
        TherapeuticCategoryEntry entry = new TherapeuticCategoryEntry("First-generation" +
                " antihistamines (as single agent or as part of combination products)");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Avoid", "Highly anticholinergic; clearance reduced with advanced age, and" +
                "tolerance develops when used as hypnotic; increased risk of confu-" +
                "sion, dry mouth, constipation, and other anticholinergic effects/" +
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
        category.addEntry(entry);
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
        category.addEntry(entry);
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
        category.addEntry(entry);
        beersData.add(category);

        // Antithrombotics
        category = new TherapeuticCategoryOrganSystem("Antithrombotics");
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
        category.addEntry(entry);
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
        category.addEntry(entry);
        beersData.add(category);

        // Antithrombotics
        category = new TherapeuticCategoryOrganSystem("Anti-infective");
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
        category.addEntry(entry);
        beersData.add(category);

        // Cardiovascular
        category = new TherapeuticCategoryOrganSystem("Cardiovascular");
        // entry 1
        entry = new TherapeuticCategoryEntry("Alpha 1 blockers");
        recommendationInfo = new RecommendationInfo("Avoid use as an antihypertensive",
                "High risk of orthostatic hypotension; not recommended as routine" +
                        "treatment for hypertension; alternative agents have superior risk/" +
                        "benefit profile");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Doxazosin");
        drugs.add("Prazosin");
        drugs.add("Terazosin");
        entry.setDrugs(drugs);
        category.addEntry(entry);
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
        category.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Antiarrhythmic drugs (Class Ia, Ic, III)");
        recommendationInfo = new RecommendationInfo("Avoid antiarrhythmic drugs as first-line treatment of atrial fibrillation.",
                "Data suggest that rate control yields better balance of benefits and harms than rhythm control for most older adults." +
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
        category.addEntry(entry);
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
        category.addEntry(entry);
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
        category.addEntry(entry);
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
        category.addEntry(entry);
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
        category.addEntry(entry);
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
        category.addEntry(entry);
        beersData.add(category);

        // Endocrine
        category = new TherapeuticCategoryOrganSystem("Central Nervous System");
        // entry 1
        entry = new TherapeuticCategoryEntry("Tertiary TCAs, alone or in combination:");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Highly anticholinergic, sedating, and cause orthostatic hypotension;" +
                        "the safety profile of low-dose doxepin (≤6 mg/day) is comparable" +
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
        category.addEntry(entry);
        // entry 2
        entry = new TherapeuticCategoryEntry("Antipsychotics, first- (conventional) and second-" +
                "(atypical) generation (see online for full list)");
        recommendationInfo = new RecommendationInfo("Avoid use for behavioral problems of dementia unless" +
                "non-pharmacologic options have failed and patient is" +
                "threat to self or others",
                "Increased risk of cerebrovascular accident (stroke) and mortality in" +
                        "persons with dementia.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Antipsychotics, first- (conventional) and second-" +
                "(atypical) generation (see online for full list)");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Thioridazine" +
                "Mesoridazine");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Highly anticholinergic and greater risk of QT-interval prolongation");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Thioridazine" +
                "Mesoridazine");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Benzodiazepines");
        recommendationInfo = new RecommendationInfo("Avoid benzodiazepines (any type) for treatment of insomnia," +
                "agitation, or delirium.",
                "Older adults have increased sensitivity to benzodiazepines and" +
                        "decreased metabolism of long-acting agents. In general, all benzodiazepines" +
                        "increase risk of cognitive impairment, delirium, falls," +
                        "fractures, and motor vehicle accidents in older adults." +
                        "May be appropriate for seizure disorders, rapid eye movement" +
                        "sleep disorders, benzodiazepine withdrawal, ethanol withdrawal," +
                        "severe generalized anxiety disorder, periprocedural anesthesia," +
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
        category.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Chloral hydrate");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Tolerance occurs within 10 days and risk outweighs the benefits in" +
                        "light of overdose with doses only 3 times the recommended dose.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Chloral hydrate");
        entry.setDrugs(drugs);
        category.addEntry(entry);
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
        category.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Nonbenzodiazepine" +
                "hypnotics");
        recommendationInfo = new RecommendationInfo("Avoid chronic use (>90 days)",
                "Benzodiazepine-receptor agonists that have adverse events similar" +
                        "to those of benzodiazepines in older adults (e.g., delirium, falls," +
                        "fractures); minimal improvement in sleep latency and duration.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Eszopiclone");
        drugs.add("Zolpidem");
        drugs.add("Zaleplon");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 8
        entry = new TherapeuticCategoryEntry("Ergot mesylates*" +
                "Isoxsuprine*");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Lack of efficacy.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Ergot mesylates*" +
                "Isoxsuprine*");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        beersData.add(category);

        // Endocrine
        category = new TherapeuticCategoryOrganSystem("Endocrine");
        // entry 1
        entry = new TherapeuticCategoryEntry("Androgens");
        recommendationInfo = new RecommendationInfo("Avoid unless indicated for moderate to severe hypogonadism.",
                "Potential for cardiac problems and contraindicated in men with" +
                        "prostate cancer");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Methyltestosterone*");
        drugs.add("Testosterone");
        entry.setDrugs(drugs);
        category.addEntry(entry);
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
        category.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Estrogens with or without progestins");
        recommendationInfo = new RecommendationInfo("Avoid oral and topical patch. Topical vaginal cream: Acceptable" +
                "to use low-dose intravaginal estrogen for the" +
                "management of dyspareunia, lower urinary tract infections," +
                "and other vaginal symptoms.",
                "Evidence of carcinogenic potential (breast and endometrium); lack" +
                        "of cardioprotective effect and cognitive protection in older women." +
                        "Evidence that vaginal estrogens for treatment of vaginal dryness is" +
                        "safe and effective in women with breast cancer, especially at dosages" +
                        "of estradiol <25 mcg twice weekly.");
        recommendationInfo.setQualityOfEvidence("High (Oral and Patch), Moderate (Topical)");
        recommendationInfo.setStrengthOfRecommendation("Strong (Oral and" +
                "Patch), Weak (Topical)");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Estrogens with or without progestins");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Growth hormone");
        recommendationInfo = new RecommendationInfo("Avoid, except as hormone replacement following pituitary" +
                "gland removal.",
                "Effect on body composition is small and associated with edema," +
                        "arthralgia, carpal tunnel syndrome, gynecomastia, impaired fasting" +
                        "glucose.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Growth hormone");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Insulin, sliding scale");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Higher risk of hypoglycemia without improvement in hyperglycemia" +
                        "management regardless of care setting.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Insulin, sliding scale");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 6
        entry = new TherapeuticCategoryEntry("Megestrol");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Minimal effect on weight; increases risk of thrombotic events and" +
                        "possibly death in older adults.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Megestrol");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Sulfonylureas, long-duration");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Chlorpropamide: prolonged half-life in older adults; can cause" +
                        "prolonged hypoglycemia; causes SIADH" +
                        "Glyburide: higher risk of severe prolonged hypoglycemia in older" +
                        "adults.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Chlorpropamide");
        drugs.add("Glyburide");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        beersData.add(category);

        // Gastrointestinal
        category = new TherapeuticCategoryOrganSystem("Gastrointestinal");
        // entry 1
        entry = new TherapeuticCategoryEntry("Metoclopramide");
        recommendationInfo = new RecommendationInfo("Avoid, unless for gastroparesis.",
                "Can cause extrapyramidal effects including tardive dyskinesia; risk" +
                        "may be further increased in frail older adults.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Metoclopramide");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 2
        entry = new TherapeuticCategoryEntry("Mineral oil, given orally");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Potential for aspiration and adverse effects; safer alternatives available.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Mineral oil, given orally");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 3
        entry = new TherapeuticCategoryEntry("Trimethobenzamide");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "One of the least effective antiemetic drugs; can cause extrapyramidal" +
                        "adverse effects.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Moderate;");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Trimethobenzamide");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 4
        entry = new TherapeuticCategoryEntry("Meperidine");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Not an effective oral analgesic in dosages commonly used; may" +
                        "cause neurotoxicity; safer alternatives available.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Meperidine");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 5
        entry = new TherapeuticCategoryEntry("Non-COX-selective NSAIDs, oral");
        recommendationInfo = new RecommendationInfo("Avoid chronic use unless other alternatives are not effective" +
                "and patient can take gastroprotective agent (protonpump" +
                "inhibitor or misoprostol).",
                "Increases risk of GI bleeding/peptic ulcer disease in high-risk" +
                        "groups, including those ≥75 years old or taking oral or parenteral" +
                        "corticosteroids, anticoagulants, or antiplatelet agents. Use of proton" +
                        "pump inhibitor or misoprostol reduces but does not eliminate" +
                        "risk. Upper GI ulcers, gross bleeding, or perforation caused by" +
                        "NSAIDs occur in approximately 1% of patients treated for 3–6" +
                        "months, and in about 2%–4% of patients treated for 1 year. These" +
                        "trends continue with longer duration of use.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Aspirin >325 mg/day");
        drugs.add("Diclofenac");
        drugs.add("Diflunisal");
        drugs.add("Etodolac");
        drugs.add("Fenoprofen");
        drugs.add("Ibuprofen");
        drugs.add("Ketoprofen");
        drugs.add("Meclofenamate");
        drugs.add("Mefenamic acid");
        drugs.add("Meloxicam");
        drugs.add("Nabumetone");
        drugs.add("Naproxen");
        drugs.add("Oxaprozin");
        drugs.add("Piroxicam");
        drugs.add("Sulindac");
        drugs.add("Tolmetin");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 6
        entry = new TherapeuticCategoryEntry("Indomethacin" +
                "Ketorolac, includes parenteral");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Increases risk of GI bleeding/peptic ulcer disease in high-risk" +
                        "groups (See Non-COX selective NSAIDs)" +
                        "Of all the NSAIDs, indomethacin has most adverse effects.");
        recommendationInfo.setQualityOfEvidence("Moderate (Indomethacin), High (Ketorolac);");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Indomethacin" +
                "Ketorolac, includes parenteral");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 7
        entry = new TherapeuticCategoryEntry("Pentazocine*");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Opioid analgesic that causes CNS adverse effects, including confusion" +
                        "and hallucinations, more commonly than other narcotic drugs;" +
                        "is also a mixed agonist and antagonist; safer alternatives available.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Pentazocine*");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        // entry 8
        entry = new TherapeuticCategoryEntry("Skeletal muscle relaxants");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Most muscle relaxants poorly tolerated by older adults, because of" +
                        "anticholinergic adverse effects, sedation, increased risk of fractures;" +
                        "effectiveness at dosages tolerated by older adults is questionable.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        entry.setInfo(recommendationInfo);
        drugs = new ArrayList<>();
        drugs.add("Carisoprodol");
        drugs.add("Chlorzoxazone");
        drugs.add("Cyclobenzaprine");
        drugs.add("Metaxalone");
        drugs.add("Methocarbamol");
        drugs.add("Orphenadrine");
        entry.setDrugs(drugs);
        category.addEntry(entry);
        beersData.add(category);


        return beersData;
    }

    /**
     * Get Beers (grouped by disease or syndrome)
     *
     * @return
     */
    public static ArrayList<OrganSystemWithDiseasesSyndromes> getBeersDiseaseSyndrome() {

        ArrayList<OrganSystemWithDiseasesSyndromes> beersData = new ArrayList<>();
        // system 1
        OrganSystemWithDiseasesSyndromes system = new OrganSystemWithDiseasesSyndromes("Cardiovascular");
        // diseaseSyndrome 1
        DiseaseSyndrome diseaseSyndrome = new DiseaseSyndrome("Heart failure");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Avoid",
                "Potential to promote fluid retention and/or exacerbate" +
                        "heart failure.");
        recommendationInfo.setQualityOfEvidence("High (Hydroxyzine and Promethazine), Moderate (All others)");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // cat 1
        DrugCategory category = new DrugCategory("NSAIDs and COX-2 inhibitors");
        diseaseSyndrome.addDrugCategory(category);
        // cat 2
        category = new DrugCategory("Nondihydropyridine CCBs (avoid only for" +
                "systolic heart failure)");
        category.addDrug("Diltiazem");
        category.addDrug("Verapamil");
        diseaseSyndrome.addDrugCategory(category);
        // cat 3
        category = new DrugCategory("Pioglitazone, rosiglitazone");
        diseaseSyndrome.addDrugCategory(category);
        // cat 4
        category = new DrugCategory("Cilostazol");
        diseaseSyndrome.addDrugCategory(category);
        // cat 5
        category = new DrugCategory("Dronedarone");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 2
        diseaseSyndrome = new DiseaseSyndrome("Syncope");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Increases risk of orthostatic hypotension or bradycardia.");
        recommendationInfo.setQualityOfEvidence("High (Alpha blockers), Moderate (AChEIs, TCAs and" +
                "antipsychotics)");
        recommendationInfo.setStrengthOfRecommendation("Strong (AChEIs and TCAs), Weak" +
                "(Alpha blockers and antipsychotics)");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // cat 1
        category = new DrugCategory("Acetylcholinesterase inhibitors (AChEIs)");
        diseaseSyndrome.addDrugCategory(category);
        // cat 2
        category = new DrugCategory("Peripheral alpha blockers");
        category.addDrug("Doxazosin");
        category.addDrug("Prazosin");
        category.addDrug("Terazosin");
        diseaseSyndrome.addDrugCategory(category);
        // cat 3
        category = new DrugCategory("Tertiary TCAs");
        diseaseSyndrome.addDrugCategory(category);
        // cat 4
        category = new DrugCategory("Chlorpromazine, thioridazine, and olanzapine");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);
        beersData.add(system);

        // system 2
        system = new OrganSystemWithDiseasesSyndromes("Central Nervous System");
        // diseaseSyndrome 1
        diseaseSyndrome = new DiseaseSyndrome("Chronic seizures or epilepsy");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Lowers seizure threshold; may be acceptable in" +
                        "patients with well-controlled seizures in whom alternative" +
                        "agents have not been effective");
        recommendationInfo.setQualityOfEvidence("Moderate;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Bupropion"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Chlorpromazine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Clozapine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Maprotiline"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Olanzapine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Thioridazine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Thiothixene"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Tramadol"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 2
        diseaseSyndrome = new DiseaseSyndrome("Delirium");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Avoid in older adults with or at high risk of delirium" +
                        "because of inducing or worsening delirium in older" +
                        "adults; if discontinuing drugs used chronically, taper to" +
                        "avoid withdrawal symptoms.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("All TCAs"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Anticholinergics"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Benzodiazepines"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Chlorpromazine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Corticosteroids"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("H2-receptor antagonist"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Meperidine"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Sedative hypnotics"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Thioridazine"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 3
        diseaseSyndrome = new DiseaseSyndrome("Dementia & cognitive impairment");
        recommendationInfo = new RecommendationInfo("Avoid",
                "Avoid due to adverse CNS effects. " +
                        "Avoid antipsychotics for behavioral problems of " +
                        "dementia unless non-pharmacologic options have " +
                        "failed and patient is a threat to themselves or others. " +
                        "Antipsychotics are associated with an increased risk " +
                        "of cerebrovascular accident (stroke) and mortality in " +
                        "persons with dementia.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Anticholinergics"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Benzodiazepines"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("H2-receptor antagonists"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Zolpidem"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Antipsychotics, chronic and as-needed use"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 4
        diseaseSyndrome = new DiseaseSyndrome("History " +
                "of falls or " +
                "fractures");
        recommendationInfo = new RecommendationInfo("Avoid unless safer alternatives are not available; " +
                "avoid anticonvulsants except for seizure.",
                "Ability to produce ataxia, impaired psychomotor " +
                        "function, syncope, and additional falls; shorter-acting " +
                        "benzodiazepines are not safer than long-acting ones.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Anticonvulsants"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Antipsychotics"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Benzodiazepines"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("TCAs/SSRIs"));
        category = new DrugCategory("Nonbenzodiazepine hypnotics");
        category.addDrug("Eszopiclone");
        category.addDrug("Zaleplon");
        category.addDrug("Zolpidem");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 5
        diseaseSyndrome = new DiseaseSyndrome("Insomnia");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "CNS stimulant effects.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        category = new DrugCategory("Oral decongestants");
        category.addDrug("Pseudoephedrine");
        category.addDrug("Phenylephrine Stimulants");
        category.addDrug("Amphetamine");
        category.addDrug("Methylphenidate");
        category.addDrug("Pemoline Theobromines");
        category.addDrug("Theophylline");
        category.addDrug("Caffeine");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 6
        diseaseSyndrome = new DiseaseSyndrome("Parkinson’s " +
                "disease");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "Dopamine receptor antagonists with potential to " +
                        "worsen parkinsonian symptoms. " +
                        "Quetiapine and clozapine appear to be less likely to " +
                        "precipitate worsening of Parkinson disease.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        category = new DrugCategory("Antiemetics");
        category.addDrug("Metoclopramide");
        category.addDrug("Prochlorperazine");
        category.addDrug("Promethazine");
        diseaseSyndrome.addDrugCategory(category);
        diseaseSyndrome.addDrugCategory(new DrugCategory("All antipsychotics (see online publication " +
                "for full list, except for quetiapine and " +
                "clozapine)"));
        system.addDiseaseSyndrome(diseaseSyndrome);
        beersData.add(system);

        // system 3
        system = new OrganSystemWithDiseasesSyndromes("Gastrointestinal");
        // diseaseSyndrome 1
        diseaseSyndrome = new DiseaseSyndrome("Chronic constipation");
        recommendationInfo = new RecommendationInfo("Avoid unless no other alternatives.",
                "Can worsen constipation; agents for urinary incontinence: " +
                        "antimuscarinics overall differ in incidence of " +
                        "constipation; response variable; consider alternative " +
                        "agent if constipation develops.");
        recommendationInfo.setQualityOfEvidence("High (For Urinary Incontinence), Moderate/Low (All " +
                "Others)");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        category = new DrugCategory("Oral antimuscarinics for urinary incontinence");
        category.addDrug("Darifenacin");
        category.addDrug("Fesoterodine");
        category.addDrug("Oxybutynin (oral)");
        category.addDrug("Solifenacin");
        category.addDrug("Tolterodine");
        category.addDrug("Trospium");
        diseaseSyndrome.addDrugCategory(category);
        // categories/drugs
        category = new DrugCategory("Nondihydropyridine CCB");
        category.addDrug("Diltiazem");
        category.addDrug("Verapamil");
        diseaseSyndrome.addDrugCategory(category);
        // categories/drugs
        category = new DrugCategory("First-generation antihistamines as single " +
                "agent or part of combination products");
        category.addDrug("Brompheniramine (various)");
        category.addDrug("Carbinoxamine");
        category.addDrug("Chlorpheniramine");
        category.addDrug("Clemastine (various)");
        category.addDrug("Cyproheptadine");
        category.addDrug("Dexbrompheniramine");
        category.addDrug("Dexchlorpheniramine (various)");
        category.addDrug("Diphenhydramine");
        category.addDrug("Doxylamine");
        category.addDrug("Hydroxyzine");
        category.addDrug("Promethazine");
        category.addDrug("Triprolidine");
        diseaseSyndrome.addDrugCategory(category);
        // categories/drugs
        category = new DrugCategory("Anticholinergics/antispasmodics");
        category.addDrug("Antipsychotics");
        category.addDrug("Belladonna alkaloids");
        category.addDrug("Clidinium-chlordiazepoxide");
        category.addDrug("Dicyclomine");
        category.addDrug("Hyoscyamine");
        category.addDrug("Propantheline");
        category.addDrug("Scopolamine");
        category.addDrug("Tertiary TCAs (amitriptyline, clomipramine, " +
                "doxepin, imipramine, and trimipramine)");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 2
        diseaseSyndrome = new DiseaseSyndrome("History of " +
                "gastric or " +
                "duodenal " +
                "ulcers");
        recommendationInfo = new RecommendationInfo("Avoid unless other alternatives are not effective " +
                "and patient can take gastroprotective " +
                "agent (proton-pump inhibitor or misoprostol).",
                "May exacerbate existing ulcers or cause new/additional " +
                        "ulcers.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Aspirin (>325 mg/day)"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Non–COX-2 selective NSAIDs"));
        system.addDiseaseSyndrome(diseaseSyndrome);
        beersData.add(system);

        // system 4
        system = new OrganSystemWithDiseasesSyndromes("Kidney/Urinary Tract");
        // diseaseSyndrome 1
        diseaseSyndrome = new DiseaseSyndrome("Chronic kidney " +
                "disease " +
                "stages IV " +
                "and V");
        recommendationInfo = new RecommendationInfo("Avoid.",
                "May increase risk of kidney injury. " +
                        "May increase risk of acute kidney injury.");
        recommendationInfo.setQualityOfEvidence("Moderate (NSAIDs), Low (Triamterene);");
        recommendationInfo.setStrengthOfRecommendation("Strong " +
                "(NSAIDs), Weak (Triamterene)");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("NSAIDs"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Triamterene (alone or in combination)"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 2
        diseaseSyndrome = new DiseaseSyndrome("Urinary " +
                "incontinence " +
                "(all types) in " +
                "women");
        recommendationInfo = new RecommendationInfo("Avoid in women.",
                "Aggravation of incontinence.");
        recommendationInfo.setQualityOfEvidence("High");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Estrogen oral and transdermal (excludes " +
                "intravaginal estrogen)"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 3
        diseaseSyndrome = new DiseaseSyndrome("Lower " +
                "urinary tract " +
                "symptoms, " +
                "benign " +
                "prostatic " +
                "hyperplasia");
        recommendationInfo = new RecommendationInfo("Avoid in men.",
                "May decrease urinary flow and cause urinary retention.");
        recommendationInfo.setQualityOfEvidence("Moderate;");
        recommendationInfo.setStrengthOfRecommendation("Strong (Inhaled agents), Weak (All " +
                "others)");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        diseaseSyndrome.addDrugCategory(new DrugCategory("Inhaled anticholinergic agents"));
        diseaseSyndrome.addDrugCategory(new DrugCategory("Strongly anticholinergic drugs, except " +
                "antimuscarinics for urinary incontinence"));
        system.addDiseaseSyndrome(diseaseSyndrome);

        // diseaseSyndrome 4
        diseaseSyndrome = new DiseaseSyndrome("Stress or " +
                "mixed " +
                "urinary incontinence");
        recommendationInfo = new RecommendationInfo("Avoid in women.",
                "Aggravation of incontinence");
        recommendationInfo.setQualityOfEvidence("Moderate;");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        diseaseSyndrome.setRecommendationInfo(recommendationInfo);
        // categories/drugs
        category = new DrugCategory("Alpha-blockers");
        category.addDrug("Doxazosin");
        category.addDrug("Prazosin");
        category.addDrug("Terazosin");
        diseaseSyndrome.addDrugCategory(category);
        system.addDiseaseSyndrome(diseaseSyndrome);
        beersData.add(system);

        return beersData;
    }

    public static ArrayList<BeersDrug> getBeersDrugs() {

        ArrayList<BeersDrug> beersData = new ArrayList<>();

        // drug 1
        BeersDrug drug = new BeersDrug("Aspirin for primary prevention " +
                "of cardiac events");
        RecommendationInfo recommendationInfo = new RecommendationInfo("Use with caution in adults ≥80 years old.",
                "Lack of evidence of benefit versus risk in individuals ≥80 years old.");
        recommendationInfo.setQualityOfEvidence("Low");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);

        // drug 2
        drug = new BeersDrug("Dabigatran");
        recommendationInfo = new RecommendationInfo("Use with caution in adults ≥75 years old or if CrCl <30 mL/min.",
                "Increased risk of bleeding compared with warfarin in adults ≥75 years old; lack of " +
                        "evidence for efficacy and safety in patients with CrCl <30 mL/min");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);

        // drug 2
        drug = new BeersDrug("Prasugrel");
        recommendationInfo = new RecommendationInfo("Use with caution in adults ≥75 years old.",
                "Greater risk of bleeding in older adults; risk may be offset by benefit in highestrisk " +
                        "older patients (eg, those with prior myocardial infarction or diabetes).");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);

        // drugs 3.1
        drug = new BeersDrug("Antipsychotics");
        recommendationInfo = new RecommendationInfo("Use with caution.",
                "May exacerbate or cause SIADH or hyponatremia; need to monitor sodium level " +
                        "closely when starting or changing dosages in older adults due to increased risk.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Strong");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.2
        drug = new BeersDrug("Carbamazepine");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.3
        drug = new BeersDrug("Carboplatin");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.4
        drug = new BeersDrug("Cisplatin");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.5
        drug = new BeersDrug("Mirtazapine");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.6
        drug = new BeersDrug("SNRIs");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.7
        drug = new BeersDrug("SSRIs");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.8
        drug = new BeersDrug("TCAs");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);
        // 3.9
        drug = new BeersDrug("Vincristine");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);


        // drug 4
        drug = new BeersDrug("Vasodilators");
        recommendationInfo = new RecommendationInfo("Use with caution.",
                "May exacerbate episodes of syncope in individuals with history of syncope.");
        recommendationInfo.setQualityOfEvidence("Moderate");
        recommendationInfo.setStrengthOfRecommendation("Weak");
        drug.setRecommendationInfo(recommendationInfo);
        beersData.add(drug);

        return beersData;
    }

    /**
     * Return the Beers criteria beersGeneral for a certain drug patientName.
     *
     * @param selectedDrug
     * @return
     */
    public static ArrayList<RecommendationInfo> getBeersCriteria(String selectedDrug) {
        ArrayList<RecommendationInfo> recommendationInfos = new ArrayList<>();
        // by organ system
        for (TherapeuticCategoryOrganSystem rec : getBeersTherapeuticCategoryOrganSystem()) {
            for (TherapeuticCategoryEntry entry : rec.getEntries()) {
                ArrayList<String> drugs = entry.getDrugs();
                if (drugs.contains(selectedDrug)) {
                    recommendationInfos.add(entry.getInfo());
                }
            }
        }

        // by disease/syndrome
        for (OrganSystemWithDiseasesSyndromes rec : getBeersDiseaseSyndrome()) {
            for (DiseaseSyndrome diseaseSyndrome : rec.getDiseaseSyndromes()) {
                for (DrugCategory drugCategory : diseaseSyndrome.getDrugCategories()) {
                    if (drugCategory.getCategory().equals(selectedDrug)) {
                        recommendationInfos.add(diseaseSyndrome.getRecommendationInfo());
                    }
                    if (drugCategory.getDrugs().contains(selectedDrug)) {
                        recommendationInfos.add(diseaseSyndrome.getRecommendationInfo());
                    }
                }

            }
        }

        // drugs
        for (BeersDrug drug : getBeersDrugs()) {
            if (drug.getDrug().equals(selectedDrug)) {
                recommendationInfos.add(drug.getRecommendationInfo());
            }
        }

        return recommendationInfos;
    }
}
