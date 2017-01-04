package com.ihsinformatics.gfatmmobile.util;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Haris on 11/30/2016.
 */
public class HttpGet {

    private static final String TAG = "HttpsClient";
    private static final String PERSON_RESOURCE = "person";
    private static final String PATIENT_RESOURCE = "patient";
    private static final String PROGRAM_RESOURCE = "program";
    private static final String CONCEPT_RESOURCE = "concept";
    private static final String ENCOUNTER_RESOURCE = "encounter";
    private static final String LOCATION_RESOURCE = "location";
    private static final String USER_RESOURCE = "user";
    private static final String PRIVILEGE_RESOURCE = "privelge";
    private static final String ROLE_RESOURCE = "role";
    private static final String PATIENT_IDENTIFIER_TYPE_RESOURCE = "patientidentifiertype";
    private static final String IDENTIFIER_TYPE_RESOURCE = "patientidentifiertype";
    private static final String PERSON_ATTRIBUTE_TYPE_RESOURCE = "personattributetype";
    private static final String ENCOUNTER_TYPE_RESOURCE = "encountertype";
    private static final String PROVIDER_RESOURCE = "provider";
    private static final String OBSERVATION_RESOURCE = "obs";
    private static final String PROVIDER_ATTRIBUTE_TYPE_RESOURCE = "providerattributetype";
    private static final String LOCATION_ATTRIBUTE_TYPE_RESOURCE = "locationattributetype";
    private static final String LOCATION_TAG = "locationtag";
    private static final String ENCOUNTER_TYPE_PARAM = "encounterType";
    Properties properties = new Properties();
    private boolean early = true;
    private String serverAdress = "";

