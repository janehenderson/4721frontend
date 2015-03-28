package com.example.frontendian.mappprototype;

import android.app.NotificationManager;
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
        intent.getStringExtra("tag"); // c or l if l do stuff.

        intent.getStringExtra("id"); //exactly which geofence it is.

        intent.getStringExtra("Translation");
        intent.getStringExtra("Text");

        //TODO update with inscription data.
        if(intent.getStringExtra("tag").equals("l")) {//do stuff if local geofence.
            if (intent.getIntExtra("transitiontype", -1) == Geofence.GEOFENCE_TRANSITION_DWELL ||
                    intent.getIntExtra("transitiontype", -1) == Geofence.GEOFENCE_TRANSITION_ENTER) {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello asshat");
                NotificationManager mNotifyMana =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMana.notify(01, mBuilder.build());
            }
        }
        Log.w("Receving", "Received exit");



    }
}
