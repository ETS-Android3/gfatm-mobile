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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author owais.hussain@irdresearch.org
 */
public class Person extends AbstractModel {
    public static final String FIELDS = "uuid,name";

    String givenName;
    String familyName;
    int age;
    String birthdate;
    String gender;

    public Person(String uuid, String givenName, String familyName, int age, String birthdate, String gender) {
        super(uuid);
        this.givenName = givenName;
        this.familyName = familyName;
        this.age = age;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public static Person parseJSONObject(JSONObject json) {
        Person person = null;
        String uuid = "";
        String givenName = "";
        String familyName = "";
        int age = 0;
        String birthdate = "";
        String gender = "";
        try {
            uuid = json.getString("uuid");
            String[] names = json.getString("display").split(" ");
            givenName = names[0];
            familyName = names[1];
            age = json.getInt("age");
            birthdate = json.getString("birthdate");
            gender = json.getString("gender");

        } catch (JSONException e) {
            e.printStackTrace();
            person = null;
        }
        person = new Person(uuid, givenName, familyName, age, birthdate, gender);
        return person;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", givenName);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + givenName;
    }
}