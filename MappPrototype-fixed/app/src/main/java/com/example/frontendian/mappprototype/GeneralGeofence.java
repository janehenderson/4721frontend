package com.example.frontendian.mappprototype;

import com.google.android.gms.location.Geofence;

/**
 * @ Authors Reece, James, Milton, Federico
 */
public class GeneralGeofence {
    //Instance variables for building the Geofence correctly
    protected final String iD;
    protected final double latitude;
    protected final double longitude;
    protected final float radius;
    //Would we need this? Any reason not to have it set to NEVER_EXPIRE?
    //private final long artifactExpirationDuration;
    protected int transitionType;

    public GeneralGeofence(String iD, double latitude, double longitude, float radius, int transitionType) {
        this.iD = iD;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.transitionType = transitionType;
    }


    public String getID() {
        return iD;
    }

    public Geofence toGeofence() {
        return new Geofence.Builder()
                .setRequestId(iD)
                .setLoiteringDelay(100)
                .setTransitionTypes(transitionType)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }
}
