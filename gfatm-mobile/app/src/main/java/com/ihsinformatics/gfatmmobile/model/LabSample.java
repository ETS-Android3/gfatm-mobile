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
public class LabSample extends AbstractModel {
    public static final String FIELDS = "uuid";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public LabSample(String uuid, String status) {

        super(uuid);
        this.status = status;
    }

    public static LabSample parseJSONObject(JSONObject json) {
        LabSample labSample = null;
        String uuid = "";
        String status = "";
        try {
            uuid = json.getString("uuid");
            status = json.getString("status");

        } catch (JSONException e) {
            e.printStackTrace();
            labSample = null;
        }
        labSample = new LabSample(uuid, status);
        return labSample;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}