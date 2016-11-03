package com.example.rafael.appprototype.DrugPrescription;

/**
 * Created by rafael on 01-11-2016.
 */
public class Issue {

    String description;
    String risk;

    public Issue(String desc, String risk) {
        this.description = desc;
        this.risk = risk;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "- " + description + "("+risk + ")";
        return ret;
    }
}
