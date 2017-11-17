package com.ihsinformatics.gfatmmobile.util;

import android.content.Context;
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
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Haris on 12/9/2016.
 */
public class HttpPost {

    private static String PERSON_RESOURCE = "person";
    private static String PATIENT_RESOURCE = "patient";
    private static String PERSON_ATTRIBUTE_TYPE_RESOURCE = "personattributetype";
    private static String ENCOUNTER_RESOURCE = "encounter";
    private static String PROGRAM_ENROLLEMENT = "programenrollment";
    private static String TAG = "";
    private String serverAddress = "";
    private Context context = null;

    public HttpPost(String serverIP, String port, Context context) {
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        serverAddress = serverIP;
        if (port == null || port.equals("")) {
        } else {
            serverAddress += ":" + port;
        }
    }

    private String post(String postUri, String content) {
        HttpUriRequest request = null;
        HttpResponse response = null;
        HttpEntity entity;
        StringBuilder builder = new StringBuilder();
        String auth = "";

        try {
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(postUri);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-Type", "application/json");
            StringEntity stringEntity = new StringEntity(content);
            httpPost.setEntity(stringEntity);
            request = httpPost;
            auth = Base64.encodeToString(
                    (App.getUsername() + ":" + App.getPassword()).getBytes("UTF-8"),
                    Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + auth);
            if (App.getSsl().equalsIgnoreCase("Enabled")) {
                HttpsClient client = new HttpsClient(context);
                response = client.execute(request);
            } else {
                HttpClient client = new DefaultHttpClient();
                response = client.execute(request);
            }
            StatusLine statusLine = response.getStatusLine();
            Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK || statusLine.getStatusCode() == HttpStatus.SC_CREATED) {
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

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        String requestURI = http + serverAddress + "/openmrs/ws/rest/v1/" + resourceName;
        String content = contentObject.toString();
        return post(requestURI, content);
    }

    private String postEntityByJSON(String resource, JSONObject jsonObject) {
        return postJSONObject(resource, jsonObject);
    }

    public String backgroundPost(String uri, String content) {
        String requestURI = uri.replace("serverAddress", serverAddress);

        String http = "";
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";

        requestURI = http + requestURI;

        return post(requestURI, content);
    }

    public String savePatientDied(Calendar deathDate, String patientUuid){

        JSONObject personObj = new JSONObject();
        try {
            personObj.put("dead", true);
            personObj.put("deathDate", App.getSqlDate(deathDate));
            personObj.put("causeOfDeath", "5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PERSON_RESOURCE + "/" + patientUuid;
                String content = personObj.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(PATIENT_RESOURCE + "/" + patientUuid , personObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String savePatientByEntitiy(Patient patient) {

        JSONObject personObj = new JSONObject();
        JSONArray names = new JSONArray();
        JSONArray identifiers = new JSONArray();
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

            personObj.put("gender", patient.getGender());
            personObj.put("birthdate", App.getSqlDate(patient.getBirthdate()));
            personObj.put("names", names);

            JSONObject newPerson = null;
            String offlineReturnString = "";
            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PERSON_RESOURCE;
                String content = personObj.toString();

                offlineReturnString = requestURI + " ;;;; " + content;

            } else {
                String string = postEntityByJSON(PERSON_RESOURCE, personObj);
                newPerson = JSONParser.getJSONObject("{" + string + "}");
            }

            for (PatientIdentifier patientIdentifier : patient.getIdentifiers()) {
                JSONObject identifier = new JSONObject();
                identifier.put("identifier", patientIdentifier.getIdentifier());
                identifier.put("identifierType", patientIdentifier.getIdentifierType().getUuid());
                identifier.put("location", patientIdentifier.getLocation().getUuid());
                identifier.put("preferred", patientIdentifier.getPreferred());
                identifier.put("voided", patientIdentifier.getVoided());
                identifiers.put(identifier);
            }

            if (patient.getUuid().equals(""))
                patientObject.put("person", newPerson.get("uuid"));
            else
                patientObject.put("person", patient.getUuid());

            patientObject.put("identifiers", identifiers);
            patientObject.put("voided", patient.getVoided());

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PATIENT_RESOURCE;
                String content = patientObject.toString();

                offlineReturnString = offlineReturnString + " ;;;; " + requestURI + " ;;;; " + content;
                return offlineReturnString;
            }

            return postEntityByJSON(PATIENT_RESOURCE, patientObject);

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
            for (Obs observation : encounter.getObsAtTopLevel(false)) {
                String value = null;
                JSONObject obsObject = new JSONObject();
                obsObject.put("concept", observation.getConcept().getUuid());

                if (observation.getGroupMembers() == null) {
                    if (observation.getValueCoded() == null)
                        value = observation.getValueText();
                    else
                        value = observation.getValueCoded().getUuid();
                } else {

                    JSONArray obsGroupArray = new JSONArray();
                    Set<Obs> groupedObs = observation.getGroupMembers();

                    for (Obs obs1 : groupedObs) {
                        String value1 = null;
                        JSONObject valueObject = new JSONObject();
                        valueObject.put("concept", obs1.getConcept().getUuid());

                        if (obs1.getValueCoded() == null)
                            value1 = obs1.getValueText();
                        else
                            value1 = obs1.getValueCoded().getUuid();

                        valueObject.put("value", value1);
                        obsGroupArray.put(valueObject);

                    }
                    obsObject.put("groupMembers", obsGroupArray);
                }

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

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + ENCOUNTER_RESOURCE;
                String content = encounterObject.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(ENCOUNTER_RESOURCE, encounterObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String savePatientIdentifierByEntity(PatientIdentifier identifier, String patientUuid) {
        try {

            JSONObject identifierObject = new JSONObject();
            identifierObject.put("identifier", identifier.getIdentifier());
            identifierObject.put("identifierType", identifier.getIdentifierType().getUuid());
            identifierObject.put("location", identifier.getLocation().getUuid());
            //identifierObject.put("preferred", identifier.getPreferred());
            //identifierObject.put("voided", identifier.getVoided());

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PATIENT_RESOURCE + "/" + patientUuid + "/" + "identifier";
                String content = identifierObject.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(PATIENT_RESOURCE + "/" + patientUuid + "/" + "identifier", identifierObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String saveProgramEnrollment(String programName, String location, String enrollmentDate) {

        try {

            JSONObject programEnrollementObject = new JSONObject();

            if (App.getPatient().getUuid() == null || App.getPatient().getUuid().equals(""))
                programEnrollementObject.put("patient", "uuid-replacement-string");
            else
                programEnrollementObject.put("patient", App.getPatient().getUuid());

            programEnrollementObject.put("program", programName);
            programEnrollementObject.put("location", location);
            programEnrollementObject.put("dateEnrolled", enrollmentDate);

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PROGRAM_ENROLLEMENT;
                String content = programEnrollementObject.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(PROGRAM_ENROLLEMENT, programEnrollementObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

    public String savePersonAddressByEntity(PersonAddress personAddress, String patientUuid) {
        try {

            JSONObject personAddressObject = new JSONObject();
            personAddressObject.put("address1", personAddress.getAddress1());
            personAddressObject.put("address2", personAddress.getAddress2());
            personAddressObject.put("address3", personAddress.getAddress3());
            personAddressObject.put("cityVillage", personAddress.getCityVillage());
            personAddressObject.put("stateProvince", personAddress.getStateProvince());
            personAddressObject.put("countyDistrict", personAddress.getCountyDistrict());
            personAddressObject.put("country", personAddress.getCountry());
            personAddressObject.put("longitude", personAddress.getLongitude());
            personAddressObject.put("latitude", personAddress.getLatitude());
            //personAddressObject.put("preferred", 1);

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PERSON_RESOURCE + "/" + patientUuid + "/" + "address";
                String content = personAddressObject.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(PERSON_RESOURCE + "/" + patientUuid + "/" + "address", personAddressObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String savePersonAttribute(String attributeType, String attributeTypeFormat, String value, String patientUuid) {
        try {

            JSONObject personAttributeObject = new JSONObject();
            personAttributeObject.put("attributeType", attributeType);
            if(attributeTypeFormat.equals("org.openmrs.Location") || attributeTypeFormat.equals("org.openmrs.Concept"))
                personAttributeObject.put("hydratedObject", value);
            else if (attributeTypeFormat.equals("java.lang.Boolean")) {
                if(value.equalsIgnoreCase("Yes"))
                    personAttributeObject.put("value", "true");
                else if(value.equalsIgnoreCase("No"))
                    personAttributeObject.put("value", "false");
                else
                    personAttributeObject.put("value", value);
            }else personAttributeObject.put("value", value);

            if (App.getMode().equalsIgnoreCase("OFFLINE")) {

                String requestURI = "serverAddress/openmrs/ws/rest/v1/" + PERSON_RESOURCE + "/" + patientUuid + "/" + "attribute";
                String content = personAttributeObject.toString();

                return requestURI + " ;;;; " + content;

            }

            return postEntityByJSON(PERSON_RESOURCE + "/" + patientUuid + "/" + "attribute", personAttributeObject);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
