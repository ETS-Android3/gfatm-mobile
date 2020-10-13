package com.ihsinformatics.gfatmmobile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.Backup;
import com.example.backupservice.Params;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;

import java.io.File;

public class ScheduleBackupActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected TextView resetButton;
    protected CheckBox encryptDbCheckbox;
    protected LinearLayout credentialsLayout;
    protected EditText username;
    protected EditText password;
    protected EditText expiryPeriod;
    protected TextView backupButton;
    protected RadioButton dailyRadioButton;
    protected RadioButton weeklyRadioButton;
    protected RadioButton monthlyRadioButton;
    protected LinearLayout monthLayout;
    protected LinearLayout weekLayout;
    protected RadioGroup frequencyRadioGroup;
    protected Spinner time;
    protected Spinner weekPeriod;

    protected static ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_backup);

        loading = new ProgressDialog(ScheduleBackupActivity.this, ProgressDialog.THEME_HOLO_LIGHT);

        resetButton = (TextView) findViewById(R.id.cancelButton);
        resetButton.setOnClickListener(this);
        resetButton.setTextColor(Color.GRAY);
        encryptDbCheckbox = (CheckBox) findViewById(R.id.encrypt_db_cb);
        encryptDbCheckbox.setClickable(false);
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
        expiryPeriod.setText(App.getExpiryPeriod());
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
        dailyRadioButton = (RadioButton) findViewById(R.id.daily);
        weeklyRadioButton = (RadioButton) findViewById(R.id.weekly);
        monthlyRadioButton = (RadioButton) findViewById(R.id.monthly);
        monthLayout = (LinearLayout) findViewById(R.id.month_layout);
        weekLayout = (LinearLayout) findViewById(R.id.week_layout);

        frequencyRadioGroup = (RadioGroup) findViewById(R.id.schedule);
        frequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                if(checkedRadioButton == dailyRadioButton) {
                    weekLayout.setVisibility(View.GONE);
                    monthLayout.setVisibility(View.GONE);
                }
                else if(checkedRadioButton == weeklyRadioButton) {
                    weekLayout.setVisibility(View.VISIBLE);
                    monthLayout.setVisibility(View.GONE);
                }
                else if(checkedRadioButton == monthlyRadioButton) {
                    weekLayout.setVisibility(View.GONE);
                    monthLayout.setVisibility(View.VISIBLE);
                }
            }
        });

       if(App.getBackupFrequency().equals(getString(R.string.daily)))
            dailyRadioButton.setChecked(true);
        else if(App.getBackupFrequency().equals(getString(R.string.weekly))) {
            weeklyRadioButton.setChecked(true);
        }
        else if(App.getBackupFrequency().equals(getString(R.string.monthly))) {
            monthlyRadioButton.setChecked(true);
        }
        time = (Spinner) findViewById(R.id.time);
        String timeValue = App.getBackupTime();
        time.setSelection(Integer.valueOf(timeValue)-1);
        weekPeriod = (Spinner) findViewById(R.id.week_period);
        if(weekLayout.getVisibility() == View.VISIBLE) {
            String dayValue = App.getBackupDay();
            weekPeriod.setSelection(Integer.valueOf(dayValue)-1);
        }

        this.setFinishOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {

        if (v == resetButton) {
            try {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(credentialsLayout.getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            onBackPressed();
        } else if (v == backupButton){
            if(validate()){
                scdeuleBackup();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            scdeuleBackup();
        } else
        {
            Toast.makeText(ScheduleBackupActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
        }

    }

    public void scdeuleBackup(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            return;
        }

        File path = new File(Environment.getExternalStorageDirectory(), "GFATM-BACKUP");
        path.mkdirs();
        MediaScannerConnection.scanFile(this, new String[] {path.toString()}, null, null);

        String Password = App.get(password);
        String expiry = App.get(expiryPeriod);
        App.setExpiryPeriod(expiry);
        int expiryDays = Integer.parseInt(expiry);

        final Params backupParams = new Params();
        backupParams.setDbName(DatabaseUtil.getDbName());
        backupParams.setStoragePath("//GFATM-BACKUP");
        backupParams.setNoOfExpiryDays(expiryDays);

        if(dailyRadioButton.isChecked()) {
            backupParams.setSchedule(Params.Schedule.DAILY);
            App.setBackupFrequency(getString(R.string.daily));
        }
        else if(weeklyRadioButton.isChecked()) {
            backupParams.setSchedule(Params.Schedule.WEEKLY);
            App.setBackupFrequency(getString(R.string.weekly));
        }
        else if(monthlyRadioButton.isChecked()) {
            backupParams.setSchedule(Params.Schedule.MONTHLY);
            App.setBackupFrequency(getString(R.string.monthly));
        }

        backupParams.setKeepMonthlyBackup(false);

        if (encryptDbCheckbox.isChecked()) {
            backupParams.setEncryptDB(true);
            backupParams.setPassword(Password);
        } else {
            backupParams.setEncryptDB(false);
            backupParams.setPassword("");
        }

        String text = time.getSelectedItem().toString();
        text = text.replace(":00","");
        backupParams.setTime(Integer.parseInt(text));
        App.setBackupTime(text);

        if(weekLayout.getVisibility() == View.VISIBLE){

            text = weekPeriod.getSelectedItem().toString();
            if(text.equals("MON")) {
                backupParams.setDay(1);
                App.setBackupDay("1");
            }
            else if(text.equals("TUES")) {
                backupParams.setDay(2);
                App.setBackupDay("2");
            }
            else if(text.equals("WED")) {
                backupParams.setDay(3);
                App.setBackupDay("3");
            }
            else if(text.equals("THURS")) {
                backupParams.setDay(4);
                App.setBackupDay("4");
            }
            else if(text.equals("FRI")) {
                backupParams.setDay(5);
                App.setBackupDay("5");
            }
            else if(text.equals("SAT")) {
                backupParams.setDay(6);
                App.setBackupDay("6");
            }
        } else backupParams.setDay(0);

        Backup backup = new Backup(getApplicationContext());
        backup.setupService(backupParams);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ScheduleBackupActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preferences.BACKUP_FRQUENCY, App.getBackupFrequency());
        editor.putString(Preferences.BACKUP_TIME, App.getBackupTime());
        editor.putString(Preferences.BACKUP_DAY, App.getBackupDay());
        editor.putString(Preferences.EXPIRY_PERIOD, App.getExpiryPeriod());
        editor.apply();

        try {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(credentialsLayout.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        finish();

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
