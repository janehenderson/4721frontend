package com.example.frontendian.mappprototype;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;


/**
 * Created by Filip on 17/03/2015.
 * Need to first add this observer to the observable class
 */
public class GeoHandler extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //So, the observable class has noticed a geofence, updated us, and has sent us some data.
        //Question 1: what kind of object will the data be?

        ///transitiontype : int, describes if its an enter, dwell, or exit.
        ///TODO import geofence library
        //intent.getExtra();
        //tag for if it is city or local. so ignore if not local.
        //Geofence.GEOFENCE_TRANSITION_DWELL or exit or enter.

        //Test succesful. can notify.
        //Test2: Create a notification.
        //If we have entered the geofence or are still in it.
        //We can kinda assume that this is true.



        //TODO update with inscription data.

                //Stuff happening when we're making the notification.
                //CREATING THE INSCRIPTION
                String trans = intent.getStringExtra("Translation");
                String text = intent.getStringExtra("Text");
                String name = intent.getStringExtra("Name");
                Inscription inscription = new Inscription(name,trans,text);

                //ADDING TO LISTS
                HistoryList list = HistoryList.getHistoryList();
                list.add(inscription);
                //ADD TO INCSRIPTIONNAMELIST
                InscriptionNameList nameList = InscriptionNameList.getList();
                nameList.addFirst(name);
                //inscriptionNameList.addFirst(inscription)
                //?????

                //Stuff that links this notification to an inscriptionDisplay???
                Intent resultIntent = new Intent(context, InscriptionDisplay.class);
                resultIntent.putExtra("IDString",name);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                //How do we get the inscription object into the display???

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Here is a new Notification!:"+name)
                        .setContentIntent(resultPendingIntent);
                NotificationManager mNotifyMana =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMana.notify(01, mBuilder.build());

        Log.w("Receving", "Received exit");



    }
}