    public HttpGet(String serverIP, String port) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        serverAdress = serverIP;
        if (port == null || port.equals("")) {
        } else {
            serverAdress += ":" + port;
        }
    }

    private JSONObject get(String requestUri) {
        JSONObject result = null;
        HttpUriRequest request = null;
        String responseString = "";
        String auth = "";

        try {
            //    String file = "res/raw/openmrs.properties";
            //    InputStream propertiesFile = this.getClass().getClassLoader().getResourceAsStream(file);
            //    properties.load(propertiesFile);
            //    String id = properties.getProperty(PropertyName.OPENMRS_USER);
            //    String password = properties.getProperty(PropertyName.OPENMRS_PASSWORD);
            request = new org.apache.http.client.methods.HttpGet(requestUri);
            auth = Base64.encodeToString(
                    ("admin" + ":" + "Admin123").getBytes("UTF-8"),
                    Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + auth);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            Log.d(TAG, "Http response code: " + statusLine.getStatusCode());
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                result = JSONParser.getJSONObject(responseString);
            } else {
                response.getEntity().getContent().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject get(String requestUri, boolean condition, String resourceName) {
        JSONObject obj = get(requestUri);
        List<String> newArr = new ArrayList<String>();
        try {
            JSONArray arrJson = obj.getJSONArray("results");
            if (arrJson.length() == 0) {
                return null;
            } else if (arrJson.length() == 1) {
                JSONObject object = arrJson.getJSONObject(0);
                String uuid = object.getString("uuid");
                if (resourceName.equals(ENCOUNTER_RESOURCE))
                    return getEncounterByUuid(uuid);
                else
                    return getObservationByUuid(uuid);
            } else {
                for (int i = 0; i < arrJson.length(); i++) {
                    JSONObject newObj = arrJson.getJSONObject(i);
                    String date = newObj.getJSONObject("auditInfo").getString("dateCreated");
                    String uuid = newObj.getString("uuid");
                    date = date.replace("T", " ");
                    String[] arr = date.split("\\+");
                    newArr.add(arr[0] + "," + uuid);
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
                if (condition) {
                    String[] splitter = newArr.get(0).split(",");
                    if (resourceName.equals(ENCOUNTER_RESOURCE))
                        return getEncounterByUuid(splitter[1]);
                    else
                        return getObservationByUuid(splitter[1]);
                } else {
                    String[] splitter = newArr.get(newArr.size() - 1).split(",");
                    if (resourceName.equals(ENCOUNTER_RESOURCE))
                        return getEncounterByUuid(splitter[1]);
                    else
                        return getObservationByUuid(splitter[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getJsonObjectByName(String resourceName, String content) {
        if (content == null)
            return null;
        content = content.trim();
        content = content.replaceAll(" ", "%20");
        String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                "?q=" + content + "&v=full";
        return get(requestUri);
    }

    private JSONObject getJsonObjectByUuid(String resourceName, String content) {
        if (content == null)
            return null;
        content = content.trim();
        String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                "/" + content + "?v=full";
        return get(requestUri);
    }

    private JSONObject getCustomJsonObject(String resourceName, String param1, String param2, String param3, boolean condition) {
        if (param1 == null || param2 == null)
            return null;
        param1 = param1.trim();
        param2 = param2.trim();
        String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                "?patient=" + param1 + "&v=full&" + param3 + "=" + param2;
        return get(requestUri, condition, resourceName);
    }

    private JSONArray getJSONArray(String resourceName) {
        JSONArray jsonArray = null;
        try {
            String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName + "?v=full";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private JSONArray getJSONArray(String resourceName, String param1, String param2, String searchResource) {
        JSONArray jsonArray = null;
        try {
            String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                    "?patient=" + param1 + "&v=full&" + searchResource + "=" + param2;
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private JSONArray getJSONArray(String resourceName, String searchParam, String content) {
        JSONArray jsonArray = null;
        try {
            String requestUri = "http://" + serverAdress + "/openmrs/ws/rest/v1/" + resourceName +
                    "?" + searchParam + "=" + content + "&v=full";
            JSONObject obj = get(requestUri);
            jsonArray = obj.getJSONArray("results");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONObject getPersonByUuid(String content) {
        return getJsonObjectByUuid(PERSON_RESOURCE, content);
    }

    public JSONObject getPersonByName(String content) {
        return getJsonObjectByName(PERSON_RESOURCE, content);
    }

    public JSONObject getPatientByName(String content) {
        return getJsonObjectByName(PATIENT_RESOURCE, content);
    }

    public JSONObject getPatientByUuid(String content) {
        return getJsonObjectByUuid(PATIENT_RESOURCE, content);
    }

    public JSONObject getProgramByUuid(String content) {
        return getJsonObjectByUuid(PROGRAM_RESOURCE, content);
    }

    public JSONObject getProgramByName(String content) {
        return getJsonObjectByName(PROGRAM_RESOURCE, content);
    }

    public JSONObject getConceptByName(String content) {
        return getJsonObjectByName(CONCEPT_RESOURCE, content);
    }

    public JSONObject getConceptByUuid(String content) {
        return getJsonObjectByUuid(CONCEPT_RESOURCE, content);
    }

    public JSONObject getConceptByConceptId(String content) {
        return getJsonObjectByName(CONCEPT_RESOURCE, content);
    }

    public JSONObject getLocationByName(String content) {
        return getJsonObjectByName(LOCATION_RESOURCE, content);
    }

    public JSONObject getLocationByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_RESOURCE, content);
    }

    public JSONObject getEncounterByUuid(String content) {
        return getJsonObjectByUuid(ENCOUNTER_RESOURCE, content);
    }

    public JSONObject getUserByUuid(String content) {
        return getJsonObjectByUuid(USER_RESOURCE, content);
    }

    public JSONObject getRoleByUuid(String content) {
        return getJsonObjectByUuid(ROLE_RESOURCE, content);
    }

    public JSONObject getPrivilegeByUuid(String content) {
        return getJsonObjectByUuid(PRIVILEGE_RESOURCE, content);
    }

    public JSONObject getIdentifierTypeByUuid(String content) {
        return getJsonObjectByUuid(IDENTIFIER_TYPE_RESOURCE, content);
    }

    public JSONObject getPersonAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(PERSON_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getPersonAttributeTypeByName(String content) {
        return getJsonObjectByName(PERSON_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getEncounterTypeByUuid(String content) {
        return getJsonObjectByUuid(ENCOUNTER_TYPE_RESOURCE, content);
    }

    public JSONObject getEncounterTypeByName(String content) {
        return getJsonObjectByName(ENCOUNTER_TYPE_RESOURCE, content);
    }

    public JSONObject getProviderByIdentifier(String content) {
        return getJsonObjectByName(PROVIDER_RESOURCE, content);
    }

    public JSONObject getProviderByUuid(String content) {
        return getJsonObjectByUuid(PROVIDER_RESOURCE, content);
    }

    public JSONObject getObservationByUuid(String content) {
        return getJsonObjectByUuid(OBSERVATION_RESOURCE, content);
    }

    public JSONObject getProviderAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(PROVIDER_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getProviderAttributeTypeByName(String content) {
        return getJsonObjectByName(PROVIDER_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getLocationAttributeTypeByName(String content) {
        return getJsonObjectByName(LOCATION_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getLocationAttributeTypeByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_ATTRIBUTE_TYPE_RESOURCE, content);
    }

    public JSONObject getLocationTagByName(String content) {
        return getJsonObjectByName(LOCATION_TAG, content);
    }

    public JSONObject getLocationTagByUuid(String content) {
        return getJsonObjectByUuid(LOCATION_TAG, content);
    }

    public JSONObject getEarliestEncounter(String patient, String encounterType) {
        return getCustomJsonObject(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM, early);
    }

    public JSONObject getLatestEncounter(String patient, String encounterType) {
        return getCustomJsonObject(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM, !early);
    }

    public JSONObject getEarliestObservation(String patient, String concept) {
        return getCustomJsonObject(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE, early);
    }

    public JSONObject getLatestObservation(String patient, String concept) {
        return getCustomJsonObject(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE, !early);
    }

    public JSONObject getPatientIdentifierTypeByUuid(String content) {
        return getJsonObjectByUuid(PATIENT_IDENTIFIER_TYPE_RESOURCE, content);
    }

    public JSONArray getAllRoles() {
        return getJSONArray(ROLE_RESOURCE);
    }

    public JSONArray getAllPrivileges() {
        return getJSONArray(PRIVILEGE_RESOURCE);
    }

    public JSONArray getAllPatientIdentifierTypes() {
        return getJSONArray(PATIENT_IDENTIFIER_TYPE_RESOURCE);
    }

    public JSONArray getAllPersonAttributeTypes() {
        return getJSONArray(PERSON_ATTRIBUTE_TYPE_RESOURCE);
    }

    public JSONArray getAllEncounterTypes() {
        return getJSONArray(ENCOUNTER_TYPE_RESOURCE);
    }

    public JSONArray getAllProviderAttributeTypes() {
        return getJSONArray(PROVIDER_ATTRIBUTE_TYPE_RESOURCE);
    }

    public JSONArray getAllLocationAttributeTypes() {
        return getJSONArray(LOCATION_ATTRIBUTE_TYPE_RESOURCE);
    }

    public JSONArray getAllLocationTags() {
        return getJSONArray(LOCATION_TAG);
    }

    public JSONArray getAllEncountersByPatientAndEncounterType(String patient, String encounterType) {
        return getJSONArray(ENCOUNTER_RESOURCE, patient, encounterType, ENCOUNTER_TYPE_PARAM);
    }

    public JSONArray getAllObservationsByPatientAndConcept(String patient, String concept) {
        return getJSONArray(OBSERVATION_RESOURCE, patient, concept, CONCEPT_RESOURCE);
    }

    public JSONArray getAllObservationsByEncounter(String encounter) {
        return getJSONArray(OBSERVATION_RESOURCE, ENCOUNTER_RESOURCE, encounter);
    }
}