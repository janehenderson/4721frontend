package com.example.frontendian.mappprototype;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by mariahmartinshein.
 */
public class HistoryList<Inscription> extends ArrayList<Inscription> {

    private int mData;
    private int maxListSize = 50;
    private static HistoryList main = null;

    private HistoryList(){ }

    public static HistoryList getHistoryList(){
        if(main == null){
            main = new HistoryList();
        }
        return main;
    }


    // returns an Inscription whose name matches the input string
    public Inscription getInscription(String name) {

        for(int i = 0; i < this.size(); i++) {

            Inscription inscr = this.get(i);

            if(inscr.getName().equals(name)) {
                return inscr;
            } // if

        } // for

        return null;

    } // getInscription method


    // Parcelable stuff we probably don't need anymore

/*

     public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        //ListIterator<String> listIterator = this.listIterator();
        //while (listIterator.hasNext()) {
          //  out.writeString(listIterator.next());
        //}
        String[] histString = (String[])this.toArray();
        out.writeArray(histString);
        //out.writeInt(mData);
    }

    public static final Parcelable.Creator<HistoryList> CREATOR
            = new Parcelable.Creator<HistoryList>() {
        public HistoryList createFromParcel(Parcel in) {
            return new HistoryList(in);
        }

        public HistoryList[] newArray(int size) {
            return new HistoryList[size];
        }
    };

    private HistoryList(Parcel in) {
        //mData = in.readInt();
        String[] inHist = new String[maxListSize];
        in.readStringArray(inHist);
    }*/


}
