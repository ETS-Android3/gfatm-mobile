package com.ihsinformatics.gfatmmobile.util;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

/**
 * Created by Haris on 12/9/2016.
 */
public class HttpPost {

    private static String PERSON_RESOURCE = "person";
    private static String PATIENT_RESOURCE = "patient";
    private static String PERSON_ATTRIBUTE_TYPE_RESOURCE = "personattributetype";
    private static String ENCOUNTER_RESOURCE = "encounter";
    private static String TAG = "";
    private String serverAddress = "";

    public HttpPost(String serverIP, String port) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        serverAddress = serverIP;
        if (port == null || port.equals("")) {
        } else {
            serverAddress += ":" + port;
        }
    }

    private String post(String postUri, String content) {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = null;
        HttpResponse response = null;
        HttpEntity entity;
        StringBuilder builder = new StringBuilder();
        String auth = "";

        try {
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(postUri);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity stringEntity = new StringEntity(content.toString());
            httpPost.setEntity(stringEntity);
            request = httpPost;
            auth = Base64.encodeToString(
                    (App.getUsername() + ":" + App.getPassword()).getBytes("UTF-8"),
                    Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + auth);
            response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_CREATED) {
                entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(is));
                builder = new StringBuilder();
                while (bufferedReader.read() != -1)
                    builder.append(bufferedReader.readLine());
                entity.consumeContent();
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String postJSONObject(String resourceName, JSONObject contentObject) {
        String requestURI = "http://" + serverAddress + "/openmrs/ws/rest/v1/" + resourceName;
        String content = contentObject.toString();
        return post(requestURI, content);
    }

    private String postEntityByJSON(String resource, JSONObject jsonObject) {
        return postJSONObject(resource, jsonObject);
    }

    public String savePatientByEntitiy(Patient patient) {
        JSONObject personObj = new JSONObject();
        JSONObject personAttribute = new JSONObject();
        JSONObject attribute = new JSONObject();
        JSONArray attributes = new JSONArray();
        JSONArray names = new JSONArray();
        JSONArray addresses = new JSONArray();
        JSONArray identifiers = new JSONArray();
        JSONObject preferredAddress = new JSONObject();
        JSONObject patientObject = new JSONObject();
        try {

            for (PersonName personName : patient.getNames()) {
                JSONObject preferredName = new JSONObject();
                preferredName.put("givenName", personName.getGivenName());
                preferredName.put("middleName", personName.getMiddleName());
                preferredName.put("familyName", personName.getFamilyName());
                preferredName.put("familyName2", personName.getFamilyName2());
                preferredName.put("voided", personName.getVoided());
                names.put(preferredName);
            }

            /*for (PersonAttribute personAttr : patient.getAttributes()) {
                attribute.put("attributeType", personAttr.getAttributeType().getUuid());
                attribute.put("value", personAttr.getValue());
                attribute.put("voided", personAttr.getVoided());
                attributes.put(attribute);
            }*/

            /*for (PersonAddress personAddress : patient.getAddresses()) {
                preferredAddress.put("preferred", personAddress.getPreferred());
                preferredAddress.put("address1", personAddress.getAddress1());
                preferredAddress.put("address2", personAddress.getAddress2());
                preferredAddress.put("cityVillage", personAddress.getCityVillage());
                preferredAddress.put("stateProvince", personAddress.getStateProvince());
                preferredAddress.put("country", personAddress.getCountry());
                preferredAddress.put("postalCode", personAddress.getPostalCode());
                preferredAddress.put("countyDistrict", personAddress.getCountyDistrict());
                preferredAddress.put("address3", personAddress.getAddress3());
                preferredAddress.put("address4", personAddress.getAddress4());
                preferredAddress.put("address5", personAddress.getAddress5());
                preferredAddress.put("address6", personAddress.getAddress6());
                preferredAddress.put("startDate", personAddress.getStartDate());
                preferredAddress.put("endDate", personAddress.getEndDate());
                preferredAddress.put("latitude", personAddress.getLatitude());
                preferredAddress.put("longitude", personAddress.getLongitude());
                preferredAddress.put("voided", personAddress.getVoided());
                addresses.put(preferredAddress);
            }*/

            personObj.put("gender", patient.getGender());
            personObj.put("birthdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(patient.getBirthdate()));
            /*personObj.put("birthdateEstimated", patient.getBirthdateEstimated());
            personObj.put("dead", patient.getDead());
            personObj.put("deathDate", patient.getDeathDate());
            personObj.put("causeOfDeath", patient.getCauseOfDeath());*/
            personObj.put("names", names);
            /*personObj.put("addresses", addresses);
            personObj.put("attributes", attributes);
            personObj.put("voided", patient.getPersonVoided());
            personObj.put("deathdateEstimated", patient.getDeathdateEstimated());
            personObj.put("birthtime", patient.getBirthtime());*/
            String response = postEntityByJSON(PERSON_RESOURCE, personObj);
            JSONObject newPerson = JSONParser.getJSONObject("{"
                    + response.toString() + "}");

            for (PatientIdentifier patientIdentifier : patient.getIdentifiers()) {
                JSONObject identifier = new JSONObject();
                identifier.put("identifier", patientIdentifier.getIdentifier());
                identifier.put("identifierType", patientIdentifier.getIdentifierType().getUuid());
                identifier.put("location", patientIdentifier.getLocation().getUuid());
                identifier.put("preferred", patientIdentifier.getPreferred());
                identifier.put("voided", patientIdentifier.getVoided());
                identifiers.put(identifier);
            }

            patientObject.put("person", newPerson.get("uuid"));
            patientObject.put("identifiers", identifiers);
            patientObject.put("voided", patient.getVoided());
            return postEntityByJSON(PATIENT_RESOURCE, patientObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String savePersonAttributeByEntity(PersonAttributeType personAttributeType) {
        JSONObject personAttributeObject = new JSONObject();
        try {
            personAttributeObject.put("name", personAttributeType.getName());
            personAttributeObject.put("description", (personAttributeType.getDescription()) == null ? "NULL" : personAttributeType.getDescription());
            personAttributeObject.put("format", personAttributeType.getFormat());
            personAttributeObject.put("foreignKey", personAttributeType.getForeignKey());
            personAttributeObject.put("sortWeight", personAttributeType.getSortWeight());
            personAttributeObject.put("searchable", personAttributeType.getSearchable());
            personAttributeObject.put("editPrivilege", personAttributeType.getEditPrivilege());
            personAttributeObject.put("retired", personAttributeType.getRetired());
            return postEntityByJSON(PERSON_ATTRIBUTE_TYPE_RESOURCE, personAttributeObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveEncounterWithObservationByEntity(Encounter encounter) {
        JSONObject encounterObject = new JSONObject();
        JSONArray encounterProviderArray = new JSONArray();
        JSONArray obsArray = new JSONArray();

        try {
            for (Obs observation : encounter.getObs()) {
                String value = null;
                JSONObject obsObject = new JSONObject();
                obsObject.put("concept", observation.getConcept().getUuid());
                if (observation.getValueCoded() == null)
                    value = observation.getValueText();
                else
                    value = observation.getValueCoded().getUuid();

                obsObject.put("value", value);
                obsArray.put(obsObject);
            }

            for (EncounterProvider encounterProvider : encounter.getEncounterProviders()) {
                JSONObject encounterProviderObject = new JSONObject();
                encounterProviderObject.put("provider", encounterProvider.getProvider().getUuid());
                encounterProviderObject.put("encounterRole", encounterProvider.getEncounterRole().getUuid());
                encounterProviderObject.put("voided", encounterProvider.getVoided());
                encounterProviderArray.put(encounterProviderObject);
            }

            encounterObject.put("encounterDatetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(encounter.getEncounterDatetime()));
            encounterObject.put("patient", encounter.getPatient().getUuid());
            encounterObject.put("location", encounter.getLocation().getUuid());
            encounterObject.put("encounterType", encounter.getEncounterType().getUuid());
            encounterObject.put("obs", obsArray);
            encounterObject.put("voided", encounter.getVoided());
            encounterObject.put("encounterProviders", encounterProviderArray);

            return postEntityByJSON(ENCOUNTER_RESOURCE, encounterObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String savePatientIdentifierByEntity(PatientIdentifier identifier) {
        try {

            JSONObject identifierObject = new JSONObject();
            identifierObject.put("identifier", identifier.getIdentifier());
            identifierObject.put("identifierType", identifier.getIdentifierType().getUuid());
            identifierObject.put("location", identifier.getLocation().getUuid());
            identifierObject.put("preferred", identifier.getPreferred());
            identifierObject.put("voided", identifier.getVoided());

            return postEntityByJSON(PATIENT_RESOURCE + "/" + App.getPatient().getUuid() + "/" + "identifier", identifierObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String savePersonAddressByEntity(PersonAddress personAddress) {
        try {

            JSONObject identifierObject = new JSONObject();
            identifierObject.put("address1", personAddress.getAddress1());
            identifierObject.put("address2", personAddress.getAddress2());
            identifierObject.put("cityVillage", personAddress.getCityVillage());
            identifierObject.put("stateProvince", personAddress.getStateProvince());
            identifierObject.put("country", personAddress.getCountry());
            identifierObject.put("longitude", personAddress.getLongitude());
            identifierObject.put("latitude", personAddress.getLatitude());

            return postEntityByJSON(PERSON_RESOURCE + "/" + App.getPatient().getUuid() + "/" + "address", identifierObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String savePersonAttribute(String attributeType, String value) {
        try {

            JSONObject identifierObject = new JSONObject();
            identifierObject.put("attributeType", attributeType);
            identifierObject.put("value", value);

            return postEntityByJSON(PERSON_RESOURCE + "/" + App.getPatient().getUuid() + "/" + "attribute", identifierObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
