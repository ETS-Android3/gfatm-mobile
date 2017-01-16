package com.ihsinformatics.gfatmmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class UpdateDatabaseActivity extends AppCompatActivity {

    CheckBox locationCheckBox;
    TextView locationLastUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_database);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationCheckBox = (CheckBox) findViewById(R.id.location);
        locationLastUpdate = (TextView) findViewById(R.id.location_last_update);


        locationLastUpdate.setText(App.getLocationLastUpdate());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


