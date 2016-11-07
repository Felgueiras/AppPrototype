package com.example.rafael.appprototype.DrugPrescription.StartStopp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rafael on 01-11-2016.
 */

public class StoppGeneral {
    ArrayList<StoppCriterion> criterions;

    public StoppGeneral() {
        this.criterions = new ArrayList<>();

    }

    public ArrayList<StoppCriterion> getCriterions() {
        return criterions;
    }

    public void setCriterions(ArrayList<StoppCriterion> criterions) {
        this.criterions = criterions;
    }

    public void addStoppCriterion(StoppCriterion stoppCriterion) {
        criterions.add(stoppCriterion);
    }

    /**
     * Get the issues associated to a given drug (start stopp criteria).
     *
     * @param drugSearchingFor
     * @return
     */
    public ArrayList<Issue> getIssuesForGivenDrug(String drugSearchingFor) {
        for (StoppCriterion criterion : criterions) {
            ArrayList<Prescription> prescriptions = criterion.getPrescriptions();
            for (Prescription pr : prescriptions) {
                String drugName = pr.getDrugName();
                System.out.println(drugName);
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
    public Prescription getStoppCriteriaPresciptionForDrug(String drugSearchingFor) {
        for (StoppCriterion criterion : criterions) {
            ArrayList<Prescription> prescriptions = criterion.getPrescriptions();
            for (Prescription pr : prescriptions) {
                String drugName = pr.getDrugName();
                if (drugName.equals(drugSearchingFor)) {
                    return pr;
                }
            }
        }
        return null;
    }
}
