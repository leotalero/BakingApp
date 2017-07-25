package com.android.leonardotalero.bakingapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leonardotalero on 7/8/17.
 */

public class Step implements Parcelable {
    public int sId;
    public String sShortDescription;
    public String sDescription;
    public String sVideoUrl;
    public String sThumbnailURL;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sId);
        dest.writeString(this.sShortDescription);
        dest.writeString(this.sDescription);
        dest.writeString(this.sVideoUrl);
        dest.writeString(this.sThumbnailURL);
    }

    public Step() {
    }

    public Step(int sId, String sShortDescription, String sDescription, String sVideoUrl, String sThumbnailURL) {
        this.sId = sId;
        this.sShortDescription = sShortDescription;
        this.sDescription = sDescription;
        this.sVideoUrl = sVideoUrl;
        this.sThumbnailURL = sThumbnailURL;
    }

    protected Step(Parcel in) {
        this.sId = in.readInt();
        this.sShortDescription = in.readString();
        this.sDescription = in.readString();
        this.sVideoUrl = in.readString();
        this.sThumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
