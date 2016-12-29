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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


/**
 * @author owais.hussain@irdresearch.org
 */
public class App {
    private static String program = "";
    private static String version = "";
    private static String theme = "";
    private static String language = "";
    private static String mode = "";
    private static String supportContact = "";
    private static String supportEmail = "";
    private static String city = "";
    private static String country = "";
    private static String ip = "";
    private static String port = "";
    private static String ssl = "";
    private static String username = "";
    private static String password = "";
    private static String autoLogin = "";
    private static String lastLogin = "";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static Locale currentLocale;

    public enum dialogButtonPosition {
        LEFT, CENTER, RIGHT
    }

    public enum dialogButtonStatus {
        NEUTRAL, POSITIVE, NEGATIVE
    }

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
        }
        if (view instanceof TitledRadioGroup) {
            str = ((TitledRadioGroup) view).getRadioGroupSelectedValue();
        } else if (view instanceof TextView) {
            str = ((TextView) view).getText().toString();
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
        return DateFormat.format("yyyy-MM-dd hh:mm:ss", date).toString();
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDate(Date date) {
        return DateFormat.format("yyy-MM-dd", date).toString();
    }

    /**
     * Returns date in sql date string format
     *
     * @param date
     * @return
     */
    public static String getSqlDateTime(Date date) {
        return DateFormat.format("yyy-MM-dd hh:mm:ss", date).toString();
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

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
