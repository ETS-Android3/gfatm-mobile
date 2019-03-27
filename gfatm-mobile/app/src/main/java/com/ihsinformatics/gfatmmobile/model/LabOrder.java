/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
/**
 *
 */

package com.ihsinformatics.gfatmmobile.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author owais.hussain@irdresearch.org
 */
public class LabOrder extends AbstractModel {
    public static final String FIELDS = "uuid,labTestType,labReferenceNumber,labTestSamples,attributes";

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    String testType;

    public String getLabReferenceNumber() {
        return labReferenceNumber;
    }

    public void setLabReferenceNumber(String labReferenceNumber) {
        this.labReferenceNumber = labReferenceNumber;
    }

    String labReferenceNumber;

    public String getEncounterUuid() {
        return encounterUuid;
    }

    public void setEncounterUuid(String encounterUuid) {
        this.encounterUuid = encounterUuid;
    }

    String encounterUuid;

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    String orderUuid;

    public ArrayList<LabSample> getLabSamples() {
        return labSamples;
    }

    public void setLabSamples(ArrayList<LabSample> labSsmples) {
        this.labSamples = labSsmples;
    }

    ArrayList<LabSample> labSamples;

    public ArrayList<LabAttribute> getResult() {
        return result;
    }

    public void setResult(ArrayList<LabAttribute> result) {
        this.result = result;
    }

    ArrayList<LabAttribute> result;

    public LabOrder(String uuid, String testType, String labReferenceNumber, ArrayList<LabSample> labSamples, ArrayList<LabAttribute> result, String encounterUuid, String orderUuid) {
        super(uuid);
        this.testType = testType;
        this.labReferenceNumber = labReferenceNumber;
        this.labSamples = labSamples;
        this.result = result;
        this.encounterUuid = encounterUuid;
        this.orderUuid = orderUuid;
    }

    public static LabOrder parseJSONObject(JSONObject json) {
        LabOrder labOrder = null;
        String uuid = "";
        String testType = "";
        String labReferenceNumber = "";
        String encounterUuid = "";
        String orderUuid = "";
        ArrayList<LabSample> labSamples = new ArrayList<>();
        ArrayList<LabAttribute> result = new ArrayList<>();
        try {
            uuid = json.getString("uuid");

            JSONObject obj1 = json.getJSONObject("labTestType");
            testType = obj1.getString("name");

            labReferenceNumber = json.getString("labReferenceNumber");

            JSONObject obj2 = json.getJSONObject("order");
            JSONObject obj3 = obj2.getJSONObject("encounter");
            encounterUuid = obj3.getString("uuid");
            orderUuid = obj2.getString("uuid");

            if(json.has("labTestSamples")) {
                JSONArray sampleArray = json.getJSONArray("labTestSamples");
                for (int i = 0; i < sampleArray.length(); i++) {
                    JSONObject newObj = sampleArray.getJSONObject(i);
                    LabSample labSample = LabSample.parseJSONObject(newObj);
                    labSamples.add(labSample);
                }
            }

            JSONArray attributeArray = json.getJSONArray("attributes");
            for(int i=0; i <attributeArray.length(); i++){
                JSONObject newObj = attributeArray.getJSONObject(i);
                LabAttribute labAttribute = LabAttribute.parseJSONObject(newObj);
                result.add(labAttribute);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            labOrder = null;
        }
        labOrder = new LabOrder(uuid, testType, labReferenceNumber, labSamples, result, encounterUuid, orderUuid);
        return labOrder;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}