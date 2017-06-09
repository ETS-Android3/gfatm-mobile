package com.ihsinformatics.gfatmmobile.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tahira on 6/2/2017.
 */

/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

public class SearchPatient extends AbstractModel {

    private String identifier;
    private String fullName;
    private String gender;
    private String dob;
    private String age;

    public SearchPatient(String uuid, String identifier, String fullName, String gender, String dob, String age) {
        super(uuid);
        this.identifier = identifier;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
    }

    public static SearchPatient parseJSONObject(JSONObject json) {
        SearchPatient patient = null;
        String uuid = "";
        String identifier = "";
        String fullName = "";
        String gender = "";
        String dob = "";
        String age = "";
        try {
            uuid = json.getString("uuid");
            identifier = json.getString("identifier");
            fullName = json.getString("fullName");
            gender = json.getString("gender");
            dob = json.getString("dob");
            age = json.getString("age");

        } catch (JSONException e) {
            e.printStackTrace();
            patient = null;
        }
        patient = new SearchPatient(uuid, identifier, fullName, gender, dob, age);
        return patient;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("identifier", identifier);
            jsonObject.put("fullName", fullName);
            jsonObject.put("gender", fullName);
            jsonObject.put("dob", fullName);
            jsonObject.put("age", fullName);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


}
