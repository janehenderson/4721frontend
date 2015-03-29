package com.example.frontendian.mappprototype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jane on 2015-03-19.
 */
public class Inscription {

    String LOCATION;
    private int COUNTER;
    private String name, trans, text;

    public Inscription(){
        name= null;
        trans= null;
        text= null;
    }
    public Inscription(String inname, String intrans, String intext) {
        name = inname;
        trans=intrans;
        text=intext;
    }
    public String getName() {
        return name;
    } // getName

    public String getTrans() {
        return trans;
    } // getTrans method

    public String getText() {
        return text;
    } // getText method


    /*public Inscription(Parcel in) {
        LOCATION = in.readString();
        COUNTER = 0;
    }
*/


    // Parcelable stuff we no longer need is commented out below

   /* public static final Parcelable.Creator<Inscription> CREATOR = new Parcelable.Creator<Inscription>() {
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
    } */

    public int getCount() {
        return COUNTER;
    }

    public void setCount(int count) {
        COUNTER = count;
    }

}
