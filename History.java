package com.example.frontendian.mappprototype;

//Much of this code was adapted from http://www.raywenderlich.com/78576/android-tutorial-for-beginners-part-2
// and some from http://chrisrisner.com/31-Days-of-Android--Day-5%E2%80%93Adding-Multiple-Activities-and-using-Intents

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class History extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    TextView mainTextView;
    Button mainButton;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    //ArrayList mHistList = new ArrayList();
    HistoryList mHistList = new HistoryList();
    /**Removing the line below temporarily so that prototype works better for the moment,
     * but will need to make this work so that history info is saved **/
    //HistoryList mHistList = new HistoryList();
    ShareActionProvider mShareActionProvider;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
                mHistList);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        // display old saved history in the list somehow
        if(savedInstanceState != null){
            //this method will need to be uncommented as well once we've figured out the parcelable stuff
            //mHistList = savedInstanceState.getParcelable("theList");
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle outState){
        outState.getParcelableArrayList("key");
        super.onRestoreInstanceState(outState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("theList", mHistList);
        super.onSaveInstanceState(outState);
        //this method will need to be uncommented as well once we've figured out the parcelable stuff
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
        Inscription nextInscription = new Inscription();
        // Also add that value to the list shown in the ListView
        mHistList.addFirst("Inscription " + nextInscription);
        mArrayAdapter.notifyDataSetChanged();
        nextInscription.setCount(nextInscription.getCount()+1);
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
        myIntent.putExtra("IDstring", mHistList.get(position).toString());
        startActivity(myIntent);
    }

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
