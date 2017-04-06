package com.felgueiras.apps.geriatric_helper.Prescription.AllDrugs;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


public class DrugSuggestion implements SearchSuggestion {

    private String drugName;
    private boolean mIsHistory = false;

    public DrugSuggestion(String suggestion) {
        this.drugName = suggestion;
    }

    public DrugSuggestion(Parcel source) {
        this.mIsHistory = source.readInt() != 0;
    }


    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return drugName;
    }

    public static final Creator<DrugSuggestion> CREATOR = new Creator<DrugSuggestion>() {
        @Override
        public DrugSuggestion createFromParcel(Parcel in) {
            return new DrugSuggestion(in);
        }

        @Override
        public DrugSuggestion[] newArray(int size) {
            return new DrugSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drugName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}