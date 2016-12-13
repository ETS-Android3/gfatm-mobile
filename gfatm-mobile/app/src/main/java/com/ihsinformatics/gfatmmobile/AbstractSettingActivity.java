package com.ihsinformatics.gfatmmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public abstract class AbstractSettingActivity extends AppCompatActivity implements View.OnClickListener {

    protected RadioGroup radioGroup;
    protected TextView okButton;
    protected TextView resetButton;
    protected LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        layout = (LinearLayout) findViewById(R.id.layout);

        resetButton = (TextView) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        resetButton.setVisibility(View.GONE);
        okButton = (TextView) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);

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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {

        if(v == okButton){
            onBackPressed();
        }

    }
}
