package com.example.frontendian.mappprototype;

/**
 * @ Authors Reece, James, Milton, Federico
 */

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

public class GeofenceTransitionsIntentService extends IntentService implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "GEOFENCE_INTENT_SERVICE";
    private GoogleApiClient mGoogleApiClient;
    private BackendController bcontroller;//TODO do we need this?

    public GeofenceTransitionsIntentService() {
        super(TAG);
        bcontroller = null;
        //pastGeofences = new ArrayList<String>();
    }

    //For testing purposes
    public GoogleApiClient getGoogleApiClient() {
        return this.mGoogleApiClient;
    }

    @Override
    public void onCreate() {
        //Code from sample repository
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

    }

    //Method for testing
    public boolean isConnected() {
        boolean value = mGoogleApiClient.isConnected();
        if (value) {
            Log.i(TAG, "google API client is Connected");
        } else {
            Log.i(TAG, "google API client is Not Connected");
        }
        return value;
    }

    @Override
    public void onConnected(Bundle b) {
        Log.i(TAG, "You are being connected to the api");
    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    public void onHandleIntent(Intent i) {

        //Get if it is a city or smaller geofence and pass to controller

        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(i);

        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.i(TAG, "Error: " + errorCode);
        } else {

            int transitionType = geoFenceEvent.getGeofenceTransition();
            String triggeredGeoFenceId = geoFenceEvent.getTriggeringGeofences().get(0)
                    .getRequestId();


            Log.i(TAG, "Sending Geofence: " + triggeredGeoFenceId);
            sendMessage(transitionType, triggeredGeoFenceId);
        }
    }

    private void sendMessage(int transitionType, String triggeredGeoFenceId) {

        Log.i(TAG, "Broadcasting message: GTIS TO BEC");
        //Make Intent to broadcast
        Intent intent = new Intent("BEC");
        intent.putExtra(Constants.TRANSITION_TYPE, transitionType);
        intent.putExtra(Constants.CITYORLOCALTAG, triggeredGeoFenceId.substring(0, 1));
        intent.putExtra(Constants.ID, triggeredGeoFenceId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //return boolean for testing ability only
    public boolean onHandleIntent(Intent i, String s) {

        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(i);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();

        } else {

            int transitionType = geoFenceEvent.getGeofenceTransition();

            //Entering a geofence
            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {
                String triggeredGeoFenceId = geoFenceEvent.getTriggeringGeofences().get(0)
                        .getRequestId();


//                bcontroller.setCurrentGeofenceid(triggeredGeoFenceId);

                // Create a DataItem with this geofence's id. The wearable can use this to create
                // a notification.
                return true;
            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {
                //Notify controller that we exit
//                bcontroller.setCurrentGeofenceid("");

                return true;
            }


        }
        return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "Connection failed, Passing result to controller");
        this.bcontroller.onConnectionFailed(result);
    }
}
