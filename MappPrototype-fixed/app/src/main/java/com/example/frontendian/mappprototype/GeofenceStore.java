package com.example.frontendian.mappprototype;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * @ Authors Reece, James, Milton, Federico
 */
public class GeofenceStore {

    //TAG for Log
    private final String TAG = "GEOFENCE_STORE";
    private Context context = null;

    private ArrayList<CityGeofence> cityGeofences = null;
    private ArrayList<ArtifactGeofence> artifactGeofences = null;
    private CityGeofence currentCity = null;
    private final File OFFLINE_CITIES;
    private File currArtifactGeofences = null;
    private String preferredLanguage = null;
    /**
     * Context will be used for having private fie storage.
     */
    public GeofenceStore(Context context) {
        this.context = context;
        cityGeofences = new ArrayList<>();
        artifactGeofences = new ArrayList<>();
        OFFLINE_CITIES = new File(context.getFilesDir(), Constants.MASTER_FILE_NAME);
        preferredLanguage = Locale.getDefault().getDisplayLanguage();
    }

    /**
     * Goes online and fetches a JSON file, then saves it
     * The JSON file is located at the
     */
    public void loadCities() {

        File onlineCityJF = new File("");

        //this file (offline) is located at "context.getFilesDir(), Constants.MASTER_FILE_NAME"
        //See constructor for more
        boolean offlineExists = OFFLINE_CITIES.exists();

        //TODO: VERSIONING (if offlineExists, check version, etc)
        if (this.isNetworkAvailable()) {




            String json = "";
            try {
                json = this.getCityGeofenceListFromJSONUrl(new URL(Constants.ONLINE_CITY_LIST_URL));
                Log.i(TAG, "Got JSON String: " + json);

                //versioning
                if(currentCity != null) {
                    double currVersion = currentCity.getVersion();
                    String currCityID = currentCity.getID();
                    JSONArray jsonArray = new JSONArray(json);
                    int len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        String id = jsonArray.getJSONObject(i).getString("city");
                        if (id.equals(currCityID)) {
                            //Check what the key is for version date for JSON file
                            double newestVersion = jsonArray.getJSONObject(i).getDouble("version");
                            if (currVersion < newestVersion) {
                                this.loadArtifacts(currentCity);
                            }
                        }
                    }//End Versioning
                }

                this.saveLocally(json);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {

            }


        }
    }

    /**
     * This method loads the artifact geofences for a
     * specific city, and then returns the list.
     *
     * @return ArrayList of Artifact geofences for a city
     */
    public ArrayList<ArtifactGeofence> loadArtifacts(CityGeofence city) {

        currArtifactGeofences = new File(context.getFilesDir(), city.getName());

        //TODO: VERSIONING (if offlineExists, check version, etc)
        if (this.isNetworkAvailable()) {

            String json = "";
            try {
                json = this.getArtifactGeofenceListFromJSONUrl(new URL(Constants.ONLINE_CITY_URL_ROOT
                        + Constants.ONLINE_CITIES_DIR + city.getName()));//cuts off the 'c' TODO: Add catch
                Log.i(TAG, "Got JSON String: " + json);
//                this.saveLocally(json);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.e(TAG, "The URL containing the file was invalid");
                e.printStackTrace();
            }

            //currArtifactGeofences = offline artifacts file for the
            //current loaded city
        } else if (currArtifactGeofences.exists()) {
            //read from file if we have no internet connection
            //and the offline file exists
        }

        this.currentCity = city;
        return this.artifactGeofences;
    }

