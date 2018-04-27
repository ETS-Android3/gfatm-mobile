package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.Backup;
import com.example.backupservice.Params;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;

public class BackupDatabaseActivity  extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected TextView resetButton;
    protected CheckBox encryptDbCheckbox;
    protected LinearLayout credentialsLayout;
    protected EditText username;
    protected EditText password;
    protected TextView backupButton;

    protected static ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_db);

        loading = new ProgressDialog(BackupDatabaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);

        resetButton = (TextView) findViewById(R.id.cancelButton);
        resetButton.setOnClickListener(this);
        resetButton.setTextColor(Color.GRAY);
        encryptDbCheckbox = (CheckBox) findViewById(R.id.encrypt_db_cb);
        encryptDbCheckbox.setOnCheckedChangeListener(this);
        credentialsLayout = (LinearLayout) findViewById(R.id.credentials_layout);
        username = (EditText) findViewById(R.id.username);
        username.setText(App.getUsername());
        username.setOnKeyListener(null);
        username.setFocusable(false);
        password = (EditText) findViewById(R.id.password);
        backupButton = (TextView) findViewById(R.id.backupButton);
        backupButton.setOnClickListener(this);


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
                backupNow();
            }
        }

    }

    public void backupNow(){

        String Password = App.get(password);

        final Params backupParams = new Params();
        backupParams.setDbName(DatabaseUtil.getDbName());
        backupParams.setStoragePath("//DCIM");
        backupParams.setKeepMonthlyBackup(false);

        if (encryptDbCheckbox.isChecked()) {
            backupParams.setEncryptDB(true);
            backupParams.setPassword(Password);
        } else {
            backupParams.setEncryptDB(false);
            backupParams.setPassword("");
        }

        AsyncTask<String, String, Boolean> dbBackupTask = new AsyncTask<String, String, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                Backup backup = new Backup(getApplicationContext());
                Boolean flag = backup.takeBackupNow(backupParams);

                return flag;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                loading.dismiss();

                try {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(credentialsLayout.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (result) {
                    finish();
                    Toast.makeText(getApplicationContext(), getString(R.string.backup_success), Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

            }
        };
        dbBackupTask.execute("");



    }

    public boolean validate(){

        Boolean flag = true;

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
