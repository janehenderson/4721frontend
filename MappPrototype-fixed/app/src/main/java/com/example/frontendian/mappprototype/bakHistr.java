package com.example.frontendian.mappprototype;

//Much of this code was adapted from http://www.raywenderlich.com/78576/android-tutorial-for-beginners-part-2
// and some from http://chrisrisner.com/31-Days-of-Android--Day-5%E2%80%93Adding-Multiple-Activities-and-using-Intents

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class bakHistr extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        Observer {

    private final String TAG = "HISTORY";
    private BackendController backendController = null;
    TextView mainTextView;
    Button mainButton;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("BEC"));
        //Isnor - Initialize the backendController
        if (backendController == null) {
            backendController = new BackendController(this);
        }
        Log.i(TAG, "CREATED HISTORY");
        // 1. Access the TextView defined in layout XML
        // and then set its text
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainTextView.setText("Set in Java!");
        // 2. Access the Button defined in layout XML
        // and listen for it here
        mainButton = (Button) findViewById(R.id.first_switch);
        mainButton.setOnClickListener(this);

        // 4. Access the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);

        // Create an ArrayAdapter for the ListView
        // change the layout (second argument) to a customized layout when needed
        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                mNameList);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String currGeofenceID = intent.getStringExtra(Constants.ID);
            Log.i(TAG + " RCV", "Got message: " + currGeofenceID);
            //Code from onCLick()
            if(currGeofenceID.substring(0,1).equals("l")) {


                mainTextView.setText("Entered: " + currGeofenceID);
                //These two lines add an entry to the list
                mNameList.add(currGeofenceID);
                mArrayAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "Local_Geofence".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("Local_Geofence"));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // Test the Button
        mainTextView.setText("Button pressed!");

        // Also add that value to the list shown in the ListView
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Log the item's position and contents
        // to the console in Debug
        //Log.d("mapp history", position + ": " + mNameList.get(position));

        //On click, open the individual inscription view
        Intent myIntent = new Intent(getApplicationContext(), InscriptionDisplay.class);
        // pass information about which item selected to inscription display activity
        myIntent.putExtra("Geofence city ID:", backendController.getGeoStore().getCities().get(position).getID()
                + ": Information about Sackville will go here.");

        startActivity(myIntent);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link java.util.Observable} object.
     * @param data       the data passed to {@link java.util.Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {

        Log.i(TAG, "UPDATING FRONT END");
        for (CityGeofence city : backendController.getGeoStore().getCities()) {
            mNameList.add(city.getID());
            mArrayAdapter.notifyDataSetChanged();
        }


    }

    /**@Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
    return true;
    }

    return super.onOptionsItemSelected(item);
    }*/
}
