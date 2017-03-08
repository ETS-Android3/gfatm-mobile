package com.ihsinformatics.gfatmmobile.model;

/**
 * Created by Rabbia on 1/9/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

public class User extends AbstractModel {
    private String username;
    private String fullName;
    private String roles;
    private String identifier;

    public User(String uuid, String username, String fullName, String roles, String identifier) {
        super(uuid);
        this.username = username;
        this.fullName = fullName;
        this.roles = roles;
        this.identifier = identifier;
    }

    public static User parseJSONObject(JSONObject json) {
        User user = null;
        String uuid = "";
        String username = "";
        String fullName = "";
        String roles = "";
        String identifier = "";
        try {
            uuid = json.getString("uuid");
            username = json.getString("username");
            identifier = json.getString("systemId");
            JSONObject person = json.getJSONObject("person");
            fullName = person.getString("display");
            JSONArray rolesArray = json.getJSONArray("roles");

            for (int i = 0; i < rolesArray.length(); i++) {

                JSONObject jsonobject = rolesArray.getJSONObject(i);

                if (!jsonobject.get("name").equals("Provider")) {
                    if (!roles.equals(""))
                        roles = roles + " , ";

                    roles = roles + jsonobject.get("name");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            user = null;
        }
        user = new User(uuid, username, fullName, roles, identifier);
        return user;
    }

    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", super.getUuid());
            jsonObject.put("username", username);
            jsonObject.put("fullName", fullName);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String username) {
        this.fullName = fullName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + username;
    }
}