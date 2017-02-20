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
public class Location extends AbstractModel {
    public static final String FIELDS = "uuid,name,address1,address2,address3,cityVillage,description,attributes";
    private String name;
    private String locationId;
    private String primaryContact;
    private String fastLocation;
    private String pmdtLocation;
    private String petLocation;
    private String comorbiditiesLocation;
    private String childhoodTbLocation;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String description;

    public Location(String uuid, String name, String locationId, String primaryContact, String fastLocation, String pmdtLocation, String comorbiditiesLocation, String petLocation, String childhoodTbLocation, String address1, String address2, String address3, String city, String description) {
        super(uuid);
        this.name = name;
        this.locationId = locationId;
        this.primaryContact = primaryContact;
        this.fastLocation = fastLocation;
        this.pmdtLocation = pmdtLocation;
        this.comorbiditiesLocation = comorbiditiesLocation;
        this.petLocation = petLocation;
        this.childhoodTbLocation = childhoodTbLocation;
        this.address1 = address1;
        this.address1 = address2;
        this.address3 = address3;
        this.city = city;
        this.description = description;
    }

    public static Location parseJSONObject(JSONObject json) {
        Location location = null;
        String uuid = "";
        String name = "";
        String locationId = "";
        String primaryContact = "";
        String fastLocation = "N";
        String pmdtLocation = "N";
        String petLocation = "N";
        String comorbiditiesLocation = "N";
        String childhoodTbLocation = "N";
        String address1 = "";
        String address2 = "";
        String address3 = "";
        String city = "";
        String description = "";
        try {
            uuid = json.getString("uuid");
            name = json.getString("name");
            address1 = json.getString("address1");
            address2 = json.getString("address2");
            address3 = json.getString("address3");
            city = json.getString("cityVillage");
            description = json.getString("description");
            JSONArray attributes = json.getJSONArray("attributes");
            try {
                for (int i = 0; i < attributes.length(); i++) {

                    JSONObject jsonobject = attributes.getJSONObject(i);
                    String display = jsonobject.getString("display");
                    String value = jsonobject.getString("value");

                    if (display.contains("Primary Contact")) {
                        primaryContact = value;
                    } else if (display.contains("Location ID")) {
                        locationId = value;
                    } else if (display.contains("FAST Location") && display.contains("true")) {
                        fastLocation = "Y";
                    } else if (display.contains("PET Location") && display.contains("true")) {
                        petLocation = "Y";
                    } else if (display.contains("Co-Morbidities Location") && display.contains("true")) {
                        comorbiditiesLocation = "Y";
                    } else if (display.contains("PET Location") && display.contains("true")) {
                        petLocation = "Y";
                    } else if (display.contains("Childhood TB Location") && display.contains("true")) {
                        childhoodTbLocation = "Y";
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            location = null;
        }
        location = new Location(uuid, name, locationId, primaryContact, fastLocation, pmdtLocation, comorbiditiesLocation, petLocation, childhoodTbLocation, address1, address2, address3, city, description);
        return location;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getFastLocation() {
        return fastLocation;
    }

    public void setFastLocation(String fastLocation) {
        this.fastLocation = fastLocation;
    }

    public String getPmdtLocation() {
        return pmdtLocation;
    }

    public void setPmdtLocation(String pmdtLocation) {
        this.pmdtLocation = pmdtLocation;
    }

    public String getPetLocation() {
        return petLocation;
    }

    public void setPetLocation(String petLocation) {
        this.petLocation = petLocation;
    }

    public String getComorbiditiesLocation() {
        return comorbiditiesLocation;
    }

    public void setComorbiditiesLocation(String comorbiditiesLocation) {
        this.comorbiditiesLocation = comorbiditiesLocation;
    }

    public String getChildhoodTbLocation() {
        return childhoodTbLocation;
    }

    public void setChildhoodTbLocation(String childhoodTbLocation) {
        this.childhoodTbLocation = childhoodTbLocation;
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + name;
    }
}