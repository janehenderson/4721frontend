package com.example.frontendian.mappprototype;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

/**
 * Created by mariahmartinshein.
 */
public class HistoryList<Inscription> extends LinkedList implements Parcelable{
    private int mData;
    private int maxListSize = 50;
    private static HistoryList main=null;

    private HistoryList(){

    }
    public static HistoryList getHistoryList(){
        if(main==null){
            main= new HistoryList();
        }
        return main;
    }

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
    }


}
