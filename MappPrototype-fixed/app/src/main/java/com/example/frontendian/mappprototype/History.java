package com.example.frontendian.mappprototype;

//Some of this code was adapted from http://www.raywenderlich.com/78576/android-tutorial-for-beginners-part-2
// and some from http://chrisrisner.com/31-Days-of-Android--Day-5%E2%80%93Adding-Multiple-Activities-and-using-Intents

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class History extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //Front end code
    private final String TAG = "HISTORY";
    private BackendController backendController = null;
    ArrayList mNameList = new ArrayList();

    //Our code
    TextView mainTextView;
    Button mainButton;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    GeoHandler mGeohandler;

    //ArrayList mHistList = new ArrayList();
    //LinkedList mHistList = new LinkedList();
    /**Removing the line below temporarily so that prototype works better for the moment,
     * but will need to make this work so that history info is saved **/
    HistoryList mHistList = HistoryList.getHistoryList();

    LinkedList<String> inscriptionNameList = new LinkedList<String>();

    ShareActionProvider mShareActionProvider;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info", "did we enter onCreate");
        // display old saved history in the list somehow
        if(savedInstanceState != null){
            //this method will need to be uncommented as well once we've figured out the parcelable stuff
            //mHistList = savedInstanceState.getParcelable("theList");
            ArrayList inscriptionNameArray = savedInstanceState.getStringArrayList("key");
            inscriptionNameList = new LinkedList(Arrays.asList(inscriptionNameArray));
            Log.i("info","This is inscription list: " + inscriptionNameList.toString());

        }


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_history);
        LinearLayout layout = (LinearLayout) findViewById(R.id.MainLayout);
        layout.setBackgroundColor(Color.rgb(62, 58, 110));


        // 1. Access the TextView defined in layout XML
        // and then set its text
        mGeohandler= new GeoHandler();
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
        //mArrayAdapter = new ArrayAdapter(this,
          //      android.R.layout.simple_list_item_1,
            //    mHistList);
        mArrayAdapter = new ArrayAdapter(this, R.layout.custom_simple, inscriptionNameList);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        /////Testing notifications!!!!/////
        LocalBroadcastManager.getInstance(this).registerReceiver(mGeohandler, new IntentFilter("Local_Geofence"));
    }


    @Override
    public void onRestoreInstanceState(Bundle outState){
        outState.getParcelableArrayList("key");

        super.onRestoreInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //outState.putParcelable("theList", mHistList);
        //outState.putParcelableArrayList("key", mHistList);

        Log.i("info","Do we enter save instance state");

        super.onSaveInstanceState(outState);

        ArrayList<String> arrayInscriptionNameList = new ArrayList<String>();
        for (String name : inscriptionNameList) {
            arrayInscriptionNameList.add(name);
        }
        outState.putStringArrayList("key", arrayInscriptionNameList);


        //this method will need to be uncommented as well once we've figured out the parcelable stuff
        //outState.putParcelable("theList", mHistList);

        FileInputStream fileInputStream;
        try {
            fileInputStream = openFileInput("HistList");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Object> histList = (ArrayList<Object>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        // Test the Button
        mainTextView.setText("Button pressed!");
       // Inscription nextInscription = new Inscription();
        // Also add that value to the list shown in the ListView
        //More stuff than in bakHistr
        inscriptionNameList.addFirst("Name " + counter);
        mHistList.add(new Inscription("Name " + counter, "badText", "badTrans"));

        mArrayAdapter.notifyDataSetChanged();
        counter++;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Log the item's position and contents
        // to the console in Debug
        //Log.d("mapp history", position + ": " + mHistList.get(position));

        //On click, open the individual inscription view
        Intent myIntent = new Intent(getApplicationContext(), InscriptionDisplay.class);
        // pass information about which item selected to inscription display activity
        //myIntent.putExtra("IDstring", mHistList.get(position).toString());
        myIntent.putExtra("IDstring", inscriptionNameList.get(position).toString());
        startActivity(myIntent);
    }

    /**
    public ArrayList<Object> readFromInternalStorage() {
        ArrayList<Object> histList;
        FileInputStream fileInputStream;
        try {
            fileInputStream = openFileInput("HistList");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            histList = (ArrayList<Object>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
            histList = null;
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
            histList = null;
        }  catch (ClassNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
            histList = null;
        }
        return histList;
    }
*/
    /**@Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
