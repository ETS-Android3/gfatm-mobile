package com.ihsinformatics.gfatmmobile.model;

/**
 * Created by Rabbia on 1/9/2017.
 */

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.util.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

public class Encounter extends AbstractModel {
    private String encounterType;
    private String encounterDatetime;
    private String encounterLocation;
    private String patientId;
    private ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> obsGroup;

    public Encounter(String uuid, String encounterType, String encounterDatetime, ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> obsGroup, String encounterLocation) {
        super(uuid);
        this.encounterType = encounterType;
        this.encounterDatetime = encounterDatetime;
        this.obsGroup = obsGroup;
        this.encounterLocation = encounterLocation;
    }

    public static Encounter parseJSONObject(JSONObject json) {
        Encounter encounter = null;
        String uuid = "";
        String encounterType = "";
        String encounterDatetime = "";
        String encounterLocation = "";
        ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> obsGroup = new ArrayList<>();
        try {
            uuid = json.getString("uuid");
            String display = json.getString("display");
            encounterDatetime = display.substring(display.length() - 10, display.length());
            encounterType = display.replace(" " + encounterDatetime, "");
            JSONObject locationObject = json.getJSONObject("location");
            encounterLocation = locationObject.getString("display");
            JSONObject patientObject = json.getJSONObject("patient");
            String patientUuid = patientObject.getString("uuid");
            JSONArray obsArray = json.getJSONArray("obs");
            for (int i = 0; i < obsArray.length(); i++) {
                JSONObject jsonObject = obsArray.getJSONObject(i);
                com.ihsinformatics.gfatmmobile.model.Obs obs = com.ihsinformatics.gfatmmobile.model.Obs.parseJSONObject(jsonObject);
                if (obs != null)
                    obsGroup.add(obs);
                else {
                    if (jsonObject.has("groupMembers")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("groupMembers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonA = jsonArray.getJSONObject(j);
                            com.ihsinformatics.gfatmmobile.model.Obs obs1 = com.ihsinformatics.gfatmmobile.model.Obs.parseJSONObject(jsonA);
                            if (obs1 != null)
                                obsGroup.add(obs1);
                        }
                    } else { //post returns doesn't indlude group members :/
                        HttpGet httpGet = new HttpGet(App.getIp(), App.getPort());
                        JSONArray linkJsonArray = jsonObject.getJSONArray("links");
                        JSONObject linkObject = linkJsonArray.getJSONObject(0);
                        String link = linkObject.getString("uri");
                        String[] linkArray = link.split("/");
                        JSONObject observation = httpGet.getObservationByUuid(linkArray[linkArray.length - 1]);
                        JSONArray jsonArray = observation.getJSONArray("groupMembers");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonA = jsonArray.getJSONObject(j);
                            com.ihsinformatics.gfatmmobile.model.Obs obs1 = com.ihsinformatics.gfatmmobile.model.Obs.parseJSONObject(jsonA);
                            if (obs1 != null)
                                obsGroup.add(obs1);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            encounter = null;
        }
        encounter = new Encounter(uuid, encounterType, encounterDatetime, obsGroup, encounterLocation);
        return encounter;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("encounterType", encounterType);
            jsonObject.put("encounterDatetime", encounterDatetime);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getEncounterType() {
        return encounterType;
    }

    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    public String getEncounterLocation() {
        return encounterLocation;
    }

    public void setEncounterLocation(String encounterLocation) {
        this.encounterLocation = encounterLocation;
    }

    public String getEncounterDatetime() {
        return encounterDatetime;
    }

    public void setEncounterDatetime(String encounterDatetime) {
        this.encounterDatetime = encounterDatetime;
    }

    public ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> getObsGroup() {
        return obsGroup;
    }

    public void setObsGroup(ArrayList<com.ihsinformatics.gfatmmobile.model.Obs> obsGroup) {
        this.obsGroup = obsGroup;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + encounterType;
    }
}