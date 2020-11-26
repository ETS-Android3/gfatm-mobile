package com.ihsinformatics.gfatmmobile.commonlab.network;

import android.util.Base64;

import com.ihsinformatics.gfatmmobile.App;

import java.io.UnsupportedEncodingException;

public class Utils {

    public static String getBasicAuth() {
        String auth= null;
        try {
            auth = Base64.encodeToString(
                    (App.getUsername() + ":" + App.getPassword()).getBytes("UTF-8"),
                    Base64.NO_WRAP);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "Basic " + auth;
    }
}
