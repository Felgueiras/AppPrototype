package com.felgueiras.apps.geriatric_helper.Firebase;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by felgueiras on 18/04/2017.
 */

class ScaleMetadata {

    @Expose
    private String name;




    private String key;


    @Expose
    private ArrayList<String> languages = new ArrayList<>();

    public ScaleMetadata() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }
}
