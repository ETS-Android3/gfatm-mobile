
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.model.Concept;
import com.ihsinformatics.gfatmmobile.model.EncounterType;
import com.ihsinformatics.gfatmmobile.model.Location;
import com.ihsinformatics.gfatmmobile.model.PersonAttributeType;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Metadata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
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

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        String patientId = values.getAsString("patientId");
        String givenName = values.getAsString("firstName");
        String familyName = values.getAsString("lastName");
        String gender = values.getAsString("gender");
        String dob = values.getAsString("dob");
        Date dateOfBirth = App.stringToDate(dob, "yyy-MM-dd");
        String externalId = values.getAsString("externalId");

        if (App.getCommunicationMode().equals("REST")) {

            String uuid = getPatientUuid(patientId);
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

                    Patient patient = new Patient(person);
                    patient.setIdentifiers(patientIdentifierSet);

                    httpPost.savePatientByEntitiy(patient);

                    getPatient(patientId);

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

    public String getPatient(String patientId) {

        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {
                com.ihsinformatics.gfatmmobile.model.Patient patient = null;
                patient = getPatientByPatientIdFromLocalDB(patientId);

                if (patient == null) {

                    String uuid = getPatientUuid(patientId);
                    if (uuid == null)
                        return "PATIENT_NOT_FOUND";
                    else {

                        JSONObject response = httpGet.getPatientByUuid(uuid);
                        patient = com.ihsinformatics.gfatmmobile.model.Patient.parseJSONObject(response);

                        com.ihsinformatics.gfatmmobile.model.Person person = patient.getPerson();
                        String puuid = patient.getUuid();
                        String pid = patient.getPatientId();
                        String eid = patient.getExternalId();
                        String erns = patient.getEnrs();
                        String endTbId = patient.getEndTBId();
                        String fname = patient.getPerson().getGivenName();
                        String lname = patient.getPerson().getFamilyName();
                        String gender = patient.getPerson().getGender();
                        String birthdate = patient.getPerson().getBirthdate();
                        int age = patient.getPerson().getAge();

                        ContentValues values = new ContentValues();
                        values.put("uuid", puuid);
                        values.put("identifier", pid);
                        values.put("external_id", eid);
                        values.put("enrs", erns);
                        values.put("endtb_emr_id", endTbId);
                        values.put("first_name", fname);
                        values.put("last_name", lname);
                        values.put("gender", gender);
                        values.put("birthdate", birthdate);
                        dbUtil.insert(Metadata.PATIENT, values);

                        App.setPatientUuid(uuid);
                        App.setPatient(patient);

                    }
                } else {
                    App.setPatientUuid(patient.getUuid());
                    App.setPatient(patient);
                }


            } catch (Exception e) {
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

    public String getPersonAttributeTypeUuid(String personAttributeType) {

        String[][] result = dbUtil.getTableData(Metadata.PERSON_ATTRIBUTE_TYPE, "uuid", "person_attribute_type = '" + personAttributeType + "'");
        if (result.length > 0)
            return result[0][0];
        else
            return null;

    }

    public String[][] getConceptUuidAndDataType(String concept_name) {

        String[][] result = dbUtil.getTableData(Metadata.CONCEPT, "uuid,data_type", "full_name = '" + concept_name + "'");
        if (result.length > 0)
            return result;
        else {

            JSONObject jsonobject = httpGet.getConceptByName(concept_name);
            if (jsonobject == null)
                return null;
            JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(jsonobject, "results");
            for (JSONObject json : jsonObjects) {
                Concept concept = Concept.parseJSONObject(json);

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

    public com.ihsinformatics.gfatmmobile.model.Patient getPatientByUuidFromLocalDB(String uuid) {

        com.ihsinformatics.gfatmmobile.model.Patient patient = null;

        if (uuid == null || uuid.equals(""))
            return patient;

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, identifier, external_id, enrs, endtb_emr_id, first_name, last_name, gender, birthdate", "uuid = '" + uuid + "'");

        if (result == null || result.length == 0)
            return patient;

        Date date = App.stringToDate(result[0][8], "yyyy-MM-DD");
        int age = App.getDiffYears(date, new Date());
        com.ihsinformatics.gfatmmobile.model.Person person = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][5], result[0][6], age, result[0][8], result[0][7]);

        patient = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][1], result[0][2], result[0][3], result[0][4], person);

        return patient;

    }

    public com.ihsinformatics.gfatmmobile.model.Patient getPatientByPatientIdFromLocalDB(String patientId) {

        com.ihsinformatics.gfatmmobile.model.Patient patient = null;

        if (patientId == null || patientId.equals(""))
            return patient;

        String[][] result = dbUtil.getTableData(Metadata.PATIENT, "uuid, identifier, external_id, enrs, endtb_emr_id, first_name, last_name, gender, birthdate", "identifier = '" + patientId + "'");

        if (result == null || result.length == 0)
            return patient;

        Date date = App.stringToDate(result[0][8], "yyyy-MM-DD");
        int age = App.getDiffYears(date, new Date());
        com.ihsinformatics.gfatmmobile.model.Person person = new com.ihsinformatics.gfatmmobile.model.Person(result[0][0], result[0][5], result[0][6], age, result[0][8], result[0][7]);

        patient = new com.ihsinformatics.gfatmmobile.model.Patient(result[0][0], result[0][1], result[0][2], result[0][3], result[0][4], person);

        return patient;

    }

    public String saveEncounterAndObservation(String formName, String encounterDateTime, String[][] obss) {
        //String response = "SUCCESS";
       /* try {

            Set<ConceptAnswer> conceptAnswers = new HashSet<>();
            ConceptAnswer conceptAnswer = new ConceptAnswer();
            conceptAnswer.setUuid("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            conceptAnswers.add(conceptAnswer);

            org.openmrs.Concept concept = new org.openmrs.Concept();
            concept.setUuid("143264AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            //concept.setAnswers(conceptAnswers);

            ConceptName conceptName = new ConceptName();
            Order order = new Order();

            org.openmrs.Location location1 = new org.openmrs.Location();
            location1.setUuid("58c57d25-8d39-41ab-8422-108a0c277d98");

            Person person = new Person();
            person.setUuid(App.getPatient().getPerson().getUuid());
            Obs obs = new Obs();
            obs.setConcept(concept);
            obs.setValueText("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            Set<Obs> observations = new HashSet<>();
            observations.add(obs);

            Patient patient1 = new Patient();
            patient1.setUuid(App.getPatient().getUuid());

            org.openmrs.EncounterType encounterType = new org.openmrs.EncounterType();
            encounterType.setUuid(getEncounterTypeUuid(formName));

            Set<EncounterProvider> encounterProviders = new HashSet<>();
            EncounterProvider encounterProvider = new EncounterProvider();

            Provider provider = new Provider();
            provider.setUuid(App.getProviderUUid());

            EncounterRole encounterRole = new EncounterRole();
            encounterRole.setUuid("a0b03050-c99b-11e0-9572-0800200c9a66");

            encounterProvider.setProvider(provider);
            encounterProvider.setEncounterRole(encounterRole);
            encounterProviders.add(encounterProvider);

            Encounter encounter = new Encounter();
            encounter.setEncounterDatetime(new Date());
            encounter.setPatient(patient1);
            encounter.setEncounterType(encounterType);
            encounter.setLocation(location1);
            encounter.setEncounterProviders(encounterProviders);
            encounter.setObs(observations);

            httpPost.saveEncounterWithObservationByEntity(encounter);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            //response = context.getResources ().getString (R.string.insert_error);
        }
        return response;*/
        return "SUCCESS";
    }

    public String saveEncounterAndObservation(String formName, Calendar encounterDateTime, String[][] obss) {

       /* if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        String response = "SUCCESS";

        if (App.getCommunicationMode().equals("REST")) {
            try {

                org.openmrs.Location location1 = new org.openmrs.Location();
                location1.setUuid(getLocationUuid(App.getLocation()));

                org.openmrs.Person person = new org.openmrs.Person();
                person.setUuid(App.getPatient().getPerson().getUuid());

                Patient patient1 = new Patient();
                patient1.setUuid(App.getPatientUuid());

                org.openmrs.EncounterType encounterType = new org.openmrs.EncounterType();
                String encounterUuid = getEncounterTypeUuid(formName);
                if (encounterUuid == null) {
                    return "ERROR RETRIEVING ENCOUNTER TYPE";
                }
                encounterType.setUuid(encounterUuid);

                Set<EncounterProvider> encounterProviders = new HashSet<>();
                EncounterProvider encounterProvider = new EncounterProvider();

                Provider provider = new Provider();
                provider.setUuid(App.getProviderUUid());

                EncounterRole encounterRole = new EncounterRole();
                encounterRole.setUuid("a0b03050-c99b-11e0-9572-0800200c9a66");

                encounterProvider.setProvider(provider);
                encounterProvider.setEncounterRole(encounterRole);
                encounterProviders.add(encounterProvider);

                Set<Obs> observations = new HashSet<>();

                for (int i = 0; i < obss.length; i++) {

                    if ("".equals(obss[i][0]) || "".equals(obss[i][1]))
                        continue;

                    Obs obs = new Obs();

                    org.openmrs.Concept conceptQuestion = new org.openmrs.Concept();
                    String[][] conceptUuid = getConceptUuidAndDataType(obss[i][0]);
                    if (conceptUuid == null) {
                        return "ERROR RETRIEVING CONCEPT";
                    }
                    conceptQuestion.setUuid(conceptUuid[0][0]);
                    obs.setConcept(conceptQuestion);

                    if (conceptUuid[0][1].equals("Coded")) {
                        String[][] valueUuid = getConceptUuidAndDataType(obss[i][1]);
                        if (valueUuid == null) {
                            return "ERROR RETRIEVING CONCEPT";
                        }

                        org.openmrs.Concept conceptAnswer = new org.openmrs.Concept();
                        conceptAnswer.setUuid(valueUuid[0][0]);
                        obs.setValueCoded(conceptAnswer);
                    } else
                        obs.setValueText(obss[i][1]);

                    observations.add(obs);
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

                httpPost.saveEncounterWithObservationByEntity(encounter);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                response = "INSERT ERROR";
            }
        }
        return response;*/
        return "SUCCESS";
    }


    public String savePersonAttributeType(String attributeType, String value) {

       /* if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                String personAttributeTypeUuid = getPersonAttributeTypeUuid(attributeType);
                httpPost.savePersonAttribute(personAttributeTypeUuid, value);

            } catch (Exception e) {
                return "FAIL";
            }
        }*/

        return "SUCCESS";
    }

    public String saveIdentifier(String identifierType, String identifier) {

        /*if (!isURLReachable()) {
            return "CONNECTION_ERROR";
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

                    httpPost.savePatientIdentifierByEntity(patientIdentifier);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(idType, identifier);

                    dbUtil.update(Metadata.PATIENT, contentValues, "uuid=?", new String[]{App.getPatient().getUuid()});
                    App.setPatient(getPatientByUuidFromLocalDB(App.getPatientUuid()));

                }


            } catch (Exception e) {
                return "FAIL";
            }
        }*/

        return "SUCCESS";
    }

    public String getPersonAttribute(String personAttributeType) {
        if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                JSONObject jsonObj = httpGet.getAllPersonAttributesByPersonUuid(App.getPatientUuid());
                JSONObject[] jsonObjects = JSONParser.getJSONArrayFromObject(jsonObj, "results");
                for (JSONObject json : jsonObjects) {

                    String display = json.getString("display");
                    if (display.contains(personAttributeType)) {
                        return json.getString("value");
                    }

                }
            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "SUCCESS";
    }

    public String savePersonAddress(String address1, String address2, String city, String province, String country, double longitude, double latitude) {
        /*if (!isURLReachable()) {
            return "CONNECTION_ERROR";
        }

        if (App.getCommunicationMode().equals("REST")) {
            try {

                PersonAddress personAddress = new PersonAddress();
                personAddress.setPreferred(false);
                personAddress.setAddress1(address1);
                personAddress.setAddress2(address2);
                personAddress.setCityVillage(city);
                personAddress.setStateProvince(province);
                personAddress.setCountry(country);
                personAddress.setLongitude(String.valueOf(longitude));
                personAddress.setLatitude(String.valueOf(latitude));

                httpPost.savePersonAddressByEntity(personAddress);

            } catch (Exception e) {
                return "FAIL";
            }
        }*/

        return "SUCCESS";
    }

}

