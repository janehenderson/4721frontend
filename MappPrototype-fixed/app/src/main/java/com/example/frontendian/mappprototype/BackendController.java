package com.example.frontendian.mappprototype;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Authors Reece, James, Milton, Federico
 */
public class BackendController implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private final String TAG = "BACKEND_CONTROLLER";
    private ArtifactGeofence artifact;
    private GeofenceStore geoStore;
    private Context context;
    private GoogleApiClient googleApiClient;
    private GeofenceRegistrationCallbacks mCallback;
    private ArrayList<String> pastGeofences = new ArrayList<>();


    //this keeps track of the currently loaded city so that we
    // don't try to load it over and over (for example if loading
    // when dwelling and entering).
    private String currLoadedCity = "";

    public BackendController(Context context) {
        this.context = context;
        geoStore = new GeofenceStore(context);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect(); //probably redundant

        Log.i(TAG, "CREATED BACKEND");

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("BEC"));
    }


    //Checks to see if the given city geofence is the newest version.
    //If not, load in new geofences for that city
    private void checkVersion(CityGeofence oldGeofence) {
        String ID = oldGeofence.getID();
        double version = oldGeofence.getVersion();
        //geoStore.loadCities();
        ArrayList<CityGeofence> newestCity = geoStore.getCities();
        int len = newestCity.size();
        for (int i = 0; i < len; i++) {
            CityGeofence curr = newestCity.get(i);
            if (curr.getID().equals(ID)) {
                if (version < curr.getVersion()) {
                    //Load all small geofences within this city geofence
                    geoStore.loadArtifacts(oldGeofence);
                }
            }
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean entered = false;
            for (int j = 0; j < pastGeofences.size(); j++) {
                if (pastGeofences.get(j).equals(intent.getStringExtra(Constants.ID))) {
                    entered = true;
                }
            }

            if (!entered) {
                pastGeofences.add(intent.getStringExtra(Constants.ID));


                Log.i(TAG, "RECEIVED BROADCAST: GTIS TO BEC");
                //Get information pertaining to id and broadcast to Local_Geofence
                String ID = intent.getStringExtra(Constants.ID);
                int transitionType = intent.getIntExtra(Constants.TRANSITION_TYPE, -1); //-1 is a default

                //one of these two geofences will be initialized
                //depending on if it is a city intent or an
                //artifact intent we are dealing with.
                CityGeofence city = null;
                ArtifactGeofence curr = null;
                //find out if it is a city or an artifact
                //and initialize the appropriate one
                boolean isCity = intent.getStringExtra(Constants.CITYORLOCALTAG).equals(Constants.CITY_TAG);
                boolean isLocal = intent.getStringExtra(Constants.CITYORLOCALTAG).equals(Constants.LOCAL_TAG);

                if (isCity) {

                    city = geoStore.getCityGeofence(ID);
                } else if (isLocal) {
                    curr = geoStore.getArtifactGeofence(ID);
                    //It isn't a city or a local geofence - ?
                } else {
                    Log.e(TAG, "Oh man we really fucked up. This geofence isn't a city or a local. ABORT");
                }

                //what type of intent is it?
                //go through the different types and work accordingly
                if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    if (isCity) {
                        Log.i(TAG, "ENTERED CITY");
                        //call controller to load local geofences for that city
                        //this will only do something if the city trying to be registered hasnt already
                        loadArtifactGeofences(city);

                    } else if (isLocal) {
                        Log.i(TAG, "ENTERED ARTIFACT ZONE");
                        Intent localIntent = new Intent("BEC");
                        localIntent.putExtra("ID", ID);
                        localIntent.putExtra("Name", curr.getName());
                        localIntent.putExtra("Text", curr.getArtifactText());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    if (isCity) {
                        Log.i(TAG, "EXIT CITY");
                        //call controller to unregister local geofences
                    } else if (isLocal) {
                        Log.i(TAG, "EXIT ARTIFACT ZONE");
                    }
                } else if (transitionType == Geofence.GEOFENCE_TRANSITION_DWELL) {
                    if (isCity) {
                        Log.i(TAG, "DWELL CITY");
                        //call controller to load local geofences for that city
                        //this will only do something if the city trying to be registered hasnt already
                        loadArtifactGeofences(city);
                    } else if (isLocal) {
                        Log.i(TAG, "DWELL ARTIFACT ZONE");
                        Intent localIntent = new Intent("BEC");
                        localIntent.putExtra("ID", ID);
                        localIntent.putExtra("Name", curr.getName());
                        localIntent.putExtra("Text", curr.getArtifactText());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }
            }
        }//end onReceive
    };

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public ArtifactGeofence getArtifactGeofence() {
        return artifact;
    }

    @Override
    public void onConnected(Bundle bundle) {

        geoStore.loadCities();

        if (geoStore.getCities() != null) {
            loadCityGeofences();
            Log.i(TAG, "CONNECTED TO API, ATTEMPTING TO REGISTER CITY GEOFENCES");
        } else {
            Log.e(TAG, "FAILED TO LOAD CITIES: CITY LIST IS NULL");
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Backend API connection suspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "BACKEND FAILED TO CONNECT TO API: " + connectionResult);
    }

    /**
     * Registers the CityGeofences in geoStore
     */
    public void loadCityGeofences() {

        List<Geofence> cityGeoList = new ArrayList<>();
        Log.i(TAG, "GEO SIZE: " + geoStore.getCities().size());

        for (CityGeofence g : geoStore.getCities()) {

            cityGeoList.add(g.toGeofence());
        }
        GeofencingRequest cityReq = new GeofencingRequest.Builder()
                .addGeofences(cityGeoList)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();

        try {

            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient, cityReq,
                    getGeofenceTransitionPendingIntent()
            ).setResultCallback(this); // Result processed in onResult().

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, "APP REQUIRES PERMISSION");
        }
    }

    /**
     * Registers the geofences stored in GeoStore as Geofences in the
     * GeofencingAPI
     *
     * @param city We will load the artifacts that correspond to this city
     */
    public void loadArtifactGeofences(CityGeofence city) {

        if (currLoadedCity.equals(city.getID())) {
            return;
        } else {
            currLoadedCity = city.getID();
        }

        geoStore.loadArtifacts(city);
        List<Geofence> artifactGeoList = new ArrayList<>();
        for (ArtifactGeofence g : geoStore.getArtifactGeofences()) {
            artifactGeoList.add(g.toGeofence());
        }

        GeofencingRequest artifactReq = new GeofencingRequest.Builder()
                .addGeofences(artifactGeoList)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();

        try {

            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient, artifactReq,
                    getGeofenceTransitionPendingIntent()
            ).setResultCallback(this); // Result processed in onResult().

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            Log.e(TAG, "APP REQUIRES PERMISSION: LOADING ARTIFACTS");
        }

    }


    public void onResult(Status status) {
        if (status.isSuccess()) {

            Log.i(TAG, "RESULT WAS SUCCESS: GEOFENCE REGISTERED");

        } else {

            Log.e(TAG, "ERROR");
        }
    }

    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
//        intent.putExtra(TAG, this);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Get the geoStore objects
     */
    public GeofenceStore getGeoStore() {
        return this.geoStore;
    }

    /**
     * Isnor - I added this so we can talk to the front end. The idea
     * is that we set the currArtifact in transitionIntent and then
     * Front end can access it with getCurrArtifactGeofence()
     *
     * @param currArtifact set the current artifact to currArtifact
     */
    public void setCurrArtifact(ArtifactGeofence currArtifact) {
        artifact = currArtifact;
    }

}