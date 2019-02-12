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

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.example.backupservice.Backup;
import com.example.backupservice.Params;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.model.Address;
import com.ihsinformatics.gfatmmobile.model.Concept;
import com.ihsinformatics.gfatmmobile.model.EncounterType;
import com.ihsinformatics.gfatmmobile.model.Location;
import com.ihsinformatics.gfatmmobile.model.TreatmentUser;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.model.PersonAttributeType;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Metadata;
import com.ihsinformatics.gfatmmobile.shared.RequestType;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.EncounterRole;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Provider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;

/**
 * This class handles all mobile form requests to the server
 */


public class ServerService {
    private static final String TAG = "ServerService";
    private static DatabaseUtil dbUtil;
    private static HttpGet httpGet;
    private static HttpPost httpPost;
    private static Context context;
    private static String fastGfatmUri;
    private HttpGwtRequest httpGwtClient;
    private static String searchGfatmUri;

    public ServerService(Context context) {
        this.context = context;
        // Specify REST module link
        httpGet = new HttpGet(App.getIp(), App.getPort(), context);
        httpPost = new HttpPost(App.getIp(), App.getPort(), context);
        dbUtil = new DatabaseUtil(this.context);

        // GWT Connections
        fastGfatmUri = App.getIp()+":"+App.getPort() + "/gfatmweb/fastweb.jsp";
        //searchGfatmUri = "199.172.1.211:8888/gfatmtasks.jsp";
        searchGfatmUri = App.getIp()+":"+App.getPort() + "/gfatmweb/gfatmtasks.jsp";
        httpGwtClient = new HttpGwtRequest(this.context);
    }
    /**
     * Checks to see if the client is connected to any network (GPRS/Wi-Fi)
     *
     * @return status
     */
    public boolean isURLReachable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {

                if(!App.getSsl().equalsIgnoreCase("Enabled")) {
                    URL url = new URL("http://" + App.getIp() + ":" + App.getPort());
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(10 * 1000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    } else {
                        return false;
                    }
                }
                else{
                    try {
                        HttpUriRequest request = new cz.msebera.android.httpclient.client.methods.HttpGet("https://" + App.getIp() + ":" + App.getPort());
                        HttpClient client =  HttpGet.createHttpClient_AcceptsUntrustedCerts();
                        HttpResponse response = client.execute(request);
                        StatusLine statusLine = response.getStatusLine();
                        Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
                        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                            return true;
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public String getPatientSystemIdByUuidLocalDB(String uuid) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "patient_id", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String[] getPersonAttributeTypeByName(String name){
        String[][] result = dbUtil.getTableData(Metadata.PERSON_ATTRIBUTE_TYPE, "id,format,foreign_key,uuid", "name = '" + name + "'");
        if (result.length > 0)
            return result[0];
        else
            return null;
    }

    public String[] getPersonAttributeTypeByUuid(String uuid){
        String[][] result = dbUtil.getTableData(Metadata.PERSON_ATTRIBUTE_TYPE, "id,format,foreign_key,name", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0];
        else
            return null;
    }

    public String getProgramUuidFromProgramName(String programName) {

        String[][] result = dbUtil.getTableData(Metadata.PROGRAM, "uuid", "name = '" + programName + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getPatientSystemIdByIdentifierLocalDB(String uuid) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "patient_id", "identifier = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getPatientIdentifierBySystemIdLocalDB(String id) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "identifier", "patient_id = '" + id + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getPatientUUIDByIdentifierLocalDB(String id) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid", "identifier = '" + id + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String saveFormLocallyTesting(String formName, FormsObject form,Calendar encounterDateTime, String[][] obss) {

        ContentValues values5 = new ContentValues();
        values5.put("program", "");
        values5.put("form_name", formName);
        values5.put("p_id", App.getPatientId());
        values5.put("form_date", App.getSqlDate(encounterDateTime));
        Date date = new Date();
        values5.put("timestamp", date.getTime());
        values5.put("location", App.getLocation());
        values5.put("username", App.getUsername());

        if(App.getMode().equalsIgnoreCase("OFFLINE"))
            values5.put("offline_form", "Y");
        else
            values5.put("offline_form", "N");

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
        values5.put("form_object", data);
        dbUtil.insert(Metadata.FORM, values5);

        String formId = dbUtil.getObject(Metadata.FORM, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

        for (int i = 0; i < obss.length; i++) {

            if (obss[i][1].contains(" ; ")) {
                String[] valueArray = obss[i][1].split(" ; ");
                for (int j = 0; j < valueArray.length; j++) {

                    ContentValues values6 = new ContentValues();
                    values6.put("field_name", obss[i][0]);
                    values6.put("value", valueArray[j]);
                    values6.put("form_id", formId);
                    dbUtil.insert(Metadata.FORM_VALUE, values6);

                }

            } else {

                ContentValues values6 = new ContentValues();
                values6.put("field_name", obss[i][0]);
                values6.put("value", obss[i][1]);
                values6.put("form_id", formId);
                dbUtil.insert(Metadata.FORM_VALUE, values6);
            }
        }
        return formId;
    }

    public int getGwtAppFormCount(String date, String formName){
        Object[][] data = dbUtil.getFormTableData("select counts from " + Metadata.SCREENING_COUNT + " where username='" + App.getUsername() + "' and form='" + formName + "' and today='" + date + "'");
        if(data.length == 0)
            return -1;
        else
            return Integer.parseInt(String.valueOf(data[0][0]));
    }

    public int getOfflineGwtAppFormCount(String date, String formName){
        Object[][] data = dbUtil.getFormTableData("select offlinecounts from " + Metadata.SCREENING_COUNT + " where username='" + App.getUsername() + "' and form='" + formName + "' and today='" + date + "'");
        if(data.length == 0)
            return -1;
        else
            return Integer.parseInt(String.valueOf(data[0][0]));
    }

    public int getOnlineGwtAppFormCount(String date, String formName){
        Object[][] data = dbUtil.getFormTableData("select onlinecounts from " + Metadata.SCREENING_COUNT + " where username='" + App.getUsername() + "' and form='" + formName + "' and today='" + date + "'");
        if(data.length == 0)
            return -1;
        else
            return Integer.parseInt(String.valueOf(data[0][0]));
    }

    public Object[][] getOfflineSavedFormsByLimits(String username, int start, int end) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username, autoSyncTries from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'Y' limit " + start + ", " + end);
        return forms;
    }

    public Object[][] getOfflineSavedFormsByPid(String username, String pid) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, patient_id ", "identifier = '" + pid + "'");
        Object[][] forms = null;
        if(result.length != 0)
            forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username, autoSyncTries from " + Metadata.FORM + " where username='" + username + "' and p_id = '" + result[0][1] + "'" );
        return forms;
    }

    public Object[][] getOnlineSavedFormsByLimits(String username, int start, int end) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username, autoSyncTries from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'N' limit " + start + ", " + end);
        return forms;
    }

    public Object[][] getOfflineSavedForms(String username) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username, autoSyncTries from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'Y'");
        return forms;
    }

    public Object[][] getOnlineSavedForms(String username) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username, autoSyncTries from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'N'");
        return forms;
    }

    public int getPendingOfflineSavedFormsCount(String username) {
        Object[][] forms = dbUtil.getFormTableData("select count(*) from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'Y'");
        return Integer.parseInt(String.valueOf(forms[0][0]));
    }

    public int getPendingOnlineSavedFormsCount(String username) {
        Object[][] forms = dbUtil.getFormTableData("select count(*) from " + Metadata.FORM + " where username='" + username + "' and offline_form = 'N'");
        return Integer.parseInt(String.valueOf(forms[0][0]));
    }

    public int getPatientCount() {
        Object[][] forms = dbUtil.getFormTableData("select count(*) from " + Metadata.PATIENT);
        return Integer.parseInt(String.valueOf(forms[0][0]));
    }

