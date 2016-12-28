
package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Rabbia on 11/18/2016.
 *//*


*/
/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. *//*


import android.content.Context;

import com.ihsinformatics.gfatmmobile.App;

*/

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.shared.Metadata;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all mobile form requests to the server
 */


public class ServerService {
    private static final String TAG = "ServerService";
    private static String httpsUri;
    private static DatabaseUtil dbUtil;
    private static Context context;

    public ServerService(Context context) {
        this.context = context;
        // Specify REST module link
        httpsUri = App.getIp() + ":" + App.getPort() + "/ws/rest/v1/";
        /*httpClient = new HttpRequest (this.context);
        httpsClient = new HttpsClient (this.context);
        mdUtil = new MetadataUtil (this.context);*/
        dbUtil = new DatabaseUtil(this.context);
    }

    /**
     * Checks to see if the client is connected to any network (GPRS/Wi-Fi)
     *
     * @return status
     */
    public boolean checkInternetConnection() {
        boolean status = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            status = true;
        }
        return status;
    }


    public boolean saveFormLocally(String formName, String pid, HashMap<String, String> formValues) {

        ContentValues values = new ContentValues();

        values.put("program", App.getProgram());
        values.put("form_name", formName);
        values.put("form_date", formValues.get("formDate"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        values.put("timestamp", timestamp.toString());
        values.put("username", App.getUsername());
        values.put("p_id", pid);

        dbUtil.insert(Metadata.FORMS, values);

        String id = dbUtil.getObject(Metadata.FORMS, "id", "program='" + App.getProgram() + "' AND form_name='" + formName + "' AND form_date='" + formValues.get("formDate") + "' AND timestamp='" + timestamp.toString() + "' AND username='" + App.getUsername() + "' AND p_id='" + pid + "'");

        for (Map.Entry<String, String> entry : formValues.entrySet()) {

            ContentValues value = new ContentValues();

            value.put("form_id", id);
            value.put("field_name", entry.getKey());
            value.put("value", entry.getValue());

            dbUtil.insert(Metadata.FORMS, value);

        }

        return false;
    }

    public int getTotalSavedForms() {
        return Integer.parseInt(dbUtil.getObject("select count(*) from " + Metadata.FORMS + " where username='" + App.getUsername() + "'"));
    }

    public String[][] getSavedForms(String username) {
        String[][] forms = dbUtil.getTableData("select id, program, form_name, p_id, form_date, timestamp from " + Metadata.FORMS + " where username='" + username + "'");
        return forms;
    }

}

