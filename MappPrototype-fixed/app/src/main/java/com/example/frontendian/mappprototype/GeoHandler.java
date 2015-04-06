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
public class GeoHandler extends BroadcastReceiver {
// ID, Name, Text
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("GeoHandler", "started");
        InscriptionNameList nameList = InscriptionNameList.getList();
        HistoryList list = HistoryList.getHistoryList();

        //ID is Translation for now
        String trans = intent.getStringExtra("ID");
        String text = intent.getStringExtra("Text");
        String name = intent.getStringExtra("Name");
        Log.i("Geohandler", "Got ID,Text, Name");
        Inscription inscription = new Inscription(name, trans, text);
        Log.i("Geohandler", "Created Inscription");
        Log.i("Geohandler", "Adding to lists");
        list.add(inscription);
        nameList.addFirst(name);
        Log.i("Geohandler", "Added to lists Completed");
        //?????

        Log.i("Geohandler", "Creating Notification");
        Intent resultIntent = new Intent(context, InscriptionDisplay.class);
        resultIntent.putExtra("IDString", name);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //How do we get the inscription object into the display???

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Here is a new Notification!:" + name)
                .setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMana =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMana.notify(01, mBuilder.build());
        Log.i("Geohandler", "Finished");

    }
}
