package com.ihsinformatics.gfatmmobile.model;

/**
 * Created by Rabbia on 1/9/2017.
 */

import org.json.JSONException;
import org.json.JSONObject;

/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

public class Obs extends AbstractModel {
    private String conceptName;
    private String value;
    private Boolean voided;

    public Obs(String uuid, String conceptName, String value, Boolean voided) {
        super(uuid);
        this.conceptName = conceptName;
        this.value = value;
        this.voided = voided;
    }

    public static Obs parseJSONObject(JSONObject json) {
        Obs obs = null;
        String uuid = "";
        String conceptName = "";
        String value = "";
        Boolean voided = false;
        try {
            uuid = json.getString("uuid");

            String display = json.getString("display");
            String[] arrayString = display.split(": ");
            if(json.has("voided"))
                voided = json.getBoolean("voided");

            if (arrayString.length == 1)
                return null;

            conceptName = arrayString[0];
            value = arrayString[1];

        } catch (JSONException e) {
            e.printStackTrace();
            obs = null;
        }
        obs = new Obs(uuid, conceptName, value, voided);
        return obs;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("conceptName", conceptName);
            jsonObject.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String username) {
        this.value = value;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setBoolean(Boolean voided) {
        this.voided = voided;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + conceptName;
    }
}