    public Object[][] getSavedForm(int id) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORM + " where id = " + id + "");
        return forms;
    }

    public boolean deleteForms(String id) {

        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORM + " where id='" + id + "'");

        if(forms == null || forms.length == 0){
            return true;
        }

        String encounterId = dbUtil.getObject(Metadata.FORM, "encounter_id", "id='" + id + "'");

        if(!(encounterId == "" || encounterId == null)) {
            dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{encounterId});
            dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{encounterId});
        }

        dbUtil.delete(Metadata.FORM, "id=?", new String[]{id});
        dbUtil.delete(Metadata.FORM_VALUE, "form_id=?", new String[]{id});

        if (String.valueOf(forms[0][2]).equals("CREATE PATIENT")) {

            Object[][] childForms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORM + " where p_id='" + String.valueOf(forms[0][3]) + "'");

            for (int j = 0; j < childForms.length; j++) {
                dbUtil.delete(Metadata.FORM, "id=?", new String[]{String.valueOf(childForms[j][0])});
                dbUtil.delete(Metadata.FORM_VALUE, "form_id=?", new String[]{String.valueOf(childForms[j][0])});
                dbUtil.delete(Metadata.FORM_JSON, "form_id=?", new String[]{String.valueOf(childForms[j][0])});
            }

            deletePatientId(String.valueOf(forms[0][3]));
        }

        return dbUtil.delete(Metadata.FORM_JSON, "form_id=?", new String[]{id});

    }

    public boolean deleteAllLocations() {
        return dbUtil.delete(Metadata.LOCATION, null, null);
    }

    public boolean deleteLocationsByProgram(String columnName) {
        return dbUtil.delete(Metadata.LOCATION, columnName + "=?", new String[]{"Y"});
    }

    public boolean deleteAllConcepts() {
        return dbUtil.delete(Metadata.CONCEPT, null, null);
    }

    public boolean deleteAllEncounterTypes() {
        return dbUtil.delete(Metadata.ENCOUNTER_TYPE, null, null);
    }

    public boolean deleteAllPersonAttributeTypes() {
        return dbUtil.delete(Metadata.PERSON_ATTRIBUTE_TYPE, null, null);
    }

    public Object[][] getAllPersonAttributeTypes() {
        Object[][] personAttributesType = dbUtil.getFormTableData("select name, format, foreign_key, uuid from " + Metadata.PERSON_ATTRIBUTE_TYPE);
        return personAttributesType;
    }

    public String getPersonAttributeFormat(String attributeType) {
        Object[][] personAttributesType = dbUtil.getFormTableData("select format from " + Metadata.PERSON_ATTRIBUTE_TYPE + " where name = '" + attributeType + "'");
        if(personAttributesType != null && personAttributesType.length > 0)
            return String.valueOf(personAttributesType[0][0]);
        else
            return null;
    }

    public Object[][] getAllLocationsFromLocalDB() {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description from " + Metadata.LOCATION);
        return locations;
    }

    public Object[][] getAllLocationsFromLocalDBByQuery(String whereQuery) {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description from " + Metadata.LOCATION + " where " + whereQuery );
        return locations;
    }

    public String getLocationTagId(String name){

        Object[][] locationsTag = dbUtil.getFormTableData("select id from " + Metadata.LOCATION_TAG + " where  name = '" + name + "'" );
        if(locationsTag.length == 0){
            return null;
        }
        return String.valueOf(locationsTag[0][0]);
    }

    public Object[][] getAllLocationsFromLocalDB(String programColumn) {
        String where = "1 = 1";
        if(!programColumn.equals(""))
            where = programColumn + " = 'Y'";
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description from " + Metadata.LOCATION + " where " + where + " and (state_province = '" + App.getProvince() + "' OR state_province = '')" );
        return locations;
    }

    public Object[][] getAllTowns() {
        Object[][] locations = dbUtil.getFormTableData("select * from " + Metadata.TOWN);
        return locations;
    }

    public String getConceptMappingForConceptId(String conceptId){
        Object[][] uuid = dbUtil.getFormTableData("select uuid from " + Metadata.CONCEPT_MAPPING + " where concept_id = " + conceptId);
        if(uuid != null && uuid.length > 0)
            return String.valueOf(uuid[0][0]);
        else
            return null;
    }

    public String getConceptMappingForConceptName(String conceptName){

        String[][] result = dbUtil.getTableData(Metadata.CONCEPT, "uuid,data_type", "full_name = '" + conceptName + "'");

        if(result.length == 0)
            return null;

        Object[][] id = dbUtil.getFormTableData("select concept_id from " + Metadata.CONCEPT_MAPPING + " where uuid = '" + result[0][0] + "'");
        if(id != null && id.length > 0)
            return String.valueOf(id[0][0]);
        else
            return null;
    }

    public Object[] getLocationNameThroughLocationId(String locationId){
        String query = "SELECT location_name, description FROM " + Metadata.LOCATION +
                " where location_id = "+ locationId;

        Object[][] locs = dbUtil.getFormTableData(query);
        if(locs != null && locs.length > 0)
            return locs[0];
        else
            return null;
    }

    public Object[][] getConceptAnswers(String conceptUuid){

        String query = "SELECT full_name FROM " + Metadata.CONCEPT_ANSWERS + " ca " +
                "join " + Metadata.CONCEPT + " c on ca.answer_concept_uuid = c.uuid " +
                "where ca.question_concept_uuid = '"+ conceptUuid +"'";

        return dbUtil.getFormTableData(query);
    }

    public void addTown(String name) {

        if(!name.equals("")) {
            String[][] result = dbUtil.getTableData(Metadata.TOWN, "name", "name = '" + name + "'");
            if (!(result.length < 1)) {
                ContentValues values4 = new ContentValues();
                values4.put("name", name);
                dbUtil.insert(Metadata.TOWN, values4);
            }
        }

    }

    /**
     * Gets username from App variable and checks to see if it exists in the
     * local database. The method doesn't exactly matches the user but attempts
     * to see if the call is authenticated on the server
     *
     * @return status
     */
    public String getUser() {

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            Object[][] user = getUserFromLoccalDB(App.getUsername());
            if (user.length < 1) {
                return "USER_NOT_FOUND";
            }
            if (!App.getPassword().equals(String.valueOf(user[0][4]))) {
                return "AUTHENTICATION_ERROR";
            }
            App.setUserFullName(String.valueOf(user[0][2]));
            App.setRoles(String.valueOf(user[0][3]));
            App.setProviderUUid(String.valueOf(user[0][0]));
            App.setPrivileges(String.valueOf(user[0][5]));
            return "SUCCESS";
        }
        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }
        if (App.getCommunicationMode().equals("REST")) {
            JSONObject response = httpGet.getUserByName(App.getUsername());
            if (response == null) {
                return "AUTHENTICATION_ERROR";
            }
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(response, "results");
            if (jsonObjects == null) {
                return "AUTHENTICATION_ERROR";
            }
            if (jsonObjects.length == 0) {
                return "AUTHENTICATION_ERROR";
            }

            JSONObject j = null;
            if(jsonObjects.length > 1){

                for(int i=0; i<jsonObjects.length; i++){

                    try {
                        JSONObject json = jsonObjects[i];
                        String username = json.getString("username");
                        if(username.equals(App.getUsername())){
                            j = json;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            } else j = jsonObjects[0];

            User user = User.parseJSONObject(j);

            String providerUUid = "";
            JSONObject provider = httpGet.getProviderByUserId(user.getIdentifier());
            if (provider == null) {
                return "AUTHENTICATION_ERROR";
            }
            try {
                JSONObject[] providerObjects = JSONParser.getJSONArrayFromObject(provider, "results");
                if (providerObjects == null) {
                    return "PROVIDER_NOT_FOUND";
                }
                if (providerObjects.length == 0) {
                    return "PROVIDER_NOT_FOUND";
                }
                providerUUid = providerObjects[0].getString("uuid");
            } catch (JSONException e) {
                providerUUid = "";
                e.printStackTrace();
            }

            if (providerUUid == "") {
                return "PROVIDER_NOT_FOUND";
            }

            if (!isMobileAppCompatible()) {
                return "VERSION_MISMATCH";
            }

            App.setUserFullName(user.getFullName());
            App.setRoles(user.getRoles());
            App.setProviderUUid(providerUUid);
            String pr = user.getPrivileges();
            App.setPrivileges(pr);

            deleteUserByUuid(user.getUuid());

            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("fullName", user.getFullName());
            values.put("uuid", user.getUuid());
            values.put("password", App.getPassword());
            values.put("role", App.getRoles());
            values.put("provider_uuid", providerUUid);
            values.put("privileges", App.getPrivileges());
            dbUtil.insert(Metadata.USERS, values);

        }

        return "SUCCESS";
    }

    public User getUser(String username) {

        User user = null;
        if (!isURLReachable()) {
            return user;
        }
        if (App.getCommunicationMode().equals("REST")) {
            JSONObject response = httpGet.getUserByName(username);
            if (response == null) {
                return user;
            }
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(response, "results");
            if (jsonObjects == null) {
                return user;
            }
            if (jsonObjects.length == 0) {
                return user;
            }

            JSONObject j = jsonObjects[0];

            user = User.parseJSONObject(j);
            return user;

        }
        return user;
    }

    public boolean isMobileAppCompatible(){

        try {

            JSONObject j = httpGet.getSystemSetting("gfatm-mobile-version");
            String version = j.getString("value");
            String appVersion = App.getVersion();

            if(version.equals(appVersion))
                return true;
            else{
                String[] systemVersions = version.split("\\.");
                String[] appVersions = appVersion.split("\\.");

                if(appVersions[0].equals(systemVersions[0]) && systemVersions[1].equals(appVersions[1]))
                    return true;
                else
                    return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getAllLocations(){

        //if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }
        //}

        String response = "";
        JSONArray locations = null;
        JSONObject json = new JSONObject();

        try {

            json.put("app_ver", App.getVersion());
            json.put("type", RequestType.GFATM_GET_LOCATION);
            json.put("username", App.getUsername());
            json.put("password", App.getPassword());
            json.put("location", "IHS");

            String val = json.toString();

            response = httpGwtClient.clientPost(searchGfatmUri, val);
            JSONObject jsonResponse = JSONParser.getJSONObject(response);
            if(jsonResponse.has("locationArray"))
                locations = jsonResponse.getJSONArray("locationArray");
            else {
                return "FAIL";
            }

            deleteAllLocations();

            for(int i = 0; i < locations.length(); i++){
                JSONObject loc = (JSONObject) locations.get(i);
                String locationId = loc.getString("location_id");
                String name = loc.getString("name");
                String uuid = loc.getString("uuid");
                String parentUuid = loc.getString("parent_id");
                String fastLocation = loc.getString("fast_location");
                if(fastLocation.equals("true")) fastLocation = "Y";
                else fastLocation = "N";
                String childhoodTbLocation = loc.getString("childhood_tb_location");
                if(childhoodTbLocation.equals("true")) childhoodTbLocation = "Y";
                else childhoodTbLocation = "N";
                String petLocation = loc.getString("pet_location");
                if(petLocation.equals("true")) petLocation = "Y";
                else petLocation = "N";
                String commorboditiesLocation = loc.getString("commorbodities_location");
                if(commorboditiesLocation.equals("true")) commorboditiesLocation = "Y";
                else commorboditiesLocation = "N";
                String pmdtLocation = loc.getString("pmdt_location");
                if(pmdtLocation.equals("true")) pmdtLocation = "Y";
                else pmdtLocation = "N";
                String aicLocation = loc.getString("aic_location");
                if(aicLocation.equals("true")) aicLocation = "Y";
                else aicLocation = "N";
                String zttsLocation = "N";
                if(loc.has("ztts_location")) {
                    zttsLocation = loc.getString("ztts_location");
                    if(zttsLocation.equals("true")) zttsLocation = "Y";
                } else {
                    if(name.equalsIgnoreCase("SOUTH") || name.equalsIgnoreCase("KORANGI") || name.equalsIgnoreCase("CENTRAL"))
                        zttsLocation = "Y";
                }

                String contactName = "";
                if(loc.has("contact_name"))
                    contactName = loc.getString("contact_name");
                String tags = "";
                if(loc.has("tags"))
                    tags = loc.getString("tags");

                String contact = loc.getString("contact");
                String address1 = loc.getString("address1");
                String address2 = loc.getString("address2");
                String address3 = loc.getString("address3");
                String cityVillage = loc.getString("cityVillage");
                String stateProvince = loc.getString("stateProvince");
                String county_district = loc.getString("county_district");
                String description = loc.getString("description");

                String locationType = "";
                if(loc.has("location_type")){

                    locationType = loc.getString("location_type");

                }

                ContentValues values = new ContentValues();
                values.put("location_id", locationId);
                values.put("location_name", name);
                values.put("uuid", uuid);
                values.put("parent_uuid", parentUuid);
                values.put("pet_location", petLocation);
                values.put("pmdt_location", pmdtLocation);
                values.put("fast_location", fastLocation);
                values.put("comorbidities_location", commorboditiesLocation);
                values.put("childhood_tb_location", childhoodTbLocation);
                values.put("aic_location", aicLocation);
                values.put("ztts_location", zttsLocation);
                values.put("primary_contact", contact);
                values.put("description", description);
                values.put("address1", address1);
                values.put("address2", address2);
                values.put("address3", address3);
                values.put("city_village", cityVillage);
                values.put("county_district", county_district);
                values.put("state_province", stateProvince);
                values.put("location_type", locationType);

                tags = tags.replace(", ,",",");
                tags = ","+tags;

                values.put("tags", tags);
                values.put("contact_name", contactName);

                dbUtil.insert(Metadata.LOCATION, values);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }

    public String getLocations() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONArray response = httpGet.getAllLocations();
            if (response == null) {
                return "AUTHENTICATION_ERROR";
            }

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
                    String address3 = location.getAddress3();
                    String city = location.getCity();
                    String province = location.getProvince();
                    String district = location.getDistrict();
                    String petLocation = location.getPetLocation();
                    String pmdtLocation = location.getPmdtLocation();
                    String fastLocation = location.getFastLocation();
                    String comorbiditiesLocation = location.getComorbiditiesLocation();
                    String childhoodtbLocation = location.getChildhoodTbLocation();
                    String zttsLocation = location.getZttsLocation();

                    ContentValues values = new ContentValues();
                    values.put("location_name", name);
                    values.put("uuid", uuid);
                    values.put("primary_contact", primaryContact);
                    values.put("description", description);
                    values.put("address1", address1);
                    values.put("address2", address2);
                    values.put("address3", address3);
                    values.put("city_village", city);
                    values.put("county_district", district);
                    values.put("state_province", province);
                    values.put("pet_location", petLocation);
                    values.put("pmdt_location", pmdtLocation);
                    values.put("fast_location", fastLocation);
                    values.put("comorbidities_location", comorbiditiesLocation);
                    values.put("childhood_tb_location", childhoodtbLocation);
                    values.put("ztts_location", zttsLocation);
                    dbUtil.insert(Metadata.LOCATION, values);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "SUCCESS";
    }

    public String getConcepts() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONArray response = httpGet.getAllConcepts();
            if (response == null) {
                return "AUTHENTICATION_ERROR";
            }

            deleteAllConcepts();
            try {
                int val = response.length();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    Concept concept = Concept.parseJSONObject(jsonobject);

                    String name = concept.getName();
                    String uuid = concept.getUuid();
                    String dataType = concept.getDataType();

                    ContentValues values = new ContentValues();
                    values.put("full_name", name);
                    values.put("uuid", uuid);
                    values.put("data_type", dataType);
                    dbUtil.insert(Metadata.CONCEPT, values);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "SUCCESS";
    }

    public String getEncounterTypes() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONArray response = httpGet.getAllEncounterTypes();
            if (response == null) {
                return "AUTHENTICATION_ERROR";
            }

            deleteAllEncounterTypes();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    EncounterType encounterType = EncounterType.parseJSONObject(jsonobject);

                    String name = encounterType.getName();
                    String uuid = encounterType.getUuid();

                    ContentValues values = new ContentValues();
                    values.put("encounter_type", name);
                    values.put("uuid", uuid);
                    dbUtil.insert(Metadata.ENCOUNTER_TYPE, values);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "SUCCESS";
    }




    public String getPersonAttributeTypes() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONArray response = httpGet.getAllPersonAttributeTypes();
            if (response == null) {
                return "AUTHENTICATION_ERROR";
            }

            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    PersonAttributeType personAttributeType = PersonAttributeType.parseJSONObject(jsonobject);

                    if(!personAttributeType.getRetired()) {

                        if(!personAttributeType.getRetired()) {

                            String name = personAttributeType.getName();
                            String uuid = personAttributeType.getUuid();
                            String format = personAttributeType.getFormat();
                            String foreignKey = personAttributeType.getForeignKey();

                            String[] pa = getPersonAttributeTypeByUuid(uuid);
                            if(pa == null){
                                ContentValues values = new ContentValues();
                                values.put("name", name);
                                values.put("uuid", uuid);
                                values.put("format", format);
                                values.put("foreign_key", foreignKey);
                                dbUtil.insert(Metadata.PERSON_ATTRIBUTE_TYPE, values);
                            }
                            else if(!(pa[1].equalsIgnoreCase(format) && pa[3].equalsIgnoreCase(name))){
                                ContentValues values = new ContentValues();
                                values.put("name", name);
                                values.put("format", format);
                                values.put("foreign_key", foreignKey);
                                dbUtil.update(Metadata.PERSON_ATTRIBUTE_TYPE, values, "uuid=?", new String[]{uuid});
                            }
                            else if(pa[2] != null && pa[2].equalsIgnoreCase(foreignKey)){
                                ContentValues values = new ContentValues();
                                values.put("foreign_key", foreignKey);
                                dbUtil.update(Metadata.PERSON_ATTRIBUTE_TYPE, values, "uuid=?", new String[]{uuid});
                            }

                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "SUCCESS";
    }

    public String updateContactIndexRelationship(Date formDate, String relationshipUuid, String encounterId){

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }


        String returnString = httpPost.updateRelationship(formDate, relationshipUuid);

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            String[] uriArray = returnString.split(" ;;;; ");

            ContentValues values4 = new ContentValues();
            values4.put("form_id", Integer.valueOf(encounterId));
            values4.put("uri", uriArray[0]);
            values4.put("content", uriArray[1]);
            values4.put("pid", App.getPatientId());
            values4.put("form", Metadata.RELATIONSHIP);
            values4.put("username", App.getUsername());
            dbUtil.insert(Metadata.FORM_JSON, values4);

        } else if(returnString == null)
            return "CANNOT CREATE RELATIONSHIP";

        return "SUCCESS";

    }

    public String saveContactIndexRelationship(String indexPatientId, String contactPatientId, Date formDate, String encounterId){

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        String indexuuid = getPatientUuid(indexPatientId);
        if(indexuuid == null && !App.getMode().equalsIgnoreCase("OFFLINE"))
            return "INDEX NOT FOUND";

        String contactuuid = getPatientUuid(contactPatientId);

        if(App.getMode().equalsIgnoreCase("OFFLINE")){
            if(indexuuid == null) indexuuid = "<UUID for patient id: " + indexPatientId + ">";
            if(contactuuid == null) contactuuid = "<UUID for patient id: " + contactPatientId + ">";
        }

        String returnString = httpPost.saveRelationship(indexuuid,contactuuid,formDate,"0fdb0891-bece-4540-93db-937b9d8c4905");

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            String[] uriArray = returnString.split(" ;;;; ");

            ContentValues values4 = new ContentValues();
            values4.put("form_id", Integer.valueOf(encounterId));
            values4.put("uri", uriArray[0]);
            values4.put("content", uriArray[1]);
            values4.put("pid", App.getPatientId());
            values4.put("form", Metadata.RELATIONSHIP);
            values4.put("username", App.getUsername());
            dbUtil.insert(Metadata.FORM_JSON, values4);

            return "SUCCESS"+";;;"+"<RELATIONSHIP-UUID>";

        } else if(returnString == null)
            return "CANNOT CREATE RELATIONSHIP";

        JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
        String uuid = "";
        try {
            uuid = jsonObject.getString("uuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "SUCCESS"+";;;"+uuid;

    }

    public String createPatient(ContentValues values) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        String patientId = values.getAsString("patientId");
        String givenName = values.getAsString("firstName");
        String familyName = values.getAsString("lastName");
        String gender = values.getAsString("gender");
        String dob = values.getAsString("dob");
        Date dateOfBirth = App.stringToDate(dob, "yyyy-MM-dd");
        String externalId = values.getAsString("externalId");

        if (App.getCommunicationMode().equals("REST")) {

            String uuid = null;
            if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
                com.ihsinformatics.gfatmmobile.model.Patient patient1 = getPatientByIdentifierFromLocalDB(patientId);
                if (patient1 != null) {
                    return "PATIENT EXISTS IN OFFLINE MODE";
                }
                uuid = getPatientUuid(patientId);
            }else {
                com.ihsinformatics.gfatmmobile.model.Patient patient = getPatientByIdentifierFromLocalDB(patientId);
                if (patient == null)
                    uuid = null;
                else {
                    uuid = patient.getUuid();
                    if(uuid == null) return "PATIENT ID ALREADY EXISTS IN OFFLINE MODE";
                }
            }
            if (uuid != null) {
                return "DUPLICATE";
            }
            else {
                try {
                    PersonName personName = new PersonName(givenName, "", familyName);

                    Set<PatientIdentifier> patientIdentifierSet = new HashSet<PatientIdentifier>();
                    PatientIdentifier patientIdentifier = new PatientIdentifier();
                    patientIdentifier.setPreferred(true);
                    patientIdentifier.setIdentifier(patientId);
                    PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
                    patientIdentifierType.setUuid(getPatientIdentifierTypeUuid("Patient ID"));
                    patientIdentifier.setIdentifierType(patientIdentifierType);
                    org.openmrs.Location ll = new org.openmrs.Location();
                    ll.setUuid(getLocationUuid(App.getLocation()));
                    patientIdentifier.setLocation(ll);
                    patientIdentifierSet.add(patientIdentifier);

                    if (!externalId.equals("")) {
                        PatientIdentifier patientIdentifier1 = new PatientIdentifier();
                        patientIdentifier1.setIdentifier(externalId);
                        org.openmrs.Location l2 = new org.openmrs.Location();
                        l2.setUuid(getLocationUuid(App.getLocation()));
                        patientIdentifier1.setLocation(l2);
                        PatientIdentifierType patientIdentifierType1 = new PatientIdentifierType();
                        patientIdentifierType1.setUuid(getPatientIdentifierTypeUuid("External ID"));
                        patientIdentifier1.setIdentifierType(patientIdentifierType1);
                        patientIdentifierSet.add(patientIdentifier1);
                    }

                    Set<PersonName> personNames = new HashSet<>();
                    personNames.add(personName);

                    Person person = new Person();
                    person.setNames(personNames);
                    person.setGender(gender);
                    person.setBirthdate(dateOfBirth);
                    String personUuid = "";
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        personUuid = "uuid-replacement-string";

                    Patient patient = new Patient(person);
                    patient.setIdentifiers(patientIdentifierSet);
                    patient.setUuid(personUuid);

                    if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                        if(getPatientCount() >= App.PATIENT_COUNT_CAP)
                            deleteEarliestPatient();

                        ContentValues values3 = new ContentValues();
                        values3.put("identifier", patientId);
                        values3.put("first_name", givenName);
                        values3.put("last_name", familyName);
                        values3.put("gender", gender);
                        values3.put("birthdate", dob);
                        dbUtil.insert(Metadata.PATIENT, values3);

                        getPatient(patientId, true);

                        ContentValues values5 = new ContentValues();
                        values5.put("program", "");
                        values5.put("form_name", "CREATE PATIENT");
                        values5.put("p_id", App.getPatientId());
                        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String formDate = formatter.format(new Date());
                        values5.put("form_date", formDate);
                        Date date = new Date();
                        values5.put("timestamp", date.getTime());
                        values5.put("location", App.getLocation());
                        values5.put("username", App.getUsername());
                        dbUtil.insert(Metadata.FORM, values5);

                        String formId = dbUtil.getObject(Metadata.FORM, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

                        ContentValues values6 = new ContentValues();
                        values6.put("field_name", "identifier");
                        values6.put("value", patientId);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "name");
                        values6.put("value", givenName + " " + familyName);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "gender");
                        values6.put("value", gender);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "dob");
                        values6.put("value", dob);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);

                        String uri = httpPost.savePatientByEntitiy(patient);
                        String[] uriArray = uri.split(" ;;;; ");

                        ContentValues values1 = new ContentValues();
                        values1.put("uri", uriArray[0]);
                        values1.put("content", uriArray[1]);
                        values1.put("form", "CREATE PATIENT");
                        values1.put("username", App.getUsername());
                        values1.put("form_id", formId);
                        values1.put("pid", App.getPatientId());

                        dbUtil.insert(Metadata.FORM_JSON, values1);

                    } else {
                        httpPost.savePatientByEntitiy(patient);
                        getPatient(patientId, true);
                    }

                } catch (Exception e) {
                    return "FAIL";
                }
            }

        }
        return "SUCCESS";
    }

    public String getPatientUuid(String patientId) {

        String uuid = getPatientUUIDByIdentifierLocalDB(patientId);

        if(uuid == null && !App.getMode().equalsIgnoreCase("OFFLINE")) {
            try {
                JSONArray uuids = httpGet.getPatientUuidByPatientId(patientId);
                if (uuids.length() > 0) {
                    JSONObject jsonobject = uuids.getJSONObject(0);
                    uuid = jsonobject.getString("uuid");
                }

            } catch (Exception e) {
                return uuid;
            }
        }

        return uuid;
    }

    public boolean isPatientAvailableLocally(String pid){

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid", "identifier = '" + pid + "'");

        if (result.length < 1)
            return false;
        else return true;

    }

    public String[][] getPatientNameFromLocalDB(String pid){

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "first_name,last_name", "identifier = '" + pid + "'");

        if (result.length < 1)
            return null;
        else return result;

    }

    public String getEncounterUUidByEncounterType(String encounterType){

        String[][] result = dbUtil.getTableData(Metadata.ENCOUNTER, "uuid", "encounterType='" + encounterType + "' and patientId='" + App.getPatientId() +"' order by encounterDatetime DESC, dateCreated DESC");
        if (result.length < 1)
            return null;

        return result[0][0];

    }


    public String saveQFTTestOrder(String lab_ref_number, Calendar formDate, String encounterType, String id) {

        JSONObject jsonObject = new JSONObject();

        String uuid = "";

        if(App.getMode().equalsIgnoreCase("OFFLINE"))
            uuid = "<encounter-uuid-replacement>";
        else {
            uuid = getEncounterUUidByEncounterType(encounterType);
            if (uuid == null) return null;
        }

        try {
            jsonObject.put("labReferenceNumber", lab_ref_number);
            jsonObject.put("labTestType", "4f4c97c8-61c3-4c4e-82bc-ef3e8abe8ffa");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("specimenType", "1000AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            jsonObject2.put("specimenSite", "161939AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            jsonObject2.put("collectionDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(formDate.getTime()));
            jsonObject2.put("status", "COLLECTED");
            jsonObject2.put("collector", App.getProviderUUid());
            jsonArray.put(jsonObject2);
            jsonObject.put("labTestSamples",jsonArray);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("action","NEW");

            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                jsonObject3.put("patient","uuid-replacement-string");
            else
                jsonObject3.put("patient",App.getPatient().getUuid());

            jsonObject3.put("concept","dcd97733-4262-4947-ac69-fd2d00880803");
            jsonObject3.put("encounter",uuid);
            jsonObject3.put("careSetting","6f0c9a92-6f24-11e3-af88-005056821db0");
            jsonObject3.put("type","testorder");
            jsonObject3.put("orderer",App.getProviderUUid());

            jsonObject.put("order",jsonObject3);

            String returnString = httpPost.saveOrderObject(jsonObject);

            if(App.getMode().equalsIgnoreCase("OFFLINE")){
                String[] uriArray = returnString.split(" ;;;; ");

                ContentValues values4 = new ContentValues();
                values4.put("form_id", Integer.valueOf(id));
                values4.put("uri", uriArray[0]);
                values4.put("content", uriArray[1]);
                values4.put("pid", App.getPatientId());
                values4.put("form", Metadata.QFT_TEST);
                values4.put("username", App.getUsername());
                dbUtil.insert(Metadata.FORM_JSON, values4);

            }

            if(returnString == null)
                return "ERROR";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }

    public String getPatient(String patientId, Boolean select) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {
                com.ihsinformatics.gfatmmobile.model.Patient patient = null;

                patient = getPatientByIdentifierFromLocalDB(patientId);

                if (patient == null && App.getMode().equalsIgnoreCase("OFFLINE")) {
                    return "PATIENT_NOT_FOUND";
                }

                if (patient == null) {
                    String uuid = getPatientUuid(patientId);
                    if (uuid == null) {
                        return "PATIENT_NOT_FOUND";
                    }
                    else {

                        JSONObject response = httpGet.getPatientByUuid(uuid);
                        patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response, context);

                        if(getPatientCount() >= App.PATIENT_COUNT_CAP)
                            deleteEarliestPatient();

                        com.ihsinformatics.gfatmmobile.model.Person person = patient.getPerson();
                        String puuid = patient.getUuid();
                        String identifier = patient.getPatientId();
                        String eid = patient.getExternalId();
                        String erns = patient.getEnrs();
                        String endTbId = patient.getEndTBId();
                        String fname = patient.getPerson().getGivenName();
                        String lname = patient.getPerson().getFamilyName();
                        String gender = patient.getPerson().getGender();
                        String birthdate = patient.getPerson().getBirthdate();
                        int age = patient.getPerson().getAge();
                        String address1 = patient.getPerson().getAddress1();
                        String address2 = patient.getPerson().getAddress2();
                        String address3 = patient.getPerson().getAddress3();
                        String stateProvince = patient.getPerson().getStateProvince();
                        String countyDistict = patient.getPerson().getCountyDistrict();
                        String cityVillage = patient.getPerson().getCityVillage();
                        String country = patient.getPerson().getCountry();
                        String addressUuid = patient.getPerson().getAddressUuid();
                        String identifierLocation = patient.getIdentifierlocation();

                        ContentValues values = new ContentValues();
                        values.put("uuid", puuid);
                        values.put("identifier", identifier);
                        values.put("external_id", eid);
                        values.put("enrs", erns);
                        values.put("endtb_emr_id", endTbId);
                        values.put("first_name", fname);
                        values.put("last_name", lname);
                        values.put("gender", gender);
                        values.put("birthdate", birthdate);
                        values.put("identifier", identifier);
                        values.put("address1", address1);
                        values.put("address2", address2);
                        values.put("address3", address3);
                        values.put("stateProvince", stateProvince);
                        values.put("cityVillage", cityVillage);
                        values.put("countyDistrict", countyDistict);
                        values.put("country", country);
                        values.put("address_uuid", addressUuid);
                        values.put("patientIdentifier",identifierLocation);

                        dbUtil.insert(Metadata.PATIENT, values);

                        String pid = getPatientSystemIdByUuidLocalDB(uuid);
                        HashMap<String,String> personAttributes = patient.getPerson().getAllPersonAttributes();
                        for (Map.Entry<String, String> entry : personAttributes.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();

                            String[] personAttributeType = getPersonAttributeTypeByName(key);

                            if(personAttributeType != null) {
                                ContentValues val = new ContentValues();
                                val.put("person_attribute_type", personAttributeType[0]);
                                val.put("value", value);
                                val.put("patient_id", pid);

                                dbUtil.insert(Metadata.PERSON_ATTRIBUTE, val);
                            }
                        }

                        JSONArray jsonArray = httpGet.getPatientsEncounters(patientId);
                        for(int i=0; i <jsonArray.length(); i++){
                            JSONObject newObj = jsonArray.getJSONObject(i);
                            com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);
                            encounter.setPatientId(pid);

                            ContentValues values1 = new ContentValues();
                            values1.put("uuid", encounter.getUuid());
                            values1.put("encounterType", encounter.getEncounterType());

                            Date d = null;
                            if (encounter.getEncounterDatetime().contains("/")) {
                                d = App.stringToDate(encounter.getEncounterDatetime(), "dd/MM/yyyy");
                            } else {
                                d = App.stringToDate(encounter.getEncounterDatetime(), "yyyy-MM-dd");
                            }

                            values1.put("encounterDatetime", App.getSqlDate(d));
                            values1.put("encounterLocation", encounter.getEncounterLocation());
                            values1.put("patientId", encounter.getPatientId());
                            values1.put("dateCreated", encounter.getDateCreated());
                            values1.put("createdBy", encounter.getCreator());
                            dbUtil.insert(Metadata.ENCOUNTER, values1);

                            String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "uuid='" + encounter.getUuid() + "'");

                            for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter.getObsGroup()) {
                                ContentValues values2 = new ContentValues();
                                values2.put("uuid", obs.getUuid());
                                values2.put("conceptName", obs.getConceptName());
                                values2.put("value", obs.getValue());
                                values2.put("encounter_id", id);
                                dbUtil.insert(Metadata.OBS, values2);
                            }

                        }

                        if (select) {
                            App.setPatientId(pid);
                            App.setPatient(patient);
                        }
                    }
                } else {

                    if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
                        String patientUuid = patient.getUuid();
                        if (patientUuid == null || patientUuid.equals("")) {
                            return "OFFLINE_PATIENT";
                        }
                    }

                    if (select) {
                        App.setPatientId(String.valueOf(patient.getPid()));
                        App.setPatient(patient);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String getPatientIdentifierTypeUuid(String identifierType) {

        String[][] result = dbUtil.getTableData(Metadata.IDENTIFIER_TYPE, "uuid", "identifier_name = '" + identifierType + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return "";

    }

    public String getLocationUuid(String location) {

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "uuid", "location_name = '" + location + "' or description = '" + location + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public String getLocationNameFromDescription(String location) {

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "location_name", "location_name = '" + location + "' or description = '" + location + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public String getLocationDescriptionFromName(String location) {

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "description", "location_name = '" + location + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public Object[][] getAllPersonAttributesByPatientId(String pid) {

        String query = "SELECT pat.name, pa.value FROM " + Metadata.PERSON_ATTRIBUTE + " pa " +
                "join " + Metadata.PERSON_ATTRIBUTE_TYPE + " pat on pa.person_attribute_type = pat.id " +
                " where pa.patient_id = " + pid + " and retired = 0";

        return dbUtil.getFormTableData(query);

    }

    public String getOfflineFormIdForPatientCreation(String pid) {

        String[][] result = dbUtil.getTableData(Metadata.FORM_JSON, "id", "pid = '" + pid + "' and form = 'CREATE PATIENT' and username = '" + App.getUsername() + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String[][] getPersonAttributeTypeUuid(String personAttributeType) {

        String[][] result = dbUtil.getTableData(Metadata.PERSON_ATTRIBUTE_TYPE, "uuid, format, id", "name = '" + personAttributeType + "'");
        if (result.length > 0)
            return result;
        else
            return null;

    }

    public String getConceptNameFromUuid(String uuid){

        String[][] result = dbUtil.getTableData(Metadata.CONCEPT, "full_name", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getLocationNameFromUuid(String uuid){

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "description", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getLocationSystemIdFromUuid(String uuid){

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "location_id", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String[] getLocationsProgamByName(String location) {

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "fast_location, pet_location, childhood_tb_location, comorbidities_location, ztts_location", "location_name = '" +location + "'");
        if (result.length > 0)
            return result[0];
        else
            return null;

    }

    public String[][] getConceptUuidAndDataType(String concept_name) {

        String[][] result = dbUtil.getTableData(Metadata.CONCEPT, "uuid,data_type", "full_name = '" + concept_name + "'");
        if (result.length > 0) {
            return result;
        }
        else {

            if (App.getMode().equalsIgnoreCase("OFFLINE"))
                return null;

            if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
                if (!isURLReachable()) {
                    return null;
                }
            }

            JSONObject jsonobject = httpGet.getConceptByName(concept_name);
            if (jsonobject == null) {
                return null;
            }
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(jsonobject, "results");
            for (JSONObject json : jsonObjects) {
                Concept concept = Concept.parseJSONObject(json);

                if (!concept.getName().equals(concept_name))
                    continue;

                ContentValues values = new ContentValues();
                values.put("full_name", concept.getName());
                values.put("uuid", concept.getUuid());
                values.put("data_type", concept.getDataType());
                dbUtil.insert(Metadata.CONCEPT, values);

                if (concept.getName().equals(concept_name))
                    result = getConceptUuidAndDataType(concept_name);
            }
            return result;
        }

    }

    public String getEncounterTypeUuid(String type) {


        String encounter = null;
        String[][] result = dbUtil.getTableData(Metadata.ENCOUNTER_TYPE, "uuid", "encounter_type = '" + type + "'");
        if (result.length > 0) {
            return result[0][0];
        }
        else {
            JSONObject jsonobject = httpGet.getEncounterTypeByName(type);
            if (jsonobject == null) {
                return "ERROR RETRIEVING ENCOUNTER TYPE";
            }
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(jsonobject, "results");
            for (JSONObject json : jsonObjects) {
                EncounterType eT = EncounterType.parseJSONObject(json);

                ContentValues values = new ContentValues();
                values.put("encounter_type", eT.getName());
                values.put("uuid", eT.getUuid());
                dbUtil.insert(Metadata.ENCOUNTER_TYPE, values);

                if (type.equals(eT.getName()))
                    encounter = eT.getUuid();
            }
            return encounter;
        }

    }

    public boolean deleteUserByUuid(String uuid) {

        return dbUtil.delete(Metadata.USERS, "uuid=?", new String[]{uuid});

    }



    public com.ihsinformatics.gfatmmobile.model.Patient getPatientBySystemIdFromLocalDB(String id) {

        com.ihsinformatics.gfatmmobile.model.Patient patient = null;

        if (id == null || id.equals(""))
            return patient;

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, first_name, last_name, birthdate, gender, " +  // 0 - 4
                "identifier, external_id, enrs, endtb_emr_id, " +  // 5 - 8
                "address1, address2, address3, stateProvince, countyDistrict, cityVillage, country, address_uuid, patient_id, patientIdentifier, district_tb_number", "patient_id = '" + id + "'"); //9 - 19

        if (result.length < 1)
            return null;

        Date date = App.stringToDate(result[0][3], "yyyy-MM-dd");
        int age = App.getDiffYears(date, new Date());
        HashMap<String, String> personAttributes = new HashMap<String, String>();

        Object[][] attributes = getAllPersonAttributesByPatientId(id);
        for(Object[] attribute : attributes)
            personAttributes.put(String.valueOf(attribute[0]),String.valueOf(attribute[1]));

        com.ihsinformatics.gfatmmobile.model.Person person1 = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][1], result[0][2], age, result[0][3], result[0][4],
                result[0][9], result[0][10], result[0][11], result[0][12], result[0][13], result[0][14], result[0][15], result[0][16], personAttributes);

        patient = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][5], result[0][6], result[0][7], result[0][8], result[0][19],  person1, result[0][18]);
        patient.setPid(Integer.valueOf(result[0][17]));

        return patient;

    }

    public com.ihsinformatics.gfatmmobile.model.Patient getPatientByIdentifierFromLocalDB(String patientId) {

        com.ihsinformatics.gfatmmobile.model.Patient patient1 = null;

        if (patientId == null || patientId.equals(""))
            return patient1;

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, first_name, last_name, birthdate, gender, " +  // 0 - 4
                "identifier, external_id, enrs, endtb_emr_id, " +  // 5 - 8
                "address1, address2, address3, stateProvince, countyDistrict, cityVillage, country, address_uuid, patient_id, patientIdentifier, district_tb_number ", "identifier = '" + patientId + "'"); //9 - 19

        if (result.length < 1)
            return null;

        Date date = App.stringToDate(result[0][3], "yyyy-MM-dd");
        int age = App.getDiffYears(date, new Date());
        HashMap<String, String> personAttributes = new HashMap<String, String>();

        Object[][] attributes = getAllPersonAttributesByPatientId(String.valueOf(result[0][17]));
        for(Object[] attribute : attributes)
            personAttributes.put(String.valueOf(attribute[0]),String.valueOf(attribute[1]));


        com.ihsinformatics.gfatmmobile.model.Person person1 = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][1], result[0][2], age, result[0][3], result[0][4],
                result[0][9], result[0][10], result[0][11], result[0][12], result[0][13], result[0][14], result[0][15], result[0][16], personAttributes);

        patient1 = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][5], result[0][6], result[0][7], result[0][8], result[0][19], person1, result[0][18]);
        patient1.setPid(Integer.valueOf(result[0][17]));

        return patient1;
    }

    public String saveEncounterAndObservation(String formName, FormsObject form, Calendar encounterDateTime, String[][] obss, Boolean orderForm) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        String concept = "";
        String response = "SUCCESS";
        String testId = "";

        if (App.getCommunicationMode().equals("REST")) {

            org.openmrs.Location location1 = new org.openmrs.Location();
            location1.setUuid(getLocationUuid(App.getLocation()));

            org.openmrs.Person person = new org.openmrs.Person();
            person.setUuid(App.getPatient().getPerson().getUuid());

            Patient patient1 = new Patient();
            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                patient1.setUuid("uuid-replacement-string");
            else
                patient1.setUuid(App.getPatient().getUuid());

            org.openmrs.EncounterType encounterType = new org.openmrs.EncounterType();
            Set<EncounterProvider> encounterProviders = new HashSet<>();
            Set<Obs> observations = new HashSet<>();

            try {

                String encounterUuid = getEncounterTypeUuid(formName);
                if (encounterUuid == null) {
                    return "ERROR RETRIEVING ENCOUNTER TYPE";
                }
                encounterType.setUuid(encounterUuid);

                EncounterProvider encounterProvider = new EncounterProvider();

                Provider provider = new Provider();
                provider.setUuid(App.getProviderUUid());

                /*** Form not submitting without encounter role ***/
                EncounterRole encounterRole = new EncounterRole();
                encounterRole.setUuid("a0b03050-c99b-11e0-9572-0800200c9a66");

                encounterProvider.setProvider(provider);
                encounterProvider.setEncounterRole(encounterRole);
                encounterProviders.add(encounterProvider);

                for (int i = 0; i < obss.length; i++) {

                    if ("".equals(obss[i][0]) || "".equals(obss[i][1]))
                        continue;

                    if (obss[i][0].equals("TEST ID") && orderForm) {
                        testId = obss[i][1];
                    }

                    Obs obs = new Obs();

                    org.openmrs.Concept conceptQuestion = new org.openmrs.Concept();
                    String s = obss[i][0];
                    String[][] conceptUuid = getConceptUuidAndDataType(s);
                    if (conceptUuid == null) {
                        return "ERROR RETRIEVING CONCEPT: " + s;
                    }
                    concept = s;
                    conceptQuestion.setUuid(conceptUuid[0][0]);
                    obs.setConcept(conceptQuestion);

                    if (!obss[i][1].contains(" ; ")) {

                        if (conceptUuid[0][1].equals("Coded")) {
                            String[][] valueUuid = getConceptUuidAndDataType(obss[i][1]);
                            if (valueUuid == null) {
                                return "ERROR RETRIEVING CONCEPT: " + obss[i][1];
                            }
                            concept = obss[i][1];
                            org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                            conceptAnswer.setUuid(valueUuid[0][0]);
                            obs.setValueCoded(conceptAnswer);
                        } else
                            obs.setValueText(obss[i][1]);

                    } else {

                        String[] valueArray = obss[i][1].split(" ; ");
                        for (int j = 0; j < valueArray.length; j++) {

                            Obs groupedObs = new Obs();
                            groupedObs.setConcept(conceptQuestion);

                            if (conceptUuid[0][1].equals("Coded")) {
                                String[][] valueUuid = getConceptUuidAndDataType(valueArray[j]);
                                if (valueUuid == null) {
                                    return "ERROR RETRIEVING CONCEPT: " + valueArray[j];
                                }
                                concept = valueArray[j];
                                org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                                conceptAnswer.setUuid(valueUuid[0][0]);
                                groupedObs.setValueCoded(conceptAnswer);
                            } else
                                groupedObs.setValueText(valueArray[j]);

                            obs.addGroupMember(groupedObs);

                        }

                    }

                    observations.add(obs);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "INSERT ERROR: " + concept;
            }

            Encounter encounter = new Encounter();
            encounterDateTime.set(Calendar.HOUR_OF_DAY, 0);
            encounterDateTime.set(Calendar.MINUTE, 0);
            encounterDateTime.set(Calendar.MINUTE, 0);
            encounter.setEncounterDatetime(encounterDateTime.getTime());
            encounter.setPatient(patient1);
            encounter.setEncounterType(encounterType);
            encounter.setLocation(location1);
            encounter.setEncounterProviders(encounterProviders);
            encounter.setObs(observations);

            try {

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                    String uri = httpPost.saveEncounterWithObservationByEntity(encounter);
                    String[] uriArray = uri.split(" ;;;; ");

                    Date now = new Date();
                    ContentValues values2 = new ContentValues();
                    values2.put("encounterType", formName);
                    values2.put("encounterDatetime", App.getSqlDate(encounterDateTime));
                    values2.put("encounterLocation", App.getLocation());
                    values2.put("patientId", App.getPatientId());
                    values2.put("dateCreated", App.getSqlDateTime(now));
                    values2.put("createdBy", App.getUsername());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String encounterId  = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "dateCreated = '" + App.getSqlDateTime(now) +"' and encounterType='" + formName + "' and patientId=" + App.getPatientId());

                    ContentValues values5 = new ContentValues();
                    values5.put("program", "");
                    values5.put("form_name", formName);
                    values5.put("p_id", App.getPatientId());
                    values5.put("form_date", App.getSqlDate(encounterDateTime));
                    Date date = new Date();
                    values5.put("timestamp", date.getTime());
                    values5.put("location", App.getLocation());
                    values5.put("encounter_id", encounterId);
                    values5.put("username", App.getUsername());

                    if(App.getMode().equalsIgnoreCase("OFFLINE"))
                        values5.put("offline_form", "Y");
                    else
                        values5.put("offline_form", "N");

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
                    values5.put("form_object", data);
                    dbUtil.insert(Metadata.FORM, values5);

                    String formId = dbUtil.getObject(Metadata.FORM, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

                    for (int i = 0; i < obss.length; i++) {

                        if (obss[i][1].contains(" ; ")) {
                            String[] valueArray = obss[i][1].split(" ; ");
                            for (int j = 0; j < valueArray.length; j++) {

                                ContentValues values3 = new ContentValues();
                                values3.put("conceptName", obss[i][0]);
                                values3.put("value", valueArray[j]);
                                values3.put("encounter_id", encounterId);
                                dbUtil.insert(Metadata.OBS, values3);

                                ContentValues values6 = new ContentValues();
                                values6.put("field_name", obss[i][0]);
                                values6.put("value", valueArray[j]);
                                values6.put("form_id", formId);
                                dbUtil.insert(Metadata.FORM_VALUE, values6);

                            }

                        } else {

                            ContentValues values3 = new ContentValues();
                            values3.put("conceptName", obss[i][0]);
                            values3.put("value", obss[i][1]);
                            values3.put("encounter_id", encounterId);
                            dbUtil.insert(Metadata.OBS, values3);

                            ContentValues values6 = new ContentValues();
                            values6.put("field_name", obss[i][0]);
                            values6.put("value", obss[i][1]);
                            values6.put("form_id", formId);
                            dbUtil.insert(Metadata.FORM_VALUE, values6);
                        }
                    }

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", formId);
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", formName);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    return "SUCCESS_" + formId;

                } else {

                    String returnString = httpPost.saveEncounterWithObservationByEntity(encounter);
                    JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                    com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                    encounter1.setPatientId(App.getPatientId());

                    ContentValues values2 = new ContentValues();
                    values2.put("uuid", encounter1.getUuid());
                    values2.put("encounterType", encounter1.getEncounterType());

                    Date d = null;
                    if (encounter1.getEncounterDatetime().contains("/")) {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "dd/MM/yyyy");
                    } else {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "yyyy-MM-dd");
                    }

                    values2.put("encounterDatetime", App.getSqlDate(d));
                    values2.put("encounterLocation", encounter1.getEncounterLocation());
                    values2.put("patientId", encounter1.getPatientId());
                    values2.put("dateCreated", encounter1.getDateCreated());
                    values2.put("createdBy", App.getUsername());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "uuid='" + encounter1.getUuid() + "'");

                    for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter1.getObsGroup()) {
                        ContentValues values3 = new ContentValues();
                        values3.put("uuid", obs.getUuid());
                        values3.put("conceptName", obs.getConceptName());
                        values3.put("value", obs.getValue());
                        values3.put("encounter_id", id);
                        dbUtil.insert(Metadata.OBS, values3);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "POST ERROR";
            }
        }
        return response;
    }

    public String saveEncounterAndObservationTesting(String formName, FormsObject form, Calendar encounterDateTime, String[][] obss, String formId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        String concept = "";
        String response = "SUCCESS";

        if (App.getCommunicationMode().equals("REST")) {

            org.openmrs.Location location1 = new org.openmrs.Location();
            location1.setUuid(getLocationUuid(App.getLocation()));

            org.openmrs.Person person = new org.openmrs.Person();
            person.setUuid(App.getPatient().getPerson().getUuid());

            Patient patient1 = new Patient();
            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                patient1.setUuid("uuid-replacement-string");
            else
                patient1.setUuid(App.getPatient().getUuid());

            org.openmrs.EncounterType encounterType = new org.openmrs.EncounterType();
            Set<EncounterProvider> encounterProviders = new HashSet<>();
            Set<Obs> observations = new HashSet<>();

            try {

                String encounterUuid = getEncounterTypeUuid(formName);
                if (encounterUuid == null) {
                    return "ERROR RETRIEVING ENCOUNTER TYPE";
                }
                encounterType.setUuid(encounterUuid);

                EncounterProvider encounterProvider = new EncounterProvider();

                Provider provider = new Provider();
                provider.setUuid(App.getProviderUUid());

                /*** Form not submitting without encounter role ***/
                EncounterRole encounterRole = new EncounterRole();
                encounterRole.setUuid("a0b03050-c99b-11e0-9572-0800200c9a66");

                encounterProvider.setProvider(provider);
                encounterProvider.setEncounterRole(encounterRole);
                encounterProviders.add(encounterProvider);

                for (int i = 0; i < obss.length; i++) {

                    if ("".equals(obss[i][0]) || "".equals(obss[i][1]))
                        continue;

                    Obs obs = new Obs();

                    org.openmrs.Concept conceptQuestion = new org.openmrs.Concept();
                    String s = obss[i][0];
                    String[][] conceptUuid = getConceptUuidAndDataType(s);
                    if (conceptUuid == null) {
                        return "ERROR RETRIEVING CONCEPT: " + s;
                    }
                    concept = s;
                    conceptQuestion.setUuid(conceptUuid[0][0]);
                    obs.setConcept(conceptQuestion);

                    if (!obss[i][1].contains(" ; ")) {

                        if (conceptUuid[0][1].equals("Coded")) {
                            String[][] valueUuid = getConceptUuidAndDataType(obss[i][1]);
                            if (valueUuid == null) {
                                return "ERROR RETRIEVING CONCEPT: " + obss[i][1];
                            }
                            concept = obss[i][1];
                            org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                            conceptAnswer.setUuid(valueUuid[0][0]);
                            obs.setValueCoded(conceptAnswer);
                        } else
                            obs.setValueText(obss[i][1]);

                    } else {

                        String[] valueArray = obss[i][1].split(" ; ");
                        for (int j = 0; j < valueArray.length; j++) {

                            Obs groupedObs = new Obs();
                            groupedObs.setConcept(conceptQuestion);

                            if (conceptUuid[0][1].equals("Coded")) {
                                String[][] valueUuid = getConceptUuidAndDataType(valueArray[j]);
                                if (valueUuid == null) {
                                    return "ERROR RETRIEVING CONCEPT: " + valueArray[j];
                                }
                                concept = valueArray[j];
                                org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                                conceptAnswer.setUuid(valueUuid[0][0]);
                                groupedObs.setValueCoded(conceptAnswer);
                            } else
                                groupedObs.setValueText(valueArray[j]);

                            obs.addGroupMember(groupedObs);

                        }

                    }

                    observations.add(obs);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "INSERT ERROR: " + concept;
            }

            Encounter encounter = new Encounter();
            encounterDateTime.set(Calendar.HOUR_OF_DAY, 0);
            encounterDateTime.set(Calendar.MINUTE, 0);
            encounterDateTime.set(Calendar.MINUTE, 0);
            encounter.setEncounterDatetime(encounterDateTime.getTime());
            encounter.setPatient(patient1);
            encounter.setEncounterType(encounterType);
            encounter.setLocation(location1);
            encounter.setEncounterProviders(encounterProviders);
            encounter.setObs(observations);

            try {

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                    String uri = httpPost.saveEncounterWithObservationByEntity(encounter);
                    String[] uriArray = uri.split(" ;;;; ");

                    Date now = new Date();
                    ContentValues values2 = new ContentValues();
                    values2.put("encounterType", formName);
                    values2.put("encounterDatetime", App.getSqlDate(encounterDateTime));
                    values2.put("encounterLocation", App.getLocation());
                    values2.put("patientId", App.getPatientId());
                    values2.put("dateCreated", App.getSqlDateTime(now));
                    values2.put("createdBy", App.getUsername());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String encounterId  = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "dateCreated = '" + App.getSqlDateTime(now) +"' and encounterType='" + formName + "' and patientId=" + App.getPatientId());

                    ContentValues values = new ContentValues();
                    values.put("encounter_id", encounterId);
                    dbUtil.update(Metadata.FORM, values, "id=?", new String[]{formId});

                    for (int i = 0; i < obss.length; i++) {

                        if (obss[i][1].contains(" ; ")) {
                            String[] valueArray = obss[i][1].split(" ; ");
                            for (int j = 0; j < valueArray.length; j++) {

                                ContentValues values3 = new ContentValues();
                                values3.put("conceptName", obss[i][0]);
                                values3.put("value", valueArray[j]);
                                values3.put("encounter_id", encounterId);
                                dbUtil.insert(Metadata.OBS, values3);

                            }

                        } else {

                            ContentValues values3 = new ContentValues();
                            values3.put("conceptName", obss[i][0]);
                            values3.put("value", obss[i][1]);
                            values3.put("encounter_id", encounterId);
                            dbUtil.insert(Metadata.OBS, values3);

                        }
                    }

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", formId);
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", formName);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    return "SUCCESS_<encounter-uuid-replacement>";

                } else {

                    String returnString = httpPost.saveEncounterWithObservationByEntity(encounter);
                    JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                    com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                    encounter1.setPatientId(App.getPatientId());

                    ContentValues values2 = new ContentValues();
                    values2.put("uuid", encounter1.getUuid());
                    values2.put("encounterType", encounter1.getEncounterType());

                    Date d = null;
                    if (encounter1.getEncounterDatetime().contains("/")) {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "dd/MM/yyyy");
                    } else {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "yyyy-MM-dd");
                    }

                    values2.put("encounterDatetime", App.getSqlDate(d));
                    values2.put("encounterLocation", encounter1.getEncounterLocation());
                    values2.put("patientId", encounter1.getPatientId());
                    values2.put("dateCreated", encounter1.getDateCreated());
                    values2.put("createdBy", App.getUsername());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "uuid='" + encounter1.getUuid() + "'");

                    for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter1.getObsGroup()) {
                        ContentValues values3 = new ContentValues();
                        values3.put("uuid", obs.getUuid());
                        values3.put("conceptName", obs.getConceptName());
                        values3.put("value", obs.getValue());
                        values3.put("encounter_id", id);
                        dbUtil.insert(Metadata.OBS, values3);
                    }

                    response = response + "_" + encounter1.getUuid();

                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "POST ERROR";
            }
        }
        return response;
    }

    public String updateEncounterAndObservationTesting(String formName, String encounterUuid, String[][] obss, String formId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        String concept = "";
        String response = "SUCCESS";

        if (App.getCommunicationMode().equals("REST")) {

            Set<Obs> observations = new HashSet<>();

            try {

                for (int i = 0; i < obss.length; i++) {

                    if ("".equals(obss[i][0]) || "".equals(obss[i][1]))
                        continue;

                    Obs obs = new Obs();

                    org.openmrs.Concept conceptQuestion = new org.openmrs.Concept();
                    String s = obss[i][0];
                    String[][] conceptUuid = getConceptUuidAndDataType(s);
                    if (conceptUuid == null) {
                        return "ERROR RETRIEVING CONCEPT: " + s;
                    }
                    concept = s;
                    conceptQuestion.setUuid(conceptUuid[0][0]);
                    obs.setConcept(conceptQuestion);

                    if (!obss[i][1].contains(" ; ")) {

                        if (conceptUuid[0][1].equals("Coded")) {
                            String[][] valueUuid = getConceptUuidAndDataType(obss[i][1]);
                            if (valueUuid == null) {
                                return "ERROR RETRIEVING CONCEPT: " + obss[i][1];
                            }
                            concept = obss[i][1];
                            org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                            conceptAnswer.setUuid(valueUuid[0][0]);
                            obs.setValueCoded(conceptAnswer);
                        } else
                            obs.setValueText(obss[i][1]);

                    } else {

                        String[] valueArray = obss[i][1].split(" ; ");
                        for (int j = 0; j < valueArray.length; j++) {

                            Obs groupedObs = new Obs();
                            groupedObs.setConcept(conceptQuestion);

                            if (conceptUuid[0][1].equals("Coded")) {
                                String[][] valueUuid = getConceptUuidAndDataType(valueArray[j]);
                                if (valueUuid == null) {
                                    return "ERROR RETRIEVING CONCEPT: " + valueArray[j];
                                }
                                concept = valueArray[j];
                                org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                                conceptAnswer.setUuid(valueUuid[0][0]);
                                groupedObs.setValueCoded(conceptAnswer);
                            } else
                                groupedObs.setValueText(valueArray[j]);

                            obs.addGroupMember(groupedObs);

                        }

                    }

                    observations.add(obs);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "INSERT ERROR: " + concept;
            }

            Encounter encounter = new Encounter();
            encounter.setUuid(encounterUuid);
            encounter.setObs(observations);

            try {

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                    String uri = httpPost.updateEncounterWithObservationByEntity(encounter);
                    String[] uriArray = uri.split(" ;;;; ");

                    String encounterId  = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "encounterType='" + formName + "' and patientId=" + App.getPatientId() + " order by dateCreated desc");

                    for (int i = 0; i < obss.length; i++) {

                        if (obss[i][1].contains(" ; ")) {
                            String[] valueArray = obss[i][1].split(" ; ");
                            for (int j = 0; j < valueArray.length; j++) {

                                ContentValues values3 = new ContentValues();
                                values3.put("conceptName", obss[i][0]);
                                values3.put("value", valueArray[j]);
                                values3.put("encounter_id", encounterId);
                                dbUtil.insert(Metadata.OBS, values3);

                            }

                        } else {

                            ContentValues values3 = new ContentValues();
                            values3.put("conceptName", obss[i][0]);
                            values3.put("value", obss[i][1]);
                            values3.put("encounter_id", encounterId);
                            dbUtil.insert(Metadata.OBS, values3);

                        }
                    }

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", formId);
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", formName);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    return "SUCCESS";

                } else {

                    String returnString = httpPost.updateEncounterWithObservationByEntity(encounter);
                    JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                    com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                    encounter1.setPatientId(App.getPatientId());

                    Object[][] encounterArray = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where uuid ='" + encounterUuid + "'");
                    Boolean flag = dbUtil.delete(Metadata.ENCOUNTER, "uuid=?", new String[]{encounterUuid});
                    if (flag)
                        dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounterArray[0][0])});

                    ContentValues values2 = new ContentValues();
                    values2.put("uuid", encounter1.getUuid());
                    values2.put("encounterType", encounter1.getEncounterType());

                    Date d = null;
                    if (encounter1.getEncounterDatetime().contains("/")) {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "dd/MM/yyyy");
                    } else {
                        d = App.stringToDate(encounter1.getEncounterDatetime(), "yyyy-MM-dd");
                    }

                    values2.put("encounterDatetime", App.getSqlDate(d));
                    values2.put("encounterLocation", encounter1.getEncounterLocation());
                    values2.put("patientId", encounter1.getPatientId());
                    values2.put("dateCreated", encounter1.getDateCreated());
                    values2.put("createdBy", App.getUsername());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "uuid='" + encounter1.getUuid() + "'");

                    for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter1.getObsGroup()) {
                        ContentValues values3 = new ContentValues();
                        values3.put("uuid", obs.getUuid());
                        values3.put("conceptName", obs.getConceptName());
                        values3.put("value", obs.getValue());
                        values3.put("encounter_id", id);
                        dbUtil.insert(Metadata.OBS, values3);
                    }

                    response = response + "_" + encounter1.getUuid();

                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "POST ERROR";
            }
        }
        return response;
    }

    public String savePatientInformationForm(HashMap<String, String> personAttribute) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {

            try {

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                    ContentValues values5 = new ContentValues();
                    values5.put("program", "");
                    values5.put("form_name", "PATIENT INFORMATION form");
                    values5.put("p_id", App.getPatientId());
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String formDate = formatter.format(new Date());
                    values5.put("form_date", formDate);
                    Date date = new Date();
                    values5.put("timestamp", date.getTime());
                    values5.put("location", App.getLocation());
                    values5.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM, values5);

                    String formId = dbUtil.getObject(Metadata.FORM, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

                    for (Map.Entry<String, String> entry : personAttribute.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        ContentValues values6 = new ContentValues();
                        values6.put("field_name", key);
                        values6.put("value", value);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);

                    }
                    return "SUCCESS_"+formId;

                }

            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String saveMultiplePersonAttribute(HashMap<String, String> personAttribute, String encounterId){

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }
        if (App.getCommunicationMode().equals("REST")) {

            try {

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();


                String[] personAttributeUuid = new String[personAttribute.size()];
                String[] personAttributeFormat = new String[personAttribute.size()];
                String[] personAttributeValue = new String[personAttribute.size()];
                String[] personAttributeNames = new String[personAttribute.size()];
                String[] personAttributesId = new String[personAttribute.size()];
                int i = 0;
                for (Map.Entry<String, String> entry : personAttribute.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    String[][] personAttributeType = getPersonAttributeTypeUuid(key);
                    personAttributeUuid[i] = personAttributeType[0][0];
                    personAttributeFormat[i] = personAttributeType[0][1];
                    personAttributeValue[i] = value;
                    personAttributeNames[i] = key;
                    personAttributesId[i] = personAttributeType[0][2];
                    i++;
                }

                String uri = httpPost.savePersonAttributes(personAttributeUuid, personAttributeFormat, personAttributeValue,  patientUuid);

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ATTRIBUTE);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                }

                for(int j=0; j<personAttributesId.length; j++){

                    ContentValues val1 = new ContentValues();
                    val1.put("retired", 1);

                    dbUtil.update(Metadata.PERSON_ATTRIBUTE, val1, "patient_Id=? and person_attribute_type=?", new String[]{App.getPatientId(), personAttributesId[j]});

                    if(personAttributeFormat[j].equalsIgnoreCase("org.openmrs.Concept")){
                        personAttributeValue[j] = getConceptNameFromUuid(personAttributeValue[j]);
                        if(personAttributeValue[j] == null)
                            personAttributeValue[j] = "";
                    }
                    else if(personAttributeFormat[j].equalsIgnoreCase("org.openmrs.Location")){
                        personAttributeValue[j] = getLocationNameFromUuid(personAttributeValue[j]);
                        if(personAttributeValue[j] == null)
                            personAttributeValue[j] = "";
                    }

                    ContentValues val = new ContentValues();
                    val.put("person_attribute_type", personAttributesId[j]);
                    val.put("value", personAttributeValue[j]);
                    val.put("patient_id", App.getPatientId());
                    dbUtil.insert(Metadata.PERSON_ATTRIBUTE, val);

                }

                App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));


            } catch (Exception e) {
                return "FAIL";
            }

        }
        return "SUCCESS";
    }

    public String savePersonAttribute(String attributeType, String value, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }
        if (App.getCommunicationMode().equals("REST")) {
            try {

                String[][] personAttributeType = getPersonAttributeTypeUuid(attributeType);

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();

                String uri = httpPost.savePersonAttribute(personAttributeType[0][0], personAttributeType[0][1], value, patientUuid);

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ATTRIBUTE);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                }

                String[] pat = getPersonAttributeTypeByName(attributeType);

                ContentValues val1 = new ContentValues();
                val1.put("retired", 1);

                dbUtil.update(Metadata.PERSON_ATTRIBUTE, val1, "patient_Id=? and person_attribute_type=?", new String[]{App.getPatientId(), pat[0]});


                if(personAttributeType[0][1].equalsIgnoreCase("org.openmrs.Concept")){
                    value = getConceptNameFromUuid(value);
                    if(value == null)
                        value = "";
                }
                else if(personAttributeType[0][1].equalsIgnoreCase("org.openmrs.Location")){
                    value = getLocationNameFromUuid(value);
                    if(value == null)
                        value = "";
                }

                ContentValues val = new ContentValues();
                val.put("person_attribute_type", pat[0]);
                val.put("value", value);
                val.put("patient_id", App.getPatientId());
                dbUtil.insert(Metadata.PERSON_ATTRIBUTE, val);


                App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));


            } catch (Exception e) {
                return "FAIL";
            }
        }
        return "SUCCESS";
    }

    public String saveIdentifier(String identifierType, String identifier, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                String idType = identifierType;
                idType = idType.toLowerCase();
                idType = idType.replace(" ", "_");

                PatientIdentifier patientIdentifier = new PatientIdentifier();
                patientIdentifier.setPreferred(false);
                patientIdentifier.setIdentifier(identifier);
                PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
                patientIdentifierType.setUuid(getPatientIdentifierTypeUuid(identifierType));
                patientIdentifier.setIdentifierType(patientIdentifierType);
                org.openmrs.Location ll = new org.openmrs.Location();
                ll.setUuid(getLocationUuid(App.getLocation()));
                patientIdentifier.setLocation(ll);

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();

                String uri = httpPost.savePatientIdentifierByEntity(patientIdentifier, patientUuid);
                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PATIENT_IDENTIFIER_FORM);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    ContentValues values6 = new ContentValues();
                    values6.put("field_name", identifierType);
                    values6.put("value", identifier);
                    values6.put("form_id", encounterId);
                    dbUtil.insert(Metadata.FORM_VALUE, values6);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(idType, identifier);

                dbUtil.update(Metadata.PATIENT, contentValues, "patient_Id=?", new String[]{App.getPatientId()});
                App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));

            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String savePersonAddress(String address1, String address2, String city, String district, String province, String country, double longitude, double latitude, String landmark, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                PersonAddress personAddress = new PersonAddress();
                personAddress.setPreferred(true);
                personAddress.setAddress1(address1);
                personAddress.setAddress2(address2);
                personAddress.setAddress3(landmark);
                personAddress.setCityVillage(city);
                personAddress.setStateProvince(province);
                personAddress.setCountyDistrict(district);
                personAddress.setCountry(country);
                personAddress.setLongitude(String.valueOf(longitude));
                personAddress.setLatitude(String.valueOf(latitude));

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();

                String uri = httpPost.savePersonAddressByEntity(personAddress, patientUuid);
                ContentValues contentValues = new ContentValues();
                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ADDRESS_FORM);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    contentValues.put("address_uuid", "");

                }
                else {

                    uri = "{" + uri;
                    JSONObject json = JSONParser.getJSONObject(uri);
                    contentValues.put("address_uuid", json.getString("uuid"));
                }

                contentValues.put("address1", address1);
                contentValues.put("address2", address2);
                contentValues.put("address3", landmark);
                contentValues.put("cityVillage", city);
                contentValues.put("countyDistrict", district);
                contentValues.put("stateProvince", province);
                contentValues.put("country", country);
                contentValues.put("longitude", longitude);
                contentValues.put("latitude", latitude);

                dbUtil.update(Metadata.PATIENT, contentValues, "patient_id=?", new String[]{App.getPatientId()});
                App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));

            } catch (Exception e) {
                return "FAIL";
            }
        }
        return "SUCCESS";
    }

    public String updatePersonAddress(String address1, String address2, String city, String district, String province, String country, double longitude, double latitude, String landmark, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                PersonAddress personAddress = new PersonAddress();
                personAddress.setPreferred(true);
                personAddress.setAddress1(address1);
                personAddress.setAddress2(address2);
                personAddress.setAddress3(landmark);
                personAddress.setCityVillage(city);
                personAddress.setStateProvince(province);
                personAddress.setCountyDistrict(district);
                personAddress.setCountry(country);
                personAddress.setLongitude(String.valueOf(longitude));
                personAddress.setLatitude(String.valueOf(latitude));

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();

                String uri = "";
                if(App.getPatient().getPerson().getAddressUuid().equals(""))
                    uri = httpPost.savePersonAddressByEntity(personAddress, patientUuid);
                else
                    uri = httpPost.updatePersonAddressByEntity(personAddress, App.getPatient().getPerson().getAddressUuid(), patientUuid);
                ContentValues contentValues = new ContentValues();
                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ADDRESS_FORM);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                    contentValues.put("address_uuid", "");

                }
                else {

                    uri = "{" + uri;
                    JSONObject json = JSONParser.getJSONObject(uri);
                    contentValues.put("address_uuid", json.getString("uuid"));
                }

                contentValues.put("address1", address1);
                contentValues.put("address2", address2);
                contentValues.put("address3", landmark);
                contentValues.put("cityVillage", city);
                contentValues.put("countyDistrict", district);
                contentValues.put("stateProvince", province);
                contentValues.put("country", country);
                contentValues.put("longitude", longitude);
                contentValues.put("latitude", latitude);

                dbUtil.update(Metadata.PATIENT, contentValues, "patient_id=?", new String[]{App.getPatientId()});
                App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));

            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String saveProgramEnrollement(String enrollementDate, String program, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                String programUuid = getProgramUuidFromProgramName(program);
                String locationUuid = getLocationUuid(App.getLocation());

                String uri = httpPost.saveProgramEnrollment(programUuid, locationUuid, enrollementDate);
                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PROGRAM);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.FORM_JSON, values4);

                }

            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public Object[][] getEncounterTypesFromLocalDBByProgramName(String programName) {

        Object[][] encounterTypes = dbUtil.getFormTableData("select uuid, encounter_type from " + Metadata.ENCOUNTER_TYPE + " where encounter_type like '" + programName + "%'");
        return encounterTypes;

    }

    public Object[][] getAllEncounterTypesFromLocalDB() {

        Object[][] encounterTypes = dbUtil.getFormTableData("select uuid, encounter_type from " + Metadata.ENCOUNTER_TYPE );
        return encounterTypes;

    }

    public Object[][] getOfflineFormByFormAndEncounter(String formName, int encounterId) {

        Object[][] form = dbUtil.getFormTableData("select content from " + Metadata.FORM_JSON + " where form = '" + formName + "' and encounter_id = " + encounterId);
        return form;
    }

    public OfflineForm getSavedFormById(int formId) {

        Object[][] form = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORM + " where id = " + formId);
        if (form.length < 1)
            return null;
        OfflineForm fo = new OfflineForm(String.valueOf(form[0][0]), String.valueOf(form[0][1]), String.valueOf(form[0][2]), String.valueOf(form[0][3]), String.valueOf(form[0][4]), String.valueOf(form[0][5]),
                String.valueOf(form[0][6]), String.valueOf(form[0][7]), String.valueOf(form[0][8]), String.valueOf(form[0][9]));

        Object[][] obs = dbUtil.getFormTableData("select field_name, value from " + Metadata.FORM_VALUE + " where form_id = " + String.valueOf(form[0][0]));
        for (int i = 0; i < obs.length; i++) {
            fo.putObsValue(String.valueOf(obs[i][0]), String.valueOf(obs[i][1]));
        }

        return fo;
    }

    public Object[][] getAllEncounterFromLocalDB(String programName) {

        if (App.getPatient() == null)
            return null;
        Object[][] encounter = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation, dateCreated from " + Metadata.ENCOUNTER + " where patientId='" + App.getPatientId() + "' and encounterType like '" + programName + "%' order by encounterDatetime DESC, dateCreated DESC");
        return encounter;

    }

    public Object[][] getEncounterFromLocalDB() {

        if (App.getPatient() == null)
            return null;
        Object[][] encounter = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation, dateCreated from " + Metadata.ENCOUNTER + " where patientId='" + App.getPatientId() + "' order by encounterDatetime DESC, dateCreated DESC");
        return encounter;

    }

    public Object[][] getAllObsFromEncounterId(int encounterId) {

        if (App.getPatient() == null)
            return null;
        Object[][] encounter = dbUtil.getFormTableData("select value, conceptName from " + Metadata.OBS + " where encounter_id=" + encounterId + " order by conceptName ASC");
        return encounter;

    }

    public Object[][] getUserFromLoccalDB(String username) {


        Object[][] user1 = dbUtil.getFormTableData("select provider_uuid, username, fullName, role, password, privileges from " + Metadata.USERS + " where username='" + username + "'");
        return user1;

    }

    public String getLatestObsValue(String patientId, String encounterType, String conceptName) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id, dateCreated from " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and encounterType = '" + encounterType + "' order by encounterDatetime DESC, dateCreated DESC");
        if (encounter.length < 1)
            return null;
        Object[][] obs = dbUtil.getFormTableData("select value from " + Metadata.OBS + " where encounter_id=" + String.valueOf(encounter[0][0]) + " and conceptName = '" + conceptName + "'");
        if (obs.length < 1)
            return null;
        else if (obs.length == 1)
            return String.valueOf(obs[0][0]);
        else {
            String value = "";
            for (Object[] o : obs) {
                if (value.equals(""))
                    value = String.valueOf(o[0]);
                else
                    value = value + ", " + String.valueOf(o[0]);
            }
            return value;
        }

    }

    public String[] getAllObsValues(String patientId, String encounterType, String conceptName) {

        Object[][] obs = dbUtil.getFormTableData("select value from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + conceptName + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs.length < 1)
            return null;

        String[] obsResults = new String[obs.length];
        for(int i = 0; i<obs.length; i++){

            obsResults[i]=String.valueOf(obs[i][0]);

        }

        return obsResults;
    }

    public boolean deleteOnlinePatientEncounters(String patientId) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' and uuid is not null");
        if (encounter.length < 1)
            return false;
        for (int i = 0; i < encounter.length; i++) {
            Boolean flag = dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
            if (!flag) return flag;
            dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
        }
        return true;
    }

    public boolean deletePersonAttributes(String patientId){
        return dbUtil.delete(Metadata.PERSON_ATTRIBUTE, "patient_id=?", new String[]{patientId});
    }

    public boolean deletePatientEncounters(String patientId) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "'");
        if (encounter.length < 1)
            return false;
        for (int i = 0; i < encounter.length; i++) {
            Boolean flag = dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
            if (!flag) return flag;
            dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
        }
        return true;
    }

    public boolean deleteEncounterById(String encounterId) {

        Boolean flag = dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounterId)});
        if (!flag) return flag;
        dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounterId)});

        return true;
    }

    public boolean deletePatientTestIdByProgram(String patientId, String programName) {

        Object[][] encounter = dbUtil.getFormTableData("select id from " + Metadata.TEST_ID + " where pid='" + patientId + "' and form like '" + programName + "%'");

        for (int i = 0; i < encounter.length; i++) {
            Boolean flag = dbUtil.delete(Metadata.TEST_ID, "id=?", new String[]{String.valueOf(encounter[i][0])});
            if (!flag) return false;
        }

        return true;
    }

    public void syncTriesIncrementForm(String formId, int tries){

        ContentValues values = new ContentValues();
        values.put("autoSyncTries", tries);
        dbUtil.update(Metadata.FORM, values, "id=?", new String[]{formId});

    }

    public String submitForm(String formId, Boolean check) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {

                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            Object[][] forms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.FORM_JSON + " where form_id='" + formId + "'");

            String uuidEncounter = "";
            String uuidOrder = "";
            for (int i = 0; i < forms.length; i++) {

                Object[] form = forms[i];

                if(form[3].toString().contains("uuid-replacement-string") || form[4].toString().contains("uuid-replacement-string"))
                    return "POST_ERROR";

                if (String.valueOf(form[1]).contains("CREATE")) {

                    if(check){

                        Object[][] identifier = dbUtil.getFormTableData("select identifier from " + Metadata.PATIENT + " where patient_id='" + String.valueOf(form[2]) + "'");
                        String identifierString = String.valueOf(identifier[0][0]);

                        String returnString = searchPatient(identifierString);
                        if(!returnString.equals("PATIENT_NOT_FOUND")){

                            return returnString;

                        }

                    }

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));

                    if (returnString == null)
                        return "POST_ERROR";

                    try {
                        JSONObject newPerson = JSONParser.getJSONObject("{"
                                + returnString.toString() + "}");

                        /*i++;
                        form = forms[i];
                        String patientContent = String.valueOf(form[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));
                        returnString = httpPost.backgroundPost(String.valueOf(form[3]), patientContent);

                        if (returnString == null)
                            return "POST_ERROR";*/

                        Object[][] encounterForms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.FORM_JSON + " where pid='" + String.valueOf(form[2]) + "'");
                        for (int j = 0; j < encounterForms.length; j++) {
                            Object[] encounterForm = encounterForms[j];

                            if (String.valueOf(encounterForm[4]).contains("uuid-replacement-string")) {
                                String content = String.valueOf(encounterForm[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));

                                ContentValues values = new ContentValues();
                                values.put("content", content);
                                dbUtil.update(Metadata.FORM_JSON, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                            }

                            if (String.valueOf(encounterForm[3]).contains("uuid-replacement-string")) {
                                String uri = String.valueOf(encounterForm[3]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));

                                ContentValues values = new ContentValues();
                                values.put("uri", uri);

                                dbUtil.update(Metadata.FORM_JSON, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                            }

                        }

                        ContentValues values = new ContentValues();
                        values.put("uuid", String.valueOf(newPerson.get("uuid")));

                        /*JSONObject json = JSONParser.getJSONObject(patientContent);
                        JSONArray jsonArray = json.getJSONArray("identifiers");
                        JSONObject jsonobject = jsonArray.getJSONObject(0);*/

                        dbUtil.update(Metadata.PATIENT, values, "patient_id=?", new String[]{String.valueOf(form[2])});

                    } catch (Exception e) {
                        return "PARSER_ERROR";
                    }

                } else if (String.valueOf(form[1]).equals(Metadata.PERSON_ATTRIBUTE_FORM) || String.valueOf(form[1]).equals(Metadata.PATIENT_IDENTIFIER_FORM) || String.valueOf(form[1]).equals(Metadata.PROGRAM) || String.valueOf(form[1]).equals(Metadata.PERSON_ATTRIBUTE)) {

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";
                } else if (String.valueOf(form[1]).equals(Metadata.PERSON_ADDRESS_FORM) ){
                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";
                    else{

                        try {
                            JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                            String pid = String.valueOf(form[2]);
                            String uuid = jsonObject.getString("uuid");
                            ContentValues values = new ContentValues();
                            values.put("address_uuid", uuid);
                            dbUtil.update(Metadata.PATIENT, values, "patient_id=?", new String[]{pid});
                        } catch (Exception e) {
                            return "PARSER_ERROR";
                        }

                    }
                } else if(String.valueOf(form[1]).equals(RequestType.FAST_SCREENING)){

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";

                    Date date1 = new Date();
                    String dateInString = App.getSqlDate(date1);

                    String date = dbUtil.getObject(Metadata.FORM, "form_date", "id=" + formId);

                    if(date.equals(dateInString)) {

                        int offlineCount = getOfflineGwtAppFormCount(dateInString, RequestType.FAST_SCREENING);
                        int onlineCount = getOnlineGwtAppFormCount(dateInString, RequestType.FAST_SCREENING);

                        offlineCount = offlineCount - 1;
                        onlineCount = onlineCount + 1;
                        ContentValues v = new ContentValues();
                        v.put("onlinecounts", onlineCount);
                        v.put("offlinecounts", offlineCount);
                        dbUtil.update(Metadata.SCREENING_COUNT, v, "username=? and today=? and form=?", new String[]{App.getUsername(), dateInString, RequestType.FAST_SCREENING});

                    }

                } else if(String.valueOf(form[1]).equals(Metadata.RELATIONSHIP)) {

                    JSONObject jsonObject = JSONParser.getJSONObject(String.valueOf(form[4]));
                    try {

                        String personA="";
                        String personB = "";
                        Date formDate = null;

                        if(jsonObject.has("personA")){

                            personA = jsonObject.getString("personA");
                            if(personA.contains("<UUID for patient id: ")){
                                personA = personA.replace("<UUID for patient id: ","");
                                personA = personA.replace(">","");
                                personA = getPatientUuid(personA);
                            }

                        }

                        if(jsonObject.has("personB")){

                            personB = jsonObject.getString("personB");
                            if(personB.contains("<UUID for patient id: ")){
                                personB = personB.replace("<UUID for patient id: ","");
                                personB = personB.replace(">","");
                                personB = getPatientUuid(personB);
                                if(personB == null)
                                    return "INDEX NOT FOUND";
                            }

                        }

                        if(jsonObject.has("startDate")){

                            String date = jsonObject.getString("startDate");
                            formDate = App.stringToDate(date,"yyyy-MM-dd HH:mm:ss");

                        }

                        if(!personA.equals("") && !personB.equals("")) {

                            String returnString = httpPost.saveRelationship(personB, personA, formDate, "0fdb0891-bece-4540-93db-937b9d8c4905");
                            if (returnString == null)
                                return "CANNOT CREATE RELATIONSHIP";

                            jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                            String uuid = "";
                            try {
                                uuid = jsonObject.getString("uuid");

                                i++;
                                form = forms[i];
                                String patientContent = String.valueOf(form[4]).replace("<RELATIONSHIP-UUID>", uuid);
                                returnString = httpPost.backgroundPost(String.valueOf(form[3]), patientContent);

                                updateLatestObsValue(String.valueOf(form[2]), "INDEX CONTACT RELATIONSHIP UUID", uuid);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                            String patientContent = String.valueOf(form[3]).replace("<RELATIONSHIP-UUID>", getLatestObsValue(String.valueOf(form[2]), "INDEX CONTACT RELATIONSHIP UUID"));
                            String returnString = httpPost.backgroundPost(patientContent,String.valueOf(form[4]));
                            if (returnString == null)
                                return "POST_ERROR";

                        }

                    } catch (JSONException e) {
                        return "PARSER_ERROR";
                    }

                } else if(String.valueOf(form[1]).equals(Metadata.QFT_TEST)) {

                    form[4] = String.valueOf(form[4]).replace("<encounter-uuid-replacement>",uuidEncounter);

                    Object[][] identifier = dbUtil.getFormTableData("select identifier from " + Metadata.PATIENT + " where patient_id='" + String.valueOf(form[2]) + "'");
                    String identifierString = String.valueOf(identifier[0][0]);

                    form[4] = String.valueOf(form[4]).replace("uuid-replacement-string",identifierString);

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";

                } else {

                    try {

                        form[4] = String.valueOf(form[4]).replace("<encounter-uuid-replacement>",uuidEncounter);
                        form[3] = String.valueOf(form[3]).replace("<encounter-uuid-replacement>",uuidEncounter);

                        Object[][] identifier = dbUtil.getFormTableData("select identifier from " + Metadata.PATIENT + " where patient_id='" + String.valueOf(form[2]) + "'");
                        String identifierString = String.valueOf(identifier[0][0]);

                        form[4] = String.valueOf(form[4]).replace("uuid-replacement-string",identifierString);

                        if(String.valueOf(form[4]).contains("<lab-test-order-uuid>") && JSONParser.getJSONObject(String.valueOf(form[4])).has("labReferenceNumber")) {

                            String labRef = JSONParser.getJSONObject(String.valueOf(form[4])).getString("labReferenceNumber");
                            String orderUuid = getObsValueByObs(String.valueOf(form[2]), String.valueOf(form[1]).replace("Result", "Order"), "ORDER ID", labRef, "LAB ORDER UUID");
                            form[4] = String.valueOf(form[4]).replace("<lab-test-order-uuid>", orderUuid);

                        }

                        String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                        if (returnString == null)
                            return "POST_ERROR";


                        if(String.valueOf(form[3]).contains("/commonlab/labtestorder")){

                            JSONObject jObject = JSONParser.getJSONObject("{" + returnString);
                            JSONObject oObject = jObject.getJSONObject("order");
                            uuidOrder = oObject.getString("uuid");

                            String fId = String.valueOf(form[5]);
                            String encounterId = dbUtil.getObject(Metadata.FORM, "encounter_id", "id='" + fId + "'");

                            ContentValues values = new ContentValues();
                            values.put("value", uuidOrder);
                            dbUtil.update(Metadata.OBS, values, "encounter_id=? and conceptName='LAB ORDER UUID'", new String[]{encounterId});

                        }
                        else {

                                JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                                com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);

                                String fId = String.valueOf(form[5]);

                                String encounterId = dbUtil.getObject(Metadata.FORM, "encounter_id", "id='" + fId + "'");

                                ContentValues values = new ContentValues();
                                uuidEncounter = encounter1.getUuid();
                                values.put("uuid", encounter1.getUuid());
                                dbUtil.update(Metadata.ENCOUNTER, values, "encounter_id=?", new String[]{encounterId});

                        }


                    } catch (Exception e) {
                        return "PARSER_ERROR";
                    }

                }
            }

            dbUtil.delete(Metadata.FORM_JSON, "form_id=?", new String[]{formId});
            dbUtil.delete(Metadata.FORM, "id=?", new String[]{formId});
            dbUtil.delete(Metadata.FORM_VALUE, "form_id=?", new String[]{formId});
        }

        return "SUCCESS";

    }

    public String emailOfflineForm(String formId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        StringBuilder formsData = new StringBuilder ();
        if (App.getCommunicationMode().equals("REST")) {
            Object[][] forms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.FORM_JSON + " where form_id='" + formId + "'");


            for (int i = 0; i < forms.length; i++) {

                Object[] form = forms[i];

                formsData.append(String.valueOf(form[1]));
                formsData.append ("\n");
                formsData.append ("uri:\n");
                formsData.append(String.valueOf(form[3]));
                formsData.append ("\n");
                formsData.append ("content:\n");
                formsData.append(String.valueOf(form[4]));

                formsData.append ("\n\n\n");

                /*if (String.valueOf(form[1]).contains("CREATE")) {
                    i++;
                    form = forms[i];
                    formsData.append(String.valueOf(form[1]));
                    formsData.append ("\n");
                    formsData.append ("uri:\n");
                    formsData.append(String.valueOf(form[3]));
                    formsData.append ("\n");
                    formsData.append ("content:\n");
                    formsData.append(String.valueOf(form[4]));

                    formsData.append ("\n\n\n");
                }*/

            }

            formsData.append ("---------------------------------------------------------------");
            formsData.append ("\n\n\n");
        }

        return formsData.toString();
    }

    public Object[][] getEncounterIdByEncounterType(String encounterType) {

        Object[][] encounterObject = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation from " + Metadata.ENCOUNTER + " where encounterType='" + encounterType + "' and patientId=" + App.getPatientId() + "");
        return encounterObject;
    }


    public String getLatestEncounterDateTime(String patientId, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select encounterDatetime from " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and encounterType = '" + encounterType + "' order by encounterDatetime DESC, dateCreated DESC");
        if (encounter.length < 1)
            return null;

        return String.valueOf(encounter[0][0]).substring(0,10);

    }

    public String getEncounterDateTimeByObs(String patientId, String encounterType, String conceptName, String conceptValue) {

        Object[][] obs = dbUtil.getFormTableData("select encounterDatetime from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + conceptName + "' and " + Metadata.OBS + ".value = '" + conceptValue  + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs.length < 1)
            return null;

        return String.valueOf(obs[0][0]).substring(0,10);
    }

    public String getObsValueByObs(String patientId, String encounterType, String filterConceptName, String filterConceptValue, String conceptName) {

        Object[][] obs = dbUtil.getFormTableData("select "+ Metadata.ENCOUNTER +".encounter_id, dateCreated from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + filterConceptName + "' and " + Metadata.OBS + ".value = '" + filterConceptValue  + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs == null || obs.length < 1)
            return null;

        Object[][] encounter = dbUtil.getFormTableData("select value from " + Metadata.OBS + " where encounter_id = '" + String.valueOf(obs[0][0]) + "' and conceptName = '" + conceptName + "'");
        if (encounter.length < 1)
            return null;
        else if (encounter.length == 1)
            return String.valueOf(encounter[0][0]);
        else {
            String value = "";
            for (Object[] o : encounter) {
                if (value.equals(""))
                    value = String.valueOf(o[0]);
                else
                    value = value + ", " + String.valueOf(o[0]);
            }
            return value;
        }

    }

    public String getEncounterDateTimeByObsValue(String patientId, String encounterType, String filterConceptName, String filterConceptValue) {

        Object[][] obs = dbUtil.getFormTableData("select "+ Metadata.ENCOUNTER +".encounterDatetime from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + filterConceptName + "' and " + Metadata.OBS + ".value = '" + filterConceptValue  + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs == null || obs.length < 1)
            return null;

        return String.valueOf(obs[0][0]);

    }

    public String getEncounterLocation(String patientId, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select encounterLocation from " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and encounterType = '" + encounterType + "' order by encounterDatetime DESC, dateCreated DESC");
        if (encounter.length < 1)
            return null;

        return String.valueOf(encounter[0][0]);

    }


    public int getEncounterCountForDate(String date, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select count(*) from " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and encounterDatetime like '"+ date +"%' and createdBy = '" + App.getUsername() + "'");
        if (encounter.length < 1)
            return -1;

        return Integer.parseInt(String.valueOf(encounter[0][0]));

    }

    public int getOnlineEncounterCountForDate(String date, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select count(*) from " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and encounterDatetime like '"+ date +"%' and createdBy = '" + App.getUsername() + "' and uuid is not null");
        if (encounter.length < 1)
            return -1;

        return Integer.parseInt(String.valueOf(encounter[0][0]));

    }

    public int getOfflineEncounterCountForDate(String date, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select count(*) from " + Metadata.ENCOUNTER + " where encounterType = '" + encounterType + "' and encounterDatetime like '"+ date +"%' and createdBy = '" + App.getUsername() + "' and uuid is null");
        if (encounter.length < 1)
            return -1;

        return Integer.parseInt(String.valueOf(encounter[0][0]));

    }

    public Object[][] getTestIdByPatientAndEncounterType(String patientId, String encounterType) {

        Object[][] testId = dbUtil.getFormTableData("select test_id, encounterDateTime from " + Metadata.TEST_ID + " where pid='" + patientId + "' and form = '" + encounterType + "'");
        return testId;

    }

    public String getDBVersion() {

        Object[][] value = dbUtil.getFormTableData("select value from " + Metadata.KEY_VALUE + " where key='DB_VERSION'");
        if (value == null || value.length < 1)
            return null;

        return String.valueOf(value[0][0]);

    }

    public boolean deleteOfflineForms(String fromId) {

        Object[][] encounterId = dbUtil.getFormTableData("select encounter_id from " + Metadata.FORM + " where id='" + fromId + "'");
        if(encounterId == null || encounterId.length == 0)
            return false;

        deleteEncounterById(String.valueOf(encounterId[0][0]));

        dbUtil.delete(Metadata.FORM, "id=?", new String[]{fromId});
        dbUtil.delete(Metadata.FORM_VALUE, "form_id=?", new String[]{fromId});
        dbUtil.delete(Metadata.FORM_JSON, "form_id=?", new String[]{fromId});
        return true;

    }

    public String updatePatientDetails(String patientId, Boolean select) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equalsIgnoreCase("REST")) {
            try {
                com.ihsinformatics.gfatmmobile.model.Patient patient = null;
                //patient = getPatientByIdentifierFromLocalDB(patientId);

                String uuid = getPatientUuid(patientId);
                if (uuid == null)
                    return "PATIENT_NOT_FOUND";
                else {

                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        return "PATIENT_NOT_FOUND";

                    JSONObject response = httpGet.getPatientByUuid(uuid);
                    patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response, context);

                    com.ihsinformatics.gfatmmobile.model.Person person = patient.getPerson();
                    String puuid = patient.getUuid();
                    String identifier = patient.getPatientId();
                    String eid = patient.getExternalId();
                    String erns = patient.getEnrs();
                    String endTbId = patient.getEndTBId();
                    String fname = patient.getPerson().getGivenName();
                    String lname = patient.getPerson().getFamilyName();
                    String gender = patient.getPerson().getGender();
                    String birthdate = patient.getPerson().getBirthdate();
                    int age = patient.getPerson().getAge();
                    String address1 = patient.getPerson().getAddress1();
                    String address2 = patient.getPerson().getAddress2();
                    String address3 = patient.getPerson().getAddress3();
                    String stateProvince = patient.getPerson().getStateProvince();
                    String cityVillage = patient.getPerson().getCityVillage();
                    String country = patient.getPerson().getCountry();
                    String addressUuid = patient.getPerson().getAddressUuid();
                    String identifierLocation = patient.getIdentifierlocation();

                    ContentValues values = new ContentValues();
                    values.put("uuid", puuid);
                    values.put("identifier", identifier);
                    values.put("external_id", eid);
                    values.put("enrs", erns);
                    values.put("endtb_emr_id", endTbId);
                    values.put("first_name", fname);
                    values.put("last_name", lname);
                    values.put("gender", gender);
                    values.put("birthdate", birthdate);
                    values.put("identifier", identifier);
                    values.put("address1", address1);
                    values.put("address2", address2);
                    values.put("address3", address3);
                    values.put("stateProvince", stateProvince);
                    values.put("cityVillage", cityVillage);
                    values.put("country", country);
                    values.put("address_uuid", addressUuid);
                    values.put("patientIdentifier",identifierLocation);

                    dbUtil.update(Metadata.PATIENT, values, "identifier=?", new String[]{patientId});

                    deletePersonAttributes(App.getPatientId());

                    String pid = getPatientSystemIdByUuidLocalDB(uuid);
                    HashMap<String,String> personAttributes = patient.getPerson().getAllPersonAttributes();
                    for (Map.Entry<String, String> entry : personAttributes.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        String[] personAttributeType = getPersonAttributeTypeByName(key);

                        if(personAttributeType != null) {
                            ContentValues val = new ContentValues();
                            val.put("person_attribute_type", personAttributeType[0]);
                            val.put("value", value);
                            val.put("patient_id", pid);

                            dbUtil.insert(Metadata.PERSON_ATTRIBUTE, val);
                        }
                    }

                    if(select) {
                        App.setPatientId(getPatientSystemIdByUuidLocalDB(uuid));
                        App.setPatient(patient);
                    }

                    deleteOnlinePatientEncounters(App.getPatientId());

                    JSONArray jsonArray = httpGet.getPatientsEncounters(patientId);

                    for(int i=0; i <jsonArray.length(); i++) {
                        JSONObject newObj = jsonArray.getJSONObject(i);
                        com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);

                        if (!encounter.getVoided()) {
                            encounter.setPatientId(App.getPatientId());

                            ContentValues values1 = new ContentValues();
                            values1.put("uuid", encounter.getUuid());
                            values1.put("encounterType", encounter.getEncounterType());

                            Date d = null;
                            if (encounter.getEncounterDatetime().contains("/")) {
                                d = App.stringToDate(encounter.getEncounterDatetime(), "dd/MM/yyyy");
                            } else {
                                d = App.stringToDate(encounter.getEncounterDatetime(), "yyyy-MM-dd");
                            }

                            values1.put("encounterDatetime", App.getSqlDate(d));
                            values1.put("encounterLocation", encounter.getEncounterLocation());
                            values1.put("patientId", encounter.getPatientId());
                            values1.put("dateCreated", encounter.getDateCreated());
                            values1.put("createdBy", encounter.getCreator());
                            dbUtil.insert(Metadata.ENCOUNTER, values1);

                            String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "uuid='" + encounter.getUuid() + "'");

                            for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter.getObsGroup()) {
                                if(!obs.getVoided()) {
                                    ContentValues values2 = new ContentValues();
                                    values2.put("uuid", obs.getUuid());
                                    values2.put("conceptName", obs.getConceptName());
                                    values2.put("value", obs.getValue());
                                    values2.put("encounter_id", id);
                                    dbUtil.insert(Metadata.OBS, values2);
                                }
                            }

                        }
                    }
                }


            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String[] getCountryList() {

        String[][] result = dbUtil.getTableData(Metadata.COUNTRY, "name", "1 = 1");
        String[] countryArray = new String[result.length];

        for (int i = 0; i < result.length; i++)
            countryArray[i] = result[i][0];

        return countryArray;

    }

    public String[] getProvinceList(String country) {

        String[][] result = dbUtil.getTableData(Metadata.COUNTRY, "id", "name = '" + country + "'");
        String countryId = result[0][0];

        String[][] provinceIds = dbUtil.getTableData(Metadata.ADDRESS_HIERARCHY, "distinct(province_id)", "country_id = " + countryId + "");

        String[] provinceArray = new String[provinceIds.length];

        for (int i = 0; i < provinceIds.length; i++) {
            String[][] province = dbUtil.getTableData(Metadata.PROVINCE, "name", "id = " + provinceIds[i][0] + "");
            provinceArray[i] = province[0][0];
        }

        return provinceArray;

    }

    public String[] getDistrictList(String province) {

        String[][] result = dbUtil.getTableData(Metadata.PROVINCE, "id", "name = '" + province + "'");
        String provinceId = result[0][0];

        String[][] districtIds = dbUtil.getTableData(Metadata.ADDRESS_HIERARCHY, "distinct(district_id)", "province_id = " + provinceId + "");

        String[] districtArray = new String[districtIds.length];

        for (int i = 0; i < districtIds.length; i++) {
            String[][] district = dbUtil.getTableData(Metadata.DISTRICT, "name", "id = " + districtIds[i][0] + "");
            districtArray[i] = district[0][0];
        }

        return districtArray;

    }

    public String[] getCityList(String district) {

        String[][] result = dbUtil.getTableData(Metadata.DISTRICT, "id", "name = '" + district + "'");
        String districtId = result[0][0];

        String[][] cityIds = dbUtil.getTableData(Metadata.ADDRESS_HIERARCHY, "distinct(city_id)", "district_id = " + districtId + "");

        String[] cityArray = new String[cityIds.length];

        for (int i = 0; i < cityIds.length; i++) {
            String[][] city = dbUtil.getTableData(Metadata.CITY, "name", "id = " + cityIds[i][0] + "");
            cityArray[i] = city[0][0];
        }

        return cityArray;

    }

    public TreatmentUser[] getUsersByRole(String role){
        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        if (App.getCommunicationMode().equals("REST")) {

            role = role.replace(" ","+");
            JSONArray response = httpGet.getUsersByRole(role);
            if (response == null)
                return null;

            try {
                TreatmentUser[] treatmentUsers = new TreatmentUser[response.length()];
                for (int i = 0; i < response.length(); i++) {

                    JSONObject json = response.getJSONObject(i);
                    TreatmentUser treatmentUser = TreatmentUser.parseJSONObject(json);
                    treatmentUsers[i] = treatmentUser;

                }
                return treatmentUsers;
            } catch (Exception e){
                return null;
            }

        }
        return null;
    }

    public Address getPreferredAddressByPersonUuid(String personUuid){
        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONObject json  = httpGet.getPersonAddressByPersonUuid(personUuid);
            if (json == null)
                return null;

            try {
                Address address = Address.parseJSONObject(json);
                return address;
            } catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public String submitToGwtApp(String encounterType, FormsObject form, ContentValues values, String[][] observations) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        String response = "";

        String formDate = values.getAsString("entereddate");
        String location = values.getAsString("location");

        try {

            // Save Form
            JSONObject json = new JSONObject();
            json.put("app_ver", App.getVersion());
            json.put("type", encounterType);
            json.put("username", App.getUsername());
            json.put("password", App.getPassword());
            json.put("location", location);
            json.put("entereddate", formDate);

            JSONArray obs = new JSONArray();
            for (int i = 0; i < observations.length; i++) {
                if ("".equals(observations[i][0])
                        || "".equals(observations[i][1]))
                    continue;
                JSONObject obsJson = new JSONObject();
                obsJson.put("name", observations[i][0]);
                obsJson.put("value", observations[i][1]);

                obs.put(obsJson);
            }
            json.put("results", obs);

            String val = json.toString();

            // Save form locally if in offline mode
            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                ContentValues values5 = new ContentValues();
                values5.put("program", "");
                values5.put("form_name", encounterType);
                //values5.put("p_id", App.getPatientId());
                values5.put("form_date", formDate);
                Date date = new Date();
                values5.put("timestamp", date.getTime());
                values5.put("location", location);
                //values5.put("encounter_id", encounterId);
                values5.put("username", App.getUsername());
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
                values5.put("form_object", data);
                dbUtil.insert(Metadata.FORM, values5);

                String formId = dbUtil.getObject(Metadata.FORM, "id", "timestamp='" + date.getTime() + "'");

                for (int i = 0; i < observations.length; i++) {

                    if (observations[i][1].contains(" ; ")) {
                        String[] valueArray = observations[i][1].split(" ; ");
                        for (int j = 0; j < valueArray.length; j++) {

                            ContentValues values6 = new ContentValues();
                            values6.put("field_name", observations[i][0]);
                            values6.put("value", valueArray[j]);
                            values6.put("form_id", formId);
                            dbUtil.insert(Metadata.FORM_VALUE, values6);

                        }

                    } else {

                        ContentValues values6 = new ContentValues();
                        values6.put("field_name", observations[i][0]);
                        values6.put("value", observations[i][1]);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORM_VALUE, values6);
                    }
                }

                ContentValues values4 = new ContentValues();
                values4.put("form_id", formId);

                values4.put("uri", "serverAddress/gfatmweb/fastweb.jsp");
                values4.put("content", val);
                values4.put("pid", App.getPatientId());
                values4.put("form", encounterType);
                values4.put("username", App.getUsername());
                dbUtil.insert(Metadata.FORM_JSON, values4);

                Date date1 = new Date();
                String dateInString = App.getSqlDate(date1);
                if(/*formDate.equals(dateInString) &&*/ encounterType.equals(RequestType.FAST_SCREENING)) {
                    int count = getGwtAppFormCount(dateInString, encounterType);
                    if (count == -1) {
                        ContentValues v = new ContentValues();
                        v.put("username", App.getUsername());
                        v.put("today", dateInString);
                        v.put("form", encounterType);
                        v.put("counts", 1);
                        //v.put("offlinecounts",1);
                        dbUtil.insert(Metadata.SCREENING_COUNT, v);
                    } else {

                        //int offlineCount = getOfflineGwtAppFormCount(dateInString, encounterType);

                        count = count + 1;
                        //offlineCount = offlineCount +1;
                        ContentValues v = new ContentValues();
                        v.put("counts", count);
                        //v.put("offlinecounts",offlineCount);
                        dbUtil.update(Metadata.SCREENING_COUNT, v, "username=? and today=? and form=?", new String[]{App.getUsername(), dateInString, encounterType});

                    }
                }

                return "SUCCESS";

            }


            response = httpGwtClient.clientPost(fastGfatmUri, val);
            JSONObject jsonResponse = JSONParser.getJSONObject(response);
            if (jsonResponse == null) {
                return response;
            }
            if (jsonResponse.has("response")) {
                String result = jsonResponse.getString("response");
                if (jsonResponse.getString("response").equals("ERROR"))
                    result = jsonResponse.getString("details");
                else {

                    Date date1 = new Date();
                    String dateInString = App.getSqlDate(date1);

                    if(formDate.equals(dateInString) && encounterType.equals(RequestType.FAST_SCREENING)) {
                        int count = getGwtAppFormCount(dateInString, encounterType);
                        if (count == -1) {

                            ContentValues v = new ContentValues();
                            v.put("username", App.getUsername());
                            v.put("today", dateInString);
                            v.put("form", encounterType);
                            v.put("counts", 1);
                            v.put("onlinecounts",1);
                            dbUtil.insert(Metadata.SCREENING_COUNT, v);
                        } else {

                            int onlineCount = getOnlineGwtAppFormCount(dateInString, encounterType);

                            count = count + 1;
                            onlineCount = onlineCount +1;

                            ContentValues v = new ContentValues();
                            v.put("counts", count);
                            v.put("onlinecounts",onlineCount);
                            dbUtil.update(Metadata.SCREENING_COUNT, v, "username=? and today=? and form=?", new String[]{App.getUsername(), dateInString, encounterType});

                        }
                    }

                }
                return result;
            }
            return response;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            response = "POST_ERROR";
        }

        return response;
    }

    public String submitFeedbackToServer(String encounterType, FormsObject form, ContentValues values, String[][] observations) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        String response = "";

        String formDate = values.getAsString("entereddate");
        String location = values.getAsString("location");

        try {

            // Save Form
            JSONObject json = new JSONObject();
            json.put("app_ver", App.getVersion());
            json.put("type", encounterType);
            json.put("username", App.getUsername());
            json.put("password", App.getPassword());
            json.put("location", "IHS");
            json.put("entereddate", formDate);

            JSONArray obs = new JSONArray();
            for (int i = 0; i < observations.length; i++) {
                if ("".equals(observations[i][0])
                        || "".equals(observations[i][1]))
                    continue;
                JSONObject obsJson = new JSONObject();
                obsJson.put("name", observations[i][0]);
                obsJson.put("value", observations[i][1]);

                obs.put(obsJson);
            }
            json.put("results", obs);

            String val = json.toString();

            response = httpGwtClient.clientPost(searchGfatmUri, val);
            JSONObject jsonResponse = JSONParser.getJSONObject(response);
            if (jsonResponse == null) {
                return response;
            }
            if (jsonResponse.has("response")) {
                String result = jsonResponse.getString("response");
                if (jsonResponse.getString("response").equals("ERROR"))
                    result = result + " <br> "
                            + jsonResponse.getString("details");
                return result;
            }
            return response;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            response = "POST_ERROR";
        }

        return response;
    }


    public JSONObject searchPatients(String encounterType, ContentValues values, String[][] params) {

        JSONObject jsonResponse = null;
        String response = "";
        String responseDetails = "";

        try {

            if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
                if (!isURLReachable()) {
                    return null;
                }

                JSONObject json = new JSONObject();
                json.put("app_ver", App.getVersion());
                json.put("type", encounterType);
                json.put("username", App.getUsername());
                json.put("password", App.getPassword());
                json.put("location", "IHS");
                json.put("age_range", values.get("age_range"));
                json.put("gender", values.get("gender"));

                JSONArray obs = new JSONArray();
                for (int i = 0; i < params.length; i++) {
                    if ("".equals(params[i][0])
                            || "".equals(params[i][1]))
                        continue;
                    JSONObject obsJson = new JSONObject();
                    obsJson.put("name", params[i][0]);
                    obsJson.put("value", params[i][1]);

                    obs.put(obsJson);
                }
                json.put("results", obs);

                String val = json.toString();

                response = httpGwtClient.clientPost(searchGfatmUri, val);
                jsonResponse = JSONParser.getJSONObject(response);

//            if (jsonResponse == null) {
//                return response;
//            }
//            if (jsonResponse.has("response")) {
//                String result = jsonResponse.getString("response");
//                if (jsonResponse.getString("response").equals("ERROR"))
//                    result = result + " <br> "
//                            + jsonResponse.getString("details");
//                return result;
//            }
//            return response;

            } else {

                String lowerAge = values.get("age_range").toString().split("-")[0].trim();
                String upperAge = values.get("age_range").toString().split("-")[1].trim();
                String gender = values.get("gender").toString();

                Object[][] patientOnAgeAndGender = dbUtil.getFormTableData("select patient_id from " + Metadata.PATIENT + " where (strftime('%Y', 'now') - strftime('%Y', datetime(substr(birthdate, 1, 10)))) - (strftime('%m-%d', 'now') < strftime('%m-%d', datetime(substr(birthdate, 1, 10)))) >=" + lowerAge + " AND (strftime('%Y', 'now') - strftime('%Y', datetime(substr(birthdate, 1, 10)))) - (strftime('%m-%d', 'now') < strftime('%m-%d', datetime(substr(birthdate, 1, 10)))) <=" + upperAge + " AND gender ='" + gender + "'");

//                Object[][] patientOnAgeAndGender = dbUtil.getFormTableData("select patient_id from " + Metadata.PATIENT + " where gender ='" + gender + "'");


                if (patientOnAgeAndGender.length > 0) {

                    StringBuilder patientIdBuilder = new StringBuilder();

                    for (int i = 0; i < patientOnAgeAndGender.length; i++) {
                        Object[] obj = patientOnAgeAndGender[i];

                        if (i != 0)
                            patientIdBuilder.append(",");
                        patientIdBuilder.append(obj[0].toString());

                    }

                    for (int i = 0; i < params.length; i++) {
                        if ("".equals(params[i][0])
                                || "".equals(params[i][1]))
                            continue;

                        StringBuilder whereClause = new StringBuilder();

                        if (params[i][0].equals("PATIENT_IDENTIFIER")) {
                            whereClause.append(" AND identifier = '");
                            whereClause.append(params[i][1]);
                            whereClause.append("' ");

                        } else if (params[i][0].equals("CNIC")) {
                            whereClause.append(" AND nationalid = '");
                            whereClause.append(params[i][1]);
                            whereClause.append("' ");

                        } else if (params[i][0].equals("CONTACT_NUMBER")) {
                            whereClause.append(" AND primarycontact = '");
                            whereClause.append(params[i][1]);
                            whereClause.append("' ");

                        } else if (params[i][0].equals("GUARDIAN_NAME")) {

                            whereClause.append(" AND guardianname LIKE '%");
                            whereClause.append(params[i][1]);
                            whereClause.append("%' ");

                        } else if (params[i][0].equals("MOTHER_NAME")) {
                            whereClause.append(" AND mothername LIKE '%");
                            whereClause.append(params[i][1]);
                            whereClause.append("%' ");

                        } else if (params[i][0].equals("PERSON_NAME")) {

                            whereClause.append("AND (first_name  || ' ' || last_name) LIKE '%");
                            whereClause.append(params[i][1]);
                            whereClause.append("%' ");

                        } else if (params[i][0].equals("PROGRAM")) {

                            if (params[i][1].equals("PMDT"))
                                whereClause.append(" AND in_pmdt = 'Y' ");
                            else if (params[i][1].equals("PET"))
                                whereClause.append(" AND in_pet = 'Y' ");
                            else if (params[i][1].equals("ChildhoodTB"))
                                whereClause.append(" AND in_childhood_tb = 'Y' ");
                            else if (params[i][1].equals("Comorbidities"))
                                whereClause.append(" AND in_comorbidities = 'Y' ");
                            else if (params[i][1].equals("FAST"))
                                whereClause.append(" AND in_fast = 'Y' ");

                        }

                        Object[][] patients = dbUtil.getFormTableData("select uuid, identifier, (first_name  || ' ' || last_name) as full_name, gender, datetime(substr(birthdate, 1, 10)), (strftime('%Y', 'now') - strftime('%Y', datetime(substr(birthdate, 1, 10)))) - (strftime('%m-%d', 'now') < strftime('%m-%d', datetime(substr(birthdate, 1, 10)))) as age from " + Metadata.PATIENT + " where 1=1 " + whereClause.toString() + " AND patient_id IN (" + patientIdBuilder.toString() + ")");

                        JSONArray personDetails = new JSONArray();
                        JSONObject person;


                        for (int j = 0; j < patients.length; j++) {
                            Object[] obj = patients[j];

                            person = new JSONObject();
                            if(obj[0] != null)
                                person.put("uuid", obj[0].toString());
                            if(obj[1] != null)
                                person.put("identifier", obj[1].toString());
                            if(obj[2] != null)
                                person.put("fullName", obj[2].toString());
                            if(obj[3] != null)
                                person.put("gender", obj[3].toString());
                            if(obj[4] != null)
                                person.put("dob", obj[4].toString());
                            if(obj[5] != null)
                                person.put("age", obj[5].toString());

                            personDetails.put(person);

                        }

                        jsonResponse = new JSONObject();
                        if (personDetails != null && personDetails.length() > 0) {

                            response = "SUCCESS";
                            responseDetails = "Detail :  Data found";

                            jsonResponse.put("response", response);
                            jsonResponse.put("details", responseDetails);
                            jsonResponse.put("personArray", personDetails);

                        } else {
                            response = "ERROR";
                            responseDetails = "Detail : No data found for matching criteria";
                            jsonResponse.put("response", response);
                            jsonResponse.put("details", responseDetails);
                        }
                    }
                } else {
                    jsonResponse = new JSONObject();
                    response = "ERROR";
                    responseDetails = "Detail : No data found for matching criteria";
                    jsonResponse.put("response", response);
                    jsonResponse.put("details", responseDetails);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }


    /**
     * @param weight
     * @return String
     */
    public String getPercentile(String weight) {

        String result = "";

        String genderValue = "1";
        if(App.getPatient().getPerson().getGender().equalsIgnoreCase("F") || App.getPatient().getPerson().getGender().equalsIgnoreCase("FEMALE"))
            genderValue = "2";

        Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
        int age = App.getDiffMonths(birthDate, new Date());

        Object[][] percentile = dbUtil.getFormTableData("select * from " + Metadata.WEIGHT_PERCENTILE + " where gender = '" + genderValue + "' and age = '" + age + "'");

        double weightInDouble = Double.parseDouble(weight);

        if(percentile == null || percentile.length == 0)
            return "";

        ///this Parse is not required if we store data in Double(when read )..
        double p3 = Double.parseDouble(String.valueOf(percentile[0][3]));
        double p5 = Double.parseDouble(String.valueOf(percentile[0][4]));
        double p10 = Double.parseDouble(String.valueOf(percentile[0][5]));
        double p25 = Double.parseDouble(String.valueOf(percentile[0][6]));
        double p50 = Double.parseDouble(String.valueOf(percentile[0][7]));
        double p75 = Double.parseDouble(String.valueOf(percentile[0][8]));
        double p90 = Double.parseDouble(String.valueOf(percentile[0][9]));
        double p95 = Double.parseDouble(String.valueOf(percentile[0][10]));
        double p97 = Double.parseDouble(String.valueOf(percentile[0][11]));

        if (weightInDouble <= p50) { //if weight or height less then  medium

            if (weightInDouble <= p3) {
                return "<=3rd Centile";
            } else if (weightInDouble <= p5) {
                return "<=5th Centile";
            } else if (weightInDouble <= p10) {
                return "Between 6-10th Centile";
            } else if (weightInDouble <= p25) {
                return "Between 11-25th Centile";
            } else if (weightInDouble <= p50) {
                return "Between 26-50th Centile";
            }

        } else if (weightInDouble > p50) {//if weight or height greater then  medium

            if (weightInDouble <= p75) {
                return "Between 51-75th Centile";
            } else if (weightInDouble <= p90) {
                return "Between 76-90th Centile";
            } else if (weightInDouble <= p95) {
                return "Between 91-95th Centile";
            } else if (weightInDouble <= p97) {
                return "Between 96-98th Centile";
            }
            else
                return "> 98th Centile";
        }
        return "";
    }

    public String mergePatient(String pid, String offlineFormId){

        String resultString = updatePatientDetails(pid, false);
        if(!resultString.equals("SUCCESS"))
            return resultString;

        dbUtil.delete(Metadata.FORM_JSON, "form_id=?", new String[]{offlineFormId});
        dbUtil.delete(Metadata.FORM, "id=?", new String[]{offlineFormId});
        dbUtil.delete(Metadata.FORM_VALUE, "form_id=?", new String[]{offlineFormId});

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, patient_id ", "identifier = '" + pid + "'");

        try {

            Object[][] encounterForms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.FORM_JSON + " where pid='" + String.valueOf(result[0][1]) + "'");
            for (int j = 0; j < encounterForms.length; j++) {
                Object[] encounterForm = encounterForms[j];

                if (String.valueOf(encounterForm[4]).contains("uuid-replacement-string")) {
                    String content = String.valueOf(encounterForm[4]).replace("uuid-replacement-string", String.valueOf(result[0][0]));

                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    dbUtil.update(Metadata.FORM_JSON, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                }

                if (String.valueOf(encounterForm[3]).contains("uuid-replacement-string")) {
                    String uri = String.valueOf(encounterForm[3]).replace("uuid-replacement-string", String.valueOf(result[0][0]));

                    ContentValues values = new ContentValues();
                    values.put("uri", uri);

                    dbUtil.update(Metadata.FORM_JSON, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                }

            }

        } catch (Exception e) {
            return "PARSER_ERROR";
        }

        return "SUCCESS";

    }

    public String searchPatient(String patientId){

        if (App.getCommunicationMode().equals("REST")) {

            com.ihsinformatics.gfatmmobile.model.Patient patient = null;

            String uuid = getPatientUuid(patientId);
            if (uuid == null)
                return "PATIENT_NOT_FOUND";


            JSONObject response = httpGet.getPatientByUuid(uuid);
            patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response, context);

            com.ihsinformatics.gfatmmobile.model.Person person = patient.getPerson();
            String identifier = patient.getPatientId();
            String fname = patient.getPerson().getGivenName();
            String lname = patient.getPerson().getFamilyName();
            String gender = patient.getPerson().getGender();
            String birthdate = patient.getPerson().getBirthdate();
            int age = patient.getPerson().getAge();


            String returnString = "PATIENT ALREADY EXISTS" + " ; ";
            returnString = returnString + identifier + " ; ";
            returnString = returnString + fname + " " + lname + " ; ";
            returnString = returnString + gender + " ; ";
            returnString = returnString + age + " ; ";
            returnString = returnString + birthdate + " ; ";

            return returnString;

        }
        return "";
    }

    public void resetScreeningCounts(){
        dbUtil.delete(Metadata.SCREENING_COUNT,null,null);
    }


    public void deletePatientId(String patientId){

        dbUtil.delete(Metadata.PATIENT, "patient_id=?", new String[]{patientId});
        dbUtil.delete(Metadata.PERSON_ATTRIBUTE, "patient_id=?", new String[]{patientId});
        deletePatientEncounters(patientId);
        dbUtil.delete(Metadata.TEST_ID, "pid=?", new String[]{patientId});
        ContentValues values = new ContentValues();
        values.put("p_id", "");
        dbUtil.update(Metadata.FORM, values, "p_id=?", new String[]{patientId});

    }

    public void deleteEarliestPatient(){

        String patientId = dbUtil.getObject(Metadata.PATIENT, "patient_id", "1=1 order by patient_id asc limit 1");
        deletePatientId(patientId);

    }


    public void scheduleBackupWithDefaultValues(){

        File path = new File(Environment.getExternalStorageDirectory(), "GFATM-BACKUP");
        path.mkdirs();
        MediaScannerConnection.scanFile(context, new String[] {path.toString()}, null, null);

        String Password = App.getPassword();
        String expiry = App.getExpiryPeriod();
        if(expiry.equals("")) return;
        int expiryDays = Integer.parseInt(expiry);

        final Params backupParams = new Params();
        backupParams.setDbName(DatabaseUtil.getDbName());
        backupParams.setStoragePath("//GFATM-BACKUP");
        backupParams.setNoOfExpiryDays(expiryDays);

        if(App.getBackupFrequency().equals(context.getString(R.string.daily))) {
            backupParams.setSchedule(Params.Schedule.DAILY);
        }
        else if(App.getBackupFrequency().equals(context.getString(R.string.weekly))) {
            backupParams.setSchedule(Params.Schedule.WEEKLY);
            int value = Integer.parseInt(App.getBackupDay());
            backupParams.setDay(value);
        }
        else if(App.getBackupFrequency().equals(context.getString(R.string.monthly))) {
            backupParams.setSchedule(Params.Schedule.MONTHLY);
        }

        backupParams.setKeepMonthlyBackup(false);
        backupParams.setEncryptDB(true);
        backupParams.setPassword(Password);

        String text = App.getBackupTime();
        backupParams.setTime(Integer.parseInt(text));

        Backup backup = new Backup(context);
        backup.setupService(backupParams);

    }

    public Object[][] getLocationsByTag(String tag){
        Object[][] locationsTag = dbUtil.getFormTableData("select id from " + Metadata.LOCATION_TAG + " where  name = '" + tag + "'" );
        if(locationsTag.length == 0){
            return locationsTag;
        }

        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description, contact_name from " + Metadata.LOCATION + " where  tags LIKE '%," + String.valueOf(locationsTag[0][0]) + ",%'" );
        return locations;
    }

    public Object[][] getLocationContactDetails(String tag){
        Object[][] locations = dbUtil.getFormTableData("select  primary_contact, contact_name from " + Metadata.LOCATION + " where  description LIKE '" + tag + "'" );
        return locations;
    }

    public boolean checkMetadata(){

        if(!dbUtil.doesTableExists("key_value"))
            return false;

        Object[][] locations = dbUtil.getFormTableData("select * from " + Metadata.KEY_VALUE + " where  key = 'METADATA'" );
        if(locations == null)
            return false;
        if(locations.length == 0)
            return false;

        return true;
    }

    public void updateLatestObsValue(String patientId, String conceptName, String value) {


        Object[][] obs = dbUtil.getFormTableData("select obs_id from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + conceptName + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs.length < 1)
            return ;

        ContentValues values = new ContentValues();
        values.put("value", value);
        dbUtil.update(Metadata.OBS, values, "obs_id=?", new String[]{String.valueOf(obs[0][0])});

    }

    public String getLatestObsValue(String patientId, String conceptName) {
        Object[][] obs = dbUtil.getFormTableData("select value from " + Metadata.OBS + ", " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and " + Metadata.ENCOUNTER + ".encounter_id=" + Metadata.OBS + ".encounter_id and conceptName = '" + conceptName + "' order by encounterDatetime DESC, dateCreated DESC");
        if (obs.length < 1)
            return null;
        return String.valueOf(obs[0][0]);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }

    public String getRelationshipBPersonName(String uuid) {
        try {

            String bPersonName = "";

            if(App.getMode().equalsIgnoreCase("OFFLINE"))
                return bPersonName;
            else {

                JSONObject relationship = httpGet.getRelationshipByUuid(uuid);
                if(relationship == null)
                    return null;
                JSONObject bPersonObject = relationship.getJSONObject("personB");

                if (!relationship.getBoolean("voided"))
                    bPersonName = bPersonObject.getString("display");

                return WordUtils.capitalize(bPersonName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String saveLabTestResult(String formName, String testShortName, String lab_ref_number, String orderUuid, String[][] obss, String id){

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.put("order", orderUuid);
            jsonObject.put("labReferenceNumber", lab_ref_number);

            String[][] result = dbUtil.getTableData(Metadata.TEST_TYPE, "uuid", "short_name = '" + testShortName + "'");
            if (result.length > 0)
                jsonObject.put("labTestType", result[0][0]);

            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < obss.length; i++){

                JSONObject jsonObject2 = new JSONObject();

                String[][] attribute = dbUtil.getTableData(Metadata.LAB_TEST_ATTRIBUTE_TYPE, "uuid,datatype", "name = '" + obss[i][0] + "'");

                if(attribute.length > 0) {
                    jsonObject2.put("attributeType", attribute[0][0]);

                    if(attribute[0][1].equals("org.openmrs.customdatatype.datatype.ConceptDatatype"))
                        jsonObject2.put("valueReference", getConceptMappingForConceptName(obss[i][1]));
                    else
                     jsonObject2.put("valueReference", obss[i][1]);
                }

                jsonArray.put(jsonObject2);

            }
            jsonObject.put("attributes", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String returnString = httpPost.saveOrderObject(jsonObject);

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            String[] uriArray = returnString.split(" ;;;; ");

            ContentValues values4 = new ContentValues();
            values4.put("form_id", Integer.valueOf(id));
            values4.put("uri", uriArray[0]);
            values4.put("content", uriArray[1]);
            values4.put("pid", App.getPatientId());
            values4.put("form",formName);
            values4.put("username", App.getUsername());
            dbUtil.insert(Metadata.FORM_JSON, values4);

        }

        if (returnString == null)
            return "ERROR";

        return "SUCCESS";

    }

    public String saveLabTestOrder(String uuidEncounter, String testShortName, String lab_ref_number, Calendar formDate, String encounterType, String id, String specimenType, String specimenSite) {

        JSONObject jsonObject = new JSONObject();

        try {
            String testTypeUuid = "";
            String testTypeConceptId = "";
            String testSpecimenRequired = "";
            String[][] result = dbUtil.getTableData(Metadata.TEST_TYPE, "uuid,concept_id,specimen_required", "short_name = '" + testShortName + "'");
            if (result.length > 0) {
                testTypeUuid = result[0][0];
                testTypeConceptId = result[0][1];
                testSpecimenRequired = result[0][2];
            }

            String conceptUUID = "";
            String[][] result2 = dbUtil.getTableData(Metadata.CONCEPT_MAPPING, "uuid", "concept_id = " + testTypeConceptId);
            if (result.length > 0)
                conceptUUID = result2[0][0];

            jsonObject.put("labReferenceNumber", lab_ref_number);
            jsonObject.put("labTestType", testTypeUuid);
            if (testSpecimenRequired.equals("Yes")) {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("specimenType", getConceptUuidAndDataType(specimenType)[0][0]);
                jsonObject2.put("specimenSite", getConceptUuidAndDataType(specimenSite)[0][0]);
                jsonObject2.put("collectionDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(formDate.getTime()));
                jsonObject2.put("status", "ACCEPTED");
                jsonObject2.put("collector", App.getProviderUUid());
                jsonObject2.put("labTest", "865e0c87-5c7e-4f71-86b7-5354a4a5a8c1");
                jsonArray.put(jsonObject2);
                jsonObject.put("labTestSamples", jsonArray);
            }
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("action", "NEW");
            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                jsonObject3.put("patient","uuid-replacement-string");
            else
                jsonObject3.put("patient",App.getPatient().getUuid());
            jsonObject3.put("concept", conceptUUID);
            jsonObject3.put("encounter", uuidEncounter);
            jsonObject3.put("careSetting", "6f0c9a92-6f24-11e3-af88-005056821db0");
            jsonObject3.put("type", "testorder");
            jsonObject3.put("orderer", App.getProviderUUid());

            jsonObject.put("order", jsonObject3);

            String returnString = httpPost.saveOrderObject(jsonObject);

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                String[] uriArray = returnString.split(" ;;;; ");

                ContentValues values4 = new ContentValues();
                values4.put("form_id", Integer.valueOf(id));
                values4.put("uri", uriArray[0]);
                values4.put("content", uriArray[1]);
                values4.put("pid", App.getPatientId());
                values4.put("form", encounterType);
                values4.put("username", App.getUsername());
                dbUtil.insert(Metadata.FORM_JSON, values4);

                return "SUCCESS_<lab-test-order-uuid>";

            } else  if (returnString != null){

                JSONObject jObject = JSONParser.getJSONObject("{" + returnString);
                JSONObject oObject = jObject.getJSONObject("order");

                return "SUCCESS_"+oObject.get("uuid");

            }

            if (returnString == null)
                return "ERROR";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "SUCCESS";


    }


    public String saveLabTestOrder(String shortName, String lab_ref_number, Calendar formDate, String encounterType, String id) {

        JSONObject jsonObject = new JSONObject();

        String uuid = "";

        if (App.getMode().equalsIgnoreCase("OFFLINE"))
            uuid = "<encounter-uuid-replacement>";
        else {
            uuid = getEncounterUUidByEncounterType(encounterType);
            if (uuid == null) return null;
        }


        try {
           /* String testTypeUuid = "";
            String testTypeConceptId = "";
            String[][] result = dbUtil.getTableData(Metadata.TEST_TYPE, "uuid,concept_id", "short_name = '" + shortName + "'");
            if (result.length > 0) {
                testTypeUuid = result[0][0];
                testTypeConceptId = result[0][1];
            }*/

            /*String conceptUUID = "";
            String[][] result2 = dbUtil.getTableData(Metadata.CONCEPT_MAPPING, "uuid", "concept_id = " + testTypeConceptId);
            if (result.length > 0)
                conceptUUID = result2[0][0];*/

            jsonObject.put("labReferenceNumber", lab_ref_number);
            //jsonObject.put("labTestType", testTypeUuid);
            jsonObject.put("labTestType", "4f4c97c8-61c3-4c4e-82bc-ef3e8abe8ffa");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("specimenType", "1000AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            jsonObject2.put("specimenSite", "161939AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            jsonObject2.put("collectionDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(formDate.getTime()));
            jsonObject2.put("status", "COLLECTED");
            jsonObject2.put("collector", App.getProviderUUid());
            jsonObject2.put("labTest", "865e0c87-5c7e-4f71-86b7-5354a4a5a8c1");
            jsonArray.put(jsonObject2);
            jsonObject.put("labTestSamples", jsonArray);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("action", "NEW");
            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                jsonObject3.put("patient","uuid-replacement-string");
            else
                jsonObject3.put("patient",App.getPatient().getUuid());
            //jsonObject3.put("concept", conceptUUID);
            jsonObject3.put("concept", "dcd97733-4262-4947-ac69-fd2d00880803");
            jsonObject3.put("encounter", uuid);
            jsonObject3.put("careSetting", "6f0c9a92-6f24-11e3-af88-005056821db0");
            jsonObject3.put("type", "testorder");
            jsonObject3.put("orderer", App.getProviderUUid());

            jsonObject.put("order", jsonObject3);

            String returnString = httpPost.saveQFTOrderObject(jsonObject);

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                String[] uriArray = returnString.split(" ;;;; ");

                ContentValues values4 = new ContentValues();
                values4.put("form_id", Integer.valueOf(id));
                values4.put("uri", uriArray[0]);
                values4.put("content", uriArray[1]);
                values4.put("pid", App.getPatientId());
                values4.put("form", Metadata.QFT_TEST);
                values4.put("username", App.getUsername());
                dbUtil.insert(Metadata.FORM_JSON, values4);

            }

            if (returnString == null)
                return "ERROR";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "SUCCESS";


    }


}
