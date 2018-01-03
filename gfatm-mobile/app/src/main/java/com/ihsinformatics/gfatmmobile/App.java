/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 * This class is used to hold data and other methods that are globally accessed
 */

package com.ihsinformatics.gfatmmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


/**
 * @author owais.hussain@irdresearch.org
 */
public class App {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int OFFLINE_FORM_CAP = 500;
    public static final int OFFLINE_FORM_WARNING = 400;
    public static final int PATIENT_COUNT_CAP = 500;
    private static String program = "";
    private static String version = "";
    private static String theme = "";
    private static String language = "";
    private static String mode = "";
    private static String supportContact = "";
    private static String supportEmail = "";
    private static String province = "";
    private static String city = "";
    private static String country = "";
    private static String ip = "";
    private static String port = "";
    private static String ssl = "";
    private static String username = "";
    private static String password = "";
    private static String autoLogin = "";
    private static String lastLogin = "";
    private static String communicationMode = "";
    private static String userFullName = "";
    private static String location = "";
    private static String locationLastUpdate = "";
    private static String personAttributeLastUpdate = "";
    private static String patientId = "";
    private static String roles = "";
    private static String providerUUid = "";
    private static Patient patient = null;
    private static double longitude = 0.0;
    private static double latitude = 0.0;
    private static Locale currentLocale;


    public static void setThreadSafety(boolean state) {
        StrictMode.ThreadPolicy policy = StrictMode.getThreadPolicy();
        if (state) {
            policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        } else {
            policy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
        }
        StrictMode.setThreadPolicy(policy);
    }

    public static String getProgram() {
        return program;
    }

    public static void setProgram(String program) {
        App.program = program;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        App.version = version;
    }

    public static String getTheme() {
        return theme;
    }

    public static void setTheme(String theme) {
        App.theme = theme;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        App.language = language;
    }

    public static String getMode() {
        return mode;
    }

    public static void setMode(String mode) {
        App.mode = mode;
    }

    public static String getSupportContact() {
        return supportContact;
    }

    public static void setSupportContact(String supportContact) {
        App.supportContact = supportContact;
    }

    public static String getSupportEmail() {
        return supportEmail;
    }

    public static void setSupportEmail(String supportEmail) {
        App.supportEmail = supportEmail;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        App.city = city;
    }

    public static String getProvince() {
        return province;
    }

    public static void setProvince(String province) {
        App.province = province;
    }

    public static String getCountry() {
        return country;
    }

    public static void setCountry(String country) {
        App.country = country;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        App.ip = ip;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        App.port = port;
    }

    public static String getSsl() {
        return ssl;
    }

    public static void setSsl(String ssl) {
        App.ssl = ssl;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        App.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        App.password = password;
    }

    public static String getAutoLogin() {
        return autoLogin;
    }

    public static void setAutoLogin(String autoLogin) {
        App.autoLogin = autoLogin;
    }

    public static String getUserFullName() {
        return userFullName;
    }

    public static void setUserFullName(String userFullName) {
        App.userFullName = userFullName;
    }

    public static String getLastLogin() {
        return lastLogin;
    }

