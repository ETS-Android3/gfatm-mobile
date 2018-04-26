package com.ihsinformatics.gfatmmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BackupDatabaseActivity  extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected TextView resetButton;
    protected CheckBox encryptDbCheckbox;
    protected LinearLayout credentialsLayout;
    protected EditText username;
    protected EditText password;
    protected EditText expiryPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_db);

        resetButton = (TextView) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        encryptDbCheckbox = (CheckBox) findViewById(R.id.encrypt_db_cb);
        encryptDbCheckbox.setOnCheckedChangeListener(this);
        credentialsLayout = (LinearLayout) findViewById(R.id.credentials_layout);
        username = (EditText) findViewById(R.id.username);
        username.setText(App.getUsername());
        username.setOnKeyListener(null);
        username.setFocusable(false);
        password = (EditText) findViewById(R.id.password);
        expiryPeriod = (EditText) findViewById(R.id.expiry_period);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(2);
        expiryPeriod.setFilters(filters);
        expiryPeriod.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!App.get(expiryPeriod).equals("")) {
                    int expPeriod = Integer.parseInt(App.get(expiryPeriod));
                    if (expPeriod <= 0 || expPeriod > 30)
                        expiryPeriod.setError(getString(R.string.expiry_period_valid_range));
                }

            }
        });

        this.setFinishOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {

        if (v == resetButton) {
            onBackPressed();
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView == encryptDbCheckbox){

            if(encryptDbCheckbox.isChecked())
                credentialsLayout.setVisibility(View.VISIBLE);
            else
                credentialsLayout.setVisibility(View.GONE);

        }

    }
}
