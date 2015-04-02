package com.example.frontendian.mappprototype;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @ Authors Reece, James, Milton, Federico
 */
public class GetJSONStringFromInternet extends AsyncTask<URL, Void, String> {

    private final String TAG = "GET_JSON_STR_FROM_INET";

    @Override
    protected String doInBackground(URL... urls) {
        //Create new geofence store
        String result = "";
        try {

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
            String str;
            while ((str = in.readLine()) != null) {

                //write to file on phone
                result = result.concat(str);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "I/O error: file DNE");
        }

        return result;
    }

}
