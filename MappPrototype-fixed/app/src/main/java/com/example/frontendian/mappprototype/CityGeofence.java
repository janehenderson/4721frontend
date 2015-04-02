package com.example.frontendian.mappprototype;

/**
 * @ Authors Reece, James, Milton, Federico
 */
public class CityGeofence extends GeneralGeofence {

    private String jsonURL;
    private double version;

    /**
     * @param ID         The Artifact's request ID
     * @param latitude   Latitude of the Geofence's center (degrees)
     * @param longitude  Longitude of the Geofence's center (degrees)
     * @param radius     Radius of the Geofence (meters)
     * @param transition Type of Geofence transition
     * @param jsonURL    String URL to json file with all artifacts for this city
     */
    public CityGeofence(String ID, double latitude, double longitude, float radius, int transition, String jsonURL) {
        super("c" + ID, latitude, longitude, radius, transition);
        this.jsonURL = jsonURL;
    }

    public String getJsonURL() {
        return jsonURL;
    }

    public void setJsonURL(String jsonURL) {
        this.jsonURL = jsonURL;
    }

    public void setCityJsonURL(String cityJsonURL) {
        this.jsonURL = cityJsonURL;
    }

    public double getVersion() {
        return version;
    }

    public String getName() { return super.getID().substring(1); }

    public void setVersion(double lasModified) {
        this.version = lasModified;
    }
}
