
package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Rabbia on 11/18/2016.
 *//*


*/
/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. *//*


import android.content.Context;

import com.ihsinformatics.gfatmmobile.App;

*/

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Preferences;
import com.ihsinformatics.gfatmmobile.model.Location;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Metadata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class handles all mobile form requests to the server
 */


public class ServerService {
    private static final String TAG = "ServerService";
    private static DatabaseUtil dbUtil;
    private static HttpGet httpGet;
    private static HttpPost httpPost;
    private static Context context;


    public ServerService(Context context) {
        this.context = context;
        // Specify REST module link
        httpGet = new HttpGet(App.getIp(), App.getPort());
        httpPost = new HttpPost(App.getIp(), App.getPort());
        dbUtil = new DatabaseUtil(this.context);
    }

    /**
     * Checks to see if the client is connected to any network (GPRS/Wi-Fi)
     *
     * @return status
     */
    static public boolean isURLReachable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://" + App.getIp() + ":" + App.getPort());   // Change to "http://google.com" for www  ContactRegistryForm.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }


    public boolean saveFormLocally(String formName, FormsObject form, String pid, HashMap<String, String> formValues) {

        ContentValues values = new ContentValues();

        values.put("program", App.getProgram());
        values.put("form_name", formName);
        values.put("form_date", formValues.get("formDate"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        values.put("timestamp", timestamp.toString());
        values.put("username", App.getUsername());
        values.put("p_id", pid);
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(form);
            oos.flush();
            oos.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = bos.toByteArray();
        values.put("form_object", data);

        dbUtil.insert(Metadata.FORMS, values);

        String id = dbUtil.getObject(Metadata.FORMS, "id", "program='" + App.getProgram() + "' AND form_name='" + formName + "' AND form_date='" + formValues.get("formDate") + "' AND timestamp='" + timestamp.toString() + "' AND username='" + App.getUsername() + "' AND p_id='" + pid + "'");

        for (Map.Entry<String, String> entry : formValues.entrySet()) {

            ContentValues value = new ContentValues();

            value.put("form_id", id);
            value.put("field_name", entry.getKey());
            value.put("value", entry.getValue());

            dbUtil.insert(Metadata.FORMS, value);

        }

        return false;
    }

    public int getTotalSavedForms() {
        return Integer.parseInt(dbUtil.getObject("select count(*) from " + Metadata.FORMS + " where username='" + App.getUsername() + "'"));
    }

    public Object[][] getSavedForms(String username) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object from " + Metadata.FORMS + " where username='" + username + "'");
        return forms;
    }

    public Object[][] getSavedForms(String username, String programName) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object from " + Metadata.FORMS + " where username='" + username + "' and program = '" + programName + "'");
        return forms;
    }

    public boolean deleteForms(String id) {
        dbUtil.delete(Metadata.FORMS, "id=?", new String[]{id});
        return dbUtil.delete(Metadata.FORMS_VALUE, "form_id=?", new String[]{id});
    }

    public boolean deleteAllLocations() {
        return dbUtil.delete(Metadata.LOCATION, null, null);
    }

    public Object[][] getAllLocations() {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_id, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, description from " + Metadata.LOCATION);
        return locations;
    }

    public Object[][] getAllLocations(String programColumn) {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_id, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, description from " + Metadata.LOCATION + " where " + programColumn + " = 'Y'");
        return locations;
    }

