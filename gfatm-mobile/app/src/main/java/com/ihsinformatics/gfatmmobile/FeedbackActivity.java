package com.ihsinformatics.gfatmmobile;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    TextView cancelButton;
    TextView sendButton;
    Spinner feedbacktype;
    EditText description;
    private static final int PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        cancelButton = (TextView) findViewById(R.id.cancelButton);
        sendButton = (TextView) findViewById(R.id.sendButton);
        feedbacktype = (Spinner) findViewById(R.id.feedbacktype);
        description =  (EditText) findViewById(R.id.description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cancelButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

    }

    private void sendSMS() {
        String phoneNumber = App.getSupportContact();

        String msg = "Feedback by " + App.getUserFullName() + "\n";
        msg = msg + "Feedback type: " + feedbacktype.getSelectedItem().toString() + "\n";
        msg = msg + "Description: " + description.getText().toString();

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, msg, null, null);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Snackbar.make(findViewById(R.id.description), "Permission Granted",
                    Snackbar.LENGTH_LONG).show();
            sendSMS();

        } else {

            Snackbar.make(findViewById(R.id.description), "Permission denied",
                    Snackbar.LENGTH_LONG).show();

        }
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


    @Override
    public void onClick(View v) {
        if (v == cancelButton) {
            feedbacktype.setSelection(0);
            description.setText("");
        } else if (v == sendButton) {

            if(description.getText().toString().equals("")){
                Toast.makeText(this,"Please enter your message to continue.", Toast.LENGTH_SHORT).show();
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                            Snackbar.make(findViewById(R.id.description), "You need to grant SEND SMS permission to send sms",
                                    Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                                }
                            }).show();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                        }
                    } else {
                        sendSMS();
                    }
                } else {
                    sendSMS();
                }
            }
        }
    }
}


