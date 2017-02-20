package com.ihsinformatics.gfatmmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Rabbia on 11/15/2016.
 */

public class StartActivity extends Activity {

    private static DatabaseUtil dbUtil;

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

        ServerService serverService = new ServerService(context);
        com.ihsinformatics.gfatmmobile.model.Patient patient = serverService.getPatientByIdFromLocalDB(App.getPatientId());
        App.setPatient(patient);

        Locale locale = new Locale(preferences.getString(Preferences.LANGUAGE, "en").toLowerCase().substring(0, 2));
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        StartActivity.resetPreferences(this);      //loading preferences


        try {
            dbUtil = new DatabaseUtil(this);
            dbUtil.buildDatabase(false);            // build sql lite db in app memory
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        // Check if Login needed...
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String v = App.getLastLogin();
        if (App.getAutoLogin().equals("Enabled") && App.getLastLogin().equals(date)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
