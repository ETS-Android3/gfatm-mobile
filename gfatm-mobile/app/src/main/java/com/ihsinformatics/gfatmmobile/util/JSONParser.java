package com.ihsinformatics.gfatmmobile.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Haris on 01/03/2017.
 */

public class JSONParser {
    private static final String TAG = "JSONParser";

    public static JSONObject getJSONObject(String jsonText) {
        try {
            JSONObject jsonObj = new JSONObject(jsonText);
            return jsonObj;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data. " + e.getMessage());
            return null;
        }
    }

    public static JSONObject[] getJSONArrayFromObject(JSONObject jsonObj, String arrayElement) {
        try {
            JSONArray jsonArray = jsonObj.getJSONArray(arrayElement);
            JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjects[i] = JSONParser.getJSONObject(jsonArray.getString(i));
            }
            return jsonObjects;
        } catch (JSONException e) {
            Log.e(TAG,
                    "Error parsing array from JSON Object using element \'" + arrayElement + "\'. " + e.getMessage());
            return null;
        }
    }
}

