package com.example.frontendian.mappprototype;

import java.util.LinkedList;

/**
 * Created by Filip on 28/03/2015.
 */
public class InscriptionNameList<String> extends LinkedList {

    private static InscriptionNameList theList=null;

    private InscriptionNameList(){

    }
    public static InscriptionNameList getList(){
        if(theList == null){
            theList = new InscriptionNameList();
        }
        return theList;
    }

}
