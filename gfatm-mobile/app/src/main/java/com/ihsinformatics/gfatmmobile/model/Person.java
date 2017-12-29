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

import android.content.Context;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.util.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
    String address1;
    String address2;
    String address3;
    String stateProvince;
    String cityVillage;
    String countyDistrict;
    String country;
    String addressuuid;

    HashMap<String, String> personAttributes;

    public Person(String uuid, String givenName, String familyName, int age, String birthdate, String gender,
                  String address1, String address2, String address3, String stateProvince, String countyDistrict,
                  String cityVillage, String country, String addressUuid, HashMap<String,String> personAttributes ) {

        super(uuid);
        this.givenName = givenName;
        this.familyName = familyName;
        this.age = age;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.stateProvince = stateProvince;
        this.cityVillage = cityVillage;
        this.countyDistrict = countyDistrict;
        this.country = country;
        this.addressuuid = addressUuid;
        this.personAttributes = personAttributes;

    }

    public static Person parseJSONObject(JSONObject json, Context context) {
        Person person = null;
        String uuid = "";
        String givenName = "";
        String familyName = "";
        int age = 0;
        String birthdate = "";
        String gender = "";

        String address1 = "";
        String address2 = "";
        String address3 = "";
        String stateProvince = "";
        String cityVillage = "";
        String countyDistrict = "";
        String country = "";
        String addressUuid = "";
        HashMap<String, String> personAttributes = new HashMap<>();

        try {
            uuid = json.getString("uuid");
            String names = json.getString("display");
            givenName = names;
            familyName = "";
            age = json.getInt("age");
            birthdate = json.getString("birthdate");
            gender = json.getString("gender");

            HttpGet httpGet = new HttpGet(App.getIp(), App.getPort(), context);
            JSONObject addressObject = httpGet.getPersonAddressByPersonUuid(uuid);

            //JSONObject addressObject = json.getJSONObject("preferredAddress");
            if(addressObject != null && !addressObject.getBoolean("voided")) {
                if (!(addressObject.getString("address1") == null || addressObject.getString("address1").equals("null")))
                    address1 = addressObject.getString("address1");
                if (!(addressObject.getString("address2") == null || addressObject.getString("address2").equals("null")))
                    address2 = addressObject.getString("address2");
                if (!(addressObject.getString("stateProvince") == null || addressObject.getString("stateProvince").equals("null")))
                    stateProvince = addressObject.getString("stateProvince");
                if (!(addressObject.getString("cityVillage") != null || addressObject.getString("stateProvince").equals("null")))
                    cityVillage = addressObject.getString("cityVillage");
                if (!(addressObject.getString("countyDistrict") == null || addressObject.getString("countyDistrict").equals("null")))
                    countyDistrict = addressObject.getString("countyDistrict");
                if (!(addressObject.getString("country") == null || addressObject.getString("country").equals("null")))
                    country = addressObject.getString("country");
                if (!(addressObject.getString("address3") == null || addressObject.getString("address3").equals("null")))
                    address3 = addressObject.getString("address3");
                if (!(addressObject.getString("uuid") == null || addressObject.getString("uuid").equals("null")))
                    addressUuid = addressObject.getString("uuid");

            }

            JSONArray attributes = json.getJSONArray("attributes");

            for (int i = 0; i < attributes.length(); i++) {

                JSONObject object = attributes.getJSONObject(i);
                String display = object.getString("display");
                Boolean voided = object.getBoolean("voided");

                if(!voided){

                    String[] displayString = display.split(" = ");
                    if (displayString.length == 2)
                        personAttributes.put(displayString[0],displayString[1]);
                     else {

                        JSONObject attributeTypeObj = object.getJSONObject("attributeType");
                        String attributeType = attributeTypeObj.getString("display");

                        personAttributes.put(attributeType,display);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            person = null;
        }
        person = new Person(uuid, givenName, familyName, age, birthdate, gender,
                address1, address2, address3, stateProvince, countyDistrict,
                cityVillage, country, addressUuid, personAttributes);
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountyDistrict() {
        return countyDistrict;
    }

    public void setCountyDistrict(String countyDistrict) {
        this.countyDistrict = countyDistrict;
    }

    public String getCityVillage() {
        return cityVillage;
    }

    public void setCityVillage(String cityVillage) {
        this.cityVillage = cityVillage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressUuid() {
        return addressuuid;
    }

    public void setAddressUuid(String country) {
        this.addressuuid = addressuuid;
    }

    public String getPersonAttribute(String personAttributeType){

        String personAttribute = personAttributes.get(personAttributeType);
        if(personAttribute ==  null)
            return "";
        else
            return personAttribute;
    }

    public void setPersonAttribute(String personAttributeType, String personAttribute){
        personAttributes.put(personAttributeType, personAttribute);
    }

    public HashMap<String,String> getAllPersonAttributes(){
        return personAttributes;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + givenName;
    }
}