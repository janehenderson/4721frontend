package com.example.frontendian.mappprototype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jane on 2015-03-19.
 */
public class Inscription implements Parcelable {

    String LOCATION;
    private int COUNTER;

    public Inscription() {
    }

    public Inscription(Parcel in) {
        LOCATION = in.readString();
        COUNTER = 0;
    }

    public static final Parcelable.Creator<Inscription> CREATOR = new Parcelable.Creator<Inscription>() {
        public Inscription createFromParcel(Parcel in) {
            return new Inscription(in);
        }

        public Inscription[] newArray(int size) {
            return new Inscription[size];
        }
    };




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LOCATION);
        dest.writeInt(COUNTER);
    }

    public int getCount() {
        return COUNTER;
    }

    public void setCount(int count) {
        COUNTER = count;
    }

}
