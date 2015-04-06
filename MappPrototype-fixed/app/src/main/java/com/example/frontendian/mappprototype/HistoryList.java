package com.example.frontendian.mappprototype;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by mariahmartinshein.
 */
public class HistoryList extends LinkedList {
//Cannot have HistoryList<Inscription> due to "type erasure"
//what happens is that we can't use any inscription methods in this class....
    private int mData;
    private int maxListSize = 50;
    private static HistoryList main = null;
    private static int counter;
    private HistoryList(){ }

    public static HistoryList getHistoryList(){
        if(main == null){
            main = new HistoryList();
            counter=0;
        }
        return main;
    }
    public void updateCount(){
        counter++;
    }
    public int getCount(){
        return counter;
    }

    // returns an Inscription whose name matches the input string
    public Inscription getInscription(String name) {

        for(int i = 0; i < this.size(); i++) {

            Inscription inscr = (Inscription) this.get(i);
            if(inscr.getName()!=null) {
               // Log.i("MR.MEESEEKS","We got the inscription name!");
                if (inscr.getName().equals(name)) {
                    return inscr;
                } // if
            }
            else{
                Log.e("MR.MEESEEKS", "HOLYSHIT INSCRIPTIONS BE BROKE Y'ALL");
            }
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
