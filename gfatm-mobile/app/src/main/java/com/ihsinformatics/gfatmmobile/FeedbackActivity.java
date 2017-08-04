package com.ihsinformatics.gfatmmobile;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    TextView cancelButton;
    TextView sendButton;
    Spinner feedbacktype;
    EditText description;

    protected static ProgressDialog loading;
    private ServerService serverService;

    private static final int PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        loading = new ProgressDialog(FeedbackActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        serverService = new ServerService(getApplicationContext());

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

            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.feedback_sms_send),
                    Toast.LENGTH_LONG).show();

            description.setText("");
            feedbacktype.setSelection(0,true);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.feedback_sms_error),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        //sendFeedbackToServer();
    }

    private boolean sendFeedbackToServer(){

        String msg = "Feedback by " + App.getUserFullName() + "\n";
        msg = msg + "Feedback type: " + feedbacktype.getSelectedItem().toString() + "\n";
        msg = msg + "Description: " + description.getText().toString();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        final ContentValues values = new ContentValues();

        values.put("location", App.getLocation());
        values.put("entereddate", App.getSqlDateTime(new Date()));

        observations.add(new String[]{"FEEDBACK", msg});

        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
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

                String result = serverService.submitFeedbackToServer("gfatm_feedback", null, values, observations.toArray(new String[][]{}));
                if (result != null && result.contains("SUCCESS"))
                    return "SUCCESS";

                return "ERROR";

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS"))
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.feedback_submitted_successfully),
                            Toast.LENGTH_LONG).show();

                 else

                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.feedback_submitting_error),
                            Toast.LENGTH_LONG).show();


            }
        };
        submissionFormTask.execute("");

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Snackbar.make(findViewById(R.id.description), getResources().getString(R.string.feedback_permission_granted),
                    Snackbar.LENGTH_LONG).show();
            sendSMS();

        } else {

            Snackbar.make(findViewById(R.id.description), getResources().getString(R.string.feedback_permission_denied),
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
                Toast.makeText(this,getResources().getString(R.string.feedback_enter_message), Toast.LENGTH_SHORT).show();
            }else {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                                Snackbar.make(findViewById(R.id.description), getResources().getString(R.string.feedback_sms_permission_needed),
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

                sendFeedbackToServer();

            }
        }
    }
}


