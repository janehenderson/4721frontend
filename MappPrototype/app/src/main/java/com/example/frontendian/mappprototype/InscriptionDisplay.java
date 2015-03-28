package com.example.frontendian.mappprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class InscriptionDisplay extends ActionBarActivity {

    TextView textView1;
    HistoryList<Inscription> histList = HistoryList.getHistoryList();
    Inscription currIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_display);

        // get information passed from history activity
        textView1 = (TextView) findViewById(R.id.inscr_info);
        Intent myIntent = getIntent();

        String nameString = myIntent.getStringExtra("IDstring");

        textView1.setText(nameString);

        currIns = histList.getInscription(nameString);
        


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