    /**
     * Reads the given file, (hopefully) a file of
     * city data, and adds it to this object's list
     *
     * @param f The file of city geofence data
     * @throws IOException
     */
    public void getCityGeofenceListFromFile(File f) throws IOException {

        FileReader fileReader = new FileReader(f);

        String json = "";
        int temp;
        //should create a json string
        while ((temp = fileReader.read()) != -1) {
            json += (char) temp;
        }
        fileReader.close();


        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                //Get the contruction data from the JSON file
                String id = jsonArray.getJSONObject(i).getString("city");
                double lat = jsonArray.getJSONObject(i).getDouble("lat");
                double lon = jsonArray.getJSONObject(i).getDouble("lon");

                this.getCities().add(new CityGeofence(id, lat, lon, 100, Geofence.GEOFENCE_TRANSITION_ENTER,
                        "http://www.geofencing.comule.com/cities/sackville.json"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON error in getCityGeofenceListFromFile: " + json);
            e.printStackTrace();
        }
    }

    /**
     * Writes the given string to this app's file, which is
     * (context.getFilesDir(), Constants.MASTER_FILE_NAME)
     *
     * @param toSave the string to write
     */
    private void saveLocally(String toSave) {

        try {
            FileWriter fileWriter = new FileWriter(OFFLINE_CITIES);

            fileWriter.write(toSave);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "SAVED");
    }

    /**
     * A method that takes in a URL object and returns a List of
     * CityGeofences, which it obtains from the JSON file
     * retrieved from the URL.
     * This URL better have ArtifactGeofences or we're out of luck!
     * TODO: Create Exception so that reading non-artifacts in this method raises one
     *
     * @param urlWithGeofencesJSON - Properly formatted URL that ends with .json
     *                             (there is assumed to be a JSON file located at this
     *                             location). Ex. "http://www.geofencing.comule.com/test.json"
     * @return The JSON String
     */
    public String getCityGeofenceListFromJSONUrl(URL urlWithGeofencesJSON)
            throws ExecutionException, InterruptedException {

        //This ASyncTask will get the JSON file in String format, so
        //We can easily convert it to a JSONArray Object
        GetJSONStringFromInternet jsonStringFromInternetGetter = new GetJSONStringFromInternet();
        String json = jsonStringFromInternetGetter.execute(urlWithGeofencesJSON).get();

        Log.i(TAG, json);

        try {

            /*We have a single object with 2 fields:
            //1 for the version, 1 for the list of cities
            JSONObject cityList = new JSONObject(json);
            double cityListVersion = cityList.getDouble("version");
            JSONArray jsonArray = cityList.getJSONArray("cities");
            */
            JSONArray jsonArray = new JSONArray(json);

            //Loop through the array of objects, creating new ArtifactGeofences as we go,
            //Adding them to our list
            for (int i = 0; i < jsonArray.length(); i++) {

                //Get the contruction data from the JSON file
                String id = jsonArray.getJSONObject(i).getString("city");
                double lat = jsonArray.getJSONObject(i).getDouble("lat");
                double lon = jsonArray.getJSONObject(i).getDouble("lon");

                //Construct object and fill in other fields (description, etc).
                CityGeofence newCityGeofence = new CityGeofence(id, lat, lon, 100000, Geofence.GEOFENCE_TRANSITION_ENTER, null);

                //When we pull, store the current version. This will get saved in a file
                //If it needs to be, then after that, we can compare the file's versions
                //to the newly pulled down cities (which are what the below are)
                newCityGeofence.setVersion(jsonArray.getJSONObject(i).getDouble("version"));
                this.cityGeofences.add(newCityGeofence);
            }

        } catch (JSONException e) {
            Log.i(TAG, "There was no JSON file - is something spelled wrong?" +
                    "\n + The String storing the JSON data was parsed wrong - look" +
                    " for tabs or weird characters, make sure the JSON is properly formatted.");
            e.printStackTrace();

        }

        return json;
    }

    /**
     * A method that takes in a URL object and returns a List of
     * ArtifactGeofences, which it obtains from the JSON file
     * retrieved from the URL.
     *
     * @param urlWithGeofencesJSON - Properly formatted URL that ends with .json
     *                             (there is assumed to be a JSON file located at this
     *                             location). Ex. "http://www.geofencing.comule.com/test.json"
     */
    public String getArtifactGeofenceListFromJSONUrl(URL urlWithGeofencesJSON)
            throws ExecutionException, InterruptedException {

        //This ASyncTask will get the JSON file in String format, so
        //We can easily convert it to a JSONArray Object
        GetJSONStringFromInternet jsonStringFromInternetGetter = new GetJSONStringFromInternet();
        String json = jsonStringFromInternetGetter.execute(urlWithGeofencesJSON).get();
        Log.i(TAG, json);

        try {
            //The legend of testing continues this week on: JSON Arrays
            JSONArray jsonArray = new JSONArray(json);

            //Loop through the array of objects, creating new ArtifactGeofences as we go,
            //Adding them to our list
            for (int i = 0; i < jsonArray.length(); i++) {

                //Get the contruction data from the JSON file
                String name = jsonArray.getJSONObject(i).getString("name");
                double lat = jsonArray.getJSONObject(i).getDouble("lat");
                double lon = jsonArray.getJSONObject(i).getDouble("lon");
                String description = jsonArray.getJSONObject(i).getString("description");

                //Construct object and fill in other fields (description, etc).
                ArtifactGeofence newArtifactGeofence = new ArtifactGeofence(name, lat, lon, 100, Geofence.GEOFENCE_TRANSITION_ENTER);
                newArtifactGeofence.getArtifactGeofenceFromJSONObject(jsonArray.getJSONObject(i), preferredLanguage);

                artifactGeofences.add(newArtifactGeofence);
            }

        } catch (JSONException e) {
            Log.i(TAG, "There was no JSON file - is something spelled wrong?" +
                    "\n + The String storing the JSON data was parsed wrong - look" +
                    " for tabs or weird characters, make sure the JSON is properly formatted.");
            e.printStackTrace();

        }

        return json;
    }

    /**
     * Check if network connectivity is available
     *
     * @return true if the app can connect to the internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public ArrayList<CityGeofence> getCities() {
        return this.cityGeofences;
    }

    public ArrayList<ArtifactGeofence> getArtifactGeofences() {
        return artifactGeofences;
    }

    /**
     * Returns the ArtifactGeofence with an ID equal to the given String
     * from this object's list
     *
     * @param ID the ID of the ArtifactGeofence to be returned
     * @return The ArtifactGeofence specified by ID, or null, if such
     * a geofence does not exist in this object's list
     */
    public ArtifactGeofence getArtifactGeofence(String ID) {
        int len = artifactGeofences.size();
        for (int i = 0; i < len; i++) {
            Log.i(TAG,"Artifact ID in Geostore: " + artifactGeofences.get(i).getID());
            if (artifactGeofences.get(i).getID().equals(ID)) {
                return artifactGeofences.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the CityGeofence with an ID equal to the given String
     * from this object's list
     *
     * @param ID the ID of the CityGeofence to be returned
     * @return The CityGeofence specified by ID, or null, if such
     * a geofence does not exist in this object's list
     */
    public CityGeofence getCityGeofence(String ID) {
        int len = cityGeofences.size();
        for (int i = 0; i < len; i++) {
            if (cityGeofences.get(i).getID().equals(ID)) {
                return cityGeofences.get(i);
            }
        }
        return null;
    }

}