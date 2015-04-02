package com.example.frontendian.mappprototype;

/**
 * Created by Jane on 2015-03-19.
 */
public class Inscription {

    String LOCATION;
    private int COUNTER;
    private String name, trans, text;
    private boolean seen;

    public Inscription(){
        name= null;
        trans= null;
        text= null;
        seen = false;
    }
    public Inscription(String inname, String intrans, String intext) {
        name = inname;
        trans=intrans;
        text=intext;
        seen = false;
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

    public void setSeen(boolean toSet){
        seen = toSet;
    }

    public boolean isSeen(){
        return seen;
    }
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
