package com.example.frontendian.mappprototype;

import android.media.Image;

import com.google.android.gms.location.Geofence;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @ Authors Reece, James, Milton, Federico
 */
public class ArtifactGeofence {

    private final String TAG = "ARTIFACT_GEOFENCE";
    //Instance variables for building the Geofence correctly
    private final String artifactID;
    private final double artifactLatitude;
    private final double artifactLongitude;
    private final float artifactRadius;
    //Would we need this? Any reason not to have it set to NEVER_EXPIRE?
    //private final long artifactExpirationDuration;
    private int transitionType;

    //Instance variables for the Artifact
    private String artifactText;
    private String translation;
    private String locationDescription;
    private Image artifactImg;
    private String name;


    /**
     * @param name         The Artifact's name
     * @param latitude   Latitude of the Geofence's center (degrees)
     * @param longitude  Longitude of the Geofence's center (degrees)
     * @param radius     Radius of the Geofence (meters)
     * @param transition Type of Geofence transition
     */

    public ArtifactGeofence(String name, double latitude, double longitude, float radius, int transition) {
        this.name = name;
        artifactID = "l" + name;
        artifactLatitude = latitude;
        artifactLongitude = longitude;
        artifactRadius = radius;
        transitionType = transition;
    }

    public void setArtifactText(String str) {
        artifactText = str;
    }

    public void setTranslation(String str) {
        translation = str;
    }

    public void setLocationDescription(String str) {
        locationDescription = str;
    }

    public void setArtifactImg(Image img) {
        artifactImg = img;
    }


    //Getters for instance variables
    public String getID() {
        return artifactID;
    }

    public double getLatitude() {
        return artifactLatitude;
    }

    public double getLongitude() {
        return artifactLongitude;
    }

    public float getRadius() {
        return artifactRadius;
    }

    public int getTransitionType() {
        return transitionType;
    }

    public String getTranslation() { return translation; }
    public String getName() {
        return name;
    }

    public String getArtifactText() {
        return artifactText;
    }


    /**
     * Fills in some info from a JSON object
     *
     * @param jOBject JSON object to get text from
     * @throws JSONException
     */
    public void getArtifactGeofenceFromJSONObject(JSONObject jOBject, String preferredLanguage) throws JSONException {

        //this'll break I'd say - Isnor
        setArtifactText(jOBject.getString("text"));
        setTranslation(jOBject.getJSONObject("translation").getString(preferredLanguage));
        setLocationDescription(jOBject.getString("description"));
//        setArtifactImg(null); - We don't do this yet
    }

    /**
     * Create Geofence from ArtifactGeofence
     *
     * @return Geofence Object
     */
    public Geofence toGeofence() {
        return new Geofence.Builder()
                .setRequestId(artifactID)
                .setTransitionTypes(transitionType)
                .setCircularRegion(artifactLatitude, artifactLongitude, artifactRadius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }
}
