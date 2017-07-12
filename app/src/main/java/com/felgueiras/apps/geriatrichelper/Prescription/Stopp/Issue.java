package com.felgueiras.apps.geriatrichelper.Prescription.Stopp;

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

    public String getDescription() {
        return description;
    }

    public String getRisk() {
        return risk;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "- " + description + "("+risk + ")";
        return ret;
    }
}