    public static void setLastLogin(String lastLogin) {
        App.lastLogin = lastLogin;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setCurrentLocale(Locale currentLocale) {
        App.currentLocale = currentLocale;
    }

    public static String getCommunicationMode() {
        return communicationMode;
    }

    public static void setCommunicationMode(String communicationMode) {
        App.communicationMode = communicationMode;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        App.location = location;
    }

    public static String getLocationLastUpdate() {
        return locationLastUpdate;
    }

    public static void setLocationLastUpdate(String locationLastUpdate) {
        App.locationLastUpdate = locationLastUpdate;
    }

    public static String getPersonAttributeLastUpdate() {
        return personAttributeLastUpdate;
    }

    public static void setPersonAttributeLastUpdate(String personAttributeLastUpdate) {
        App.personAttributeLastUpdate = personAttributeLastUpdate;
    }

    public static boolean isTabletDevice(Context activityContext) {

        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
        DisplayMetrics metrics = new DisplayMetrics();
        Activity activity = (Activity) activityContext;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (device_large) {
            //Tablet
            return true;
        }else{
            return false;
        }

    }
    public static String getPatientId() {
        return patientId;
    }

    public static void setPatientId(String patientId) {
        App.patientId = patientId;
    }

    public static Patient getPatient() {
        return patient;
    }

    public static void setPatient(Patient patient) {
        App.patient = patient;
    }

    public static String getRoles() {
        return roles;
    }

    public static void setRoles(String roles) {
        App.roles = roles;
    }

    public static String getProviderUUid() {
        return providerUUid;
    }

    public static void setProviderUUid(String providerUUid) {
        App.providerUUid = providerUUid;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        App.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        App.latitude = latitude;
    }


    /**
     * Returns selected value in string, depending on the view passed. If no
     * value is present, an empty string will be returned
     *
     * @param view
     * @return
     */
    public static String get(View view) {

        String str = null;

        if (view instanceof TitledEditText) {
            str = ((TitledEditText) view).getEditText().getText().toString();
        } else if (view instanceof MyEditText) {
            str = ((MyEditText) view).getText().toString();
        } else if (view instanceof EditText) {
            str = ((EditText) view).getText().toString();
        } else if (view instanceof TitledRadioGroup) {
            str = ((TitledRadioGroup) view).getRadioGroupSelectedValue();
        } else if (view instanceof TextView) {
            str = ((TextView) view).getText().toString();
        } else if (view instanceof TitledSpinner) {
            str = ((TitledSpinner) view).getSpinnerValue();
        } else if (view instanceof Spinner) {
            str = ((Spinner) view).getSelectedItem().toString();
        }

        return (str == null ? "" : str);
    }

    /**
     * Returns index of the spinner item location (value) in the spinner.
     *
     * @param spinner, value
     * @return
     */
    public static int getIndex(Spinner spinner, String value) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getColor(Context context, int attribute) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attribute, typedValue, true);
        return typedValue.data;
    }

    /**
     * Returns true if system language is Right-to-Left
     *
     * @return
     */
    public static boolean isLanguageRTL() {
        String code = currentLocale.getLanguage();
        if (code.equals("ar") || code.equals("fa") || code.equals("he") || code.equals("ur"))
            return true;
        return false;
    }

    public static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDate(Calendar date) {
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDateTime(Calendar date) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString();
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDate(Date date) {
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDateTime(Date date) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString();
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static int getDiffMonths(Date first, Date last) {

            int m1 = first.getYear() * 12 + first.getMonth();
            int m2 = last.getYear() * 12 + last.getMonth();
            return m2 - m1;

    }

    public static long getDiffDays(Date first, Date last) {

        long diff = last.getTime() - first.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = null;
        try {
            stringDate = simpledateformat.parse(aDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;

    }

    public static boolean hasKeyListener(TitledEditText view) {
        if (view.getEditText().getKeyListener() == null)
            return false;
        else
            return true;
    }

    public static String convertToTitleCase(String word) {
        if (word == null) {
            return "";
        }

        switch (word.length()) {
            case 0:
                return "";
            case 1:
                return word.toUpperCase(Locale.getDefault()) + " ";
            default:
                char firstLetter = Character.toUpperCase(word.charAt(0));
                return firstLetter + word.substring(1).toLowerCase(Locale.getDefault()) + " ";
        }
    }

    public static int getTimeDurationBetween(Date startTime, Date endTime) {
        Long secondsBetween = (endTime.getTime() - startTime.getTime()) / 1000;
        return secondsBetween.intValue();
    }

    public enum dialogButtonPosition {
        LEFT, CENTER, RIGHT
    }

    public enum dialogButtonStatus {
        NEUTRAL, POSITIVE, NEGATIVE
    }

}
