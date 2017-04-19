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

/**
 * @author owais.hussain@irdresearch.org
 */
public class Address extends AbstractModel {
    public static final String FIELDS = "uuid,name";

    String address1;
    String address2;
    String address3;
    String stateProvince;
    String cityVillage;
    String countyDistrict;
    String country;


    public Address(String uuid, String address1, String address2, String address3, String stateProvince, String countyDistrict, String cityVillage, String country) {

        super(uuid);
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.stateProvince = stateProvince;
        this.cityVillage = cityVillage;
        this.countyDistrict = countyDistrict;
        this.country = country;
    }

    public static Address parseJSONObject(JSONObject json) {
        Address person = null;
        String uuid = "";

        String address1 = "";
        String address2 = "";
        String address3 = "";
        String stateProvince = "";
        String cityVillage = "";
        String countyDistrict = "";
        String country = "";

        try {

            uuid = json.getString("uuid");

            if (json.getString("address1") != null)
                address1 = json.getString("address1");
            if (json.getString("address2") != null)
                address2 = json.getString("address2");
            if (json.getString("stateProvince") != null)
                stateProvince = json.getString("stateProvince");
            if (json.getString("cityVillage") != null)
                cityVillage = json.getString("cityVillage");
            if (json.getString("countyDistrict") != null)
                countyDistrict = json.getString("countyDistrict");
            if (json.getString("country") != null)
                country = json.getString("country");
            if (json.getString("address3") != null)
                address3 = json.getString("address3");

        } catch (JSONException e) {
            e.printStackTrace();
            person = null;
        }
        person = new Address(uuid,address1, address2, address3, stateProvince, countyDistrict, cityVillage, country);
        return person;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", address1);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
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

    @Override
    public String toString() {
        return super.toString() + ", " + address1;
    }
}