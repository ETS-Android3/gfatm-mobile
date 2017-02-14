/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 *
 */

package com.ihsinformatics.gfatmmobile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Owais
 */
public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    public static final String PROGRAM = "program";
    public static final String VERSION = "version";
    public static final String THEME = "theme";
    public static final String LANGUAGE = "language";
    public static final String MODE = "mode";
    public static final String SUPPORT_CONTACT = "support_contact";
    public static final String SUPPORT_EMAIL = "support_email";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String IP = "ip";
    public static final String PORT = "port";
    public static final String SSL_ENCRYPTION = "ssl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_FULLNAME = "user_fullname";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LAST_LOGIN = "last_login";
    public static final String COMMUNICATION_MODE = "communication_mode";
    public static final String LOCATION = "location";
    public static final String LOCATION_LAST_UPDATE = "location_last_update";
    public static final String PATIENT_ID = "patient_id";
    public static final String ROLES = "roles";
    public static final String PROVIDER_UUID = "provider_uuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Use the code below if preferences are meant to be changed instantly
        // without having to re-open the application

        if (key.equals(PROGRAM)) {
            App.setProgram(sharedPreferences.getString(key, ""));
        } else if (key.equals(VERSION)) {
            App.setProgram(sharedPreferences.getString(key, ""));
        } else if (key.equals(THEME)) {
            App.setTheme(sharedPreferences.getString(key, ""));
        } else if (key.equals(LANGUAGE)) {
            App.setLanguage(sharedPreferences.getString(key, ""));
        } else if (key.equals(MODE)) {
            App.setMode(sharedPreferences.getString(key, ""));
        } else if (key.equals(SUPPORT_CONTACT)) {
            App.setSupportContact(sharedPreferences.getString(key, ""));
        } else if (key.equals(SUPPORT_EMAIL)) {
            App.setSupportEmail(sharedPreferences.getString(key, ""));
        } else if (key.equals(CITY)) {
            App.setCity(sharedPreferences.getString(key, ""));
        } else if (key.equals(COUNTRY)) {
            App.setCountry(sharedPreferences.getString(key, ""));
        } else if (key.equals(IP)) {
            App.setIp(sharedPreferences.getString(key, ""));
        } else if (key.equals(PORT)) {
            App.setPort(sharedPreferences.getString(key, ""));
        } else if (key.equals(SSL_ENCRYPTION)) {
            App.setSsl(sharedPreferences.getString(key, ""));
        } else if (key.equals(USERNAME)) {
            App.setUsername(sharedPreferences.getString(key, ""));
        } else if (key.equals(PASSWORD)) {
            App.setPassword(sharedPreferences.getString(key, ""));
        } else if (key.equals(AUTO_LOGIN)) {
            App.setAutoLogin(sharedPreferences.getString(key, ""));
        } else if (key.equals(LAST_LOGIN)) {
            App.setLastLogin(sharedPreferences.getString(key, ""));
        } else if (key.equals(COMMUNICATION_MODE)) {
            App.setCommunicationMode(sharedPreferences.getString(key, ""));
        } else if (key.equals(USER_FULLNAME)) {
            App.setUserFullName(sharedPreferences.getString(key, ""));
        } else if (key.equals(LOCATION)) {
            App.setLocation(sharedPreferences.getString(key, ""));
        } else if (key.equals(LOCATION_LAST_UPDATE)) {
            App.setLocationLastUpdate(sharedPreferences.getString(key, ""));
        } else if (key.equals(PATIENT_ID)) {
            App.setPatientId(sharedPreferences.getString(key, ""));
        } else if (key.equals(ROLES)) {
            App.setRoles(sharedPreferences.getString(key, ""));
        } else if (key.equals(PROVIDER_UUID)) {
            App.setProviderUUid(sharedPreferences.getString(key, ""));
        }


    }
}
