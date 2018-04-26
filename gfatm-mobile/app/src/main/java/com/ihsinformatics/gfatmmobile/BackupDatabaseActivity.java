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
import android.widget.Toast;

public class BackupDatabaseActivity  extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected TextView resetButton;
    protected CheckBox encryptDbCheckbox;
    protected LinearLayout credentialsLayout;
    protected EditText username;
    protected EditText password;
    protected EditText expiryPeriod;
    protected TextView backupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_db);

        resetButton = (TextView) findViewById(R.id.cancelButton);
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
        backupButton = (TextView) findViewById(R.id.backupButton);
        backupButton.setOnClickListener(this);


        this.setFinishOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {

        if (v == resetButton) {
            onBackPressed();
        } else if (v == backupButton){
            if(validate()){
                Toast.makeText(getApplicationContext(), "Your toast message",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public boolean validate(){

        Boolean flag = true;

        if(App.get(expiryPeriod).equals("")) {
            expiryPeriod.requestFocus();
            expiryPeriod.setError(getString(R.string.empty_field));
            flag = false;
        } else {
            int expPeriod = Integer.parseInt(App.get(expiryPeriod));
            if (expPeriod <= 0 || expPeriod > 30) {
                expiryPeriod.requestFocus();
                expiryPeriod.setError(getString(R.string.expiry_period_valid_range));
                flag = false;
            }
        }

        if(encryptDbCheckbox.isChecked()){

            if(App.get(password).equals("")){
                password.requestFocus();
                password.setError(getString(R.string.empty_field));
                flag = false;
            }else {
                if(!App.get(password).equals(App.getPassword())){
                    password.requestFocus();
                    password.setError(getString(R.string.invalid_password));
                    flag = false;
                }
            }

        }

        return flag;

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
