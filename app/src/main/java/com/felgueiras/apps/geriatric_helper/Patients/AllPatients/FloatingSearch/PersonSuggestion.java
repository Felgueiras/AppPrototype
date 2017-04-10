package com.felgueiras.apps.geriatric_helper.Patients.AllPatients.FloatingSearch;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.felgueiras.apps.geriatric_helper.Firebase.PatientFirebase;


public class PersonSuggestion implements SearchSuggestion {

    private PatientFirebase patient;
    private String mColorName;
    private boolean mIsHistory = false;

    public PersonSuggestion(PatientFirebase suggestion) {
//        this.mColorName = suggestion.toLowerCase();
        this.patient = suggestion;
        this.mColorName = suggestion.getName();
    }

    public PersonSuggestion(Parcel source) {
        this.mColorName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }


    public PatientFirebase getPatient() {
        return patient;
    }

    public void setPatient(PatientFirebase patient) {
        this.patient = patient;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mColorName;
    }

    public static final Creator<PersonSuggestion> CREATOR = new Creator<PersonSuggestion>() {
        @Override
        public PersonSuggestion createFromParcel(Parcel in) {
            return new PersonSuggestion(in);
        }

        @Override
        public PersonSuggestion[] newArray(int size) {
            return new PersonSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mColorName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}