    /**
     * Gets username from App variable and checks to see if it exists in the
     * local database. The method doesn't exactly matches the user but attempts
     * to see if the call is authenticated on the server
     *
     * @return status
     */
    public String getUser() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }
        if (App.getCommunicationMode().equals("REST")) {
            JSONObject response = httpGet.getUserByName(App.getUsername());
            if (response == null)
                return "AUTHENTICATION_ERROR";
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(response, "results");
            if (jsonObjects == null)
                return "AUTHENTICATION_ERROR";
            if (jsonObjects.length == 0)
                return "AUTHENTICATION_ERROR";
            for (JSONObject j : jsonObjects) {
                User user = User.parseJSONObject(j);
                App.setUserFullName(user.getFullName());

                ContentValues values = new ContentValues();
                values.put("username", user.getUsername());
                values.put("fullName", user.getFullName());
                values.put("uuid", user.getUuid());
                values.put("password", App.getPassword());
                dbUtil.insert(Metadata.USERS, values);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.USER_FULLNAME, App.getUserFullName());
                editor.apply();

            }
        }
        return "SUCCESS";
    }


    public String getLocations() {

        if (App.getCommunicationMode().equals("REST")) {

            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }

            JSONArray response = httpGet.getAllLocations();
            if (response == null)
                return "AUTHENTICATION_ERROR";

            deleteAllLocations();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    Location location = Location.parseJSONObject(jsonobject);
                    String name = location.getName();
                    String uuid = location.getUuid();
                    String primaryContact = location.getPrimaryContact();
                    String description = location.getDescription();
                    String address1 = location.getAddress1();
                    String address2 = location.getAddress2();
                    String city = location.getCity();
                    String petLocation = location.getPetLocation();
                    String pmdtLocation = location.getPmdtLocation();
                    String fastLocation = location.getFastLocation();
                    String comorbiditiesLocation = location.getComorbiditiesLocation();
                    String childhoodtbLocation = location.getChildhoodTbLocation();

                    ContentValues values = new ContentValues();
                    values.put("location_name", name);
                    values.put("uuid", uuid);
                    values.put("primary_contact", primaryContact);
                    values.put("description", description);
                    values.put("address1", address1);
                    values.put("address2", address2);
                    values.put("city_village", city);
                    values.put("pet_location", petLocation);
                    values.put("pmdt_location", pmdtLocation);
                    values.put("fast_location", fastLocation);
                    values.put("comorbidities_location", comorbiditiesLocation);
                    values.put("childhood_tb_location", childhoodtbLocation);
                    dbUtil.insert(Metadata.LOCATION, values);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Calendar calendar = Calendar.getInstance();
        App.setLocationLastUpdate(DateFormat.format("dd-MMM-yyyy HH:mm:ss", calendar).toString());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Preferences.LOCATION_LAST_UPDATE, App.getLocationLastUpdate());
        editor.apply();

        return "SUCCESS";
    }


    public String createPatient(ContentValues values) {

        String patientId = values.getAsString("patientId");
        String givenName = values.getAsString("firstName");
        String familyName = values.getAsString("lastName");
        int age = values.getAsInteger("age");
        String gender = values.getAsString("gender");
        String location = values.getAsString("location");

        if (App.getCommunicationMode().equals("REST")) {

            String uuid = getPatientUuid(patientId);
            if (uuid != null)
                return "DUPLICATE";
            else {

                PersonName personName = new PersonName("Muhammad", "Haris", "Asif");

                Set<PatientIdentifier> patientIdentifierSet = new HashSet<>();
                PatientIdentifier patientIdentifier = new PatientIdentifier();
                patientIdentifier.setPreferred(true);
                patientIdentifier.setIdentifier("66325-2");
                PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
                patientIdentifierType.setUuid("05a29f94-c0ed-11e2-94be-8c13b969e334");
                patientIdentifier.setIdentifierType(patientIdentifierType);
                org.openmrs.Location ll = new org.openmrs.Location();
                ll.setUuid("7f65d926-57d6-4402-ae10-a5b3bcbf7986");
                patientIdentifier.setLocation(ll);
                patientIdentifierSet.add(patientIdentifier);


                Set<PersonName> personNames = new HashSet<>();
                personNames.add(personName);

                Patient patient = new Patient();
                patient.setNames(personNames);
                patient.setGender("male");
                patient.setBirthdate(new Date(94, 12, 1));
                patient.setIdentifiers(patientIdentifierSet);
                PersonAttributeType personAttributeType = new PersonAttributeType();
                personAttributeType.setName("Haris");
                personAttributeType.setFormat("java.lang.String");
                httpPost.savePatientByEntitiy(patient);

            }


        }

        return "SUCCESS";
    }

    public String getPatientUuid(String patientId) {
        String uuid = null;
        try {
            JSONArray uuids = httpGet.getPatientUuidByPatientId(patientId);
            if (uuids.length() > 0) {
                JSONObject jsonobject = uuids.getJSONObject(0);
                uuid = jsonobject.getString("uuid");
            }

        } catch (Exception e) {
        }
        return uuid;
    }

}

