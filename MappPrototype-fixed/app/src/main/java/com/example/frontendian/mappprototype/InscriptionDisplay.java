package com.example.frontendian.mappprototype;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;


public class InscriptionDisplay extends ActionBarActivity {

    TextView nameTV;
    TextView translationTV;
    TextView textTV;
    HistoryList histList = HistoryList.getHistoryList();
    Inscription currIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_display);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.DisplayLayout);
        layout2.setBackgroundColor(Color.rgb(62, 58, 110));
        // get information passed from history activity
        nameTV = (TextView) findViewById(R.id.inscr_name);
        translationTV = (TextView) findViewById(R.id.inscr_translation);
        textTV = (TextView) findViewById(R.id.inscr_text);

        Intent myIntent = getIntent();
        nameTV.setText("name");

        translationTV.setText("translation");

        textTV.setText("text");
        String nameString = myIntent.getStringExtra("IDstring");

        currIns =  histList.getInscription(nameString);
        Log.i("DISPLAY", "Got the Inscription via name: "+nameString);
        if(currIns!=null) {
            String tranString = currIns.getTrans();
            String textString = currIns.getText();
            nameTV.setText(nameString);
            translationTV.setText(tranString);
            textTV.setText(textString);
        }
        else{

            Log.i("IncsriptionDisplay", "Got a null inscription");

        }
        Log.i("IncsriptionDisplay", "Successful display");





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
