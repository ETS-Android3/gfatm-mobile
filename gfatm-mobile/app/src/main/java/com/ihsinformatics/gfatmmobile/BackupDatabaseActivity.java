package com.ihsinformatics.gfatmmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.Backup;
import com.example.backupservice.Params;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;

public class BackupDatabaseActivity extends AppCompatActivity implements View.OnClickListener{
    RadioGroup radioGroup;
    RadioButton radioButton;
    CheckBox checkbox_monthly, checkBox_encrypt;
    Button buttonDone,buttonCancel;
    EditText editTextExpiryDays, editTextPassWord;
    TextView textViewPassword;
    Boolean requiredPassword = false;
    public String TAG = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_database);
        this.setFinishOnTouchOutside(false);
        initView();
        checkBox_encrypt.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }
    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkbox_monthly = (CheckBox) findViewById(R.id.checkBox_monthly);
        checkBox_encrypt = (CheckBox) findViewById(R.id.checkBox_encrypt);
        buttonDone = (Button) findViewById(R.id.button_done);
        buttonCancel=(Button) findViewById(R.id.button_cancel);
        editTextExpiryDays = (EditText) findViewById(R.id.editText_noOfExpiryDays);
        editTextPassWord = (EditText) findViewById(R.id.editText_password);
        textViewPassword = (TextView) findViewById(R.id.password);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_done:
                String Password = editTextPassWord.getText().toString();
                String expiry = editTextExpiryDays.getText().toString();

                if (!(expiry.equals(""))) {
                    if (requiredPassword && Password.equals("")) {
                        Toast.makeText(this, "Please enter Password!", Toast.LENGTH_LONG).show();
                    } else {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);
                        String radioButtonText = (String) radioButton.getText();
                        int expiryDays = Integer.parseInt(expiry);

                        Params params = new Params();
                        params.setDbName(DatabaseUtil.getDbName());
                        params.setStoragePath("//DCIM");
                        params.setNoOfExpiryDays(expiryDays);

                        if (radioButtonText.equals("Daily")) {
                            params.setSchedule(Params.Schedule.DAILY);
                        } else if (radioButtonText.equals("Weekly")) {
                            params.setSchedule(Params.Schedule.WEEKLY);
                        } else if (radioButtonText.equals("Monthly")) {
                            params.setSchedule(Params.Schedule.MONTHLY);
                        }
                        if (checkbox_monthly.isChecked()) {
                            params.setKeepMonthlyBackup(true);
                        } else {
                            params.setKeepMonthlyBackup(false);
                        }
                        if (checkBox_encrypt.isChecked()) {
                            params.setEncryptDB(true);
                            params.setPassword(Password);
                        } else {
                            params.setEncryptDB(false);
                            params.setPassword("");
                        }
                        Backup backup = new Backup(this);
                        backup.setupService(params);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Please enter expiry days!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.checkBox_encrypt:
                if (checkBox_encrypt.isChecked()) {
                    textViewPassword.setVisibility(View.VISIBLE);
                    editTextPassWord.setVisibility(View.VISIBLE);
                    requiredPassword = true;
                } else {
                    textViewPassword.setVisibility(View.INVISIBLE);
                    editTextPassWord.setVisibility(View.INVISIBLE);
                    requiredPassword = false;
                }
                break;
        }
    }
}
