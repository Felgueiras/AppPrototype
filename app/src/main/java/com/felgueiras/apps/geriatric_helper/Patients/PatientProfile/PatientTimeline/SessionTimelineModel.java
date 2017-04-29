package com.felgueiras.apps.geriatric_helper.Patients.PatientProfile.PatientTimeline;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class SessionTimelineModel implements Parcelable {

    private String mMessage;
    private String mDate;
    private OrderStatus mStatus;

    public SessionTimelineModel() {
    }

    public SessionTimelineModel(String mMessage, String mDate, OrderStatus mStatus) {
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void semMessage(String message) {
        this.mMessage = message;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public OrderStatus getStatus() {
        return mStatus;
    }

    public void setStatus(OrderStatus mStatus) {
        this.mStatus = mStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mDate);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
    }

    protected SessionTimelineModel(Parcel in) {
        this.mMessage = in.readString();
        this.mDate = in.readString();
        int tmpMStatus = in.readInt();
        this.mStatus = tmpMStatus == -1 ? null : OrderStatus.values()[tmpMStatus];
    }

    public static final Creator<SessionTimelineModel> CREATOR = new Creator<SessionTimelineModel>() {
        @Override
        public SessionTimelineModel createFromParcel(Parcel source) {
            return new SessionTimelineModel(source);
        }

        @Override
        public SessionTimelineModel[] newArray(int size) {
            return new SessionTimelineModel[size];
        }
    };
}
