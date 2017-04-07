
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
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.model.Concept;
import com.ihsinformatics.gfatmmobile.model.EncounterType;
import com.ihsinformatics.gfatmmobile.model.Location;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.model.PersonAttributeType;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Metadata;

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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        httpGet = new HttpGet(App.getIp(), App.getPort(), context);
        httpPost = new HttpPost(App.getIp(), App.getPort(), context);
        dbUtil = new DatabaseUtil(this.context);
    }
    /**
     * Checks to see if the client is connected to any network (GPRS/Wi-Fi)
     *
     * @return status
     */
    static public boolean isURLReachable() {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected()) {
//            try {
//                if(App.getSsl().equalsIgnoreCase("Enabled")) {
//                    HttpsClient httpsClient = new HttpsClient(context);
//                    httpsClient.
//                }
//                else {
//                    URL url = new URL("http://" + App.getIp() + ":" + App.getPort());
//                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                    urlc.setConnectTimeout(10 * 1000);
//                    urlc.connect();
//                    if (urlc.getResponseCode() == 200) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//
//            } catch (MalformedURLException e1) {
//                return false;
//            } catch (IOException e) {
//                return false;
//            }
//        }
        return true;
    }

    public String getPatientSystemIdByUuidLocalDB(String uuid) {

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "patient_id", "uuid = '" + uuid + "'");
        if (result.length > 0)
            return result[0][0];
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

            dbUtil.insert(Metadata.FORMS_VALUE, value);

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
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORMS + " where username='" + username + "' and program = '" + programName + "'");
        return forms;
    }

    public Object[][] getSavedForms(int id) {
        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORMS + " where id = " + id + "");
        return forms;
    }

    public boolean deleteForms(String id) {

        Object[][] forms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORMS + " where id='" + id + "'");

        dbUtil.delete(Metadata.FORMS, "id=?", new String[]{id});
        dbUtil.delete(Metadata.FORMS_VALUE, "form_id=?", new String[]{id});

        if (String.valueOf(forms[0][2]).equals("CREATE PATIENT")) {

            Object[][] childForms = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORMS + " where p_id='" + String.valueOf(forms[0][3]) + "'");

            for (int j = 0; j < childForms.length; j++) {
                dbUtil.delete(Metadata.FORMS, "id=?", new String[]{String.valueOf(childForms[j][0])});
                dbUtil.delete(Metadata.FORMS_VALUE, "form_id=?", new String[]{String.valueOf(childForms[j][0])});
                dbUtil.delete(Metadata.OFFLINE_FORM, "form_id=?", new String[]{String.valueOf(childForms[j][0])});
            }
        }

        return dbUtil.delete(Metadata.OFFLINE_FORM, "form_id=?", new String[]{id});

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

    public Object[][] getAllLocations() {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description from " + Metadata.LOCATION);
        return locations;
    }

    public Object[][] getAllLocations(String programColumn) {
        Object[][] locations = dbUtil.getFormTableData("select location_id, location_name, uuid, parent_uuid, fast_location, pet_location, childhood_tb_location, comorbidities_location, pmdt_location, aic_location, primary_contact, address1, address2, city_village, state_province, county_district, description from " + Metadata.LOCATION + " where " + programColumn + " = 'Y' and state_province = '" + App.getProvince() + "'");
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
            return "SUCCESS";
        }
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

            JSONObject j = jsonObjects[0];

            User user = User.parseJSONObject(j);

            String providerUUid = "";
            JSONObject provider = httpGet.getProviderByUserId(user.getIdentifier());
            if (provider == null)
                return "AUTHENTICATION_ERROR";
            try {
                JSONObject[] providerObjects = JSONParser.getJSONArrayFromObject(provider, "results");
                if (providerObjects == null)
                    return "PROVIDER_NOT_FOUND";
                if (providerObjects.length == 0)
                    return "PROVIDER_NOT_FOUND";
                providerUUid = providerObjects[0].getString("uuid");
            } catch (JSONException e) {
                providerUUid = "";
                e.printStackTrace();
            }

            if (providerUUid == "")
                return "PROVIDER_NOT_FOUND";

            App.setUserFullName(user.getFullName());
            App.setRoles(user.getRoles());
            App.setProviderUUid(providerUUid);

            deleteUserByUuid(user.getUuid());

            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("fullName", user.getFullName());
            values.put("uuid", user.getUuid());
            values.put("password", App.getPassword());
            values.put("role", App.getRoles());
            values.put("provider_uuid", providerUUid);
            dbUtil.insert(Metadata.USERS, values);

        }
        return "SUCCESS";
    }


    public String getLocations() {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {

            JSONArray response = httpGet.getAllLocations();
            if (response == null)
                return "AUTHENTICATION_ERROR";

            String columnName = "";
            if (App.getProgram().equals(context.getResources().getString(R.string.pet)))
                columnName = "pet_location";
            else if (App.getProgram().equals(context.getResources().getString(R.string.fast)))
                columnName = "fast_location";
            else if (App.getProgram().equals(context.getResources().getString(R.string.comorbidities)))
                columnName = "comorbidities_location";
            else if (App.getProgram().equals(context.getResources().getString(R.string.pmdt)))
                columnName = "pmdt_location";
            else if (App.getProgram().equals(context.getResources().getString(R.string.childhood_tb)))
                columnName = "childhood_tb_location";

            deleteLocationsByProgram(columnName);
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

                    if (App.getProgram().equals(context.getResources().getString(R.string.pet)) && location.getPetLocation().equals("Y"))
                        columnName = "pet_location";
                    else if (App.getProgram().equals(context.getResources().getString(R.string.fast)) && location.getFastLocation().equals("Y"))
                        columnName = "fast_location";
                    else if (App.getProgram().equals(context.getResources().getString(R.string.comorbidities)) && location.getComorbiditiesLocation().equals("Y"))
                        columnName = "comorbidities_location";
                    else if (App.getProgram().equals(context.getResources().getString(R.string.pmdt)))
                        columnName = "pmdt_location";
                    else if (App.getProgram().equals(context.getResources().getString(R.string.childhood_tb)))
                        columnName = "childhood_tb_location";

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
            if (response == null)
                return "AUTHENTICATION_ERROR";

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
            if (response == null)
                return "AUTHENTICATION_ERROR";

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
            if (response == null)
                return "AUTHENTICATION_ERROR";

            deleteAllPersonAttributeTypes();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    PersonAttributeType personAttributeType = PersonAttributeType.parseJSONObject(jsonobject);

                    String name = personAttributeType.getName();
                    String uuid = personAttributeType.getUuid();

                    ContentValues values = new ContentValues();
                    values.put("person_attribute_type", name);
                    values.put("uuid", uuid);
                    dbUtil.insert(Metadata.PERSON_ATTRIBUTE_TYPE, values);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return "SUCCESS";
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
            if (!App.getMode().equalsIgnoreCase("OFFLINE"))
                uuid = getPatientUuid(patientId);
            else {
                com.ihsinformatics.gfatmmobile.model.Patient patient = getPatientByIdentifierFromLocalDB(patientId);
                if (patient == null)
                    uuid = null;
                else
                    uuid = patient.getUuid();
            }
            if (uuid != null)
                return "DUPLICATE";
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

                        ContentValues values3 = new ContentValues();
                        values3.put("identifier", patientId);
                        values3.put("first_name", givenName);
                        values3.put("last_name", familyName);
                        values3.put("gender", gender);
                        values3.put("birthdate", dob);
                        dbUtil.insert(Metadata.PATIENT, values3);

                        getPatient(patientId, true);

                        ContentValues values5 = new ContentValues();
                        values5.put("program", App.getProgram());
                        values5.put("form_name", "CREATE PATIENT");
                        values5.put("p_id", App.getPatientId());
                        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String formDate = formatter.format(new Date());
                        values5.put("form_date", formDate);
                        Date date = new Date();
                        values5.put("timestamp", date.getTime());
                        values5.put("location", App.getLocation());
                        values5.put("username", App.getUsername());
                        dbUtil.insert(Metadata.FORMS, values5);

                        String formId = dbUtil.getObject(Metadata.FORMS, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

                        ContentValues values6 = new ContentValues();
                        values6.put("field_name", "identifier");
                        values6.put("value", patientId);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORMS_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "name");
                        values6.put("value", givenName + " " + familyName);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORMS_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "gender");
                        values6.put("value", gender);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORMS_VALUE, values6);

                        values6 = new ContentValues();
                        values6.put("field_name", "dob");
                        values6.put("value", dob);
                        values6.put("form_id", formId);
                        dbUtil.insert(Metadata.FORMS_VALUE, values6);

                        String uri = httpPost.savePatientByEntitiy(patient);
                        String[] uriArray = uri.split(" ;;;; ");

                        ContentValues values1 = new ContentValues();
                        values1.put("uri", uriArray[0]);
                        values1.put("content", uriArray[1]);
                        values1.put("form", "CREATE PERSON");
                        values1.put("username", App.getUsername());
                        values1.put("form_id", formId);
                        values1.put("pid", App.getPatientId());
                        dbUtil.insert(Metadata.OFFLINE_FORM, values1);

                        ContentValues values2 = new ContentValues();
                        values2.put("uri", uriArray[2]);
                        values2.put("content", uriArray[3]);
                        values2.put("form", "CREATE PATIENT");
                        values2.put("username", App.getUsername());
                        values2.put("form_id", formId);
                        values2.put("pid", App.getPatientId());
                        dbUtil.insert(Metadata.OFFLINE_FORM, values2);

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
        String uuid = null;
        try {
            JSONArray uuids = httpGet.getPatientUuidByPatientId(patientId);
            if (uuids.length() > 0) {
                JSONObject jsonobject = uuids.getJSONObject(0);
                uuid = jsonobject.getString("uuid");
            }

        } catch (Exception e) {
            return uuid;
        }
        return uuid;
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

                if (patient == null && App.getMode().equalsIgnoreCase("OFFLINE"))
                    return "PATIENT_NOT_FOUND";

                if (patient == null) {
                    String uuid = getPatientUuid(patientId);
                    if (uuid == null)
                        return "PATIENT_NOT_FOUND";
                    else {

                        JSONObject response = httpGet.getPatientByUuid(uuid);
                        patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response);

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
                        String birthplace = patient.getPerson().getBirthPlace();
                        String citizenship = patient.getPerson().getCitizenship();
                        String maritalStatus = patient.getPerson().getMaritalStatus();
                        String healthCenter = patient.getPerson().getHealthCenter();
                        String healthDistrict = patient.getPerson().getHealthDistrict();
                        String motherName = patient.getPerson().getMotherName();
                        String primaryContact = patient.getPerson().getPrimaryContact();
                        String primaryContactOwner = patient.getPerson().getPrimaryContactOwner();
                        String secondaryContact = patient.getPerson().getSecondaryContact();
                        String secondaryContactOwner = patient.getPerson().getSecondaryContactOwner();
                        String ethnicity = patient.getPerson().getEthnicity();
                        String educationLevel = patient.getPerson().getEducationLevel();
                        String employmentStatus = patient.getPerson().getEmploymentStatus();
                        String occupation = patient.getPerson().getOccupation();
                        String incomeClass = patient.getPerson().getIncomeClass();
                        String motherTongue = patient.getPerson().getMotherTongue();
                        String nationalId = patient.getPerson().getNationalId();
                        String nationalIdOwner = patient.getPerson().getNationalIdOwner();
                        String guardianName = patient.getPerson().getGuardianName();
                        String address1 = patient.getPerson().getAddress1();
                        String address2 = patient.getPerson().getAddress2();
                        String address3 = patient.getPerson().getAddress3();
                        String stateProvince = patient.getPerson().getStateProvince();
                        String countyDistict = patient.getPerson().getCountyDistrict();
                        String cityVillage = patient.getPerson().getCityVillage();
                        String country = patient.getPerson().getCountry();

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
                        values.put("birthplace", birthplace);
                        values.put("citizenship", citizenship);
                        values.put("maritalstatus", maritalStatus);
                        values.put("healthcenter", healthCenter);
                        values.put("healthdistrict", healthDistrict);
                        values.put("mothername", motherName);
                        values.put("primarycontact", primaryContact);
                        values.put("primarycontactowner", primaryContactOwner);
                        values.put("secondarycontact", secondaryContact);
                        values.put("secondarycontactowner", secondaryContactOwner);
                        values.put("ethnicity", ethnicity);
                        values.put("educationlevel", educationLevel);
                        values.put("employmentstatus", employmentStatus);
                        values.put("occupation", occupation);
                        values.put("incomeclass", incomeClass);
                        values.put("mothertongue", motherTongue);
                        values.put("nationalid", nationalId);
                        values.put("nationalidowner", nationalIdOwner);
                        values.put("guardianname", guardianName);
                        values.put("address1", address1);
                        values.put("address2", address2);
                        values.put("address3", address3);
                        values.put("stateProvince", stateProvince);
                        values.put("cityVillage", cityVillage);
                        values.put("countyDistrict", countyDistict);
                        values.put("country", country);
                        dbUtil.insert(Metadata.PATIENT, values);

                        if (select) {
                            App.setPatientId(getPatientSystemIdByUuidLocalDB(uuid));
                            App.setPatient(patient);
                        }

                        Object[][] encounterTypes = getEncounterTypesFromLocalDBByProgramName(App.getProgram());

                        for (Object[] encType : encounterTypes) {

                            if (!String.valueOf(encType[1]).startsWith(App.getProgram()))
                                continue;

                            if (String.valueOf(encType[1]).contains("Test Order")) {
                                JSONArray jsonArray = httpGet.getAllEncountersByPatientAndEncounterType(uuid, String.valueOf(encType[0]));
                                if (jsonArray == null || jsonArray.length() < 1)
                                    continue;

                                List<String> newArr = new ArrayList<String>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newObj = jsonArray.getJSONObject(i);
                                    String date = newObj.getJSONObject("auditInfo").getString("dateCreated");
                                    String uuid1 = newObj.getString("uuid");
                                    date = date.replace("T", " ");
                                    String[] arr = date.split("\\+");
                                    newArr.add(arr[0] + "," + i);

                                    com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);
                                    encounter.setPatientId(App.getPatientId());

                                    for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter.getObsGroup()) {

                                        if (obs.getConceptName().equals("TEST ID")) {
                                            ContentValues values2 = new ContentValues();
                                            values2.put("pid", encounter.getPatientId());
                                            values2.put("form", encounter.getEncounterType());
                                            values2.put("test_id", obs.getValue());
                                            dbUtil.insert(Metadata.TEST_ID, values2);
                                        }
                                    }

                                }
                                Collections.sort(newArr, new Comparator<String>() {
                                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                                    @Override
                                    public int compare(String o1, String o2) {
                                        try {
                                            String[] b1 = o1.split(",");
                                            String[] b2 = o2.split(",");
                                            return f.parse(b1[0]).compareTo(f.parse(b2[0]));
                                        } catch (Exception e) {
                                            throw new IllegalArgumentException(e);
                                        }
                                    }
                                });
                                String[] splitter = newArr.get(newArr.size() - 1).split(",");
                                JSONObject newObj = jsonArray.getJSONObject(Integer.valueOf(splitter[1]));

                                com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);
                                encounter.setPatientId(App.getPatientId());

                                ContentValues values1 = new ContentValues();
                                values1.put("uuid", encounter.getUuid());
                                values1.put("encounterType", encounter.getEncounterType());
                                values1.put("encounterDatetime", encounter.getEncounterDatetime());
                                values1.put("encounterLocation", encounter.getEncounterLocation());
                                values1.put("patientId", encounter.getPatientId());
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

                            } else {
                                JSONObject jsonObject = httpGet.getLatestEncounter(uuid, String.valueOf(encType[0]));
                                if (jsonObject == null)
                                    continue;
                                com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                                encounter.setPatientId(getPatientSystemIdByUuidLocalDB(uuid));

                                ContentValues values1 = new ContentValues();
                                values1.put("uuid", encounter.getUuid());
                                values1.put("encounterType", encounter.getEncounterType());
                                values1.put("encounterDatetime", encounter.getEncounterDatetime());
                                values1.put("encounterLocation", encounter.getEncounterLocation());
                                values1.put("patientId", encounter.getPatientId());
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
                        }
                    }
                } else {

                    if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
                        String patientUuid = patient.getUuid();
                        if (patientUuid == null || patientUuid.equals(""))
                            return "OFFLINE_PATIENT";
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

        String[][] result = dbUtil.getTableData(Metadata.LOCATION, "uuid", "location_name = '" + location + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public String getOfflineFormIdForPatientCreation(String pid) {

        String[][] result = dbUtil.getTableData(Metadata.OFFLINE_FORM, "id", "pid = '" + pid + "' and form = 'CREATE PATIENT' and username = '" + App.getUsername() + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;
    }

    public String getPersonAttributeTypeUuid(String personAttributeType) {

        String[][] result = dbUtil.getTableData(Metadata.PERSON_ATTRIBUTE_TYPE, "uuid", "person_attribute_type = '" + personAttributeType + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public String[][] getConceptUuidAndDataType(String concept_name) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return null;
            }
        }

        String[][] result = dbUtil.getTableData(Metadata.CONCEPT, "uuid,data_type", "full_name = '" + concept_name + "'");
        if (result.length > 0)
            return result;
        else {

            if (App.getMode().equalsIgnoreCase("OFFLINE"))
                return null;

            JSONObject jsonobject = httpGet.getConceptByName(concept_name);
            if (jsonobject == null)
                return null;
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
        String[][] result = dbUtil.getTableData(Metadata.ENCOUNTER_TYPE, "uuid", "encounter_type = '" + App.getProgram() + "-" + type + "'");
        if (result.length > 0)
            return result[0][0];
        else {
            String fn = App.getProgram() + "-" + type;
            JSONObject jsonobject = httpGet.getEncounterTypeByName(fn);
            if (jsonobject == null)
                return "ERROR RETRIEVING ENCOUNTER TYPE";
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(jsonobject, "results");
            for (JSONObject json : jsonObjects) {
                EncounterType eT = EncounterType.parseJSONObject(json);

                ContentValues values = new ContentValues();
                values.put("encounter_type", eT.getName());
                values.put("uuid", eT.getUuid());
                dbUtil.insert(Metadata.ENCOUNTER_TYPE, values);

                if (fn.equals(eT.getName()))
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
                "birthplace, citizenship, maritalstatus, healthcenter, healthdistrict, " +  // 5 - 9
                "mothername, primarycontact, primarycontactOwner, secondarycontact, secondarycontactOwner, tertiarycontact, quaternarycontact, ethnicity, " + // 10 - 17
                "educationlevel, employmentstatus, occupation, incomeclass, mothertongue, nationalid, nationalidowner, guardianname, " + // 18 - 25
                "identifier, external_id, enrs, endtb_emr_id, " +  // 26 - 29
                "address1, address2, address3, stateProvince, countyDistrict, cityVillage, country, patient_id ", "patient_id = '" + id + "'"); //30 - 37

        Date date = App.stringToDate(result[0][3], "yyyy-MM-dd");
        int age = App.getDiffYears(date, new Date());
        com.ihsinformatics.gfatmmobile.model.Person person1 = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][1], result[0][2], age, result[0][3], result[0][4],
                result[0][5], result[0][6], result[0][7], result[0][8], result[0][9], result[0][10], result[0][11], result[0][12], result[0][13], result[0][14],
                result[0][15], result[0][16], result[0][17], result[0][18], result[0][19], result[0][20], result[0][21], result[0][22], result[0][23], result[0][24], result[0][25],
                result[0][30], result[0][31], result[0][32], result[0][33], result[0][34], result[0][35], result[0][36]);

        patient = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][26], result[0][27], result[0][28], result[0][29], person1);
        patient.setPid(Integer.valueOf(result[0][37]));

        return patient;

    }

    public com.ihsinformatics.gfatmmobile.model.Patient getPatientByIdentifierFromLocalDB(String patientId) {

        com.ihsinformatics.gfatmmobile.model.Patient patient = null;

        if (patientId == null || patientId.equals(""))
            return patient;

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, first_name, last_name, birthdate, gender, " +  // 0 - 4
                "birthplace, citizenship, maritalstatus, healthcenter, healthdistrict, " +  // 5 - 9
                "mothername, primarycontact, primarycontactOwner, secondarycontact, secondarycontactOwner, tertiarycontact, quaternarycontact, ethnicity, " + // 10 - 17
                "educationlevel, employmentstatus, occupation, incomeclass, mothertongue, nationalid, nationalidowner, guardianname, " + // 18 - 25
                "identifier, external_id, enrs, endtb_emr_id, " +  // 26 - 29
                "address1, address2, address3, stateProvince, countyDistrict, cityVillage, country, patient_id ", "identifier = '" + patientId + "'"); //30 - 37

        if (result.length < 1)
            return null;

        Date date = App.stringToDate(result[0][3], "yyyy-MM-dd");
        int age = App.getDiffYears(date, new Date());
        com.ihsinformatics.gfatmmobile.model.Person person1 = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][1], result[0][2], age, result[0][3], result[0][4],
                result[0][5], result[0][6], result[0][7], result[0][8], result[0][9], result[0][10], result[0][11], result[0][12], result[0][13], result[0][14],
                result[0][15], result[0][16], result[0][17], result[0][18], result[0][19], result[0][20], result[0][21], result[0][22], result[0][23], result[0][24], result[0][25],
                result[0][30], result[0][31], result[0][32], result[0][33], result[0][34], result[0][35], result[0][36]);

        com.ihsinformatics.gfatmmobile.model.Patient patient1 = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][26], result[0][27], result[0][28], result[0][29], person1);
        patient1.setPid(Integer.valueOf(result[0][37]));

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

                    deleteEncounter(App.getPatientId(), App.getProgram() + "-" + formName);

                    ContentValues values2 = new ContentValues();
                    values2.put("encounterType", App.getProgram() + "-" + formName);
                    values2.put("encounterDatetime", App.getSqlDate(encounterDateTime));
                    values2.put("encounterLocation", App.getLocation());
                    values2.put("patientId", App.getPatientId());
                    dbUtil.insert(Metadata.ENCOUNTER, values2);

                    String id = dbUtil.getObject(Metadata.ENCOUNTER, "encounter_id", "encounterType='" + App.getProgram() + "-" + formName + "' and patientId=" + App.getPatientId());

                    ContentValues values5 = new ContentValues();
                    values5.put("program", App.getProgram());
                    values5.put("form_name", formName);
                    values5.put("p_id", App.getPatientId());
                    values5.put("form_date", App.getSqlDate(encounterDateTime));
                    Date date = new Date();
                    values5.put("timestamp", date.getTime());
                    values5.put("location", App.getLocation());
                    values5.put("encounter_id", id);
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
                    dbUtil.insert(Metadata.FORMS, values5);

                    String formId = dbUtil.getObject(Metadata.FORMS, "id", "p_id='" + App.getPatientId() + "' and timestamp='" + date.getTime() + "'");

                    for (int i = 0; i < obss.length; i++) {

                        if (obss[i][1].contains(" ; ")) {
                            String[] valueArray = obss[i][1].split(" ; ");
                            for (int j = 0; j < valueArray.length; j++) {
                                ContentValues values3 = new ContentValues();
                                values3.put("conceptName", obss[i][0]);
                                values3.put("value", valueArray[j]);
                                values3.put("encounter_id", id);
                                dbUtil.insert(Metadata.OBS, values3);

                                ContentValues values6 = new ContentValues();
                                values6.put("field_name", obss[i][0]);
                                values6.put("value", valueArray[j]);
                                values6.put("form_id", formId);
                                dbUtil.insert(Metadata.FORMS_VALUE, values6);

                            }

                        } else {
                            ContentValues values3 = new ContentValues();
                            values3.put("conceptName", obss[i][0]);
                            values3.put("value", obss[i][1]);
                            values3.put("encounter_id", id);
                            dbUtil.insert(Metadata.OBS, values3);

                            ContentValues values6 = new ContentValues();
                            values6.put("field_name", obss[i][0]);
                            values6.put("value", obss[i][1]);
                            values6.put("form_id", formId);
                            dbUtil.insert(Metadata.FORMS_VALUE, values6);
                        }
                    }

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", formId);
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", App.getProgram() + "-" + formName);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.OFFLINE_FORM, values4);

                    if (!testId.equals("")) {
                        ContentValues values = new ContentValues();
                        values.put("pid", App.getPatientId());
                        values.put("form", App.getProgram() + "-" + formName);
                        values.put("test_id", testId);
                        dbUtil.insert(Metadata.TEST_ID, values);
                    }

                    return "SUCCESS_" + formId;

                } else {
                    String returnString = httpPost.saveEncounterWithObservationByEntity(encounter);
                    JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                    com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                    encounter1.setPatientId(App.getPatientId());

                    deleteEncounter(App.getPatientId(), encounter1.getEncounterType());

                    ContentValues values2 = new ContentValues();
                    values2.put("uuid", encounter1.getUuid());
                    values2.put("encounterType", encounter1.getEncounterType());
                    values2.put("encounterDatetime", encounter1.getEncounterDatetime());
                    values2.put("encounterLocation", encounter1.getEncounterLocation());
                    values2.put("patientId", encounter1.getPatientId());
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

                if (!testId.equals("")) {
                    ContentValues values2 = new ContentValues();
                    values2.put("pid", App.getPatientId());
                    values2.put("form", App.getProgram() + "-" + formName);
                    values2.put("test_id", testId);
                    dbUtil.insert(Metadata.TEST_ID, values2);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return "POST ERROR";
            }
        }
        return response;
    }


    public String savePersonAttributeType(String attributeType, String value, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }
        if (App.getCommunicationMode().equals("REST")) {
            try {

                String personAttributeTypeUuid = getPersonAttributeTypeUuid(attributeType);

                String patientUuid = "";
                if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                    patientUuid = "uuid-replacement-string";
                else
                    patientUuid = App.getPatient().getUuid();

                String uri = httpPost.savePersonAttribute(personAttributeTypeUuid, value, patientUuid);

                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ATTRIBUTE);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.OFFLINE_FORM, values4);

                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(attributeType.replace(" ", "").toLowerCase(), value);

                dbUtil.update(Metadata.PATIENT, contentValues, "patient_Id=?", new String[]{App.getPatientId()});
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

                String[][] data = dbUtil.getTableData(Metadata.PATIENT, idType, "uuid = '" + App.getPatient().getUuid() + "'");
                if (data[0][0] == null || data[0][0].equals("")) {

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
                        values4.put("form", Metadata.PATIENT_IDENTIFIER);
                        values4.put("username", App.getUsername());
                        dbUtil.insert(Metadata.OFFLINE_FORM, values4);

                        ContentValues values6 = new ContentValues();
                        values6.put("field_name", identifierType);
                        values6.put("value", identifier);
                        values6.put("form_id", encounterId);
                        dbUtil.insert(Metadata.FORMS_VALUE, values6);
                    }

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(idType, identifier);

                    dbUtil.update(Metadata.PATIENT, contentValues, "uuid=?", new String[]{App.getPatient().getUuid()});
                    App.setPatient(getPatientBySystemIdFromLocalDB(App.getPatientId()));

                }


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
                personAddress.setPreferred(false);
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
                if (App.getMode().equalsIgnoreCase("OFFLINE")) {
                    String[] uriArray = uri.split(" ;;;; ");

                    ContentValues values4 = new ContentValues();
                    values4.put("form_id", Integer.valueOf(encounterId));
                    values4.put("uri", uriArray[0]);
                    values4.put("content", uriArray[1]);
                    values4.put("pid", App.getPatientId());
                    values4.put("form", Metadata.PERSON_ADDRESS);
                    values4.put("username", App.getUsername());
                    dbUtil.insert(Metadata.OFFLINE_FORM, values4);

                }

                ContentValues contentValues = new ContentValues();
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

    public String saveProgramEnrollement(String enrollementDate, String encounterId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                String programUuid = "";
                if (App.getProgram().equalsIgnoreCase("PET"))
                    programUuid = "PET";
                else if (App.getProgram().equalsIgnoreCase("PMDT"))
                    programUuid = "PMDT";
                else if (App.getProgram().equalsIgnoreCase("FAST"))
                    programUuid = "FAST";
                else if (App.getProgram().equalsIgnoreCase("Childhood TB"))
                    programUuid = "ChildhoodTB";
                else if (App.getProgram().equalsIgnoreCase("Comorbidities"))
                    programUuid = "Comorbidities";

                programUuid = getProgramUuidFromProgramName(programUuid);
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
                    dbUtil.insert(Metadata.OFFLINE_FORM, values4);

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

    public Object[][] getOfflineFormByFormAndEncounter(String formName, int encounterId) {

        Object[][] form = dbUtil.getFormTableData("select content from " + Metadata.OFFLINE_FORM + " where form = '" + formName + "' and encounter_id = " + encounterId);
        return form;
    }

    public OfflineForm getOfflineFormById(int formId) {

        Object[][] form = dbUtil.getFormTableData("select id, program, form_name, p_id, form_date, timestamp, form_object, location, encounter_id, username from " + Metadata.FORMS + " where id = " + formId);
        if (form.length < 1)
            return null;
        OfflineForm fo = new OfflineForm(String.valueOf(form[0][0]), String.valueOf(form[0][1]), String.valueOf(form[0][2]), String.valueOf(form[0][3]), String.valueOf(form[0][4]), String.valueOf(form[0][5]),
                String.valueOf(form[0][6]), String.valueOf(form[0][7]), String.valueOf(form[0][8]), String.valueOf(form[0][9]));

        Object[][] obs = dbUtil.getFormTableData("select field_name, value from " + Metadata.FORMS_VALUE + " where form_id = " + String.valueOf(form[0][0]));
        for (int i = 0; i < obs.length; i++) {
            fo.putObsValue(String.valueOf(obs[i][0]), String.valueOf(obs[i][1]));
        }

        return fo;
    }

    public Object[][] getAllEncounterFromLocalDB(String programName) {

        if (App.getPatient() == null)
            return null;
        Object[][] encounter = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation from " + Metadata.ENCOUNTER + " where patientId='" + App.getPatientId() + "' and encounterType like '" + programName + "%'");
        return encounter;

    }

    public Object[][] getAllObsFromEncounterId(int encounterId) {

        if (App.getPatient() == null)
            return null;
        Object[][] encounter = dbUtil.getFormTableData("select value, conceptName from " + Metadata.OBS + " where encounter_id=" + encounterId + "");
        return encounter;

    }

    public Object[][] getUserFromLoccalDB(String username) {

        Object[][] user = dbUtil.getFormTableData("select provider_uuid, username, fullName, role, password from " + Metadata.USERS + " where username='" + username + "'");
        return user;

    }

    public String getObsValue(String patientId, String encounterType, String conceptName) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where patientId=" + patientId + " and encounterType = '" + encounterType + "'");
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

    public boolean deleteEncounter(String patientId, String encounterType) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' and encounterType = '" + encounterType + "'");
        if (encounter.length < 1)
            return false;
        Boolean flag = dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounter[0][0])});
        if (!flag) return flag;
        return dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounter[0][0])});
    }

    public boolean deletePatientEncounterByProgram(String patientId, String programName) {
        Object[][] encounter = dbUtil.getFormTableData("select encounter_id from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' and encounterType like '" + programName + "%'");
        if (encounter.length < 1)
            return false;
        for (int i = 0; i < encounter.length; i++) {
            Boolean flag = dbUtil.delete(Metadata.ENCOUNTER, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
            if (!flag) return flag;
            dbUtil.delete(Metadata.OBS, "encounter_id=?", new String[]{String.valueOf(encounter[i][0])});
        }
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

    public String submitOfflineForm(String formId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equals("REST")) {
            Object[][] forms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.OFFLINE_FORM + " where form_id='" + formId + "'");

            for (int i = 0; i < forms.length; i++) {

                Object[] form = forms[i];

                if (String.valueOf(form[1]).contains("CREATE")) {

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));

                    if (returnString == null)
                        return "POST_ERROR";

                    try {
                        JSONObject newPerson = JSONParser.getJSONObject("{"
                                + returnString.toString() + "}");

                        i++;
                        form = forms[i];
                        String patientContent = String.valueOf(form[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));
                        returnString = httpPost.backgroundPost(String.valueOf(form[3]), patientContent);

                        if (returnString == null)
                            return "POST_ERROR";

                        Object[][] encounterForms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.OFFLINE_FORM + " where pid='" + String.valueOf(form[2]) + "'");
                        for (int j = 0; j < encounterForms.length; j++) {
                            Object[] encounterForm = encounterForms[j];

                            if (String.valueOf(form[4]).contains("uuid-replacement-string")) {
                                String content = String.valueOf(encounterForm[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));

                                ContentValues values = new ContentValues();
                                values.put("content", content);

                                dbUtil.update(Metadata.OFFLINE_FORM, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                            }

                        }

                        ContentValues values = new ContentValues();
                        values.put("uuid", String.valueOf(newPerson.get("uuid")));

                        JSONObject json = JSONParser.getJSONObject(patientContent);

                        dbUtil.update(Metadata.PATIENT, values, "identifier=?", new String[]{String.valueOf(json.get("identifier"))});

                    } catch (Exception e) {
                        return "PARSER_ERROR";
                    }

                } else if (String.valueOf(form[1]).equals(Metadata.PERSON_ATTRIBUTE) || String.valueOf(form[1]).equals(Metadata.PERSON_ADDRESS) || String.valueOf(form[1]).equals(Metadata.PATIENT_IDENTIFIER) || String.valueOf(form[1]).equals(Metadata.PROGRAM)) {
                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";
                } else {

                    String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));
                    if (returnString == null)
                        return "POST_ERROR";

                    try {
                        JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                        com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);

                        deleteEncounter(App.getPatientId(), encounter1.getEncounterType());

                        ContentValues values2 = new ContentValues();
                        values2.put("uuid", encounter1.getUuid());
                        values2.put("encounterType", encounter1.getEncounterType());
                        values2.put("encounterDatetime", encounter1.getEncounterDatetime());
                        values2.put("encounterLocation", encounter1.getEncounterLocation());
                        values2.put("patientId", String.valueOf(form[2]));
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


                    } catch (Exception e) {
                        return "PARSER_ERROR";
                    }

                }
            }

            dbUtil.delete(Metadata.OFFLINE_FORM, "form_id=?", new String[]{formId});
            dbUtil.delete(Metadata.FORMS, "id=?", new String[]{formId});
            dbUtil.delete(Metadata.FORMS_VALUE, "form_id=?", new String[]{formId});
        }

        return "SUCCESS";

    }

    public void submitOfflineForms() {

        Object[][] forms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.OFFLINE_FORM + " where username='" + App.getUsername() + "'");

        for (int i = 0; i < forms.length; i++) {

            Object[] form = forms[i];

            if (String.valueOf(form[1]).contains("CREATE")) {

                String returnString = httpPost.backgroundPost(String.valueOf(form[3]), String.valueOf(form[4]));

                String id = String.valueOf(form[0]);

                try {
                    JSONObject newPerson = JSONParser.getJSONObject("{"
                            + returnString.toString() + "}");

                    i++;
                    form = forms[i];
                    String patientContent = String.valueOf(form[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));
                    httpPost.backgroundPost(String.valueOf(form[3]), patientContent);

                    dbUtil.delete(Metadata.OFFLINE_FORM, "form_id=?", new String[]{String.valueOf(form[5])});
                    dbUtil.delete(Metadata.FORMS, "id=?", new String[]{String.valueOf(form[5])});

                    Object[][] encounterForms = dbUtil.getFormTableData("select id, form, pid, uri, content, form_id from " + Metadata.OFFLINE_FORM + " where pid='" + String.valueOf(form[2]) + "'");
                    for (int j = 0; j < encounterForms.length; j++) {
                        Object[] encounterForm = encounterForms[j];

                        if (String.valueOf(form[4]).contains("uuid-replacement-string")) {
                            String content = String.valueOf(form[4]).replace("uuid-replacement-string", String.valueOf(newPerson.get("uuid")));

                            ContentValues values = new ContentValues();
                            values.put("content", content);

                            dbUtil.update(Metadata.OFFLINE_FORM, values, "id=?", new String[]{String.valueOf(encounterForm[0])});
                        }

                    }


                    ContentValues values = new ContentValues();
                    values.put("uuid", String.valueOf(newPerson.get("uuid")));

                    JSONObject json = JSONParser.getJSONObject(patientContent);

                    dbUtil.update(Metadata.PATIENT, values, "identifier=?", new String[]{String.valueOf(json.get("identifier"))});

                } catch (Exception e) {

                }

            } else {

                com.ihsinformatics.gfatmmobile.model.Patient patient = getPatientBySystemIdFromLocalDB(String.valueOf(form[2]));
                String patientContent = String.valueOf(form[4]).replace("uuid-replacement-string", patient.getUuid());

                String returnString = httpPost.backgroundPost(String.valueOf(form[3]), patientContent);

                JSONObject jsonObject = JSONParser.getJSONObject("{" + returnString.toString() + "}");
                com.ihsinformatics.gfatmmobile.model.Encounter encounter1 = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                String patientSystemId = "";
                try {
                    JSONObject patientObject = jsonObject.getJSONObject("patient");
                    String uuid = patientObject.getString("uuid");
                    patientSystemId = getPatientSystemIdByUuidLocalDB(uuid);

                } catch (Exception e) {

                }

                deleteEncounter(App.getPatientId(), encounter1.getEncounterType());

                ContentValues values2 = new ContentValues();
                values2.put("uuid", encounter1.getUuid());
                values2.put("encounterType", encounter1.getEncounterType());
                values2.put("encounterDatetime", encounter1.getEncounterDatetime());
                values2.put("encounterLocation", encounter1.getEncounterLocation());
                values2.put("patientId", patientSystemId);
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

                dbUtil.delete(Metadata.OFFLINE_FORM, "id=?", new String[]{String.valueOf(form[0])});
            }
        }

    }

    public com.ihsinformatics.gfatmmobile.model.Encounter getEncounterAndObservationFromLocalDb(int encounterId) {

        Object[][] encounterObject = dbUtil.getFormTableData("select encounter_id, uuid, encounterType, encounterDatetime, encounterLocation, patientId from " + Metadata.ENCOUNTER + " where encounter_id=" + encounterId + "");

        if (encounterObject.length < 1)
            return null;

        String uuid = String.valueOf(encounterObject[0][1]);
        String encounterType = String.valueOf(encounterObject[0][2]);
        String encounterDatetime = String.valueOf(encounterObject[0][3]);
        String encounterLocation = String.valueOf(encounterObject[0][4]);
        String patientId = String.valueOf(encounterObject[0][5]);

        Object[][] obsObject = dbUtil.getFormTableData("select obs_id, uuid, encounter_id, conceptName, value from " + Metadata.OBS + " where encounter_id=" + encounterId + "");

        ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> obsGroup = new ArrayList<>();

        for (int i = 0; i < obsObject.length; i++) {

            String obsUuid = String.valueOf(obsObject[i][1]);
            String conceptName = String.valueOf(obsObject[i][3]);
            String value = String.valueOf(obsObject[i][4]);
            com.ihsinformatics.gfatmmobile.model.Obs obs = new com.ihsinformatics.gfatmmobile.model.Obs(obsUuid, conceptName, value);

            obsGroup.add(obs);
        }

        com.ihsinformatics.gfatmmobile.model.Encounter encounter = new com.ihsinformatics.gfatmmobile.model.Encounter(uuid, encounterType, encounterDatetime, obsGroup, encounterLocation);
        encounter.setPatientId(patientId);

        return encounter;
    }

    public Object[][] getEncounterIdByEncounterType(String encounterType) {

        Object[][] encounterObject = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation from " + Metadata.ENCOUNTER + " where encounterType='" + encounterType + "' and patientId=" + App.getPatientId() + "");
        return encounterObject;
    }


    public String getEncounterDateTime(String patientId, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select encounterDatetime from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' and encounterType = '" + encounterType + "'");
        if (encounter.length < 1)
            return null;

        return String.valueOf(encounter[0][0]);

    }

    public String getEncounterLocation(String patientId, String encounterType) {

        Object[][] encounter = dbUtil.getFormTableData("select encounterLocation from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' and encounterType = '" + encounterType + "'");
        if (encounter.length < 1)
            return null;

        return String.valueOf(encounter[0][0]);

    }

    public Object[][] getTestIdByPatientAndEncounterType(String patientId, String encounterType) {

        Object[][] testId = dbUtil.getFormTableData("select test_id from " + Metadata.TEST_ID + " where pid='" + patientId + "' and form = '" + encounterType + "'");
        return testId;

    }

    public boolean deleteOfflineForms(String fromId) {
        dbUtil.delete(Metadata.FORMS, "id=?", new String[]{fromId});
        dbUtil.delete(Metadata.FORMS_VALUE, "form_id=?", new String[]{fromId});
        dbUtil.delete(Metadata.OFFLINE_FORM, "form_id=?", new String[]{fromId});
        return true;
    }

    public String updatePatientDetails(String patientId) {

        if (!App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!isURLReachable()) {
                return "CONNECTION_ERROR";
            }
        }

        if (App.getCommunicationMode().equalsIgnoreCase("REST")) {
            try {
                com.ihsinformatics.gfatmmobile.model.Patient patient = null;
                patient = getPatientByIdentifierFromLocalDB(patientId);

                String uuid = getPatientUuid(patientId);
                if (uuid == null)
                    return "PATIENT_NOT_FOUND";
                else {

                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        return "PATIENT_NOT_FOUND";

                    JSONObject response = httpGet.getPatientByUuid(uuid);
                    patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response);

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
                    String birthplace = patient.getPerson().getBirthPlace();
                    String citizenship = patient.getPerson().getCitizenship();
                    String maritalStatus = patient.getPerson().getMaritalStatus();
                    String healthCenter = patient.getPerson().getHealthCenter();
                    String healthDistrict = patient.getPerson().getHealthDistrict();
                    String motherName = patient.getPerson().getMotherName();
                    String primaryContact = patient.getPerson().getPrimaryContact();
                    String primaryContactOwner = patient.getPerson().getPrimaryContactOwner();
                    String secondaryContact = patient.getPerson().getSecondaryContact();
                    String secondaryContactOwner = patient.getPerson().getSecondaryContactOwner();
                    String ethnicity = patient.getPerson().getEthnicity();
                    String educationLevel = patient.getPerson().getEducationLevel();
                    String employmentStatus = patient.getPerson().getEmploymentStatus();
                    String occupation = patient.getPerson().getOccupation();
                    String incomeClass = patient.getPerson().getIncomeClass();
                    String motherTongue = patient.getPerson().getMotherTongue();
                    String nationalId = patient.getPerson().getNationalId();
                    String nationalIdOwner = patient.getPerson().getNationalIdOwner();
                    String guardianName = patient.getPerson().getGuardianName();
                    String address1 = patient.getPerson().getAddress1();
                    String address2 = patient.getPerson().getAddress2();
                    String address3 = patient.getPerson().getAddress3();
                    String stateProvince = patient.getPerson().getStateProvince();
                    String cityVillage = patient.getPerson().getCityVillage();
                    String country = patient.getPerson().getCountry();

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
                    values.put("birthplace", birthplace);
                    values.put("citizenship", citizenship);
                    values.put("maritalstatus", maritalStatus);
                    values.put("healthcenter", healthCenter);
                    values.put("healthdistrict", healthDistrict);
                    values.put("mothername", motherName);
                    values.put("primarycontact", primaryContact);
                    values.put("primarycontactowner", primaryContactOwner);
                    values.put("secondarycontact", secondaryContact);
                    values.put("secondarycontactowner", secondaryContactOwner);
                    values.put("ethnicity", ethnicity);
                    values.put("educationlevel", educationLevel);
                    values.put("employmentstatus", employmentStatus);
                    values.put("occupation", occupation);
                    values.put("incomeclass", incomeClass);
                    values.put("mothertongue", motherTongue);
                    values.put("nationalid", nationalId);
                    values.put("nationalidowner", nationalIdOwner);
                    values.put("guardianname", guardianName);
                    values.put("address1", address1);
                    values.put("address2", address2);
                    values.put("address3", address3);
                    values.put("stateProvince", stateProvince);
                    values.put("cityVillage", cityVillage);
                    values.put("country", country);
                    dbUtil.update(Metadata.PATIENT, values, "uuid=?", new String[]{App.getPatient().getUuid()});

                    App.setPatientId(getPatientSystemIdByUuidLocalDB(uuid));
                    App.setPatient(patient);

                    Object[][] encounterTypes = getEncounterTypesFromLocalDBByProgramName(App.getProgram());
                    deletePatientEncounterByProgram(App.getPatientId(), App.getProgram());
                    deletePatientTestIdByProgram(App.getPatientId(), App.getProgram());

                    for (Object[] encType : encounterTypes) {

                        if (String.valueOf(encType[1]).contains("Test Order")) {
                            JSONArray jsonArray = httpGet.getAllEncountersByPatientAndEncounterType(App.getPatient().getUuid(), String.valueOf(encType[0]));
                            if (jsonArray == null || jsonArray.length() < 1)
                                continue;

                            List<String> newArr = new ArrayList<String>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject newObj = jsonArray.getJSONObject(i);
                                String date = newObj.getJSONObject("auditInfo").getString("dateCreated");
                                String uuid1 = newObj.getString("uuid");
                                date = date.replace("T", " ");
                                String[] arr = date.split("\\+");
                                newArr.add(arr[0] + "," + i);

                                com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);
                                encounter.setPatientId(App.getPatientId());

                                for (com.ihsinformatics.gfatmmobile.model.Obs obs : encounter.getObsGroup()) {

                                    if (obs.getConceptName().equals("TEST ID")) {
                                        ContentValues values2 = new ContentValues();
                                        values2.put("pid", encounter.getPatientId());
                                        values2.put("form", encounter.getEncounterType());
                                        values2.put("test_id", obs.getValue());
                                        dbUtil.insert(Metadata.TEST_ID, values2);
                                    }
                                }

                            }
                            Collections.sort(newArr, new Comparator<String>() {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                                @Override
                                public int compare(String o1, String o2) {
                                    try {
                                        String[] b1 = o1.split(",");
                                        String[] b2 = o2.split(",");
                                        return f.parse(b1[0]).compareTo(f.parse(b2[0]));
                                    } catch (Exception e) {
                                        throw new IllegalArgumentException(e);
                                    }
                                }
                            });
                            String[] splitter = newArr.get(newArr.size() - 1).split(",");
                            JSONObject newObj = jsonArray.getJSONObject(Integer.valueOf(splitter[1]));

                            com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(newObj, context);
                            encounter.setPatientId(App.getPatientId());

                            ContentValues values1 = new ContentValues();
                            values1.put("uuid", encounter.getUuid());
                            values1.put("encounterType", encounter.getEncounterType());
                            values1.put("encounterDatetime", encounter.getEncounterDatetime());
                            values1.put("encounterLocation", encounter.getEncounterLocation());
                            values1.put("patientId", encounter.getPatientId());
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

                        } else {
                            JSONObject jsonObject = httpGet.getLatestEncounter(App.getPatient().getUuid(), String.valueOf(encType[0]));
                            if (jsonObject == null)
                                continue;
                            com.ihsinformatics.gfatmmobile.model.Encounter encounter = com.ihsinformatics.gfatmmobile.model.Encounter.parseJSONObject(jsonObject, context);
                            encounter.setPatientId(App.getPatientId());

                            ContentValues values1 = new ContentValues();
                            values1.put("uuid", encounter.getUuid());
                            values1.put("encounterType", encounter.getEncounterType());
                            values1.put("encounterDatetime", encounter.getEncounterDatetime());
                            values1.put("encounterLocation", encounter.getEncounterLocation());
                            values1.put("patientId", encounter.getPatientId());
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

}
