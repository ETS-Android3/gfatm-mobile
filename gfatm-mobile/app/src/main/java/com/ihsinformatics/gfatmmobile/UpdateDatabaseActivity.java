package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.Calendar;

/**
 * A login screen that offers login via email/password.
 */
public class UpdateDatabaseActivity extends AppCompatActivity implements View.OnTouchListener {

    protected static ProgressDialog loading;
    private ServerService serverService;

    protected TextView syncText;

    CheckBox personAttributeCheckBox;
    TextView personAttributeLastUpdateTextView;
    TextView personAttributeLastUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_database);

        loading = new ProgressDialog(UpdateDatabaseActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        serverService = new ServerService(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        syncText = (TextView) findViewById(R.id.sync);

        personAttributeCheckBox = (CheckBox) findViewById(R.id.person_attribute);
        personAttributeLastUpdateTextView = (TextView) findViewById(R.id.person_attribute_last_update_text);
        personAttributeLastUpdate = (TextView) findViewById(R.id.person_attribute_last_update);

        String lastUpdate =  App.getPersonAttributeLastUpdate();
        if(lastUpdate == null || lastUpdate.equals("")){
            personAttributeLastUpdateTextView.setVisibility(View.GONE);
            personAttributeLastUpdate.setVisibility(View.GONE);
        } else {
            personAttributeLastUpdateTextView.setVisibility(View.VISIBLE);
            personAttributeLastUpdate.setVisibility(View.VISIBLE);
            personAttributeLastUpdate.setText(lastUpdate);
        }

        syncText.setOnTouchListener(this);


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

    public void syncPersonAttributes() {
        AsyncTask<String, String, String> syncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.fetching_locations));
                        loading.show();
                    }
                });

                String result = serverService.getPersonAttributeTypes();
                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                if (result.equals("SUCCESS")) {

                    Calendar calendar = Calendar.getInstance();
                    App.setPersonAttributeLastUpdate(DateFormat.format("dd-MMM-yyyy HH:mm:ss", calendar).toString());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.PERSON_ATTRIBUTE_LAST_UPDATE, App.getPersonAttributeLastUpdate());
                    editor.apply();

                    String lastUpdate =  App.getPersonAttributeLastUpdate();
                    personAttributeLastUpdateTextView.setVisibility(View.VISIBLE);
                    personAttributeLastUpdate.setVisibility(View.VISIBLE);
                    personAttributeLastUpdate.setText(lastUpdate);

                }
            }
        };
        syncTask.execute("");
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                TextView view = (TextView) v;
                view.setTextColor(getResources().getColor(R.color.dark_grey));
                view.invalidate();

                if(personAttributeCheckBox.isChecked()){
                    syncPersonAttributes();
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                TextView view = (TextView) v;
                //clear the overlay
                int color1 = App.getColor(this, R.attr.colorAccent);
                view.setTextColor(color1);
                view.invalidate();
                break;
            }
        }
        return true;
    }
}


