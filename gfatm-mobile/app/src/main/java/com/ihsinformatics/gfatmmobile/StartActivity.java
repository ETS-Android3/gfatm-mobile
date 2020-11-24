package com.ihsinformatics.gfatmmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;
import com.ihsinformatics.gfatmmobile.util.OfflineFormSyncService;
import com.ihsinformatics.gfatmmobile.util.OnlineFormSyncService;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Rabbia on 11/15/2016.
 */

public class StartActivity extends Activity {

    private static DatabaseUtil dbUtil;

    private Context context;
    private TextView progressTextView;

    public static void resetPreferences(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        App.setProgram(preferences.getString(Preferences.PROGRAM, ""));
        App.setTheme(preferences.getString(Preferences.THEME, ""));
        App.setLanguage(preferences.getString(Preferences.LANGUAGE, ""));
        App.setMode(preferences.getString(Preferences.MODE, ""));
        App.setSupportContact(preferences.getString(Preferences.SUPPORT_CONTACT, ""));
        App.setSupportEmail(preferences.getString(Preferences.SUPPORT_EMAIL, ""));
        App.setCity(preferences.getString(Preferences.CITY, ""));
        App.setCountry(preferences.getString(Preferences.COUNTRY, ""));
        App.setProvince(preferences.getString(Preferences.PROVINCE, ""));
        App.setDistrict(preferences.getString(Preferences.DISTRICT, ""));
        App.setProvince(preferences.getString(Preferences.PROVINCE, ""));
        App.setIp(preferences.getString(Preferences.IP, ""));
        App.setPort(preferences.getString(Preferences.PORT, ""));
        App.setSsl(preferences.getString(Preferences.SSL_ENCRYPTION, ""));
        App.setUsername(preferences.getString(Preferences.USERNAME, ""));
        App.setPassword(preferences.getString(Preferences.PASSWORD, ""));
        App.setAutoLogin(preferences.getString(Preferences.AUTO_LOGIN, ""));
        App.setLastLogin(preferences.getString(Preferences.LAST_LOGIN, ""));
        App.setCommunicationMode(preferences.getString(Preferences.COMMUNICATION_MODE, ""));
        App.setUserFullName(preferences.getString(Preferences.USER_FULLNAME, ""));
        App.setLocation(preferences.getString(Preferences.LOCATION, ""));
        App.setLocationLastUpdate(preferences.getString(Preferences.LOCATION_LAST_UPDATE, ""));
        App.setPatientId(preferences.getString(Preferences.PATIENT_ID, ""));
        App.setRoles(preferences.getString(Preferences.ROLES, ""));
        App.setProviderUUid(preferences.getString(Preferences.PROVIDER_UUID, ""));
        App.setPrivileges(preferences.getString(Preferences.PRIVILEGES, ""));
        App.setPersonAttributeLastUpdate(preferences.getString(Preferences.PERSON_ATTRIBUTE_LAST_UPDATE, ""));
        String dateInString = preferences.getString(Preferences.LAST_ACTIVITY, "");
        if(dateInString.equals(""))
            App.setPersonAttributeLastUpdate(preferences.getString(Preferences.LAST_ACTIVITY, ""));
        else {
            Date date = App.stringToDate(dateInString, "yyyy-MM-dd HH:mm:ss");
            App.setLastActivity(date);
            App.setPersonAttributeLastUpdate(preferences.getString(Preferences.LAST_ACTIVITY, ""));
        }
        String frequency = preferences.getString(Preferences.BACKUP_FRQUENCY, context.getString(R.string.daily));
        App.setBackupFrequency(frequency);

        frequency = preferences.getString(Preferences.BACKUP_DAY, "5");
        App.setBackupDay(frequency);

        frequency = preferences.getString(Preferences.BACKUP_TIME, "23");
        App.setBackupTime(frequency);

        frequency = preferences.getString(Preferences.EXPIRY_PERIOD, "2");
        App.setExpiryPeriod(frequency);

        ServerService serverService = new ServerService(context);
        com.ihsinformatics.gfatmmobile.model.Patient patient = serverService.getPatientBySystemIdFromLocalDB(App.getPatientId());
        App.setPatient(patient);

        //Locale locale = new Locale("en");

        String language = preferences.getString(Preferences.LANGUAGE, "en");
        if(language == null)
            language = "en";
        else if(language == "")
            language = "en";

        Locale locale = new Locale(language.toLowerCase().substring(0, 2));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        App.setCurrentLocale(locale);

        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        App.setVersion(version);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // on Application start

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressTextView = (TextView) findViewById(R.id.progressLabel);

        new PrefetchData().execute();
    }

    public void setProgress(String progressLabel) {
        progressTextView.setText(progressLabel);
    }


    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            StartActivity.resetPreferences(context);      //loading preferences

            if(App.getIp().equalsIgnoreCase("202.63.219.11")){

                App.setIp(getResources().getString(R.string.ip_default));
                App.setPort(getResources().getString(R.string.port_default));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.IP, App.getIp());
                editor.putString(Preferences.PORT, App.getPort());
                editor.apply();

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgress(getResources().getString(R.string.saving_trees));
                }
            });

            try {

                dbUtil = new DatabaseUtil(context);
                Boolean flag = dbUtil.doesDatabaseExist();
                if (!flag) {
                    dbUtil.buildDatabase(false);            // build sql lite db in app memory
                }
                else{
                    ServerService serverService = new ServerService(context);
                    Boolean f = serverService.checkMetadata();
                    if(!serverService.checkMetadata())
                        dbUtil.buildDatabase(true);
                    else
                        dbUtil.getWritableDatabase();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Check if Login needed...
            ServerService service = new ServerService(context);

            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String v = App.getLastLogin();
            if(App.getLastActivity() != null) {

                Date lastActivity = App.getLastActivity();
                Date currentTime = Calendar.getInstance().getTime();

                long diff = currentTime.getTime() - lastActivity.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;

                if(!App.getLastLogin().equals(date))
                    service.resetScreeningCounts();

                if(minutes >= App.TIME_OUT && !OfflineFormSyncService.isRunning()){

                    App.setAutoLogin("Disabled");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                    editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                    editor.apply();

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else if (App.getAutoLogin().equals("Enabled") && App.getLastLogin().equals(date) && !App.getPrivileges().equals("")) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            else if (App.getAutoLogin().equals("Enabled") && App.getLastLogin().equals(date)) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }


}
